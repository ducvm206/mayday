package com.ducvm.mayday.service;

import com.ducvm.mayday.controller.UpdateMedicalRecordAnswersResult;
import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.entity.*;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.enums.Roles;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.repository.*;
import com.ducvm.mayday.security.JwtService;
import com.ducvm.mayday.util.MedicalRecordClassification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
public class MedicalRecordService {

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CustomMedicalRecordRepository customMedicalRecordRepository;

	@Autowired
	private MedicalVisitRepository medicalVisitRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private MedicalRecordAnswerRepository medicalRecordAnswerRepository;

	@Autowired
	private MedicalRecordSubtypeRepository subtypeRepository;

	private static final Set<String> ALLOWED_STATUSES = Set.of(
			"pending",
			"in_progress",
			"completed"
	);

	public List<MedicalRecordListItem> getAllMedicalRecords(String status) {

		List<MedicalRecord> records;

		if (status == null || status.isBlank()) {
			records = medicalRecordRepository.findAllByOrderByIdDesc();
		} else {
			String normalizedStatus = status.trim().toLowerCase();

			if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			records = medicalRecordRepository
					.findAllByStatusOrderByIdDesc(normalizedStatus);
		}

		return records.stream()
				.map(record -> new MedicalRecordListItem(
						record.getId(),
						record.getPatientId(),
						record.getRecordType(),
						record.getStatus(),
						record.getFirstVisitTime()
				))
				.toList();
	}

	public GetMedicalRecordResult getMedicalRecordById(Long id) {

		MedicalRecord record = medicalRecordRepository.findById(id)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		return new GetMedicalRecordResult(
				record.getId(),
				record.getPatientId(),
				record.getRecordType(),
				record.getFirstVisitTime(),
				record.getOutpatientTreatmentStartDate(),
				record.getOutpatientTreatmentEndDate(),
				record.getReferralDiagnosis(),
				record.getInitialClinicDiagnosis(),
				record.getInsuranceTreatmentOutcome(),
				record.getOnsetDurationWeeks(),
				record.getStatus(),
				record.getFamilyHistoryStatus(),
				record.getFamilyHistoryDetails(),
				record.getPreviouslyTreated()
		);
	}

	public MedicalRecordSummaryResult getMedicalRecordSummary(Long id) {

		MedicalRecord record = medicalRecordRepository.findById(id)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		Patient patient = patientRepository.findById(record.getPatientId())
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		return new MedicalRecordSummaryResult(
				record.getId(),
				record.getStatus(),
				patient.getFullName(),
				record.getFirstVisitTime()
		);
	}

	@Transactional(readOnly = true)
	public List<MedicalRecordListItem> getPatientMedicalRecordHistory(
			Long patientId,
			String token
	) {
		Long roleId = jwtService.extractRoleId(token);

		if (Objects.equals(roleId, (long) Roles.PATIENT.getId())) {
			String username = jwtService.extractUsername(token);

			Account account = accountRepository.findByUsername(username)
					.orElseThrow(() ->
							new ApiException(ResponseCode.NOT_ACCESS));

			if (!Objects.equals(account.getPersonId(), patientId)) {
				throw new ApiException(ResponseCode.NOT_ACCESS);
			}
		} else if (!Objects.equals(roleId, (long) Roles.DOCTOR.getId())) {
			throw new ApiException(ResponseCode.NOT_ACCESS);
		}

		if (!patientRepository.existsById(patientId)) {
			throw new ApiException(ResponseCode.OBJECT_NOT_EXISTED);
		}

		return medicalRecordRepository
				.findByPatientIdOrderByIdDesc(patientId)
				.stream()
				.map(record -> new MedicalRecordListItem(
						record.getId(),
						record.getPatientId(),
						record.getRecordType(),
						record.getStatus(),
						record.getFirstVisitTime()
				))
				.toList();
	}

	@Transactional
	public void deleteMedicalRecord(Long id) {
		MedicalRecord record = medicalRecordRepository.findById(id)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		try {
			medicalRecordRepository.delete(record);
			medicalRecordRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new ApiException(ResponseCode.NOT_ACCESS);
		}
	}

