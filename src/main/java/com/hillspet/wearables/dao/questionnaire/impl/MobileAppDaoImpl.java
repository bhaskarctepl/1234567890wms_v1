package com.hillspet.wearables.dao.questionnaire.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.constants.WearablesErrorCode;
import com.hillspet.wearables.common.dto.WearablesError;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.common.utils.GCPClientUtil;
import com.hillspet.wearables.common.utils.GCStorageUtil;
import com.hillspet.wearables.dao.BaseDaoImpl;
import com.hillspet.wearables.dao.questionnaire.MobileAppDao;
import com.hillspet.wearables.dto.ActivityLimit;
import com.hillspet.wearables.dto.Address;
import com.hillspet.wearables.dto.Campaign;
import com.hillspet.wearables.dto.DeviceDTO;
import com.hillspet.wearables.dto.LeaderBoard;
import com.hillspet.wearables.dto.MeasurementUnit;
import com.hillspet.wearables.dto.MobileAppFeedback;
import com.hillspet.wearables.dto.PetCampaignPointsDTO;
import com.hillspet.wearables.dto.PetCampaignPointsListDTO;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetRedemptionHistoryDTO;
import com.hillspet.wearables.dto.Question;
import com.hillspet.wearables.dto.QuestionAnswer;
import com.hillspet.wearables.dto.QuestionAnswerOption;
import com.hillspet.wearables.dto.Questionnaire;
import com.hillspet.wearables.dto.QuestionnaireInstruction;
import com.hillspet.wearables.dto.Study;
import com.hillspet.wearables.dto.TimeZone;
import com.hillspet.wearables.dto.User;
import com.hillspet.wearables.request.AssignSensorRequest;
import com.hillspet.wearables.request.QuestionAnswerRequest;
import com.hillspet.wearables.request.ValidateDuplicatePetRequest;
import com.hillspet.wearables.response.AppVersion;
import com.hillspet.wearables.response.PetParentAddressResponse;

@Repository
public class MobileAppDaoImpl extends BaseDaoImpl implements MobileAppDao {

	private static final Logger LOGGER = LogManager.getLogger(MobileAppDaoImpl.class);

	@Value("${gcp.env}")
	private String environment;

	@Autowired
	private GCPClientUtil gcpClientUtil;

	@Autowired
	private GCStorageUtil gcStorageUtil;

	public static final int QUESTIONNAIRE_LEVEL_PET = 1;
	public static final int QUESTIONNAIRE_LEVEL_PET_PARENT = 2;

	public static final String RESULT_SET_1 = "#result-set-1";
	public static final String RESULT_SET_2 = "#result-set-2";
	public static final String RESULT_SET_3 = "#result-set-3";
	public static final String RESULT_SET_4 = "#result-set-4";
	public static final String RESULT_SET_5 = "#result-set-5";

	public static final String MOBILE_APP_GET_FEEDBACK_QUESTIONNAIRE_BY_PET_ID = "MOBILE_APP_GET_FEEDBACK_QUESTIONNAIRE_BY_PET_ID";
	public static final String MOBILE_APP_GET_QUESTIONNAIRE_BY_PET_ID = "MOBILE_APP_GET_QUESTIONNAIRE_BY_PET_ID";
	public static final String MOBILE_APP_GET_QUESTIONNAIRE_ASNWER_BY_ID = "MOBILE_APP_GET_QUESTIONNAIRE_ASNWER_BY_ID";
	public static final String MOBILE_APP_INSERT_QUESTIONNAIRE_RESPONSE = "MOBILE_APP_INSERT_QUESTIONNAIRE_RESPONSE";
	public static final String MOBILE_APP_GET_CAMPAIGN_POINTS_LIST_BY_PET = "call MOBILE_APP_GET_CAMPAIGN_POINTS_LIST_BY_PET(?)";
	public static final String MOBILE_APP_GET_ALL_CAMPAIGN_POINTS_BY_PET = "call MOBILE_APP_GET_ALL_CAMPAIGN_POINTS_BY_PET(?)";
	public static final String MOBILE_APP_PET_PARENT_AUTH_BY_KEY = "MOBILE_APP_PET_PARENT_AUTH_BY_KEY";
	public static final String MOBILE_APP_GET_CAMPAIGNS_BY_PET = "call MOBILE_APP_GET_CAMPAIGNS_BY_PET(?)";
	public static final String MOBILE_APP_GET_LEADER_BOARD_BY_CAMPAIGN = "call MOBILE_APP_GET_LEADER_BOARD_BY_CAMPAIGN(?)";
	public static final String MOBILE_APP_GET_REDEEMTION_HISTORY_BY_PET = "call MOBILE_APP_GET_REDEEMTION_HISTORY_BY_PET(?)";
	public static final String MOBILE_APP_ASSIGN_PET_SENSOR = "MOBILE_APP_ASSIGN_PET_SENSOR";
	public static final String MOBILE_APP_GET_PET_DEVICES_BY_PET_PARENT_ID = "call MOBILE_APP_GET_PET_DEVICES_BY_PET_PARENT_ID(?)";
	public static final String MOBILE_APP_FEEDBACK_LIST = "call MOBILE_APP_GET_FEEDBACK_BY_PET_PARENT (?)";
	public static final String MOBILE_APP_GET_DEVICE_TYPES = "CALL MOBILE_APP_GET_GET_DEVICE_TYPES()";
	public static final String MOBILE_APP_GET_DEVICE_MODELS = "CALL MOBILE_APP_GET_DEVICE_MODELS(?)";
	public static final String MOBILE_APP_GET_TIMEZONE_BY_NAME = "CALL MOBILE_APP_GET_TIMEZONE_BY_NAME(?)";
	public static final String MOBILE_APP_GET_STUDY_INFO_BY_PET_ID = "CALL MOBILE_APP_GET_STUDY_INFO_BY_PET_ID(?)";
	public static final String MOBILE_APP_GET_ADDRESS_BY_PET_PARENT_ID = "CALL MOBILE_APP_GET_ADDRESS_BY_PET_PARENT_ID(?)";
	public static final String MOBILE_APP_PET_PARENT_GET_ADDRESSES_BY_ID = "MOBILE_APP_PET_PARENT_GET_ADDRESSES_BY_ID";
	public static final String QUESTIONNAIRE_GET_BY_ID = "QUESTIONNAIRE_GET_BY_ID";
	public static final String MOBILE_APP_INSERT_PET_PARENT_LEVEL_QUESTIONNAIRE = "MOBILE_APP_INSERT_PET_PARENT_LEVEL_QUESTIONNAIRE";
	public static final String MOBILE_PET_VALIDATE_DUPLICATE_PET = "MOBILE_PET_VALIDATE_DUPLICATE_PET";
	public static final String MOBILE_APP_GET_APP_CURRENT_VERSION_BY_APP_OS = "CALL MOBILE_APP_GET_APP_CURRENT_VERSION_BY_APP_OS(?,?)";

	public static final String MOBILE_APP_INSERT_QUESTIONNAIRE_RESPONSE_V1 = "MOBILE_APP_INSERT_QUESTIONNAIRE_RESPONSE_V1";
	public static final String MOBILE_APP_INSERT_PET_PARENT_LEVEL_QUESTIONNAIRE_V1 = "MOBILE_APP_INSERT_PET_PARENT_LEVEL_QUESTIONNAIRE_V1";

	public static final String MOBILE_APP_GET_USER_INFO_BY_USER_ID = "MOBILE_APP_GET_USER_INFO_BY_USER_ID";

	public static final String LU_GET_MEASUREMENT_UNITS = "CALL LU_GET_MEASUREMENT_UNITS(?)";

