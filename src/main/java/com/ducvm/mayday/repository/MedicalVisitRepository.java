package com.ducvm.mayday.repository;

import com.ducvm.mayday.entity.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {

	Optional<MedicalVisit> findTopByMedicalRecord_IdOrderByVisitNumberDesc(
			Long medicalRecordId
	);
}
