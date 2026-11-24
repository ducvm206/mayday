package com.ducvm.mayday.dataset;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record MedicalRecordListItem(
		Long id,

		@JsonProperty("patient_id")
		Long patientId,

		@JsonProperty("record_type")
		String recordType,

		String status,

		@JsonProperty("first_visit_time")
		Instant firstVisitTime
) {}

