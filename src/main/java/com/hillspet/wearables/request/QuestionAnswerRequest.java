package com.hillspet.wearables.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hillspet.wearables.dto.QuestionAnswer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author sgorle
 *
 */
@ApiModel(description = "Contains all information that needed to save questionnaire answers", value = "QuestionAnswerRequest")
@JsonInclude(value = Include.NON_NULL)
public class QuestionAnswerRequest {

	@ApiModelProperty(value = "questionnaireId", required = true, example = "1")
	private Integer questionnaireId;
	
	@ApiModelProperty(value = "questionnaireLevelId", required = true, example = "1")
	private Integer questionnaireLevelId;
	
	@ApiModelProperty(value = "studyIds", required = true, example = "1,2,3")
	private String  studyIds;
	
	@ApiModelProperty(value = "petId", required = true, example = "1")
	private Integer petId;
	
	@ApiModelProperty(value = "petParentId", required = true, example = "12")
	private Integer petParentId;
	
	@ApiModelProperty(value = "questionAnswers", required = true)
	private List<QuestionAnswer> questionAnswers;

	public Integer getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Integer questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public Integer getQuestionnaireLevelId() {
		return questionnaireLevelId;
	}

	public void setQuestionnaireLevelId(Integer questionnaireLevelId) {
		this.questionnaireLevelId = questionnaireLevelId;
	}

	public String getStudyIds() {
		return studyIds;
	}

	public void setStudyIds(String studyIds) {
		this.studyIds = studyIds;
	}

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

	public List<QuestionAnswer> getQuestionAnswers() {
		return questionAnswers;
	}

	public void setQuestionAnswers(List<QuestionAnswer> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}

}
