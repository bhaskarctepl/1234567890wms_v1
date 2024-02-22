package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.PetBfiImage;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BfiPetImageDetailsResponse {

	private List<PetBfiImage> petBFIImages;

	public List<PetBfiImage> getPetBFIImages() {
		return petBFIImages;
	}

	public void setPetBFIImages(List<PetBfiImage> petBFIImages) {
		this.petBFIImages = petBFIImages;
	}
}
