package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateMedicalRecordResult(
		@JsonProperty("medical_record_id") Long medicalRecordId,
		@JsonProperty("patient_id") Long patientId,
		@JsonProperty("assigned_doctor_id") Long assignedDoctorId,
		String status,
		String instructions
) {}
