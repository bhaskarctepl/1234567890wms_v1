package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.BfiScoreImages;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BfiScoreImagesResponse {

	private List<BfiScoreImages> bfiScoreImagesList;

	public List<BfiScoreImages> getBfiScoreImagesList() {
		return bfiScoreImagesList;
	}

	public void setBfiScoreImagesList(List<BfiScoreImages> bfiScoreImagesList) {
		this.bfiScoreImagesList = bfiScoreImagesList;
	}

}
