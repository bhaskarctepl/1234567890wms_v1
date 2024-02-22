package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hillspet.wearables.dto.DietFeedbackDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DietFeedback {

	private Integer feedbackId;
	private String feedbackCategory;
	private String description;
	private DietFeedbackDTO selectedFeedback;

	public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getFeedbackCategory() {
		return feedbackCategory;
	}

	public void setFeedbackCategory(String feedbackCategory) {
		this.feedbackCategory = feedbackCategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DietFeedbackDTO getSelectedFeedback() {
		return selectedFeedback;
	}

	public void setSelectedFeedback(DietFeedbackDTO selectedFeedback) {
		this.selectedFeedback = selectedFeedback;
	}

}
