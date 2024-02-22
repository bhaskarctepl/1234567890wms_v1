package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.BfiImageScore;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BfiImageScoreResponse {
	private List<BfiImageScore> bfiImageScores;

	public List<BfiImageScore> getBfiImageScores() {
		return bfiImageScores;
	}

	public void setBfiImageScores(List<BfiImageScore> bfiImageScores) {
		this.bfiImageScores = bfiImageScores;
	}

}
