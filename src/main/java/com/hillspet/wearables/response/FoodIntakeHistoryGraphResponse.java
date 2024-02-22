package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.FoodIntakeHistoryGraphDTO;
import com.hillspet.wearables.dto.PetRecommondedDietsDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodIntakeHistoryGraphResponse {

	private List<PetRecommondedDietsDTO> dietList;

	private List<FoodIntakeHistoryGraphDTO> foodIntakeHistory;

	public List<PetRecommondedDietsDTO> getDietList() {
		return dietList;
	}

	public void setDietList(List<PetRecommondedDietsDTO> dietList) {
		this.dietList = dietList;
	}

	public List<FoodIntakeHistoryGraphDTO> getFoodIntakeHistory() {
		return foodIntakeHistory;
	}

	public void setFoodIntakeHistory(List<FoodIntakeHistoryGraphDTO> foodIntakeHistory) {
		this.foodIntakeHistory = foodIntakeHistory;
	}

}
