package com.hillspet.wearables.dao.user.impl;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.threeten.bp.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.common.utils.BeanUtil;
import com.hillspet.wearables.common.utils.Cryptography;
import com.hillspet.wearables.dao.BaseDaoImpl;
import com.hillspet.wearables.dao.user.MigratedDao;
import com.hillspet.wearables.dto.Address;
import com.hillspet.wearables.dto.ClientInfo;
import com.hillspet.wearables.dto.ClientSMSCode;
import com.hillspet.wearables.dto.DeviceAssignDTO;
import com.hillspet.wearables.dto.DeviceInfo;
import com.hillspet.wearables.dto.Menu;
import com.hillspet.wearables.dto.MobileAppFeedbackDTO;
import com.hillspet.wearables.dto.MonitoringPlan;
import com.hillspet.wearables.dto.OnboardingInfo;
import com.hillspet.wearables.dto.PetCheckedInfo;
import com.hillspet.wearables.dto.PetInfoDTO;
import com.hillspet.wearables.dto.PetParentKeyInfoDTO;
import com.hillspet.wearables.dto.SensorDetailsDTO;
import com.hillspet.wearables.dto.TimerLog;
import com.hillspet.wearables.dto.User;
import com.hillspet.wearables.dto.UserInfo;

@Repository
public class MigratedDaoImpl extends BaseDaoImpl implements MigratedDao {

	private static final Logger LOGGER = LogManager.getLogger(MigratedDaoImpl.class);

	public static final String RESULT_SET_1 = "#result-set-1";
	public static final String RESULT_SET_2 = "#result-set-2";
	public static final String RESULT_SET_3 = "#result-set-3";
	public static final String RESULT_SET_4 = "#result-set-4";

	public static final String MOBILE_APP_GET_USER_INFO_BY_EMAIL = "call MOBILE_APP_GET_USER_INFO_BY_EMAIL(?)";
	public static final String MOBILE_APP_VALIDATE_PET_PARENT_LOGIN = "MOBILE_APP_VALIDATE_PET_PARENT_LOGIN";
	public static final String MOBILE_APP_USER_LOGIN = "MOBILE_APP_USER_LOGIN";
	public static final String MOBILE_APP_INSERT_PET_PARENT_KEY = "MOBILE_APP_INSERT_PET_PARENT_KEY";
	public static final String MOBILE_APP_UPDATE_PET_PARENT_INFO = "MOBILE_APP_UPDATE_PET_PARENT_INFO";
	public static final String MOBILE_APP_UPDATE_USER_INFO = "MOBILE_APP_UPDATE_USER_INFO";
	public static final String MOBILE_APP_GET_PET_PARENT_SMS_CODE_BY_PET_PARENT_ID = "call MOBILE_APP_GET_PET_PARENT_SMS_CODE_BY_PET_PARENT_ID(?)";
	public static final String MOBILE_APP_GET_USER_SMS_CODE_BY_USER_ID = "call MOBILE_APP_GET_USER_SMS_CODE_BY_USER_ID(?)";
	public static final String MOBILE_APP_UPDATE_PET_PARENT_SMS_CODE = "MOBILE_APP_UPDATE_PET_PARENT_SMS_CODE";
	public static final String MOBILE_APP_INSERT_PET_PARENT_SMS_CODE = "MOBILE_APP_INSERT_PET_PARENT_SMS_CODE";
	public static final String MOBILE_APP_GET_SMS_CODE_BY_PET_PARENT_ID_AND_VERIFICATION_CODE = "MOBILE_APP_GET_SMS_CODE_BY_PET_PARENT_ID_AND_VERIFICATION_CODE";
	public static final String MOBILE_APP_UPDATE_USER_PASSWORD = "MOBILE_APP_UPDATE_USER_PASSWORD";
	public static final String MOBILE_APP_GET_PET_LIST_BY_PET_PARENT_ID = "call MOBILE_APP_GET_PET_LIST_BY_PET_PARENT_ID(?)";
	public static final String MOBILE_APP_GET_DEVICE_ASSIGN_LIST_BY_PLAN_ID_AND_PET_ID = "CALL MOBILE_APP_GET_DEVICE_ASSIGN_LIST_BY_PLAN_ID_AND_PET_ID(?,?)";
	public static final String MOBILE_APP_PET_PARENT_AUTH_BY_KEY = "MOBILE_APP_PET_PARENT_AUTH_BY_KEY";
	public static final String MOBILE_APP_GET_PET_PARENT_INFO_BY_PET_PARENT_ID = "MOBILE_APP_GET_PET_PARENT_INFO_BY_PET_PARENT_ID";
	public static final String MOBILE_APP_GET_PET_PARENT_PASSWORD_BY_PET_PARENT_ID = "call MOBILE_APP_GET_PET_PARENT_PASSWORD_BY_PET_PARENT_ID(?)";
	public static final String MOBILE_APP_GET_PET_TIMER_LOG = "call MOBILE_APP_GET_PET_TIMER_LOG(?)";
	public static final String MOBILE_APP_INSERT_PET_TIMER_LOG = "MOBILE_APP_INSERT_PET_TIMER_LOG";
	public static final String MOBILE_APP_INSERT_MOBILE_APP_SCREENS_FEEDBACK = "MOBILE_APP_INSERT_MOBILE_APP_SCREENS_FEEDBACK";
	public static final String MOBILE_APP_UPDATE_SENSOR_SETUP_STATUS = "MOBILE_APP_UPDATE_SENSOR_SETUP_STATUS";
	public static final String MOBILE_APP_GET_SENSOR_SETUP_STATUS = "MOBILE_APP_GET_SENSOR_SETUP_STATUS";
	public static final String MOBILE_APP_INSERT_UPDATE_SENSOR_CHARGING_NOTIFICATION_SETTINGS = "MOBILE_APP_INSERT_UPDATE_SENSOR_CHARGING_NOTIFICATION_SETTINGS";
	public static final String MOBILE_APP_GET_DEVICE_ASSIGNMENT_BY_DEVICE_NUMBER = "MOBILE_APP_GET_DEVICE_ASSIGNMENT_BY_DEVICE_NUMBER";
	public static final String MOBILE_APP_INSERT_PET_PARENT_INFO = "MOBILE_APP_INSERT_PET_PARENT_INFO";
	public static final String MOBILE_APP_HANDLE_ONBOARDING_INFO = "MOBILE_APP_HANDLE_ONBOARDING_INFO";
	public static final String MOBILE_APP_GET_ONBOARDING_INFO_BY_UID = "call MOBILE_APP_GET_ONBOARDING_INFO_BY_UID(?)";
	public static final String MOBILE_APP_GET_STUDY_PLANS_LIST_BY_STUDY_ID = "call MOBILE_APP_GET_STUDY_PLANS_LIST_BY_STUDY_ID(?)";
	public static final String MOBILE_APP_GET_PET_INFO_BY_PET_ID = "call MOBILE_APP_GET_PET_INFO_BY_PET_ID(?)";
	public static final String MOBILE_APP_UPDATE_PET_INFO = "MOBILE_APP_UPDATE_PET_INFO";
	public static final String MOBILE_APP_INSERT_PET_INFO = "MOBILE_APP_INSERT_PET_INFO";
	public static final String MOBILE_APP_INSERT_PET_CHECKED_INFO = "MOBILE_APP_INSERT_PET_CHECKED_INFO";
	public static final String MOBILE_APP_INSERT_PET_STUDY_DEVICE = "MOBILE_APP_INSERT_PET_STUDY_DEVICE";
	public static final String MOBLIE_APP_GET_DEVICE_INFO_BY_DEVICE_NUMBER = "call MOBLIE_APP_GET_DEVICE_INFO_BY_DEVICE_NUMBER(?)";
	public static final String MOBILE_APP_INSERT_DEVICE_INFO = "MOBILE_APP_INSERT_DEVICE_INFO";
	public static final String MOBILE_APP_UPDATE_ONBOARDING_ARCHIVED = "MOBILE_APP_UPDATE_ONBOARDING_ARCHIVED";
	public static final String MOBILE_APP_UPDATE_ONBOARDING_STATUS = "MOBILE_APP_UPDATE_ONBOARDING_STATUS";
	public static final String MOBILE_APP_INSERT_MONITORING_PLAN = "MOBILE_APP_INSERT_MONITORING_PLAN";
	public static final String MOBILE_APP_PET_PARENT_LOG_OUT = "MOBILE_APP_PET_PARENT_LOG_OUT";
	public static final String MOBILE_APP_USER_DELETE_ACCOUNT = "MOBILE_APP_USER_DELETE_ACCOUNT";
	public static final String MOBILE_APP_GET_CLIENT_INFO_BY_SECONDARY_EMAIL = "call MOBILE_APP_GET_CLIENT_INFO_BY_SECONDARY_EMAIL (?)";
	public static final String MOBILE_APP_REGISTER_USER = "MOBILE_APP_REGISTER_USER";
	public static final String MOBILE_APP_VALIDATE_EMAIL_BY_SECONDARY_EMAIL_CLIENT_ID = "call MOBILE_APP_VALIDATE_EMAIL_BY_SECONDARY_EMAIL_CLIENT_ID (?, ?)";
	public static final String MOBILE_APP_GET_USER_PERMISSIONS = "call  MOBILE_APP_GET_USER_PERMISSIONS(?)";
	public static final String MIGRATE_GET_PET_PARENT_PASSWORD_TO_DECRYPT = "call  MIGRATE_GET_PET_PARENT_PASSWORD_TO_DECRYPT()";
	public static final String MOBILE_APP_GET_USER_PASSWORD_BY_ID = "call MOBILE_APP_GET_USER_PASSWORD_BY_ID(?)";
	public static final String MOBILE_APP_VALIDATE_EMAIL_BY_SECONDARY_EMAIL_USER_ID = "call MOBILE_APP_VALIDATE_EMAIL_BY_SECONDARY_EMAIL_USER_ID (?, ?)";

