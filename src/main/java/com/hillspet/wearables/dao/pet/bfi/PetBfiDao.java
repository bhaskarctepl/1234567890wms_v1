package com.hillspet.wearables.dao.pet.bfi;

import java.util.List;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.BfiImageScore;
import com.hillspet.wearables.dto.PetBfiInstruction;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetFoodBrand;
import com.hillspet.wearables.dto.PetImagePosition;
import com.hillspet.wearables.request.PetBfiImageScoreRequest;
import com.hillspet.wearables.request.PetBfiImagesRequest;

/**
 * @author vvodyaram
 *
 */
public interface PetBfiDao {

	/**
	 * @param instructionType 
	 * @return
	 * @throws ServiceExecutionException
	 */
	public List<PetBfiInstruction> getPetBfiInstructions(int instructionType) throws ServiceExecutionException;

	/**
	 * @return
	 * @throws ServiceExecutionException
	 */
	public List<PetImagePosition> getPetImagePositions() throws ServiceExecutionException;

	/**
	 * @param speciesId
	 * @return
	 * @throws ServiceExecutionException
	 */
	public List<BfiImageScore> getBfiImageScores(int speciesId) throws ServiceExecutionException;

	/**
	 * @param speciesId
	 * @return
	 * @throws ServiceExecutionException
	 */
	public List<PetFoodBrand> getPetFoodBrands(int speciesId) throws ServiceExecutionException;

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
	public List<PetDTO> getPetsToCaptureBfiImages(int petParentId, int speciesId, int pageNo, int pageLength,
			String searchText, int userId) throws ServiceExecutionException;

	/**
	 * @param petBfiImagesRequest
	 * @throws ServiceExecutionException
	 */
	public void savePetBfiImages(PetBfiImagesRequest petBfiImagesRequest) throws ServiceExecutionException;

	/**
	 * @param pageNo
	 * @param pageLength
	 * @param searchText
	 * @param userId 
	 * @return
	 * @throws ServiceExecutionException
	 */
	public List<PetDTO> getBfiPets(int pageNo, int pageLength, String searchText, boolean isScored, int userId)
			throws ServiceExecutionException;

	/**
	 * @param petId
	 * @param petBfiImageSetIds
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
