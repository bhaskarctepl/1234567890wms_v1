package com.hillspet.wearables.jaxrs.resource.impl;

import java.io.IOException;
import java.util.ArrayList;
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
import com.hillspet.wearables.dto.BehaviorHistory;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetFeedingEnthusiasmScale;
import com.hillspet.wearables.dto.PetObservation;
import com.hillspet.wearables.dto.PetWeightDTO;
import com.hillspet.wearables.jaxrs.resource.PetResource;
import com.hillspet.wearables.objects.common.response.CommonResponse;
import com.hillspet.wearables.request.AddPetWeight;
import com.hillspet.wearables.request.BehaviorHistoryRequest;
import com.hillspet.wearables.request.AssignSensorRequest;
import com.hillspet.wearables.request.FMGoalSettingRequest;
import com.hillspet.wearables.request.PetAddFeedingPreferences;
import com.hillspet.wearables.request.PetAddImageScoring;
import com.hillspet.wearables.request.PetIds;
import com.hillspet.wearables.request.UpdatePet;
import com.hillspet.wearables.request.UpdatePetWeight;
import com.hillspet.wearables.request.ValidateDuplicatePetRequest;
import com.hillspet.wearables.response.BehaviorHistoryResponse;
import com.hillspet.wearables.response.BehaviorVisualizationResponse;
import com.hillspet.wearables.response.EatingEnthusiasmScaleResponse;
import com.hillspet.wearables.response.ImageScoringScalesResponse;
import com.hillspet.wearables.response.PetAddressResponse;
import com.hillspet.wearables.response.PetBehaviorsResponse;
import com.hillspet.wearables.response.PetBreedResponse;
import com.hillspet.wearables.response.PetFeedingEnthusiasmScaleResponse;
import com.hillspet.wearables.response.PetFeedingPreferenceResponse;
import com.hillspet.wearables.response.PetFeedingTimeResponse;
import com.hillspet.wearables.response.PetMobileAppConfigResponse;
import com.hillspet.wearables.response.PetObservationResponse;
import com.hillspet.wearables.response.PetObservationsResponse;
import com.hillspet.wearables.response.PetSpeciesResponse;
import com.hillspet.wearables.response.PetWeightHistoryResponse;
import com.hillspet.wearables.response.PetWeightResponse;
import com.hillspet.wearables.response.PetsResponse;
import com.hillspet.wearables.service.pet.PetService;
import com.hillspet.wearables.service.questionnaire.MobileAppService;

/**
 * This class providing Pet details.
 * 
 * @author sgorle
 * @since w2.0
 * @version w2.0
 * @version Dec 8, 2020
 */
@Service
public class PetResourceImpl implements PetResource {

	private static final Logger LOGGER = LogManager.getLogger(PetResourceImpl.class);

	@Autowired
	private PetService petService;

