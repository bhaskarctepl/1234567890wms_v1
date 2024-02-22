package com.hillspet.wearables.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;

/**
 * @author akumarkhaspa
 *
 */
@ApiModel(description = "Contains all information that needed for Food Intake History", value = "FoodIntakeHistoryDTO")
@JsonInclude(value = Include.NON_NULL)
public class FoodIntakeHistoryDTO {

	private Integer foodHistoryId;
	private Integer intakeId;
	private Integer feedingScheduledId;
	private Integer dietId;
	private String dietName;
	private Integer otherFoodtypeId;
	private String otherFoodTypeName;
	private String foodName;
	private String quantityRecommendedRounded;
	private Double quantityRecommended;
	private Double quantityOffered;
	private Double quantityConsumed;
	private String quantityRecommendedRoundedCups;
	private Double quantityRecommendedCups;
	private Double quantityOfferedCups;
	private Double quantityConsumedCups;
	private Integer quantityUnitId;
	private String quantityUnitName;
	private Double percentConsumed;
	private Double calDensity;
	private Integer calDensityUnitId;
	private Integer calDensityUnitName;
	private Integer userId;
	private Integer isDeleted;

	public Integer getFoodHistoryId() {
		return foodHistoryId;
	}

	public void setFoodHistoryId(Integer foodHistoryId) {
		this.foodHistoryId = foodHistoryId;
	}

	public Integer getIntakeId() {
		return intakeId;
	}

	public void setIntakeId(Integer intakeId) {
		this.intakeId = intakeId;
	}

	public Integer getFeedingScheduledId() {
		return feedingScheduledId;
	}

	public void setFeedingScheduledId(Integer feedingScheduledId) {
		this.feedingScheduledId = feedingScheduledId;
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

	public Integer getOtherFoodtypeId() {
		return otherFoodtypeId;
	}

	public void setOtherFoodtypeId(Integer otherFoodtypeId) {
		this.otherFoodtypeId = otherFoodtypeId;
	}

	public String getOtherFoodTypeName() {
		return otherFoodTypeName;
	}

	public void setOtherFoodTypeName(String otherFoodTypeName) {
		this.otherFoodTypeName = otherFoodTypeName;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getQuantityRecommendedRounded() {
		return quantityRecommendedRounded;
	}

	public void setQuantityRecommendedRounded(String quantityRecommendedRounded) {
		this.quantityRecommendedRounded = quantityRecommendedRounded;
	}

	public Double getQuantityRecommended() {
		return quantityRecommended;
	}

	public void setQuantityRecommended(Double quantityRecommended) {
		this.quantityRecommended = quantityRecommended;
	}

	public Double getQuantityOffered() {
		return quantityOffered;
	}

	public void setQuantityOffered(Double quantityOffered) {
		this.quantityOffered = quantityOffered;
	}

	public Double getQuantityConsumed() {
		return quantityConsumed;
	}

	public void setQuantityConsumed(Double quantityConsumed) {
		this.quantityConsumed = quantityConsumed;
	}

	public String getQuantityRecommendedRoundedCups() {
		return quantityRecommendedRoundedCups;
	}

	public void setQuantityRecommendedRoundedCups(String quantityRecommendedRoundedCups) {
		this.quantityRecommendedRoundedCups = quantityRecommendedRoundedCups;
	}

	public Double getQuantityRecommendedCups() {
		return quantityRecommendedCups;
	}

	public void setQuantityRecommendedCups(Double quantityRecommendedCups) {
		this.quantityRecommendedCups = quantityRecommendedCups;
	}

	public Double getQuantityOfferedCups() {
		return quantityOfferedCups;
	}

	public void setQuantityOfferedCups(Double quantityOfferedCups) {
		this.quantityOfferedCups = quantityOfferedCups;
	}

	public Double getQuantityConsumedCups() {
		return quantityConsumedCups;
	}

	public void setQuantityConsumedCups(Double quantityConsumedCups) {
		this.quantityConsumedCups = quantityConsumedCups;
	}

	public Integer getQuantityUnitId() {
		return quantityUnitId;
	}

	public void setQuantityUnitId(Integer quantityUnitId) {
		this.quantityUnitId = quantityUnitId;
	}

	public String getQuantityUnitName() {
		return quantityUnitName;
	}

	public void setQuantityUnitName(String quantityUnitName) {
		this.quantityUnitName = quantityUnitName;
	}

	public Double getPercentConsumed() {
		return percentConsumed;
	}

	public void setPercentConsumed(Double percentConsumed) {
		this.percentConsumed = percentConsumed;
	}

	public Double getCalDensity() {
		return calDensity;
	}

	public void setCalDensity(Double calDensity) {
		this.calDensity = calDensity;
	}

	public Integer getCalDensityUnitId() {
		return calDensityUnitId;
	}

	public void setCalDensityUnitId(Integer calDensityUnitId) {
		this.calDensityUnitId = calDensityUnitId;
	}

	public Integer getCalDensityUnitName() {
		return calDensityUnitName;
	}

	public void setCalDensityUnitName(Integer calDensityUnitName) {
		this.calDensityUnitName = calDensityUnitName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

}
