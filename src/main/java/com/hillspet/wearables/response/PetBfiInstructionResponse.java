package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.PetBfiInstruction;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PetBfiInstructionResponse {
	private List<PetBfiInstruction> petBfiInstructions;

	public List<PetBfiInstruction> getPetBfiInstructions() {
		return petBfiInstructions;
	}

	public void setPetBfiInstructions(List<PetBfiInstruction> petBfiInstructions) {
		this.petBfiInstructions = petBfiInstructions;
	}

}
