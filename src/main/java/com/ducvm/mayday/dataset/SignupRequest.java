package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest implements PersonRequest {

	@JsonProperty("full_name")
	private String fullName;

	private String phone;

	private String password;

	private String gender;

}
