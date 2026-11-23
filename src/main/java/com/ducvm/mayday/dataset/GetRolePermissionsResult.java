package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record GetRolePermissionsResult(
		@JsonProperty("permission_id") Long permissionId,
		String name,
		String description,
		@JsonProperty("assigned_at") Instant assignedAt
) {}
