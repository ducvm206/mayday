package com.ducvm.mayday.controller;

import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.enums.Roles;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.security.JwtService;
import com.ducvm.mayday.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/api/v1/medical_records")
public class MedicalRecordController extends BaseController {

	@Autowired
	private MedicalRecordService medicalRecordService;

	@Autowired
	private JwtService jwtService;

	@GetMapping("/all")
	public ResponseEntity<?> getAllMedicalRecords(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestParam(required = false) String status
	) {
		if (authorization == null ||
				!authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(
					jwtService.extractRoleId(token),
					Roles.DOCTOR.getId()
			)) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			List<MedicalRecordListItem> result = medicalRecordService.getAllMedicalRecords(status);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(),
					result
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getMedicalRecordById(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id
	) {
		if (authorization == null ||
				!authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(
					jwtService.extractRoleId(token),
					Roles.DOCTOR.getId()
			)) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			GetMedicalRecordResult result = medicalRecordService.getMedicalRecordById(id);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(),
					result
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@GetMapping("/{id}/summary")
	public ResponseEntity<?> getMedicalRecordSummary(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id
	) {
		if (authorization == null ||
				!authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(
					jwtService.extractRoleId(token),
					Roles.DOCTOR.getId()
			)) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			MedicalRecordSummaryResult result =
					medicalRecordService.getMedicalRecordSummary(id);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(),
					result
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@GetMapping("/patients/{id}/history")
	public ResponseEntity<?> getPatientMedicalRecordHistory(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id
	) {
		if (authorization == null ||
				!authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			List<MedicalRecordListItem> result =
					medicalRecordService.getPatientMedicalRecordHistory(
							id,
							authorization.substring(7)
					);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(),
					result
			);

		} catch (ApiException e) {
			if (e.getResponseCode() == ResponseCode.NOT_ACCESS) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			return handleApiException(e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMedicalRecord(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id
	) {
		if (authorization == null ||
				!authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(
					jwtService.extractRoleId(token),
					(long) Roles.DOCTOR.getId())) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			medicalRecordService.deleteMedicalRecord(id);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(),
					null
			);

		} catch (ApiException e) {
			if (e.getResponseCode() == ResponseCode.NOT_ACCESS) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			return handleApiException(e);
		}
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> createMedicalRecord(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody CreateMedicalRecordRequest request) {
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			String token = authorization.substring(7);

			Long roleId = jwtService.extractRoleId(token);

			if (!Objects.equals(roleId, (long) Roles.DOCTOR.getId())
					&& !Objects.equals(roleId, (long) Roles.PATIENT.getId())) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			String username = jwtService.extractUsername(token);

			CreateMedicalRecordResult result =
					medicalRecordService.createMedicalRecord(
							request,
							username,
							roleId
					);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Tạo hồ sơ bệnh án thành công",
					result
			);

		} catch (ApiException e) {
			if (e.getResponseCode() == ResponseCode.NOT_ACCESS) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			return handleApiException(e);
		}
	}

	@PostMapping("/{id}/accept")
	public ResponseEntity<?> acceptMedicalRecord(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id
	) {
		if (authorization == null ||
				!authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			AcceptMedicalRecordResult result =
					medicalRecordService.acceptMedicalRecord(
							id,
							authorization.substring(7)
					);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Tiếp nhận bệnh án thành công",
					result
			);

		} catch (ApiException e) {
			if (e.getResponseCode() == ResponseCode.NOT_ACCESS) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			return handleApiException(e);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateMedicalRecord(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id,
			@RequestBody UpdateMedicalRecordRequest request) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			UpdateMedicalRecordResult result =
					medicalRecordService.updateMedicalRecord(
							id,
							request,
							authorization.substring(7)
					);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Cập nhật bệnh án thành công",
					result
			);

		} catch (ApiException e) {
			if (e.getResponseCode() == ResponseCode.NOT_ACCESS) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			return handleApiException(e);
		}
	}

	@GetMapping("/{id}/answers")
	public ResponseEntity<Map<String, Object>> getMedicalRecordAnswers(
			@RequestHeader(value = "Authorization", required = false)
			String authorization,
			@PathVariable Long id) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			GetMedicalRecordAnswersResult result =
					medicalRecordService.getMedicalRecordAnswers(
							id,
							authorization.substring(7)
					);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Lấy câu trả lời bệnh án thành công",
					result
			);

		} catch (ApiException e) {
			if (e.getResponseCode() == ResponseCode.NOT_ACCESS) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			return handleApiException(e);
		}
	}

	@PutMapping("/{id}/answers")
	public ResponseEntity<Map<String, Object>> updateMedicalRecordAnswers(
			@RequestHeader(value = "Authorization", required = false)
			String authorization,
			@PathVariable Long id,
			@RequestBody UpdateMedicalRecordAnswersRequest request) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			UpdateMedicalRecordAnswersResult result =
					medicalRecordService.updateMedicalRecordAnswers(
							id,
							request,
							authorization.substring(7)
					);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Cập nhật câu trả lời bệnh án thành công",
					result
			);

		} catch (ApiException e) {
			if (e.getResponseCode() == ResponseCode.NOT_ACCESS) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			return handleApiException(e);
		}
	}

	@GetMapping("/classify-questions")
	public ResponseEntity<Map<String, Object>> getClassificationQuestions(
			@RequestHeader(value = "Authorization", required = false)
			String authorization
	) {
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			String token = authorization.substring(7);
			Long roleId = jwtService.extractRoleId(token);

			if (!Objects.equals(roleId, (long) Roles.DOCTOR.getId())
					&& !Objects.equals(roleId, (long) Roles.PATIENT.getId())) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			List<ClassificationQuestion> questions =
					medicalRecordService.getClassificationQuestions();

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Lấy danh sách câu hỏi phân loại thành công",
					questions
			);

		} catch (ApiException e) {
			if (e.getResponseCode() == ResponseCode.NOT_ACCESS) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			return handleApiException(e);
		}
	}

	@PostMapping("/classify")
	public ResponseEntity<Map<String, Object>> classifyMedicalRecord(
			@RequestHeader("Authorization") String authorization,
			@RequestBody ClassifyMedicalRecordRequest request) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(),
					null
			);
		}

		try {
			String token = authorization.substring(7);
			Long roleId = jwtService.extractRoleId(token);

			if (!Objects.equals(roleId, (long) Roles.DOCTOR.getId())
					&& !Objects.equals(roleId, (long) Roles.PATIENT.getId())) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			ClassifyMedicalRecordResult result =
					medicalRecordService.classifyMedicalRecord(request);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(),
					result
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}
}
