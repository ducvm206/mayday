package com.ducvm.mayday.service;

import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.entity.Permission;
import com.ducvm.mayday.entity.Role;
import com.ducvm.mayday.entity.RolePermission;
import com.ducvm.mayday.entity.RolePermissionId;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.repository.PermissionRepository;
import com.ducvm.mayday.repository.RolePermissionRepository;
import com.ducvm.mayday.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private RolePermissionRepository rolePermissionRepository;

	@Transactional
	public CreatePermissionResult createPermission(CreatePermissionRequest request) {

		if (request.name() == null || request.name().isBlank()) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		String name = request.name().trim();

		if (name.length() > 100) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		if (permissionRepository.existsByName(name)) {
			throw new ApiException(ResponseCode.USER_EXISTED);
		}

		Permission permission = new Permission();
		permission.setName(name);
		permission.setDescription(request.description());

		permission = permissionRepository.save(permission);

		return new CreatePermissionResult(
				permission.getId(),
				permission.getName(),
				permission.getDescription()
		);
	}

	@Transactional(readOnly = true)
	public List<GetPermissionsResult> getPermissions() {
		return permissionRepository.findAll()
				.stream()
				.map(permission -> new GetPermissionsResult(
						permission.getId(),
						permission.getName(),
						permission.getDescription(),
						permission.getCreatedAt(),
						permission.getUpdatedAt()
				))
				.toList();
	}

	@Transactional
	public UpdatePermissionResult updatePermission(
			Long id,
			UpdatePermissionRequest request) {

		if (request.name() == null && request.description() == null) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		if (request.name() != null) {
			if (request.name().isBlank()) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			String name = request.name().trim();

			if (name.length() > 100) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			if (permissionRepository.existsByNameAndIdNot(name, id)) {
				throw new ApiException(ResponseCode.USER_EXISTED);
			}

			permission.setName(name);
		}

		if (request.description() != null) {
			permission.setDescription(request.description());
		}

		permission = permissionRepository.save(permission);

		return new UpdatePermissionResult(
				permission.getId(),
				permission.getName(),
				permission.getDescription(),
				permission.getCreatedAt(),
				permission.getUpdatedAt()
		);
	}

	@Transactional
	public void deletePermission(Long id) {
		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		permission.getRoles().clear();
		permissionRepository.delete(permission);
	}

	@Transactional
	public void assignPermissions(AssignPermissionsRequest request) {

		if (request.roleId() == null
				|| request.permissions() == null
				|| request.permissions().isEmpty()) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		Role role = roleRepository.findById(request.roleId())
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		List<Long> permissionIds = request.permissions().stream()
				.distinct()
				.toList();

		List<Permission> permissions =
				permissionRepository.findAllById(permissionIds);

		if (permissions.size() != permissionIds.size()) {
			throw new ApiException(ResponseCode.OBJECT_NOT_EXISTED);
		}

		Set<Long> existingPermissionIds = role.getPermissions().stream()
				.map(rp -> rp.getPermission().getId())
				.collect(Collectors.toSet());

		for (Permission permission : permissions) {
			if (existingPermissionIds.contains(permission.getId())) {
				continue;
			}

			RolePermission rolePermission = new RolePermission();

			RolePermissionId id = new RolePermissionId();
			id.setRoleId(role.getId());
			id.setPermissionId(permission.getId());

			rolePermission.setId(id);
			rolePermission.setRole(role);
			rolePermission.setPermission(permission);

			role.getPermissions().add(rolePermission);
		}

		roleRepository.save(role);
	}

	@Transactional(readOnly = true)
	public List<GetRolePermissionsResult> getRolePermissions(Long roleId) {

		if (!roleRepository.existsById(roleId)) {
			throw new ApiException(ResponseCode.OBJECT_NOT_EXISTED);
		}

		return rolePermissionRepository.findByRole_Id(roleId)
				.stream()
				.map(rp -> new GetRolePermissionsResult(
						rp.getPermission().getId(),
						rp.getPermission().getName(),
						rp.getPermission().getDescription(),
						rp.getAssignedAt()
				))
				.toList();
	}

	@Transactional
	public void revokePermission(Long roleId, Long permissionId) {

		RolePermissionId id = new RolePermissionId();
		id.setRoleId(roleId);
		id.setPermissionId(permissionId);

		RolePermission rolePermission = rolePermissionRepository.findById(id)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		rolePermissionRepository.delete(rolePermission);
	}
}
