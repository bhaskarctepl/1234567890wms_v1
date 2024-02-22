package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hillspet.wearables.dto.FoodIntakeHistoryDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherFoods {

	private Integer otherFoodId;
	private String otherFoodType;
	private String description;
	private List<FoodIntakeHistoryDTO> foodIntakeHistory;

	public Integer getOtherFoodId() {
		return otherFoodId;
	}

	public void setOtherFoodId(Integer otherFoodId) {
		this.otherFoodId = otherFoodId;
	}

	public String getOtherFoodType() {
		return otherFoodType;
	}

	public void setOtherFoodType(String otherFoodType) {
		this.otherFoodType = otherFoodType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<FoodIntakeHistoryDTO> getFoodIntakeHistory() {
		return foodIntakeHistory;
	}

	public void setFoodIntakeHistory(List<FoodIntakeHistoryDTO> foodIntakeHistory) {
		this.foodIntakeHistory = foodIntakeHistory;
	}

}
