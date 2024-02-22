package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hillspet.wearables.dto.Address;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author vvodyaram
 *
 */
@ApiModel(description = "Contains all information that needed to create a pet parent", value = "PetParentRequest")
@JsonInclude(value = Include.NON_NULL)
public class PetParentInfo {

	@ApiModelProperty(value = "petParentId", required = false, example = "1")
	private Integer petParentId;

	@ApiModelProperty(value = "email", required = true, example = "John@gmail.com")
	private String email;

	@ApiModelProperty(value = "phoneNumber", required = true, example = "+1 (846) 546-5468")
	private String phoneNumber;

	@ApiModelProperty(value = "petParentFirstName", required = true)
	private String petParentFirstName;

	@ApiModelProperty(value = "petParentLastName", required = true)
	private String petParentLastName;

	@ApiModelProperty(value = "secondaryEmailId", required = false)
	private String secondaryEmail;

	@ApiModelProperty(value = "residentialAddress", required = false, example = "Mr John Smith. 132, My Street, Kingston, New York 12401")
	private Address address;

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPetParentFirstName() {
		return petParentFirstName;
	}

	public void setPetParentFirstName(String petParentFirstName) {
		this.petParentFirstName = petParentFirstName;
	}

	public String getPetParentLastName() {
		return petParentLastName;
	}

	public void setPetParentLastName(String petParentLastName) {
		this.petParentLastName = petParentLastName;
	}

	public String getSecondaryEmail() {
		return secondaryEmail;
	}

	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
