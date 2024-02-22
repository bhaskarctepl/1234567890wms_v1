package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to get sensor status", value = "SensorStatusRequest")
@JsonInclude(value = Include.NON_NULL)
public class SensorStatusRequest {

	@ApiModelProperty(value = "clientId", required = true, example = "3332")
	@JsonProperty("ClientID")
	private Integer clientId;

	@ApiModelProperty(value = "petId", required = true, example = "33")
	@JsonProperty("PetID")
	private Integer petId;

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

}
