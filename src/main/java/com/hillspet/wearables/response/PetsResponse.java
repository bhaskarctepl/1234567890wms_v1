package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.PetDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PetsResponse {

	private List<PetDTO> pets;

	public List<PetDTO> getPets() {
		return pets;
	}

	public void setPets(List<PetDTO> pets) {
		this.pets = pets;
	}

}
