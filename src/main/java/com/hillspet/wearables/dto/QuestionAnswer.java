package com.hillspet.wearables.dto;

import io.swagger.annotations.ApiModelProperty;

public class QuestionAnswer {

	@ApiModelProperty(value = "questionnaireResponseId", required = false, example = "12")
	private Integer questionnaireResponseId;
	
	@ApiModelProperty(value = "questionId", required = true, example = "1")
	private Integer questionId;

	@ApiModelProperty(value = "answerOptionId", required = false, example = "12")
	private Integer answerOptionId;
	
	@ApiModelProperty(value = "answer", required = true, example = "answer")
	private String answer;
	
	@ApiModelProperty(value = "mediaType", required = false, example = "1")
	private Integer mediaType;
	
	@ApiModelProperty(value = "mediaFileName", required = false)
	private String mediaFileName;

	public Integer getQuestionnaireResponseId() {
		return questionnaireResponseId;
	}

	public void setQuestionnaireResponseId(Integer questionnaireResponseId) {
		this.questionnaireResponseId = questionnaireResponseId;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getAnswerOptionId() {
		return answerOptionId;
	}

	public void setAnswerOptionId(Integer answerOptionId) {
		this.answerOptionId = answerOptionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getMediaType() {
		return mediaType;
	}

	public void setMediaType(Integer mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaFileName() {
		return mediaFileName;
	}

	public void setMediaFileName(String mediaFileName) {
		this.mediaFileName = mediaFileName;
	}

}
