package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SearchPatientsResult(
		List<PatientSearchItem> patients,

		@JsonProperty("total_records") long totalRecords,

		int page,

		int limit
) {}
