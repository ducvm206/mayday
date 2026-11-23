package com.ducvm.mayday.controller;

import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.enums.Roles;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.security.JwtService;
import com.ducvm.mayday.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/api/departments")
public class DepartmentController extends BaseController {

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private JwtService jwtService;

	@PostMapping
	public ResponseEntity<?> createDepartment(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody CreateDepartmentRequest request) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED, ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(), null);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(HttpStatus.FORBIDDEN, ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(), null);
			}

			CreateDepartmentResult result = departmentService.createDepartment(request);

			return respond(HttpStatus.CREATED, ResponseCode.SUCCESS,
					"Tạo chuyên khoa thành công", result);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@GetMapping
	public ResponseEntity<?> getDepartments(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestParam(value = "branch_id", required = false) Long branchId) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED, ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(), null);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(HttpStatus.FORBIDDEN, ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(), null);
			}

			List<GetDepartmentsResult> result =
					departmentService.getDepartments(branchId);

			return respond(HttpStatus.OK, ResponseCode.SUCCESS,
					"Lấy danh sách chuyên khoa thành công", result);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getDepartment(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED, ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(), null);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(HttpStatus.FORBIDDEN, ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(), null);
			}

			GetDepartmentResult result = departmentService.getDepartment(id);

			return respond(HttpStatus.OK, ResponseCode.SUCCESS,
					"Lấy thông tin chuyên khoa thành công", result);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateDepartment(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id,
			@RequestBody UpdateDepartmentRequest request) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED, ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(), null);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(HttpStatus.FORBIDDEN, ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(), null);
			}

			UpdateDepartmentResult result =
					departmentService.updateDepartment(id, request);

			return respond(HttpStatus.OK, ResponseCode.SUCCESS,
					"Cập nhật chuyên khoa thành công", result);

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

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> deleteDepartment(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED, ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(), null);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(HttpStatus.FORBIDDEN, ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(), null);
			}

			departmentService.deleteDepartment(id);

			return respond(HttpStatus.OK, ResponseCode.SUCCESS,
					"Xóa chuyên khoa thành công", Map.of());

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}
}
