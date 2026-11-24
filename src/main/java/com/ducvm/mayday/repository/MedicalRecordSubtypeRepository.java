package com.ducvm.mayday.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MedicalRecordSubtypeRepository {

	private final JdbcTemplate jdbcTemplate;

	public void createAcuteSubtype(Long medicalRecordId) {
		jdbcTemplate.update("""
                INSERT INTO benhanmaydaycap (ma_ho_so)
                VALUES (?)
                """, medicalRecordId);
	}

	public void createChronicSubtype(Long medicalRecordId) {
		jdbcTemplate.update("""
                INSERT INTO benhanmaydayman (ma_ho_so)
                VALUES (?)
                """, medicalRecordId);
	}
}
