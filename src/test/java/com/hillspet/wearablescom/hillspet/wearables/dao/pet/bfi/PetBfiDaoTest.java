package com.hillspet.wearablescom.hillspet.wearables.dao.pet.bfi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.test.util.ReflectionTestUtils;

import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.common.utils.GCPClientUtil;
import com.hillspet.wearables.dao.configuration.DataSourceConfig;
import com.hillspet.wearables.dao.pet.bfi.impl.PetBfiDaoImpl;
import com.hillspet.wearables.dto.BfiImageScore;
import com.hillspet.wearables.dto.PetBfiInstruction;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetFoodBrand;
import com.hillspet.wearables.dto.PetImagePosition;
import com.hillspet.wearables.request.PetBfiImageScoreRequest;
import com.hillspet.wearables.request.PetBfiImagesRequest;

public class PetBfiDaoTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Spy
	private DataSourceConfig dataSourceConfig;

	@Mock
	private GCPClientUtil gcpClientUtil;

	@InjectMocks
	private PetBfiDaoImpl petBfiDao = new PetBfiDaoImpl();

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(petBfiDao, "jdbcTemplate", jdbcTemplate);
		ReflectionTestUtils.setField(petBfiDao, "gcpClientUtil", gcpClientUtil);
	}

	@Mock
	private Logger logger;

	@Test
	public void testGetPetBfiInstructions() throws SQLException {
		// Mocking the ResultSet for the query
		System.out.println("testGetPetImagePositions called");
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.next()).thenReturn(true, false); // Simulating one row in the result set
		when(resultSet.getInt("BFI_INSTRUCTION_ID")).thenReturn(1);
		when(resultSet.getString("INSTRUCTION"))
				.thenReturn("Take photos in a well-lit area to avoid shadows and enhance image clarity.");
		when(resultSet.getString("IMAGE_URL")).thenReturn(
				"https://firebasestorage.googleapis.com:443/v0/b/ct-wearables-portal-pf.appspot.com/o/New_Pet_BFI_Score%2F74484120230825003049.jpg?alt=media&token=5671577a-841b-4192-9735-b991578900f2");
		when(resultSet.getInt("INSTRUCTION_TYPE")).thenReturn(1);
		when(resultSet.getInt("INSTRUCTION_ORDER")).thenReturn(1);

		// Mock the behavior of jdbcTemplate.query
		doAnswer(invocation -> {
			RowCallbackHandler rowCallbackHandler = invocation.getArgument(1);
			// Simulate the behavior of processRow
			rowCallbackHandler.processRow(resultSet);
			return null;
		}).when(jdbcTemplate).query(anyString(), any(RowCallbackHandler.class), anyInt());

		// Calling the method under test
		List<PetBfiInstruction> petBfiInstructions = petBfiDao.getPetBfiInstructions(1);

		// Assertions
		assertEquals(1, petBfiInstructions.size());
		PetBfiInstruction bfiInstruction = petBfiInstructions.get(0);
		assertEquals(1, bfiInstruction.getInstructionId().intValue());
		assertEquals("Take photos in a well-lit area to avoid shadows and enhance image clarity.",
				bfiInstruction.getInstruction());
		assertEquals(1, bfiInstruction.getInstructionType());
	}

	@Test
	public void testGetPetImagePositions() throws SQLException {
		// Mocking the ResultSet for the query
		System.out.println("testGetPetImagePositions called");
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.next()).thenReturn(true, false); // Simulating one row in the result set
		when(resultSet.getInt("PET_IMAGE_POSITION_ID")).thenReturn(1);
		when(resultSet.getString("IMAGE_POSITION")).thenReturn("Front");
		when(resultSet.getBoolean("IS_MANDATORY")).thenReturn(true);

		// Mock the behavior of jdbcTemplate.query
		doAnswer(invocation -> {
			RowCallbackHandler rowCallbackHandler = invocation.getArgument(1);
			// Simulate the behavior of processRow
			rowCallbackHandler.processRow(resultSet);
			return null;
		}).when(jdbcTemplate).query(anyString(), any(RowCallbackHandler.class));

		// Calling the method under test
		List<PetImagePosition> imagePositions = petBfiDao.getPetImagePositions();

		// Assertions
		assertEquals(1, imagePositions.size());
		PetImagePosition position = imagePositions.get(0);
		assertEquals(1, position.getImagePositionId().intValue());
		assertEquals("Front", position.getImagePositionName());
		assertEquals(true, position.getIsMandatory());
	}

	@Test
	public void testGetBfiImageScores() throws SQLException {
		// Mocking the ResultSet for the query
		System.out.println("testGetBfiImageScores called");
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.next()).thenReturn(true, false); // Simulating one row in the result set
		when(resultSet.getInt("BFI_IMAGE_SCORE_ID")).thenReturn(2);
		when(resultSet.getInt("SPECIES_ID")).thenReturn(1);
		when(resultSet.getString("SCORE")).thenReturn("20");
		when(resultSet.getString("IMAGE_URL")).thenReturn(
				"https://firebasestorage.googleapis.com:443/v0/b/ct-wearables-portal-pf.appspot.com/o/New_Pet_BFI_Score%2F74484120230825003049.jpg?alt=media&token=5671577a-841b-4192-9735-b991578900f2");
		when(resultSet.getString("IMAGE_LABEL")).thenReturn("Healthy Weight");
		when(resultSet.getString("RANGE")).thenReturn("15-25% Body Fat");
		when(resultSet.getString("INSTRUCTIONS")).thenReturn(
				"[{\"order\":1,\"header\":\"Ribs\",\"description\":[\"Slightly prominent\",\"Easily felt\",\"Thin fat cover\"]},{\"order\":2,\"header\":\"Shape From Above\",\"description\":[\"Well proportioned lumbar waist\"]},{\"order\":3,\"header\":\"Shape From the Side\",\"description\":[\"Abdominal tuck present\"]},{\"order\":4,\"header\":\"Shape From Behind\",\"description\":[\"Clear muscle definition\",\"smooth contour\"]},{\"order\":5,\"header\":\"Tail Base Bones\",\"description\":[\"Slightly prominent\",\"Easily felt\"]},{\"order\":6,\"header\":\"Tail Base Fat\",\"description\":[\"Thin fat cover\"]}]");

		// Mock the behavior of jdbcTemplate.query
		doAnswer(invocation -> {
			RowCallbackHandler rowCallbackHandler = invocation.getArgument(1);
			// Simulate the behavior of processRow
			rowCallbackHandler.processRow(resultSet);
			return null;
		}).when(jdbcTemplate).query(anyString(), any(RowCallbackHandler.class), anyInt());

		// Calling the method under test
		List<BfiImageScore> bfiImageScores = petBfiDao.getBfiImageScores(1);

		// Assertions
		assertEquals(1, bfiImageScores.size());
		BfiImageScore bfiImageScore = bfiImageScores.get(0);
		assertEquals(2, bfiImageScore.getBfiImageScoreId().intValue());
		assertEquals("Healthy Weight", bfiImageScore.getImageLabel());
		assertEquals("20", bfiImageScore.getScore());
	}

	@Test
	public void testGetPetFoodBrands() throws SQLException {
		// Mocking the ResultSet for the query
		System.out.println("testGetPetFoodBrands called");
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.next()).thenReturn(true, false); // Simulating one row in the result set
		when(resultSet.getInt("BRAND_ID")).thenReturn(1);
		when(resultSet.getInt("SPECIES_ID")).thenReturn(20);
		when(resultSet.getString("BRAND_NAME")).thenReturn("Acana Singles");
		when(resultSet.getString("COMPANY_NAME")).thenReturn("Champion");

		// Mock the behavior of jdbcTemplate.query
		doAnswer(invocation -> {
			RowCallbackHandler rowCallbackHandler = invocation.getArgument(1);
			// Simulate the behavior of processRow
			rowCallbackHandler.processRow(resultSet);
			return null;
		}).when(jdbcTemplate).query(anyString(), any(RowCallbackHandler.class), anyInt());

		// Calling the method under test
		List<PetFoodBrand> petFoodBrands = petBfiDao.getPetFoodBrands(1);

		// Assertions
		assertEquals(1, petFoodBrands.size());
		PetFoodBrand petFoodBrand = petFoodBrands.get(0);
		assertEquals(1, petFoodBrand.getBrandId().intValue());
		assertEquals("Acana Singles", petFoodBrand.getBrandName());
		assertEquals("Champion", petFoodBrand.getCompanyName());
	}

	@Test
	public void testSavePetBfiImages_Success() throws SQLException, ServiceExecutionException {
		System.out.println("testSavePetBfiImages_Success called");
		// Arrange
		PetBfiImagesRequest request = new PetBfiImagesRequest();
		request.setPetId(1);
		request.setPetParentId(2);
		// request.setStartTime(LocalDateTime.now());
		// request.setEndTime(LocalDateTime.now().plusMinutes(2));
		request.setCreatedBy(1);
		// Set up other properties of the request object...

		Map<String, Object> outParams = new HashMap<>();
		outParams.put("out_error_msg", ""); // Simulate a successful execution
		outParams.put("out_flag", 1); // Simulate a success flag
		outParams.put("last_insert_id", 123); // Simulate the last insert ID

		// Mock the behavior of the callStoredProcedure method
		PetBfiDaoImpl spyPetBfiDao = spy(new PetBfiDaoImpl());

		doReturn(outParams).when(spyPetBfiDao).callStoredProcedure(anyString(), anyMap());
		spyPetBfiDao.savePetBfiImages(request);
	}

	@Test
	public void testSavePetBfiImages_Failure() throws SQLException {
		System.out.println("testSavePetBfiImages_Failure called");
		// Arrange
		PetBfiImageScoreRequest request = new PetBfiImageScoreRequest();
		request.setPetBfiImageSetId(8);
		request.setBfiImageScoreId(2);
		request.setStartTime(LocalDateTime.now());
		request.setEndTime(LocalDateTime.now().plusMinutes(2));
		request.setCreatedBy(1);
		// Set up other properties of the request object...

		Map<String, Object> outParams = new HashMap<>();
		outParams.put("out_error_msg", "An error occurred");
		outParams.put("out_flag", -1);

		// Mock the behavior of the callStoredProcedure method
		PetBfiDaoImpl spyPetBfiDao = spy(new PetBfiDaoImpl());

		// Mock the behavior of the callStoredProcedure method
		doReturn(outParams).when(spyPetBfiDao).callStoredProcedure(any(), any());

		// Act and Assert
		ServiceExecutionException exception = org.junit.jupiter.api.Assertions
				.assertThrows(ServiceExecutionException.class, () -> spyPetBfiDao.savePetBfiImageScore(request));

		assertEquals("An error occurred", exception.getMessage());
	}

	@Test
	public void testGetBfiPets() throws Exception {
		System.out.println("testGetBfiPets called");
		// Mock the data in the ResultSet
		int expectedPetId = 1;
		String expectedPetName = "Powny";
		String expectedPhotoName = "image.jpg";

		// Mock the ResultSet for the query
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getInt("PET_ID")).thenReturn(expectedPetId);
		when(resultSet.getString("PET_NAME")).thenReturn(expectedPetName);
		when(resultSet.getString("PHOTO_NAME")).thenReturn(expectedPhotoName);
		when(resultSet.getString("weightData")).thenReturn("6.3###2.9###lbs");
		when(resultSet.getString("PET_AGE")).thenReturn("6 yrs 5 mos");
		when(resultSet.getString("BREED_NAME")).thenReturn("Dalmatian");
		when(resultSet.getString("SPECIES_ID")).thenReturn("1");
		when(resultSet.getString("BIRTHDAY")).thenReturn("06/01/2017 00:00:00");
		when(resultSet.getString("GENDER")).thenReturn("Male");
		when(resultSet.getBoolean("IS_NEUTERED")).thenReturn(true);
		when(resultSet.getInt("PET_STATUS_ID")).thenReturn(2);
		when(resultSet.getString("BFI_IMAGE_DETAILS")).thenReturn(
				"[{\"petId\": 4711, \"petParentId\": 3693, \"petBfiImages\": [{\"imageUrl\": \"https://firebasestorage.googleapis.com/v0/b/ct-wearables-portal-pf.appspot.com/o/BFI_Score_App_Images%2Fab173cb5-5544-44ef-9054-dc6703aeb36d.JPEG?alt=media&token=1cd6c8b0-5b6e-4bd9-bb43-7b277cf84838\", \"imageName\": \"BFI_Score_App_Images/ab173cb5-5544-44ef-9054-dc6703aeb36d.JPEG\", \"thumbnailUrl\": \"https://firebasestorage.googleapis.com/v0/b/ct-wearables-portal-pf.appspot.com/o/BFI_Score_App_Images%2Fe5bfad77-ab1b-40af-ab31-841980b647e5.jpg?alt=media&token=4c019964-eb75-4432-ac1e-51385c629f7c\", \"imagePosition\": \"Full Body\", \"imagePositionId\": 6}, {\"imageUrl\": \"https://firebasestorage.googleapis.com/v0/b/ct-wearables-portal-pf.appspot.com/o/BFI_Score_App_Images%2Fmrousavy6609426488091504440.jpg?alt=media&token=67ed6e8c-7826-4674-95a6-2b6051920e0d\", \"imageName\": \"BFI_Score_App_Images/mrousavy6609426488091504440.jpg\", \"thumbnailUrl\": \"https://firebasestorage.googleapis.com/v0/b/ct-wearables-portal-pf.appspot.com/o/BFI_Score_App_Images%2Fbbfb8cfe-e95a-4529-aac9-4e1bc0ccc561.jpg?alt=media&token=a2cc9c69-855a-489e-88b6-80276209428b\", \"imagePosition\": \"Front\", \"imagePositionId\": 1}], \"petBfiImagesSetId\": 15}]");

		// Mock GCPClientUtil behavior
		when(gcpClientUtil.getDownloaFiledUrl(expectedPhotoName, Constants.GCP_PET_PHOTO_PATH))
				.thenReturn("http://gooogle.cloudstorage/image.jpg");

		// Assuming there is one row in the result set
		doAnswer(invocation -> {
			RowCallbackHandler rowCallbackHandler = invocation.getArgument(1);
			rowCallbackHandler.processRow(resultSet);
			return null;
		}).when(jdbcTemplate).query(any(String.class), any(RowCallbackHandler.class), anyInt(), anyInt(),
				any(String.class), any(Boolean.class), anyInt());

		// Calling the method under test
		List<PetDTO> result = petBfiDao.getBfiPets(1, 5, "Powny", true, 5);

		// Assertions
		assertEquals(1, result.size());
		PetDTO actualPet = result.get(0);
		assertEquals(expectedPetId, actualPet.getPetID());
		assertEquals(expectedPetName, actualPet.getPetName());
		assertEquals("http://gooogle.cloudstorage/image.jpg", actualPet.getPhotoUrl());
	}

	@Test
	public void testSavePetBfiImageScore_Success() throws SQLException, ServiceExecutionException {
		System.out.println("testSavePetBfiImageScore_Success called");
		// Arrange
		PetBfiImageScoreRequest request = new PetBfiImageScoreRequest();
		request.setPetBfiImageSetId(8);
		request.setBfiImageScoreId(2);
		request.setStartTime(LocalDateTime.now());
		request.setEndTime(LocalDateTime.now().plusMinutes(2));
		request.setCreatedBy(1);
		// Set up other properties of the request object...

		Map<String, Object> outParams = new HashMap<>();
		outParams.put("out_error_msg", ""); // Simulate a successful execution
		outParams.put("out_flag", 1); // Simulate a success flag
		outParams.put("last_insert_id", 123); // Simulate the last insert ID

		// Mock the behavior of the callStoredProcedure method
		PetBfiDaoImpl spyPetBfiDao = spy(new PetBfiDaoImpl());

		doReturn(outParams).when(spyPetBfiDao).callStoredProcedure(anyString(), anyMap());
		spyPetBfiDao.savePetBfiImageScore(request);
	}

	@Test
	public void testSavePetBfiImageScore_Failure() throws SQLException {
		System.out.println("testSavePetBfiImageScore_Failure called");
		// Arrange
		PetBfiImageScoreRequest request = new PetBfiImageScoreRequest();
		request.setPetBfiImageSetId(8);
		request.setBfiImageScoreId(2);
		request.setStartTime(LocalDateTime.now());
		request.setEndTime(LocalDateTime.now().plusMinutes(2));
		request.setCreatedBy(1);
		// Set up other properties of the request object...

		Map<String, Object> outParams = new HashMap<>();
		outParams.put("out_error_msg", "An error occurred");
		outParams.put("out_flag", -1);

		// Mock the behavior of the callStoredProcedure method
		PetBfiDaoImpl spyPetBfiDao = spy(new PetBfiDaoImpl());

		// Mock the behavior of the callStoredProcedure method
		doReturn(outParams).when(spyPetBfiDao).callStoredProcedure(any(), any());

		// Act and Assert
		ServiceExecutionException exception = org.junit.jupiter.api.Assertions
				.assertThrows(ServiceExecutionException.class, () -> spyPetBfiDao.savePetBfiImageScore(request));

		assertEquals("An error occurred", exception.getMessage());
	}

}
