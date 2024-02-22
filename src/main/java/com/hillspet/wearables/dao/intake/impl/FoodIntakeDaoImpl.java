package com.hillspet.wearables.dao.intake.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dao.BaseDaoImpl;
import com.hillspet.wearables.dao.intake.FoodIntakeDao;
import com.hillspet.wearables.dto.DietFeedbackDTO;
import com.hillspet.wearables.dto.FoodIntakeDTO;
import com.hillspet.wearables.dto.FoodIntakeHistoryDTO;
import com.hillspet.wearables.dto.FoodIntakeHistoryGraphDTO;
import com.hillspet.wearables.dto.PetRecommondedDietsDTO;
import com.hillspet.wearables.response.DietFeedback;
import com.hillspet.wearables.response.FoodIntakeHistoryGraphResponse;
import com.hillspet.wearables.response.FoodIntakeLookUpResponse;
import com.hillspet.wearables.response.FoodIntakeResponse;
import com.hillspet.wearables.response.MeasurementUnit;
import com.hillspet.wearables.response.OtherFoods;

/**
 * 
 * @author akumarkhaspa
 * 
 */
@Repository
public class FoodIntakeDaoImpl extends BaseDaoImpl implements FoodIntakeDao {

	private static final Logger LOGGER = LogManager.getLogger(FoodIntakeDaoImpl.class);

	public static final String RESULT_SET_1 = "#result-set-1";
	public static final String RESULT_SET_2 = "#result-set-2";
	public static final String RESULT_SET_3 = "#result-set-3";
	public static final String RESULT_SET_4 = "#result-set-4";

	private static String MOBILE_APP_GET_INTAKE_LOOKUP_DATA = "MOBILE_APP_GET_INTAKE_LOOKUP_DATA";
	private static String MOBILE_APP_GET_INTAKE_PET_RECOMMENDED_DIETS = "CALL MOBILE_APP_GET_INTAKE_PET_RECOMMENDED_DIETS(?,?,?)";
	private static String MOBILE_APP_GET_INTAKE_HISTORY_LIST = "MOBILE_APP_GET_INTAKE_HISTORY_LIST";
	private static String MOBILE_APP_GET_INTAKE_DATA_BY_ID = "MOBILE_APP_GET_INTAKE_DATA_BY_ID";
	private static String MOBILE_APP_SAVE_PET_INTAKE = "MOBILE_APP_SAVE_PET_INTAKE";
	private static String MOBILE_APP_GET_INTAKE_HISTORY_CHART_DATA = "MOBILE_APP_GET_INTAKE_HISTORY_CHART_DATA";