	@Transactional
	public CreateMedicalRecordResult createMedicalRecord(
			CreateMedicalRecordRequest request,
			String username,
			Long roleId) {

		if (request == null || request.answers() == null) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		Account account = accountRepository.findByUsername(username)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		Long creatorId = account.getPersonId();
		Long patientId;

		// Determine which patient the record belongs to.
		if (Objects.equals(roleId, (long) Roles.DOCTOR.getId())) {

			if (request.patientId() == null) {
				throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
			}

			patientId = request.patientId();

		} else if (Objects.equals(roleId, (long) Roles.PATIENT.getId())) {

			patientId = account.getPersonId();

			if (request.patientId() != null
					&& !Objects.equals(request.patientId(), patientId)) {
				throw new ApiException(ResponseCode.NOT_ACCESS);
			}

		} else {
			throw new ApiException(ResponseCode.NOT_ACCESS);
		}

		patientRepository.findById(patientId)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		// Extract the symptom duration from the initial answers.
		BigDecimal durationWeeks = null;
		Set<String> receivedQuestionIds = new HashSet<>();

		for (MedicalRecordAnswerRequest answer : request.answers()) {

			if (answer == null || answer.questionId() == null
					|| answer.questionId().isBlank()) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			if (!receivedQuestionIds.add(answer.questionId())) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			if (MedicalRecordClassification.URTICARIA_DURATION_WEEKS
					.equals(answer.questionId())) {

				if (answer.answerValue() == null
						|| !answer.answerValue().isNumber()
						|| answer.questionOptionId() != null
						|| answer.answerText() != null) {
					throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
				}

				durationWeeks = answer.answerValue().decimalValue();

				if (durationWeeks.signum() < 0) {
					throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
				}

			} else {
				// The approved schema has no generic answers table.
				// Do not silently discard answers that cannot be stored.
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}
		}

		// Determine the record type from the supplied template or duration.
		String recordType = null;
		String templateId = request.templateId();

		if (templateId != null && !templateId.isBlank()) {
			recordType =
					MedicalRecordClassification.getRecordTypeFromTemplate(
							templateId
					);
		}

		if (durationWeeks != null) {
			String durationRecordType =
					durationWeeks.compareTo(
							MedicalRecordClassification.CHRONIC_THRESHOLD_WEEKS
					) <= 0
							? MedicalRecordClassification.ACUTE
							: MedicalRecordClassification.CHRONIC;

			if (recordType != null
					&& !recordType.equals(durationRecordType)) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			recordType = durationRecordType;

			if (templateId == null || templateId.isBlank()) {
				templateId = MedicalRecordClassification.ACUTE.equals(recordType)
						? MedicalRecordClassification.ACUTE_TEMPLATE_ID
						: MedicalRecordClassification.CHRONIC_TEMPLATE_ID;
			}
		}

		if (recordType == null) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		// Create the parent medical record.
		MedicalRecord record = new MedicalRecord();
		record.setPatientId(patientId);
		record.setRecordType(recordType);
		record.setStatus("pending");
		record.setOnsetDurationWeeks(durationWeeks);

		record = medicalRecordRepository.saveAndFlush(record);

		// Create the matching subtype row.
		if (MedicalRecordClassification.ACUTE.equals(recordType)) {
			subtypeRepository.createAcuteSubtype(record.getId());
		} else {
			subtypeRepository.createChronicSubtype(record.getId());
		}

		return new CreateMedicalRecordResult(
				record.getId(),
				patientId,
				null,
				creatorId,
				templateId,
				recordType,
				record.getStatus(),
				null,
				record.getOnsetDurationWeeks()
		);
	}

	@Transactional
	public AcceptMedicalRecordResult acceptMedicalRecord(
			Long medicalRecordId,
			String token
	) {
		// 1. Verify doctor role.
		Long roleId = jwtService.extractRoleId(token);

		if (!Objects.equals(roleId, (long) Roles.DOCTOR.getId())) {
			throw new ApiException(ResponseCode.NOT_ACCESS);
		}

		// 2. Get authenticated doctor's ID.
		String username = jwtService.extractUsername(token);

		Account account = accountRepository.findByUsername(username)
				.orElseThrow(() ->
						new ApiException(ResponseCode.NOT_ACCESS));

		Employee doctor = employeeRepository.findById(account.getPersonId())
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		// 3. Find medical record.
		MedicalRecord record = medicalRecordRepository.findById(medicalRecordId)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		// 4. Only pending records can be accepted.
		if (!"pending".equals(record.getStatus())) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		// 5. Determine the next visit number.
		int visitNumber = medicalVisitRepository
				.findTopByMedicalRecord_IdOrderByVisitNumberDesc(medicalRecordId)
				.map(visit -> visit.getVisitNumber() + 1)
				.orElse(1);

		// 6. Create a visit assigned to this doctor.
		MedicalVisit visit = new MedicalVisit();

		visit.setMedicalRecord(record);
		visit.setDoctor(doctor);
		visit.setVisitDate(LocalDate.now());
		visit.setVisitNumber(visitNumber);

		medicalVisitRepository.save(visit);

		// 7. Update medical record status.
		record.setStatus("in_progress");

		medicalRecordRepository.save(record);

		return new AcceptMedicalRecordResult(
				record.getId(),
				doctor.getId(),
				record.getStatus()
		);
	}

