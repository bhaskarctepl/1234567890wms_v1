package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to verify the valide verification code", value = "EmailVerificationCodeReqeust")
@JsonInclude(value = Include.NON_NULL)
public class EmailVerificationCodeReqeust {
	
	@ApiModelProperty(value = "email", required = true, example = "sgorle@ctepl.com")
	@JsonProperty("Email")
	private String email;

	@ApiModelProperty(value = "verificationCode", required = true, example = "4343434")
	@JsonProperty("VerificationCode")
	private String verificationCode;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

}
