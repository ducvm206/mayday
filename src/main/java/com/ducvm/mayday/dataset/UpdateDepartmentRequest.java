package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateDepartmentRequest(
		String name,

		@JsonProperty("department_type")
		String departmentType,

		String description,

		@JsonProperty("branch_id")
		Long branchId
) {}
