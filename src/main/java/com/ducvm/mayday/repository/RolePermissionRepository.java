package com.ducvm.mayday.repository;

import com.ducvm.mayday.entity.RolePermission;
import com.ducvm.mayday.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository
		extends JpaRepository<RolePermission, RolePermissionId> {

	List<RolePermission> findByRole_Id(Long roleId);

	Optional<RolePermission> findByRole_IdAndPermission_Id(
			Long roleId, Long permissionId);

	boolean existsByRole_IdAndPermission_Id(Long roleId, Long permissionId);
}
