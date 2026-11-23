package com.ducvm.mayday.dataset;

public record LoginResult(

		String token,

		Long userId,

		String fullName,

		String phone,

		Long roleId

) {
}
