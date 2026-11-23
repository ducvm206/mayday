package com.ducvm.mayday.controller;

import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.enums.Roles;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.security.JwtService;
import com.ducvm.mayday.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/api/v1/admin")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	@GetMapping("/users")
	public ResponseEntity<?> getUsers(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@ModelAttribute GetUsersRequest request) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(), null);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(), null);
			}

			List<GetUsersResult> users = userService.getUsers(request);
			long total = userService.countUsers(request);

			Map<String, Object> data = new LinkedHashMap<>();
			data.put("users", users);
			data.put("total", total);

			return respond(HttpStatus.OK,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(), data);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<?> getPatientUser(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id
	) {
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED, ResponseCode.TOKEN_INVALID, ResponseCode.TOKEN_INVALID.getMessageVi(), null);
		}

		try {
			String token = authorization.substring(7);

			// Verify Admin role
			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(HttpStatus.FORBIDDEN, ResponseCode.NOT_ACCESS, ResponseCode.NOT_ACCESS.getMessageVi(), null);
			}

			GetPatientResult result = userService.getPatientUser(id);

			return respond(HttpStatus.OK, ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessageVi(), result);

		} catch (EntityNotFoundException e) {
			return respond(HttpStatus.NOT_FOUND, ResponseCode.OBJECT_NOT_EXISTED, ResponseCode.OBJECT_NOT_EXISTED.getMessageVi(), null);
		}
	}

	@PostMapping("/users")
	public ResponseEntity<Map<String, Object>> createUser(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody CreateUserRequest request) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(), null);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(), null);
			}

			CreateUserResult result = userService.createUser(request);

			return respond(HttpStatus.CREATED,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(), result);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<Map<String, Object>> updateUser(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id,
			@RequestBody UpdateUserRequest request) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(), null);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(), null);
			}

			UpdateUserResult result = userService.updateUser(id, request);

			return respond(HttpStatus.OK,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(), result);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED,
					ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(), null);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(), null);
			}

			userService.deleteUser(id);

			return respond(HttpStatus.OK,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(), null);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@PutMapping("/users/{id}/assign")
	public ResponseEntity<?> assignEmployee(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id,
			@RequestBody AssignEmployeeRequest request) {

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

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			AssignEmployeeResult result = userService.assignEmployee(id, request);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Phân công thành công",
					result
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@PostMapping("/roles")
	public ResponseEntity<?> createRole(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody CreateRoleRequest request) {

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

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			CreateRoleResult result = userService.createRole(request);

			return respond(
					HttpStatus.CREATED,
					ResponseCode.SUCCESS,
					ResponseCode.SUCCESS.getMessageVi(),
					result
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@GetMapping("/roles")
	public ResponseEntity<?> getRoles(
			@RequestHeader(value = "Authorization", required = false) String authorization) {

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

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			List<GetRolesResult> result = userService.getRoles();

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

	public ResponseEntity<Map<String, Object>> handleApiException(ApiException e) {
		ResponseCode code = e.getResponseCode();

		HttpStatus status = switch (code) {
			case USER_EXISTED -> HttpStatus.CONFLICT;
			case OBJECT_NOT_EXISTED -> HttpStatus.NOT_FOUND;
			case PARAMETER_NOT_ENOUGH,
			     PARAMETER_TYPE_INVALID,
			     PARAMETER_VALUE_INVALID -> HttpStatus.BAD_REQUEST;
			default -> HttpStatus.BAD_REQUEST;
		};

		return respond(status, code, code.getMessageVi(), null);
	}
}
