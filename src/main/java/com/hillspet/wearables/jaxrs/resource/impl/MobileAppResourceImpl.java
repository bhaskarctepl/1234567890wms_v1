
/**
 * Created Date: 08-Dec-2020
 * Class Name  : CustomerSupportImpl.java
 * Description : Description of the package.
 *
 * © Copyright 2020 Cambridge Technology Enterprises(India) Pvt. Ltd.,All rights reserved.
 *
 * * * * * * * * * * * * * * * Change History *  * * * * * * * * * * *
 * <Defect Tag>        <Author>        <Date>        <Comments on Change>
 * ID                sgorle          08-Dec-2020        Mentions the comments on change, for the new file it's not required.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.hillspet.wearables.jaxrs.resource.impl;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hillspet.wearables.common.builders.JaxrsJsonResponseBuilder;
import com.hillspet.wearables.common.constants.WearablesErrorCode;
import com.hillspet.wearables.common.dto.WearablesError;
import com.hillspet.wearables.common.exceptions.ServiceValidationException;
import com.hillspet.wearables.common.response.SuccessResponse;
import com.hillspet.wearables.common.utils.GoogleTimeZoneUtil;
import com.hillspet.wearables.dto.AddressFilter;
import com.hillspet.wearables.dto.Campaign;
import com.hillspet.wearables.dto.LeaderBoard;
import com.hillspet.wearables.dto.MeasurementUnit;
import com.hillspet.wearables.dto.MobileAppFeedback;
import com.hillspet.wearables.dto.PetCampaignPointsDTO;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetRedemptionHistoryDTO;
import com.hillspet.wearables.dto.Questionnaire;
import com.hillspet.wearables.dto.Study;
import com.hillspet.wearables.dto.User;
import com.hillspet.wearables.jaxrs.resource.MobileAppResource;
import com.hillspet.wearables.objects.common.response.CommonResponse;
import com.hillspet.wearables.request.AssignSensorRequest;
import com.hillspet.wearables.request.QuestionAnswerRequest;
import com.hillspet.wearables.request.ValidateAddressRequest;
import com.hillspet.wearables.response.AppVersion;
import com.hillspet.wearables.response.AppVersionResponse;
import com.hillspet.wearables.response.CampaignListResponse;
import com.hillspet.wearables.response.LeaderBoardResponse;
import com.hillspet.wearables.response.MeasurementUnitResponse;
import com.hillspet.wearables.response.MobileAppFeedbackResponse;
import com.hillspet.wearables.response.PetCampaignListResponse;
import com.hillspet.wearables.response.PetCampaignResponse;
import com.hillspet.wearables.response.PetDevicesResponse;
import com.hillspet.wearables.response.PetParentAddressResponse;
import com.hillspet.wearables.response.PetRedemptionHistoryResponse;
import com.hillspet.wearables.response.QuestionnaireListResponse;
import com.hillspet.wearables.response.StudyResponse;
import com.hillspet.wearables.response.TimeZoneResponse;
import com.hillspet.wearables.response.UserResponse;
import com.hillspet.wearables.service.questionnaire.MobileAppService;

/**
 * Enter detailed explanation of the class here..
 * <p>
 * This class implementation of the <tt>Interface or class Name</tt> interface
 * or class. In addition to implementing the <tt>Interface Name</tt> interface,
 * this class provides methods to do other operations. (Mention other methods
 * purpose)
 *
 * <p>
 * More Description about the class need to be entered here.
 *
 * @author vvodyaram
 * @version Wearables Portal Relase Version..
 * @since Available Since Wearables Portal Version.
 * @see New line seperated Classes or Interfaces related to this class.
 */
@Service
public class MobileAppResourceImpl implements MobileAppResource {

	private static final Logger LOGGER = LogManager.getLogger(MobileAppResourceImpl.class);

	@Autowired
	private MobileAppService mobileAppService;

	@Autowired
	private GoogleTimeZoneUtil googleTimeZoneUtil;

	@Autowired
	private JaxrsJsonResponseBuilder responseBuilder;

