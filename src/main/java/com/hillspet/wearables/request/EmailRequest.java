package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to validate email", value = "ValidateEmailRequest")
@JsonInclude(value = Include.NON_NULL)
public class EmailRequest {

	@ApiModelProperty(value = "email", required = true, example = "sgorle@ctepl.com")
	@JsonProperty("Email")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
