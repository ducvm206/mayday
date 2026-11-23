package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateUserResult(

		@JsonProperty("user_id")
		Long userId,

		@JsonProperty("full_name")
		String fullName,

		String phone,

		@JsonProperty("role_id")
		Long roleId,

		@JsonProperty("is_active")
		Integer isActive

) {}
