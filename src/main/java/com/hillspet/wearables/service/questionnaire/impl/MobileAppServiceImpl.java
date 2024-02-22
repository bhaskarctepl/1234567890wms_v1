
/**
 * Created Date: 08-Jan-2021
 * Class Name  : QuestionnaireServiceImpl.java
 * Description : Description of the package.
 *
 * © Copyright 2020 Cambridge Technology Enterprises(India) Pvt. Ltd.,All rights reserved.
 *
 * * * * * * * * * * * * * * * Change History *  * * * * * * * * * * *
 * <Defect Tag>        <Author>        <Date>        <Comments on Change>
 * ID                sgorle          08-Jan-2021        Mentions the comments on change, for the new file it's not required.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.hillspet.wearables.service.questionnaire.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dao.questionnaire.MobileAppDao;
import com.hillspet.wearables.dto.Address;
import com.hillspet.wearables.dto.Campaign;
import com.hillspet.wearables.dto.LeaderBoard;
import com.hillspet.wearables.dto.MeasurementUnit;
import com.hillspet.wearables.dto.MobileAppFeedback;
import com.hillspet.wearables.dto.PetCampaignPointsDTO;
import com.hillspet.wearables.dto.PetCampaignPointsListDTO;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetRedemptionHistoryDTO;
import com.hillspet.wearables.dto.Questionnaire;
import com.hillspet.wearables.dto.Study;
import com.hillspet.wearables.dto.TimeZone;
import com.hillspet.wearables.dto.User;
import com.hillspet.wearables.request.AssignSensorRequest;
import com.hillspet.wearables.request.QuestionAnswerRequest;
import com.hillspet.wearables.request.ValidateDuplicatePetRequest;
import com.hillspet.wearables.response.AppVersion;
import com.hillspet.wearables.response.PetCampaignListResponse;
import com.hillspet.wearables.response.PetParentAddressResponse;
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
 * @version Wearables Portal Release Version..
 * @since Available Since Wearables Portal Version.
 * @see New line seperated Classes or Interfaces related to this class.
 */
@Service
public class MobileAppServiceImpl implements MobileAppService {

	private static final Logger LOGGER = LogManager.getLogger(MobileAppServiceImpl.class);

	@Autowired
	private MobileAppDao mobileAppDao;

	@Override
	public List<Questionnaire> getFeedbackQuestionnaireByPetId(int petId, String deviceModel, String isDateSupported)
			throws ServiceExecutionException {
		LOGGER.debug("getFeedbackQuestionnaireByPetId called");
		List<Questionnaire> questionnaireList = mobileAppDao.getFeedbackQuestionnaireByPetId(petId, deviceModel,
				isDateSupported);
		questionnaireList.stream().filter(e -> e.getQuestions().size() > 0).collect(Collectors.toList());
		LOGGER.debug("getFeedbackQuestionnaireByPetId completed successfully");
		return questionnaireList;
	}

	@Override
	public List<Questionnaire> getQuestionnaireByPetId(int petId, String isDateSupported)
			throws ServiceExecutionException {
		LOGGER.debug("getQuestionnaireByPetId called");
		List<Questionnaire> questionnaireList = mobileAppDao.getQuestionnaireByPetId(petId, isDateSupported);
		questionnaireList.stream().filter(e -> e.getQuestions().size() > 0).collect(Collectors.toList());
		LOGGER.debug("getQuestionnaireByPetId completed successfully");
		return questionnaireList;
	}

	@Override
	public List<Questionnaire> getQuestionnaireAnswers(int petId, int questionnaireId)
			throws ServiceExecutionException {
		LOGGER.debug("getQuestionnaireAnswers called");
		List<Questionnaire> questionnaireList = mobileAppDao.getQuestionnaireAnswers(petId, questionnaireId);
		LOGGER.debug("getQuestionnaireAnswers completed successfully");
		return questionnaireList;
	}

	@Override
	public void saveQuestionAnswers(QuestionAnswerRequest questionAnswerRequest, Boolean fromDeprecated)
			throws ServiceExecutionException {
		LOGGER.debug("saveQuestionAnswers called");
		mobileAppDao.saveQuestionAnswers(questionAnswerRequest, fromDeprecated);
		LOGGER.debug("saveQuestionAnswers completed successfully");
	}

	@Override
	public PetCampaignPointsDTO getPetCampaignPoints(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetCampaignPoints called");
		PetCampaignPointsDTO petDTO = mobileAppDao.getPetCampaignPoints(petId);
		LOGGER.debug("getPetCampaignPoints completed successfully");
		return petDTO;
	}

	@Override
	public PetCampaignListResponse getPetCampaignPointsList(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetCampaignPointsList called");
		List<PetCampaignPointsListDTO> campaignList = mobileAppDao.getPetCampaignPointsList(petId);
		PetCampaignListResponse response = new PetCampaignListResponse();
		response.setPetCampaignList(campaignList);
		LOGGER.debug("getPetCampaignPointsList end");
		return response;
	}

	@Override
	public int getPetParentByPetParentKey(String petParentKey) throws ServiceExecutionException {
		LOGGER.debug("getPetParentByPetParentKey called");
		int petParentKeyId = mobileAppDao.getPetParentByPetParentKey(petParentKey);
		LOGGER.debug("getPetParentByPetParentKey end");
		return petParentKeyId;
	}

