package com.hillspet.wearables.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;

/**
 * @author akumarkhaspa
 *
 */
@ApiModel(description = "Contains all information that needed for Food Intake", value = "FoodIntakeDTO")
@JsonInclude(value = Include.NON_NULL)
public class PetRecommondedDietsDTO {

	private Integer feedingScheduledId;
	private Integer petId;
	private Integer petParentId;
	private LocalDate intakeDate;
	private Double recommendedAmountGrams;
	private Double recommendedAmountCups;
	private String recommendedRoundedGrams;
	private String recommendedRoundedCups;
	private Integer dietId;
	private String dietName;
	private Integer unitId;
	private String unit;
	private Integer isDietSelected;
	private List<FoodIntakeHistoryDTO> foodIntakeHistory;

	public Integer getFeedingScheduledId() {
		return feedingScheduledId;
	}

	public void setFeedingScheduledId(Integer feedingScheduledId) {
		this.feedingScheduledId = feedingScheduledId;
	}

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

	public LocalDate getIntakeDate() {
		return intakeDate;
	}

	public void setIntakeDate(LocalDate intakeDate) {
		this.intakeDate = intakeDate;
	}

	public Double getRecommendedAmountGrams() {
		return recommendedAmountGrams;
	}

	public void setRecommendedAmountGrams(Double recommendedAmountGrams) {
		this.recommendedAmountGrams = recommendedAmountGrams;
	}

	public Double getRecommendedAmountCups() {
		return recommendedAmountCups;
	}

	public void setRecommendedAmountCups(Double recommendedAmountCups) {
		this.recommendedAmountCups = recommendedAmountCups;
	}

	public String getRecommendedRoundedGrams() {
		return recommendedRoundedGrams;
	}

	public void setRecommendedRoundedGrams(String recommendedRoundedGrams) {
		this.recommendedRoundedGrams = recommendedRoundedGrams;
	}

	public String getRecommendedRoundedCups() {
		return recommendedRoundedCups;
	}

	public void setRecommendedRoundedCups(String recommendedRoundedCups) {
		this.recommendedRoundedCups = recommendedRoundedCups;
	}

	public Integer getDietId() {
		return dietId;
	}

	public void setDietId(Integer dietId) {
		this.dietId = dietId;
	}

	public String getDietName() {
		return dietName;
	}

	public void setDietName(String dietName) {
		this.dietName = dietName;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getIsDietSelected() {
		return isDietSelected;
	}

	public void setIsDietSelected(Integer isDietSelected) {
		this.isDietSelected = isDietSelected;
	}

	public List<FoodIntakeHistoryDTO> getFoodIntakeHistory() {
		return foodIntakeHistory;
	}

	public void setFoodIntakeHistory(List<FoodIntakeHistoryDTO> foodIntakeHistory) {
		this.foodIntakeHistory = foodIntakeHistory;
	}

}
