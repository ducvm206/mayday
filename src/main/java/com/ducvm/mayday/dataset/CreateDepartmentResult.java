package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateDepartmentResult(
		Long id,
		String name,

		@JsonProperty("department_type")
		String departmentType,

		String description,

		@JsonProperty("branch_id")
		Long branchId
) {}
