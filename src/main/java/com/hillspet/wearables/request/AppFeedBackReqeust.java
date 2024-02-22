package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to send Mobile App Feedback", value = "AppFeedBackReqeust")
@JsonInclude(value = Include.NON_NULL)
public class AppFeedBackReqeust {

	@ApiModelProperty(value = "clientId", required = true, example = "3332")
	@JsonProperty("ClientID")
	private Integer clientId;

	@ApiModelProperty(value = "PetId", required = true, example = "33")
	@JsonProperty("PetId")
	private Integer petId;

	@ApiModelProperty(value = "pageName", required = false, example = "Dashboard")
	@JsonProperty("PageName")
	private String pageName;

	@ApiModelProperty(value = "deviceType", required = false, example = "Sensor")
	@JsonProperty("DeviceType")
	private String deviceType;

	@ApiModelProperty(value = "feedbackText", required = false, example = "Looking good")
	@JsonProperty("FeedbackText")
	private String feedbackText;

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

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

}
