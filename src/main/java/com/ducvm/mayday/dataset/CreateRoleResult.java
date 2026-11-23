package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateRoleResult(
		Long id,
		String name,

		@JsonProperty("display_name")
		String displayName,

		String description
) {}
