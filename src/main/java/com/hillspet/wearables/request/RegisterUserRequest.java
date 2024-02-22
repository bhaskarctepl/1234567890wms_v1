package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hillspet.wearables.dto.Address;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to Register a User", value = "RegisterUserRequest")
@JsonInclude(value = Include.NON_NULL)
public class RegisterUserRequest extends ResetPasswordReqeust {

	@ApiModelProperty(value = "secondaryEmail", required = false, example = "sgorle@ctepl.com")
	@JsonProperty("SecondaryEmail")
	private String secondaryEmail;

	@ApiModelProperty(value = "notifyToSecondaryEmail", required = true, example = "true")
	@JsonProperty("NotifyToSecondaryEmail")
	private Boolean notifyToSecondaryEmail;

	@ApiModelProperty(value = "petParentAddress", required = true, example = "4343434")
	@JsonProperty("PetParentAddress")
	private Address petParentAddress;

	public String getSecondaryEmail() {
		return secondaryEmail;
	}

	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}

	public Boolean getNotifyToSecondaryEmail() {
		return notifyToSecondaryEmail;
	}

	public void setNotifyToSecondaryEmail(Boolean notifyToSecondaryEmail) {
		this.notifyToSecondaryEmail = notifyToSecondaryEmail;
	}

	public Address getPetParentAddress() {
		return petParentAddress;
	}

	public void setPetParentAddress(Address petParentAddress) {
		this.petParentAddress = petParentAddress;
	}

}
