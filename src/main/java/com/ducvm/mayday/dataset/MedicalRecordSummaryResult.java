package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record MedicalRecordSummaryResult(
		Long id,

		String status,

		@JsonProperty("patient_name")
		String patientName,

		@JsonProperty("first_visit_time")
		Instant firstVisitTime
) {}
