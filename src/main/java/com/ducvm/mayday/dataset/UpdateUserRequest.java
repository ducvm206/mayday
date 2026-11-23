package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateUserRequest(

		@JsonProperty("full_name")
		String fullName,

		String gender,

		@JsonProperty("role_id")
		Long roleId,

		@JsonProperty("is_active")
		Integer isActive,

		@JsonProperty("branch_id")
		Long branchId

) {}
