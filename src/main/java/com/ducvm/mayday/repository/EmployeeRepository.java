package com.ducvm.mayday.repository;

import com.ducvm.mayday.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	boolean existsByPhone(String phone);

}
