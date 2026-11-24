package com.ducvm.mayday.service;

import com.ducvm.mayday.dataset.CreatePatientRequest;
import com.ducvm.mayday.dataset.CreatePatientResult;
import com.ducvm.mayday.dataset.PatientSearchItem;
import com.ducvm.mayday.dataset.SearchPatientsResult;
import com.ducvm.mayday.entity.Account;
import com.ducvm.mayday.entity.Patient;
import com.ducvm.mayday.entity.Role;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.enums.Roles;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.repository.AccountRepository;
import com.ducvm.mayday.repository.PatientRepository;
import com.ducvm.mayday.repository.RoleRepository;
import com.ducvm.mayday.specification.PatientSpecification;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class DoctorService {

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	public SearchPatientsResult searchPatients(
			String keyword,
			String gender,
			Integer minYear,
			Integer maxYear,
			int page,
			int limit) {

		if (page < 1 || limit < 1 || limit > 100) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		// Validate gender. Accept only "Nam" or "Nu".
		if (gender != null && !gender.equals("Nam") && !gender.equals("Nu")) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		if ((minYear != null && minYear < 0)
				|| (maxYear != null && maxYear < 0)) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		int min = minYear == null ? 0 : minYear;
		int max = maxYear == null ? 0 : maxYear;

		if (min > 0 && max > 0 && min > max) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		LocalDate startDate = null;
		LocalDate endDate = null;

		if (min > 0 && max > 0 && min == max) {
			// Exact year
			startDate = LocalDate.of(min, 1, 1);
			endDate = startDate.plusYears(1);

		} else if (min > 0 && max > 0) {
			// Year range
			startDate = LocalDate.of(min, 1, 1);
			endDate = LocalDate.of(max + 1, 1, 1);

		} else if (min > 0 || max > 0) {
			// Only one year provided
			int year = min > 0 ? min : max;

			startDate = LocalDate.of(year, 1, 1);
			endDate = startDate.plusYears(1);
		}

		Specification<Patient> specification =
				PatientSpecification.search(
						keyword, gender, startDate, endDate);

		Pageable pageable = PageRequest.of(
				page - 1,
				limit,
				Sort.by(Sort.Direction.DESC, "id")
		);

		Page<Patient> patientPage =
				patientRepository.findAll(specification, pageable);

		List<PatientSearchItem> patients = patientPage.getContent()
				.stream()
				.map(patient -> new PatientSearchItem(
						patient.getId(),
						patient.getFullName(),
						patient.getPhone(),
						patient.getSex(),
						patient.getDateOfBirth() == null
								? null : patient.getDateOfBirth().getYear()
				))
				.toList();

		return new SearchPatientsResult(
				patients,
				patientPage.getTotalElements(),
				page,
				limit
		);
	}

	@Transactional
	public CreatePatientResult createPatient(CreatePatientRequest request) {

		validateRequest(request);

		// Check whether phone number is already used as an account username.
		if (accountRepository.existsByUsername(request.phone())) {
			throw new ApiException(ResponseCode.USER_EXISTED);
		}

		// Find the patient role.
		Role patientRole = roleRepository.findById(
				(long) Roles.PATIENT.getId()
		).orElseThrow(() ->
				new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		// Create Patient.
		Patient patient = new Patient();

		patient.setFullName(request.fullName().trim());
		patient.setPhone(request.phone().trim());
		patient.setSex(request.gender());

		if (request.yearOfBirth() != null) {
			patient.setDateOfBirth(
					LocalDate.of(request.yearOfBirth(), 1, 1)
			);
		}

		patient.setOccupation(request.career());
		patient.setEthnicity(request.ethnic());

		patient.setAddress(request.hamletAddress());
		patient.setWard(request.communeAddress());
		patient.setCity(request.cityAddress());

		patient.setMedicalInsuranceNumber(
				request.healthInsuranceNumber()
		);

		if (request.healthInsuranceValidate() != null
				&& !request.healthInsuranceValidate().isBlank()) {
			try {
				patient.setMedicalInsuranceExpiry(
						LocalDate.parse(request.healthInsuranceValidate())
				);
			} catch (DateTimeParseException e) {
				throw new ApiException(
						ResponseCode.PARAMETER_VALUE_INVALID
				);
			}
		}

		patient.setFamilyNameAndAddress(request.nextOfKinName());
		patient.setFamilyPhoneNumber(request.nextOfKinPhone());

		patient = patientRepository.saveAndFlush(patient);

		// Create Account.
		Account account = new Account();

		account.setPersonId(patient.getId());
		account.setRole(patientRole);
		account.setUsername(patient.getPhone());
		account.setEncodedPassword(
				passwordEncoder.encode(request.password())
		);
		account.setActive(true);

		account = accountRepository.saveAndFlush(account);

		// Reload database-generated created_at.
		entityManager.refresh(account);

		return new CreatePatientResult(
				patient.getId(),
				patient.getFullName(),
				patient.getPhone(),
				Roles.PATIENT.getId(),
				patient.getSex(),
				patient.getDateOfBirth() == null
						? null : patient.getDateOfBirth().getYear(),
				patient.getOccupation(),
				patient.getEthnicity(),
				patient.getAddress(),
				patient.getWard(),
				patient.getCity(),
				patient.getMedicalInsuranceNumber(),
				patient.getMedicalInsuranceExpiry(),
				patient.getFamilyNameAndAddress(),
				patient.getFamilyPhoneNumber(),
				account.getCreatedAt()
		);
	}

	private void validateRequest(CreatePatientRequest request) {

		if (request == null
				|| request.fullName() == null
				|| request.fullName().isBlank()
				|| request.phone() == null
				|| request.phone().isBlank()
				|| request.password() == null
				|| request.password().isBlank()) {
			throw new ApiException(
					ResponseCode.PARAMETER_NOT_ENOUGH
			);
		}

		if (request.fullName().trim().length() > 150
				|| !request.phone().matches("\\d{10}")
				|| (request.gender() != null
				&& !request.gender().equals("Nam")
				&& !request.gender().equals("Nu"))
				|| (request.yearOfBirth() != null
				&& (request.yearOfBirth() < 1
				|| request.yearOfBirth() > Year.now().getValue()))
				|| (request.career() != null
				&& request.career().length() > 40)
				|| (request.ethnic() != null
				&& request.ethnic().length() > 50)
				|| (request.hamletAddress() != null
				&& request.hamletAddress().length() > 255)
				|| (request.communeAddress() != null
				&& request.communeAddress().length() > 100)
				|| (request.cityAddress() != null
				&& request.cityAddress().length() > 100)
				|| (request.healthInsuranceNumber() != null
				&& request.healthInsuranceNumber().length() > 30)
				|| (request.nextOfKinPhone() != null
				&& request.nextOfKinPhone().length() > 10)) {
			throw new ApiException(
					ResponseCode.PARAMETER_VALUE_INVALID
			);
		}
	}
}
