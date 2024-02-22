package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to login user", value = "LoginRequest")
@JsonInclude(value = Include.NON_NULL)
public class LoginRequest {

	@ApiModelProperty(value = "email", required = true, example = "sgorle@ctepl.com")
	@JsonProperty("Email")
	private String email;

	@ApiModelProperty(value = "password", required = true, example = "xxxxxx")
	@JsonProperty("Password")
	private String password;

	@ApiModelProperty(value = "fcmToken", required = true, example = "232323dsdsdfsf44545454")
	@JsonProperty("FCMToken")
	private String fcmToken;

	@ApiModelProperty(value = "appVersion", required = false, example = "3.0")
	@JsonProperty("AppVersion")
	private String appVersion;

	@ApiModelProperty(value = "appOS", required = false, example = "1 - iOS / 2 - Android")
	@JsonProperty("AppOS")
	private String appOS;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppOS() {
		return appOS;
	}

	public void setAppOS(String appOS) {
		this.appOS = appOS;
	}

}
