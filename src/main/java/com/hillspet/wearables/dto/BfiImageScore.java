package com.hillspet.wearables.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BfiImageScore {
	private Integer bfiImageScoreId;
	private String score;
	private Integer speciesId;
	private String imageLabel;
	private String imageUrl;
	private String range;

	private List<BfiScoreInstruction> instructions;

	public Integer getBfiImageScoreId() {
		return bfiImageScoreId;
	}

	public void setBfiImageScoreId(Integer bfiImageScoreId) {
		this.bfiImageScoreId = bfiImageScoreId;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Integer getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(Integer speciesId) {
		this.speciesId = speciesId;
	}

	public String getImageLabel() {
		return imageLabel;
	}

	public void setImageLabel(String imageLabel) {
		this.imageLabel = imageLabel;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public List<BfiScoreInstruction> getInstructions() {
		return instructions;
	}

	public void setInstructions(List<BfiScoreInstruction> instructions) {
		this.instructions = instructions;
	}

}
