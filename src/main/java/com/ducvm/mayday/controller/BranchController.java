package com.ducvm.mayday.controller;

import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.enums.Roles;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.security.JwtService;
import com.ducvm.mayday.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/api/branches")
public class BranchController extends BaseController {

	@Autowired
	private BranchService branchService;

	@Autowired
	private JwtService jwtService;

	@PostMapping
	public ResponseEntity<?> createBranch(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody CreateBranchRequest request) {

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

			CreateBranchResult result = branchService.createBranch(request);

			return respond(
					HttpStatus.CREATED,
					ResponseCode.SUCCESS,
					"Tạo chi nhánh thành công",
					result
			);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@GetMapping
	public ResponseEntity<?> getBranches(
			@RequestHeader(value = "Authorization", required = false)
			String authorization) {

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

			List<GetBranchesResult> result = branchService.getBranches();

			return respond(HttpStatus.OK, ResponseCode.SUCCESS,
					"Lấy danh sách chi nhánh thành công", result);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getBranch(
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

			GetBranchResult result = branchService.getBranch(id);

			return respond(HttpStatus.OK, ResponseCode.SUCCESS,
					"Lấy thông tin chi nhánh thành công", result);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateBranch(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable Long id,
			@RequestBody UpdateBranchRequest request) {

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

			UpdateBranchResult result = branchService.updateBranch(id, request);

			return respond(HttpStatus.OK, ResponseCode.SUCCESS,
					"Cập nhật chi nhánh thành công", result);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> deleteBranch(
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

			branchService.deleteBranch(id);

			return respond(HttpStatus.OK, ResponseCode.SUCCESS,
					"Xóa chi nhánh thành công", Map.of());

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
