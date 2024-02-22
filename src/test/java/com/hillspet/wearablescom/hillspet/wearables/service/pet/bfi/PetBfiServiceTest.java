package com.hillspet.wearablescom.hillspet.wearables.service.pet.bfi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

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
import com.hillspet.wearables.service.pet.bfi.PetBfiService;
import com.hillspet.wearables.service.pet.bfi.impl.PetBfiServiceImpl;

public class PetBfiServiceTest {

	@Mock
	private PetBfiDao petBfiDao;

	@InjectMocks
	private PetBfiService petBfiService = new PetBfiServiceImpl();

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(petBfiService, "petBfiDao", petBfiDao);
	}
	
	@Test
	public void testGetPetBfiInstructions() throws ServiceExecutionException {
		// Arrange
		int instructionType = 1;
		List<PetBfiInstruction> instructions = Arrays.asList(new PetBfiInstruction(), new PetBfiInstruction());
		when(petBfiDao.getPetBfiInstructions(anyInt())).thenReturn(instructions);

		PetBfiInstructionResponse response = petBfiService.getPetBfiInstructions(instructionType);

		// Assert
		assertEquals(instructions, response.getPetBfiInstructions());
		verify(petBfiDao).getPetBfiInstructions(instructionType);
	}

	@Test
	public void testGetPetImagePositions() throws ServiceExecutionException {
		// Arrange
		List<PetImagePosition> positions = Arrays.asList(new PetImagePosition(), new PetImagePosition());
		when(petBfiDao.getPetImagePositions()).thenReturn(positions);

		PetImagePositionResponse response = petBfiService.getPetImagePositions();

		// Assert
		assertEquals(positions, response.getImagePositions());
		verify(petBfiDao).getPetImagePositions();
	}

	@Test
	public void testGetBfiImageScores() throws ServiceExecutionException {
		int speciesId = 1;
		List<BfiImageScore> scores = Arrays.asList(new BfiImageScore(), new BfiImageScore());
		when(petBfiDao.getBfiImageScores(anyInt())).thenReturn(scores);

		BfiImageScoreResponse response = petBfiService.getBfiImageScores(speciesId);

		// Assert
		assertEquals(scores, response.getBfiImageScores());
		verify(petBfiDao).getBfiImageScores(speciesId);
	}

	@Test
	public void testGetPetFoodBrands() throws ServiceExecutionException {
		int speciesId = 1;
		List<PetFoodBrand> brands = Arrays.asList(new PetFoodBrand(), new PetFoodBrand());
		when(petBfiDao.getPetFoodBrands(anyInt())).thenReturn(brands);

		PetFoodBrandResponse response = petBfiService.getPetFoodBrands(speciesId);

		// Assert
		assertEquals(brands, response.getPetFoodBrands());
		verify(petBfiDao).getPetFoodBrands(speciesId);
	}

	@Test
	public void testSavePetBfiImages() throws ServiceExecutionException {
		PetBfiImagesRequest request = new PetBfiImagesRequest();
		petBfiService.savePetBfiImages(request);

		// Assert
		verify(petBfiDao).savePetBfiImages(request);
	}

	@Test
	public void testGetBfiPets() throws ServiceExecutionException {
		int pageNo = 1;
		int pageLength = 10;
		String searchText = "test";
		boolean isScored = true;
		List<PetDTO> pets = Arrays.asList(new PetDTO(), new PetDTO());
		when(petBfiDao.getBfiPets(anyInt(), anyInt(), anyString(), anyBoolean(), anyInt())).thenReturn(pets);

		List<PetDTO> result = petBfiService.getBfiPets(pageNo, pageLength, searchText, isScored, 5);

		// Assert
		assertEquals(pets, result);
		verify(petBfiDao).getBfiPets(pageNo, pageLength, searchText, isScored, 5);
	}

	@Test
	public void testSavePetBfiImageScore() throws ServiceExecutionException {
		PetBfiImageScoreRequest request = new PetBfiImageScoreRequest();
		petBfiService.savePetBfiImageScore(request);

		// Assert
		verify(petBfiDao).savePetBfiImageScore(request);
	}
}
