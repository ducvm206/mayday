package com.ducvm.mayday.controller;

import com.ducvm.mayday.dataset.GetProfileResult;
import com.ducvm.mayday.dataset.UpdateProfileRequest;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.service.ProfileService;
import com.ducvm.mayday.validator.PersonValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/profile")
public class ProfileController {
	/**
	 * Profile service.
	 */
	@Autowired
	private ProfileService profileService;

	/**
	 * GET profile endpoint.
	 * @param authorization
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> getProfile(
			@RequestHeader(value = "Authorization", required = false) String authorization
	) {
		ResponseCode invalid = ResponseCode.TOKEN_INVALID;
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED, invalid, invalid.getMessageVi(), Map.of());
		}

		try {
			GetProfileResult res = profileService.getProfile(authorization.substring(7).trim());
			Map<String, Object> data = new LinkedHashMap<>();
			data.put("user_id", res.userId());
			data.put("full_name", res.fullName());
			data.put("phone", res.phone());
			data.put("role_id", res.roleId());
			data.put("gender", res.gender());
			data.put("year_of_birth", res.dateOfBirth());
			data.put("career", res.career());
			data.put("ethnic", res.ethnic());
			data.put("hamlet_address", res.address());
			data.put("commune_address", res.ward());
			data.put("city_address", res.city());
			data.put("health_insurance_number", res.healthInsuranceNumber());
			data.put("health_insurance_validate", res.healthInsuranceValidate());
			data.put("next_of_kin_name", res.nextOfKinName());
			data.put("next_of_kin_phone", res.nextOfKinPhone());
			data.put("first_visit", res.firstVisit());
			return respond(HttpStatus.OK, ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessageVi(), data);

		} catch (UsernameNotFoundException | EntityNotFoundException | DisabledException e) {
			return respond(HttpStatus.BAD_REQUEST, ResponseCode.OBJECT_NOT_EXISTED, ResponseCode.OBJECT_NOT_EXISTED.getMessageVi(), Map.of());
		}
	}

	/**
	 * PUT profile endpoint.
	 * @param authorization
	 * @param req
	 * @return
	 */
	@PutMapping
	public ResponseEntity<Map<String, Object>> updateProfile(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody(required = false) UpdateProfileRequest req
	) {
		if (req == null || isEmptyUpdateRequest(req)) {
			return respond(
					HttpStatus.BAD_REQUEST,
					ResponseCode.PARAMETER_NOT_ENOUGH,
					ResponseCode.PARAMETER_NOT_ENOUGH.getMessage(),
					null
			);
		}

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(
					HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessage(),
					null
			);
		}

		try {
			String token = authorization.substring(7);

			GetProfileResult result = profileService.updateProfile(token, req);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessage(),
					result
			);

		} catch (IllegalArgumentException e) {
			return respond(
					HttpStatus.BAD_REQUEST,
					ResponseCode.PARAMETER_VALUE_INVALID,
					ResponseCode.PARAMETER_VALUE_INVALID.getMessage(),
					null
			);
		}
	}

	private boolean isEmptyUpdateRequest(UpdateProfileRequest req) {
		return req.fullName() == null
				&& req.gender() == null
				&& req.dateOfBirth() == null
				&& req.career() == null
				&& req.ethnic() == null
				&& req.hamletAddress() == null
				&& req.communeAddress() == null
				&& req.cityAddress() == null
				&& req.healthInsuranceNumber() == null
				&& req.healthInsuranceValidate() == null
				&& req.nextOfKinPhone() == null;
	}

	private ResponseEntity<Map<String, Object>> respond(
			HttpStatus status, ResponseCode code, String message, Object data) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("code", String.valueOf(code.getCode()));
		body.put("message", message);
		body.put("data", data);
		return ResponseEntity.status(status).body(body);
	}
}
