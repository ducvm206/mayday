package com.ducvm.mayday.controller;

import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.exceptions.InvalidFieldException;
import com.ducvm.mayday.exceptions.OtpTooFrequentException;
import com.ducvm.mayday.exceptions.UserExistedException;
import com.ducvm.mayday.service.AuthService;
import com.ducvm.mayday.util.Constant;
import com.ducvm.mayday.validator.ChangePasswordValidator;
import com.ducvm.mayday.validator.LoginValidator;
import com.ducvm.mayday.validator.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	/**
	 * Auth service.
	 */
	@Autowired
	private AuthService authService;
	/**
	 * Patient validator.
	 */
	@Autowired
	private PersonValidator personValidator;
	/**
	 * Login validator.
	 */
	@Autowired
	private LoginValidator loginValidator;
	/**
	 * Change password validator
	 */
	@Autowired
	private ChangePasswordValidator changePasswordValidator;


	/**
	 * Sign up endpoint for a new patient.
	 * @param req
	 * @return
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest req) {

		Map<String, String> errors = personValidator.validate(req);
		if (!errors.isEmpty()) {
			ResponseCode c = ResponseCode.PARAMETER_VALUE_INVALID;
			return respond(HttpStatus.BAD_REQUEST, c, c.getMessageVi(), errors);
		}

		try {
			SignupResult r = authService.signup(req);

			Map<String, Object> data = new LinkedHashMap<>();
			data.put("user_id", r.userId());
			data.put("full_name", r.fullName());
			data.put("phone", r.phone());
			data.put("gender", r.gender());
			data.put("role_id", r.roleId());
			data.put("is_active", r.active() ? 1 : 0);
			data.put("created_at", r.createdAt().toString());   // ISO-8601

			return respond(HttpStatus.CREATED, ResponseCode.SUCCESS, "Created successfully", data);

		} catch (UserExistedException e) {
			ResponseCode c = ResponseCode.USER_EXISTED;   // 9996
			return respond(HttpStatus.CONFLICT, c, c.getMessageVi(), Map.of());
		}
	}

	/**
	 * Login endpoint.
	 * @param req
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest req) {

		Map<String, String> errors = loginValidator.validate(req);
		if (!errors.isEmpty()) {
			ResponseCode c = ResponseCode.PARAMETER_VALUE_INVALID;
			return respond(HttpStatus.BAD_REQUEST, c, c.getMessageVi(), errors);
		}

		try {
			LoginResult r = authService.login(req);

			Map<String, Object> userInfo = new LinkedHashMap<>();
			userInfo.put("user_id", String.valueOf(r.userId()));
			userInfo.put("full_name", r.fullName());
			userInfo.put("phone", r.phone());
			userInfo.put("role_id", String.valueOf(r.roleId()));

			Map<String, Object> data = new LinkedHashMap<>();
			data.put("token", r.token());
			data.put("user_info", userInfo);

			return respond(HttpStatus.OK, ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(), data);

		} catch (UsernameNotFoundException e) {
			ResponseCode c = ResponseCode.OBJECT_NOT_EXISTED;      // 9992
			return respond(HttpStatus.NOT_FOUND, c, c.getMessageVi(), Map.of());

		} catch (AccountStatusException e) {
			ResponseCode c = ResponseCode.NOT_ACCESS;              // 1009
			return respond(HttpStatus.FORBIDDEN, c, c.getMessageVi(), Map.of());

		} catch (AuthenticationException e) {
			ResponseCode c = ResponseCode.USER_NOT_VALIDATED;      // 9995
			return respond(HttpStatus.UNAUTHORIZED, c, c.getMessageVi(), Map.of());
		}
	}

	/**
	 * Logout endpoint.
	 */
	@PostMapping("/logout")
	public ResponseEntity<?> logout(
			@RequestHeader(value = "Authorization", required = false) String authorization) {

		ResponseCode invalid = ResponseCode.TOKEN_INVALID;

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED, invalid, invalid.getMessageVi(), Map.of());
		}

		try {
			authService.logout(authorization.substring(7).trim());
			return respond(HttpStatus.OK, ResponseCode.SUCCESS, "Đăng xuất thành công", Map.of());
		} catch (JwtException e) {
			return respond(HttpStatus.UNAUTHORIZED, invalid, invalid.getMessageVi(), Map.of());
		}
	}

	/**
	 * Endpoint for forgetting password.
	 * @param req
	 * @return
	 */
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest req) {

		String phone = req == null || req.getPhone() == null ? "" : req.getPhone().trim();
		if (!phone.matches(Constant.PHONE_NUMBER_REGEX)) {
			ResponseCode c = ResponseCode.PARAMETER_VALUE_INVALID;
			return respond(HttpStatus.BAD_REQUEST, c, c.getMessageVi(),
					Map.of("phone", "Phone number must contain exactly 10 or 11 digits"));
		}

		try {
			authService.forgotPassword(phone);
			return respond(HttpStatus.OK, ResponseCode.SUCCESS,
					"Đã gửi hướng dẫn khôi phục", Map.of());

		} catch (UsernameNotFoundException e) {
			ResponseCode c = ResponseCode.OBJECT_NOT_EXISTED;          // 9992
			return respond(HttpStatus.NOT_FOUND, c, c.getMessageVi(), Map.of());

		} catch (AccountStatusException e) {
			ResponseCode c = ResponseCode.NOT_ACCESS;                  // 1009
			return respond(HttpStatus.FORBIDDEN, c, c.getMessageVi(), Map.of());

		} catch (OtpTooFrequentException e) {
			ResponseCode c = ResponseCode.ACTION_ALREADY_DONE;         // 1010
			return respond(HttpStatus.TOO_MANY_REQUESTS, c, c.getMessageVi(), Map.of());
		}
	}

	/**
	 * Endpoint for changing password.
	 * @param authorization
	 * @param req
	 * @return
	 */
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody ChangePasswordRequest req) {

		ResponseCode invalid = ResponseCode.TOKEN_INVALID;
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED, invalid, invalid.getMessageVi(), Map.of());
		}

		Map<String, String> errors = changePasswordValidator.validate(req);
		if (!errors.isEmpty()) {
			ResponseCode c = ResponseCode.PARAMETER_VALUE_INVALID;
			return respond(HttpStatus.BAD_REQUEST, c, c.getMessageVi(), errors);
		}

		try {
			authService.changePassword(authorization.substring(7).trim(), req);
			return respond(HttpStatus.OK, ResponseCode.SUCCESS,
					"Đổi mật khẩu thành công", Map.of());

		} catch (InvalidFieldException e) {
			ResponseCode c = ResponseCode.PARAMETER_VALUE_INVALID;       // 1004
			return respond(HttpStatus.BAD_REQUEST, c, c.getMessageVi(),
					Map.of(e.getField(), e.getMessage()));

		} catch (JwtException e) {
			return respond(HttpStatus.UNAUTHORIZED, invalid, invalid.getMessageVi(), Map.of());

		} catch (UsernameNotFoundException e) {
			ResponseCode c = ResponseCode.OBJECT_NOT_EXISTED;            // 9992
			return respond(HttpStatus.NOT_FOUND, c, c.getMessageVi(), Map.of());

		} catch (AccountStatusException e) {
			ResponseCode c = ResponseCode.NOT_ACCESS;                    // 1009
			return respond(HttpStatus.FORBIDDEN, c, c.getMessageVi(), Map.of());
		}
	}

	@PostMapping("/create-staff")
	public ResponseEntity<?> createStaff(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody CreateStaffRequest req
	) {
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED, ResponseCode.TOKEN_INVALID, ResponseCode.TOKEN_INVALID.getMessageVi(), Map.of());
		}

		Map<String, String> errors = personValidator.validate(req);
		if (!errors.isEmpty()) {
			ResponseCode c = ResponseCode.PARAMETER_VALUE_INVALID;
			return respond(HttpStatus.BAD_REQUEST, c, c.getMessageVi(), errors);
		}

		try {
			CreateStaffResult res = authService.createStaff(authorization.substring(7).trim(), req);

			Map<String, Object> data = new LinkedHashMap<>();
			data.put("user_id", res.userId());
			data.put("full_name", res.fullName());
			data.put("role_id", res.roleId());
			data.put("branch_id", res.branchId());
			data.put("department_id", res.departmentId());

			return respond(HttpStatus.CREATED, ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(), data);

		} catch (JwtException | UsernameNotFoundException e) {
			return respond(HttpStatus.UNAUTHORIZED, ResponseCode.TOKEN_INVALID, ResponseCode.TOKEN_INVALID.getMessageVi(), Map.of()); // 9998

		} catch (AccessDeniedException | AccountStatusException e) {
			ResponseCode c = ResponseCode.NOT_ACCESS;                                                      // 1009
			return respond(HttpStatus.FORBIDDEN, c, c.getMessageVi(), Map.of());

		} catch (InvalidFieldException e) {
			ResponseCode c = ResponseCode.PARAMETER_VALUE_INVALID;                                         // 1004
			return respond(HttpStatus.BAD_REQUEST, c, c.getMessageVi(),
					Map.of(e.getField(), e.getMessage()));

		} catch (UserExistedException e) {
			ResponseCode c = ResponseCode.USER_EXISTED;                                                    // 9996
			return respond(HttpStatus.CONFLICT, c, c.getMessageVi(), Map.of());
		}
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
