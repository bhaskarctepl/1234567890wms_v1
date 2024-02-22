package com.hillspet.wearables.service.pet.bfi.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dao.pet.bfi.PetBfiDao;
import com.hillspet.wearables.dto.BfiImageScore;
import com.hillspet.wearables.dto.PetBfiInstruction;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetFoodBrand;
import com.hillspet.wearables.dto.PetImagePosition;
import com.hillspet.wearables.request.PetBfiImageScoreRequest;
import com.hillspet.wearables.request.PetBfiImagesRequest;
import com.hillspet.wearables.response.BfiImageScoreResponse;
import com.hillspet.wearables.response.PetBfiInstructionResponse;
import com.hillspet.wearables.response.PetFoodBrandResponse;
import com.hillspet.wearables.response.PetImagePositionResponse;
import com.hillspet.wearables.response.PetsResponse;
import com.hillspet.wearables.service.pet.bfi.PetBfiService;

/**
 * @author vvodyaram
 *
 */
@Service
public class PetBfiServiceImpl implements PetBfiService {

	private static final Logger LOGGER = LogManager.getLogger(PetBfiServiceImpl.class);

	@Autowired
	private PetBfiDao petBfiDao;

	@Override
	public PetBfiInstructionResponse getPetBfiInstructions(int instructionType) throws ServiceExecutionException {
		LOGGER.debug("getPetBfiInstructions called");
		List<PetBfiInstruction> petBfiInstructions = petBfiDao.getPetBfiInstructions(instructionType);
		PetBfiInstructionResponse response = new PetBfiInstructionResponse();
		response.setPetBfiInstructions(petBfiInstructions);
		LOGGER.debug("getPetBfiInstructions completed successfully");
		return response;
	}

	@Override
	public PetImagePositionResponse getPetImagePositions() throws ServiceExecutionException {
		LOGGER.debug("getPetImagePositions called");
		List<PetImagePosition> imagePositions = petBfiDao.getPetImagePositions();
		PetImagePositionResponse response = new PetImagePositionResponse();
		response.setImagePositions(imagePositions);
		LOGGER.debug("getPetImagePositions completed successfully");
		return response;
	}

	@Override
	public BfiImageScoreResponse getBfiImageScores(int speciesId) throws ServiceExecutionException {
		LOGGER.debug("getBfiImageScores called");
		List<BfiImageScore> bfiImageScores = petBfiDao.getBfiImageScores(speciesId);
		BfiImageScoreResponse response = new BfiImageScoreResponse();
		response.setBfiImageScores(bfiImageScores);
		LOGGER.debug("getBfiImageScores completed successfully");
		return response;
	}

	@Override
	public PetFoodBrandResponse getPetFoodBrands(int speciesId) throws ServiceExecutionException {
		LOGGER.debug("getPetFoodBrands called");
		List<PetFoodBrand> petFoodBrands = petBfiDao.getPetFoodBrands(speciesId);
		PetFoodBrandResponse response = new PetFoodBrandResponse();
		response.setPetFoodBrands(petFoodBrands);
		LOGGER.debug("getPetFoodBrands completed successfully");
		return response;
	}

	@Override
	public PetsResponse getPetsToCaptureBfiImages(int petParentId, int speciesId, int pageNo, int pageLength,
			String searchText, int userId) throws ServiceExecutionException {
		LOGGER.debug("getPetsToCaptureBfiImages called");
		List<PetDTO> pets = petBfiDao.getPetsToCaptureBfiImages(petParentId, speciesId, pageNo, pageLength, searchText,
				userId);
		PetsResponse response = new PetsResponse();
		response.setPets(pets);
		LOGGER.debug("getPetsToCaptureBfiImages completed successfully");
		return response;
	}

	@Override
	public void savePetBfiImages(PetBfiImagesRequest petBfiImagesRequest) throws ServiceExecutionException {
		LOGGER.debug("savePetBfiImages called");
		petBfiDao.savePetBfiImages(petBfiImagesRequest);
		LOGGER.debug("savePetBfiImages completed successfully");
	}

	@Override
	public List<PetDTO> getBfiPets(int pageNo, int pageLength, String searchText, boolean isScored, int userId)
			throws ServiceExecutionException {
		LOGGER.debug("getBfiPets called");
		List<PetDTO> pets = petBfiDao.getBfiPets(pageNo, pageLength, searchText, isScored, userId);
		LOGGER.debug("getBfiPets completed successfully");
		return pets;
	}

	@Override
	public List<PetDTO> getPetBfiImages(int petId, String petBfiImageSetIds, boolean isScored, int userId)
			throws ServiceExecutionException {
		LOGGER.debug("getPetBfiImages called");
		List<PetDTO> pets = petBfiDao.getPetBfiImages(petId, petBfiImageSetIds, isScored, userId);
		LOGGER.debug("getPetBfiImages completed successfully");
		return pets;
	}

	@Override
	public void savePetBfiImageScore(PetBfiImageScoreRequest petBfiImageScoreRequest) throws ServiceExecutionException {
		LOGGER.debug("savePetBfiImageScore called");
		petBfiDao.savePetBfiImageScore(petBfiImageScoreRequest);
		LOGGER.debug("savePetBfiImageScore completed successfully");
	}

}
