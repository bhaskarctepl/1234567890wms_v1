package com.hillspet.wearables.service.pet.bfi;

import java.util.List;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.request.PetBfiImageScoreRequest;
import com.hillspet.wearables.request.PetBfiImagesRequest;
import com.hillspet.wearables.response.BfiImageScoreResponse;
import com.hillspet.wearables.response.PetBfiInstructionResponse;
import com.hillspet.wearables.response.PetFoodBrandResponse;
import com.hillspet.wearables.response.PetImagePositionResponse;
import com.hillspet.wearables.response.PetsResponse;

/**
 * @author vvodyaram
 *
 */
public interface PetBfiService {

	/**
	 * @param instructionType
	 * @return
	 * @throws ServiceExecutionException
	 */
	public PetBfiInstructionResponse getPetBfiInstructions(int instructionType) throws ServiceExecutionException;

	/**
	 * @return
	 * @throws ServiceExecutionException
	 */
	public PetImagePositionResponse getPetImagePositions() throws ServiceExecutionException;

	/**
	 * @param speciesId
	 * @return
	 * @throws ServiceExecutionException
	 */
	public BfiImageScoreResponse getBfiImageScores(int speciesId) throws ServiceExecutionException;

	/**
	 * @param speciesId
	 * @return
	 * @throws ServiceExecutionException
	 */
	public PetFoodBrandResponse getPetFoodBrands(int speciesId) throws ServiceExecutionException;

	/**
	 * @param petParentId
	 * @param speciesId
	 * @param pageNo
	 * @param pageLength
	 * @param searchText
	 * @param userId
	 * @return
	 * @throws ServiceExecutionException
	 */
	public PetsResponse getPetsToCaptureBfiImages(int petParentId, int speciesId, int pageNo, int pageLength,
			String searchText, int userId) throws ServiceExecutionException;

	/**
	 * @param petBfiImagesRequest
	 * @throws ServiceExecutionException
	 */
	public void savePetBfiImages(PetBfiImagesRequest petBfiImagesRequest) throws ServiceExecutionException;

	/**
	 * @param pageNo
	 * @param pageLength
	 * @param userId 
	 * @return
	 * @throws ServiceExecutionException
	 */
	public List<PetDTO> getBfiPets(int pageNo, int pageLength, String searchText, boolean isScored, int userId)
			throws ServiceExecutionException;

	/**
	 * @param petId
	 * @param petBfiImageSetIds
	 * @param isScored
	 * @param userId
	 * @return
	 * @throws ServiceExecutionException
	 */
	public List<PetDTO> getPetBfiImages(int petId, String petBfiImageSetIds, boolean isScored, int userId)
			throws ServiceExecutionException;

	/**
	 * @param petBfiImageScoreRequest
	 * @throws ServiceExecutionException
	 */
	public void savePetBfiImageScore(PetBfiImageScoreRequest petBfiImageScoreRequest) throws ServiceExecutionException;

}
