package com.hillspet.wearables.service.intake.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dao.intake.FoodIntakeDao;
import com.hillspet.wearables.dto.FoodIntakeDTO;
import com.hillspet.wearables.response.FoodIntakeHistoryGraphResponse;
import com.hillspet.wearables.response.FoodIntakeLookUpResponse;
import com.hillspet.wearables.response.FoodIntakeResponse;
import com.hillspet.wearables.service.intake.FoodIntakeService;

/**
 * @author akumarkhaspa
 */
@Service
public class FoodIntakeServiceImpl implements FoodIntakeService {

	private static final Logger LOGGER = LogManager.getLogger(FoodIntakeServiceImpl.class);

	@Autowired
	private FoodIntakeDao intakeDao;

	/**
	 * @author akumarkhaspa
	 * @param petId
	 * @param petParentId
	 * @param intakeDate
	 * @return
	 */
	@Override
	public FoodIntakeLookUpResponse getPetFoodIntakeConfigData(int petId, int petParentId, String intakeDate)
			throws ServiceExecutionException {
		LOGGER.debug("getPetFoodIntakeConfigData called ");
		FoodIntakeLookUpResponse response = intakeDao.getPetFoodIntakeConfigData(petId, petParentId, intakeDate);
		LOGGER.debug("getPetFoodIntakeConfigData end");
		return response;
	}

	/**
	 * @author akumarkhaspa
	 * @param petId
	 * @param petParentId
	 * @param intakeDate
	 * @return
	 */
	@Override
	public FoodIntakeResponse getPetIntakeList(int petId, int petParentId, String intakeDate)
			throws ServiceExecutionException {
		LOGGER.debug("getPetIntakeList called {}, {}, {} ", petId, petParentId, intakeDate);
		FoodIntakeResponse response = intakeDao.getPetIntakeList(petId, petParentId, intakeDate);
		LOGGER.debug("getPetIntakeList end");
		return response;
	}

	/**
	 * @author akumarkhaspa
	 * @param intakeId
	 * @return
	 */
	@Override
	public FoodIntakeDTO getPetIntakeById(int intakeId) throws ServiceExecutionException {
		LOGGER.debug("getPetIntakeById called {}", intakeId);
		FoodIntakeDTO response = intakeDao.getPetIntakeById(intakeId);
		LOGGER.debug("getPetIntakeById end");
		return response;
	}

	/**
	 * @author akumarkhaspa
	 * @param foodIntakeDTO
	 * @return
	 */
	@Override
	public FoodIntakeDTO saveOrUpdatePetFoodIntake(FoodIntakeDTO foodIntakeDTO) throws ServiceExecutionException {
		LOGGER.debug("saveOrUpdatePetFoodIntake called");
		FoodIntakeDTO response = intakeDao.saveOrUpdatePetFoodIntake(foodIntakeDTO);
		LOGGER.debug("saveOrUpdatePetFoodIntake end");
		return response;
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
	public FoodIntakeHistoryGraphResponse studyDietIntakeHistory(int petId, int petParentId, int dietId,
			String fromDate, String toDate) throws ServiceExecutionException {
		LOGGER.debug("studyDietIntakeHistory called");
		FoodIntakeHistoryGraphResponse response = intakeDao.studyDietIntakeHistory(petId, petParentId, dietId, fromDate, toDate);
		LOGGER.debug("studyDietIntakeHistory end");
		return response;
	}

}
