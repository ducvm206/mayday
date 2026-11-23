package com.ducvm.mayday.dataset;

public record CreateBranchRequest(
		String name,
		String address,
		String phone
) {}