package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to Validate Duplicate Pet", value = "ValidateDuplicatePetRequest")
@JsonInclude(value = Include.NON_NULL)
public class ValidateDuplicatePetRequest {

	@ApiModelProperty(value = "petName", required = true, example = "Puppy")
	private String petName;

	@ApiModelProperty(value = "breedId", required = true, example = "7143")
	private Integer breedId;

	@ApiModelProperty(value = "gender", required = true, example = "Male")
	private String gender;

	@ApiModelProperty(value = "petParentId", required = true, example = "7143")
	private Integer petParentId;

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public Integer getBreedId() {
		return breedId;
	}

	public void setBreedId(Integer breedId) {
		this.breedId = breedId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

}
