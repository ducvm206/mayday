package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetDepartmentsResult(
		Long id,
		String name,

		@JsonProperty("branch_id")
		Long branchId,

		String description
) {}
