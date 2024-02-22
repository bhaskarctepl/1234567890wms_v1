package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.PetImagePosition;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PetImagePositionResponse {
	private List<PetImagePosition> imagePositions;

	public List<PetImagePosition> getImagePositions() {
		return imagePositions;
	}

	public void setImagePositions(List<PetImagePosition> imagePositions) {
		this.imagePositions = imagePositions;
	}
}