	@Autowired
	private MobileAppService mobileAppService;

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
	public Response addWeight(AddPetWeight addPetWeight, String token) {
		LOGGER.debug("addWeight called");
		authorizeClientToken(token);

		// addPetWeight.setUserId(petParentKeyId);
		PetWeightDTO petWeightDTO = petService.addPetWeight(addPetWeight);
		PetWeightResponse response = new PetWeightResponse();
		response.setPetWeightDTO(petWeightDTO);

		// Build a successful response
		SuccessResponse<PetWeightResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response updateWeight(UpdatePetWeight updatePetWeight, String token) {
		LOGGER.debug("updateWeight called");
		authorizeClientToken(token);

		// addPetWeight.setUserId(petParentKeyId);
		PetWeightDTO petWeightDTO = petService.updateWeight(updatePetWeight);
		PetWeightResponse response = new PetWeightResponse();
		response.setPetWeightDTO(petWeightDTO);

		// Build a successful response
		SuccessResponse<PetWeightResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetWeightHistory(int petId, String fromDate, String toDate, String token) throws IOException {
		LOGGER.debug("getPetWeightHistory called");
		authorizeClientToken(token);

		PetWeightHistoryResponse response = petService.getPetWeightHistory(petId, fromDate, toDate);
		SuccessResponse<PetWeightHistoryResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetSpecies(String token) {
		LOGGER.debug("getPetSpecies called");
		authorizeClientToken(token);

		PetSpeciesResponse petSpeciesResponse = petService.getPetSpecies();
		SuccessResponse<PetSpeciesResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(petSpeciesResponse);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetBreeds(int speciesId, String token) {
		LOGGER.debug("getPetBreeds called");
		authorizeClientToken(token);

		PetBreedResponse response = petService.getPetBreeds(speciesId);
		SuccessResponse<PetBreedResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetBehaviors(int speciesId, String token, int behaviorTypeId) {
		LOGGER.debug("getPetBehaviors called");
		authorizeClientToken(token);

		PetBehaviorsResponse response = petService.getPetBehaviors(speciesId, behaviorTypeId);
		SuccessResponse<PetBehaviorsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetEatingEnthusiasmScale(String token, int speciesId) {
		LOGGER.debug("getPetEatingEnthusiasmScale called");
		authorizeClientToken(token);

		EatingEnthusiasmScaleResponse eatingEnthusiasmScaleResponse = petService.getPetEatingEnthusiasmScale(speciesId);
		SuccessResponse<EatingEnthusiasmScaleResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(eatingEnthusiasmScaleResponse);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetFeedingTime(String token) {
		LOGGER.debug("getPetFeedingTime called");
		authorizeClientToken(token);

		PetFeedingTimeResponse petFeedingTimeResponse = petService.getPetFeedingTime();
		SuccessResponse<PetFeedingTimeResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(petFeedingTimeResponse);
		return responseBuilder.buildResponse(successResponse);
	}

	// @Override
	public Response getMobileAppConfigs(int petId, String token) throws IOException {
		LOGGER.debug("getMobileAppConfigs called");
		authorizeClientToken(token);

		List<Integer> petIdList = new ArrayList<>();
		petIdList.add(petId);
		PetIds petIds = new PetIds();
		petIds.setPetIds(petIdList);

		PetMobileAppConfigResponse response = petService.getMobileAppConfigs(petIds);
		SuccessResponse<PetMobileAppConfigResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getMobileAppConfigs(PetIds petIds, String token) throws IOException {
		LOGGER.debug("getMobileAppConfigs called");
		authorizeClientToken(token);

		PetMobileAppConfigResponse response = petService.getMobileAppConfigs(petIds);
		SuccessResponse<PetMobileAppConfigResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response addPetFeedingTime(PetFeedingEnthusiasmScale petFeedingEnthusiasmScale, String token) {
		LOGGER.debug("addPetFeedingTime called");
		authorizeClientToken(token);

		petFeedingEnthusiasmScale = petService.addPetFeedingTime(petFeedingEnthusiasmScale);
		PetFeedingEnthusiasmScaleResponse response = new PetFeedingEnthusiasmScaleResponse();
		response.setPetFeedingEnthusiasmScale(petFeedingEnthusiasmScale);

		// Build a successful response
		SuccessResponse<PetFeedingEnthusiasmScaleResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetObservationsByPetId(int petId, String token) {
		LOGGER.debug("getPetObservationsByPetId called");
		authorizeClientToken(token);

		PetObservationsResponse response = petService.getPetObservationsByPetId(petId);
		SuccessResponse<PetObservationsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response savePetObservation(PetObservation addPetObservation, String token) {
		LOGGER.debug("savePetObservation called");
		authorizeClientToken(token);

		PetObservation petObservation = petService.savePetObservation(addPetObservation);
		PetObservationResponse response = new PetObservationResponse();
		response.setPetObservation(petObservation);

		// Build a successful response
		SuccessResponse<PetObservationResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response deletePetObservation(int observationId, int petId, int petParentId, String token) {
		LOGGER.debug("deletePetObservation called");
		authorizeClientToken(token);

		petService.deletePetObservation(observationId, petId, petParentId);

		// Step 5: build a successful response
		CommonResponse response = new CommonResponse();
		response.setMessage("Pet observation has been deleted successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetImageScoringScales(int petId, String token) {
		LOGGER.debug("getPetImageScoringScales called");
		authorizeClientToken(token);

		ImageScoringScalesResponse response = petService.getPetImageScoringScales(petId);
		SuccessResponse<ImageScoringScalesResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response addPetImageScoring(PetAddImageScoring addPetImageScorings, String token) {
		LOGGER.debug("addPetImageScoring called");
		authorizeClientToken(token);
		petService.addPetImageScoring(addPetImageScorings);

		// Step 5: build a successful response
		CommonResponse response = new CommonResponse();
		response.setMessage("Pet Image Scoring has been added successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetFeedingPreferences(String token) {
		LOGGER.debug("getPetFeedingPreferences called");
		authorizeClientToken(token);

		PetFeedingPreferenceResponse petFeedingPreferencesResponse = petService.getPetFeedingPreferences();
		SuccessResponse<PetFeedingPreferenceResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(petFeedingPreferencesResponse);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response addPetFeedingPreferences(PetAddFeedingPreferences petAddFeedingPreferences, String token) {
		LOGGER.debug("addPetFeedingPreferences called");
		int userId = authorizeClientToken(token);

		petAddFeedingPreferences.setUserId(userId);
		petService.addPetFeedingPreferences(petAddFeedingPreferences);

		CommonResponse response = new CommonResponse();
		response.setMessage("Pet Feeding Preferences has been added successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response updatePet(UpdatePet updatePet, String token) {
		LOGGER.debug("updatePet called");
		authorizeClientToken(token);
		petService.updatePet(updatePet);

		CommonResponse response = new CommonResponse();
		response.setMessage("Pet has been updated successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetAddressHistoryById(int petId, String token) {
		LOGGER.debug("getPetAddressHistoryById called");
		authorizeClientToken(token);

		PetAddressResponse response = petService.getPetAddressHistoryById(petId);
		SuccessResponse<PetAddressResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response validateDuplicatePet(ValidateDuplicatePetRequest validateDuplicatePetRequest, String token) {
		LOGGER.debug("validateDuplicatePet called");
		authorizeClientToken(token);

		// Step 2: process
		String result = mobileAppService.validateDuplicatePet(validateDuplicatePetRequest);

		if ("1".equals(result)) {
			throw new ServiceValidationException("validateDuplicatePet validation failed",
					Arrays.asList(new WearablesError(WearablesErrorCode.DUPLICATE_PET_VALIDATION_FAILED)));
		}

		// Step 5: build a successful response
		CommonResponse response = new CommonResponse();
		response.setMessage("Valid Pet");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetsByPetParentIdAndMobileAppConfigId(int petParentId, int mobileAppConfigId, String token) {
		LOGGER.debug("getPetsByPetParentIdAndMobileAppConfigId called");
		authorizeClientToken(token);

		List<PetDTO> pets = petService.getPetsByPetParentIdAndMobileAppConfigId(petParentId, mobileAppConfigId);
		PetsResponse response = new PetsResponse();
		response.setPets(pets);
		SuccessResponse<PetsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}
	
	@Override
	public Response getBehaviorVisualization(int petId, String token) {
		
		int userId = mobileAppService.getPetParentByPetParentKey(token);
		if (userId == 0) {
			throw new ServiceValidationException(
					"getPetsByPetParentIdAndMobileAppConfigId service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}
		// Step 1: process
		BehaviorVisualizationResponse behaviorVisualizationResponse = petService.getBehaviorVisualization(petId);

		// Step 2: build a successful response

		SuccessResponse<BehaviorVisualizationResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(behaviorVisualizationResponse);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getBehaviorHistoryByType(int petId, String behaviorType,
			BehaviorHistoryRequest behaviorHistoryRequest, String token) {
		
		int userId = mobileAppService.getPetParentByPetParentKey(token);
		if (userId == 0) {
			throw new ServiceValidationException(
					"getPetsByPetParentIdAndMobileAppConfigId service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}
		// Step 1: process
		List<BehaviorHistory> result = petService.getBehaviorHistoryByType(petId, behaviorType, behaviorHistoryRequest);

		// Step 2: build a successful response
		BehaviorHistoryResponse historyResponse = new BehaviorHistoryResponse();
		historyResponse.setBehaviorHistory(result);
		SuccessResponse<BehaviorHistoryResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(historyResponse);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response replaceSensorToPet(AssignSensorRequest assignSensorRequest, String token) {
		int userId = authorizeClientToken(token);
		assignSensorRequest.setCreatedBy(userId);
		petService.replaceSensorToPet(assignSensorRequest);
		CommonResponse response = new CommonResponse();
		response.setMessage("Sensor has been assigned successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response saveForwardMotionGoal(FMGoalSettingRequest fmGoalSettingRequest, String token) {
		int userId = authorizeClientToken(token);
		fmGoalSettingRequest.setUserId(userId);

		petService.saveForwardMotionGoal(fmGoalSettingRequest);
		CommonResponse response = new CommonResponse();
		response.setMessage("Goal setting has been saved successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

}
