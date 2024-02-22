package com.hillspet.wearables.response;

import java.util.List;

import com.hillspet.wearables.dto.PetRecommondedDietsDTO;

public class FoodIntakeLookUpResponse {

	private List<MeasurementUnit> measurementUnits;
	private List<OtherFoods> otherFoods;
	private List<DietFeedback> dietFeedback;
	private List<PetRecommondedDietsDTO> recommondedDiet;

	public List<MeasurementUnit> getMeasurementUnits() {
		return measurementUnits;
	}

	public void setMeasurementUnits(List<MeasurementUnit> measurementUnits) {
		this.measurementUnits = measurementUnits;
	}

	public List<OtherFoods> getOtherFoods() {
		return otherFoods;
	}

	public void setOtherFoods(List<OtherFoods> otherFoods) {
		this.otherFoods = otherFoods;
	}

	public List<DietFeedback> getDietFeedback() {
		return dietFeedback;
	}

	public void setDietFeedback(List<DietFeedback> dietFeedback) {
		this.dietFeedback = dietFeedback;
	}

	public List<PetRecommondedDietsDTO> getRecommondedDiet() {
		return recommondedDiet;
	}

	public void setRecommondedDiet(List<PetRecommondedDietsDTO> recommondedDiet) {
		this.recommondedDiet = recommondedDiet;
	}

}
