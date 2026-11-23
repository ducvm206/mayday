package com.ducvm.mayday.repository;

import com.ducvm.mayday.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, Long id);

}
