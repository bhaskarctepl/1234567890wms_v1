package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to validate registred user email", value = "ValidateUserEmailReqeust")
@JsonInclude(value = Include.NON_NULL)
public class ValidateUserEmailReqeust {

	@ApiModelProperty(value = "email", required = true, example = "sgorle@ctepl.com")
	@JsonProperty("Email")
	private String email;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}
