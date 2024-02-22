package com.hillspet.wearables.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetObservation {

	@ApiModelProperty(value = "observationId", required = true, example = "0")
	private int observationId;

	@ApiModelProperty(value = "petId", required = true, example = "123")
	private int petId;

	@ApiModelProperty(value = "obsText", required = true, example = "Jumping")
	private String obsText;

	@ApiModelProperty(value = "tag", required = false, example = "")
	private String tag;

	@ApiModelProperty(value = "behaviorId", required = true, example = "123")
	private int behaviorTypeId;

	@ApiModelProperty(value = "behaviorId", required = true, example = "123")
	private int behaviorId;

	@ApiModelProperty(value = "behaviorName", required = false, example = "Jumping")
	private String behaviorName;

	@ApiModelProperty(value = "observationDateTime", required = true, example = "Wed Nov 01 2023 19:25:19 GMT+0530 (India Standard Time)")
	private LocalDateTime observationDateTime;

	@ApiModelProperty(value = "emotionIconsText", required = false, example = "")
	private String emotionIconsText;

	@ApiModelProperty(value = "seizuresDescription", required = false, example = "")
	private String seizuresDescription;

	@ApiModelProperty(value = "loginUserId", required = true, example = "123")
	private int loginUserId;

	@ApiModelProperty(value = "modifiedDate", required = true, example = "2022-12-21")
	private LocalDateTime modifiedDate;

	@ApiModelProperty(value = "videos", required = false, example = "[]")
	private List<ObservationVideo> videos;

	@ApiModelProperty(value = "photos", required = false, example = "[]")
	private List<ObservationPhoto> photos;

	public int getObservationId() {
		return observationId;
	}

	public void setObservationId(int observationId) {
		this.observationId = observationId;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public String getObsText() {
		return obsText;
	}

	public void setObsText(String obsText) {
		this.obsText = obsText;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getBehaviorTypeId() {
		return behaviorTypeId;
	}

	public void setBehaviorTypeId(int behaviorTypeId) {
		this.behaviorTypeId = behaviorTypeId;
	}

	public int getBehaviorId() {
		return behaviorId;
	}

	public void setBehaviorId(int behaviorId) {
		this.behaviorId = behaviorId;
	}

	public String getBehaviorName() {
		return behaviorName;
	}

	public void setBehaviorName(String behaviorName) {
		this.behaviorName = behaviorName;
	}

	public LocalDateTime getObservationDateTime() {
		return observationDateTime;
	}

	public void setObservationDateTime(LocalDateTime observationDateTime) {
		this.observationDateTime = observationDateTime;
	}

	public String getEmotionIconsText() {
		return emotionIconsText;
	}

	public void setEmotionIconsText(String emotionIconsText) {
		this.emotionIconsText = emotionIconsText;
	}

	public String getSeizuresDescription() {
		return seizuresDescription;
	}

	public void setSeizuresDescription(String seizuresDescription) {
		this.seizuresDescription = seizuresDescription;
	}

	public int getLoginUserId() {
		return loginUserId;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setLoginUserId(int loginUserId) {
		this.loginUserId = loginUserId;
	}

	public List<ObservationVideo> getVideos() {
		return videos;
	}

	public void setVideos(List<ObservationVideo> videos) {
		this.videos = videos;
	}

	public List<ObservationPhoto> getPhotos() {
		return photos;
	}

	public void setPhotos(List<ObservationPhoto> photos) {
		this.photos = photos;
	}

}