	@Transactional
	public UpdateMedicalRecordResult updateMedicalRecord(
			Long medicalRecordId,
			UpdateMedicalRecordRequest request,
			String token) {

		Long roleId = jwtService.extractRoleId(token);

		if (!Objects.equals(roleId, (long) Roles.DOCTOR.getId())) {
			throw new ApiException(ResponseCode.NOT_ACCESS);
		}

		String username = jwtService.extractUsername(token);

		Account account = accountRepository.findByUsername(username)
				.orElseThrow(() ->
						new ApiException(ResponseCode.NOT_ACCESS));

		MedicalRecord record = medicalRecordRepository.findById(medicalRecordId)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		MedicalVisit visit = medicalVisitRepository
				.findTopByMedicalRecord_IdOrderByVisitNumberDesc(medicalRecordId)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		// Only the doctor assigned to the latest visit can finalize the record.
		if (!Objects.equals(
				visit.getDoctor().getId(),
				account.getPersonId())) {
			throw new ApiException(ResponseCode.NOT_ACCESS);
		}

		String status = request.status();

		if (status == null || status.isBlank()) {
			status = "completed";
		}

		if (!status.equals("completed") && !status.equals("cancelled")) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		if (request.instructions() == null || request.instructions().isBlank()) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		if (!"in_progress".equals(record.getStatus())) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		visit.setInstructions(request.instructions().trim());
		record.setStatus(status);

		medicalVisitRepository.save(visit);
		medicalRecordRepository.save(record);

		return new UpdateMedicalRecordResult(
				record.getId(),
				record.getPatientId(),
				visit.getDoctor().getId(),
				record.getStatus(),
				visit.getInstructions()
		);
	}

	@Transactional(readOnly = true)
	public GetMedicalRecordAnswersResult getMedicalRecordAnswers(
			Long medicalRecordId,
			String token) {

		// Verify doctor role
		Long roleId = jwtService.extractRoleId(token);

		if (!Objects.equals(roleId, (long) Roles.DOCTOR.getId())) {
			throw new ApiException(ResponseCode.NOT_ACCESS);
		}

		// Find medical record
		MedicalRecord record = medicalRecordRepository.findById(medicalRecordId)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		List<MedicalRecordAnswerSection> sections = new ArrayList<>();

		// Personal history
		addSection(
				sections,
				"personal_history",
				medicalRecordAnswerRepository.findPersonalHistory(medicalRecordId)
		);

		// Allergy triggers
		addSection(
				sections,
				"allergy_triggers",
				medicalRecordAnswerRepository.findAllergyTriggers(medicalRecordId)
		);

		// Record-type-specific information
		if ("CAP".equals(record.getRecordType())) {

			addSection(
					sections,
					"acute_details",
					medicalRecordAnswerRepository.findAcuteDetails(medicalRecordId)
			);

			addSection(
					sections,
					"infection_status",
					medicalRecordAnswerRepository.findInfectionStatus(medicalRecordId)
			);

		} else if ("MAN".equals(record.getRecordType())) {

			addSection(
					sections,
					"chronic_details",
					medicalRecordAnswerRepository.findChronicDetails(medicalRecordId)
			);

			addSection(
					sections,
					"previous_episodes",
					medicalRecordAnswerRepository.findPreviousEpisodes(medicalRecordId)
			);
		}

		return new GetMedicalRecordAnswersResult(
				record.getId(),
				record.getRecordType(),
				sections
		);
	}

	private void addSection(
			List<MedicalRecordAnswerSection> sections,
			String sectionName,
			List<Map<String, Object>> values) {

		if (!values.isEmpty()) {
			sections.add(new MedicalRecordAnswerSection(
					sectionName,
					values
			));
		}
	}