	public ClientInfo getClientInfoByEmail(String email) throws ServiceExecutionException {
		LOGGER.info("entered into getClientInfoByEmail   " + email);
		ClientInfo clientInfo = new ClientInfo();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_USER_INFO_BY_EMAIL, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					// set the column values to fields like below
					clientInfo.setEmail(rs.getString("EMAIL"));
					clientInfo.setPhoneNumber(rs.getString("PHONE_NUMBER"));
					clientInfo.setUniqueId(rs.getString("UNIQUE_ID"));
					clientInfo.setClientId(rs.getInt("PET_PARENT_ID"));
					clientInfo.setActive(rs.getInt("IS_ACTIVE") == 1 ? Boolean.TRUE : Boolean.FALSE);
					clientInfo.setRoleId(rs.getInt("ROLE_ID"));
					clientInfo.setRoleName(rs.getString("ROLE_NAME"));
					clientInfo.setUserId(rs.getInt("USER_ID"));
				}
			}, email);

			LOGGER.info("clientInfo clientId {}", clientInfo.getClientId());

		} catch (Exception e) {
			LOGGER.error("error while fetching getClientInfoByEmail", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.info("returning from getClientInfoByEmail");
		return clientInfo;
	}

	@SuppressWarnings("unchecked")
	public UserInfo userLogin(String email) throws ServiceExecutionException {
		LOGGER.info("entered into userLogin");

		UserInfo userInfo = new UserInfo();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_email", email);

			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_USER_LOGIN, inputParams);
			LOGGER.info("userLogin outParams are {}", outParams);

			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				if (entry.getValue() instanceof Integer) {
					LOGGER.info("No records found");
				} else {
					LOGGER.info("record found");
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(userDtls -> {
						userInfo.setUserId((Integer) userDtls.get("USER_ID"));
						userInfo.setUserName((String) userDtls.get("FULL_NAME"));
						userInfo.setPassword((String) userDtls.get("USER_PASSWORD"));
						userInfo.setPhoneNumber((String) userDtls.get("PHONE_NUMBER"));
						userInfo.setNeedToChangePassword((Integer) userDtls.get("NEED_CHANGE_PWD"));
					});
				}
			}
			LOGGER.info("returning from userLogin");
		} catch (SQLException e) {
			LOGGER.error("error while executing userLogin ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return userInfo;
	}

	@SuppressWarnings("unchecked")
	public ClientInfo clientLogin(String email, String password) throws ServiceExecutionException {
		LOGGER.info("entered into clientLogin");

		ClientInfo clientInfo = new ClientInfo();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_email", email);
			inputParams.put("p_password", password);

			// LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_VALIDATE_PET_PARENT_LOGIN, inputParams);
			LOGGER.info("clientLogin outParams are {}", outParams);

			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				if (entry.getValue() instanceof Integer) {
					LOGGER.info("No records found");
				} else {
					LOGGER.info("record found");
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(sensorSetupStatus -> {
						clientInfo.setClientId((Integer) sensorSetupStatus.get("PET_PARENT_ID"));
						clientInfo.setUniqueId((String) sensorSetupStatus.get("UNIQUE_ID"));
						clientInfo.setClientName((String) sensorSetupStatus.get("PET_PARENT_NAME"));
						clientInfo.setFullName((String) sensorSetupStatus.get("FULL_NAME"));
						clientInfo.setEmail((String) sensorSetupStatus.get("EMAIL"));
						clientInfo.setPhoneNumber((String) sensorSetupStatus.get("PHONE_NUMBER"));
						clientInfo.setCustomerID((String) sensorSetupStatus.get("CUSTOMER_ID"));
						clientInfo.setActive((Boolean) sensorSetupStatus.get("IS_ACTIVE"));
					});
				}
			}
			LOGGER.info("returning from clientLogin");
		} catch (SQLException e) {
			LOGGER.error("error while executing clientLogin ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return clientInfo;
	}

	@Override
	public String insertClientKey(PetParentKeyInfoDTO parentKeyInfoDTO) throws ServiceExecutionException {
		LOGGER.debug("entered into insertClientKey");
		String authToken = "";
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_id", parentKeyInfoDTO.getPetParentId());
			inputParams.put("p_pet_parent_key", parentKeyInfoDTO.getKey());
			inputParams.put("p_fcm_token", parentKeyInfoDTO.getFcmToken());
			inputParams.put("p_login_app_version", parentKeyInfoDTO.getAppVersion());
			inputParams.put("p_login_os", parentKeyInfoDTO.getAppOS());
			inputParams.put("p_is_expired", parentKeyInfoDTO.getIsExpired());
			inputParams.put("p_add_date", parentKeyInfoDTO.getAddDate());
			inputParams.put("p_login_user_id", parentKeyInfoDTO.getUserId());

			LOGGER.info("insertClientKey inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_PARENT_KEY, inputParams);
			LOGGER.info("insertClientKey outParams are {}", outParams);
			// System.out.println(outParams);
			authToken = (String) outParams.get("auth_token");

			LOGGER.debug("returning from insertClientKey");
		} catch (SQLException e) {
			LOGGER.error("error while executing insertClientKey ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return authToken;
	}

	@Override
	public boolean updateClientInfo(ClientInfo clientInfo) throws ServiceExecutionException {
		LOGGER.debug("entered into updateClientInfo");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_id", clientInfo.getClientId());
			inputParams.put("p_full_name", clientInfo.getFullName());
			inputParams.put("p_first_name", clientInfo.getFirstName());
			inputParams.put("p_last_name", clientInfo.getLastName());
			inputParams.put("p_email", clientInfo.getEmail());
			inputParams.put("p_phone_number", clientInfo.getPhoneNumber());
			inputParams.put("p_fcm_token", clientInfo.getFcmToken());
			inputParams.put("p_login_user_id", clientInfo.getClientId());
			inputParams.put("p_secondary_email", clientInfo.getSecondaryEmail());
			inputParams.put("p_notfi_secondary_email", clientInfo.getNotifyToSecondaryEmail());
			inputParams.put("p_residential_address_json",
					new ObjectMapper().writeValueAsString(clientInfo.getPetParentAddress()));
			inputParams.put("p_login_app_version", clientInfo.getAppVersion());
			inputParams.put("p_login_app_os", clientInfo.getAppOS());

			LOGGER.info("updateClientInfo inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_UPDATE_PET_PARENT_INFO, inputParams);
			LOGGER.info("updateClientInfo outParams are {}", outParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");

			int clientId = 0;
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				clientId = (int) outParams.get("last_insert_id");
			}
			LOGGER.debug("returning from updateClientInfo");
			return clientId > 0 ? Boolean.TRUE : Boolean.FALSE;
		} catch (Exception e) {
			LOGGER.error("error while executing updateClientInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public boolean updateUserInfo(User user) throws ServiceExecutionException {
		LOGGER.debug("entered into updateUserInfo");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_user_id", user.getUserId());
			inputParams.put("p_pet_parent_id", user.getPetParentId());
			inputParams.put("p_full_name", user.getFullName());
			inputParams.put("p_first_name", user.getFirstName());
			inputParams.put("p_last_name", user.getLastName());
			inputParams.put("p_email", user.getEmail());
			inputParams.put("p_phone_number", user.getPhoneNumber());
			inputParams.put("p_login_user_id", user.getCreatedBy());
			inputParams.put("p_secondary_email", user.getSecondaryEmail());
			inputParams.put("p_residential_address_json", new ObjectMapper().writeValueAsString(user.getAddress()));
			inputParams.put("p_preferred_weight_unit_id", user.getPreferredWeightUnitId());
			inputParams.put("p_preferred_food_unit_id", user.getPreferredFoodRecUnitId());
			inputParams.put("p_preferred_recomm_time", user.getPreferredFoodRecTime());

			LOGGER.info("updateUserInfo inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_UPDATE_USER_INFO, inputParams);
			LOGGER.info("updateUserInfo outParams are {}", outParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");

			int userId = 0;
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				userId = (int) outParams.get("last_insert_id");
			}
			LOGGER.debug("returning from updateUserInfo");
			return userId > 0 ? Boolean.TRUE : Boolean.FALSE;
		} catch (Exception e) {
			LOGGER.error("error while executing updateUserInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public ClientSMSCode getClientSMSCodeByUserId(int userId) throws ServiceExecutionException {
		LOGGER.debug("entered into getClientSMSCodeByUserId");
		ClientSMSCode clientSMSCode = new ClientSMSCode();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_USER_SMS_CODE_BY_USER_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					clientSMSCode.setPetParentSMSCodeId(rs.getInt("PET_PARENT_SMS_CODE_ID"));
					clientSMSCode.setClientID(rs.getInt("PET_PARENT_ID"));
					clientSMSCode.setUserId(rs.getInt("USER_ID"));
					clientSMSCode.setVerificationCode(rs.getString("VERIFICATION_CODE"));
					int expired = rs.getInt("IS_EXPIRED");
					clientSMSCode.setExpired(expired > 0 ? true : false);
					clientSMSCode.setAddDate(rs.getString("ADD_DATE"));
					int active = rs.getInt("IS_ACTIVE");
					clientSMSCode.setIsActive(active > 0 ? true : false);
					clientSMSCode.setCreateDate(rs.getString("CREATED_DATE"));
					clientSMSCode.setUpdateDate(rs.getString("MODIFIED_DATE"));
				}
			}, userId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getClientSMSCodeByUserId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("returning from getClientSMSCodeByUserId");
		return clientSMSCode;
	}

	@Override
	public int updateClientSMSCode(ClientSMSCode clientSMSCode) throws ServiceExecutionException {
		LOGGER.debug("entered into updateClientSMSCode");
		int uniqueId = 0;
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_parent_sms_code_id", clientSMSCode.getPetParentSMSCodeId());
			inputParams.put("p_login_user_id", clientSMSCode.getUserId());

			LOGGER.info("updateClientSMSCode inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_UPDATE_PET_PARENT_SMS_CODE, inputParams);
			LOGGER.info("updateClientSMSCode outParams are {}", outParams);
			// System.out.println(outParams);
			uniqueId = (int) outParams.get("last_insert_id");
			LOGGER.debug("returning from updateClientSMSCode");
		} catch (SQLException e) {
			LOGGER.error("error while executing updateClientSMSCode ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return uniqueId;
	}

	@Override
	public int insertClientSMSCode(ClientSMSCode clientSMSCode) throws ServiceExecutionException {
		LOGGER.debug("entered into insertClientSMSCode");
		int uniqueId = 0;
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_id", clientSMSCode.getClientID());
			inputParams.put("p_verification_code", clientSMSCode.getVerificationCode());
			inputParams.put("p_is_expired", clientSMSCode.getExpired());
			inputParams.put("p_add_date", clientSMSCode.getAddDate());
			inputParams.put("p_user_id", clientSMSCode.getUserId());

			LOGGER.info("insertClientSMSCode inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_PARENT_SMS_CODE, inputParams);
			LOGGER.info("insertClientSMSCode outParams are {}", outParams);
			// System.out.println(outParams);
			uniqueId = (int) outParams.get("last_insert_id");
			LOGGER.debug("returning from insertClientSMSCode");
		} catch (SQLException e) {
			LOGGER.error("error while executing insertClientSMSCode ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return uniqueId;
	}

	@SuppressWarnings("unchecked")
	public ClientSMSCode getClientSMSCodeByClientIDAndVerificationCode(int clientID, String verificationCode)
			throws ServiceExecutionException {
		LOGGER.debug("entered into getClientSMSCodeByClientIDAndVerificationCode");
		ClientSMSCode smsCodeDTO = new ClientSMSCode();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_id", clientID);
			inputParams.put("p_verification_code", verificationCode);

			LOGGER.info("getClientSMSCodeByClientIDAndVerificationCode inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(
					MOBILE_APP_GET_SMS_CODE_BY_PET_PARENT_ID_AND_VERIFICATION_CODE, inputParams);
			LOGGER.info("getClientSMSCodeByClientIDAndVerificationCode outParams are {}", outParams);

			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				if (entry.getValue() instanceof Integer) {
					LOGGER.debug("No records found");
				} else {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(sensorSetupStatus -> {
						smsCodeDTO.setVerificationCode((String) sensorSetupStatus.get("VERIFICATION_CODE"));
						smsCodeDTO.setIsActive((Boolean) sensorSetupStatus.get("IS_ACTIVE"));
						LocalDateTime addDate = (LocalDateTime) sensorSetupStatus.get("ADD_DATE");
						smsCodeDTO.setAddDate(addDate.toString());
						LocalDateTime createdDate = (LocalDateTime) sensorSetupStatus.get("CREATED_DATE");
						smsCodeDTO.setCreateDate(createdDate.toString());
						smsCodeDTO.setExpired((Boolean) sensorSetupStatus.get("IS_EXPIRED"));
						LocalDateTime modifiedDate = (LocalDateTime) sensorSetupStatus.get("MODIFIED_DATE");
						smsCodeDTO.setUpdateDate(modifiedDate.toString());
						smsCodeDTO.setClientID((Integer) sensorSetupStatus.get("PET_PARENT_ID"));
						smsCodeDTO.setPetParentSMSCodeId((Integer) sensorSetupStatus.get("PET_PARENT_SMS_CODE_ID"));
						smsCodeDTO.setUserId((Integer) sensorSetupStatus.get("USER_ID"));
					});
				}
			}
			LOGGER.debug("returning from getClientSMSCodeByClientIDAndVerificationCode");
		} catch (

		SQLException e) {
			LOGGER.error("error while executing getClientSMSCodeByClientIDAndVerificationCode ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return smsCodeDTO;
	}

	@Override
	public int updatePassword(ClientInfo clientInfo) throws ServiceExecutionException {
		LOGGER.debug("entered into updatePassword");
		int uniqueId = 0;
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_user_id", clientInfo.getUserId());
			inputParams.put("p_password", clientInfo.getPassword());

			// LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_UPDATE_USER_PASSWORD, inputParams);
			LOGGER.info("updatePassword outParams are {}", outParams);

			uniqueId = (int) outParams.get("last_insert_id");
			LOGGER.debug("returning from insertClientKey");
		} catch (SQLException e) {
			LOGGER.error("error while executing updatePassword ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return uniqueId;
	}

	@Override
	public List<PetInfoDTO> getPetListByPetParentId(int petParentId) throws ServiceExecutionException {
		LOGGER.debug("entered into getPetListByPetParentId");
		List<PetInfoDTO> petList = new ArrayList<PetInfoDTO>();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_LIST_BY_PET_PARENT_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetInfoDTO petInfoDTO = new PetInfoDTO();
					petInfoDTO.setPetId(rs.getInt("PET_ID"));
					petInfoDTO.setPetName(rs.getString("PET_NAME"));
					petInfoDTO.setGender(rs.getString("GENDER"));
					petInfoDTO.setBreedId(rs.getInt("BREED_ID"));
					petInfoDTO.setMixBreed(rs.getString("MIX_BREED"));
					petInfoDTO.setIsMixed(rs.getBoolean("IS_MIXED"));
					petInfoDTO.setUnknown(rs.getBoolean("IS_UNKNOWN"));
					petInfoDTO.setNeutered(rs.getBoolean("IS_NEUTERED"));
					petInfoDTO.setPlanId(rs.getInt("PLAN_ID"));
					petInfoDTO.setPlanStatus(rs.getInt("PLAN_STATUS"));
					petList.add(petInfoDTO);
				}
			}, petParentId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetListByPetParentId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("returning from getPetListByPetParentId");
		return petList;
	}

	public List<DeviceAssignDTO> getDeviceAssignListByPlanIDAndPetID(int planId, int petId)
			throws ServiceExecutionException {
		LOGGER.debug("entered into getDeviceAssignListByPlanIDAndPetID");
		List<DeviceAssignDTO> deviceAssignList = new ArrayList<DeviceAssignDTO>();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_DEVICE_ASSIGN_LIST_BY_PLAN_ID_AND_PET_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					DeviceAssignDTO assignDTO = new DeviceAssignDTO();
					assignDTO.setDeviceID(rs.getInt("DEVICE_ID"));
					assignDTO.setDeviceNumber(rs.getString("DEVICE_NUMBER"));
					assignDTO.setPetID(rs.getInt("PET_ID"));
					assignDTO.setPlanID(rs.getInt("PLAN_ID"));
					assignDTO.setDeviceType(rs.getString("DEVICE_TYPE"));
					assignDTO.setNote(rs.getString("NOTE"));
					assignDTO.setFirmware(rs.getString("FIRMWARE"));
					assignDTO.setFirmwareNew((String) rs.getString("FIRMWARE_NEW"));
					assignDTO.setActive(rs.getBoolean("IS_ACTIVE"));
					assignDTO.setIsAssign(
							rs.getInt("IS_ASSIGN") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE : Boolean.FALSE);
					assignDTO.setBattery(rs.getFloat("BATTERY") > 0 ? String.valueOf(rs.getFloat("BATTERY")) : "");

					Date date = rs.getDate("LAST_SEEN");
					if (date != null) {
						java.util.Date utilDate = new java.util.Date();
						utilDate.setTime(date.getTime());

						assignDTO.setLastSeen(utilDate);
					}
					// assignDTO.setAssignDate(assignDate);
					// assignDTO.setUnAssignDate(unAssignDate);
					// assignDTO.setCreateDate(createDate);
					// assignDTO.setUpdateDate(updateDate);
					assignDTO.setLastDataReceived(rs.getString("LAST_DATA_RECEIVED"));

					assignDTO.setDataUploading(rs.getString("DATA_UPLOADING"));
					assignDTO.setIsFirmwareVersionUpdateRequired(
							rs.getInt("IS_FIRMWARE_VERSION_UPDATE_REQUIRED") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
									: Boolean.FALSE);
					assignDTO.setIsDeviceSetupDone(
							rs.getInt("IS_DEVICE_SETUP_DONE") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
									: Boolean.FALSE);
					deviceAssignList.add(assignDTO);
				}
			}, planId, petId);

			LOGGER.debug("returning from getDeviceAssignListByPlanIDAndPetID");
		} catch (Exception e) {
			LOGGER.error("error while executing getDeviceAssignListByPlanIDAndPetID ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceAssignList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClientInfo getClientInfoById(int clientId) throws ServiceExecutionException {

		LOGGER.debug("getClientInfoById called");
		ClientInfo clientInfo = new ClientInfo();
		Address petParentAddress = new Address();
		try {
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_parent_id", clientId);

			LOGGER.info("getClientInfoById inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_GET_PET_PARENT_INFO_BY_PET_PARENT_ID,
					inputParams);
			LOGGER.info("getClientInfoById outParams are {}", outParams);
			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(parent -> {
						clientInfo.setClientId((Integer) parent.get("PET_PARENT_ID"));
						clientInfo.setEmail((String) parent.get("EMAIL"));
						clientInfo.setPhoneNumber((String) parent.get("PHONE_NUMBER"));
						clientInfo.setFullName((String) parent.get("FULL_NAME"));
						clientInfo.setFirstName((String) parent.get("FIRST_NAME"));
						clientInfo.setLastName((String) parent.get("LAST_NAME"));
						clientInfo.setUniqueId((String) parent.get("UNIQUE_ID"));
						clientInfo.setFcmToken((String) parent.get("FCM_TOKEN"));
						clientInfo.setPhoneNumber((String) parent.get("PHONE_NUMBER"));
						clientInfo.setUserId((Integer) parent.get("USER_ID"));
						clientInfo.setSecondaryEmail((String) parent.get("SECONDARY_EMAIL"));

						if (parent.get("SECONDARY_EMAIL_NOTIF_FLAG") != null) {
							int isNotifySecondaryEmail = (Integer) parent.get("SECONDARY_EMAIL_NOTIF_FLAG");
							clientInfo.setNotifyToSecondaryEmail(isNotifySecondaryEmail == 1 ? true : false);
						}
					});
				}

				if (key.equals(RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(address -> {
						petParentAddress.setAddressId((Integer) address.get("PET_PARENT_ADDRESS_ID"));
						petParentAddress.setAddress1((String) address.get("ADDRESS_1"));
						petParentAddress.setAddress2((String) address.get("ADDRESS_2"));
						petParentAddress.setCity((String) address.get("CITY"));
						petParentAddress.setState((String) address.get("STATE"));
						petParentAddress.setCountry((String) address.get("COUNTRY"));
						petParentAddress.setZipCode((String) address.get("ZIP_CODE"));
						petParentAddress.setTimeZone((String) address.get("TIME_ZONE"));
						petParentAddress.setTimeZoneId((Integer) address.get("TIMEZONE_ID"));
						petParentAddress.setIsPreludeAddress((Integer) address.get("IS_PRELUDE_ADDRESS"));
					});
				}
				clientInfo.setPetParentAddress(petParentAddress);
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching getClientInfoById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return clientInfo;
	}

	@Override
	public String getPasswordByClientID(int clientId) throws ServiceExecutionException {
		LOGGER.debug("entered into getPasswordByClientID");
		ClientInfo clientInfo = new ClientInfo();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_PARENT_PASSWORD_BY_PET_PARENT_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					clientInfo.setPassword(rs.getString(1));
				}
			}, clientId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPasswordByClientID", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("returning from getPasswordByClientID");
		return clientInfo.getPassword();
	}

	@Override
	public List<TimerLog> getPetTimerLog(int petParentId) throws ServiceExecutionException {
		List<TimerLog> petTimerLogDTOs = new ArrayList<TimerLog>();
		LOGGER.debug("getPetTimerLog called");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_TIMER_LOG, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					TimerLog timerLog = new TimerLog();
					timerLog.setCategory(rs.getString("CATEGORY"));
					timerLog.setCreatedDate(rs.getString("CREATED_DATE"));
					timerLog.setDeviceNumber(rs.getString("DEVICE_NUMBER"));
					timerLog.setDuration(rs.getString("DURATION"));
					timerLog.setIsActive(rs.getInt("IS_ACTIVE"));
					timerLog.setPetId(rs.getInt("PET_ID"));
					timerLog.setPetName(rs.getString("PET_NAME"));
					timerLog.setPetParentId(rs.getInt("PET_PARENT_ID"));
					timerLog.setPetParentName(rs.getString("PET_PARENT_NAME"));
					timerLog.setPetTimerLogId(rs.getInt("PET_TIMER_LOG_ID"));
					timerLog.setRecName(rs.getString("REC_NAME"));
					timerLog.setTimerDate(rs.getString("TIMER_DATE"));

					petTimerLogDTOs.add(timerLog);
				}
			}, petParentId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetTimerLog", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("returning from getPetTimerLog");
		return petTimerLogDTOs;
	}

	@Override
	public boolean managePetTimerLog(TimerLog timerLog) throws ServiceExecutionException {
		LOGGER.debug("entered into managePetTimerLog");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", timerLog.getPetId());
			inputParams.put("p_pet_parent_id", timerLog.getPetParentId());
			inputParams.put("p_category", timerLog.getCategory());
			inputParams.put("P_device_number", timerLog.getDeviceNumber());
			inputParams.put("p_duration", timerLog.getDuration());
			inputParams.put("p_timer_date", timerLog.getTimerDate());
			inputParams.put("p_login_user_id", timerLog.getUserId());

			LOGGER.info("managePetTimerLog inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_TIMER_LOG, inputParams);
			LOGGER.info("managePetTimerLog outParams are {}", outParams);
			// System.out.println(outParams);
			int uniqueId = (int) outParams.get("last_insert_id");
			LOGGER.debug("returning from managePetTimerLog");
			return uniqueId > 0 ? true : false;
		} catch (SQLException e) {
			LOGGER.error("error while executing managePetTimerLog ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public boolean manageMobileAppScreensFeedback(MobileAppFeedbackDTO mobileAppFeedbackDTO)
			throws ServiceExecutionException {
		LOGGER.debug("entered into manageMobileAppScreensFeedback");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_id", mobileAppFeedbackDTO.getPetParentId());
			inputParams.put("p_pet_id", mobileAppFeedbackDTO.getPetId());
			inputParams.put("p_page_name", mobileAppFeedbackDTO.getPageName());
			inputParams.put("p_device_type", mobileAppFeedbackDTO.getDeviceType());
			inputParams.put("p_feedback_text", mobileAppFeedbackDTO.getFeedbackText());
			inputParams.put("p_login_user_id", mobileAppFeedbackDTO.getLoginUserId());
			if (mobileAppFeedbackDTO.getOpt() != null && mobileAppFeedbackDTO.getOpt().trim().length() > 0) {
				inputParams.put("p_opt", mobileAppFeedbackDTO.getOpt());
			} else {
				inputParams.put("p_opt", 0);
			}

			LOGGER.info("manageMobileAppScreensFeedback inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_MOBILE_APP_SCREENS_FEEDBACK,
					inputParams);
			LOGGER.info("manageMobileAppScreensFeedback outParams are {}", outParams);
			// System.out.println(outParams);
			int uniqueId = (int) outParams.get("last_insert_id");
			LOGGER.debug("returning from manageMobileAppScreensFeedback");
			return uniqueId > 0 ? true : false;
		} catch (SQLException e) {
			LOGGER.error("error while executing manageMobileAppScreensFeedback ", e);
			return Boolean.FALSE;
		}
	}

	public boolean updateSensorSetupStatus(SensorDetailsDTO sensorDetailsDTO) throws ServiceExecutionException {
		LOGGER.debug("entered into updateSensorSetupStatus");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", sensorDetailsDTO.getPetId());
			inputParams.put("p_pet_parent_id", sensorDetailsDTO.getPetParentId());
			inputParams.put("p_device_number", sensorDetailsDTO.getDeviceNumber());
			inputParams.put("p_setup_status", sensorDetailsDTO.getSetupStatus());
			inputParams.put("p_ssid_list", sensorDetailsDTO.getSsidList());
			inputParams.put("p_login_user_id", sensorDetailsDTO.getPetParentId());

			LOGGER.info("updateSensorSetupStatus inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_UPDATE_SENSOR_SETUP_STATUS, inputParams);
			LOGGER.info("updateSensorSetupStatus outParams are {}", outParams);
			// System.out.println(outParams);
			int uniqueId = (int) outParams.get("last_insert_id");
			LOGGER.debug("returning from updateSensorSetupStatus");
			return uniqueId > 0 ? true : false;
		} catch (SQLException e) {
			LOGGER.error("error while executing updateSensorSetupStatus ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public SensorDetailsDTO getSensorSetupStatus(String petParentId, String petId) throws ServiceExecutionException {
		SensorDetailsDTO sensorDetailsDTO = new SensorDetailsDTO();
		try {

			// in params
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_id", petId);
			inputParams.put("p_pet_parent_id", petParentId);

			LOGGER.info("getSensorSetupStatus inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_GET_SENSOR_SETUP_STATUS, inputParams);
			LOGGER.info("getSensorSetupStatus outParams are {}", outParams);

			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				if (entry.getValue() instanceof Integer) {
					LOGGER.debug("No records found");
				} else {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(sensorSetupStatus -> {
						sensorDetailsDTO.setSetupStatus((String) sensorSetupStatus.get("SETUP_STATUS"));
						sensorDetailsDTO.setSsidList((String) sensorSetupStatus.get("SSID_LIST"));
					});
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching Sensor Setup Status", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return sensorDetailsDTO;
	}

	@Override
	public boolean manageSensorChargingNotificationSettings(SensorDetailsDTO sensorDetailsDTO)
			throws ServiceExecutionException {
		LOGGER.debug("entered into manageSensorChargingNotificationSettings");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_id", sensorDetailsDTO.getPetParentId());
			inputParams.put("p_pet_id", sensorDetailsDTO.getPetId());
			inputParams.put("p_notification_type", sensorDetailsDTO.getNotificationType());
			inputParams.put("p_notification_date",
					sensorDetailsDTO.getNotificationDate() != null ? sensorDetailsDTO.getNotificationDate()
							: LocalDate.now());
			if (sensorDetailsDTO.getOpt() != null && sensorDetailsDTO.getOpt().trim().length() > 0) {
				inputParams.put("p_opt", sensorDetailsDTO.getOpt());
			} else {
				inputParams.put("p_opt", 0);
			}
			inputParams.put("p_login_user_id", sensorDetailsDTO.getPetParentId());

			LOGGER.info("manageSensorChargingNotificationSettings inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(
					MOBILE_APP_INSERT_UPDATE_SENSOR_CHARGING_NOTIFICATION_SETTINGS, inputParams);
			LOGGER.info("manageSensorChargingNotificationSettings outParams are {}", outParams);

			// System.out.println(outParams);
			int uniqueId = (int) outParams.get("last_insert_id");
			LOGGER.debug("returning from manageSensorChargingNotificationSettings");
			return uniqueId > 0 ? true : false;
		} catch (SQLException e) {
			LOGGER.error("error while executing manageSensorChargingNotificationSettings ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public DeviceAssignDTO getDeviceAssignInfoByDeviceNumber(String deviceNumber, String clientId)
			throws ServiceExecutionException {
		LOGGER.debug("entered into getDeviceAssignInfoByDeviceNumber");
		DeviceAssignDTO assignDTO = new DeviceAssignDTO();
		try {
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_device_number", deviceNumber);
			inputParams.put("p_pet_parent_id", clientId);

			LOGGER.info("getDeviceAssignInfoByDeviceNumber inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_GET_DEVICE_ASSIGNMENT_BY_DEVICE_NUMBER,
					inputParams);
			LOGGER.info("getDeviceAssignInfoByDeviceNumber outParams are {}", outParams);

			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				if (entry.getValue() instanceof Integer) {
					LOGGER.debug("No records found");
				} else {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(sensorSetupStatus -> {
						assignDTO.setPetID((Integer) sensorSetupStatus.get("PET_ID"));
						assignDTO.setPetName((String) sensorSetupStatus.get("PET_NAME"));

						assignDTO.setValidDevice(
								(Integer) sensorSetupStatus.get("IS_VALID_DEVICE") > NumberUtils.INTEGER_ZERO
										? Boolean.TRUE
										: Boolean.FALSE);

						assignDTO.setIsAssign(
								(Integer) sensorSetupStatus.get("IS_ASSIGN") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);
						assignDTO.setIsSameClient(
								(Integer) sensorSetupStatus.get("IS_SAME_PET_PARENT") > NumberUtils.INTEGER_ZERO
										? Boolean.TRUE
										: Boolean.FALSE);
					});
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching getDeviceAssignInfoByDeviceNumber", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("returning from getDeviceAssignInfoByDeviceNumber");

		return assignDTO;
	}

	@Override
	public int insertClientInfo(ClientInfo clientInfo) throws ServiceExecutionException {
		LOGGER.debug("entered into insertClientInfo");
		int uniqueId = 0;
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_name", clientInfo.getClientName());
			inputParams.put("p_unique_id", clientInfo.getUniqueId());
			inputParams.put("p_full_name", clientInfo.getFullName());
			inputParams.put("p_first_name", clientInfo.getFirstName());
			inputParams.put("p_last_name", clientInfo.getLastName());
			inputParams.put("p_email", clientInfo.getEmail());
			inputParams.put("p_phone_number", clientInfo.getPhoneNumber());
			inputParams.put("p_login_user_id", clientInfo.getUserId());
			inputParams.put("p_secondary_email", clientInfo.getSecondaryEmail());
			inputParams.put("p_notfi_secondary_email", clientInfo.getNotifyToSecondaryEmail());

			LOGGER.info("insertClientInfo inputParams are {}", inputParams);
			inputParams.put("p_password", clientInfo.getPassword());

			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_PARENT_INFO, inputParams);
			LOGGER.info("insertClientInfo outParams are {}", outParams);

			// System.out.println(outParams);
			uniqueId = (int) outParams.get("last_insert_id");

			LOGGER.debug("returning from insertClientInfo");
		} catch (SQLException e) {
			LOGGER.error("error while executing insertClientInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return uniqueId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String handleOnboardingInfo(OnboardingInfo onboardingInfo) throws ServiceExecutionException {
		LOGGER.debug("entered into handleOnboardingInfo");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_uid", onboardingInfo.getUID());
			inputParams.put("p_title", onboardingInfo.getTitle());
			inputParams.put("p_onboarding_data", onboardingInfo.getData());
			inputParams.put("p_user_id", onboardingInfo.getUserID());
			inputParams.put("p_study_id", onboardingInfo.getClinicID());
			inputParams.put("p_is_free", onboardingInfo.isIsFree());
			inputParams.put("p_login_user_id", onboardingInfo.getUserID());

			LOGGER.info("handleOnboardingInfo inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_HANDLE_ONBOARDING_INFO, inputParams);
			LOGGER.info("handleOnboardingInfo outParams are {}", outParams);
			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(instruction -> {
						if (instruction.containsKey("v_newUid")) {
							String uid = (String) instruction.get("v_newUid");
							onboardingInfo.setUID(uid);
						}
					});
				}
			}
			LOGGER.debug("returning from handleOnboardingInfo");
		} catch (SQLException e) {
			LOGGER.error("error while executing handleOnboardingInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return onboardingInfo.getUID();
	}

	@Override
	public OnboardingInfo getOnboardingInfoByUID(String uid) throws ServiceExecutionException {
		LOGGER.debug("entered into getOnboardingInfoByUID");
		OnboardingInfo onboardingInfo = new OnboardingInfo();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_ONBOARDING_INFO_BY_UID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					onboardingInfo.setUID(rs.getString("UID"));
					onboardingInfo.setTitle(rs.getString("TITLE"));
					onboardingInfo.setData(rs.getString("ONBOARDING_DATA"));
					onboardingInfo.setUserID(rs.getInt("USER_ID"));
					onboardingInfo.setClinicID(rs.getInt("STUDY_ID"));
					onboardingInfo.setIsArchived(rs.getBoolean("IS_ARCHIVED"));
					onboardingInfo.setIsFree(rs.getBoolean("IS_FREE"));
					onboardingInfo.setActive(rs.getBoolean("IS_ACTIVE"));
					onboardingInfo.setCreateDate(rs.getString("CREATED_DATE"));
					onboardingInfo.setUpdateDate(rs.getString("MODIFIED_DATE"));
				}
			}, uid);
		} catch (Exception e) {
			LOGGER.error("error while fetching getOnboardingInfoByUID", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("returning from getOnboardingInfoByUID");
		return onboardingInfo;
	}

	@Override
	public List<MonitoringPlan> getMonitoringPlanList(int clinicId) throws ServiceExecutionException {
		LOGGER.debug("entered into getMonitoringPlanList");
		List<MonitoringPlan> monitoringPlanList = new ArrayList<MonitoringPlan>();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_STUDY_PLANS_LIST_BY_STUDY_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					MonitoringPlan monitoringPlan = new MonitoringPlan();
					monitoringPlan.setPlanID(rs.getInt("PLAN_ID"));
					monitoringPlan.setPlanDesc(rs.getString("PLAN_NAME"));
					monitoringPlan.setTypeID(rs.getInt("STUDY_ID"));
					monitoringPlan.setSubscriptionID(rs.getString("STUDY_NAME"));
					monitoringPlanList.add(monitoringPlan);
				}
			}, clinicId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getMonitoringPlanList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("returning from getMonitoringPlanList");
		return monitoringPlanList;
	}

	@Override
	public PetInfoDTO getPetInfoByID(int petId) throws ServiceExecutionException {
		LOGGER.debug("entered into getPetInfoByID");
		PetInfoDTO petInfoDTO = new PetInfoDTO();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_PET_INFO_BY_PET_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Date sqlDate = null;
					java.util.Date utilDate = new java.util.Date();
					petInfoDTO.setBirthDay(rs.getString("BIRTHDAY"));
					petInfoDTO.setBreedId(rs.getInt("BREED_ID"));
					petInfoDTO.setBreedName(rs.getString("BREED_NAME"));
					petInfoDTO.setCheckedName(rs.getString("CHECKED_NAME"));

					sqlDate = rs.getDate("CHECKED_TIME");
					if (sqlDate != null) {
						utilDate = new java.util.Date();
						utilDate.setTime(sqlDate.getTime());
						petInfoDTO.setCheckedTime(utilDate);
					}

					sqlDate = rs.getDate("CREATED_DATE");
					if (sqlDate != null) {
						utilDate = new java.util.Date();
						utilDate.setTime(sqlDate.getTime());
						petInfoDTO.setCreatedDate(utilDate);
					}

					petInfoDTO.setGender(rs.getString("GENDER"));
					petInfoDTO.setIsActive(rs.getInt("IS_ACTIVE"));
					petInfoDTO.setIsDeceased(rs.getInt("IS_DECEASED"));
					petInfoDTO.setIsMixed(rs.getBoolean("IS_MIXED"));
					petInfoDTO.setNeutered(rs.getBoolean("IS_NEUTERED"));
					petInfoDTO.setUnknown(rs.getBoolean("IS_UNKNOWN"));
					petInfoDTO.setMixBreed(rs.getString("MIX_BREED"));

					sqlDate = rs.getDate("MODIFIED_DATE");
					if (sqlDate != null) {
						utilDate = new java.util.Date();
						utilDate.setTime(sqlDate.getTime());
						petInfoDTO.setModifiedDate(utilDate);
					}

					sqlDate = rs.getDate("NEXT_CHECKED_TIME");
					if (sqlDate != null) {
						utilDate = new java.util.Date();
						utilDate.setTime(sqlDate.getTime());
						petInfoDTO.setNextCheckedTime(utilDate);
					}

					petInfoDTO.setPetId(rs.getInt("PET_ID"));
					petInfoDTO.setPetName(rs.getString("PET_NAME"));
					petInfoDTO.setPetParentFullName(rs.getString("PET_PARENT_FULL_NAME"));
					petInfoDTO.setPetParentId(rs.getInt("PET_PARENT_ID"));
					petInfoDTO.setPhotoName(rs.getString("PHOTO_NAME"));

					sqlDate = rs.getDate("PLAN_START_DATE");
					if (sqlDate != null) {
						utilDate = new java.util.Date();
						utilDate.setTime(sqlDate.getTime());
						petInfoDTO.setPlanStartDate(utilDate);
					}

					sqlDate = rs.getDate("PLAN_END_DATE");
					if (sqlDate != null) {
						utilDate = new java.util.Date();
						utilDate.setTime(sqlDate.getTime());
						petInfoDTO.setPlanEndDate(utilDate);
					}

					petInfoDTO.setPlanId(rs.getInt("PLAN_ID"));
					petInfoDTO.setPlanStatus(rs.getInt("PLAN_STATUS"));
					petInfoDTO.setStudyId(rs.getInt("STUDY_ID"));
				}
			}, petId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetInfoByID", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("returning from getPetInfoByID");
		return petInfoDTO;
	}

	@Override
	public int updatePetInfo(PetInfoDTO petInfo) throws ServiceExecutionException {
		LOGGER.debug("entered into updatePetInfo");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", petInfo.getPetId());
			inputParams.put("p_pet_name", petInfo.getPetName());
			inputParams.put("p_gender", petInfo.getGender());
			inputParams.put("p_breed_id", petInfo.getBreedId());
			inputParams.put("p_is_mixed", petInfo.getIsMixed());
			inputParams.put("p_mixed_breed", petInfo.getMixBreed());
			inputParams.put("p_birthday", petInfo.getBirthDay());
			inputParams.put("p_is_unknown", petInfo.isUnknown());
			inputParams.put("p_weight", petInfo.getWeight());
			inputParams.put("p_bfi", petInfo.getPetBFI());
			inputParams.put("p_is_neutered", petInfo.isNeutered());
			inputParams.put("p_photo_name", petInfo.getPhotoName());
			inputParams.put("p_plan_id", petInfo.getPlanId());
			inputParams.put("p_ideal_bfi", petInfo.getIdealBFI());
			inputParams.put("p_ideal_weight", petInfo.getIdealWeight());
			inputParams.put("p_is_deceased", petInfo.getIsDeceased());
			inputParams.put("p_is_pet_with_pet_parent", petInfo.getIsPetWithPetParent());
			inputParams.put("p_pet_address_json", new ObjectMapper().writeValueAsString(petInfo.getPetAddress()));
			inputParams.put("p_pet_parent_id", petInfo.getPetParentId());
			inputParams.put("p_login_user_id", petInfo.getUserId());

			LOGGER.info("updatePetInfo inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_UPDATE_PET_INFO, inputParams);
			LOGGER.info("updatePetInfo outParams are {}", outParams);
			// System.out.println(outParams);
			int uniqueId = (int) outParams.get("last_insert_id");
			LOGGER.debug("returning from updatePetInfo");
			return uniqueId;
		} catch (Exception e) {
			LOGGER.error("error while executing updatePetInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}

	}

	@Override
	public int insertPetInfo(PetInfoDTO petInfo) throws ServiceExecutionException {
		LOGGER.debug("entered into insertPetInfo");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_id", petInfo.getPetParentId());
			inputParams.put("p_pet_name", petInfo.getPetName());
			inputParams.put("p_gender", petInfo.getGender());
			inputParams.put("p_breed_id", petInfo.getBreedId());
			inputParams.put("p_is_mixed", petInfo.getIsMixed());
			inputParams.put("p_mixed_breed", petInfo.getMixBreed());
			inputParams.put("p_birthday", petInfo.getBirthDay());
			inputParams.put("p_is_unknown", petInfo.isUnknown());
			inputParams.put("p_weight", petInfo.getWeight());
			inputParams.put("p_bfi", petInfo.getPetBFI());
			inputParams.put("p_is_neutered", petInfo.isNeutered());
			inputParams.put("p_weight_unit", petInfo.getWeightUnit());
			inputParams.put("p_algorithm_version", petInfo.getAlgorithmVersion());
			inputParams.put("p_is_pet_with_pet_parent", petInfo.getIsPetWithPetParent());
			inputParams.put("p_pet_address_json", new ObjectMapper().writeValueAsString(petInfo.getPetAddress()));
			inputParams.put("p_brand_id", petInfo.getBrandId());
			inputParams.put("p_food_intake", petInfo.getFoodIntake());
			inputParams.put("p_feed_unit", petInfo.getFeedUnit());
			inputParams.put("p_login_user_id", petInfo.getUserId());

			LOGGER.info("insertPetInfo inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_INFO, inputParams);
			LOGGER.info("insertPetInfo outParams are {}", outParams);
			// System.out.println(outParams);
			int uniqueId = (int) outParams.get("last_insert_id");
			LOGGER.debug("returning from insertPetInfo");
			return uniqueId;
		} catch (Exception e) {
			LOGGER.error("error while executing insertPetInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public int handlePetCheckedInfo(PetCheckedInfo petCheckedInfo) throws ServiceExecutionException {
		LOGGER.debug("entered into handlePetCheckedInfo");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", petCheckedInfo.getPetID());
			inputParams.put("p_checked_time", petCheckedInfo.getCheckedTime());
			inputParams.put("p_next_checked", petCheckedInfo.getNextCheckedTime());
			inputParams.put("p_checked_by", petCheckedInfo.getCheckedBy());
			inputParams.put("p_login_user_id", petCheckedInfo.getCheckedBy());

			LOGGER.info("handlePetCheckedInfo inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_CHECKED_INFO, inputParams);
			LOGGER.info("handlePetCheckedInfo outParams are {}", outParams);
			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();
				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(instruction -> {
						if (instruction.containsKey("LAST_INSERT_ID()")) {
							petCheckedInfo
									.setPetCheckInfoID(((BigInteger) instruction.get("LAST_INSERT_ID()")).intValue());
						}
					});
				}
			}
			LOGGER.debug("returning from handlePetCheckedInfo");
		} catch (SQLException e) {
			LOGGER.error("error while executing handlePetCheckedInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petCheckedInfo.getPetCheckInfoID();
	}

	@Override
	public DeviceInfo getDeviceInfoByDeviceNumber(String deviceNumber) throws ServiceExecutionException {
		DeviceInfo deviceInfo = new DeviceInfo();
		LOGGER.debug("getDeviceInfoByDeviceNumber called");
		try {
			jdbcTemplate.query(MOBLIE_APP_GET_DEVICE_INFO_BY_DEVICE_NUMBER, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					deviceInfo.setDeviceID(rs.getInt("DEVICE_ID"));
					deviceInfo.setDeviceNumber(rs.getString("DEVICE_NUMBER"));
					deviceInfo.setDeviceType(rs.getString("DEVICE_TYPE"));
					deviceInfo.setAddDate(rs.getString("ADD_DATE"));
					deviceInfo.setActive(rs.getBoolean("IS_ACTIVE"));
				}
			}, deviceNumber);
		} catch (Exception e) {
			LOGGER.error("error while fetching getDeviceInfoByDeviceNumber", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceInfo;

	}

	@Override
	public int insertDeviceInfo(DeviceInfo deviceInfo) throws ServiceExecutionException {
		LOGGER.debug("entered into insertDeviceInfo");
		int uniqueId = 0;
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_device_number", deviceInfo.getDeviceNumber());
			inputParams.put("p_device_type", deviceInfo.getDeviceType());
			inputParams.put("p_add_date", deviceInfo.getAddDate());
			inputParams.put("p_login_user_id", deviceInfo.getUserId());

			LOGGER.info("insertDeviceInfo inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_DEVICE_INFO, inputParams);
			LOGGER.info("insertDeviceInfo outParams are {}", outParams);
			// System.out.println(outParams);
			uniqueId = (int) outParams.get("last_insert_id");
			LOGGER.debug("returning from insertDeviceInfo");
		} catch (SQLException e) {
			LOGGER.error("error while executing insertDeviceInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return uniqueId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insertDeviceAssign(DeviceAssignDTO deviceAssign) throws ServiceExecutionException {
		LOGGER.debug("entered into insertDeviceAssign");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_study_id", deviceAssign.getPlanID());
			inputParams.put("p_device_id", deviceAssign.getDeviceID());
			inputParams.put("p_note", deviceAssign.getNote());
			inputParams.put("p_login_user_id", deviceAssign.getUserId());

			LOGGER.info("insertDeviceAssign inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_PET_STUDY_DEVICE, inputParams);
			LOGGER.info("insertDeviceAssign outParams are {}", outParams);
			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();
				if (key.equals(RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(instruction -> {
						if (instruction.containsKey("LAST_INSERT_ID()")) {
							deviceAssign.setID(((BigInteger) instruction.get("LAST_INSERT_ID()")).intValue());
						}
					});
				}
			}
			LOGGER.debug("returning from insertDeviceAssign");
		} catch (SQLException e) {
			LOGGER.error("error while executing insertDeviceAssign ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceAssign.getID();
	}

	@Override
	public void updateOnboardingArchived(String UID, int userId) throws ServiceExecutionException {
		LOGGER.debug("entered into updateOnboardingArchived");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_uid", UID);
			inputParams.put("p_login_user_id", userId);
			LOGGER.info("updateOnboardingArchived inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_UPDATE_ONBOARDING_ARCHIVED, inputParams);
			LOGGER.info("updateOnboardingArchived outParams are {}", outParams);
		} catch (SQLException e) {
			LOGGER.error("error while executing updateOnboardingArchived ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void updateOnboardingStatus(String UID, String sbStatus, int userId) throws ServiceExecutionException {
		LOGGER.debug("entered into updateOnboardingStatus");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_uid", UID);
			inputParams.put("p_onboarding_status", sbStatus);
			inputParams.put("p_login_user_id", userId);
			LOGGER.info("updateOnboardingStatus inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_UPDATE_ONBOARDING_STATUS, inputParams);
			LOGGER.info("updateOnboardingStatus outParams are {}", outParams);
		} catch (SQLException e) {
			LOGGER.error("error while executing updateSensorSetupStatus ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public int insertMonitoringPlan(MonitoringPlan monitoringPlan) throws ServiceExecutionException {
		LOGGER.debug("entered into insertMonitoringPlan");
		int uniqueId = 0;
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_plan_id", monitoringPlan.getTypeID());
			inputParams.put("p_pet_id", monitoringPlan.getPetID());
			inputParams.put("p_pet_parent_id", monitoringPlan.getClientID());
			inputParams.put("p_study_id", monitoringPlan.getClinicID());
			if (monitoringPlan.getStartDate() != null) {
				inputParams.put("p_start_date", monitoringPlan.getStartDate());
			} else {
				inputParams.put("p_start_date", null);
			}

			if (monitoringPlan.getEndDate() != null) {
				inputParams.put("p_end_date", monitoringPlan.getEndDate());
			} else {
				inputParams.put("p_end_date", null);
			}
			inputParams.put("p_user_id", monitoringPlan.getUserID());
			inputParams.put("p_login_user_id", monitoringPlan.getUserID());

			LOGGER.info("insertMonitoringPlan inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_INSERT_MONITORING_PLAN, inputParams);
			LOGGER.info("insertMonitoringPlan outParams are {}", outParams);
			// System.out.println(outParams);
			uniqueId = (int) outParams.get("last_insert_id");
			LOGGER.debug("returning from insertMonitoringPlan");
		} catch (SQLException e) {
			LOGGER.error("error while executing insertMonitoringPlan ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return uniqueId;
	}

	@Override
	public void logoutUser(String petParentId, String token) throws ServiceExecutionException {
		LOGGER.debug("entered into logoutUser");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_key", token);
			inputParams.put("p_pet_parent_id", petParentId);
			LOGGER.info("logoutUser inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_PET_PARENT_LOG_OUT, inputParams);
			LOGGER.info("logoutUser outParams are {}", outParams);
		} catch (SQLException e) {
			LOGGER.error("error while executing logoutUser ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void deleteAccount(String petParentId) throws ServiceExecutionException {
		LOGGER.debug("entered into deleteAccount");
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_id", petParentId);
			LOGGER.info("inputParams are {}", inputParams);
			callStoredProcedure(MOBILE_APP_USER_DELETE_ACCOUNT, inputParams);
		} catch (SQLException e) {
			LOGGER.error("error while executing deleteAccount ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	public Boolean validateSecondaryEmail(String secondaryEmail) throws ServiceExecutionException {
		LOGGER.info("entered into validateSecondaryEmail");
		Boolean flag = Boolean.FALSE;
		ClientInfo clientInfo = new ClientInfo();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_CLIENT_INFO_BY_SECONDARY_EMAIL, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					clientInfo.setClientId(rs.getInt("PET_PARENT_ID"));
				}
			}, secondaryEmail);
			if (clientInfo.getUserId() != null && clientInfo.getUserId() > 0) {
				flag = Boolean.TRUE;
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching validateSecondaryEmail", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.info("returning from validateSecondaryEmail");
		return flag;
	}

	public Boolean validateSecondaryEmailByPetParentId(String secondaryEmail, int petParentId)
			throws ServiceExecutionException {
		LOGGER.info("entered into validateSecondaryEmailByPetParentId");
		Boolean flag = Boolean.TRUE;
		ClientInfo clientInfo = new ClientInfo();
		try {
			jdbcTemplate.query(MOBILE_APP_VALIDATE_EMAIL_BY_SECONDARY_EMAIL_CLIENT_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					clientInfo.setClientId(rs.getInt("PET_PARENT_ID"));
				}
			}, secondaryEmail, petParentId);
			if (clientInfo.getClientId() > 0) {
				flag = Boolean.FALSE;
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching validateSecondaryEmailByPetParentId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.info("returning from validateSecondaryEmailByPetParentId");
		return flag;
	}

	public Boolean validateSecondaryEmailByUserId(String secondaryEmail, int userId) throws ServiceExecutionException {
		LOGGER.info("entered into validateSecondaryEmailByUserId");
		Boolean flag = Boolean.TRUE;
		try {
			int id = selectForObject(MOBILE_APP_VALIDATE_EMAIL_BY_SECONDARY_EMAIL_USER_ID, Integer.class,
					secondaryEmail, userId);
			if (id > 0) {
				flag = Boolean.FALSE;
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching validateSecondaryEmailByUserId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.info("returning from validateSecondaryEmailByUserId");
		return flag;
	}

	@Override
	public int registerUser(ClientInfo clientInfo, String verificationCode) throws ServiceExecutionException {
		LOGGER.debug("entered into registerUser");
		int uniqueId = 0;
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_id", clientInfo.getClientId());
			inputParams.put("p_email", clientInfo.getEmail());
			inputParams.put("p_login_user_id", clientInfo.getUserId());
			inputParams.put("p_secondary_email", clientInfo.getSecondaryEmail());
			inputParams.put("p_notfi_secondary_email", clientInfo.getNotifyToSecondaryEmail());
			inputParams.put("p_residential_address_json",
					new ObjectMapper().writeValueAsString(clientInfo.getPetParentAddress()));

			LOGGER.info("registerUser inputParams are {}", inputParams);
			inputParams.put("p_password", clientInfo.getPassword());

			Map<String, Object> outParams = callStoredProcedure(MOBILE_APP_REGISTER_USER, inputParams);
			LOGGER.info("registerUser outParams are {}", outParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");

			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				// getting the inserted flag value
				Integer petParentId = (int) outParams.get("last_insert_id");

				ClientSMSCode clientSMSCode = getClientSMSCodeByClientIDAndVerificationCode(petParentId,
						verificationCode);
				if (clientSMSCode.getPetParentSMSCodeId() > 0) {
					clientSMSCode.setExpired(true);
					updateClientSMSCode(clientSMSCode);
				}

				LOGGER.info("Mobile user (Pet Parent) has been registred successfully, Pet Parent id is {}",
						petParentId);
				uniqueId = (int) outParams.get("last_insert_id");
			} else {
				if (statusFlag == -2 || statusFlag == -3) {
					uniqueId = statusFlag;
					LOGGER.info("Email is already exits : status flag {}", statusFlag);
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
			LOGGER.debug("returning from registerUser");
		} catch (Exception e) {
			LOGGER.error("error while executing registerUser ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return uniqueId;
	}

	@Override
	public List<Menu> getUserPermissions(Integer roleId, Integer userId) throws ServiceExecutionException {
		List<Menu> rolePermissions = new ArrayList<>();
		LOGGER.debug("entered into getUserPermissions");
		try {
			jdbcTemplate.query(MOBILE_APP_GET_USER_PERMISSIONS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Menu menu = new Menu();
					menu.setMenuId(rs.getInt("MENU_ID"));
					menu.setMenuName(rs.getString("MENU_NAME"));
					menu.setParentMenuId(rs.getInt("PARENT_MENU_ID"));
					menu.setParentMenuName(rs.getString("PARENT_MENU_NAME"));
					menu.setMenuActionId(rs.getInt("MENU_ACTION_ID"));
					menu.setMenuActionName(rs.getString("MENU_ACTION_NAME"));
					menu.setMobileAppConfigId(rs.getInt("MOBILE_APP_CONFIG_ID"));
					rolePermissions.add(menu);
				}
			}, userId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getUserPermissions", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return rolePermissions;

	}

	@Override
	public void decryptPetParentPassword() throws ServiceExecutionException {
		JSONArray array = new JSONArray();
		LOGGER.debug("entered into decryptPetParentPassword");
		try {
			jdbcTemplate.query(MIGRATE_GET_PET_PARENT_PASSWORD_TO_DECRYPT, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Integer petParentId = rs.getInt("PET_PARENT_ID");
					String uniqueId = rs.getString("UNIQUE_ID");
					String password = rs.getString("PET_PARENT_PASSWORD");
					String plainPassword = "";
					try {
						plainPassword = Cryptography.decrypt(password, uniqueId);
						if (plainPassword.isEmpty()) {
						} else {
							// This is to resolve circular dependency injection
							PasswordEncoder passwordEncoder = BeanUtil.getBean(PasswordEncoder.class);
							String passwordHash = passwordEncoder.encode(plainPassword);
							updatePetParentPasswordHash(petParentId, passwordHash);
							JSONObject object = new JSONObject();
							object.put("petParentId", petParentId);
							object.put("plainPassword", plainPassword);
							array.put(object);
						}
					} catch (Exception e) {
						LOGGER.error("error while fetching decryptPetParentPassword", e);
					}

				}
			});
			System.out.println("+++++++++++++++++++" + array.length());
		} catch (Exception e) {
			LOGGER.error("error while fetching decryptPetParentPassword", e);
		}
	}

	public void updatePetParentPasswordHash(Integer petParentId, String passwordHash) throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_id", petParentId);
			inputParams.put("p_password", passwordHash);
			// LOGGER.info("===inputParams== {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(
					"MIGRATE_GET_PET_PARENT_PASSWORD_INSERT_DECRYPTED_PASSWORD", inputParams);
			LOGGER.info("===outParams== {}", outParams);
		} catch (Exception e) {
			LOGGER.error("error while executing registerUser ", e);
			// throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public String getPasswordByUserId(int userId) throws ServiceExecutionException {
		LOGGER.debug("entered into getPasswordByUserId");
		ClientInfo clientInfo = new ClientInfo();
		try {
			jdbcTemplate.query(MOBILE_APP_GET_USER_PASSWORD_BY_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					clientInfo.setPassword(rs.getString(1));
				}
			}, userId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPasswordByUserId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("returning from getPasswordByUserId");
		return clientInfo.getPassword();
	}
}
