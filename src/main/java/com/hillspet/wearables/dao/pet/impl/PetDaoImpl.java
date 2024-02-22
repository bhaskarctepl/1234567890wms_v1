package com.hillspet.wearables.dao.pet.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.constants.WearablesErrorCode;
import com.hillspet.wearables.common.dto.WearablesError;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.common.utils.BigQueryServiceUtil;
import com.hillspet.wearables.common.utils.GCPClientUtil;
import com.hillspet.wearables.common.utils.WearablesUtils;
import com.hillspet.wearables.configuration.LocalDateTimeDeserializer;
import com.hillspet.wearables.dao.BaseDaoImpl;
import com.hillspet.wearables.dao.pet.PetDao;
import com.hillspet.wearables.dto.Address;
import com.hillspet.wearables.dto.BehaviorHistory;
import com.hillspet.wearables.dto.DeviceDTO;
import com.hillspet.wearables.dto.EatingEnthusiasmScale;
import com.hillspet.wearables.dto.ImageScoringScale;
import com.hillspet.wearables.dto.ImageScoringScaleDetails;
import com.hillspet.wearables.dto.ObservationPhoto;
import com.hillspet.wearables.dto.ObservationVideo;
import com.hillspet.wearables.dto.PetBehavior;
import com.hillspet.wearables.dto.PetBreed;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetFeedingEnthusiasmScale;
import com.hillspet.wearables.dto.PetFeedingPreference;
import com.hillspet.wearables.dto.PetFeedingTime;
import com.hillspet.wearables.dto.PetForwardMotionGoalSetting;
import com.hillspet.wearables.dto.PetForwardMotionInfo;
import com.hillspet.wearables.dto.PetIBWHistoryDTO;
import com.hillspet.wearables.dto.PetMobileAppConfig;
import com.hillspet.wearables.dto.PetObservation;
import com.hillspet.wearables.dto.PetSleepInfo;
import com.hillspet.wearables.dto.PetSpecies;
import com.hillspet.wearables.dto.PetWeightChartDTO;
import com.hillspet.wearables.dto.PetWeightDTO;
import com.hillspet.wearables.dto.PetWeightHistoryDTO;
import com.hillspet.wearables.request.AddPetWeight;
import com.hillspet.wearables.request.AssignSensorRequest;
import com.hillspet.wearables.request.BehaviorHistoryRequest;
import com.hillspet.wearables.request.FMGoalSettingRequest;
import com.hillspet.wearables.request.PetAddFeedingPreferences;
import com.hillspet.wearables.request.PetAddImageScoring;
import com.hillspet.wearables.request.PetIds;
import com.hillspet.wearables.request.UpdatePet;
import com.hillspet.wearables.request.UpdatePetWeight;
import com.hillspet.wearables.response.BehaviorVisualizationResponse;
import com.hillspet.wearables.response.PetAddressResponse;
import com.hillspet.wearables.response.PetWeightHistoryResponse;

@Repository
public class PetDaoImpl extends BaseDaoImpl implements PetDao {

	@Value("${gcp.env}")
	private String environment;

	@Autowired
	private GCPClientUtil gcpClientUtil;

	public static final String RESULT_SET_1 = "#result-set-1";
	public static final String RESULT_SET_2 = "#result-set-2";
	public static final String RESULT_SET_3 = "#result-set-3";

	private static String MOBILE_APP_ADD_PET_WEIGHT = "MOBILE_APP_ADD_PET_WEIGHT";
	private static String MOBILE_APP_UPDATE_PET_WEIGHT = "MOBILE_APP_UPDATE_PET_WEIGHT";
	private static String MOBILE_APP_UPDATE_PET_PHOTO = "MOBILE_APP_UPDATE_PET_PHOTO";
	private static String MOBILE_APP_GET_PET_WEIGHT_HISTORY = "MOBILE_APP_GET_PET_WEIGHT_HISTORY";
	private static String MOBILE_APP_GET_SPECIES = "CALL MOBILE_APP_GET_SPECIES()";
	private static String MOBILE_APP_GET_PET_BREEDS_BY_SPEICES = "CALL MOBILE_APP_GET_PET_BREEDS_BY_SPEICES(?)";
	private static String MOBILE_APP_GET_PET_BEHAVIORS_BY_SPECIES = "CALL MOBILE_APP_GET_PET_BEHAVIORS_BY_SPECIES(?,?)";
	private static String MOBILE_APP_GET_ENTHUSIASM_SCALES = "CALL MOBILE_APP_GET_ENTHUSIASM_SCALES(?)";
	private static String MOBILE_APP_GET_PET_FEEDING_TIMES = "CALL MOBILE_APP_GET_PET_FEEDING_TIMES()";
	private static String MOBILE_APP_GET_MOBILE_APP_CONFIGS_BY_PET_ID = "CALL MOBILE_APP_GET_MOBILE_APP_CONFIGS_BY_PET_ID(?)";
	private static String MOBILE_APP_INSERT_PET_FEEDING_ENTHUSIASM_SCALE = "MOBILE_APP_INSERT_PET_FEEDING_ENTHUSIASM_SCALE";
	private static String MOBILE_APP_SAVE_OBSERVATION_INFO = "MOBILE_APP_SAVE_OBSERVATION_INFO";
	private static String MOBILE_APP_DELETE_OBSERVATION_INFO = "MOBILE_APP_DELETE_OBSERVATION_INFO";
	private static String MOBILE_APP_GET_PET_IMAGE_SCORINGS = "MOBILE_APP_GET_PET_IMAGE_SCORINGS";
	private static String MOBILE_APP_INSERT_PET_IMAGE_SCORINGS = "MOBILE_APP_INSERT_PET_IMAGE_SCORINGS";
	private static String MOBILE_APP_GET_PET_FEEDING_PREFERENCES = "CALL MOBILE_APP_GET_PET_FEEDING_PREFERENCES()";
	private static String MOBILE_APP_INSERT_PET_FEEDING_PREFERENCES = "MOBILE_APP_INSERT_PET_FEEDING_PREFERENCES";
	private static String MOBILE_APP_GET_OBSERVATIONS_BY_PET_ID = "MOBILE_APP_GET_OBSERVATIONS_BY_PET_ID";
	private static String MOBILE_APP_UPDATE_PET_PROFILE = "MOBILE_APP_UPDATE_PET_PROFILE";
	private static String MOBILE_APP_PET_GET_ADDRESSES_BY_ID = "CALL MOBILE_APP_PET_GET_ADDRESSES_BY_ID(?)";
	private static String MOBILE_APP_GET_PETS_BY_PET_PARENT_AND_APP_CONFIG = "CALL MOBILE_APP_GET_PETS_BY_PET_PARENT_AND_APP_CONFIG(?,?)";
	private static String BIG_QEURY_GET_BEHAVIOR_VISULAZATION = "SELECT PET_ID, TODAY_WALKING, TODAY_RUNNING, LAST_WEEK_FORWARD_MOTION_AVG, PREV_DAY_FORWARD_MOTION, "
			+ "PREV_DAY_TILL_NOW_FM, TODAY_NIGHT_SLEEP, TODAY_DAY_SLEEP, LAST_WEEK_SLEEP_AVG, PREV_DAY_SLEEP, PREV_DAY_TILL_NOW_SLEEP "
			+ "FROM HPN_AP_UAT5.BEHAVIOR_VISUALIZE_VW WHERE PET_ID={0};";

