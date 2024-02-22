package com.hillspet.wearables.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Contains all information that needed to add a bfi image scores", value = "PetBfiImageScoreRequest")
@JsonInclude(value = Include.NON_NULL)
public class PetBfiImageScoreRequest {

	private Integer petBfiImageSetId;
	private Integer bfiImageScoreId;
	private String description;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Integer createdBy;

	public Integer getPetBfiImageSetId() {
		return petBfiImageSetId;
	}

	public void setPetBfiImageSetId(Integer petBfiImageSetId) {
		this.petBfiImageSetId = petBfiImageSetId;
	}

	public Integer getBfiImageScoreId() {
		return bfiImageScoreId;
	}

	public void setBfiImageScoreId(Integer bfiImageScoreId) {
		this.bfiImageScoreId = bfiImageScoreId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

}
