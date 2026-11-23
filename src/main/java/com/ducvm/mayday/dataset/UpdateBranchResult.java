package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record UpdateBranchResult(
		Long id,
		String name,
		String address,
		String phone,
		@JsonProperty("updated_at") Instant updatedAt
) {}
