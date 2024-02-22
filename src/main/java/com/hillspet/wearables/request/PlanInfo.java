package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(value = Include.NON_NULL)
public class PlanInfo {

	@ApiModelProperty(value = "planTypeId", required = false, example = "123")
	@JsonProperty("PlanTypeID")
	private String planTypeId;

	@ApiModelProperty(value = "isJoinCompetition", required = false, example = "1")
	@JsonProperty("IsJoinCompetition")
	private String isJoinCompetition;

	public String getPlanTypeId() {
		return planTypeId;
	}

	public void setPlanTypeId(String planTypeId) {
		this.planTypeId = planTypeId;
	}

	public String getIsJoinCompetition() {
		return isJoinCompetition;
	}

	public void setIsJoinCompetition(String isJoinCompetition) {
		this.isJoinCompetition = isJoinCompetition;
	}

}
