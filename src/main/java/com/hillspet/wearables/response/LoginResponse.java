package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse extends BaseResponse {

	@ApiModelProperty(value = "result", required = true, example = "")
	@JsonProperty("result")
	private LoginResult result;

	public LoginResult getResult() {
		return result;
	}

	public void setResult(LoginResult result) {
		this.result = result;
	}

	static class LoginResult {

		@ApiModelProperty(value = "key", required = true, example = "true")
		@JsonProperty("Key")
		private Boolean key;

		@ApiModelProperty(value = "key", required = true, example = "")
		@JsonProperty("UserDetails")
		private UserDetails userDetails;

		public Boolean getKey() {
			return key;
		}

		public void setKey(Boolean key) {
			this.key = key;
		}

		public UserDetails getUserDetails() {
			return userDetails;
		}

		public void setUserDetails(UserDetails userDetails) {
			this.userDetails = userDetails;
		}

	}

	static class UserDetails {

		@ApiModelProperty(value = "email", required = true, example = "sgorle@ctepl.com")
		@JsonProperty("Email")
		private String email;

		@ApiModelProperty(value = "clientID", required = true, example = "12345")
		@JsonProperty("ClientID")
		private Integer clientID;

		@ApiModelProperty(value = "clientID", required = true, example = "946c5d03-c2e1-4a72-8d66-b7dd7640c2f5")
		@JsonProperty("Token")
		private String token;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Integer getClientID() {
			return clientID;
		}

		public void setClientID(Integer clientID) {
			this.clientID = clientID;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

	}
}