	@Override
	public List<Campaign> getCampaignListByPet(int petId) throws ServiceExecutionException {
		LOGGER.debug("getCampaignListByPet called");
		List<Campaign> campaigns = mobileAppDao.getCampaignListByPet(petId);
		LOGGER.debug("getCampaignListByPet end");
		return campaigns;
	}

	@Override
	public List<LeaderBoard> getLeaderBoardByCampaignId(int campaignId) throws ServiceExecutionException {
		LOGGER.debug("getLeaderBoardByCampaignId called");
		List<LeaderBoard> leaderBoards = mobileAppDao.getLeaderBoardByCampaignId(campaignId);
		LOGGER.debug("getLeaderBoardByCampaignId end");
		return leaderBoards;
	}

	@Override
	public List<PetRedemptionHistoryDTO> getPetRedemptionHistory(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetRedemptionHistory called");
		List<PetRedemptionHistoryDTO> pRedemptionHistoryDTOs = mobileAppDao.getPetRedemptionHistory(petId);
		LOGGER.debug("getPetRedemptionHistory end");
		return pRedemptionHistoryDTOs;
	}

	@Override
	public void assignSensorToPet(AssignSensorRequest assignSensorRequest) throws ServiceExecutionException {
		LOGGER.debug("assignSensorToPet called");
		mobileAppDao.assignSensorToPet(assignSensorRequest);
		LOGGER.debug("assignSensorToPet completed successfully");
	}

	@Override
	public List<PetDTO> getPetDevicesByPetParent(int petParentId) throws ServiceExecutionException {
		LOGGER.debug("getPetDevicesByPetParent called");
		List<PetDTO> petDevices = mobileAppDao.getPetDevicesByPetParent(petParentId);
		LOGGER.debug("getPetDevicesByPetParent end");
		return petDevices;
	}

	@Override
	public List<MobileAppFeedback> getFeedbackByPetParent(int petParentId) throws ServiceExecutionException {
		LOGGER.debug("getFeedbackByPetParent called");
		List<MobileAppFeedback> petDevices = mobileAppDao.getFeedbackByPetParent(petParentId);
		LOGGER.debug("getFeedbackByPetParent end");
		return petDevices;
	}

	@Override
	public List<String> getDeviceTypes() throws ServiceExecutionException {
		LOGGER.debug("getDeviceTypes called");
		List<String> deviceTypeList = mobileAppDao.getDeviceTypes();
		LOGGER.debug("getDeviceTypes list", deviceTypeList.size());
		return deviceTypeList;
	}

	@Override
	public List<String> getDeviceModels(String deviceType) throws ServiceExecutionException {
		LOGGER.debug("getDeviceModels called");
		List<String> deviceTypeList = mobileAppDao.getDeviceModels(deviceType);
		LOGGER.debug("getDeviceModels list", deviceTypeList.size());
		return deviceTypeList;
	}

	@Override
	public TimeZone getTimeZoneDetails(String timeZoneName) throws ServiceExecutionException {
		LOGGER.debug("getTimeZoneDetails called");
		TimeZone timeZone = mobileAppDao.getTimeZoneDetails(timeZoneName);
		LOGGER.debug("getTimeZoneDetails end");
		return timeZone;
	}

	@Override
	public Study getStudyByPetId(int petId) throws ServiceExecutionException {
		LOGGER.debug("getStudyByPetId called");
		Study study = mobileAppDao.getStudyByPetId(petId);
		LOGGER.debug("getStudyByPetId end");
		return study;
	}

	@Override
	public Address getPetParentAddressByPetParent(int petParentId) throws ServiceExecutionException {
		LOGGER.debug("getPetParentAddressByPetParent called");
		Address address = mobileAppDao.getPetParentAddressByPetParent(petParentId);
		LOGGER.debug("getPetParentAddressByPetParent end");
		return address;
	}

	@Override
	public PetParentAddressResponse getPetParentAddressHistoryById(int petParentId) throws ServiceExecutionException {
		LOGGER.debug("getPetParentAddressHistoryById called");
		PetParentAddressResponse petParentAddresses = mobileAppDao.getPetParentAddressHistoryById(petParentId);
		LOGGER.debug("getPetParentAddressHistoryById completed successfully");
		return petParentAddresses;
	}

	@Override
	public String validateDuplicatePet(ValidateDuplicatePetRequest petRequest) throws ServiceExecutionException {
		LOGGER.debug("validateDuplicatePet called");
		return mobileAppDao.validateDuplicatePet(petRequest);
	}

	@Override
	public AppVersion getAppLatestVersion(int appOSId, String appVersion) throws ServiceExecutionException {
		LOGGER.debug("getAppLatestVersion called");
		AppVersion currentAppVersion = mobileAppDao.getAppLatestVersion(appOSId, appVersion);
		LOGGER.debug("getAppLatestVersion completed successfully");
		return currentAppVersion;
	}

	@Override
	public User getUserInfoById(int userId) throws ServiceExecutionException {
		LOGGER.debug("getUserInfoById called");
		User user = mobileAppDao.getUserInfoById(userId);
		LOGGER.debug("getUserInfoById completed successfully");
		return user;
	}
	
	@Override
	public List<MeasurementUnit> getMeasurementUnits(String unitCategory) throws ServiceExecutionException {
		LOGGER.debug("getMeasurementUnits called");
		List<MeasurementUnit> measurementUnits = mobileAppDao.getMeasurementUnits(unitCategory);
		LOGGER.debug("getMeasurementUnits end");
		return measurementUnits;
	}

}
