package com.ducvm.mayday.repository;

import com.ducvm.mayday.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

	boolean existsByPhone(String phoneNumber);

	Patient findByPhone(String phoneNumber);
	
}
