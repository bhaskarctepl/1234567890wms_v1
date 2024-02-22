package com.hillspet.wearables.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;

/**
 * @author akumarkhaspa
 *
 */
@ApiModel(description = "Contains all information that needed for Food Intake History Graph", value = "FoodIntakeHistoryGraphDTO")
@JsonInclude(value = Include.NON_NULL)
public class FoodIntakeHistoryGraphDTO {

	private LocalDate intakeDate;

	private List<FoodIntakeHistoryDTO> intakeComparision;

	private List<FoodIntakeHistoryDTO> intakeDistribution;

	public LocalDate getIntakeDate() {
		return intakeDate;
	}

	public void setIntakeDate(LocalDate intakeDate) {
		this.intakeDate = intakeDate;
	}

	public List<FoodIntakeHistoryDTO> getIntakeComparision() {
		return intakeComparision;
	}

	public void setIntakeComparision(List<FoodIntakeHistoryDTO> intakeComparision) {
		this.intakeComparision = intakeComparision;
	}

	public List<FoodIntakeHistoryDTO> getIntakeDistribution() {
		return intakeDistribution;
	}

	public void setIntakeDistribution(List<FoodIntakeHistoryDTO> intakeDistribution) {
		this.intakeDistribution = intakeDistribution;
	}

}
