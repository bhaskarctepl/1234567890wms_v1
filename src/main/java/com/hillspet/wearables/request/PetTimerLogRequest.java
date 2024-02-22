package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to manage the pet timer log", value = "PetTimerLogRequest")
@JsonInclude(value = Include.NON_NULL)
public class PetTimerLogRequest {

	@ApiModelProperty(value = "clientId", required = true, example = "3332")
	@JsonProperty("ClientID")
	private Integer clientId;

	@ApiModelProperty(value = "petId", required = true, example = "33")
	@JsonProperty("PetID")
	private Integer petId;

	@ApiModelProperty(value = "category", required = true, example = "Walking")
	@JsonProperty("Category")
	private String category;

	@ApiModelProperty(value = "deviceNumber", required = true, example = "0075E84")
	@JsonProperty("DeviceNumber")
	private String deviceNumber;

	@ApiModelProperty(value = "duration", required = true, example = "0075E84")
	@JsonProperty("Duration")
	private String duration;

	@ApiModelProperty(value = "timerDate", required = true, example = "2023-09-01 11:50:12")
	@JsonProperty("TimerDate")
	private String timerDate;

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTimerDate() {
		return timerDate;
	}

	public void setTimerDate(String timerDate) {
		this.timerDate = timerDate;
	}

}
