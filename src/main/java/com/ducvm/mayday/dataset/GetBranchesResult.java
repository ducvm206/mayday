package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetBranchesResult(
		Long id,
		String name,
		String address,
		String phone,

		@JsonProperty("is_active")
		Boolean isActive
) {}
