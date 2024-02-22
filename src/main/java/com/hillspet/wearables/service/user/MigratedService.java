package com.hillspet.wearables.service.user;

import java.util.List;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
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

public interface MigratedService {

	public ClientInfo getClientInfoByEmail(String email) throws ServiceExecutionException;

	public UserInfo userLogin(String email, String password) throws ServiceExecutionException;

	public ClientInfo clientLogin(String email, String password) throws ServiceExecutionException;

	public String insertClientKey(PetParentKeyInfoDTO parentKeyInfoDTO) throws ServiceExecutionException;

	public boolean updateClientInfo(ClientInfo clientInfo) throws ServiceExecutionException;

	public boolean updateUserInfo(User user) throws ServiceExecutionException;

	public ClientSMSCode getClientSMSCodeByUserId(int userId) throws ServiceExecutionException;

	public int updateClientSMSCode(ClientSMSCode clientSMSCode) throws ServiceExecutionException;

	public int insertClientSMSCode(ClientSMSCode clientSMSCode) throws ServiceExecutionException;

	public ClientSMSCode getClientSMSCodeByClientIDAndVerificationCode(int clientId, String verificationCode)
			throws ServiceExecutionException;

	public int updatePassword(ClientInfo clientInfoResult) throws ServiceExecutionException;

	public int expiredClientSMSCode(int userId, String verificationCode) throws ServiceExecutionException;

	public List<PetInfoDTO> getPetListByPetParentId(int clientId) throws ServiceExecutionException;

	public List<DeviceAssignDTO> getDeviceAssignListByPlanIDAndPetID(int planId, int petId)
			throws ServiceExecutionException;

	public ClientInfo getClientInfoById(int clientId) throws ServiceExecutionException;

	public String getPasswordByClientID(int clientId) throws ServiceExecutionException;

	public List<TimerLog> getPetTimerLog(int parseInt) throws ServiceExecutionException;

	public boolean managePetTimerLog(TimerLog timerLog) throws ServiceExecutionException;

	public boolean manageMobileAppScreensFeedback(MobileAppFeedbackDTO mobileAppFeedbackDTO)
			throws ServiceExecutionException;

	public boolean updateSensorSetupStatus(SensorDetailsDTO sensorDetailsDTO) throws ServiceExecutionException;

	public SensorDetailsDTO getSensorSetupStatus(String petParentId, String petId) throws ServiceExecutionException;

	public boolean manageSensorChargingNotificationSettings(SensorDetailsDTO sensorDetailsDTO)
			throws ServiceExecutionException;

	public DeviceAssignDTO getDeviceAssignInfoByDeviceNumber(String sensorNumber, String clientId)
			throws ServiceExecutionException;

	public int insertClientInfo(ClientInfo newClientInfo) throws ServiceExecutionException;

	public String handleOnboardingInfo(OnboardingInfo onboardingInfo) throws ServiceExecutionException;

	public OnboardingInfo getOnboardingInfoByUID(String onboardingInfoGuid) throws ServiceExecutionException;

	public List<MonitoringPlan> getMonitoringPlanList(int clinicID) throws ServiceExecutionException;

	public PetInfoDTO getPetInfoByID(int petID) throws ServiceExecutionException;

	public int updatePetInfo(PetInfoDTO petInfo) throws ServiceExecutionException;

	public int insertPetInfo(PetInfoDTO petInfo) throws ServiceExecutionException;

	public int insertMonitoringPlan(MonitoringPlan monitoringPlan) throws ServiceExecutionException;

	public int handlePetCheckedInfo(PetCheckedInfo petCheckedInfo) throws ServiceExecutionException;

	public DeviceInfo getDeviceInfoByDeviceNumber(String deviceNumber) throws ServiceExecutionException;

	public int insertDeviceInfo(DeviceInfo deviceInfo) throws ServiceExecutionException;

	public int insertDeviceAssign(DeviceAssignDTO deviceAssign) throws ServiceExecutionException;

	public void updateOnboardingArchived(String uID, int userID) throws ServiceExecutionException;

	public void updateOnboardingStatus(String uID, String string, int userID) throws ServiceExecutionException;

	public void logoutUser(String petParentId, String token) throws ServiceExecutionException;

	public void deleteAccount(String petParentId) throws ServiceExecutionException;

	public Boolean validateSecondaryEmail(String secondaryEmail) throws ServiceExecutionException;

	public Boolean validateSecondaryEmailByPetParentId(String secondaryEmail, int petParentId)
			throws ServiceExecutionException;

	public Boolean validateSecondaryEmailByUserId(String secondaryEmail, int userId) throws ServiceExecutionException;

	// public int updatePasswordAndSecondaryEmail(ClientInfo clientInfoResult)
	// throws ServiceExecutionException;

	public int registerUser(ClientInfo client, String verificationCode) throws ServiceExecutionException;

	public List<Menu> getUserPermissions(Integer roleId, Integer userId) throws ServiceExecutionException;

	public void migrateDecryptPassword() throws ServiceExecutionException;

	public String getPasswordByUserId(int userId) throws ServiceExecutionException;

}
