package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to create pet weight", value = "PetWeightRequest")
@JsonInclude(value = Include.NON_NULL)
public class AddPetWeight {
	
	@ApiModelProperty(value = "petWeightId", required = true, example = "0")
	private Integer petWeightId;
	
	@ApiModelProperty(value = "petId", required = true, example = "1")
	private Integer petId;
	
	@ApiModelProperty(value = "userId", required = true, example = "1")
	private Integer userId;
	
	@ApiModelProperty(value = "weight", required = true, example = "1.0")
	private Double weight;
	
	@ApiModelProperty(value = "weightUnit", required = true, example = "lbs")
	private String weightUnit;
	
	@ApiModelProperty(value = "addDate", required = true, example = "2022-12-21")
	private String addDate;

	public Integer getPetWeightId() {
		return petWeightId;
	}

	public void setPetWeightId(Integer petWeightId) {
		this.petWeightId = petWeightId;
	}

	public String getAddDate() {
		return addDate;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
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

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}
}
