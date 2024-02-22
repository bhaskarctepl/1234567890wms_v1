package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hillspet.wearables.dto.Address;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to update the Client Info", value = "ClientInfoRequest")
@JsonInclude(value = Include.NON_NULL)
public class ClientInfoRequest {

	@ApiModelProperty(value = "ClientID", required = false, example = "3332")
	@JsonProperty("ClientID")
	private Integer clientId;

	@ApiModelProperty(value = "firstName", required = true, example = "Siva")
	@JsonProperty("FirstName")
	private String firstName;

	@ApiModelProperty(value = "lastName", required = true, example = "Bhaskar")
	@JsonProperty("LastName")
	private String lastName;

	@ApiModelProperty(value = "phoneNumber", required = true, example = "+14343434")
	@JsonProperty("PhoneNumber")
	private String phoneNumber;

	@ApiModelProperty(value = "secondaryEmail", required = false, example = "sgorle@ctepl.com")
	@JsonProperty("SecondaryEmail")
	private String secondaryEmail;

	@ApiModelProperty(value = "notifyToSecondaryEmail", required = true, example = "true")
	@JsonProperty("NotifyToSecondaryEmail")
	private Boolean notifyToSecondaryEmail;

	@ApiModelProperty(value = "petParentAddress", required = true, example = "4343434")
	@JsonProperty("PetParentAddress")
	private Address petParentAddress;

	@ApiModelProperty(value = "userId", required = true, example = "4343434")
	@JsonProperty("UserId")
	private Integer userId;

	@ApiModelProperty(value = "preferredWeightUnitId", required = true, example = "2")
	@JsonProperty("PreferredWeightUnitId")
	private Integer preferredWeightUnitId;

	@ApiModelProperty(value = "preferredFoodRecUnitId", required = true, example = "1")
	@JsonProperty("PreferredFoodRecUnitId")
	private Integer preferredFoodRecUnitId;

	@ApiModelProperty(value = "preferredFoodRecTime", required = true, example = "10:30")
	@JsonProperty("PreferredFoodRecTime")
	private String preferredFoodRecTime;

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPreferredWeightUnitId() {
		return preferredWeightUnitId;
	}

	public void setPreferredWeightUnitId(Integer preferredWeightUnitId) {
		this.preferredWeightUnitId = preferredWeightUnitId;
	}

	public Integer getPreferredFoodRecUnitId() {
		return preferredFoodRecUnitId;
	}

	public void setPreferredFoodRecUnitId(Integer preferredFoodRecUnitId) {
		this.preferredFoodRecUnitId = preferredFoodRecUnitId;
	}

	public String getPreferredFoodRecTime() {
		return preferredFoodRecTime;
	}

	public void setPreferredFoodRecTime(String preferredFoodRecTime) {
		this.preferredFoodRecTime = preferredFoodRecTime;
	}

}
