package com.ducvm.mayday.controller;

import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.enums.Roles;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.security.JwtService;
import com.ducvm.mayday.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/api")
public class PermissionController extends BaseController {

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/v1/permissions")
	public ResponseEntity<Map<String, Object>> createPermission(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody CreatePermissionRequest request) {

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

			CreatePermissionResult result = permissionService.createPermission(request);

			return respond(
					HttpStatus.CREATED,
					ResponseCode.SUCCESS,
					"Tạo quyền hạn thành công",
					result
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@GetMapping("/v1/permissions")
	public ResponseEntity<Map<String, Object>> getPermissions(
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

			List<GetPermissionsResult> result = permissionService.getPermissions();

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

	@PutMapping("/v1/permissions/{id}")
	public ResponseEntity<Map<String, Object>> updatePermission(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id,
			@RequestBody UpdatePermissionRequest request) {

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

			UpdatePermissionResult result =
					permissionService.updatePermission(id, request);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Cập nhật quyền hạn thành công",
					result
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@DeleteMapping("/v1/permissions/{id}")
	public ResponseEntity<Map<String, Object>> deletePermission(
			@RequestHeader(value = "Authorization", required = false) String authorization,
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
			String token = authorization.substring(7);

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(
						HttpStatus.FORBIDDEN,
						ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(),
						null
				);
			}

			permissionService.deletePermission(id);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Xóa thành công",
					Map.of()
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@PostMapping("/role_permissions")
	public ResponseEntity<Map<String, Object>> assignPermissions(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody AssignPermissionsRequest request) {

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

			permissionService.assignPermissions(request);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Gán quyền thành công",
					Map.of()
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@GetMapping("/role_permissions/role/{role_id}")
	public ResponseEntity<Map<String, Object>> getRolePermissions(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable("role_id") Long roleId) {

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

			List<GetRolePermissionsResult> result =
					permissionService.getRolePermissions(roleId);

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

	@DeleteMapping("/role_permissions/{role_id}/{permission_id}")
	public ResponseEntity<Map<String, Object>> revokePermission(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable("role_id") Long roleId,
			@PathVariable("permission_id") Long permissionId) {

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

			permissionService.revokePermission(roleId, permissionId);

			return respond(
					HttpStatus.OK,
					ResponseCode.SUCCESS,
					"Thu hồi quyền thành công",
					Map.of()
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
