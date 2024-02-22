package com.hillspet.wearables.service.questionnaire;

import java.util.List;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.Address;
import com.hillspet.wearables.dto.Campaign;
import com.hillspet.wearables.dto.LeaderBoard;
import com.hillspet.wearables.dto.MeasurementUnit;
import com.hillspet.wearables.dto.MobileAppFeedback;
import com.hillspet.wearables.dto.PetCampaignPointsDTO;
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

public interface MobileAppService {

	List<Questionnaire> getFeedbackQuestionnaireByPetId(int petId, String deviceModel, String isDateSupported)
			throws ServiceExecutionException;

	List<Questionnaire> getQuestionnaireByPetId(int petId, String isDateSupported) throws ServiceExecutionException;

	List<Questionnaire> getQuestionnaireAnswers(int petId, int questionnaireId) throws ServiceExecutionException;

	void saveQuestionAnswers(QuestionAnswerRequest questionAnswerRequest, Boolean fromDeprecated)
			throws ServiceExecutionException;

	PetCampaignPointsDTO getPetCampaignPoints(int petId) throws ServiceExecutionException;

	PetCampaignListResponse getPetCampaignPointsList(int petId) throws ServiceExecutionException;

	int getPetParentByPetParentKey(String petParentKey) throws ServiceExecutionException;

	List<Campaign> getCampaignListByPet(int petId) throws ServiceExecutionException;

	List<LeaderBoard> getLeaderBoardByCampaignId(int campaignId) throws ServiceExecutionException;

	List<PetRedemptionHistoryDTO> getPetRedemptionHistory(int petId) throws ServiceExecutionException;

	void assignSensorToPet(AssignSensorRequest assignSensorRequest) throws ServiceExecutionException;

	List<PetDTO> getPetDevicesByPetParent(int petParentId) throws ServiceExecutionException;

	List<MobileAppFeedback> getFeedbackByPetParent(int petParentId) throws ServiceExecutionException;

	List<String> getDeviceTypes() throws ServiceExecutionException;

	List<String> getDeviceModels(String deviceType) throws ServiceExecutionException;

	public TimeZone getTimeZoneDetails(String timeZoneName) throws ServiceExecutionException;

	public Study getStudyByPetId(int petId) throws ServiceExecutionException;

	public Address getPetParentAddressByPetParent(int petParentId) throws ServiceExecutionException;

	public PetParentAddressResponse getPetParentAddressHistoryById(int petParentId) throws ServiceExecutionException;

	public String validateDuplicatePet(ValidateDuplicatePetRequest petRequest) throws ServiceExecutionException;

	public AppVersion getAppLatestVersion(int appOSId, String appVersion) throws ServiceExecutionException;

	public User getUserInfoById(int userId) throws ServiceExecutionException;

	public List<MeasurementUnit> getMeasurementUnits(String unitCategory) throws ServiceExecutionException;

}