	@Transactional
	public UpdateMedicalRecordAnswersResult updateMedicalRecordAnswers(
			Long medicalRecordId,
			UpdateMedicalRecordAnswersRequest request,
			String token) {

		if (request.answers() == null || request.answers().isEmpty()) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		MedicalRecord record = medicalRecordRepository.findById(medicalRecordId)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		Long roleId = jwtService.extractRoleId(token);

		String username = jwtService.extractUsername(token);

		Account account = accountRepository.findByUsername(username)
				.orElseThrow(() ->
						new ApiException(ResponseCode.NOT_ACCESS));

		if (Objects.equals(roleId, (long) Roles.PATIENT.getId())) {

			// Patient can only update their own record before examination.
			if (!Objects.equals(account.getPersonId(), record.getPatientId())
					|| !Set.of("draft", "pending").contains(record.getStatus())) {
				throw new ApiException(ResponseCode.NOT_ACCESS);
			}

		} else if (Objects.equals(roleId, (long) Roles.DOCTOR.getId())) {

			// Doctor must be assigned to the latest visit.
			if (!"in_progress".equals(record.getStatus())) {
				throw new ApiException(ResponseCode.NOT_ACCESS);
			}

			MedicalVisit visit = medicalVisitRepository
					.findTopByMedicalRecord_IdOrderByVisitNumberDesc(medicalRecordId)
					.orElseThrow(() ->
							new ApiException(ResponseCode.NOT_ACCESS));

			if (!Objects.equals(
					visit.getDoctor().getId(),
					account.getPersonId())) {
				throw new ApiException(ResponseCode.NOT_ACCESS);
			}

		} else {
			throw new ApiException(ResponseCode.NOT_ACCESS);
		}

		int count = 0;

		for (MedicalRecordSectionUpdate answer : request.answers()) {

			if (answer.section() == null || answer.section().isBlank()
					|| answer.values() == null || answer.values().isEmpty()) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			// Ensure acute/chronic sections match the medical record type.
			if ("acute_details".equals(answer.section())
					&& !"CAP".equals(record.getRecordType())) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			if ("chronic_details".equals(answer.section())
					&& !"MAN".equals(record.getRecordType())) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			if ("infection_status".equals(answer.section())
					&& !"CAP".equals(record.getRecordType())) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			if ("previous_episodes".equals(answer.section())
					&& !"MAN".equals(record.getRecordType())) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			medicalRecordAnswerRepository.upsertSection(
					medicalRecordId,
					answer.section(),
					answer.values()
			);

			count++;
		}

		return new UpdateMedicalRecordAnswersResult(count);
	}

	public List<ClassificationQuestion> getClassificationQuestions() {
		return List.of(
				new ClassificationQuestion(
						MedicalRecordClassification.URTICARIA_DURATION_WEEKS,
						"Bệnh nhân đã bị mày đay trong bao lâu (tính theo tuần)?",
						"NUMBER",
						true,
						List.of()
				),
				new ClassificationQuestion(
						MedicalRecordClassification.PREVIOUSLY_EXAMINED,
						"Bệnh nhân đã từng khám bệnh này chưa?",
						"SINGLE_CHOICE",
						false,
						List.of(
								new ClassificationQuestionOption(
										1L, "Có", true
								),
								new ClassificationQuestionOption(
										2L, "Không", false
								)
						)
				)
		);
	}

	public ClassifyMedicalRecordResult classifyMedicalRecord(
			ClassifyMedicalRecordRequest request) {

		if (request == null || request.answers() == null
				|| request.answers().isEmpty()) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		Map<String, ClassificationAnswerRequest> answerMap =
				new HashMap<>();

		for (ClassificationAnswerRequest answer : request.answers()) {

			if (answer == null || answer.questionId() == null
					|| answer.questionId().isBlank()) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			String questionId = answer.questionId();

			if (!MedicalRecordClassification.QUESTION_IDS.contains(questionId)) {
				throw new ApiException(ResponseCode.OBJECT_NOT_EXISTED);
			}

			if (answerMap.containsKey(questionId)) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			answerMap.put(questionId, answer);
		}

		// Duration is mandatory for classification.
		ClassificationAnswerRequest durationAnswer =
				answerMap.get(
						MedicalRecordClassification.URTICARIA_DURATION_WEEKS
				);

		if (durationAnswer == null || durationAnswer.answerValue() == null) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		if (durationAnswer.answerValue().signum() < 0
				|| durationAnswer.optionValue() != null) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		// Previous examination is optional and does not affect classification.
		ClassificationAnswerRequest previousExamAnswer =
				answerMap.get(
						MedicalRecordClassification.PREVIOUSLY_EXAMINED
				);

		if (previousExamAnswer != null) {
			if (!MedicalRecordClassification.isValidBooleanOption(
					previousExamAnswer.optionValue())
					|| previousExamAnswer.answerValue() != null) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}
		}

		BigDecimal durationWeeks = durationAnswer.answerValue();

		if (durationWeeks.compareTo(
				MedicalRecordClassification.CHRONIC_THRESHOLD_WEEKS
		) <= 0) {
			return new ClassifyMedicalRecordResult(
					MedicalRecordClassification.ACUTE,
					MedicalRecordClassification.ACUTE_TEMPLATE_ID,
					MedicalRecordClassification.ACUTE_TEMPLATE_NAME
			);
		}

		return new ClassifyMedicalRecordResult(
				MedicalRecordClassification.CHRONIC,
				MedicalRecordClassification.CHRONIC_TEMPLATE_ID,
				MedicalRecordClassification.CHRONIC_TEMPLATE_NAME
		);
	}
}
