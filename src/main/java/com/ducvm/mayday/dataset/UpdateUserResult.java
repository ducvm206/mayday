package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateUserResult(

		@JsonProperty("user_id")
		Long userId,

		@JsonProperty("full_name")
		String fullName,

		String phone,

		String gender,

		@JsonProperty("role_id")
		Long roleId,

		@JsonProperty("is_active")
		Integer isActive,

		@JsonProperty("branch_id")
		Long branchId

) {}