package com.hillspet.wearables.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EatingEnthusiasmScale {
	private Integer enthusiasmScaleId;
	private String enthusiasmScaleValue;
	private String description;
	private String imageUrl;
	private Integer speciesId;

	public Integer getEnthusiasmScaleId() {
		return enthusiasmScaleId;
	}

	public void setEnthusiasmScaleId(Integer enthusiasmScaleId) {
		this.enthusiasmScaleId = enthusiasmScaleId;
	}

	public String getEnthusiasmScaleValue() {
		return enthusiasmScaleValue;
	}

	public void setEnthusiasmScaleValue(String enthusiasmScaleValue) {
		this.enthusiasmScaleValue = enthusiasmScaleValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(Integer speciesId) {
		this.speciesId = speciesId;
	}

}
