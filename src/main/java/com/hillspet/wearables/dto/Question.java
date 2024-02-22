package com.hillspet.wearables.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question {

	private Integer questionId;
	private String question;
	private String questionImageUrl;
	private String answer;
	private Integer questionTypeId;
	private String questionType;
	private Integer questionOrder;
	private Boolean isMandatory;
	private Integer floor;
	private Integer ceil;
	private Integer tickStep;
	private Boolean isVerticalScale;
	private Boolean isContinuousScale;
	private String floorDescription;
	private String ceilDescription;

	private Integer sectionId;
	private String sectionName;
	private String sectionDescription;
	private Integer sectionOrder;

	private Boolean suffleOptionOrder;

	private List<QuestionAnswerOption> questionAnswerOptions;

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestionImageUrl() {
		return questionImageUrl;
	}

	public void setQuestionImageUrl(String questionImageUrl) {
		this.questionImageUrl = questionImageUrl;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeId(Integer questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public Integer getQuestionOrder() {
		return questionOrder;
	}

	public void setQuestionOrder(Integer questionOrder) {
		this.questionOrder = questionOrder;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public Integer getCeil() {
		return ceil;
	}

	public void setCeil(Integer ceil) {
		this.ceil = ceil;
	}

	public Integer getTickStep() {
		return tickStep;
	}

	public void setTickStep(Integer tickStep) {
		this.tickStep = tickStep;
	}

	public Boolean getIsVerticalScale() {
		return isVerticalScale;
	}

	public void setIsVerticalScale(Boolean isVerticalScale) {
		this.isVerticalScale = isVerticalScale;
	}

	public Boolean getIsContinuousScale() {
		return isContinuousScale;
	}

	public void setIsContinuousScale(Boolean isContinuousScale) {
		this.isContinuousScale = isContinuousScale;
	}

	public List<QuestionAnswerOption> getQuestionAnswerOptions() {
		return questionAnswerOptions;
	}

	public void setQuestionAnswerOptions(List<QuestionAnswerOption> questionAnswerOptions) {
		this.questionAnswerOptions = questionAnswerOptions;
	}

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionDescription() {
		return sectionDescription;
	}

	public void setSectionDescription(String sectionDescription) {
		this.sectionDescription = sectionDescription;
	}

	public Integer getSectionOrder() {
		return sectionOrder;
	}

	public void setSectionOrder(Integer sectionOrder) {
		this.sectionOrder = sectionOrder;
	}

	public String getFloorDescription() {
		return floorDescription;
	}

	public void setFloorDescription(String floorDescription) {
		this.floorDescription = floorDescription;
	}

	public String getCeilDescription() {
		return ceilDescription;
	}

	public void setCeilDescription(String ceilDescription) {
		this.ceilDescription = ceilDescription;
	}

	public Boolean getSuffleOptionOrder() {
		return suffleOptionOrder;
	}

	public void setSuffleOptionOrder(Boolean suffleOptionOrder) {
		this.suffleOptionOrder = suffleOptionOrder;
	}

}
