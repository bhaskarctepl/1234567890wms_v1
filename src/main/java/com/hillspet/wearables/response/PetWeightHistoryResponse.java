package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hillspet.wearables.dto.PetIBWHistoryDTO;
import com.hillspet.wearables.dto.PetWeightChartDTO;
import com.hillspet.wearables.dto.PetWeightHistoryDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetWeightHistoryResponse {

	private List<PetWeightHistoryDTO> petWeightHistories;
	private List<PetIBWHistoryDTO> ibwList;
	private List<PetWeightChartDTO> weightChartList;

	public List<PetWeightHistoryDTO> getPetWeightHistories() {
		return petWeightHistories;
	}

	public void setPetWeightHistories(List<PetWeightHistoryDTO> petWeightHistories) {
		this.petWeightHistories = petWeightHistories;
	}

	public List<PetIBWHistoryDTO> getIbwList() {
		return ibwList;
	}

	public void setIbwList(List<PetIBWHistoryDTO> ibwList) {
		this.ibwList = ibwList;
	}

	public List<PetWeightChartDTO> getWeightChartList() {
		return weightChartList;
	}

	public void setWeightChartList(List<PetWeightChartDTO> weightChartList) {
		this.weightChartList = weightChartList;
	}

}
