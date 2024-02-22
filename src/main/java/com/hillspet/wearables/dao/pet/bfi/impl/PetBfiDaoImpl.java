package com.hillspet.wearables.dao.pet.bfi.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hillspet.wearables.common.utils.WearablesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.common.utils.GCPClientUtil;
import com.hillspet.wearables.configuration.LocalDateTimeDeserializer;
import com.hillspet.wearables.dao.BaseDaoImpl;
import com.hillspet.wearables.dao.pet.bfi.PetBfiDao;
import com.hillspet.wearables.dto.BfiImageScore;
import com.hillspet.wearables.dto.BfiScoreInstruction;
import com.hillspet.wearables.dto.DeviceDTO;
import com.hillspet.wearables.dto.PetBfiInfo;
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
@Repository
public class PetBfiDaoImpl extends BaseDaoImpl implements PetBfiDao {

	@Value("${gcp.env}")
	private String environment;

	@Autowired
	private GCPClientUtil gcpClientUtil;

	public static final String MOBILE_APP_GET_PET_IMAGE_POSITIONS = "CALL MOBILE_APP_GET_PET_IMAGE_POSITIONS()";
	public static final String MOBILE_APP_GET_PET_BFI_INSTRUCTIONS = "CALL MOBILE_APP_GET_PET_BFI_INSTRUCTIONS(?)";
	public static final String MOBILE_APP_GET_BFI_IMAGE_SCORES = "CALL MOBILE_APP_GET_BFI_IMAGE_SCORES(?)";
	public static final String MOBILE_APP_GET_PET_FOOD_BRANDS = "CALL MOBILE_APP_GET_PET_FOOD_BRANDS(?)";
	public static final String MOBILE_APP_GET_PETS_TO_CAPTURE_BFI_IMAGES = "CALL MOBILE_APP_GET_PETS_TO_CAPTURE_BFI_IMAGES(?,?,?,?,?,?)";
	public static final String MOBILE_APP_INSERT_PET_BFI_IMAGES = "MOBILE_APP_INSERT_PET_BFI_IMAGES";
	public static final String MOBILE_APP_GET_PET_BFI_INFO = "CALL MOBILE_APP_GET_PET_BFI_INFO(?,?,?,?,?)";
	public static final String MOBILE_APP_GET_PET_BFI_IMAGES = "CALL MOBILE_APP_GET_PET_BFI_IMAGES(?,?,?,?)";
	public static final String MOBILE_APP_INSERT_PET_BFI_IMAGE_SCORE = "MOBILE_APP_INSERT_PET_BFI_IMAGE_SCORE";

	private static final Logger LOGGER = LogManager.getLogger(PetBfiDaoImpl.class);

