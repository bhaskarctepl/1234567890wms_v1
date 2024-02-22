package com.hillspet.wearables.dto;

import io.swagger.annotations.ApiModelProperty;

public class ObservationPhoto {
	
	@ApiModelProperty(value = "behaviorName", required = true, example = "0")
	private int observationPhotoId;
	
	@ApiModelProperty(value = "fileName", required = true, example = "Jumping.png")
	private String fileName;
	
	@ApiModelProperty(value = "filePath", required = true, example = "")
	private String filePath;
	
	@ApiModelProperty(value = "isDeleted", required = true, example = "0")
	private int isDeleted;

	public int getObservationPhotoId() {
		return observationPhotoId;
	}

	public void setObservationPhotoId(int observationPhotoId) {
		this.observationPhotoId = observationPhotoId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

}
