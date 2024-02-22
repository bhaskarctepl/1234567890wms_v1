package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to validate device number", value = "ValidateDeviceRequest")
@JsonInclude(value = Include.NON_NULL)
public class ValidateDeviceRequest {

	@ApiModelProperty(value = "clientId", required = true, example = "3332")
	@JsonProperty("ClientID")
	private String clientId;

	@ApiModelProperty(value = "sensorNumber", required = true, example = "0011E87")
	@JsonProperty("SensorNumber")
	private String sensorNumber;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSensorNumber() {
		return sensorNumber;
	}

	public void setSensorNumber(String sensorNumber) {
		this.sensorNumber = sensorNumber;
	}

}
