package com.hillspet.wearables.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionAnswerOption {
	private Integer questionAnswerId;
	private String questionAnswer;
	private Boolean submitQuestionnaire;
	private Integer skipTo;
	private Integer skipToSectionId;

	private String ansOptionMediaFileName;
	private String ansOptionMediaUrl;
	private Integer ansOptionMediaType;

	public Integer getQuestionAnswerId() {
		return questionAnswerId;
	}

	public void setQuestionAnswerId(Integer questionAnswerId) {
		this.questionAnswerId = questionAnswerId;
	}

	public String getQuestionAnswer() {
		return questionAnswer;
	}

	public void setQuestionAnswer(String questionAnswer) {
		this.questionAnswer = questionAnswer;
	}

	public Boolean getSubmitQuestionnaire() {
		return submitQuestionnaire;
	}

	public void setSubmitQuestionnaire(Boolean submitQuestionnaire) {
		this.submitQuestionnaire = submitQuestionnaire;
	}

	public Integer getSkipTo() {
		return skipTo;
	}

	public void setSkipTo(Integer skipTo) {
		this.skipTo = skipTo;
	}

	public Integer getSkipToSectionId() {
		return skipToSectionId;
	}

	public void setSkipToSectionId(Integer skipToSectionId) {
		this.skipToSectionId = skipToSectionId;
	}

	public String getAnsOptionMediaUrl() {
		return ansOptionMediaUrl;
	}

	public void setAnsOptionMediaUrl(String ansOptionMediaUrl) {
		this.ansOptionMediaUrl = ansOptionMediaUrl;
	}

	public Integer getAnsOptionMediaType() {
		return ansOptionMediaType;
	}

	public void setAnsOptionMediaType(Integer ansOptionMediaType) {
		this.ansOptionMediaType = ansOptionMediaType;
	}

	public String getAnsOptionMediaFileName() {
		return ansOptionMediaFileName;
	}

	public void setAnsOptionMediaFileName(String ansOptionMediaFileName) {
		this.ansOptionMediaFileName = ansOptionMediaFileName;
	}

}
