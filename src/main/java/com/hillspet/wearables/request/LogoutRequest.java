package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to logout the user", value = "LogoutRequest")
@JsonInclude(value = Include.NON_NULL)
public class LogoutRequest {

	@ApiModelProperty(value = "PetParentId", required = true, example = "3332")
	@JsonProperty("PetParentId")
	private String petParentId;

	public String getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(String petParentId) {
		this.petParentId = petParentId;
	}

}
