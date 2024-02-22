package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.PetFoodBrand;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PetFoodBrandResponse {
	private List<PetFoodBrand> petFoodBrands;

	public List<PetFoodBrand> getPetFoodBrands() {
		return petFoodBrands;
	}

	public void setPetFoodBrands(List<PetFoodBrand> petFoodBrands) {
		this.petFoodBrands = petFoodBrands;
	}

}
