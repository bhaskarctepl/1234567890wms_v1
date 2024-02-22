package com.hillspet.wearables.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hillspet.wearables.request.PetInfo;
import com.hillspet.wearables.request.PetParentInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetBfiInfo {

	private Integer petBfiImagesSetId;
	private Integer petParentId;
	private Integer petId;
	private PetParentInfo petParentInfo;
	private PetInfo petInfo;
	private List<PetBfiImage> petBfiImages;
	private Integer createdBy;
	private LocalDateTime capturedOn;
	private LocalDateTime submittedOn;
	private String submittedUserName;
	private Integer bfiImageScoreId;
	private String bfiScore;

	public Integer getPetBfiImagesSetId() {
		return petBfiImagesSetId;
	}

	public void setPetBfiImagesSetId(Integer petBfiImagesSetId) {
		this.petBfiImagesSetId = petBfiImagesSetId;
	}

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public PetParentInfo getPetParentInfo() {
		return petParentInfo;
	}

	public void setPetParentInfo(PetParentInfo petParentInfo) {
		this.petParentInfo = petParentInfo;
	}

	public PetInfo getPetInfo() {
		return petInfo;
	}

	public void setPetInfo(PetInfo petInfo) {
		this.petInfo = petInfo;
	}

	public List<PetBfiImage> getPetBfiImages() {
		return petBfiImages;
	}

	public void setPetBfiImages(List<PetBfiImage> petBfiImages) {
		this.petBfiImages = petBfiImages;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCapturedOn() {
		return capturedOn;
	}

	public void setCapturedOn(LocalDateTime capturedOn) {
		this.capturedOn = capturedOn;
	}

	public LocalDateTime getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(LocalDateTime submittedOn) {
		this.submittedOn = submittedOn;
	}

	public String getSubmittedUserName() {
		return submittedUserName;
	}

	public void setSubmittedUserName(String submittedUserName) {
		this.submittedUserName = submittedUserName;
	}

	public Integer getBfiImageScoreId() {
		return bfiImageScoreId;
	}

	public void setBfiImageScoreId(Integer bfiImageScoreId) {
		this.bfiImageScoreId = bfiImageScoreId;
	}

	public String getBfiScore() {
		return bfiScore;
	}

	public void setBfiScore(String bfiScore) {
		this.bfiScore = bfiScore;
	}

}
