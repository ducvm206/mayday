package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateRoleRequest(
		String name,

		@JsonProperty("display_name")
		String displayName,

		String description
) {}
