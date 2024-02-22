package com.hillspet.wearables.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetBFIImageScore {

	private Integer petBfiImageDtlsId;
	private Integer score;
	private String description;

	public Integer getPetBfiImageDtlsId() {
		return petBfiImageDtlsId;
	}

	public void setPetBfiImageDtlsId(Integer petBfiImageDtlsId) {
		this.petBfiImageDtlsId = petBfiImageDtlsId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
