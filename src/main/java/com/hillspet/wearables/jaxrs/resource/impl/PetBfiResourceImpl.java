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
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.jaxrs.resource.PetBfiResource;
import com.hillspet.wearables.objects.common.response.CommonResponse;
import com.hillspet.wearables.request.PetBfiImageScoreRequest;
import com.hillspet.wearables.request.PetBfiImagesRequest;
import com.hillspet.wearables.response.BfiImageScoreResponse;
import com.hillspet.wearables.response.PetBfiInstructionResponse;
import com.hillspet.wearables.response.PetFoodBrandResponse;
import com.hillspet.wearables.response.PetImagePositionResponse;
import com.hillspet.wearables.response.PetsResponse;
import com.hillspet.wearables.service.pet.bfi.PetBfiService;
import com.hillspet.wearables.service.questionnaire.MobileAppService;

/**
 * @author vvodyaram
 *
 */
@Service
public class PetBfiResourceImpl implements PetBfiResource {

	private static final Logger LOGGER = LogManager.getLogger(PetResourceImpl.class);

	@Autowired
	private PetBfiService petBfiService;

	@Autowired
	private MobileAppService mobileAppService;

	@Autowired
	private JaxrsJsonResponseBuilder responseBuilder;

	@Override
	public Response getPetBfiInstructions(int instructionType, String token) {
		LOGGER.info("getPetBfiInstructions called");
		int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
		if (petParentKeyId == 0) {
			throw new ServiceValidationException(
					"getPetBfiInstructions service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}

		PetBfiInstructionResponse response = petBfiService.getPetBfiInstructions(instructionType);
		SuccessResponse<PetBfiInstructionResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetImagePositions(String token) {
		LOGGER.info("getPetImagePositions called");
		int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
		if (petParentKeyId == 0) {
			throw new ServiceValidationException(
					"getPetImagePositions service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}

		PetImagePositionResponse response = petBfiService.getPetImagePositions();
		SuccessResponse<PetImagePositionResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getBfiImageScores(int speciesId, String token) {
		LOGGER.info("getBfiImageScores called");
		int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
		if (petParentKeyId == 0) {
			throw new ServiceValidationException("getBfiImageScores service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}

		BfiImageScoreResponse response = petBfiService.getBfiImageScores(speciesId);
		SuccessResponse<BfiImageScoreResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetFoodBrands(int speciesId, String token) {
		LOGGER.info("getPetFoodBrands called");
		int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
		if (petParentKeyId == 0) {
			throw new ServiceValidationException("getPetFoodBrands service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}

		PetFoodBrandResponse response = petBfiService.getPetFoodBrands(speciesId);
		SuccessResponse<PetFoodBrandResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetsToCaptureBfiImages(int petParentId, int speciesId, int pageNo, int pageLength,
			String searchText, String token) {
		LOGGER.info("getPetsToCaptureBfiImages called");
		int userId = mobileAppService.getPetParentByPetParentKey(token);
		if (userId == 0) {
			throw new ServiceValidationException(
					"getPetsToCaptureBfiImages service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}

		PetsResponse response = petBfiService.getPetsToCaptureBfiImages(petParentId, speciesId, pageNo, pageLength,
				searchText, userId);
		SuccessResponse<PetsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response savePetBfiImages(PetBfiImagesRequest petBfiImagesRequest, String token) {
		LOGGER.info("savePetBfiImages called");
		int userId = mobileAppService.getPetParentByPetParentKey(token);
		if (userId == 0) {
			throw new ServiceValidationException("savePetBfiImages service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}
		petBfiImagesRequest.setCreatedBy(userId);

		petBfiService.savePetBfiImages(petBfiImagesRequest);
		CommonResponse response = new CommonResponse();
		response.setMessage("Pet Bfi Images uploaded Successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getBfiPets(int pageNo, int pageLength, String searchText, boolean isScored, String token) {
		LOGGER.info("getBfiPets called");
		int userId = mobileAppService.getPetParentByPetParentKey(token);
		if (userId == 0) {
			throw new ServiceValidationException("getBfiPets service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}

		List<PetDTO> pets = petBfiService.getBfiPets(pageNo, pageLength, searchText, isScored, userId);
		PetsResponse response = new PetsResponse();
		response.setPets(pets);
		SuccessResponse<PetsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetBfiImages(int petId, String petBfiImageSetIds, boolean isScored, String token) {
		LOGGER.info("getPetBfiImages called");
		int userId = mobileAppService.getPetParentByPetParentKey(token);
		if (userId == 0) {
			throw new ServiceValidationException("getPetBfiImages service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}

		List<PetDTO> pets = petBfiService.getPetBfiImages(petId, petBfiImageSetIds, isScored, userId);
		PetsResponse response = new PetsResponse();
		response.setPets(pets);
		SuccessResponse<PetsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response savePetBfiImageScore(PetBfiImageScoreRequest petBfiImageScoreRequest, String token) {
		LOGGER.info("savePetBfiImageScore called");
		int userId = mobileAppService.getPetParentByPetParentKey(token);
		if (userId == 0) {
			throw new ServiceValidationException(
					"savePetBfiImageScore service validation failed cannot proceed further",
					Arrays.asList(new WearablesError(WearablesErrorCode.AUTHORIZATION_FAILED)));
		}
		petBfiImageScoreRequest.setCreatedBy(userId);

		petBfiService.savePetBfiImageScore(petBfiImageScoreRequest);
		CommonResponse response = new CommonResponse();
		response.setMessage("Pet Bfi Images Scoring is Successful");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

}
