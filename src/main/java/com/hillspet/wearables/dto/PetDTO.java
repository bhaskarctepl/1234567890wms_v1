package com.hillspet.wearables.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetDTO {

	private Integer petID;
	private String petName;
	private String photoUrl;
	private String petAge;
	private String petBreed;
	private String birthday;
	private String gender;
	private Boolean isNeutered;
	private String weight;
	private String weightUnit;
	private String speciesId;
	private Integer petStatus;

	private Integer studyId;
	private String studyName;
	private Integer brandId;
	private String brandName;
	private Double foodIntake;
	private Integer feedUnit;

	private boolean isPetWithPetParent;
	private Integer petParentId;
	private String petParentName;
	private String petParentEmail;
	private Address petAddress;
	private Integer goalDurationInMins;

	private Integer petSpecQuesCount;
	private Boolean showFmChart;
	private Boolean showFmGoalSetting;
	private Boolean showSleepChart;

	private List<DeviceDTO> devices;

	private Integer isPetEditable;
	private String petBfiImageSetIds;
	private List<PetBfiInfo> petBfiInfos;
	private String bfiScore;
	private String feedingPreferences;

	public Integer getPetID() {
		return petID;
	}

	public void setPetID(Integer petID) {
		this.petID = petID;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getPetAge() {
		return petAge;
	}

	public void setPetAge(String petAge) {
		this.petAge = petAge;
	}

	public String getPetBreed() {
		return petBreed;
	}

	public void setPetBreed(String petBreed) {
		this.petBreed = petBreed;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getIsNeutered() {
		return isNeutered;
	}

	public void setIsNeutered(Boolean isNeutered) {
		this.isNeutered = isNeutered;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public List<DeviceDTO> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceDTO> devices) {
		this.devices = devices;
	}

	public String getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(String speciesId) {
		this.speciesId = speciesId;
	}

	public Integer getIsPetEditable() {
		return isPetEditable;
	}

	public void setIsPetEditable(Integer isPetEditable) {
		this.isPetEditable = isPetEditable;
	}

	public Integer getPetStatus() {
		return petStatus;
	}

	public void setPetStatus(Integer petStatus) {
		this.petStatus = petStatus;
	}

	public Integer getStudyId() {
		return studyId;
	}

	public void setStudyId(Integer studyId) {
		this.studyId = studyId;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	public boolean getIsPetWithPetParent() {
		return isPetWithPetParent;
	}

	public void setIsPetWithPetParent(boolean isPetWithPetParent) {
		this.isPetWithPetParent = isPetWithPetParent;
	}

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

	public String getPetParentName() {
		return petParentName;
	}

	public void setPetParentName(String petParentName) {
		this.petParentName = petParentName;
	}

	public String getPetParentEmail() {
		return petParentEmail;
	}

	public void setPetParentEmail(String petParentEmail) {
		this.petParentEmail = petParentEmail;
	}

	public Address getPetAddress() {
		return petAddress;
	}

	public void setPetAddress(Address petAddress) {
		this.petAddress = petAddress;
	}

	public Integer getGoalDurationInMins() {
		return goalDurationInMins;
	}

	public void setGoalDurationInMins(Integer goalDurationInMins) {
		this.goalDurationInMins = goalDurationInMins;
	}

	public Integer getPetSpecQuesCount() {
		return petSpecQuesCount;
	}

	public void setPetSpecQuesCount(Integer petSpecQuesCount) {
		this.petSpecQuesCount = petSpecQuesCount;
	}

	public Boolean getShowFmChart() {
		return showFmChart;
	}

	public void setShowFmChart(Boolean showFmChart) {
		this.showFmChart = showFmChart;
	}

	public Boolean getShowFmGoalSetting() {
		return showFmGoalSetting;
	}

	public void setShowFmGoalSetting(Boolean showFmGoalSetting) {
		this.showFmGoalSetting = showFmGoalSetting;
	}

	public Boolean getShowSleepChart() {
		return showSleepChart;
	}

	public void setShowSleepChart(Boolean showSleepChart) {
		this.showSleepChart = showSleepChart;
	}

	public String getPetBfiImageSetIds() {
		return petBfiImageSetIds;
	}

	public void setPetBfiImageSetIds(String petBfiImageSetIds) {
		this.petBfiImageSetIds = petBfiImageSetIds;
	}

	public List<PetBfiInfo> getPetBfiInfos() {
		return petBfiInfos;
	}

	public void setPetBfiInfos(List<PetBfiInfo> petBfiInfos) {
		this.petBfiInfos = petBfiInfos;
	}

	public String getBfiScore() {
		return bfiScore;
	}

	public void setBfiScore(String bfiScore) {
		this.bfiScore = bfiScore;
	}

	public String getFeedingPreferences() {
		return feedingPreferences;
	}

	public void setFeedingPreferences(String feedingPreferences) {
		this.feedingPreferences = feedingPreferences;
	}

}
