package com.ducvm.mayday.dataset;

import java.time.Instant;

public record SignupResult(
		Long userId,
		String fullName,
		String phone,
		String gender,
		Long roleId,
		boolean active,
		Instant createdAt) {}