	private static String BIG_QEURY_GET_BEHAVIOR_HISTORY = "CALL HPN_AP_UAT5.PR_BEHAVIOUR_VISUALIZTION_DAILY_DATA({0},\"{1}\",\"{2}\");";
	private static String MOBILE_APP_GET_PET_FORWARD_MOTION_GOAL_SETTING = "CALL MOBILE_APP_GET_PET_FORWARD_MOTION_GOAL_SETTING(?)";

	public static final String MOBILE_APP_REPLACE_PET_SENSOR = "MOBILE_APP_REPLACE_PET_SENSOR";
	private static String MOBILE_APP_SAVE_FM_GOAL_SETTING = "MOBILE_APP_SAVE_FM_GOAL_SETTING";

	private static final Logger LOGGER = LogManager.getLogger(PetDaoImpl.class);

	@Override
	public PetWeightDTO addPetWeight(AddPetWeight addPetWeight) throws ServiceExecutionException {
		PetWeightDTO petWeightDTO = new PetWeightDTO();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", addPetWeight.getPetId());
			inputParams.put("p_weight", addPetWeight.getWeight());
			inputParams.put("p_weight_unit", addPetWeight.getWeightUnit());
			inputParams.put("p_user_id", addPetWeight.getUserId());
			inputParams.put("p_add_date", addPetWeight.getAddDate());

			LOGGER.info("addPetWeight inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_ADD_PET_WEIGHT, inputParams);
			LOGGER.info("addPetWeight outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			//Added by deepak on 08-02-24
			LOGGER.info("addPetWeight outputParams are ibw_flag {}", outParams.get("ibw_flag"));
			int ibwFlag = outParams.get("ibw_flag")!=null ? (int)outParams.get("ibw_flag") :0;
			LOGGER.info("addPetWeight outputParams are ibwFlag {}", outParams.get("ibwFlag"));
			if(ibwFlag>0){
				// Added by deepak on 08-02-24 to Trigger AF engine on IBW Change.
				WearablesUtils.invokeAFEngine(addPetWeight.getPetId());
			}
			//End
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				Integer petWeightId = (int) outParams.get("last_insert_id");
				addPetWeight.setPetWeightId(petWeightId);
			} else {
				throw new ServiceExecutionException(errorMsg);
			}
			BeanUtils.copyProperties(addPetWeight, petWeightDTO);
		} catch (Exception e) {
			LOGGER.error("error while executing addPetWeight ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petWeightDTO;
	}

	@Override
	public PetWeightDTO updateWeight(UpdatePetWeight updatePetWeight) throws ServiceExecutionException {
		PetWeightDTO petWeightDTO = new PetWeightDTO();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_weight_id", updatePetWeight.getPetWeightId());
			inputParams.put("p_pet_id", updatePetWeight.getPetId());
			inputParams.put("p_weight", updatePetWeight.getWeight());
			inputParams.put("p_weight_unit", updatePetWeight.getWeightUnit());
			inputParams.put("p_user_id", updatePetWeight.getUserId());
			inputParams.put("p_modified_date", updatePetWeight.getModifiedDate());

			LOGGER.info("updateWeight inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_UPDATE_PET_WEIGHT, inputParams);
			LOGGER.info("updateWeight outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			//Added by deepak on 08-02-24
			LOGGER.info("updateWeight outputParams are ibw_flag {}", outParams.get("ibw_flag"));
			int ibwFlag = outParams.get("ibw_flag")!=null ? (int)outParams.get("ibw_flag") :0;
			LOGGER.info("updateWeight outputParams are ibwFlag {}", outParams.get("ibwFlag"));
			if(ibwFlag>0){
				// Added by deepak on 08-02-24 to Trigger AF engine on IBW Change.
				WearablesUtils.invokeAFEngine(updatePetWeight.getPetId());
			}
			//End
			if (StringUtils.isEmpty(errorMsg) && statusFlag < NumberUtils.INTEGER_ZERO) {
				throw new ServiceExecutionException(errorMsg);
			}
			BeanUtils.copyProperties(updatePetWeight, petWeightDTO);
		} catch (Exception e) {
			LOGGER.error("error while executing updateWeight ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petWeightDTO;
	}

	@Override
	public void updatePetPhoto(int petId, int petParentId, String petPhoto) throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", petId);
			inputParams.put("p_photo_name", petPhoto);
			inputParams.put("p_pet_parent_id", petParentId);

			LOGGER.info("updatePetPhoto inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_UPDATE_PET_PHOTO, inputParams);
			LOGGER.info("updatePetPhoto outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");

			if (StringUtils.isEmpty(errorMsg) && statusFlag < NumberUtils.INTEGER_ZERO) {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing updatePetPhoto ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PetWeightHistoryResponse getPetWeightHistory(int petId, String fromDate, String toDate)
			throws ServiceExecutionException {
		PetWeightHistoryResponse response = new PetWeightHistoryResponse();
		List<PetWeightHistoryDTO> weightList = new ArrayList<>();
		List<PetIBWHistoryDTO> ibwList = new ArrayList<>();
		List<PetWeightChartDTO> weightChartList = new ArrayList<>();
		Map<LocalDate, PetWeightChartDTO> dateMap = new HashMap<>();
		LOGGER.debug("getPetWeightHistory called");
		Map<String, Object> inputParams = new HashMap<String, Object>();
		try {
			inputParams.put("pet_id", petId);
			inputParams.put("start_date", fromDate);
			inputParams.put("end_date", toDate);

			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(MOBILE_APP_GET_PET_WEIGHT_HISTORY,
					inputParams);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(weight -> {
						PetWeightHistoryDTO dto = new PetWeightHistoryDTO();
						dto.setPetWeightId((Integer) weight.get("PET_WEIGHT_ID"));
						dto.setPetId((Integer) weight.get("PET_ID"));

						BigDecimal weightLbs = (BigDecimal) weight.get("WEIGHT_LBS");
						dto.setWeightLbs(weightLbs != null ? weightLbs.doubleValue() : 0);

						BigDecimal weightKgs = (BigDecimal) weight.get("WEIGHT_KGS");
						dto.setWeightKgs(weightKgs != null ? weightKgs.doubleValue() : 0);
						dto.setWeightUnit(
								(String) weight.get("WEIGHT_UNIT") == null ? null : (String) weight.get("WEIGHT_UNIT"));

						if (dto.getWeightUnit().equalsIgnoreCase("lbs")) {
							dto.setWeight(dto.getWeightLbs());
						} else {
							dto.setWeight(dto.getWeightKgs());
						}

						dto.setActive((boolean) weight.get("IS_ACTIVE"));
						dto.setAddDate((LocalDateTime) weight.get("ADD_DATE"));
						dto.setCreatedBy((Integer) weight.get("CREATED_BY"));
						dto.setModifiedBy((Integer) weight.get("MODIFIED_BY"));
						if (weight.get("CREATED_DATE") != null) {
							LocalDateTime createdDate = (LocalDateTime) weight.get("CREATED_DATE");
							dto.setCreatedDate(createdDate != null ? createdDate : null);
						}
						if (weight.get("MODIFIED_DATE") != null) {
							LocalDateTime modifiedDate = (LocalDateTime) weight.get("MODIFIED_DATE");
							dto.setModifiedDate(modifiedDate != null ? modifiedDate : null);
						}

						PetWeightChartDTO chartDto = new PetWeightChartDTO(dto.getAddDate().toLocalDate(),
								dto.getWeightKgs(), dto.getWeightLbs(), null, null);
						if (!dateMap.containsKey(dto.getAddDate().toLocalDate())) {
							dateMap.put(dto.getAddDate().toLocalDate(), chartDto);
							weightChartList.add(chartDto);
						}
						weightList.add(dto);
					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(ibw -> {
						PetIBWHistoryDTO dto = new PetIBWHistoryDTO();
						dto.setPetIBWId((Integer) ibw.get("PET_IBW_ID"));
						dto.setPetId((Integer) ibw.get("PET_ID"));

						BigDecimal ibwLbs = (BigDecimal) ibw.get("PET_IBW_LBS");
						dto.setIbwLbs(ibwLbs != null ? ibwLbs.doubleValue() : 0);

						BigDecimal ibwKgs = (BigDecimal) ibw.get("PET_IBW_KGS");
						dto.setIbwKgs(ibwKgs != null ? ibwKgs.doubleValue() : 0);

						dto.setIbwCalSource((String) ibw.get("IBW_CAL_SOURCE_NAME"));

						BigDecimal correctedIbwLbs = (BigDecimal) ibw.get("CORRECTED_IBW_LBS");
						dto.setCorrectedIBWLbs(correctedIbwLbs != null ? correctedIbwLbs.doubleValue() : 0);

						BigDecimal correctedIbwKgs = (BigDecimal) ibw.get("CORRECTED_IBW_KGS");
						dto.setCorrectedIBWKgs(correctedIbwKgs != null ? correctedIbwKgs.doubleValue() : 0);

						dto.setCorrectedIBWUnitId((Integer) ibw.get("CORRECTED_IBW_UNIT_ID"));
						dto.setCorrectedIBWUnit((String) ibw.get("UNIT"));
						dto.setComment((String) ibw.get("COMMENT"));

						dto.setRecordedDate((LocalDateTime) ibw.get("RECORDED_DATE"));

						dto.setCreatedBy((Integer) ibw.get("CREATED_BY"));
						dto.setModifiedBy((Integer) ibw.get("MODIFIED_BY"));

						if (ibw.get("CREATED_DATE") != null) {
							LocalDateTime createdDate = (LocalDateTime) ibw.get("CREATED_DATE");
							dto.setCreatedDate(createdDate != null ? createdDate : null);
						}
						if (ibw.get("MODIFIED_DATE") != null) {
							LocalDateTime modifiedDate = (LocalDateTime) ibw.get("MODIFIED_DATE");
							dto.setModifiedDate(modifiedDate != null ? modifiedDate : null);
						}

						dto.setGraphIBWLbs(dto.getCorrectedIBWLbs() != 0 ? dto.getCorrectedIBWLbs() : dto.getIbwLbs());
						dto.setGraphIBWKgs(dto.getCorrectedIBWKgs() != 0 ? dto.getCorrectedIBWKgs() : dto.getIbwKgs());

						PetWeightChartDTO chartDto = dateMap.get(dto.getRecordedDate().toLocalDate());

						if (chartDto != null) {
							if (chartDto.getIbwInKgs() == null) {
								chartDto.setIbwInKgs(dto.getGraphIBWKgs());
								chartDto.setIbwInLbs(dto.getGraphIBWLbs());
							}
						} else {
							chartDto = new PetWeightChartDTO(dto.getRecordedDate().toLocalDate(), null, null,
									dto.getGraphIBWKgs(), dto.getGraphIBWLbs());
							weightChartList.add(chartDto);
							dateMap.put(dto.getRecordedDate().toLocalDate(), chartDto);
						}
						ibwList.add(dto);
					});
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetWeightHistory", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		response.setPetWeightHistories(weightList);
		response.setIbwList(ibwList);
		response.setWeightChartList(weightChartList.stream().sorted(Comparator.comparing(PetWeightChartDTO::getDate))
				.collect(Collectors.toList()));
		return response;
	}

	@Override
	public List<PetSpecies> getPetSpecies() throws ServiceExecutionException {
		List<PetSpecies> speciesList = new ArrayList<>();
		LOGGER.debug("getPetSpecies called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_SPECIES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetSpecies species = new PetSpecies();
					species.setSpeciesId(rs.getInt("SPECIES_ID"));
					species.setSpeciesName(rs.getString("SPECIES_NAME"));
					speciesList.add(species);
				}
			});
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetSpecies ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return speciesList;
	}

	@Override
	public List<PetBreed> getPetBreeds(int speciesId) throws ServiceExecutionException {
		List<PetBreed> breedList = new ArrayList<>();
		LOGGER.debug("getPetBreeds called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_BREEDS_BY_SPEICES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetBreed petBreed = new PetBreed();
					petBreed.setBreedId(rs.getInt("BREED_ID"));
					petBreed.setBreedName(rs.getString("BREED_NAME"));
					breedList.add(petBreed);
				}
			}, speciesId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetBreeds", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return breedList;
	}

	@Override
	public List<PetBehavior> getPetBehaviors(int speciesId, int behaviorTypeId) throws ServiceExecutionException {
		List<PetBehavior> behaviorList = new ArrayList<>();
		LOGGER.debug("getPetBehaviors called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_BEHAVIORS_BY_SPECIES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetBehavior petBehavior = new PetBehavior();
					petBehavior.setBehaviorId(rs.getInt("METRIC_ID"));
					petBehavior.setBehaviorName(rs.getString("METRIC_NAME"));
					petBehavior.setSpeciesId(rs.getInt("SPECIES_ID"));
					petBehavior.setBehaviorTypeId(rs.getInt("BEHAVIOR_TYPE_ID"));
					petBehavior.setBehaviorType(rs.getString("BEHAVIOR_TYPE"));
					behaviorList.add(petBehavior);
				}
			}, speciesId, behaviorTypeId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetBehaviors", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return behaviorList;
	}

	@Override
	public List<EatingEnthusiasmScale> getPetEatingEnthusiasmScale(int speciesId) throws ServiceExecutionException {
		List<EatingEnthusiasmScale> eatingEnthusiasmScales = new ArrayList<>();
		LOGGER.debug("getPetEatingEnthusiasmScale called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_ENTHUSIASM_SCALES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					EatingEnthusiasmScale eatingEnthusiasmScale = new EatingEnthusiasmScale();
					eatingEnthusiasmScale.setEnthusiasmScaleId(rs.getInt("ENTHUSIASM_SCALE_ID"));
					eatingEnthusiasmScale.setEnthusiasmScaleValue(rs.getString("ENTHUSIASM_SCALE"));
					eatingEnthusiasmScale.setDescription(rs.getString("DESCRIPTION"));
					eatingEnthusiasmScale.setImageUrl(rs.getString("IMAGE_URL"));
					eatingEnthusiasmScale.setSpeciesId(rs.getInt("SPECIES_ID"));
					eatingEnthusiasmScales.add(eatingEnthusiasmScale);
				}
			}, speciesId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetEatingEnthusiasmScale", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return eatingEnthusiasmScales;
	}

	@Override
	public List<PetFeedingTime> getPetFeedingTime() throws ServiceExecutionException {
		List<PetFeedingTime> petFeedingTimes = new ArrayList<>();
		LOGGER.debug("getPetFeedingTime called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_FEEDING_TIMES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetFeedingTime petFeedingTime = new PetFeedingTime();
					petFeedingTime.setFeedingTimeId(rs.getInt("PET_FEEDING_TIME_ID"));
					petFeedingTime.setFeedingTime(rs.getString("FEEDING_VALUE"));
					petFeedingTimes.add(petFeedingTime);
				}
			});
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetFeedingTime ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petFeedingTimes;
	}

	@Override
	public Map<Integer, List<PetMobileAppConfig>> getMobileAppConfigs(PetIds petIds) throws ServiceExecutionException {
		List<PetMobileAppConfig> petMobileAppConfigs = new ArrayList<>();
		LOGGER.debug("getPetBreeds called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_MOBILE_APP_CONFIGS_BY_PET_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetMobileAppConfig petMobileAppConfig = new PetMobileAppConfig();
					petMobileAppConfig.setPetId(rs.getInt("PET_ID"));
					petMobileAppConfig.setMobileAppConfigId(rs.getInt("MOBILE_APP_CONFIG_ID"));
					petMobileAppConfig.setMobileAppConfigName(rs.getString("MOBILE_APP_CONFIG_NAME"));
					petMobileAppConfig.setWeightUnit(rs.getString("WEIGHT_UNIT"));
					if (rs.getDate("ENTSM_SCALE_START_DATE") != null) {
						petMobileAppConfig.setEnthsmScaleStartDate(rs.getDate("ENTSM_SCALE_START_DATE").toLocalDate());
						petMobileAppConfig.setEnthsmScaleEndDate(rs.getDate("ENTSM_SCALE_END_DATE").toLocalDate());
					}
					petMobileAppConfigs.add(petMobileAppConfig);
				}
			}, StringUtils.join(petIds.getPetIds(), ","));

			return petMobileAppConfigs.stream().collect(Collectors.groupingBy((PetMobileAppConfig::getPetId)));

		} catch (Exception e) {
			LOGGER.error("error while fetching getMobileAppConfigs", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public PetFeedingEnthusiasmScale addPetFeedingTime(PetFeedingEnthusiasmScale petFeedingEnthusiasmScale)
			throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", petFeedingEnthusiasmScale.getPetId());
			inputParams.put("p_entsm_scale_id", petFeedingEnthusiasmScale.getEnthusiasmScaleId());
			inputParams.put("p_feeding_time_id", petFeedingEnthusiasmScale.getFeedingTimeId());
			inputParams.put("p_feeding_date", petFeedingEnthusiasmScale.getFeedingDate());
			inputParams.put("p_pet_parent_id", petFeedingEnthusiasmScale.getPetParentId());

			LOGGER.info("addPetFeedingTime inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_FEEDING_ENTHUSIASM_SCALE,
					inputParams);
			LOGGER.info("addPetFeedingTime outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");

			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				Integer feedingEnthusiasmScaleId = (int) outParams.get("last_insert_id");
				petFeedingEnthusiasmScale.setFeedingEnthusiasmScaleId(feedingEnthusiasmScaleId);
			} else {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing addPetFeedingTime ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petFeedingEnthusiasmScale;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PetObservation> getPetObservationsByPetId(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetObservationsByPetId called");
		List<PetObservation> petObservations = new ArrayList<>();
		Map<Integer, List<ObservationPhoto>> photoDetailsMap = new HashMap<>();
		Map<Integer, List<ObservationVideo>> videoDetailsMap = new HashMap<>();
		long startTime = System.currentTimeMillis();
		try {
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_id", petId);

			LOGGER.info("getPetObservationsByPetId inputParams are {}", inputParams);
			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(MOBILE_APP_GET_OBSERVATIONS_BY_PET_ID,
					inputParams);
			LOGGER.info("getPetObservationsByPetId simpleJdbcCallResult are {}", simpleJdbcCallResult);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(photoObservations -> {
						ObservationPhoto observationPhoto = new ObservationPhoto();
						observationPhoto.setObservationPhotoId((Integer) photoObservations.get("PHOTO_ID"));
						observationPhoto.setFileName((String) photoObservations.get("FILE_NAME"));
						// observationPhoto.setFilePath((String) photoObservations.get("IMAGE_PATH"));

						String imagePathValue = (String) photoObservations.get("IMAGE_PATH");

						if (!StringUtils.contains(imagePathValue, "firebasestorage")) {
							String imageSignedUrl = gcpClientUtil.getDownloaFiledUrl(
									imagePathValue.concat("/").concat(observationPhoto.getFileName()),
									Constants.GCP_OBSERVATION_PHOTO_PATH);
							observationPhoto.setFilePath(imageSignedUrl);
						} else {
							observationPhoto.setFilePath(imagePathValue);
						}

						Integer petObservationId = (Integer) photoObservations.get("PET_OBSERVATION_ID");

						photoDetailsMap.computeIfAbsent(petObservationId, k -> new ArrayList<ObservationPhoto>())
								.add(observationPhoto);

					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(videoObservations -> {
						ObservationVideo observationVideo = new ObservationVideo();
						observationVideo.setObservationVideoId((Integer) videoObservations.get("VIDEO_ID"));
						observationVideo.setVideoName((String) videoObservations.get("VIDEO_NAME"));

						String videoURL = (String) videoObservations.get("VIDEO_URL");
						if (StringUtils.isNotBlank(videoURL)) {
							if (!StringUtils.contains(videoURL, "firebasestorage")) {
								String mediaSignedUrl = gcpClientUtil.getDownloaFiledUrl(
										videoURL.replaceAll("https://storage.googleapis.com/wearables-portal-media/"
												+ environment + "/GCloud/WPortal/ObservationVideo/", ""),
										Constants.GCP_OBSERVATION_VIDEO_PATH);
								observationVideo.setVideoUrl(mediaSignedUrl);
							} else {
								observationVideo.setVideoUrl(videoURL);
							}
						}

						String videoThumbnailURL = (String) videoObservations.get("VIDEO_THUMBNAIL_URL");
						if (StringUtils.isNotBlank(videoThumbnailURL)) {
							if (!StringUtils.contains(videoThumbnailURL, "firebasestorage")) {
								String mediaSignedThumbUrl = gcpClientUtil.getDownloaFiledUrl(
										videoThumbnailURL.replaceAll(
												"https://storage.googleapis.com/wearables-portal-media/" + environment
														+ "/GCloud/WPortal/ObservationVideoThumbnail/",
												""),
										Constants.GCP_OBSERVATION_VIDEO_THUMBNAIL_PATH);
								observationVideo.setVideoThumbnailUrl(mediaSignedThumbUrl);
							} else {
								observationVideo.setVideoThumbnailUrl(videoThumbnailURL);
							}
						}

						Integer petObservationId = (Integer) videoObservations.get("PET_OBSERVATION_ID");

						videoDetailsMap.computeIfAbsent(petObservationId, k -> new ArrayList<ObservationVideo>())
								.add(observationVideo);

					});
				}

				if (key.equals(RESULT_SET_3)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(obsDetails -> {
						PetObservation petObservation = new PetObservation();
						petObservation.setPetId((Integer) obsDetails.get("PET_ID"));
						petObservation.setBehaviorTypeId((Integer) obsDetails.get("BEHAVIOR_TYPE_ID"));
						petObservation.setBehaviorId(Integer.parseInt((String) obsDetails.get("BEHAVIOR_ID")));
						petObservation.setBehaviorName((String) obsDetails.get("BEHAVIOR_NAME"));
						petObservation.setObsText((String) obsDetails.get("OBS_TEXT"));
						petObservation.setObservationDateTime((LocalDateTime) obsDetails.get("OBSERVATION_DATE_TIME"));
						petObservation.setTag((String) obsDetails.get("TAG"));
						petObservation.setEmotionIconsText((String) obsDetails.get("EMOTICONS_TEXT"));
						petObservation.setSeizuresDescription((String) obsDetails.get("SEIZURES_DESCRIPTION"));
						petObservation.setObservationId((Integer) obsDetails.get("PET_OBSERVATION_ID"));
						petObservation.setModifiedDate((LocalDateTime) obsDetails.get("MODIFIED_DATE"));

						petObservation.setPhotos(photoDetailsMap.get(petObservation.getObservationId()) != null
								? photoDetailsMap.get(petObservation.getObservationId())
								: new ArrayList<>());

						petObservation.setVideos(videoDetailsMap.get(petObservation.getObservationId()) != null
								? videoDetailsMap.get(petObservation.getObservationId())
								: new ArrayList<>());
						petObservations.add(petObservation);
					});
				}
			}
			long endTime = System.currentTimeMillis();
			LOGGER.info("Time taken to execute getPetObservationsByPetId service in millis is {}",
					(endTime - startTime));
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetObservationsByPetId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petObservations;
	}

	@Override
	public PetObservation savePetObservation(PetObservation addPetObservation) throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_observation_id", addPetObservation.getObservationId());
			inputParams.put("p_pet_id", addPetObservation.getPetId());
			inputParams.put("p_obs_text", addPetObservation.getObsText());
			inputParams.put("p_tag", addPetObservation.getTag());
			inputParams.put("p_behavior_id", addPetObservation.getBehaviorId());
			inputParams.put("p_observation_date_time", addPetObservation.getObservationDateTime());
			inputParams.put("p_emoticons_text", addPetObservation.getEmotionIconsText());
			inputParams.put("p_seizures_description", addPetObservation.getSeizuresDescription());
			inputParams.put("p_login_user_id", addPetObservation.getLoginUserId());
			inputParams.put("p_videos", new ObjectMapper().writeValueAsString(addPetObservation.getVideos()));
			inputParams.put("p_photos", new ObjectMapper().writeValueAsString(addPetObservation.getPhotos()));

			LOGGER.info("savePetObservation inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_SAVE_OBSERVATION_INFO, inputParams);
			LOGGER.info("savePetObservation outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");

			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				Integer observationId = (int) outParams.get("last_insert_id");
				addPetObservation.setObservationId(observationId);
			} else {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing savePetObservation ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return addPetObservation;
	}

	@Override
	public void deletePetObservation(int observationId, int petId, int petParentId) throws ServiceExecutionException {
		LOGGER.debug("deletePetObservation called");
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_observation_id", observationId);
		inputParams.put("p_pet_id", petId);
		inputParams.put("p_pet_parent_id", petParentId);
		try {
			LOGGER.info("deletePetObservation inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_DELETE_OBSERVATION_INFO, inputParams);
			LOGGER.info("deletePetObservation outParams are {}", outParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			if (StringUtils.isNotEmpty(errorMsg) || (int) outParams.get("out_flag") < NumberUtils.INTEGER_ONE) {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing deletePetObservation", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ImageScoringScale> getPetImageScoringScales(int petId) throws ServiceExecutionException {
		List<ImageScoringScale> imageScoringScales = new ArrayList<>();
		Map<Integer, List<ImageScoringScaleDetails>> scoreDetailsMap = new HashMap<>();

		LOGGER.debug("getPetImageScoringScales called");
		try {
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_id", petId);

			LOGGER.info("getPetImageScoringScales inputParams are {}", inputParams);
			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(MOBILE_APP_GET_PET_IMAGE_SCORINGS,
					inputParams);
			// LOGGER.info("getPetImageScoringScales outParams are {}",
			// simpleJdbcCallResult);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(scaleDetails -> {
						ImageScoringScaleDetails scoringScaleDetails = new ImageScoringScaleDetails();
						scoringScaleDetails
								.setImageScoringDetailsId((Integer) scaleDetails.get("IMAGE_SCORING_DTLS_ID"));
						scoringScaleDetails.setScore((Integer) scaleDetails.get("SCORE"));
						scoringScaleDetails.setUom((Integer) scaleDetails.get("UOM"));
						scoringScaleDetails.setUnitName((String) scaleDetails.get("UNIT_NAME"));
						scoringScaleDetails.setDescription((String) scaleDetails.get("DESCRIPTION"));
						scoringScaleDetails.setImageLabel((String) scaleDetails.get("IMAGE_LABEL"));
						String imgPath = (String) scaleDetails.get("IMAGE_PATH");

						if (StringUtils.isNotEmpty(imgPath)) {
							scoringScaleDetails.setImagePath(
									gcpClientUtil.getDownloaFiledUrl(imgPath, Constants.GCP_IMAGE_SCORING_PATH));
						}

						Integer imageScoringScaleId = (Integer) scaleDetails.get("IMAGE_SCORING_ID");

						scoreDetailsMap
								.computeIfAbsent(imageScoringScaleId, k -> new ArrayList<ImageScoringScaleDetails>())
								.add(scoringScaleDetails);
					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(imageScales -> {
						ImageScoringScale imageScoringScale = new ImageScoringScale();
						imageScoringScale.setImageScoringScaleId((Integer) imageScales.get("IMAGE_SCORING_ID"));
						imageScoringScale.setImageScaleName((String) imageScales.get("IMAGE_SCALE_NAME"));
						imageScoringScale.setClassificationId((Integer) imageScales.get("CLASSIFICATION_ID"));
						imageScoringScale.setClassification((String) imageScales.get("CLASSIFICATION"));
						imageScoringScale.setScoringTypeId((Integer) imageScales.get("SCORING_TYPE_ID"));
						imageScoringScale.setScoringType((String) imageScales.get("SCORING_TYPE"));
						imageScoringScale.setSpeciesId((Integer) imageScales.get("SPECIES_ID"));
						imageScoringScale.setSpeciesName((String) imageScales.get("SPECIES_NAME"));

						imageScoringScale.setScoringScaleDetails(
								scoreDetailsMap.get(imageScoringScale.getImageScoringScaleId()) != null
										? scoreDetailsMap.get(imageScoringScale.getImageScoringScaleId())
										: new ArrayList<>());
						imageScoringScales.add(imageScoringScale);
					});
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetImageScoringScales", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return imageScoringScales;
	}

	@Override
	public PetAddImageScoring addPetImageScoring(PetAddImageScoring addPetImageScoring)
			throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_image_score_type_id", addPetImageScoring.getImageScoreType());
			inputParams.put("p_image_score_id", addPetImageScoring.getImageScoringId());
			inputParams.put("p_pet_id", addPetImageScoring.getPetId());
			inputParams.put("p_image_score_dtls_json",
					new ObjectMapper().writeValueAsString(addPetImageScoring.getPetImgScoreDetails()));
			inputParams.put("p_pet_parent_id", addPetImageScoring.getPetParentId());

			LOGGER.info("addPetImageScoring inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_IMAGE_SCORINGS, inputParams);
			LOGGER.info("addPetImageScoring outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			//Added by deepak on 08-02-24
			LOGGER.info("addPetImageScoring outputParams are ibw_flag {}", outParams.get("ibw_flag"));
			int ibwFlag = outParams.get("ibw_flag")!=null ? (int)outParams.get("ibw_flag") :0;
			LOGGER.info("addPetImageScoring outputParams are ibwFlag {}", outParams.get("ibwFlag"));
			if(ibwFlag>0){
				// Added by deepak on 08-02-24 to Trigger AF engine on IBW Change.
				WearablesUtils.invokeAFEngine(addPetImageScoring.getPetId());
			}
			//End
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				Integer petImageScoringId = (int) outParams.get("last_insert_id");
				addPetImageScoring.setPetImageScoringId(petImageScoringId);
			} else {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing addPetImageScoring ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return addPetImageScoring;
	}

	@Override
	public List<PetFeedingPreference> getPetFeedingPreferences() throws ServiceExecutionException {
		List<PetFeedingPreference> petFeedingPreferences = new ArrayList<>();
		LOGGER.debug("getPetFeedingPreferences called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_FEEDING_PREFERENCES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetFeedingPreference petFeedingPreference = new PetFeedingPreference();
					petFeedingPreference.setFeedingPreferenceId(rs.getInt("FEEDING_PREFERENCE_ID"));
					petFeedingPreference.setFeedingPreference(rs.getString("PREFERENCE_NAME"));
					petFeedingPreferences.add(petFeedingPreference);
				}
			});
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetFeedingPreferences ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petFeedingPreferences;
	}

	@Override
	public void addPetFeedingPreferences(PetAddFeedingPreferences petAddFeedingPreferences)
			throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", petAddFeedingPreferences.getPetId());
			inputParams.put("p_pet_feeding_preferences_json",
					new ObjectMapper().writeValueAsString(petAddFeedingPreferences.getPetFeedingPreferences()));
			inputParams.put("p_user_id", petAddFeedingPreferences.getUserId());

			LOGGER.info("addPetFeedingPreferences inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_FEEDING_PREFERENCES, inputParams);
			LOGGER.info("addPetFeedingPreferences outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");

			if (!StringUtils.isEmpty(errorMsg) && !(statusFlag > NumberUtils.INTEGER_ZERO)) {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing addPetFeedingPreferences ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void updatePet(UpdatePet updatePet) throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", updatePet.getPetId());
			inputParams.put("p_pet_name", updatePet.getPetName());
			inputParams.put("p_gender", updatePet.getGender());
			inputParams.put("p_is_neutered", updatePet.getIsNeutered());
			inputParams.put("p_breed_id", updatePet.getBreedId());
			inputParams.put("p_dob", updatePet.getBirthDay());
			inputParams.put("p_dob_unknown", updatePet.getIsUnknown());
			inputParams.put("p_weight", updatePet.getWeight());
			inputParams.put("p_weight_unit", updatePet.getWeightUnit());
			inputParams.put("p_user_id", updatePet.getUserId());

			LOGGER.info("updatePet inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_UPDATE_PET_PROFILE, inputParams);
			LOGGER.info("updatePet outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");

			if (!StringUtils.isEmpty(errorMsg) && !(statusFlag > NumberUtils.INTEGER_ZERO)) {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing updatePet", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public PetAddressResponse getPetAddressHistoryById(int petId) throws ServiceExecutionException {
		PetAddressResponse response = new PetAddressResponse();
		List<Address> petAddressList = new ArrayList<Address>();
		LOGGER.debug("getPetParentAddressHistoryById called");
		try {
			jdbcTemplate.query(MOBILE_APP_PET_GET_ADDRESSES_BY_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Address petAddress = new Address();
					petAddress.setAddressId(rs.getInt("PET_ADDRESS_ID"));
					petAddress.setAddress1(rs.getString("ADDRESS_1"));
					petAddress.setAddress2(rs.getString("ADDRESS_2"));
					petAddress.setCity(rs.getString("CITY"));
					petAddress.setState(rs.getString("STATE"));
					petAddress.setCountry(rs.getString("COUNTRY"));
					petAddress.setZipCode(rs.getString("ZIP_CODE"));
					petAddress.setTimeZone(rs.getString("TIME_ZONE"));
					petAddress.setTimeZoneId(rs.getInt("TIMEZONE_ID"));

					petAddress.setDateFrom(
							rs.getDate("FROM_DATE") != null ? rs.getDate("FROM_DATE").toLocalDate() : null);
					petAddress.setDateTo(rs.getDate("TO_DATE") != null ? rs.getDate("TO_DATE").toLocalDate() : null);

					petAddressList.add(petAddress);
				}
			}, petId);

			response.setPetAddressList(petAddressList);
		} catch (Exception e) {
			LOGGER.error("error while executing addPetFeedingPreferences ", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		return response;
	}

	@Override
	public List<PetDTO> getPetsByPetParentIdAndMobileAppConfigId(int petParentId, int mobileAppConfigId)
			throws ServiceExecutionException {
		List<PetDTO> pets = new ArrayList<>();
		LOGGER.debug("getPetsByPetParentIdAndMobileAppConfigId called");
		Map<Integer, List<DeviceDTO>> devicesMap = new HashMap<>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JavaTimeModule module = new JavaTimeModule();
			module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
			objectMapper.registerModule(module);

			jdbcTemplate.query(MOBILE_APP_GET_PETS_BY_PET_PARENT_AND_APP_CONFIG, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetDTO petDTO = new PetDTO();
					petDTO.setPetID(rs.getInt("PET_ID"));
					petDTO.setPetName(rs.getString("PET_NAME"));
					String imageName = rs.getString("PHOTO_NAME");
					petDTO.setStudyName(rs.getString("STUDY_NAME"));

					petDTO.setIsPetWithPetParent(rs.getBoolean("IS_PET_WITH_PET_PARENT"));
					// Address petAddress = new Gson().fromJson(rs.getString("PET_ADDRESS_JSON"),
					// Address.class);
					Address petAddress = null;
					try {
						if (StringUtils.isNotEmpty(rs.getString("PET_ADDRESS_JSON"))) {
							petAddress = new ObjectMapper().readValue(rs.getString("PET_ADDRESS_JSON"), Address.class);
						}
					} catch (JsonProcessingException | SQLException e) {
						e.printStackTrace();
					}
					petDTO.setPetAddress(petAddress == null ? new Address() : petAddress);

					if (StringUtils.isNotEmpty(imageName)) {
						// String photoUrl = photoPath + imageName;
						// petDeviceDTO.setPhotoUrl(photoUrl);
						petDTO.setPhotoUrl(gcpClientUtil.getDownloaFiledUrl(imageName, Constants.GCP_PET_PHOTO_PATH));
					}
					/*if (StringUtils.isNotEmpty(rs.getString("weightData"))) {
						String[] weightData = rs.getString("weightData").split("###");
						if (("lbs").equals(weightData[Constants.NUMBER_TWO])) {
							petDTO.setWeight(weightData[Constants.NUMBER_ZERO]);
						} else {
							petDTO.setWeight(weightData[Constants.NUMBER_ONE]);
						}
						petDTO.setWeightUnit(weightData[Constants.NUMBER_TWO]);
					}*/
					petDTO.setPetAge(rs.getString("PET_AGE"));
					petDTO.setPetBreed(rs.getString("BREED_NAME"));
					petDTO.setSpeciesId(rs.getString("SPECIES_ID"));
					petDTO.setBirthday(rs.getString("BIRTHDAY"));
					petDTO.setGender(rs.getString("GENDER"));
					petDTO.setIsNeutered(rs.getBoolean("IS_NEUTERED"));
					/*petDTO.setIsPetEditable(rs.getInt("IS_PET_EDITABLE"));
					petDTO.setPetStatus(rs.getInt("PET_STATUS_ID"));*/

					DeviceDTO deviceDto = new DeviceDTO();

					deviceDto.setDeviceId(rs.getInt("DEVICE_ID"));
					deviceDto.setDeviceNumber(rs.getString("DEVICE_NUMBER"));
					deviceDto.setDeviceType(rs.getString("DEVICE_TYPE"));
					deviceDto.setDeviceModel(rs.getString("DEVICE_MODEL"));
					// deviceDto.setBattery(rs.getString("BATTERY"));
					deviceDto.setFirmware(rs.getString("FIRMWARE"));
					deviceDto.setFirmwareNew(rs.getString("FIRMWARE_NEW"));
					deviceDto.setIsDeviceSetupDone(rs.getBoolean("IS_DEVICE_SETUP_DONE"));

					if (devicesMap.containsKey(petDTO.getPetID())) {
						if (deviceDto.getDeviceId() != 0) {
							devicesMap.get(petDTO.getPetID()).add(deviceDto);
						}
					} else {
						List<DeviceDTO> devices = new ArrayList<>();
						if (deviceDto.getDeviceId() != 0) {
							devices.add(deviceDto);
						}
						devicesMap.put(petDTO.getPetID(), devices);
						petDTO.setDevices(devicesMap.get(petDTO.getPetID()));
						pets.add(petDTO);
					}
				}
			}, petParentId, mobileAppConfigId);
			LOGGER.debug("getPetsByPetParentIdAndMobileAppConfigId successfully completed");
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetsByPetParentIdAndMobileAppConfigId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return pets;
	}

	@Override
	public BehaviorVisualizationResponse getBehaviorVisualization(int petId) throws ServiceExecutionException {
		String query = MessageFormat.format(BIG_QEURY_GET_BEHAVIOR_VISULAZATION, Integer.toString(petId));
		LOGGER.info("===============getBehaviorVisualization================ " + query);
		TableResult tableResult = BigQueryServiceUtil.queryBigQueryTable(query);
		LOGGER.info("Total Rows == getBehaviorVisualization " + tableResult.getTotalRows());
		BehaviorVisualizationResponse response = new BehaviorVisualizationResponse();
		PetForwardMotionInfo forwardMotionInfo = new PetForwardMotionInfo();
		PetSleepInfo sleepInfo = new PetSleepInfo();
		PetForwardMotionGoalSetting fmGoalSetInfo = new PetForwardMotionGoalSetting();
		for (FieldValueList row : tableResult.iterateAll()) {
			response.setPetId(petId);
			int todayWalking = Integer.parseInt((String) row.get("TODAY_WALKING").getValue());
			int todayRunning = Integer.parseInt((String) row.get("TODAY_RUNNING").getValue());
			int todayForwardMotion = todayWalking + todayRunning;

			forwardMotionInfo.setRunning(todayRunning);
			forwardMotionInfo.setRunningText(WearablesUtils.covertToReadableTime(todayRunning));

			forwardMotionInfo.setWalking(todayWalking);
			forwardMotionInfo.setWalkingText(WearablesUtils.covertToReadableTime(todayWalking));

			forwardMotionInfo.setTodayForwardMotion(todayForwardMotion);
			forwardMotionInfo.setTodayForwardMotionText(WearablesUtils.covertToReadableTime(todayForwardMotion));

			forwardMotionInfo.setLastWeekForwardMotionAverage(
					(int) (Double.parseDouble((String) row.get("LAST_WEEK_FORWARD_MOTION_AVG").getValue())));
			forwardMotionInfo.setLastWeekFMAvgText(
					WearablesUtils.covertToReadableTime(forwardMotionInfo.getLastWeekForwardMotionAverage()));

			int todayVsLastWeekFMAvgPercentage = 0;
			if (forwardMotionInfo.getLastWeekForwardMotionAverage() > 0) {
				todayVsLastWeekFMAvgPercentage = ((todayForwardMotion * 100)
						/ forwardMotionInfo.getLastWeekForwardMotionAverage());
			} else {
				todayVsLastWeekFMAvgPercentage = todayForwardMotion > 0 ? 100 : 0;
			}

			forwardMotionInfo.setPreviousDayForwardMotion(
					Integer.parseInt((String) row.get("PREV_DAY_FORWARD_MOTION").getValue()));
			forwardMotionInfo.setPreviousDayFMText(
					WearablesUtils.covertToReadableTime(forwardMotionInfo.getPreviousDayForwardMotion()));

			int prevDayFMVsLastWeekFMAvgPercent = 0;
			if (forwardMotionInfo.getLastWeekForwardMotionAverage() > 0) {
				prevDayFMVsLastWeekFMAvgPercent = ((forwardMotionInfo.getPreviousDayForwardMotion() * 100)
						/ forwardMotionInfo.getLastWeekForwardMotionAverage());
			} else {
				prevDayFMVsLastWeekFMAvgPercent = forwardMotionInfo.getPreviousDayForwardMotion() > 0 ? 100 : 0;
			}
			forwardMotionInfo.setPrevDayFMVsLastWeekFMAvgPercentage(prevDayFMVsLastWeekFMAvgPercent);

			forwardMotionInfo.setTodayForwardMotionSofar(todayForwardMotion);
			forwardMotionInfo.setTodayForwardMotionSofarText(
					WearablesUtils.covertToReadableTime(forwardMotionInfo.getTodayForwardMotionSofar()));

			forwardMotionInfo.setTodayFMSofarVsLastWeekFMAvgPercentage(todayVsLastWeekFMAvgPercentage);
			forwardMotionInfo.setPrevDayForwardMotionAtThisTime(
					Integer.parseInt((String) row.get("PREV_DAY_TILL_NOW_FM").getValue()));
			forwardMotionInfo.setTodayFMSofarVsPrevDayFMAtThisTime(
					todayForwardMotion - forwardMotionInfo.getPrevDayForwardMotionAtThisTime());
			response.setForwardMotionInfo(forwardMotionInfo);

			// Sleep
			int todayNightSleep = Integer.parseInt((String) row.get("TODAY_NIGHT_SLEEP").getValue());
			int todayDaySleep = Integer.parseInt((String) row.get("TODAY_DAY_SLEEP").getValue());
			int todayTotalSleep = todayNightSleep + todayDaySleep;

			sleepInfo.setNightSleep(todayNightSleep);
			sleepInfo.setNightSleepText(WearablesUtils.covertToReadableTime(todayNightSleep));

			sleepInfo.setDaySleep(todayDaySleep);
			sleepInfo.setDaySleepText(WearablesUtils.covertToReadableTime(todayDaySleep));

			sleepInfo.setTodaySleep(todayTotalSleep);
			sleepInfo.setTodaySleepText(WearablesUtils.covertToReadableTime(todayTotalSleep));

			sleepInfo.setLastWeekTotalSleepAverage(
					(int) (Double.parseDouble((String) row.get("LAST_WEEK_SLEEP_AVG").getValue())));
			sleepInfo.setLastWeekTotalSleepAverageText(
					WearablesUtils.covertToReadableTime(sleepInfo.getLastWeekTotalSleepAverage()));

			int todayVsLastWeekSleepAvgPercenrage = 0;
			if (forwardMotionInfo.getLastWeekForwardMotionAverage() > 0) {
				todayVsLastWeekSleepAvgPercenrage = ((todayTotalSleep * 100)
						/ sleepInfo.getLastWeekTotalSleepAverage());
			} else {
				todayVsLastWeekSleepAvgPercenrage = todayTotalSleep > 0 ? 100 : 0;
			}
			sleepInfo.setTodayVsLastWeekSleepAvgPercentage(todayVsLastWeekSleepAvgPercenrage);

			sleepInfo.setPreviousDayTotalSleep(Integer.parseInt((String) row.get("PREV_DAY_SLEEP").getValue()));
			sleepInfo.setPreviousDayTotalSleepText(
					WearablesUtils.covertToReadableTime(sleepInfo.getPreviousDayTotalSleep()));

			int prevDayTSVsLastWeekTSAvgPercent = 0;
			if (forwardMotionInfo.getLastWeekForwardMotionAverage() > 0) {
				prevDayTSVsLastWeekTSAvgPercent = ((sleepInfo.getPreviousDayTotalSleep() * 100)
						/ sleepInfo.getLastWeekTotalSleepAverage());
			} else {
				prevDayTSVsLastWeekTSAvgPercent = sleepInfo.getPreviousDayTotalSleep() > 0 ? 100 : 0;
			}
			sleepInfo.setPrevDayTSVsLastWeekTSAvgPercentage(prevDayTSVsLastWeekTSAvgPercent);

			sleepInfo.setTodayTotalSleepSofar(todayTotalSleep);
			sleepInfo.setTodayTotalSleepSofarText(
					WearablesUtils.covertToReadableTime(sleepInfo.getTodayTotalSleepSofar()));

			sleepInfo.setTodayTSSofarVsLastWeekTSAvgPercentage(todayVsLastWeekSleepAvgPercenrage);
			sleepInfo.setPrevDayTotalSleepAtThisTime(
					Integer.parseInt((String) row.get("PREV_DAY_TILL_NOW_SLEEP").getValue()));
			sleepInfo
					.setTodayTSSofarVsPrevDayTSAtThisTime(todayTotalSleep - sleepInfo.getPrevDayTotalSleepAtThisTime());
			response.setSleepInfo(sleepInfo);
		}

		int forwardMotionGoalSettingValue = selectForObject(MOBILE_APP_GET_PET_FORWARD_MOTION_GOAL_SETTING,
				Integer.class, petId);
		int todayForwardMotion = forwardMotionInfo.getTodayForwardMotionSofar();

		fmGoalSetInfo.setForwardMotionGoalSetting(forwardMotionGoalSettingValue);
		fmGoalSetInfo.setForwardMotionGoalSetText(WearablesUtils.covertToReadableTime(forwardMotionGoalSettingValue));

		fmGoalSetInfo.setTodayForwardMotionSofar(todayForwardMotion);
		fmGoalSetInfo.setTodayFMSofarText(WearablesUtils.covertToReadableTime(todayForwardMotion));

		fmGoalSetInfo.setTodayForwardMotionVsGoalSettingPercentage(
				(todayForwardMotion * 100) / forwardMotionGoalSettingValue);

		int goalToAchieve = forwardMotionGoalSettingValue - todayForwardMotion;
		int tobeAchieved = 0, overAchieved = 0;

		if (goalToAchieve > 0) {
			tobeAchieved = goalToAchieve;
		} else if (goalToAchieve < 0) {
			overAchieved = todayForwardMotion - forwardMotionGoalSettingValue;
		}

		fmGoalSetInfo.setTobeAchieved(tobeAchieved);
		fmGoalSetInfo.setTobeAchievedText(WearablesUtils.covertToReadableTime(tobeAchieved));

		fmGoalSetInfo.setOverAchieved(overAchieved);
		fmGoalSetInfo.setOverAchievedText(WearablesUtils.covertToReadableTime(overAchieved));

		response.setFmGoalSetting(fmGoalSetInfo);

		return response;
	}

	@Override
	public List<BehaviorHistory> getBehaviorHistoryByType(int petId, String behaviorType,
			BehaviorHistoryRequest behaviorHistoryRequest) throws ServiceExecutionException {
		String query = MessageFormat.format(BIG_QEURY_GET_BEHAVIOR_HISTORY, Integer.toString(petId),
				behaviorHistoryRequest.getFromDate(), behaviorHistoryRequest.getToDate());
		LOGGER.info("===============getBehaviorHistoryByType================ " + query);
		TableResult tableResult = BigQueryServiceUtil.queryBigQueryTable(query);
		LOGGER.info("Total Rows == getBehaviorHistoryByType " + tableResult.getTotalRows());
		List<BehaviorHistory> behaviorHistory = new ArrayList<BehaviorHistory>();
		for (FieldValueList row : tableResult.iterateAll()) {
			BehaviorHistory history = new BehaviorHistory();
			history.setPetId(Integer.parseInt((String) row.get("PET_ID").getValue()));
			history.setActivityDay((String) row.get("ACTIVITY_DAY").getValue());
			if ("1".equalsIgnoreCase(behaviorType)) {
				history.setWalking(Integer.parseInt((String) row.get("WALKING").getValue()));
				history.setWalkingText(WearablesUtils.covertToReadableTime(history.getWalking()));
				history.setRunning(Integer.parseInt((String) row.get("RUNNING").getValue()));
				history.setRunningText(WearablesUtils.covertToReadableTime(history.getRunning()));
				history.setForwardMotion(Integer.parseInt((String) row.get("FORWARD_MOTION").getValue()));
				history.setForwardMotionText(WearablesUtils.covertToReadableTime(history.getForwardMotion()));
			}
			if ("2".equalsIgnoreCase(behaviorType)) {
				history.setNightSleep(Integer.parseInt((String) row.get("NIGHT_SLEEP").getValue()));
				history.setNightSleepText(WearablesUtils.covertToReadableTime(history.getNightSleep()));
				history.setDaySleep(Integer.parseInt((String) row.get("DAY_SLEEP").getValue()));
				history.setDaySleepText(WearablesUtils.covertToReadableTime(history.getDaySleep()));
				history.setTotalSleep(Integer.parseInt((String) row.get("TOTAL_SLEEP").getValue()));
				history.setTotalSleepText(WearablesUtils.covertToReadableTime(history.getTotalSleep()));
			}
			behaviorHistory.add(history);
		}
		return behaviorHistory;
	}

	@Override
	public void replaceSensorToPet(AssignSensorRequest assignSensorRequest) throws ServiceExecutionException {
		LOGGER.debug("replaceSensorToPet called");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", assignSensorRequest.getPetId());
			inputParams.put("p_pet_parent_id", assignSensorRequest.getPetParentId());
			inputParams.put("p_device_number", assignSensorRequest.getDeviceNumber());
			inputParams.put("p_device_type", assignSensorRequest.getDeviceType());
			inputParams.put("p_assigned_date", assignSensorRequest.getAssignedDate());
			inputParams.put("p_old_device_number", assignSensorRequest.getOldDeviceNumber());
			inputParams.put("p_setup_status", assignSensorRequest.getSetupStatus());
			inputParams.put("p_ssid_list", assignSensorRequest.getSsidList());
			inputParams.put("p_login_user_id", assignSensorRequest.getCreatedBy());

			LOGGER.info("replaceSensorToPet inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_REPLACE_PET_SENSOR, inputParams);
			LOGGER.info("replaceSensorToPet outParams are {}", outParams);

			// System.out.println(outParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				// getting the inserted flag value
				Integer outFlag = (int) outParams.get("out_flag");
				LOGGER.info("Sensor has been assigned successfully ", outFlag);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"replaceSensorToPet service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.INVALID_DEVICE_NUMBER,
									assignSensorRequest.getDeviceNumber())));

				} else if (statusFlag == -3) {
					throw new ServiceExecutionException(
							"replaceSensorToPet service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList(
									new WearablesError(WearablesErrorCode.INVALID_STUDY_VIRTUAL_STUDY_ALREADY_MAPPED)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing replaceSensorToPet ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void saveForwardMotionGoal(FMGoalSettingRequest fmGoalSettingRequest) {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", fmGoalSettingRequest.getPetId());
			inputParams.put("p_pet_parent_id", fmGoalSettingRequest.getPetParentId());
			inputParams.put("p_goal_duration_id", fmGoalSettingRequest.getGoalDurationInMins());
			inputParams.put("p_login_user_id", fmGoalSettingRequest.getUserId());

			LOGGER.info("saveForwardMotionGoal inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_SAVE_FM_GOAL_SETTING, inputParams);
			LOGGER.info("saveForwardMotionGoal outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");

			if (StringUtils.isEmpty(errorMsg) && statusFlag < NumberUtils.INTEGER_ZERO) {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing saveForwardMotionGoal ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

}