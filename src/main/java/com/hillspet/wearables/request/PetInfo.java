package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hillspet.wearables.dto.Address;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(value = Include.NON_NULL)
public class PetInfo {

	@ApiModelProperty(value = "petAddress", required = true, example = "")
	@JsonProperty("PetAddress")
	private Address petAddress;

	@ApiModelProperty(value = "isPetWithPetParent", required = true, example = "1")
	@JsonProperty("IsPetWithPetParent")
	private Integer isPetWithPetParent;

	@ApiModelProperty(value = "petId", required = true, example = "123")
	@JsonProperty("PetID")
	private String petId;

	@ApiModelProperty(value = "petName", required = true, example = "Lilly")
	@JsonProperty("PetName")
	private String petName;

	@ApiModelProperty(value = "petGender", required = true, example = "male")
	@JsonProperty("PetGender")
	private String petGender;

	@ApiModelProperty(value = "isUnknown", required = true, example = "")
	@JsonProperty("IsUnknown")
	private String isUnknown;

	@ApiModelProperty(value = "petBirthday", required = true, example = "")
	@JsonProperty("PetBirthday")
	private String petBirthday;

	@ApiModelProperty(value = "petBreedId", required = true, example = "")
	@JsonProperty("PetBreedID")
	private String petBreedId;

	@ApiModelProperty(value = "isMixed", required = true, example = "")
	@JsonProperty("IsMixed")
	private String isMixed;

	@ApiModelProperty(value = "petMixBreed", required = true, example = "")
	@JsonProperty("PetMixBreed")
	private String petMixBreed;

	@ApiModelProperty(value = "petWeight", required = true, example = "")
	@JsonProperty("PetWeight")
	private String petWeight;

	@ApiModelProperty(value = "weightUnit", required = true, example = "")
	@JsonProperty("WeightUnit")
	private String weightUnit;

	@ApiModelProperty(value = "petBFI", required = true, example = "")
	@JsonProperty("PetBFI")
	private String petBFI;

	@ApiModelProperty(value = "isNeutered", required = true, example = "")
	@JsonProperty("IsNeutered")
	private String isNeutered;

	@ApiModelProperty(value = "brandId", required = true, example = "")
	private Integer brandId;

	@ApiModelProperty(value = "foodIntake", required = true, example = "")
	private Double foodIntake;

	@ApiModelProperty(value = "feedUnit", required = true, example = "")
	private Integer feedUnit;

	public Address getPetAddress() {
		return petAddress;
	}

	public void setPetAddress(Address petAddress) {
		this.petAddress = petAddress;
	}

	public Integer getIsPetWithPetParent() {
		return isPetWithPetParent;
	}

	public void setIsPetWithPetParent(Integer isPetWithPetParent) {
		this.isPetWithPetParent = isPetWithPetParent;
	}

	public String getPetId() {
		return petId;
	}

	public void setPetId(String petId) {
		this.petId = petId;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPetGender() {
		return petGender;
	}

	public void setPetGender(String petGender) {
		this.petGender = petGender;
	}

	public String getIsUnknown() {
		return isUnknown;
	}

	public void setIsUnknown(String isUnknown) {
		this.isUnknown = isUnknown;
	}

	public String getPetBirthday() {
		return petBirthday;
	}

	public void setPetBirthday(String petBirthday) {
		this.petBirthday = petBirthday;
	}

	public String getPetBreedId() {
		return petBreedId;
	}

	public void setPetBreedId(String petBreedId) {
		this.petBreedId = petBreedId;
	}

	public String getIsMixed() {
		return isMixed;
	}

	public void setIsMixed(String isMixed) {
		this.isMixed = isMixed;
	}

	public String getPetMixBreed() {
		return petMixBreed;
	}

	public void setPetMixBreed(String petMixBreed) {
		this.petMixBreed = petMixBreed;
	}

	public String getPetWeight() {
		return petWeight;
	}

	public void setPetWeight(String petWeight) {
		this.petWeight = petWeight;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public String getPetBFI() {
		return petBFI;
	}

	public void setPetBFI(String petBFI) {
		this.petBFI = petBFI;
	}

	public String getIsNeutered() {
		return isNeutered;
	}

	public void setIsNeutered(String isNeutered) {
		this.isNeutered = isNeutered;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Double getFoodIntake() {
		return foodIntake;
	}

	public void setFoodIntake(Double foodIntake) {
		this.foodIntake = foodIntake;
	}

	public Integer getFeedUnit() {
		return feedUnit;
	}

	public void setFeedUnit(Integer feedUnit) {
		this.feedUnit = feedUnit;
	}

}
