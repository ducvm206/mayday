package com.ducvm.mayday.repository;

import com.ducvm.mayday.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

	boolean existsByPhone(String phoneNumber);

	Patient findByPhone(String phoneNumber);
	
}