	private int authorizeClientToken(String token) {
		int userId = mobileAppService.getPetParentByPetParentKey(token);
		if (userId == 0) {
			throw new ServiceValidationException(
					"authorizeClientToken service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}
		return userId;
	}

	@Override
	public Response getFeedbackQuestionnaireByPetId(int petId, String deviceModel, String isDateSupported,
			String token) {
		authorizeClientToken(token);
		List<Questionnaire> questionnaireList = mobileAppService.getFeedbackQuestionnaireByPetId(petId, deviceModel,
				isDateSupported);
		QuestionnaireListResponse response = new QuestionnaireListResponse();
		response.setQuestionnaireList(questionnaireList);
		SuccessResponse<QuestionnaireListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getQuestionnaireByPetId(int petId, String isDateSupported, String token) {
		authorizeClientToken(token);
		List<Questionnaire> questionnaireList = mobileAppService.getQuestionnaireByPetId(petId, isDateSupported);
		QuestionnaireListResponse response = new QuestionnaireListResponse();
		response.setQuestionnaireList(questionnaireList);
		SuccessResponse<QuestionnaireListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getQuestionnaireAnswers(int petId, int questionnaireId, String token) {
		authorizeClientToken(token);
		List<Questionnaire> questionnaireList = mobileAppService.getQuestionnaireAnswers(petId, questionnaireId);
		QuestionnaireListResponse response = new QuestionnaireListResponse();
		response.setQuestionnaireList(questionnaireList);
		SuccessResponse<QuestionnaireListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response saveQuestionAnswers_v1(QuestionAnswerRequest questionAnswerRequest, String token) {
		authorizeClientToken(token);
		mobileAppService.saveQuestionAnswers(questionAnswerRequest, Boolean.TRUE);
		CommonResponse response = new CommonResponse();
		response.setMessage("Your answers has been saved successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response saveQuestionAnswers_v2(QuestionAnswerRequest questionAnswerRequest, String token) {
		authorizeClientToken(token);
		mobileAppService.saveQuestionAnswers(questionAnswerRequest, Boolean.FALSE);
		CommonResponse response = new CommonResponse();
		response.setMessage("Your answers has been saved successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetCampaignPoints(int petId, String token) {
		authorizeClientToken(token);
		PetCampaignPointsDTO petCampaignDTO = mobileAppService.getPetCampaignPoints(petId);
		PetCampaignResponse response = new PetCampaignResponse();
		response.setPetCampaign(petCampaignDTO);
		SuccessResponse<PetCampaignResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetCampaignPointsList(int petId, String token) {
		authorizeClientToken(token);
		PetCampaignListResponse response = mobileAppService.getPetCampaignPointsList(petId);
		SuccessResponse<PetCampaignListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getCampaignListByPet(int petId, String token) {
		authorizeClientToken(token);
		List<Campaign> campaigns = mobileAppService.getCampaignListByPet(petId);
		CampaignListResponse response = new CampaignListResponse();
		response.setCampaigns(campaigns);
		SuccessResponse<CampaignListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getLeaderBoardByCampaignId(int campaignId, int petId, String token) {
		authorizeClientToken(token);
		List<LeaderBoard> leaderBoards = mobileAppService.getLeaderBoardByCampaignId(campaignId);
		LeaderBoardResponse response = new LeaderBoardResponse();

		response.setCurrentPet(
				leaderBoards.stream().filter(obj -> obj.getPetId().equals(petId)).findFirst().orElse(null));

		response.setLeaderBoards(leaderBoards);
		SuccessResponse<LeaderBoardResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetRedemptionHistory(int petId, String token) {
		authorizeClientToken(token);
		List<PetRedemptionHistoryDTO> redemptionHistoryDTOs = mobileAppService.getPetRedemptionHistory(petId);
		PetRedemptionHistoryResponse response = new PetRedemptionHistoryResponse();
		response.setRedemptionHistoryList(redemptionHistoryDTOs);

		SuccessResponse<PetRedemptionHistoryResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response assignSensorToPet(AssignSensorRequest assignSensorRequest, String token) {
		authorizeClientToken(token);
		mobileAppService.assignSensorToPet(assignSensorRequest);
		CommonResponse response = new CommonResponse();
		response.setMessage("Sensor has been assigned successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetDevicesByPetParent(int petParentId, String token) {
		authorizeClientToken(token);
		List<PetDTO> petDevices = mobileAppService.getPetDevicesByPetParent(petParentId);
		PetDevicesResponse response = new PetDevicesResponse();
		response.setPetDevices(petDevices);

		SuccessResponse<PetDevicesResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getFeedbackByPetParent(int petParentId, String token) {
		LOGGER.debug("getFeedbackByPetParent called");
		authorizeClientToken(token);
		List<MobileAppFeedback> feedbacks = mobileAppService.getFeedbackByPetParent(petParentId);
		MobileAppFeedbackResponse response = new MobileAppFeedbackResponse();
		response.setMobileAppFeeback(feedbacks);

		SuccessResponse<MobileAppFeedbackResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getDeviceTypes(String token) {
		LOGGER.debug("getDeviceTypes called");
		authorizeClientToken(token);
		List<String> deviceTypeList = mobileAppService.getDeviceTypes();
		SuccessResponse<List<String>> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(deviceTypeList);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getDeviceModels(String deviceType, String token) {
		LOGGER.debug("getDeviceModels called");
		authorizeClientToken(token);
		List<String> deviceModelList = mobileAppService.getDeviceModels(deviceType);
		SuccessResponse<List<String>> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(deviceModelList);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response validateAddress(AddressFilter addressFilter) {
		LOGGER.info("validateAddress method in PetParentResourceImpl {}", addressFilter.getZipCode());

		TimeZoneResponse response = googleTimeZoneUtil.getTimeZoneByZipcode(addressFilter);
		SuccessResponse<TimeZoneResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response validateUserAddress(ValidateAddressRequest validateAddressRequest) {
		LOGGER.info("validateUserAddress method in PetParentResourceImpl {}", validateAddressRequest.getAddress());

		TimeZoneResponse response = googleTimeZoneUtil.getTimeZoneByAddress(validateAddressRequest.getAddress());
		SuccessResponse<TimeZoneResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getStudyByPetId(int petId, String token) {
		LOGGER.debug("getStudyByPetId called");
		authorizeClientToken(token);
		Study study = mobileAppService.getStudyByPetId(petId);
		StudyResponse response = new StudyResponse();
		response.setStudy(study);

		SuccessResponse<StudyResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetParentAddressHistoryById(int petParentId, String token) {
		LOGGER.debug("getPetParentAddressHistoryById called");
		authorizeClientToken(token);

		PetParentAddressResponse response = mobileAppService.getPetParentAddressHistoryById(petParentId);
		SuccessResponse<PetParentAddressResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getAppLatestVersion(int appOSId, String appVersion) {
		LOGGER.debug("getAppLatestVersion called");

		AppVersion currentAppVersion = mobileAppService.getAppLatestVersion(appOSId, appVersion);
		AppVersionResponse response = new AppVersionResponse();
		response.setAppVersion(currentAppVersion);

		SuccessResponse<AppVersionResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getProfileInfo(String token) {
		LOGGER.debug("getProfileInfo called");
		int userId = authorizeClientToken(token);

		User user = mobileAppService.getUserInfoById(userId);
		UserResponse response = new UserResponse();
		response.setUser(user);

		SuccessResponse<UserResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getMeasurementUnits(String unitCategory, String token) {
		LOGGER.info("getMeasurementUnits called");
		authorizeClientToken(token);

		List<MeasurementUnit> measurementUnits = mobileAppService.getMeasurementUnits(unitCategory);
		MeasurementUnitResponse response = new MeasurementUnitResponse();
		response.setMeasurementUnits(measurementUnits);
		SuccessResponse<MeasurementUnitResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}
}
