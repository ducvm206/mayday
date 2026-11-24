package com.ducvm.mayday.controller;

import com.ducvm.mayday.dataset.CreatePatientRequest;
import com.ducvm.mayday.dataset.CreatePatientResult;
import com.ducvm.mayday.dataset.SearchPatientsResult;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.enums.Roles;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.security.JwtService;
import com.ducvm.mayday.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/api/doctor")
public class DoctorController extends BaseController {

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private JwtService jwtService;

	@GetMapping("/patients/search")
	public ResponseEntity<?> searchPatients(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String gender,
			@RequestParam(name = "min_year", required = false) Integer minYear,
			@RequestParam(name = "max_year", required = false) Integer maxYear,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int limit) {

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

			if (!Objects.equals(
					jwtService.extractRoleId(token),
					Roles.DOCTOR.getId())) {

				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			SearchPatientsResult result = doctorService.searchPatients(
					keyword,
					gender,
					minYear,
					maxYear,
					page,
					limit
			);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Tìm kiếm bệnh nhân thành công",
					result
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@PostMapping("/patients/create")
	public ResponseEntity<?> createPatient(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody CreatePatientRequest request) {

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

			if (!Objects.equals(
					jwtService.extractRoleId(token),
					Roles.DOCTOR.getId())) {

				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			CreatePatientResult result = doctorService.createPatient(request);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Tạo bệnh nhân thành công",
					result
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}
}
