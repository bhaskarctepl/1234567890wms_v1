package com.hillspet.wearables.jaxrs.resource.impl;

import java.io.IOException;
import java.util.Arrays;

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
import com.hillspet.wearables.dto.FoodIntakeDTO;
import com.hillspet.wearables.jaxrs.resource.FoodIntakeResource;
import com.hillspet.wearables.response.FoodIntakeHistoryGraphResponse;
import com.hillspet.wearables.response.FoodIntakeLookUpResponse;
import com.hillspet.wearables.response.FoodIntakeResponse;
import com.hillspet.wearables.service.intake.FoodIntakeService;
import com.hillspet.wearables.service.questionnaire.MobileAppService;

/**
 * This class providing Food Intake details.
 * 
 * @author akumarkhaspa
 * @since Dec 26, 2023
 */
@Service
public class FoodIntakeResourceImpl implements FoodIntakeResource {

	private static final Logger LOGGER = LogManager.getLogger(FoodIntakeResourceImpl.class);

	@Autowired
	private FoodIntakeService foodIntakeService;

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

	/**
	 * @author akumarkhaspa
	 * @param petId
	 * @param petParentId
	 * @param intakeDate
	 * @param token
	 * @return
	 */
	@Override
	public Response getPetFoodIntakeConfigData(int petId, int petParentId, String intakeDate, String token) {
		LOGGER.debug("getPetFoodIntakeConfigData called");
		authorizeClientToken(token);
		FoodIntakeLookUpResponse response = foodIntakeService.getPetFoodIntakeConfigData(petId, petParentId, intakeDate);
		SuccessResponse<FoodIntakeLookUpResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	/**
	 * @author akumarkhaspa
	 * @param petId
	 * @param petParentId
	 * @param intakeDate
	 * @param token
	 * @return
	 */
	@Override
	public Response getPetIntakeList(int petId, int petParentId, String intakeDate, String token) throws IOException {
		LOGGER.debug("getPetIntakeList called");
		authorizeClientToken(token);
		FoodIntakeResponse response = foodIntakeService.getPetIntakeList(petId, petParentId, intakeDate);
		SuccessResponse<FoodIntakeResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	/**
	 * @author akumarkhaspa
	 * @param intakeId
	 * @param token
	 * @return
	 */
	@Override
	public Response getPetIntakeById(int intakeId, String token) throws IOException {
		LOGGER.debug("getPetIntakeById called");
		authorizeClientToken(token);
		FoodIntakeDTO response = foodIntakeService.getPetIntakeById(intakeId);
		SuccessResponse<FoodIntakeDTO> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	/**
	 * @author akumarkhaspa
	 * @param foodIntakeDTO
	 * @param token
	 * @return
	 */
	@Override
	public Response saveOrUpdatePetFoodIntake(FoodIntakeDTO foodIntakeDTO, String token) {
		LOGGER.debug("saveOrUpdatePetFoodIntake called");
		int userId = authorizeClientToken(token);
		foodIntakeDTO.setUserId(userId);
		FoodIntakeDTO foodIntake = foodIntakeService.saveOrUpdatePetFoodIntake(foodIntakeDTO);
		SuccessResponse<FoodIntakeDTO> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(foodIntake);
		return responseBuilder.buildResponse(successResponse);
	}

	/**
	 * @author akumarkhaspa
	 * @param petId
	 * @param petParentId
	 * @param dietId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@Override
	public Response studyDietIntakeHistory(int petId, int petParentId, int dietId, String fromDate, 
			String toDate, String token) throws IOException {
		LOGGER.debug("studyDietIntakeHistory called");
		authorizeClientToken(token);
		FoodIntakeHistoryGraphResponse response = 
				foodIntakeService.studyDietIntakeHistory(petId, petParentId, dietId, fromDate, toDate);
		SuccessResponse<FoodIntakeHistoryGraphResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

}
