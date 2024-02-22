package com.hillspet.wearables.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Contains all the petIds which need to mobile configs", value = "PetIds")
@JsonInclude(value = Include.NON_NULL)
public class PetIds {

	private List<Integer> petIds;

	public List<Integer> getPetIds() {
		return petIds;
	}

	public void setPetIds(List<Integer> petIds) {
		this.petIds = petIds;
	}

}
