package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to update forward motion goal settings", value = "FMGoalSettingRequest")
@JsonInclude(value = Include.NON_NULL)
public class FMGoalSettingRequest {

	@ApiModelProperty(value = "petParentId", required = true, example = "3017")
	private Integer petParentId;

	@ApiModelProperty(value = "goalDurationInMins", required = true, example = "10")
	private Integer goalDurationInMins;

	@ApiModelProperty(value = "petId", required = true, example = "1")
	private Integer petId;

	@ApiModelProperty(value = "userId", required = true, example = "1")
	private Integer userId;

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

	public Integer getGoalDurationInMins() {
		return goalDurationInMins;
	}

	public void setGoalDurationInMins(Integer goalDurationInMins) {
		this.goalDurationInMins = goalDurationInMins;
	}

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
