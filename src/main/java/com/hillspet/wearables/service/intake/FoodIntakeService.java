package com.hillspet.wearables.service.intake;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.FoodIntakeDTO;
import com.hillspet.wearables.response.FoodIntakeHistoryGraphResponse;
import com.hillspet.wearables.response.FoodIntakeLookUpResponse;
import com.hillspet.wearables.response.FoodIntakeResponse;

/**
 * @author akumarkhaspa
 */
public interface FoodIntakeService {

	public FoodIntakeLookUpResponse getPetFoodIntakeConfigData(int petId, int petParentId, String intakeDate)
			throws ServiceExecutionException;

	public FoodIntakeResponse getPetIntakeList(int petId, int petParentId, String intakeDate)
			throws ServiceExecutionException;

	public FoodIntakeDTO getPetIntakeById(int intakeId) throws ServiceExecutionException;

	public FoodIntakeDTO saveOrUpdatePetFoodIntake(FoodIntakeDTO foodIntakeDTO) throws ServiceExecutionException;

	public FoodIntakeHistoryGraphResponse studyDietIntakeHistory(int petId, int petParentId, int dietId, String fromDate, String toDate)
			throws ServiceExecutionException;

}