	/**
	 * @author akumarkhaspa
	 * @param petId
	 * @param petParentId
	 * @param intakeDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public FoodIntakeLookUpResponse getPetFoodIntakeConfigData(int petId, int petParentId, String intakeDate)
			throws ServiceExecutionException {
		LOGGER.debug("getFoodIntakeLookUp called");
		FoodIntakeLookUpResponse response = new FoodIntakeLookUpResponse();
		List<MeasurementUnit> measurementUnitsList = new ArrayList<>();
		List<OtherFoods> otherFoodsList = new ArrayList<>();
		List<DietFeedback> dietFeedbackList = new ArrayList<>();
		List<PetRecommondedDietsDTO> recommondedDietList = new ArrayList<PetRecommondedDietsDTO>();

		long startTime = System.currentTimeMillis();
		try {

			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(MOBILE_APP_GET_INTAKE_LOOKUP_DATA);
			LOGGER.info("getFoodIntakeLookUp simpleJdbcCallResult are {}", simpleJdbcCallResult);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(units -> {
						MeasurementUnit unit = new MeasurementUnit();
						unit.setUnitId((Integer) units.get("UNIT_ID"));
						unit.setUnitCategory((String) units.get("UNIT_CATEGORY"));
						unit.setUnit((String) units.get("UNIT"));
						unit.setUnitAbbrevation((String) units.get("UNIT_ABBREVATION"));
						measurementUnitsList.add(unit);
					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(otherFoods -> {
						OtherFoods otherFood = new OtherFoods();
						otherFood.setOtherFoodId((Integer) otherFoods.get("OTHER_FOOD_ID"));
						otherFood.setOtherFoodType((String) otherFoods.get("OTHER_FOOD_TYPE"));
						otherFood.setDescription((String) otherFoods.get("DESCRIPTION"));
						otherFoodsList.add(otherFood);
					});
				}

				if (key.equals(RESULT_SET_3)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(feedback -> {
						DietFeedback dietFeedback = new DietFeedback();
						dietFeedback.setFeedbackId((Integer) feedback.get("FEEDBACK_ID"));
						dietFeedback.setFeedbackCategory((String) feedback.get("FEEDBACK_CATEGORY"));
						dietFeedback.setDescription((String) feedback.get("DESCRIPTION"));
						dietFeedbackList.add(dietFeedback);
					});
				}
			}

			response.setMeasurementUnits(measurementUnitsList);
			response.setOtherFoods(otherFoodsList);
			response.setDietFeedback(dietFeedbackList);

		} catch (Exception e) {
			LOGGER.error("error while fetching getFoodIntakeLookUp", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		try {
			jdbcTemplate.query(MOBILE_APP_GET_INTAKE_PET_RECOMMENDED_DIETS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetRecommondedDietsDTO recommondedDiet = new PetRecommondedDietsDTO();
					recommondedDiet.setFeedingScheduledId(rs.getInt("FEEDING_SCHEDULE_ID"));
					recommondedDiet.setRecommendedAmountGrams(rs.getDouble("RECOMMENDATION_GRAMS"));
					recommondedDiet.setRecommendedAmountCups(rs.getDouble("RECOMMENDATION_CUPS"));
					recommondedDiet.setRecommendedRoundedGrams(rs.getString("FINAL_ROUNDED_REC_AMT_GRAMS"));
					recommondedDiet.setRecommendedRoundedCups(rs.getString("FINAL_ROUNDED_REC_AMT_CUPS"));
					recommondedDiet.setDietId(rs.getInt("DIET_ID"));
					recommondedDiet.setDietName(rs.getString("DIET_NAME"));
					recommondedDiet.setUnitId(rs.getInt("UNIT_ID"));
					recommondedDiet.setUnit(rs.getString("UNIT"));

					recommondedDietList.add(recommondedDiet);
				}
			}, petId, petParentId, intakeDate);

			response.setRecommondedDiet(recommondedDietList);
		} catch (Exception e) {
			LOGGER.error("error while executing getFoodIntakeLookUp ", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		long endTime = System.currentTimeMillis();
		LOGGER.info("Time taken to execute getFoodIntakeLookUp service in millis is {}", (endTime - startTime));

		return response;
	}

	/**
	 * @author akumarkhaspa
	 * @param petId
	 * @param petParentId
	 * @param intakeDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public FoodIntakeResponse getPetIntakeList(int petId, int petParentId, String intakeDate)
			throws ServiceExecutionException {

		LOGGER.debug("getPetIntakeList called");
		FoodIntakeResponse response = new FoodIntakeResponse();
		List<FoodIntakeDTO> foodIntakeList = new ArrayList<>();
		Map<Integer, List<FoodIntakeHistoryDTO>> foodIntakeHistoryMap = new HashMap<>();

		try {
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_id", petId);
			inputParams.put("p_pet_parent_id", petParentId);
			inputParams.put("p_intake_date", intakeDate);

			LOGGER.info("getPetIntakeList inputParams are {}", inputParams);
			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(MOBILE_APP_GET_INTAKE_HISTORY_LIST, inputParams);
			// LOGGER.info("getPetIntakeList outParams are {}", simpleJdbcCallResult);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(foodIntakeHistory -> {
						FoodIntakeHistoryDTO historyDto = new FoodIntakeHistoryDTO();
						historyDto.setIntakeId((Integer) foodIntakeHistory.get("FOOD_INTAKE_ID"));
						historyDto.setDietId((Integer) foodIntakeHistory.get("DIET_ID"));
						BigDecimal quantityRecommended = (BigDecimal) foodIntakeHistory.get("RECOMMENDED_FOOD_AMOUNT");
						historyDto.setQuantityRecommended((quantityRecommended != null ? quantityRecommended.doubleValue() : 0));
						if (foodIntakeHistory.get("QUANTITY_CONSUMED") != null) {
							historyDto.setQuantityConsumed(((BigDecimal) foodIntakeHistory.get("QUANTITY_CONSUMED")).doubleValue());
						}
						historyDto.setQuantityRecommendedRounded((String) foodIntakeHistory.get("FINAL_ROUNDED_REC_AMT"));
						// historyDto.setQuantityUnitId((Integer) foodIntakeHistory.get("QUANTITY_UNIT_ID"));
						Long unitId = (Long)foodIntakeHistory.get("QUANTITY_UNIT_ID");
						historyDto.setQuantityUnitId(unitId != null ? unitId.intValue() : 0);
						
						historyDto.setQuantityUnitName((String) foodIntakeHistory.get("UNIT"));
						if (foodIntakeHistory.get("PERCENT_CONSUMED") != null) {
							historyDto.setPercentConsumed(((BigDecimal) foodIntakeHistory.get("PERCENT_CONSUMED")).doubleValue());
						}
						historyDto.setDietName((String) foodIntakeHistory.get("DIET_NAME"));

						Integer foodIntakeId = (Integer) foodIntakeHistory.get("FOOD_INTAKE_ID");

						/*
						 * foodIntakeHistoryMap .computeIfAbsent(foodIntakeId, k -> new
						 * ArrayList<FoodIntakeHistoryDTO>()) .add(historyDto);
						 */