	@Override
	public List<PetBfiInstruction> getPetBfiInstructions(int instructionType) throws ServiceExecutionException {
		List<PetBfiInstruction> petBfiInstructions = new ArrayList<>();
		LOGGER.debug("getPetBfiInstructions called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_BFI_INSTRUCTIONS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetBfiInstruction petBfiInstruction = new PetBfiInstruction();
					petBfiInstruction.setInstructionId(rs.getInt("BFI_INSTRUCTION_ID"));
					petBfiInstruction.setInstruction(rs.getString("INSTRUCTION"));
					petBfiInstruction.setImageUrl(rs.getString("IMAGE_URL"));
					petBfiInstruction.setInstructionType(rs.getInt("INSTRUCTION_TYPE"));
					petBfiInstruction.setInstructionOrder(rs.getInt("INSTRUCTION_ORDER"));
					petBfiInstructions.add(petBfiInstruction);
				}
			}, instructionType);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetBfiInstructions ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petBfiInstructions;
	}

	@Override
	public List<PetImagePosition> getPetImagePositions() throws ServiceExecutionException {
		List<PetImagePosition> imagePositions = new ArrayList<>();
		LOGGER.debug("getPetImagePositions called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_IMAGE_POSITIONS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetImagePosition imagePosition = new PetImagePosition();
					imagePosition.setImagePositionId(rs.getInt("PET_IMAGE_POSITION_ID"));
					imagePosition.setImagePositionName(rs.getString("IMAGE_POSITION"));
					imagePosition.setIsMandatory(rs.getBoolean("IS_MANDATORY"));
					imagePositions.add(imagePosition);
				}
			});
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetImagePositions ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return imagePositions;
	}

	@Override
	public List<BfiImageScore> getBfiImageScores(int speciesId) throws ServiceExecutionException {
		List<BfiImageScore> bfiImageScores = new ArrayList<>();
		LOGGER.debug("getBfiImageScores called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_BFI_IMAGE_SCORES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					BfiImageScore bfiImageScore = new BfiImageScore();
					bfiImageScore.setBfiImageScoreId(rs.getInt("BFI_IMAGE_SCORE_ID"));
					bfiImageScore.setSpeciesId(rs.getInt("SPECIES_ID"));
					bfiImageScore.setScore(rs.getString("SCORE"));
					bfiImageScore.setImageUrl(rs.getString("IMAGE_URL"));
					bfiImageScore.setImageLabel(rs.getString("IMAGE_LABEL"));
					bfiImageScore.setRange(rs.getString("RANGE"));
					if (rs.getString("INSTRUCTIONS") != null) {
						try {
							bfiImageScore.setInstructions(new ObjectMapper().readValue(rs.getString("INSTRUCTIONS"),
									new TypeReference<List<BfiScoreInstruction>>() {
									}));
						} catch (JsonProcessingException e) {
							LOGGER.error("error while converting INSTRUCTIONS in getBfiImageScores", e);
						}
					}
					bfiImageScores.add(bfiImageScore);
				}
			}, speciesId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getBfiImageScores", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return bfiImageScores;
	}

	@Override
	public List<PetFoodBrand> getPetFoodBrands(int speciesId) throws ServiceExecutionException {
		List<PetFoodBrand> petFoodBrands = new ArrayList<>();
		LOGGER.debug("getPetFoodBrands called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_FOOD_BRANDS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetFoodBrand petFoodBrand = new PetFoodBrand();
					petFoodBrand.setBrandId(rs.getInt("BRAND_ID"));
					petFoodBrand.setSpeciesId(rs.getInt("SPECIES_ID"));
					petFoodBrand.setBrandName(rs.getString("BRAND_NAME"));
					petFoodBrand.setCompanyName(rs.getString("COMPANY_NAME"));
					petFoodBrands.add(petFoodBrand);
				}
			}, speciesId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetFoodBrands", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petFoodBrands;
	}

	@Override
	public List<PetDTO> getPetsToCaptureBfiImages(int petParentId, int speciesId, int pageNo, int pageLength,
			String searchText, int userId) throws ServiceExecutionException {
		LOGGER.debug("getPetsToCaptureBfiImages called");
		List<PetDTO> pets = new ArrayList<>();
		Map<Integer, List<DeviceDTO>> devicesMap = new HashMap<>();
		long startTime = System.currentTimeMillis();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PETS_TO_CAPTURE_BFI_IMAGES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetDTO petDTO = new PetDTO();
					petDTO.setPetID(rs.getInt("PET_ID"));
					petDTO.setPetName(rs.getString("PET_NAME"));
					String imageName = rs.getString("PHOTO_NAME");

					petDTO.setPetParentId(rs.getInt("PET_PARENT_ID"));
					petDTO.setPetParentName(rs.getString("PET_PARENT_FULL_NAME"));
					petDTO.setPetParentEmail(rs.getString("EMAIL"));

					/*Address petAddress = null;
					try {
						if (StringUtils.isNotEmpty(rs.getString("PET_ADDRESS_JSON"))) {
							petAddress = new ObjectMapper().readValue(rs.getString("PET_ADDRESS_JSON"), Address.class);
						}
					} catch (JsonProcessingException | SQLException e) {
						e.printStackTrace();
					}
					petDTO.setPetAddress(petAddress == null ? new Address() : petAddress);*/

					if (StringUtils.isNotEmpty(imageName)) {
						petDTO.setPhotoUrl(gcpClientUtil.getDownloaFiledUrl(imageName, Constants.GCP_PET_PHOTO_PATH));
					}
					if (StringUtils.isNotEmpty(rs.getString("weightData"))) {
						String[] weightData = rs.getString("weightData").split("###");
						if (("lbs").equals(weightData[Constants.NUMBER_TWO])) {
							petDTO.setWeight(weightData[Constants.NUMBER_ZERO]);
						} else {
							petDTO.setWeight(weightData[Constants.NUMBER_ONE]);
						}
						petDTO.setWeightUnit(weightData[Constants.NUMBER_TWO]);
					}
					petDTO.setPetAge(rs.getString("PET_AGE"));
					petDTO.setPetBreed(rs.getString("BREED_NAME"));
					petDTO.setSpeciesId(rs.getString("SPECIES_ID"));
					petDTO.setBirthday(rs.getString("BIRTHDAY"));
					petDTO.setGender(rs.getString("GENDER"));
					petDTO.setIsNeutered(rs.getBoolean("IS_NEUTERED"));
					petDTO.setPetStatus(rs.getInt("PET_STATUS_ID"));

					petDTO.setBrandId(rs.getInt("BRAND_ID"));
					petDTO.setBrandName(rs.getString("BRAND_NAME"));
					petDTO.setFeedUnit(rs.getInt("FEED_UNIT"));
					petDTO.setFoodIntake(rs.getDouble("FOOd_INTAKE"));

					pets.add(petDTO);
				}
			}, petParentId, speciesId, pageNo, pageLength, searchText, userId);
			long endTime = System.currentTimeMillis();
			LOGGER.info("Time taken to execute getPetDevicesByPetParent service in millis is {}",
					(endTime - startTime));
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetDevicesByPetParent", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return pets;
	}

	@Override
	public void savePetBfiImages(PetBfiImagesRequest petBfiImagesRequest) throws ServiceExecutionException {
		LOGGER.debug("savePetBfiImages called");
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", petBfiImagesRequest.getPetId());
			inputParams.put("p_pet_parent_id", petBfiImagesRequest.getPetParentId());
			inputParams.put("p_pet_info_json", objectMapper.writeValueAsString(petBfiImagesRequest.getPetInfo()));
			inputParams.put("p_pet_parent_info_json",
					objectMapper.writeValueAsString(petBfiImagesRequest.getPetParentInfo()));

			inputParams.put("p_pet_bfi_images_json",
					objectMapper.writeValueAsString(petBfiImagesRequest.getPetBfiImages()));
			inputParams.put("p_login_user_Id", petBfiImagesRequest.getCreatedBy());

			inputParams.put("p_pet_images_json",
					objectMapper.writeValueAsString(petBfiImagesRequest.getPetBfiImages()));
			LOGGER.info("savePetBfiImages inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_BFI_IMAGES, inputParams);
			LOGGER.info("savePetBfiImages outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int outFlag = (int) outParams.get("out_flag");
			if (outFlag > NumberUtils.INTEGER_ZERO) {
				int lastInsertId = (int) outParams.get("last_insert_id");
				LOGGER.info("Pet Bfi Images are uploaded successfully Pet Bfi Images Set Id {}", lastInsertId);
			} else {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error("error while executing savePetBfiImages ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public List<PetDTO> getBfiPets(int pageNo, int pageLength, String searchText, boolean isScored, int userId)
			throws ServiceExecutionException {
		List<PetDTO> pets = new ArrayList<>();
		LOGGER.debug("getBfiPets called");
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JavaTimeModule module = new JavaTimeModule();
			module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
			objectMapper.registerModule(module);

			jdbcTemplate.query(MOBILE_APP_GET_PET_BFI_INFO, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetDTO petDTO = new PetDTO();
					petDTO.setPetID(rs.getInt("PET_ID"));
					petDTO.setPetName(rs.getString("PET_NAME"));
					String imageName = rs.getString("PHOTO_NAME");

					if (StringUtils.isNotEmpty(imageName)) {
						petDTO.setPhotoUrl(gcpClientUtil.getDownloaFiledUrl(imageName, Constants.GCP_PET_PHOTO_PATH));
					}
					if (StringUtils.isNotEmpty(rs.getString("weightData"))) {
						String[] weightData = rs.getString("weightData").split("###");
						if (("lbs").equals(weightData[Constants.NUMBER_TWO])) {
							petDTO.setWeight(weightData[Constants.NUMBER_ZERO]);
						} else {
							petDTO.setWeight(weightData[Constants.NUMBER_ONE]);
						}
						petDTO.setWeightUnit(weightData[Constants.NUMBER_TWO]);
					}
					petDTO.setPetAge(rs.getString("PET_AGE"));
					petDTO.setPetBreed(rs.getString("BREED_NAME"));
					petDTO.setSpeciesId(rs.getString("SPECIES_ID"));
					petDTO.setBirthday(rs.getString("BIRTHDAY"));
					petDTO.setGender(rs.getString("GENDER"));
					petDTO.setIsNeutered(rs.getBoolean("IS_NEUTERED"));
					petDTO.setPetStatus(rs.getInt("PET_STATUS_ID"));
					petDTO.setPetBfiImageSetIds(rs.getString("PET_BFI_IMAGE_SET_IDs"));

					petDTO.setBfiScore(rs.getString("SCORE_VALUE"));
					/*List<PetBfiInfo> petBfiInfos = null;
					try {
						petBfiInfos = objectMapper.readValue(rs.getString("BFI_IMAGE_DETAILS"),
								new TypeReference<List<PetBfiInfo>>() {
								});
					} catch (JsonProcessingException e) {
						LOGGER.error("error while converting BFI_IMAGE_DETAILS json in getBfiPets", e);
					}
					petDTO.setPetBfiInfos(petBfiInfos);*/

					pets.add(petDTO);
				}
			}, pageNo, pageLength, searchText, isScored, userId);
			LOGGER.debug("getBfiPets successfully completed");
		} catch (Exception e) {
			LOGGER.error("error while fetching getBfiPets", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return pets;
	}

	@Override
	public List<PetDTO> getPetBfiImages(int petId, String petBfiImageSetIds, boolean isScored, int userId)
			throws ServiceExecutionException {
		List<PetDTO> pets = new ArrayList<>();
		LOGGER.debug("getPetBfiImages called");
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JavaTimeModule module = new JavaTimeModule();
			module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
			objectMapper.registerModule(module);

			jdbcTemplate.query(MOBILE_APP_GET_PET_BFI_IMAGES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetDTO petDTO = new PetDTO();
					petDTO.setPetID(rs.getInt("PET_ID"));
					petDTO.setPetName(rs.getString("PET_NAME"));
					String imageName = rs.getString("PHOTO_NAME");

					if (StringUtils.isNotEmpty(imageName)) {
						petDTO.setPhotoUrl(gcpClientUtil.getDownloaFiledUrl(imageName, Constants.GCP_PET_PHOTO_PATH));
					}
					if (StringUtils.isNotEmpty(rs.getString("weightData"))) {
						String[] weightData = rs.getString("weightData").split("###");
						if (("lbs").equals(weightData[Constants.NUMBER_TWO])) {
							petDTO.setWeight(weightData[Constants.NUMBER_ZERO]);
						} else {
							petDTO.setWeight(weightData[Constants.NUMBER_ONE]);
						}
						petDTO.setWeightUnit(weightData[Constants.NUMBER_TWO]);
					}
					petDTO.setPetAge(rs.getString("PET_AGE"));
					petDTO.setPetBreed(rs.getString("BREED_NAME"));
					petDTO.setSpeciesId(rs.getString("SPECIES_ID"));
					petDTO.setBirthday(rs.getString("BIRTHDAY"));
					petDTO.setGender(rs.getString("GENDER"));
					petDTO.setIsNeutered(rs.getBoolean("IS_NEUTERED"));
					petDTO.setPetStatus(rs.getInt("PET_STATUS_ID"));

					List<PetBfiInfo> petBfiInfos = null;
					try {
						petBfiInfos = objectMapper.readValue(rs.getString("BFI_IMAGE_DETAILS"),
								new TypeReference<List<PetBfiInfo>>() {
								});
					} catch (JsonProcessingException e) {
						LOGGER.error("error while converting BFI_IMAGE_DETAILS json in getBfiPets", e);
					}
					if (isScored) {
						petBfiInfos = petBfiInfos.stream()
								.sorted((o1, o2) -> o2.getSubmittedOn().compareTo(o1.getSubmittedOn()))
								.collect(Collectors.toList());
					} else {
						petBfiInfos = petBfiInfos.stream()
								.sorted((o1, o2) -> o2.getCapturedOn().compareTo(o1.getCapturedOn()))
								.collect(Collectors.toList());
					}
					petDTO.setPetBfiInfos(petBfiInfos);

					pets.add(petDTO);
				}
			}, petId, petBfiImageSetIds, isScored, userId);
			LOGGER.debug("getPetBfiImages successfully completed");
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetBfiImages", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return pets;
	}

	@Override
	public void savePetBfiImageScore(PetBfiImageScoreRequest petBfiImageScoreRequest) throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_bfi_image_set_id", petBfiImageScoreRequest.getPetBfiImageSetId());
			inputParams.put("p_bfi_image_score_id", petBfiImageScoreRequest.getBfiImageScoreId());
			inputParams.put("p_description", petBfiImageScoreRequest.getDescription());
			inputParams.put("p_start_time", petBfiImageScoreRequest.getStartTime());
			inputParams.put("p_end_time", petBfiImageScoreRequest.getEndTime());
			inputParams.put("p_login_user_id", petBfiImageScoreRequest.getCreatedBy());

			LOGGER.info("savePetBfiImageScore inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_BFI_IMAGE_SCORE, inputParams);
			LOGGER.info("savePetBfiImageScore outParams are {}", outParams);
			// System.out.println(outParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int outFlag = (int) outParams.get("out_flag");
			if (outFlag > NumberUtils.INTEGER_ZERO) {
				int lastInsertId = (int) outParams.get("last_insert_id");
				LOGGER.info("Pet BFI Images are uploaded successfully Pet BFI Image score Id {}", lastInsertId);
				//Added by deepak on 08-02-24
				LOGGER.info("savePetBfiImageScore outputParams are ibw_flag {}", outParams.get("ibw_flag"));
				int ibwUpdatedPetId = outParams.get("ibw_flag")!=null ? (int)outParams.get("ibw_flag") :0;
				LOGGER.info("savePetBfiImageScore outputParams are ibwUpdatedPetId {}", ibwUpdatedPetId);
				if(ibwUpdatedPetId>0){
					// Added by deepak on 08-02-24 to Trigger AF engine on IBW Change.
					WearablesUtils.invokeAFEngine(ibwUpdatedPetId);
				}
				//End
			} else {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing savePetBfiImageScore ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

}