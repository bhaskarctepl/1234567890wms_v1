package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to update pet info", value = "PetUpdateRequest")
@JsonInclude(value = Include.NON_NULL)
public class PetUpdateRequest {

	@ApiModelProperty(value = "petInfo", required = true)
	@JsonProperty("About")
	private PetInfo about;

	@ApiModelProperty(value = "client", required = true)
	@JsonProperty("Client")
	private GetClientInfoRequest client;

	public PetInfo getAbout() {
		return about;
	}

	public void setAbout(PetInfo about) {
		this.about = about;
	}

	public GetClientInfoRequest getClient() {
		return client;
	}

	public void setClient(GetClientInfoRequest client) {
		this.client = client;
	}

}
