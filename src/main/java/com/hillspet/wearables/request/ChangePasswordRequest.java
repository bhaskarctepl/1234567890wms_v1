package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to change the user password", value = "ChangePasswordRequest")
@JsonInclude(value = Include.NON_NULL)
public class ChangePasswordRequest {

	@ApiModelProperty(value = "ClientID", required = true, example = "3332")
	@JsonProperty("ClientID")
	private Integer clientId;

	@ApiModelProperty(value = "NewPassword", required = true, example = "Afss21a@3332")
	@JsonProperty("NewPassword")
	private String newPassword;

	@ApiModelProperty(value = "password", required = true, example = "Afssa@3332")
	@JsonProperty("Password")
	private String password;

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
