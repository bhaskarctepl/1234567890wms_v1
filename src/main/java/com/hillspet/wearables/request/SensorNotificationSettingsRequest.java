package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to manage sensor charging notification settings", value = "SensorNotificationSettingsRequest")
@JsonInclude(value = Include.NON_NULL)
public class SensorNotificationSettingsRequest {

	@ApiModelProperty(value = "clientId", required = true, example = "3332")
	@JsonProperty("ClientID")
	private Integer clientId;

	@ApiModelProperty(value = "petId", required = true, example = "33")
	@JsonProperty("PetID")
	private Integer petId;

	@ApiModelProperty(value = "notificationType", required = true, example = "weekly")
	@JsonProperty("NotificationType")
	private String notificationType;

	@ApiModelProperty(value = "notificationDay", required = true, example = "monday")
	@JsonProperty("NotificationDay")
	private String notificationDay;

	@ApiModelProperty(value = "opt", required = false, example = "")
	@JsonProperty("Opt")
	private String opt;

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

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotificationDay() {
		return notificationDay;
	}

	public void setNotificationDay(String notificationDay) {
		this.notificationDay = notificationDay;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

}
