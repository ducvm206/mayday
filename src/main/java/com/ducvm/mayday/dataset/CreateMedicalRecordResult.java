package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateMedicalRecordResult(
		@JsonProperty("medical_record_id")
		Long medicalRecordId,

		@JsonProperty("patient_id")
		Long patientId,

		@JsonProperty("assigned_doctor_id")
		Long assignedDoctorId,

		@JsonProperty("created_by")
		Long createdBy,

		@JsonProperty("template_id")
		String templateId,

		@JsonProperty("record_type")
		String recordType,

		String status,

		String instructions,

		@JsonProperty("onset_duration_weeks")
		BigDecimal onsetDurationWeeks
) {}
