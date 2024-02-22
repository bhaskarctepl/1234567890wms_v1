package com.hillspet.wearables.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hillspet.wearables.dto.PetBfiImage;

import io.swagger.annotations.ApiModel;

/**
 * 
 * @author vvodyaram
 *
 */
@ApiModel(description = "Contains all information that needed to add a pet images", value = "PetBfiImagesRequest")
@JsonInclude(value = Include.NON_NULL)
public class PetBfiImagesRequest {

	private Integer petBfiImagesSetId;
	private Integer petParentId;
	private Integer petId;
	private PetParentInfo petParentInfo;
	private PetInfo petInfo;
	private List<PetBfiImage> petBfiImages;
	private Integer createdBy;

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

}
