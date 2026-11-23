package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record UpdatePermissionResult(
		Long id,
		String name,
		String description,
		@JsonProperty("created_at") Instant createdAt,
		@JsonProperty("updated_at") Instant updatedAt
) {}
