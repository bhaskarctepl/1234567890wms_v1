package com.hillspet.wearables.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author sgorle
 *
 */
@ApiModel(description = "Contains all information that needed to add image score to a pet", value = "PetAddImageScorings")
@JsonInclude(value = Include.NON_NULL)
public class PetAddImageScoring {

	@ApiModelProperty(value = "imageScoreType", required = true, example = "1")
	private Integer imageScoreType;
	
	@ApiModelProperty(value = "petImageScoringId", required = true, example = "1")
	private Integer petImageScoringId;
	
	@ApiModelProperty(value = "imageScoringId", required = true, example = "1")
	private Integer imageScoringId;
	
	@ApiModelProperty(value = "petId", required = true, example = "1")
	private Integer petId;
	
	@ApiModelProperty(value = "petImgScoreDetails", required = true, example = "[]")
	private List<PetImageScoreDetails> petImgScoreDetails;
	
	@ApiModelProperty(value = "petParentId", required = true, example = "1")
	private Integer petParentId;

	public Integer getImageScoreType() {
		return imageScoreType;
	}

	public void setImageScoreType(Integer imageScoreType) {
		this.imageScoreType = imageScoreType;
	}

	public Integer getPetImageScoringId() {
		return petImageScoringId;
	}

	public void setPetImageScoringId(Integer petImageScoringId) {
		this.petImageScoringId = petImageScoringId;
	}

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public List<PetImageScoreDetails> getPetImgScoreDetails() {
		return petImgScoreDetails;
	}

	public void setPetImgScoreDetails(List<PetImageScoreDetails> petImgScoreDetails) {
		this.petImgScoreDetails = petImgScoreDetails;
	}

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

	public Integer getImageScoringId() {
		return imageScoringId;
	}

	public void setImageScoringId(Integer imageScoringId) {
		this.imageScoringId = imageScoringId;
	}

}
