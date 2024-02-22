package com.hillspet.wearables.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author sgorle
 *
 */
@ApiModel(description = "Contains all information that needed to create Pet Feeding Enthusiasm Scale Details", value = "PetFeedingEnthusiasmScale")
@JsonInclude(value = Include.NON_NULL)
public class PetFeedingEnthusiasmScale {

	@ApiModelProperty(value = "feedingEnthusiasmScaleId", required = true, example = "0")
	private Integer feedingEnthusiasmScaleId;
	
	@ApiModelProperty(value = "petId", required = true, example = "123")
	private Integer petId;
	
	@ApiModelProperty(value = "enthusiasmScaleId", required = true, example = "1")
	private Integer enthusiasmScaleId;
	
	@ApiModelProperty(value = "feedingTimeId", required = true, example = "1")
	private Integer feedingTimeId;
	
	@ApiModelProperty(value = "feedingDate", required = true, example = "2022-12-21")
	private String feedingDate;
	
	@ApiModelProperty(value = "petParentId", required = true, example = "12")
	private Integer petParentId;

	public Integer getFeedingEnthusiasmScaleId() {
		return feedingEnthusiasmScaleId;
	}

	public void setFeedingEnthusiasmScaleId(Integer feedingEnthusiasmScaleId) {
		this.feedingEnthusiasmScaleId = feedingEnthusiasmScaleId;
	}

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public Integer getEnthusiasmScaleId() {
		return enthusiasmScaleId;
	}

	public void setEnthusiasmScaleId(Integer enthusiasmScaleId) {
		this.enthusiasmScaleId = enthusiasmScaleId;
	}

	public Integer getFeedingTimeId() {
		return feedingTimeId;
	}

	public void setFeedingTimeId(Integer feedingTimeId) {
		this.feedingTimeId = feedingTimeId;
	}

	public String getFeedingDate() {
		return feedingDate;
	}

	public void setFeedingDate(String feedingDate) {
		this.feedingDate = feedingDate;
	}

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}
}
