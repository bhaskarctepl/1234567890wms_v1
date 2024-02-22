package com.hillspet.wearables.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetImagePosition {
	private Integer imagePositionId;
	private String imagePositionName;
	private Boolean isMandatory;

	public Integer getImagePositionId() {
		return imagePositionId;
	}

	public void setImagePositionId(Integer imagePositionId) {
		this.imagePositionId = imagePositionId;
	}

	public String getImagePositionName() {
		return imagePositionName;
	}

	public void setImagePositionName(String imagePositionName) {
		this.imagePositionName = imagePositionName;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

}
