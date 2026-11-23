package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record UpdateDepartmentResult(
		Long id,
		String name,

		@JsonProperty("department_type")
		String departmentType,

		String description,

		@JsonProperty("branch_id")
		Long branchId,

		@JsonProperty("updated_at")
		Instant updatedAt
) {}
