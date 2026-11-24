package com.ducvm.mayday.repository;

import com.ducvm.mayday.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

	List<MedicalRecord> findAllByOrderByIdDesc();

	List<MedicalRecord> findAllByStatusOrderByIdDesc(String status);

	List<MedicalRecord> findByPatientIdOrderByIdDesc(Long patientId);
}