	@SuppressWarnings("unchecked")
	@Override
	public List<Questionnaire> getFeedbackQuestionnaireByPetId(int petId, String deviceModel, String isDateSupported)
			throws ServiceExecutionException {
		LOGGER.debug("getFeedbackQuestionnaireByPetId called");
		List<Questionnaire> questionnaireList = new ArrayList<>();

		Map<Integer, List<QuestionnaireInstruction>> instructionsMap = new HashMap<>();
		Map<Integer, List<QuestionAnswerOption>> questionAnsOptsMap = new HashMap<>();
		Map<Integer, List<Question>> questionsMap = new HashMap<>();
		try {
			// in params
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_id", petId);
			inputParams.put("p_device_model", deviceModel);

			LOGGER.info("getFeedbackQuestionnaireByPetId inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_GET_FEEDBACK_QUESTIONNAIRE_BY_PET_ID,
					inputParams);
			// LOGGER.info("getFeedbackQuestionnaireByPetId outParams are {}", outParams);

			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(instruction -> {
						QuestionnaireInstruction questionnaireInstruction = new QuestionnaireInstruction();
						questionnaireInstruction
								.setInstructionId((Integer) instruction.get("QUESTIONNAIRE_INSTRUCTION_ID"));
						questionnaireInstruction.setInstruction((String) instruction.get("INSTRUCTION"));
						questionnaireInstruction.setInstructionOrder((Integer) instruction.get("INSTRUCTION_ORDER"));

						Integer questionnaireId = (Integer) instruction.get("QUESTIONNAIRE_ID");
						instructionsMap.computeIfAbsent(questionnaireId, k -> new ArrayList<QuestionnaireInstruction>())
								.add(questionnaireInstruction);
					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(answerOpts -> {
						QuestionAnswerOption ansOptions = new QuestionAnswerOption();
						ansOptions.setQuestionAnswerId((Integer) answerOpts.get("QUESTION_ANSWER_OPTION_ID"));
						ansOptions.setQuestionAnswer((String) answerOpts.get("ANSWER"));
						ansOptions.setSubmitQuestionnaire(answerOpts.get("AUTO_SUBMIT_QUESTIONNAIRE") != null
								&& (Integer) answerOpts.get("AUTO_SUBMIT_QUESTIONNAIRE") > NumberUtils.INTEGER_ZERO
										? Boolean.TRUE
										: Boolean.FALSE);
						ansOptions.setSkipTo((Integer) answerOpts.get("SKIP_TO"));
						ansOptions.setSkipToSectionId((Integer) answerOpts.get("SKIP_TO_SECTION_ID"));
						Integer questionId = (Integer) answerOpts.get("QUESTION_ID");
						questionAnsOptsMap.computeIfAbsent(questionId, k -> new ArrayList<QuestionAnswerOption>())
								.add(ansOptions);
					});
				}

				if (key.equals(RESULT_SET_3)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(quest -> {
						Question question = new Question();
						Integer questionId = (Integer) quest.get("QUESTION_ID");
						question.setQuestionId(questionId);
						question.setQuestion((String) quest.get("QUESTION"));

						String fileName = (String) quest.get("QUESTION_IMAGE");

						if (fileName != null && fileName != "null" && !fileName.trim().equals("")) {
							question.setQuestionImageUrl(gcpClientUtil.getDownloaFiledUrl(fileName,
									Constants.GCP_QUESTIONNAIRE_QUESTION_IMAGE_PATH));
						}

						question.setQuestionTypeId((Integer) quest.get("QUESTION_TYPE_ID"));
						question.setQuestionType((String) quest.get("QUESTION_TYPE"));
						question.setQuestionOrder((Integer) quest.get("QUESTION_ORDER"));
						question.setIsMandatory(quest.get("IS_MANDATORY") != null
								&& (Integer) quest.get("IS_MANDATORY") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);

						question.setSuffleOptionOrder((quest.get("SHUFFLE_OPTION_ORDER") != null
								&& (Integer) quest.get("SHUFFLE_OPTION_ORDER") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE));

						question.setCeil((Integer) quest.get("SCALE_MAX"));
						question.setFloor((Integer) quest.get("SCALE_MIN"));
						question.setTickStep((Integer) quest.get("STEP_VALUE"));
						question.setIsVerticalScale(quest.get("IS_VERTICAL_SCALE") != null
								&& (Integer) quest.get("IS_VERTICAL_SCALE") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);
						question.setIsContinuousScale(quest.get("IS_CONTINUOUS_SCALE") != null
								&& (Integer) quest.get("IS_CONTINUOUS_SCALE") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);

						question.setSectionId((Integer) quest.get("QUESTIONNAIRE_SECTION_ID"));
						question.setSectionName((String) quest.get("SECTION_NAME"));
						question.setSectionDescription((String) quest.get("SECTION_DESCRIPTION"));
						question.setSectionOrder((Integer) quest.get("SECTION_ORDER"));
						question.setCeilDescription((String) quest.get("SCALE_MAX_DESC"));
						question.setFloorDescription((String) quest.get("SCALE_MIN_DESC"));

						question.setQuestionAnswerOptions(
								questionAnsOptsMap.get(questionId) != null ? questionAnsOptsMap.get(questionId)
										: new ArrayList<>());

						Integer questionnaireId = (Integer) quest.get("QUESTIONNAIRE_ID");
						if (!(question.getQuestionTypeId() == Constants.APP_INDEX_SEVEN
								|| question.getQuestionTypeId() == Constants.APP_INDEX_EIGHT
								|| question.getQuestionTypeId() == Constants.APP_INDEX_NINE)) {
							questionsMap.computeIfAbsent(questionnaireId, k -> new ArrayList<Question>()).add(question);
						}
					});
				}

				if (key.equals(RESULT_SET_4)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(quesObj -> {
						Questionnaire questionnaire = new Questionnaire();
						Integer questionnaireId = (Integer) quesObj.get("QUESTIONNAIRE_ID");

						questionnaire.setQuestionnaireId(questionnaireId);
						questionnaire.setQuestionnaireName((String) quesObj.get("QUESTIONNAIRE_NAME"));

						Date startDate = (Date) quesObj.get("START_DATE");
						questionnaire.setStartDate((startDate.toLocalDate()));

						Date endDate = (Date) quesObj.get("END_DATE");
						questionnaire.setEndDate((endDate.toLocalDate()));

						/*
						 * Long ansCount = (Long) quesObj.get("ANS_CNT"); if (ansCount > 0) {
						 * questionnaire.setStatus("Submitted"); } else { if
						 * (endDate.toLocalDate().isBefore(LocalDate.now())) {
						 * questionnaire.setStatus("Elapsed"); } else { questionnaire.setStatus("Open");
						 * } }
						 */

						questionnaire.setQuestions(
								questionsMap.get(questionnaireId) != null ? questionsMap.get(questionnaireId)
										: new ArrayList<>());

						questionnaire.setInstructions(
								instructionsMap.get(questionnaireId) != null ? instructionsMap.get(questionnaireId)
										: new ArrayList<>());
						questionnaireList.add(questionnaire);
					});
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching getFeedbackQuestionnaireByPetId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return questionnaireList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Questionnaire> getQuestionnaireByPetId(int petId, String isDateSupported)
			throws ServiceExecutionException {
		LOGGER.debug("getQuestionnaireByPetId called");
		List<Questionnaire> questionnaireList = new ArrayList<>();
		List<QuestionAnswer> questionAnswerList = new ArrayList<>();

		Map<Integer, List<QuestionnaireInstruction>> instructionsMap = new HashMap<>();
		Map<Integer, List<QuestionAnswerOption>> questionAnsOptsMap = new HashMap<>();
		Map<Integer, List<Question>> questionsMap = new HashMap<>();
		long startTime = System.currentTimeMillis();
		try {
			// in params
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_id", petId);

			LOGGER.info("getQuestionnaireByPetId inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_GET_QUESTIONNAIRE_BY_PET_ID, inputParams);
			// LOGGER.info("getQuestionnaireByPetId outParams are {}", outParams);

			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(instruction -> {
						QuestionnaireInstruction questionnaireInstruction = new QuestionnaireInstruction();
						questionnaireInstruction
								.setInstructionId((Integer) instruction.get("QUESTIONNAIRE_INSTRUCTION_ID"));
						questionnaireInstruction.setInstruction((String) instruction.get("INSTRUCTION"));
						questionnaireInstruction.setInstructionOrder((Integer) instruction.get("INSTRUCTION_ORDER"));

						Integer questionnaireId = (Integer) instruction.get("QUESTIONNAIRE_ID");
						instructionsMap.computeIfAbsent(questionnaireId, k -> new ArrayList<QuestionnaireInstruction>())
								.add(questionnaireInstruction);
					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(answerOpts -> {
						QuestionAnswerOption ansOptions = new QuestionAnswerOption();
						ansOptions.setQuestionAnswerId((Integer) answerOpts.get("QUESTION_ANSWER_OPTION_ID"));
						ansOptions.setQuestionAnswer((String) answerOpts.get("ANSWER"));
						ansOptions.setSubmitQuestionnaire(answerOpts.get("AUTO_SUBMIT_QUESTIONNAIRE") != null
								&& (Integer) answerOpts.get("AUTO_SUBMIT_QUESTIONNAIRE") > NumberUtils.INTEGER_ZERO
										? Boolean.TRUE
										: Boolean.FALSE);
						ansOptions.setSkipTo((Integer) answerOpts.get("SKIP_TO"));
						ansOptions.setSkipToSectionId((Integer) answerOpts.get("SKIP_TO_SECTION_ID"));

						ansOptions.setAnsOptionMediaType((Integer) answerOpts.get("MEDIA_TYPE"));

						String fileName = (String) answerOpts.get("MEDIA_FILE_NAME");

						ansOptions.setAnsOptionMediaFileName(fileName);

						if (StringUtils.isNotEmpty(fileName) && !fileName.equals("null")) {
							ansOptions.setAnsOptionMediaUrl(gcStorageUtil.getSignedMediaUrl(fileName,
									Constants.GCP_QUESTIONNAIRE_QUESTION_IMAGE_PATH));
						}

						Integer questionId = (Integer) answerOpts.get("QUESTION_ID");

						questionAnsOptsMap.computeIfAbsent(questionId, k -> new ArrayList<QuestionAnswerOption>())
								.add(ansOptions);
					});
				}

				if (key.equals(RESULT_SET_3)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(quest -> {
						Question question = new Question();
						Integer questionId = (Integer) quest.get("QUESTION_ID");
						question.setQuestionId(questionId);
						question.setQuestion((String) quest.get("QUESTION"));
						String fileName = (String) quest.get("QUESTION_IMAGE");

						if (StringUtils.isNotEmpty(fileName) && !fileName.equals("null")) {
							question.setQuestionImageUrl(gcpClientUtil.getDownloaFiledUrl(fileName,
									Constants.GCP_QUESTIONNAIRE_QUESTION_IMAGE_PATH));
						}
						question.setQuestionTypeId((Integer) quest.get("QUESTION_TYPE_ID"));
						question.setQuestionType((String) quest.get("QUESTION_TYPE"));
						question.setQuestionOrder((Integer) quest.get("QUESTION_ORDER"));
						question.setIsMandatory(quest.get("IS_MANDATORY") != null
								&& (Integer) quest.get("IS_MANDATORY") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);

						question.setSuffleOptionOrder((quest.get("SHUFFLE_OPTION_ORDER") != null
								&& (Integer) quest.get("SHUFFLE_OPTION_ORDER") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE));

						question.setCeil((Integer) quest.get("SCALE_MAX"));
						question.setFloor((Integer) quest.get("SCALE_MIN"));
						question.setTickStep((Integer) quest.get("STEP_VALUE"));
						question.setIsVerticalScale(quest.get("IS_VERTICAL_SCALE") != null
								&& (Integer) quest.get("IS_VERTICAL_SCALE") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);
						question.setIsContinuousScale(quest.get("IS_CONTINUOUS_SCALE") != null
								&& (Integer) quest.get("IS_CONTINUOUS_SCALE") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);

						question.setSectionId((Integer) quest.get("QUESTIONNAIRE_SECTION_ID"));
						question.setSectionName((String) quest.get("SECTION_NAME"));
						question.setSectionDescription((String) quest.get("SECTION_DESCRIPTION"));
						question.setSectionOrder((Integer) quest.get("SECTION_ORDER"));
						question.setCeilDescription((String) quest.get("SCALE_MAX_DESC"));
						question.setFloorDescription((String) quest.get("SCALE_MIN_DESC"));

						question.setQuestionAnswerOptions(
								questionAnsOptsMap.get(questionId) != null ? questionAnsOptsMap.get(questionId)
										: new ArrayList<>());

						Integer questionnaireId = (Integer) quest.get("QUESTIONNAIRE_ID");

						if (!(question.getQuestionTypeId() == Constants.APP_INDEX_SEVEN
								|| question.getQuestionTypeId() == Constants.APP_INDEX_EIGHT
								|| question.getQuestionTypeId() == Constants.APP_INDEX_NINE) || isDateSupported != null
								|| Boolean.valueOf(isDateSupported) == true) {
							questionsMap.computeIfAbsent(questionnaireId, k -> new ArrayList<Question>()).add(question);
						}
					});
				}

				if (key.equals(RESULT_SET_4)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(quesObj -> {
						Questionnaire questionnaire = new Questionnaire();
						Integer questionnaireId = (Integer) quesObj.get("QUESTIONNAIRE_ID");
						questionnaire.setStudyIds((String) quesObj.get("STUDY_ID"));

						questionnaire.setQuestionnaireId(questionnaireId);
						questionnaire.setQuestionnaireLevelId((Integer) quesObj.get("QUESTIONNAIRE_LEVEL_ID"));
						questionnaire.setQuestionnaireName((String) quesObj.get("QUESTIONNAIRE_NAME"));

						Date startDate = (Date) quesObj.get("START_DATE");
						questionnaire.setStartDate((startDate.toLocalDate()));

						Date endDate = (Date) quesObj.get("END_DATE");
						questionnaire.setEndDate((endDate.toLocalDate()));

						Long ansCount = (Long) quesObj.get("ANS_CNT");
						if (ansCount > 0) {
							questionnaire.setStatus("Submitted");
						} else {
							if (endDate.toLocalDate().isBefore(LocalDate.now())) {
								questionnaire.setStatus("Elapsed");
							} else {
								questionnaire.setStatus("Open");
							}
						}

						Integer questionnaireResponseId = quesObj.get("QUESTIONNAIRE_RESPONSE_ID") != null
								? (Integer) quesObj.get("QUESTIONNAIRE_RESPONSE_ID")
								: NumberUtils.INTEGER_ZERO;
						questionnaire.setQuestionnaireResponseId(questionnaireResponseId);

						questionnaire.setQuestions(
								questionsMap.get(questionnaireId) != null ? questionsMap.get(questionnaireId)
										: new ArrayList<>());

						questionnaire.setInstructions(
								instructionsMap.get(questionnaireId) != null ? instructionsMap.get(questionnaireId)
										: new ArrayList<>());
						questionnaireList.add(questionnaire);
					});
				}

				if (key.equals(RESULT_SET_5)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(quesObj -> {
						QuestionAnswer questionAnswer = new QuestionAnswer();

						questionAnswer.setQuestionnaireResponseId((Integer) quesObj.get("QUESTIONNAIRE_RESPONSE_ID"));
						questionAnswer.setQuestionId((Integer) quesObj.get("QUESTION_ID"));
						questionAnswer.setAnswer((String) quesObj.get("ANSWER"));
						questionAnswer.setAnswerOptionId((Integer) quesObj.get("ANSWER_OPTION_ID"));

						questionAnswerList.add(questionAnswer);
					});
				}

				questionnaireList.parallelStream().forEach(questionnaire -> {
					questionnaire.getQuestions().parallelStream().forEach(question -> {
						Optional<QuestionAnswer> optionalAnswer = questionAnswerList.stream()
								.filter(answer -> answer.getQuestionnaireResponseId()
										.equals(questionnaire.getQuestionnaireResponseId())
										&& answer.getQuestionId().equals(question.getQuestionId()))
								.findFirst();
						if (optionalAnswer.isPresent()) {
							question.setAnswer(optionalAnswer.get().getAnswer());
						}
					});
				});
			}
			long endTime = System.currentTimeMillis();
			LOGGER.info("Time taken to execute getQuestionnaireByPetId service in millis is {}", (endTime - startTime));
		} catch (Exception e) {
			LOGGER.error("error while fetching getQuestionnaireByPetId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return questionnaireList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Questionnaire> getQuestionnaireAnswers(int petId, int inQuestionnaireId)
			throws ServiceExecutionException {
		LOGGER.debug("getQuestionnaireAnswers called");
		List<Questionnaire> questionnaireList = new ArrayList<>();

		Map<Integer, List<QuestionnaireInstruction>> instructionsMap = new HashMap<>();
		Map<Integer, List<QuestionAnswerOption>> questionAnsOptsMap = new HashMap<>();
		Map<Integer, List<Question>> questionsMap = new HashMap<>();
		try {
			// in params
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_id", petId);
			inputParams.put("p_questionnaire_id", inQuestionnaireId);

			LOGGER.info("getQuestionnaireAnswers inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_GET_QUESTIONNAIRE_ASNWER_BY_ID, inputParams);
			LOGGER.info("getQuestionnaireAnswers outParams are {}", outParams);

			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(instruction -> {
						QuestionnaireInstruction questionnaireInstruction = new QuestionnaireInstruction();
						questionnaireInstruction
								.setInstructionId((Integer) instruction.get("QUESTIONNAIRE_INSTRUCTION_ID"));
						questionnaireInstruction.setInstruction((String) instruction.get("INSTRUCTION"));
						questionnaireInstruction.setInstructionOrder((Integer) instruction.get("INSTRUCTION_ORDER"));

						Integer questionnaireId = (Integer) instruction.get("QUESTIONNAIRE_ID");
						instructionsMap.computeIfAbsent(questionnaireId, k -> new ArrayList<QuestionnaireInstruction>())
								.add(questionnaireInstruction);
					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(answerOpts -> {
						QuestionAnswerOption ansOptions = new QuestionAnswerOption();
						ansOptions.setQuestionAnswerId((Integer) answerOpts.get("QUESTION_ANSWER_OPTION_ID"));
						ansOptions.setQuestionAnswer((String) answerOpts.get("ANSWER"));
						ansOptions.setSubmitQuestionnaire(answerOpts.get("AUTO_SUBMIT_QUESTIONNAIRE") != null
								&& (Integer) answerOpts.get("AUTO_SUBMIT_QUESTIONNAIRE") > NumberUtils.INTEGER_ZERO
										? Boolean.TRUE
										: Boolean.FALSE);
						ansOptions.setSkipTo((Integer) answerOpts.get("SKIP_TO"));
						Integer questionId = (Integer) answerOpts.get("QUESTION_ID");

						questionAnsOptsMap.computeIfAbsent(questionId, k -> new ArrayList<QuestionAnswerOption>())
								.add(ansOptions);
					});
				}

				if (key.equals(RESULT_SET_3)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(quest -> {
						Question question = new Question();
						Integer questionId = (Integer) quest.get("QUESTION_ID");
						question.setQuestionId(questionId);
						question.setQuestion((String) quest.get("QUESTION"));
						String fileName = (String) quest.get("QUESTION_IMAGE");

						if (fileName != null && fileName != "null" && !fileName.trim().equals("")) {
							question.setQuestionImageUrl(gcpClientUtil.getDownloaFiledUrl(fileName,
									Constants.GCP_QUESTIONNAIRE_QUESTION_IMAGE_PATH));
						}
						question.setAnswer((String) quest.get("ANSWER"));
						question.setQuestionTypeId((Integer) quest.get("QUESTION_TYPE_ID"));
						question.setQuestionType((String) quest.get("QUESTION_TYPE"));
						question.setQuestionOrder((Integer) quest.get("QUESTION_ORDER"));
						question.setIsMandatory(quest.get("IS_MANDATORY") != null
								&& (Integer) quest.get("IS_MANDATORY") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);

						question.setCeil((Integer) quest.get("SCALE_MAX"));
						question.setFloor((Integer) quest.get("SCALE_MIN"));
						question.setCeilDescription((String) quest.get("SCALE_MAX_DESC"));
						question.setFloorDescription((String) quest.get("SCALE_MIN_DESC"));
						question.setTickStep((Integer) quest.get("STEP_VALUE"));
						question.setIsVerticalScale(quest.get("IS_VERTICAL_SCALE") != null
								&& (Integer) quest.get("IS_VERTICAL_SCALE") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);
						question.setIsContinuousScale(quest.get("IS_CONTINUOUS_SCALE") != null
								&& (Integer) quest.get("IS_CONTINUOUS_SCALE") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);

						question.setQuestionAnswerOptions(
								questionAnsOptsMap.get(questionId) != null ? questionAnsOptsMap.get(questionId)
										: new ArrayList<>());

						Integer questionnaireId = (Integer) quest.get("QUESTIONNAIRE_ID");
						questionsMap.computeIfAbsent(questionnaireId, k -> new ArrayList<Question>()).add(question);
					});
				}

				if (key.equals(RESULT_SET_4)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(quesObj -> {
						Questionnaire questionnaire = new Questionnaire();
						Integer questionnaireId = (Integer) quesObj.get("QUESTIONNAIRE_ID");
						questionnaire.setStudyIds((String) quesObj.get("STUDY_ID"));

						questionnaire.setQuestionnaireId(questionnaireId);
						questionnaire.setQuestionnaireName((String) quesObj.get("QUESTIONNAIRE_NAME"));

						Date startDate = (Date) quesObj.get("START_DATE");
						questionnaire.setStartDate((startDate.toLocalDate()));

						Date endDate = (Date) quesObj.get("END_DATE");
						questionnaire.setEndDate((endDate.toLocalDate()));

						Long ansCount = (Long) quesObj.get("ANS_CNT");
						if (ansCount > 0) {
							questionnaire.setStatus("Submitted");
						} else {
							if (endDate.toLocalDate().isBefore(LocalDate.now())) {
								questionnaire.setStatus("Elapsed");
							} else {
								questionnaire.setStatus("Open");
							}
						}

						questionnaire.setQuestions(
								questionsMap.get(questionnaireId) != null ? questionsMap.get(questionnaireId)
										: new ArrayList<>());

						questionnaire.setInstructions(
								instructionsMap.get(questionnaireId) != null ? instructionsMap.get(questionnaireId)
										: new ArrayList<>());
						questionnaireList.add(questionnaire);
					});
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching getQuestionnaireAnswers", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return questionnaireList;
	}

	@Override
	public void saveQuestionAnswers(QuestionAnswerRequest questionAnswerRequest, Boolean fromDeprecated)
			throws ServiceExecutionException {
		LOGGER.debug("saveQuestionAnswers called");
		try {
			Questionnaire questionnaire = getQuestionnaireById(questionAnswerRequest.getQuestionnaireId());
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", questionAnswerRequest.getPetId());
			inputParams.put("p_pet_parent_id", questionAnswerRequest.getPetParentId());
			inputParams.put("p_questionnaire_id", questionAnswerRequest.getQuestionnaireId());
			inputParams.put("p_questionnaire_type_id", questionnaire.getQuestionnaireTypeId());
			inputParams.put("p_study_ids", questionAnswerRequest.getStudyIds());
			inputParams.put("p_question_answer_json",
					new ObjectMapper().writeValueAsString(questionAnswerRequest.getQuestionAnswers()));

			questionAnswerRequest.setQuestionnaireLevelId(questionnaire.getQuestionnaireLevelId());
			LOGGER.info("saveQuestionAnswers inputParams are {}", inputParams);
			Map<String, Object> outParams;
			if (fromDeprecated) {
				if (questionAnswerRequest.getQuestionnaireLevelId() != null
						&& questionAnswerRequest.getQuestionnaireLevelId() == QUESTIONNAIRE_LEVEL_PET_PARENT) {
					outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_PARENT_LEVEL_QUESTIONNAIRE_V1, inputParams);
				} else {
					outParams = callStoredProcedure(MOBILE_APP_INSERT_QUESTIONNAIRE_RESPONSE_V1, inputParams);
				}
			} else {
				if (questionAnswerRequest.getQuestionnaireLevelId() != null
						&& questionAnswerRequest.getQuestionnaireLevelId() == QUESTIONNAIRE_LEVEL_PET_PARENT) {
					outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_PARENT_LEVEL_QUESTIONNAIRE, inputParams);
				} else {
					outParams = callStoredProcedure(MOBILE_APP_INSERT_QUESTIONNAIRE_RESPONSE, inputParams);
				}
			}

			LOGGER.info("saveQuestionAnswers outParams are {}", outParams);
			// System.out.println(outParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				// getting the inserted flag value
				Integer questionnaireResponseId = (int) outParams.get("last_insert_id");
				LOGGER.info("Questionnaire answers has been saved successfully, questionnaireResponseId is ",
						questionnaireResponseId);
			} else {
				if (statusFlag == -2) {
					/*
					 * throw new ServiceExecutionException(
					 * "saveQuestionAnswers service validation failed cannot proceed further",
					 * Status.BAD_REQUEST.getStatusCode(), Arrays.asList(new
					 * WearablesError(WearablesErrorCode.QUESTIONNAIRE_NAME_ALREADY_EXISTS,
					 * questionnaireRequest.getQuestionnaire().getQuestionnaireName())));
					 */
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error("error while executing saveQuestionAnswers ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private Questionnaire getQuestionnaireById(int questionnaireId) throws ServiceExecutionException {
		LOGGER.debug("getQuestionnaireById called");
		Questionnaire questionnaire = new Questionnaire();
		try {
			// in params
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_questionnaire_id", questionnaireId);

			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(QUESTIONNAIRE_GET_BY_ID, inputParams);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(quesObj -> {
						questionnaire.setQuestionnaireId((Integer) quesObj.get("QUESTIONNAIRE_ID"));
						questionnaire.setQuestionnaireName((String) quesObj.get("QUESTIONNAIRE_NAME"));

						questionnaire.setQuestionnaireTypeId((Integer) quesObj.get("QUESTIONNAIRE_TYPE_ID"));

						questionnaire.setQuestionnaireLevelId(quesObj.get("QUESTIONNAIRE_LEVEL_ID") != null
								? (Integer) quesObj.get("QUESTIONNAIRE_LEVEL_ID")
								: null);

						Date startDate = (Date) quesObj.get("START_DATE");
						questionnaire.setStartDate((startDate.toLocalDate()));

						Date endDate = (Date) quesObj.get("END_DATE");
						questionnaire.setEndDate((endDate.toLocalDate()));
					});
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching getQuestionnaireById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return questionnaire;
	}

	@Override
	public PetCampaignPointsDTO getPetCampaignPoints(int petId) throws ServiceExecutionException {
		final PetCampaignPointsDTO petCampaignPointsDTO = new PetCampaignPointsDTO();
		LOGGER.debug("getPetCampaignPoints called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_ALL_CAMPAIGN_POINTS_BY_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					petCampaignPointsDTO.setTotalEarnedPoints(rs.getInt("TOTAL_EARNED_POINTS"));
					petCampaignPointsDTO.setImages(rs.getInt("TOTAL_IMAGES"));
					petCampaignPointsDTO.setObservations(rs.getInt("TOTAL_OBSERVATIONS"));
					petCampaignPointsDTO.setPointsUtilized(rs.getInt("POINTS_UTILIZED"));
					petCampaignPointsDTO.setQuestionnaire(rs.getInt("TOTAL_QUESIONARES"));
					petCampaignPointsDTO.setFeeback(rs.getInt("TOTAL_FEEDBACK"));
					petCampaignPointsDTO.setVideos(rs.getInt("TOTAL_VIDEOS"));
					petCampaignPointsDTO.setRedeemablePoints(rs.getInt("REMAINING_POINTS"));
				}
			}, petId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetCampaignPoints", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petCampaignPointsDTO;
	}

	@Override
	public List<PetCampaignPointsListDTO> getPetCampaignPointsList(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetCampaignPointsList called");
		List<PetCampaignPointsListDTO> campaignList = new ArrayList<>();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_CAMPAIGN_POINTS_LIST_BY_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetCampaignPointsListDTO campagin = new PetCampaignPointsListDTO();
					campagin.setPetCampaignPointsId(rs.getInt("TRACKER_PET_POINTS_ID"));
					campagin.setPetId(rs.getInt("PET_ID"));
					campagin.setPetName(rs.getString("PET_NAME"));
					campagin.setCampaignName(rs.getString("CAMPAIGN_NAME"));

					campagin.setQuestionareId(rs.getInt("QUESTIONNAIRE_ID"));
					campagin.setQuestionnaireName(rs.getString("QUESTIONNAIRE_NAME"));
					campagin.setFeedback(rs.getString("FEED_BACK_TEXT"));
					campagin.setStatusId(rs.getInt("STATUS_ID"));
					campagin.setStatus(rs.getString("STATUS"));

					String fileName = rs.getString("FILE_NAME");
					String imageURL = rs.getString("IMAGE_PATH");
					if (StringUtils.isNotBlank(imageURL)) {
						if (!StringUtils.contains(imageURL, "firebasestorage")) {
							String imageSignedUrl = gcpClientUtil.getDownloaFiledUrl(
									imageURL.concat("/").concat(fileName), Constants.GCP_OBSERVATION_PHOTO_PATH);
							campagin.setImageUrl(imageSignedUrl);
						} else {
							campagin.setImageUrl(imageURL);
						}
					}

					String videoURL = rs.getString("VIDEO_URL");
					if (StringUtils.isNotBlank(videoURL)) {
						if (!StringUtils.contains(videoURL, "firebasestorage")) {
							String mediaSignedUrl = gcpClientUtil.getDownloaFiledUrl(
									videoURL.replaceAll("https://storage.googleapis.com/wearables-portal-media/"
											+ environment + "/GCloud/WPortal/ObservationVideo/", ""),
									Constants.GCP_OBSERVATION_VIDEO_PATH);
							campagin.setVideoUrl(mediaSignedUrl);
						} else {
							campagin.setVideoUrl(videoURL);
						}
					}

					String videoThumbnailURL = rs.getString("VIDEO_THUMBNAIL");
					if (StringUtils.isNotBlank(videoThumbnailURL)) {
						if (!StringUtils.contains(videoThumbnailURL, "firebasestorage")) {
							String mediaSignedThumbUrl = gcpClientUtil.getDownloaFiledUrl(
									videoThumbnailURL
											.replaceAll("https://storage.googleapis.com/wearables-portal-media/"
													+ environment + "/GCloud/WPortal/ObservationVideoThumbnail/", ""),
									Constants.GCP_OBSERVATION_VIDEO_THUMBNAIL_PATH);
							campagin.setVideoThumbnailUrl(mediaSignedThumbUrl);
						} else {
							campagin.setVideoThumbnailUrl(videoThumbnailURL);
						}
					}

					Timestamp createdDate = (Timestamp) rs.getTimestamp("CREATED_DATE");
					campagin.setCreatedDate(createdDate != null ? createdDate.toLocalDateTime() : null);

					campagin.setObservation(rs.getString("OBSERVATION"));
					campagin.setActivityId(rs.getInt("ACTIVITY_ID"));
					campagin.setActivityName(rs.getString("ACTIVITY"));
					campagin.setBehaviourId(rs.getInt("BEHAVIOR_ID"));
					campagin.setBehaviourName(rs.getString("BEHAVIOR"));
					campagin.setPoints(rs.getInt("POINTS"));
					campaignList.add(campagin);
				}
			}, petId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetCampaignPointsList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return campaignList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getPetParentByPetParentKey(String petParentKey) throws ServiceExecutionException {
		LOGGER.debug("getPetParentByPetParentKey called");
		int petParentKeyId = 0;
		try {
			// in params
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_parent_key", petParentKey);

			LOGGER.info("getPetParentByPetParentKey inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_PET_PARENT_AUTH_BY_KEY, inputParams);
			LOGGER.info("getPetParentByPetParentKey outParams are {}", outParams);

			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					if (CollectionUtils.isNotEmpty(list)) {
						petParentKeyId = (Integer) list.get(NumberUtils.INTEGER_ZERO).get("PET_PARENT_ID");
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetParentByPetParentKey", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petParentKeyId;
	}

	@Override
	public List<Campaign> getCampaignListByPet(int petId) throws ServiceExecutionException {
		List<Campaign> campaignList = new ArrayList<>();
		LOGGER.debug("getCampaignListByPet called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_CAMPAIGNS_BY_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Campaign campaign = new Campaign();
					campaign.setCampaignId(rs.getInt("CAMPAIGN_ID"));
					campaign.setCampaignName(rs.getString("CAMPAIGN_NAME"));
					Date startDate = rs.getDate("START_DATE");
					campaign.setStartDate((startDate.toLocalDate()));

					Date endDate = rs.getDate("END_DATE");
					campaign.setEndDate((endDate.toLocalDate()));
					try {
						campaign.setActivityLimits(new ObjectMapper().readValue(rs.getString("ACTIVITY_DETAILS"),
								new TypeReference<List<ActivityLimit>>() {
								}));
					} catch (JsonProcessingException e) {
						LOGGER.error("error while converting ACTIVITY_DETAILS in getCampaignListByPet", e);
					}
					campaignList.add(campaign);
				}
			}, petId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getCampaignListByPet", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return campaignList;
	}

	@Override
	public List<LeaderBoard> getLeaderBoardByCampaignId(int campaignId) throws ServiceExecutionException {
		List<LeaderBoard> leaderBoards = new ArrayList<>();
		LOGGER.debug("getLeaderBoardByCampaignId called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_LEADER_BOARD_BY_CAMPAIGN, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					LeaderBoard leaderBoard = new LeaderBoard();
					leaderBoard.setCampaignId(rs.getInt("CAMPAIGN_ID"));
					leaderBoard.setCampaignName(rs.getString("CAMPAIGN_NAME"));
					leaderBoard.setPetId(rs.getInt("PET_ID"));
					leaderBoard.setPetName(rs.getString("PET_NAME"));
					if (rs.getString("PHOTO_NAME") != null && !rs.getString("PHOTO_NAME").isEmpty()) {
						String imageName = rs.getString("PHOTO_NAME");
						// String petPhotoUrl = photoPath + imageName;
						// leaderBoard.setPetPhotoUrl(petPhotoUrl);
						leaderBoard.setPetPhotoUrl(
								gcpClientUtil.getDownloaFiledUrl(imageName, Constants.GCP_PET_PHOTO_PATH));
					}

					leaderBoard.setPoints(rs.getInt("POINTS"));
					leaderBoard.setRank(rs.getInt("RANKING"));

					leaderBoards.add(leaderBoard);
				}
			}, campaignId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getLeaderBoardByCampaignId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return leaderBoards;
	}

	@Override
	public List<PetRedemptionHistoryDTO> getPetRedemptionHistory(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetRedemptionHistory called");
		List<PetRedemptionHistoryDTO> redemptionHistoryDTOs = new ArrayList<>();

		try {
			jdbcTemplate.query(MOBILE_APP_GET_REDEEMTION_HISTORY_BY_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetRedemptionHistoryDTO redemptionHistoryDTO = new PetRedemptionHistoryDTO();
					redemptionHistoryDTO.setPetId(rs.getInt("PET_ID"));
					redemptionHistoryDTO.setTotalPoints(rs.getInt("TOTAL_POINTS"));
					redemptionHistoryDTO.setPointsRedeemed(rs.getInt("POINTS_REDEEMED"));
					redemptionHistoryDTO.setBalancePoints(rs.getInt("POINTS_BALANCE"));

					redemptionHistoryDTO.setRedeemedByUser(rs.getString("FULL_NAME"));
					Timestamp createdDate = (Timestamp) rs.getTimestamp("CREATED_DATE");
					redemptionHistoryDTO.setCreatedDate(createdDate != null ? createdDate.toLocalDateTime() : null);

					redemptionHistoryDTOs.add(redemptionHistoryDTO);
				}
			}, petId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetRedemptionHistory", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return redemptionHistoryDTOs;
	}

	@Override
	public void assignSensorToPet(AssignSensorRequest assignSensorRequest) throws ServiceExecutionException {
		LOGGER.debug("assignSensorToPet called");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", assignSensorRequest.getPetId());
			inputParams.put("p_pet_parent_id", assignSensorRequest.getPetParentId());
			inputParams.put("p_device_number", assignSensorRequest.getDeviceNumber());
			inputParams.put("p_device_type", assignSensorRequest.getDeviceType());
			inputParams.put("p_assigned_date", assignSensorRequest.getAssignedDate());

			LOGGER.info("assignSensorToPet inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_ASSIGN_PET_SENSOR, inputParams);
			LOGGER.info("assignSensorToPet outParams are {}", outParams);

			// System.out.println(outParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				// getting the inserted flag value
				Integer outFlag = (int) outParams.get("out_flag");
				LOGGER.info("Sensor has been assigned successfully ", outFlag);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"assignSensorToPet service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.INVALID_DEVICE_NUMBER,
									assignSensorRequest.getDeviceNumber())));

				} else if (statusFlag == -3) {
					throw new ServiceExecutionException(
							"assignSensorToPet service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList(
									new WearablesError(WearablesErrorCode.INVALID_STUDY_VIRTUAL_STUDY_ALREADY_MAPPED)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing assignSensorToPet ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public List<PetDTO> getPetDevicesByPetParent(int petParentId) throws ServiceExecutionException {
		LOGGER.debug("getPetDevicesByPetParent called");
		List<PetDTO> petDevices = new ArrayList<>();
		Map<Integer, List<DeviceDTO>> devicesMap = new HashMap<>();
		long startTime = System.currentTimeMillis();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_DEVICES_BY_PET_PARENT_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetDTO petDTO = new PetDTO();
					petDTO.setPetID(rs.getInt("PET_ID"));
					petDTO.setPetName(rs.getString("PET_NAME"));
					String imageName = rs.getString("PHOTO_NAME");

					petDTO.setStudyId(rs.getInt("STUDY_ID"));
					petDTO.setStudyName(rs.getString("STUDY_NAME"));
					petDTO.setGoalDurationInMins(rs.getInt("FM_GOAL_DUR_MINS"));

					petDTO.setPetSpecQuesCount(rs.getInt("PET_SPEC_QUES_COUNT"));
					petDTO.setShowFmChart(rs.getBoolean("SHOW_FM_CHART"));
					petDTO.setShowFmGoalSetting(rs.getBoolean("SHOW_FM_GOAL_SETTING"));
					petDTO.setShowSleepChart(rs.getBoolean("SHOW_SLEEP_CHART"));

					petDTO.setIsPetWithPetParent(rs.getBoolean("IS_PET_WITH_PET_PARENT"));
					petDTO.setFeedingPreferences(rs.getString("FEEDING_PREFERENCES"));
					// Address petAddress = new Gson().fromJson(rs.getString("PET_ADDRESS_JSON"),
					// Address.class);
					Address petAddress = null;
					try {
						if (StringUtils.isNotEmpty(rs.getString("PET_ADDRESS_JSON"))) {
							petAddress = new ObjectMapper().readValue(rs.getString("PET_ADDRESS_JSON"), Address.class);
						}
					} catch (JsonProcessingException | SQLException e) {
						e.printStackTrace();
					}
					petDTO.setPetAddress(petAddress == null ? new Address() : petAddress);

					if (StringUtils.isNotEmpty(imageName)) {
						// String photoUrl = photoPath + imageName;
						// petDeviceDTO.setPhotoUrl(photoUrl);
						petDTO.setPhotoUrl(gcpClientUtil.getDownloaFiledUrl(imageName, Constants.GCP_PET_PHOTO_PATH));
					}
					if (StringUtils.isNotEmpty(rs.getString("weightData"))) {
						String[] weightData = rs.getString("weightData").split("###");
						if (("lbs").equals(weightData[Constants.NUMBER_TWO])) {
							petDTO.setWeight(weightData[Constants.NUMBER_ZERO]);
						} else {
							petDTO.setWeight(weightData[Constants.NUMBER_ONE]);
						}
						petDTO.setWeightUnit(weightData[Constants.NUMBER_TWO]);
					}
					petDTO.setPetAge(rs.getString("PET_AGE"));
					petDTO.setPetBreed(rs.getString("BREED_NAME"));
					petDTO.setSpeciesId(rs.getString("SPECIES_ID"));
					petDTO.setBirthday(rs.getString("BIRTHDAY"));
					petDTO.setGender(rs.getString("GENDER"));
					petDTO.setIsNeutered(rs.getBoolean("IS_NEUTERED"));
					petDTO.setIsPetEditable(rs.getInt("IS_PET_EDITABLE"));
					petDTO.setPetStatus(rs.getInt("PET_STATUS_ID"));

					petDTO.setBrandId(rs.getInt("BRAND_ID"));
					petDTO.setBrandName(rs.getString("BRAND_NAME"));
					petDTO.setFeedUnit(rs.getInt("FEED_UNIT"));
					petDTO.setFoodIntake(rs.getDouble("FOOd_INTAKE"));

					DeviceDTO deviceDto = new DeviceDTO();

					deviceDto.setDeviceId(rs.getInt("DEVICE_ID"));
					deviceDto.setDeviceNumber(rs.getString("DEVICE_NUMBER"));
					deviceDto.setDeviceType(rs.getString("DEVICE_TYPE"));
					deviceDto.setDeviceModel(rs.getString("DEVICE_MODEL"));
					deviceDto.setBattery(rs.getString("BATTERY"));
					deviceDto.setFirmware(rs.getString("FIRMWARE"));
					deviceDto.setFirmwareNew(rs.getString("FIRMWARE_NEW"));
					deviceDto.setIsDeviceSetupDone(rs.getBoolean("IS_DEVICE_SETUP_DONE"));
					deviceDto.setIsFirmwareVersionUpdateRequired(rs.getBoolean("IS_FIRMWARE_VERSION_UPDATE_REQUIRED"));
					deviceDto.setLastSeen(rs.getString("LAST_SEEN"));

					if (devicesMap.containsKey(petDTO.getPetID())) {
						if (deviceDto.getDeviceId() != 0) {
							devicesMap.get(petDTO.getPetID()).add(deviceDto);
						}
					} else {
						List<DeviceDTO> devices = new ArrayList<>();
						if (deviceDto.getDeviceId() != 0) {
							devices.add(deviceDto);
						}
						devicesMap.put(petDTO.getPetID(), devices);
						petDTO.setDevices(devicesMap.get(petDTO.getPetID()));
						petDevices.add(petDTO);
					}
				}
			}, petParentId);
			long endTime = System.currentTimeMillis();
			LOGGER.info("Time taken to execute getPetDevicesByPetParent service in millis is {}",
					(endTime - startTime));
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetDevicesByPetParent", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petDevices;
	}

	@Override
	public List<MobileAppFeedback> getFeedbackByPetParent(int petParentId) throws ServiceExecutionException {
		List<MobileAppFeedback> mobileAppFeedbackList = new ArrayList<>();
		LOGGER.debug("getFeedbackByPetParent called");
		try {
			jdbcTemplate.query(MOBILE_APP_FEEDBACK_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					MobileAppFeedback mobileAppFeedback = new MobileAppFeedback();

					// set the column values to fields like below
					mobileAppFeedback.setFeedBackId(rs.getInt("app_feedback_id"));
					mobileAppFeedback.setStudyName(rs.getString("study_name"));
					mobileAppFeedback.setPetOwnerName(rs.getString("pet_owner_name"));
					mobileAppFeedback.setPetName(rs.getString("pet_name"));
					mobileAppFeedback.setPageName(rs.getString("page_name"));
					mobileAppFeedback.setDeviceType(rs.getString("device_type"));
					mobileAppFeedback.setFeedback(rs.getString("feed_back_text"));
					mobileAppFeedback.setFeedbackDate(rs.getTimestamp("created_date").toLocalDateTime());
					mobileAppFeedbackList.add(mobileAppFeedback);
				}
			}, petParentId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getFeedbackByPetParent", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return mobileAppFeedbackList;
	}

	@Override
	public List<String> getDeviceTypes() throws ServiceExecutionException {
		List<String> deviceTypeList = new ArrayList<>();
		LOGGER.debug("getDeviceTypes called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_DEVICE_TYPES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet resultSet) throws SQLException {
					deviceTypeList.add(resultSet.getString("DEVICE_TYPE"));
				}
			});
		} catch (Exception e) {
			LOGGER.error("error while fetching getDeviceTypes ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceTypeList;
	}

	@Override
	public List<String> getDeviceModels(String deviceType) throws ServiceExecutionException {
		List<String> deviceModelList = new ArrayList<>();
		LOGGER.debug("getDeviceModels called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_DEVICE_MODELS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet resultSet) throws SQLException {
					deviceModelList.add(resultSet.getString("DEVICE_MODEL"));
				}
			}, deviceType);
		} catch (Exception e) {
			LOGGER.error("error while fetching getDeviceModels ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceModelList;
	}

	@Override
	public TimeZone getTimeZoneDetails(String timeZoneName) throws ServiceExecutionException {
		TimeZone timeZone = new TimeZone();
		LOGGER.debug("getTimeZoneDetails called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_TIMEZONE_BY_NAME, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					timeZone.setTimeZoneId(rs.getInt("TIMEZONE_ID"));
					timeZone.setTimeZoneName(rs.getString("STANDARD_NAME"));
					timeZone.setIsSupportDST(rs.getInt("IS_SUPPORTS_DAY_LIGHT_SAVING_TIME"));
				}
			}, timeZoneName);
		} catch (Exception e) {
			LOGGER.error("error while fetching getTimeZoneDetails", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return timeZone;
	}

	@Override
	public Study getStudyByPetId(int petId) throws ServiceExecutionException {
		Study study = new Study();
		LOGGER.debug("getStudyByPetId called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_STUDY_INFO_BY_PET_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					study.setStudyId(rs.getInt("TIMEZONE_ID"));
					study.setStudyName(rs.getString("TIMEZONE_ID"));
					study.setStatus(rs.getInt("IS_ACTIVE") == 1 ? Boolean.TRUE : Boolean.FALSE);

					Timestamp startDate = (Timestamp) rs.getTimestamp("START_DATE");
					study.setStartDate(startDate != null ? startDate.toLocalDateTime() : null);

					Timestamp endDate = (Timestamp) rs.getTimestamp("END_DATE");
					study.setEndDate(endDate != null ? endDate.toLocalDateTime() : null);
				}
			}, petId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getStudyByPetId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return study;
	}

	@Override
	public Address getPetParentAddressByPetParent(int petParentId) throws ServiceExecutionException {
		Address address = new Address();
		LOGGER.debug("getPetParentAddressByPetParent called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_ADDRESS_BY_PET_PARENT_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {

					address.setAddressId(rs.getInt("PET_PARENT_ADDRESS_ID"));
					address.setAddress1(rs.getString("ADDRESS_1"));
					address.setAddress2(rs.getString("ADDRESS_2"));
					address.setCity(rs.getString("CITY"));
					address.setState(rs.getString("STATE"));
					address.setCountry(rs.getString("COUNTRY"));
					address.setZipCode(rs.getString("ZIP_CODE"));
					address.setTimeZone(rs.getString("TIME_ZONE"));
					address.setTimeZoneId(rs.getInt("TIMEZONE_ID"));
					address.setIsPreludeAddress(rs.getInt("IS_PRELUDE_ADDRESS"));

					Date fromDate = rs.getDate("FROM_DATE");
					address.setDateFrom(fromDate != null ? fromDate.toLocalDate() : null);

					Date toDate = rs.getDate("TO_DATE");
					address.setDateTo(toDate != null ? toDate.toLocalDate() : null);

				}
			}, petParentId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetParentAddressByPetParent", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return address;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PetParentAddressResponse getPetParentAddressHistoryById(int petParentId) throws ServiceExecutionException {
		PetParentAddressResponse response = new PetParentAddressResponse();
		List<Address> residentialAddressList = new ArrayList<Address>();
		List<Address> shippingAddressList = new ArrayList<Address>();
		LOGGER.debug("getPetParentAddressHistoryById called");

		try {

			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_parent_id", petParentId);

			LOGGER.info("getPetParentAddressHistoryById inputParams are {}", inputParams);
			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(MOBILE_APP_PET_PARENT_GET_ADDRESSES_BY_ID,
					inputParams);
			LOGGER.info("getPetParentAddressHistoryById simpleJdbcCallResult are {}", simpleJdbcCallResult);

			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(address -> {
						Address residentialAddress = new Address();
						residentialAddress.setAddressId((Integer) address.get("PET_PARENT_ADDRESS_ID"));
						residentialAddress.setAddress1((String) address.get("ADDRESS_1"));
						residentialAddress.setAddress2((String) address.get("ADDRESS_2"));
						residentialAddress.setCity((String) address.get("CITY"));
						residentialAddress.setState((String) address.get("STATE"));
						residentialAddress.setCountry((String) address.get("COUNTRY"));
						residentialAddress.setZipCode((String) address.get("ZIP_CODE"));
						residentialAddress.setTimeZone((String) address.get("TIME_ZONE"));
						residentialAddress.setTimeZoneId((Integer) address.get("TIMEZONE_ID"));

						Date fromDate = (Date) address.get("FROM_DATE");
						residentialAddress.setDateFrom(fromDate != null ? fromDate.toLocalDate() : null);

						Date toDate = (Date) address.get("TO_DATE");
						residentialAddress.setDateTo(toDate != null ? toDate.toLocalDate() : null);

						residentialAddressList.add(residentialAddress);
					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(address -> {
						Address shippingAddress = new Address();
						shippingAddress.setAddressId((Integer) address.get("PET_PARENT_ADDRESS_ID"));
						shippingAddress.setAddress1((String) address.get("ADDRESS_1"));
						shippingAddress.setAddress2((String) address.get("ADDRESS_2"));
						shippingAddress.setCity((String) address.get("CITY"));
						shippingAddress.setState((String) address.get("STATE"));
						shippingAddress.setCountry((String) address.get("COUNTRY"));
						shippingAddress.setZipCode((String) address.get("ZIP_CODE"));
						shippingAddress.setTimeZone((String) address.get("TIME_ZONE"));
						shippingAddress.setTimeZoneId((Integer) address.get("TIMEZONE_ID"));

						Date fromDate = (Date) address.get("FROM_DATE");
						shippingAddress.setDateFrom(fromDate != null ? fromDate.toLocalDate() : null);

						Date toDate = (Date) address.get("TO_DATE");
						shippingAddress.setDateTo(toDate != null ? toDate.toLocalDate() : null);

						shippingAddressList.add(shippingAddress);
					});
				}
			}
			response.setResidentialAddressList(residentialAddressList);
			response.setShippingAddressList(shippingAddressList);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetParentAddressHistoryById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return response;
	}

	@Override
	public String validateDuplicatePet(ValidateDuplicatePetRequest petRequest) throws ServiceExecutionException {
		LOGGER.debug("validateDuplicatePet called");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_name", petRequest.getPetName());
			inputParams.put("p_breed_id", petRequest.getBreedId());
			inputParams.put("p_gender", petRequest.getGender());
			inputParams.put("p_pet_parent_id", petRequest.getPetParentId());

			LOGGER.info("validateDuplicatePet inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_PET_VALIDATE_DUPLICATE_PET, inputParams);
			LOGGER.info("validateDuplicatePet outParams are {}", outParams);
			String outFlag = (String) outParams.get("out_error_msg");
			return outFlag;
		} catch (SQLException e) {
			LOGGER.error("error while executing validateDuplicatePet ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public AppVersion getAppLatestVersion(int appOSId, String appVersion) throws ServiceExecutionException {
		AppVersion currentAppVersion = new AppVersion();
		LOGGER.debug("getAppLatestVersion called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_APP_CURRENT_VERSION_BY_APP_OS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					currentAppVersion.setAppVersion(rs.getString("MOB_APP_VERSION"));
					int isForceUpdate = rs.getInt("IS_FORCE_UPDATE");

					if (isForceUpdate == 1) {
						currentAppVersion.setIsForceUpdate(Boolean.TRUE);
					} else {
						currentAppVersion.setIsForceUpdate(Boolean.FALSE);
					}

					int appOperatingSystem = rs.getInt("MOB_OS");

					if (appOperatingSystem == 1) {
						currentAppVersion.setAppOperatingSystem("iOS");
					} else {
						currentAppVersion.setAppOperatingSystem("Android");
					}

					currentAppVersion.setVersionDescription(rs.getString("VERSION_DESCRIPTION"));
				}
			}, appOSId, appVersion);
		} catch (Exception e) {
			LOGGER.error("error while fetching getAppLatestVersion", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return currentAppVersion;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public User getUserInfoById(int userId) throws ServiceExecutionException {
		User user = new User();
		Address address = new Address();
		LOGGER.debug("getUserInfoById called");
		try {
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_user_id", userId);

			LOGGER.info("getUserInfoById inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_GET_USER_INFO_BY_USER_ID, inputParams);
			LOGGER.info("getUserInfoById outParams are {}", outParams);
			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(userInfo -> {
						user.setUserId((Integer) userInfo.get("USER_ID"));
						user.setEmail((String) userInfo.get("EMAIL"));
						user.setPhoneNumber((String) userInfo.get("PHONE_NUMBER"));
						user.setFullName((String) userInfo.get("FULL_NAME"));
						user.setFirstName((String) userInfo.get("FIRST_NAME"));
						user.setLastName((String) userInfo.get("LAST_NAME"));
						user.setSecondaryEmail((String) userInfo.get("SECONDARY_EMAIL"));

						Long weightUnitId = (Long) userInfo.get("PREF_WEIGHT_UNIT_ID");
						user.setPreferredWeightUnitId(weightUnitId == null ? 1 : weightUnitId.intValue());
						user.setPreferredWeightUnit((String) userInfo.get("PREF_WEIGHT_UNIT"));

						Long foodUnitId = (Long) userInfo.get("PREF_FOOD_REC_UNIT_ID");
						user.setPreferredFoodRecUnitId(foodUnitId == null ? 4 : foodUnitId.intValue());
						user.setPreferredFoodRecUnit((String) userInfo.get("PREF_FOOD_REC_UNIT"));

						String foodRecTime = (String) userInfo.get("PREF_RECOMMENDATION_TIME");
						user.setPreferredFoodRecTime(foodRecTime == null ? "7:30" : foodRecTime);

					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(addressInfo -> {
						address.setAddressId((Integer) addressInfo.get("PET_PARENT_ADDRESS_ID"));
						address.setAddress1((String) addressInfo.get("ADDRESS_1"));
						address.setAddress2((String) addressInfo.get("ADDRESS_2"));
						address.setCity((String) addressInfo.get("CITY"));
						address.setState((String) addressInfo.get("STATE"));
						address.setCountry((String) addressInfo.get("COUNTRY"));
						address.setZipCode((String) addressInfo.get("ZIP_CODE"));
						address.setTimeZone((String) addressInfo.get("TIME_ZONE"));
						address.setTimeZoneId((Integer) addressInfo.get("TIMEZONE_ID"));
						address.setIsPreludeAddress((Integer) addressInfo.get("IS_PRELUDE_ADDRESS"));
					});
				}
				user.setAddress(address);
			}

		} catch (Exception e) {
			LOGGER.error("error while fetching getUserInfoById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return user;
	}

	@Override
	public List<MeasurementUnit> getMeasurementUnits(String unitCategory) throws ServiceExecutionException {
		List<MeasurementUnit> measurementUnits = new ArrayList<>();
		LOGGER.debug("getMeasurementUnits called");
		try {
			jdbcTemplate.query(LU_GET_MEASUREMENT_UNITS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					MeasurementUnit measurementUnit = new MeasurementUnit();
					measurementUnit.setUnitId(rs.getInt("UNIT_ID"));
					measurementUnit.setUnitCategory(rs.getString("UNIT_CATEGORY"));
					measurementUnit.setUnit(rs.getString("UNIT"));
					measurementUnit.setUnitAbbrevation(rs.getString("UNIT_ABBREVATION"));
					measurementUnits.add(measurementUnit);
				}
			}, unitCategory);
		} catch (Exception e) {
			LOGGER.error("error while fetching getMeasurementUnits", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return measurementUnits;
	}
}
