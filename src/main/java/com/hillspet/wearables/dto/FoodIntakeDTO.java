package com.hillspet.wearables.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hillspet.wearables.response.FoodIntakeLookUpResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author akumarkhaspa
 *
 */
@ApiModel(description = "Contains all information that needed for Food Intake", value = "FoodIntakeDTO")
@JsonInclude(value = Include.NON_NULL)
public class FoodIntakeDTO {

	@ApiModelProperty(value = "intakeId", required = true, example = "123")
	private Integer intakeId;
	@ApiModelProperty(value = "petId", required = true, example = "123")
	private int petId;
	@ApiModelProperty(value = "petParentId", required = true, example = "123")
	private Integer petParentId;
	@ApiModelProperty(value = "intakeDate", required = true, example = "MM-dd-yyyy HH:mm:ss")
	@JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime intakeDate;
	private Boolean isOtherFood;
	private Integer userId;
	@ApiModelProperty(value = "foodIntakeHistory", required = true, example = "[]")
	private List<FoodIntakeHistoryDTO> foodIntakeHistory;
	@ApiModelProperty(value = "dietFeedBack", required = true, example = "[]")
	private List<DietFeedbackDTO> dietFeedback;

	private FoodIntakeLookUpResponse intakeData;

	public Integer getIntakeId() {
		return intakeId;
	}

	public void setIntakeId(Integer intakeId) {
		this.intakeId = intakeId;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

	public LocalDateTime getIntakeDate() {
		return intakeDate;
	}

	public void setIntakeDate(LocalDateTime intakeDate) {
		this.intakeDate = intakeDate;
	}

	public Boolean getIsOtherFood() {
		return isOtherFood;
	}

	public void setIsOtherFood(Boolean isOtherFood) {
		this.isOtherFood = isOtherFood;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<FoodIntakeHistoryDTO> getFoodIntakeHistory() {
		return foodIntakeHistory;
	}

	public void setFoodIntakeHistory(List<FoodIntakeHistoryDTO> foodIntakeHistory) {
		this.foodIntakeHistory = foodIntakeHistory;
	}

	public List<DietFeedbackDTO> getDietFeedback() {
		return dietFeedback;
	}

	public void setDietFeedback(List<DietFeedbackDTO> dietFeedback) {
		this.dietFeedback = dietFeedback;
	}

	public FoodIntakeLookUpResponse getIntakeData() {
		return intakeData;
	}

	public void setIntakeData(FoodIntakeLookUpResponse intakeData) {
		this.intakeData = intakeData;
	}

}
