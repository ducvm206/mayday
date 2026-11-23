package com.ducvm.mayday.repository;

import com.ducvm.mayday.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {

	boolean existsByNameAndIdNot(String name, Long id);
	
}
