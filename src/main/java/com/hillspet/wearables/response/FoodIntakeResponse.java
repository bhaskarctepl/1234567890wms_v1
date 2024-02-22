package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.FoodIntakeDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodIntakeResponse {

	private List<FoodIntakeDTO> intakes;

	public List<FoodIntakeDTO> getIntakes() {
		return intakes;
	}

	public void setIntakes(List<FoodIntakeDTO> intakes) {
		this.intakes = intakes;
	}

}
