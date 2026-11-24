package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AcceptMedicalRecordResult(
		@JsonProperty("medical_record_id")
		Long medicalRecordId,

		@JsonProperty("assigned_doctor_id")
		Long assignedDoctorId,

		String status
) {}
