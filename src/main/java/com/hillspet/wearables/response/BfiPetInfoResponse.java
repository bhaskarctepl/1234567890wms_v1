package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.PetBfiInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BfiPetInfoResponse {

	private List<PetBfiInfo> bfiPetInfoList;

	public List<PetBfiInfo> getBfiPetInfoList() {
		return bfiPetInfoList;
	}

	public void setBfiPetInfoList(List<PetBfiInfo> bfiPetInfoList) {
		this.bfiPetInfoList = bfiPetInfoList;
	}

}
