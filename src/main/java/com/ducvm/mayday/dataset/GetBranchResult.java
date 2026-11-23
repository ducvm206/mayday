package com.ducvm.mayday.dataset;

public record GetBranchResult(
		Long id,
		String name,
		String address,
		String phone
) {}