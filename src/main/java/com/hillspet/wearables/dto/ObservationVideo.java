package com.hillspet.wearables.dto;

import io.swagger.annotations.ApiModelProperty;

public class ObservationVideo {

	@ApiModelProperty(value = "observationVideoId", required = true, example = "0")
	private int observationVideoId;
	
	@ApiModelProperty(value = "videoName", required = true, example = "Jumping.mp4")
	private String videoName;
	
	@ApiModelProperty(value = "videoUrl", required = true, example = "")
	private String videoUrl;
	
	@ApiModelProperty(value = "videoThumbnailUrl", required = true, example = "")
	private String videoThumbnailUrl;
	
	@ApiModelProperty(value = "isDeleted", required = true, example = "0")
	private int isDeleted;
	
	@ApiModelProperty(value = "videoStartDate (MMDDYYYYHHmmss)", required = true, example = "06112020115425")
	private String videoStartDate;
	
	@ApiModelProperty(value = "videoEndDate (MMDDYYYYHHmmss)", required = true, example = "06112020115425")
	private String videoEndDate;

	public int getObservationVideoId() {
		return observationVideoId;
	}

	public void setObservationVideoId(int observationVideoId) {
		this.observationVideoId = observationVideoId;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoThumbnailUrl() {
		return videoThumbnailUrl;
	}

	public void setVideoThumbnailUrl(String videoThumbnailUrl) {
		this.videoThumbnailUrl = videoThumbnailUrl;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getVideoStartDate() {
		return videoStartDate;
	}

	public void setVideoStartDate(String videoStartDate) {
		this.videoStartDate = videoStartDate;
	}

	public String getVideoEndDate() {
		return videoEndDate;
	}

	public void setVideoEndDate(String videoEndDate) {
		this.videoEndDate = videoEndDate;
	}

}
