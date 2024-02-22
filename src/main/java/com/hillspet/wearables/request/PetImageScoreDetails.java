package com.hillspet.wearables.request;

import io.swagger.annotations.ApiModelProperty;

public class PetImageScoreDetails {
	
	@ApiModelProperty(value = "imageScoringDtlsId", required = true, example = "1")
	private Integer imageScoringDtlsId;
	
	@ApiModelProperty(value = "imageUrl", required = true, example = "")
	private String imageUrl;
	
	@ApiModelProperty(value = "thumbnailUrl", required = true, example = "")
	private String thumbnailUrl;
	
	@ApiModelProperty(value = "value", required = true, example = "1.0")
	private String value;
	
	@ApiModelProperty(value = "uom", required = true, example = "1")
	private Integer uom;

	public Integer getUom() {
		return uom;
	}

	public void setUom(Integer uom) {
		this.uom = uom;
	}

	public Integer getImageScoringDtlsId() {
		return imageScoringDtlsId;
	}

	public void setImageScoringDtlsId(Integer imageScoringDtlsId) {
		this.imageScoringDtlsId = imageScoringDtlsId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
