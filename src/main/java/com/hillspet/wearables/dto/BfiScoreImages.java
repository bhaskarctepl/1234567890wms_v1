package com.hillspet.wearables.dto;

public class BfiScoreImages {

	private Integer bfiImageScoreId;
	private Integer speciesId;
	private String bfiScore;
	private String imageFilePath;
	private String imageLabel;
	private String description;

	public Integer getBfiImageScoreId() {
		return bfiImageScoreId;
	}

	public void setBfiImageScoreId(Integer bfiImageScoreId) {
		this.bfiImageScoreId = bfiImageScoreId;
	}

	public Integer getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(Integer speciesId) {
		this.speciesId = speciesId;
	}

	public String getBfiScore() {
		return bfiScore;
	}

	public void setBfiScore(String bfiScore) {
		this.bfiScore = bfiScore;
	}

	public String getImageFilePath() {
		return imageFilePath;
	}

	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}

	public String getImageLabel() {
		return imageLabel;
	}

	public void setImageLabel(String imageLabel) {
		this.imageLabel = imageLabel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
