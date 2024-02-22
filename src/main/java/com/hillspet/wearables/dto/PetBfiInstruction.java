package com.hillspet.wearables.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetBfiInstruction {
	private Integer instructionId;
	private String instruction;
	private String imageUrl;
	private Integer instructionType;
	private Integer instructionOrder;

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getInstructionType() {
		return instructionType;
	}

	public void setInstructionType(Integer instructionType) {
		this.instructionType = instructionType;
	}

	public Integer getInstructionOrder() {
		return instructionOrder;
	}

	public void setInstructionOrder(Integer instructionOrder) {
		this.instructionOrder = instructionOrder;
	}

}
