package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(value = Include.NON_NULL)
public class ClientInfo {

	@ApiModelProperty(value = "clientId", required = false, example = "123")
	@JsonProperty("ClientID")
	private String clientId;

	@ApiModelProperty(value = "clientFirstName", required = false, example = "Siva")
	@JsonProperty("ClientFirstName")
	private String clientFirstName;

	@ApiModelProperty(value = "clientLastName", required = false, example = "Gorle")
	@JsonProperty("ClientLastName")
	private String clientLastName;

	@ApiModelProperty(value = "clientPhone", required = false, example = "+1234354545")
	@JsonProperty("ClientPhone")
	private String clientPhone;

	@ApiModelProperty(value = "clientEmail", required = false, example = "sgorle@ctepl.com")
	@JsonProperty("ClientEmail")
	private String clientEmail;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientFirstName() {
		return clientFirstName;
	}

	public void setClientFirstName(String clientFirstName) {
		this.clientFirstName = clientFirstName;
	}

	public String getClientLastName() {
		return clientLastName;
	}

	public void setClientLastName(String clientLastName) {
		this.clientLastName = clientLastName;
	}

	public String getClientPhone() {
		return clientPhone;
	}

	public void setClientPhone(String clientPhone) {
		this.clientPhone = clientPhone;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}

}