						ArrayList<FoodIntakeHistoryDTO> listDto = new ArrayList<FoodIntakeHistoryDTO>();
						if (foodIntakeHistoryMap != null && foodIntakeHistoryMap.containsKey(foodIntakeId)) {
							listDto = (ArrayList<FoodIntakeHistoryDTO>) foodIntakeHistoryMap.get(foodIntakeId);
							listDto.add(historyDto);
						} else {
							listDto.add(historyDto);
						}
						foodIntakeHistoryMap.put(foodIntakeId, listDto);

					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(foodIntake -> {
						//System.out.println("foodIntake ::  " + foodIntake.toString());
						FoodIntakeDTO foodIntakeDto = new FoodIntakeDTO();
						foodIntakeDto.setIntakeId((Integer) foodIntake.get("FOOD_INTAKE_ID"));
						foodIntakeDto.setPetId((Integer) foodIntake.get("PET_ID"));
						foodIntakeDto.setPetParentId((Integer) foodIntake.get("PET_PARENT_ID"));
						foodIntakeDto.setIntakeDate((LocalDateTime) foodIntake.get("INTAKE_DATE"));

						foodIntakeDto.setFoodIntakeHistory(foodIntakeHistoryMap.get(foodIntakeDto.getIntakeId()) != null
								? foodIntakeHistoryMap.get(foodIntakeDto.getIntakeId())
								: new ArrayList<>());
						foodIntakeList.add(foodIntakeDto);
					});
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetIntakeList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		response.setIntakes(foodIntakeList);
		return response;

	}

	/**
	 * @author akumarkhaspa
	 * @param intakeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public FoodIntakeDTO getPetIntakeById(int intakeId) throws ServiceExecutionException {
		FoodIntakeDTO foodIntakeDto = new FoodIntakeDTO();
		
		Map<Integer, List<FoodIntakeHistoryDTO>> foodIntakeHistoryMap = new HashMap<>();
		//List<FoodIntakeHistoryDTO> foodIntakeHistoryList = new ArrayList<>();
		List<DietFeedbackDTO> dietFeedbackList = new ArrayList<>();

		LOGGER.debug("getPetIntakeById called");
		Map<String, Object> inputParams = new HashMap<String, Object>();
		try {
			inputParams.put("p_intake_id", intakeId);

			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(MOBILE_APP_GET_INTAKE_DATA_BY_ID, inputParams);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(foodIntake -> {
						foodIntakeDto.setIntakeId((Integer) foodIntake.get("FOOD_INTAKE_ID"));
						foodIntakeDto.setPetId((Integer) foodIntake.get("PET_ID"));
						foodIntakeDto.setPetParentId((Integer) foodIntake.get("PET_PARENT_ID"));
						foodIntakeDto.setIntakeDate((LocalDateTime) foodIntake.get("INTAKE_DATE"));
					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(foodIntakeHistory -> {
						FoodIntakeHistoryDTO historyDto = new FoodIntakeHistoryDTO();
						historyDto.setIntakeId((Integer) foodIntakeHistory.get("INTAKE_ID"));
						historyDto.setFoodHistoryId((Integer) foodIntakeHistory.get("FOOD_HISTORY_ID"));
						historyDto.setFeedingScheduledId((Integer) foodIntakeHistory.get("FEEDING_SCHEDULE_ID"));
						historyDto.setDietId((Integer) foodIntakeHistory.get("DIET_ID"));
						historyDto.setDietName((String) foodIntakeHistory.get("DIET_NAME"));

						historyDto.setOtherFoodtypeId((Integer) foodIntakeHistory.get("OTHER_FOOD_ID"));
						historyDto.setOtherFoodTypeName((String) foodIntakeHistory.get("OTHER_FOOD_TYPE"));
						historyDto.setFoodName((String) foodIntakeHistory.get("FOOD_NAME"));

						historyDto.setQuantityUnitId((Integer) foodIntakeHistory.get("QUANTITY_UNIT_ID"));
						historyDto.setQuantityUnitName((String) foodIntakeHistory.get("UNIT"));

						BigDecimal quantityRecommended = (BigDecimal) foodIntakeHistory.get("RECOMMENDED_FOOD_AMOUNT");
						historyDto.setQuantityRecommended((quantityRecommended != null ? quantityRecommended.doubleValue() : 0));
						historyDto.setQuantityRecommendedRounded((String) foodIntakeHistory.get("FINAL_ROUNDED_REC_AMT"));

						BigDecimal quantityOffered = (BigDecimal) foodIntakeHistory.get("QUANTITY_OFFERED");
						historyDto.setQuantityOffered(quantityOffered != null ? quantityOffered.doubleValue() : 0);

						BigDecimal quantityConsumed = (BigDecimal) foodIntakeHistory.get("QUANTITY_CONSUMED");
						historyDto.setQuantityConsumed(quantityConsumed != null ? quantityConsumed.doubleValue() : 0);

						BigDecimal percentConsumed = (BigDecimal) foodIntakeHistory.get("PERCENT_CONSUMED");
						historyDto.setPercentConsumed(percentConsumed != null ? percentConsumed.doubleValue() : 0);

						historyDto.setCalDensityUnitId((Integer) foodIntakeHistory.get("CAL_DENSITY_UNIT_ID"));
						BigDecimal calDensity = (BigDecimal) foodIntakeHistory.get("CAL_DENSITY");
						historyDto.setCalDensity(calDensity != null ? calDensity.doubleValue() : 0);

						historyDto.setIsDeleted((Integer) ((Boolean) foodIntakeHistory.get("IS_DELETED") ? 1 : 0));

						Integer otherFoodId = (Integer) (foodIntakeHistory.get("OTHER_FOOD_ID")==null?0:foodIntakeHistory.get("OTHER_FOOD_ID"));

						ArrayList<FoodIntakeHistoryDTO> listDto = new ArrayList<FoodIntakeHistoryDTO>();
						if (foodIntakeHistoryMap != null && foodIntakeHistoryMap.containsKey(otherFoodId)) {
							listDto = (ArrayList<FoodIntakeHistoryDTO>) foodIntakeHistoryMap.get(otherFoodId);
							listDto.add(historyDto);
						} else {
							listDto.add(historyDto);
						}
						foodIntakeHistoryMap.put(otherFoodId, listDto);
						//foodIntakeHistoryList.add(historyDto);
						
					});
				}

				if (key.equals(RESULT_SET_3)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(feedBack -> {
						DietFeedbackDTO dto = new DietFeedbackDTO();
						dto.setDietFeedbackId((Integer) feedBack.get("INTAKE_FEEDBACK_ID"));
						dto.setIntakeId((Integer) feedBack.get("INTAKE_ID"));
						dto.setFeedbackId((Integer) feedBack.get("FEEDBACK_ID"));
						dto.setFeedbackText((String) feedBack.get("FEEDBACK_TEXT"));
						dto.setIsDeleted((Integer) ((Boolean) feedBack.get("IS_DELETED") ? 1 : 0));
						dietFeedbackList.add(dto);
					});
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetIntakeById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		
		String intakeDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(foodIntakeDto.getIntakeDate());
		FoodIntakeLookUpResponse res = getPetFoodIntakeConfigData(foodIntakeDto.getPetId(), 
				foodIntakeDto.getPetParentId(), intakeDate);
		
		List<OtherFoods> otherFoods = res.getOtherFoods();
		otherFoods.forEach(obj->{
			obj.setFoodIntakeHistory(foodIntakeHistoryMap.get(obj.getOtherFoodId()) != null
					? foodIntakeHistoryMap.get(obj.getOtherFoodId())
					: new ArrayList<>());
		});
		res.setOtherFoods(otherFoods);
		
		List<DietFeedback> feedbackList = res.getDietFeedback();
		feedbackList.forEach(obj->{
			dietFeedbackList.forEach(feedbackObj->{
				if(feedbackObj.getFeedbackId() == obj.getFeedbackId()) {
					obj.setSelectedFeedback(feedbackObj);
				}
			});
		});
		res.setDietFeedback(feedbackList);
		
		List<PetRecommondedDietsDTO> recommondedDiet = res.getRecommondedDiet();
		recommondedDiet.forEach(obj->{
			obj.setFoodIntakeHistory(foodIntakeHistoryMap.get(0) != null
					? foodIntakeHistoryMap.get(0)
					: new ArrayList<>());
		});
		res.setRecommondedDiet(recommondedDiet);

		foodIntakeDto.setIntakeData(res);
		//foodIntakeDto.setFoodIntakeHistory(foodIntakeHistoryList);
		//foodIntakeDto.setDietFeedback(dietFeedbackList);

		return foodIntakeDto;
	}

	/**
	 * @author akumarkhaspa
	 * @param foodIntakeDTO
	 * @return
	 */
	@Override
	public FoodIntakeDTO saveOrUpdatePetFoodIntake(FoodIntakeDTO foodIntakeDTO) throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_food_intake_id", foodIntakeDTO.getIntakeId());
			inputParams.put("p_pet_id", foodIntakeDTO.getPetId());
			inputParams.put("p_pet_parent_id", foodIntakeDTO.getPetParentId());
			inputParams.put("p_intake_date", foodIntakeDTO.getIntakeDate());
			inputParams.put("p_is_other_food_fed", (foodIntakeDTO.getIsOtherFood() ? 1 : 0));
			inputParams.put("p_login_user_id", foodIntakeDTO.getUserId());
			inputParams.put("p_intakes", new ObjectMapper().writeValueAsString(foodIntakeDTO.getFoodIntakeHistory()));
			inputParams.put("p_feedback", new ObjectMapper().writeValueAsString(foodIntakeDTO.getDietFeedback()));

			LOGGER.info("saveOrUpdatePetFoodIntake inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_SAVE_PET_INTAKE, inputParams);
			LOGGER.info("saveOrUpdatePetFoodIntake outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");

			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				if (outParams.get("last_insert_id") != null) {
					Integer intakeId = (int) outParams.get("last_insert_id");
					foodIntakeDTO.setIntakeId(intakeId);
				}
			} else {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing saveOrUpdatePetFoodIntake ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return foodIntakeDTO;
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
	@SuppressWarnings("unchecked")
	@Override
	public FoodIntakeHistoryGraphResponse studyDietIntakeHistory(int petId, int petParentId, int dietId,
			String fromDate, String toDate) throws ServiceExecutionException {
		LOGGER.debug("studyDietIntakeHistory called");
		FoodIntakeHistoryGraphResponse response = new FoodIntakeHistoryGraphResponse();
		List<PetRecommondedDietsDTO> dietList = new ArrayList<>();
		List<FoodIntakeHistoryGraphDTO> foodIntakeHistoryGraphList = new ArrayList<>();
		Map<LocalDate, List<FoodIntakeHistoryDTO>> intakeComparisionMap = new HashMap<>();
		Map<LocalDate, List<FoodIntakeHistoryDTO>> intakeDistributionMap = new HashMap<>();

		try {
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_id", petId);
			inputParams.put("p_pet_parent_id", petParentId);
			inputParams.put("p_diet_id", dietId);
			inputParams.put("p_from_date", fromDate);
			inputParams.put("p_to_date", toDate);

			LOGGER.info("studyDietIntakeHistory inputParams are {}", inputParams);
			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(MOBILE_APP_GET_INTAKE_HISTORY_CHART_DATA, inputParams);
			// LOGGER.info("studyDietIntakeHistory outParams are {}", simpleJdbcCallResult);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(diet -> {
						PetRecommondedDietsDTO recommondedDiet = new PetRecommondedDietsDTO();
						recommondedDiet.setDietId((Integer) diet.get("DIET_ID"));
						recommondedDiet.setDietName((String) diet.get("DIET_NAME"));
						recommondedDiet.setIsDietSelected((Integer) diet.get("DIET_SELECTED"));
						dietList.add(recommondedDiet);
					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(foodIntakeHistory -> {
						FoodIntakeHistoryDTO historyDto = new FoodIntakeHistoryDTO();
						historyDto.setDietId((Integer) foodIntakeHistory.get("DIET_ID"));
						historyDto.setDietName((String) foodIntakeHistory.get("DIET_NAME"));
						
						/***********      Commented the below code which is used for the old logic     ************/
						/*Long unitId = (Long)foodIntakeHistory.get("RECOMMENDED_UNIT_ID");
						historyDto.setQuantityUnitId(unitId != null ? unitId.intValue() : 0);
						//historyDto.setQuantityUnitId(Math.toIntExact((Long)foodIntakeHistory.get("RECOMMENDED_UNIT_ID")));
						historyDto.setQuantityUnitName((String) foodIntakeHistory.get("UNIT"));

						Double quantityRecommended = (Double) foodIntakeHistory.get("RECOMMENDED_FOOD_AMOUNT");
						historyDto.setQuantityRecommended(quantityRecommended != null ? quantityRecommended.doubleValue() : 0);

						Double quantityOffered = (Double) foodIntakeHistory.get("QUANTITY_OFFERED");
						historyDto.setQuantityOffered(quantityOffered != null ? quantityOffered.doubleValue() : 0);

						Double quantityConsumed = (Double) foodIntakeHistory.get("QUANTITY_CONSUMED");
						historyDto.setQuantityConsumed(quantityConsumed != null ? quantityConsumed.doubleValue() : 0);*/
						
						/***********      Added the below code which is used for the new logic     ************/
						BigDecimal quantityRecommended = (BigDecimal) foodIntakeHistory.get("RECOMMENDED_FOOD_AMOUNT_GRAMS");
						historyDto.setQuantityRecommended(quantityRecommended != null ? quantityRecommended.doubleValue() : 0);

						BigDecimal quantityOffered = (BigDecimal) foodIntakeHistory.get("QUANTITY_OFFERED_GRAMS");
						historyDto.setQuantityOffered(quantityOffered != null ? quantityOffered.doubleValue() : 0);

						BigDecimal quantityConsumed = (BigDecimal) foodIntakeHistory.get("QUANTITY_CONSUMED_GRAMS");
						historyDto.setQuantityConsumed(quantityConsumed != null ? quantityConsumed.doubleValue() : 0);
						
						BigDecimal quantityRecommendedCups = (BigDecimal) foodIntakeHistory.get("RECOMMENDED_FOOD_AMOUNT_CUPS");
						historyDto.setQuantityRecommendedCups(quantityRecommendedCups != null ? quantityRecommendedCups.doubleValue() : 0);

						BigDecimal quantityOfferedCups = (BigDecimal) foodIntakeHistory.get("QUANTITY_OFFERED_CUPS");
						historyDto.setQuantityOfferedCups(quantityOfferedCups != null ? quantityOfferedCups.doubleValue() : 0);

						BigDecimal quantityConsumedCups = (BigDecimal) foodIntakeHistory.get("QUANTITY_CONSUMED_CUPS");
						historyDto.setQuantityConsumedCups(quantityConsumedCups != null ? quantityConsumedCups.doubleValue() : 0);

						Date date = (Date) foodIntakeHistory.get("INTAKE_DATE");
						LocalDate foodIntakeDate = date.toLocalDate();
						//LocalDate foodIntakeDate = (LocalDate) foodIntakeHistory.get("INTAKE_DATE");
						
						ArrayList<FoodIntakeHistoryDTO> listDto = new ArrayList<FoodIntakeHistoryDTO>();
						if (intakeComparisionMap != null && intakeComparisionMap.containsKey(foodIntakeDate)) {
							listDto = (ArrayList<FoodIntakeHistoryDTO>) intakeComparisionMap.get(foodIntakeDate);
							listDto.add(historyDto);
						} else {
							listDto.add(historyDto);
						}
						intakeComparisionMap.put(foodIntakeDate, listDto);

					});
				}

				if (key.equals(RESULT_SET_3)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(foodIntakeHistory -> {
						FoodIntakeHistoryDTO historyDto = new FoodIntakeHistoryDTO();
						historyDto.setDietId((Integer) foodIntakeHistory.get("DIET_ID"));
						historyDto.setOtherFoodtypeId((Integer) foodIntakeHistory.get("OTHER_FOOD_ID"));
						historyDto.setDietName((String) foodIntakeHistory.get("DIET_NAME"));
						BigDecimal percentConsumed = (BigDecimal) foodIntakeHistory.get("PERCENT_CONSUMED");
						historyDto.setPercentConsumed(percentConsumed != null ? percentConsumed.doubleValue() : 0);

						Date date = (Date) foodIntakeHistory.get("INTAKE_DATE");
						LocalDate foodIntakeDate = date.toLocalDate();

						ArrayList<FoodIntakeHistoryDTO> listDto = new ArrayList<FoodIntakeHistoryDTO>();
						if (intakeDistributionMap != null && intakeDistributionMap.containsKey(foodIntakeDate)) {
							listDto = (ArrayList<FoodIntakeHistoryDTO>) intakeDistributionMap.get(foodIntakeDate);
							listDto.add(historyDto);
						} else {
							listDto.add(historyDto);
						}
						intakeDistributionMap.put(foodIntakeDate, listDto);

					});
				}

				if (key.equals(RESULT_SET_4)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(foodIntake -> {
						FoodIntakeHistoryGraphDTO foodIntakeDto = new FoodIntakeHistoryGraphDTO();
						Date date = (Date) foodIntake.get("INTAKE_DATE");
						foodIntakeDto.setIntakeDate(date.toLocalDate());

						foodIntakeDto.setIntakeComparision(intakeComparisionMap.get(foodIntakeDto.getIntakeDate()) != null
										? intakeComparisionMap.get(foodIntakeDto.getIntakeDate())
										: new ArrayList<>());

						foodIntakeDto.setIntakeDistribution(intakeDistributionMap.get(foodIntakeDto.getIntakeDate()) != null
										? intakeDistributionMap.get(foodIntakeDto.getIntakeDate())
										: new ArrayList<>());

						foodIntakeHistoryGraphList.add(foodIntakeDto);
					});
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching studyDietIntakeHistory", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		response.setFoodIntakeHistory(foodIntakeHistoryGraphList);
		response.setDietList(dietList);
		return response;
	}

}