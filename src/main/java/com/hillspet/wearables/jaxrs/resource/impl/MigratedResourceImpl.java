package com.hillspet.wearables.jaxrs.resource.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.constants.WearablesErrorCode;
import com.hillspet.wearables.common.dto.WearablesError;
import com.hillspet.wearables.common.exceptions.ServiceValidationException;
import com.hillspet.wearables.common.utils.Cryptography;
import com.hillspet.wearables.concurrent.EmailSenderThread;
import com.hillspet.wearables.concurrent.EmailThreadPoolExecutor;
import com.hillspet.wearables.dto.Address;
import com.hillspet.wearables.dto.ClientInfo;
import com.hillspet.wearables.dto.ClientSMSCode;
import com.hillspet.wearables.dto.DeviceAssignDTO;
import com.hillspet.wearables.dto.DeviceInfo;
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
import com.hillspet.wearables.email.templates.EmailTemplate;
import com.hillspet.wearables.format.validation.FormatValidationService;
import com.hillspet.wearables.jaxrs.resource.MigratedResource;
import com.hillspet.wearables.request.AppFeedBackReqeust;
import com.hillspet.wearables.request.BillingInfo;
import com.hillspet.wearables.request.ChangePasswordRequest;
import com.hillspet.wearables.request.ClientInfoRequest;
import com.hillspet.wearables.request.EmailRequest;
import com.hillspet.wearables.request.EmailVerificationCodeReqeust;
import com.hillspet.wearables.request.GetClientInfoRequest;
import com.hillspet.wearables.request.LoginRequest;
import com.hillspet.wearables.request.LogoutRequest;
import com.hillspet.wearables.request.PetInfo;
import com.hillspet.wearables.request.PetOnboardingRequest;
import com.hillspet.wearables.request.PetTimerLogRequest;
import com.hillspet.wearables.request.PetUpdateRequest;
import com.hillspet.wearables.request.PlanInfo;
import com.hillspet.wearables.request.RegisterUserRequest;
import com.hillspet.wearables.request.ResetPasswordReqeust;
import com.hillspet.wearables.request.SensorNotificationSettingsRequest;
import com.hillspet.wearables.request.SensorSetupStatusRequest;
import com.hillspet.wearables.request.SensorStatusRequest;
import com.hillspet.wearables.request.ValidateDeviceRequest;
import com.hillspet.wearables.request.ValidateUserEmailReqeust;
import com.hillspet.wearables.service.questionnaire.MobileAppService;
import com.hillspet.wearables.service.user.MigratedService;

@Service
public class MigratedResourceImpl implements MigratedResource {

	private static final Logger LOGGER = LogManager.getLogger(MigratedResourceImpl.class);

	@Autowired
	private MigratedService migratedService;

	@Autowired
	private MobileAppService mobileAppService;

	@Autowired
	FormatValidationService formatValidationService;

	@Autowired
	PasswordEncoder passwordEncoder;

	private static final String dateFormat = "yyyy-MM-dd hh:mm:ss";

	@Override
	public Response clientLogin(String payload) {
		LOGGER.debug("entered into clientLogin");
		JSONObject jsonObject = null;
		JSONObject resultJson = null;
		try {
			jsonObject = new JSONObject(payload);
			resultJson = new JSONObject();
			String email = jsonObject.getString("Email");
			String password = jsonObject.getString("Password");
			String fcmToken = jsonObject.getString("FCMToken");

			if (email == null || email.trim().length() == 0) {
				throw new ServiceValidationException("Email is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.EMAIL_REQUIRED)));
			}

			if (password == null || password.trim().length() == 0) {
				throw new ServiceValidationException("Password is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.PASSWORD_REQUIRED)));
			}

			ClientInfo clientInfo = migratedService.getClientInfoByEmail(email);
			if (clientInfo.getUserId() > 0) {
				password = Cryptography.encrypt(Cryptography.petParentDecrypt(password), clientInfo.getUniqueId());
			}
			ClientInfo clientInfo1 = migratedService.clientLogin(email, password);
			clientInfo.setPassword(password);
			System.out.println("clientInfo.getClientId() " + clientInfo.getClientId());
			JSONObject object = null;

			if (clientInfo1 != null && clientInfo1.getUserId() > 0) {
				object = new JSONObject();
				String key = UUID.randomUUID().toString();
				Calendar startTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				String addDate = startTime.toString();
				PetParentKeyInfoDTO parentKeyInfoDTO = new PetParentKeyInfoDTO();
				parentKeyInfoDTO.setAddDate(addDate);
				parentKeyInfoDTO.setIsExpired(0);
				parentKeyInfoDTO.setKey(key);
				parentKeyInfoDTO.setUserId(clientInfo.getUserId());
				parentKeyInfoDTO.setPetParentId(clientInfo.getClientId());
				parentKeyInfoDTO.setFcmToken(fcmToken);
				parentKeyInfoDTO.setAppVersion("");
				parentKeyInfoDTO.setAppOS("");
				String authToken = migratedService.insertClientKey(parentKeyInfoDTO);

				clientInfo.setLastLogin(addDate);
				if (StringUtils.isNoneEmpty(fcmToken)) {
					clientInfo.setFcmToken(fcmToken);
				}
				migratedService.updateClientInfo(clientInfo);
				object.put("clientID", clientInfo.getClientId());
				object.put("email", clientInfo.getEmail());
				object.put("token", authToken);

				resultJson.put("responseCode", 0);
				resultJson.put("responseMessage", "SUCCESS");
				resultJson.put("success", true);
				resultJson.put("result", object);
			} else {
				resultJson.put("responseCode", 1);
				resultJson.put("responseMessage", "ERROR");
				resultJson.put("success", false);
				resultJson.put("result", "null");
			}

			resultJson.put("errors", "null");
			resultJson.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(resultJson.toString()).build();
	}

	@Override
	public Response clientLogin_v2(LoginRequest loginRequest) {
		LOGGER.debug("entered into clientLogin");
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();
		JSONObject userDtls = new JSONObject();
		try {
			String email = loginRequest.getEmail();
			String password = loginRequest.getPassword();
			String fcmToken = loginRequest.getFcmToken();
			String appVersion = "";
			String appOS = "";
			try {
				appVersion = loginRequest.getAppVersion();
				appOS = loginRequest.getAppOS();
			} catch (JSONException e) {
				LOGGER.debug("appVersion and appOS not found, user trying to login from older version of app");
			}

			if (email == null || email.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Email");
			} else if (password == null || password.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Password");
			} else {
				ClientInfo clientInfo = migratedService.getClientInfoByEmail(email);

				if (clientInfo.getUserId() != null && clientInfo.getUserId() > 0 && clientInfo.isActive()) {
					password = Cryptography.petParentDecrypt(password);
					UserInfo userInfo = migratedService.userLogin(email, password);

					if (userInfo != null && userInfo.getUserId() > 0) {
						String key = UUID.randomUUID().toString();
						Calendar startTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
						String addDate = startTime.toString();
						PetParentKeyInfoDTO parentKeyInfoDTO = new PetParentKeyInfoDTO();
						parentKeyInfoDTO.setAddDate(addDate);
						parentKeyInfoDTO.setIsExpired(0);
						parentKeyInfoDTO.setKey(key);
						parentKeyInfoDTO.setUserId(clientInfo.getUserId());
						parentKeyInfoDTO.setPetParentId(clientInfo.getClientId());
						parentKeyInfoDTO.setFcmToken(fcmToken);
						parentKeyInfoDTO.setAppVersion(appVersion);
						parentKeyInfoDTO.setAppOS(appOS);
						String authToken = migratedService.insertClientKey(parentKeyInfoDTO);

						if (StringUtils.isNoneEmpty(fcmToken)) {
							clientInfo.setFcmToken(fcmToken);
							clientInfo.setAppVersion(appVersion);
							clientInfo.setAppOS(appOS);
						}

						migratedService.updateClientInfo(clientInfo);
						result.put("Key", Boolean.TRUE);

						userDtls.put("ClientID", clientInfo.getClientId());
						userDtls.put("UserId", clientInfo.getUserId());
						userDtls.put("RoleId", clientInfo.getRoleId());
						userDtls.put("RoleName", clientInfo.getRoleName());
						userDtls.put("Email", clientInfo.getEmail());
						userDtls.put("RolePermissions",
								migratedService.getUserPermissions(clientInfo.getRoleId(), clientInfo.getUserId()));
						userDtls.put("Token", authToken);

						result.put("UserDetails", userDtls);

						response.put("responseCode", 1);
						response.put("responseMessage", "SUCCESS");
						response.put("success", true);
						response.put("result", result);
					} else {
						result.put("Key", Boolean.FALSE);
						result.put("UserDetails", userDtls);

						response.put("responseCode", 0);
						response.put("responseMessage", "ERROR");
						response.put("success", Boolean.FALSE);
						response.put("result", "null");
					}
				} else {
					result.put("Key", Boolean.FALSE);
					result.put("UserDetails", userDtls);

					response.put("responseCode", 0);
					response.put("responseMessage", "ERROR");
					response.put("success", Boolean.FALSE);
					response.put("result", "null");
				}
				response.put("errors", "null");
				response.put("warnings", "null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response forgotPasswordValidateEmail(EmailRequest validateEmailRequest) {
		LOGGER.debug("entered into forgotPasswordValidateEmail");
		String email = validateEmailRequest.getEmail();
		JSONObject response = sendEmailVerificationCode(email, 0);
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response forgotPasswordValidateEmailVerificationCode(EmailVerificationCodeReqeust reqeust) {
		LOGGER.debug("entered into forgotPasswordValidateEmailVerificationCode");
		String email = reqeust.getEmail();
		String verificationCode = reqeust.getVerificationCode();
		JSONObject response = validateEmailVerificationCode(email, verificationCode);
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response resetForgotPassword(ResetPasswordReqeust request) {
		LOGGER.debug("entered into resetForgotPassword");
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();

		ClientInfo client = migratedService.getClientInfoByEmail(request.getEmail());
		if (client.getUserId() != null && client.getUserId() > 0) {
			encryptPetParentPassword(request, result, client);
			if (!result.has("ResponseCode")) {
				int status = migratedService.updatePassword(client);
				boolean flag = Boolean.FALSE;
				if (status > 0) {
					migratedService.expiredClientSMSCode(client.getUserId(), request.getVerificationCode());
					flag = true;
				}
				result.put("Key", flag).put("Value", "");
			}
		}
		response.put("errors", "null");
		response.put("responseCode", 0);
		response.put("responseMessage", "SUCCESS");
		response.put("result", result);
		response.put("success", true);
		response.put("warnings", "null");
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response registerUserValidateEmail(ValidateUserEmailReqeust request) {
		LOGGER.debug("entered into registerUserValidateEmail");
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();
		ClientInfo client = null;
		Address petParentAddress = new Address();
		try {
			Boolean isValidEmail = Boolean.FALSE;

			String email = request.getEmail().trim();
			String firstName = request.getFirstName().trim();
			String lastName = request.getLastName().trim();
			String phoneNumber = request.getPhoneNumber();
			String secondaryEmail = request.getSecondaryEmail();

			if (email == null || email.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide email");
			} else if (firstName == null || firstName.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide First Name");
			} else if (lastName == null || lastName.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Last Name");
			} else if (phoneNumber == null || phoneNumber.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Phone Number");
			} else {
				client = migratedService.getClientInfoByEmail(email);
				client.setFirstName(firstName);
				client.setLastName(lastName);
				client.setEmail(email);
				client.setFullName(prepareFullName(firstName, lastName));
				client.setPhoneNumber(preparePhoneNumber(phoneNumber));
				client.setSecondaryEmail(secondaryEmail);
				insertOrUpdatePetParent(result, client, petParentAddress, isValidEmail);
			}
			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", result);
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	private boolean insertOrUpdatePetParent(JSONObject result, ClientInfo client, Address petParentAddress,
			Boolean isValidEmail) {
		String secondaryEmail = client.getSecondaryEmail();
		if (client != null && client.getUserId() != null && client.getUserId() > 0) {
			String password = migratedService.getPasswordByClientID(client.getClientId());
			if (StringUtils.isEmpty(password)) {
				if (secondaryEmail != null && !secondaryEmail.trim().isEmpty()) {
					if (migratedService.validateSecondaryEmailByPetParentId(secondaryEmail.trim(),
							client.getClientId())) {
						result.put("Key", Boolean.FALSE).put("Value", "Secondary email already exists");
					} else {
						isValidEmail = Boolean.TRUE;
					}
				} else {
					isValidEmail = Boolean.TRUE;
				}

				if (isValidEmail) {
					migratedService.updateClientInfo(client);
					petParentAddress = mobileAppService.getPetParentAddressByPetParent(client.getClientId());
					result.put("Key", isValidEmail).put("PetParentAddress",
							new JSONObject(new Gson().toJson(petParentAddress)));
				}
			} else {
				result.put("Key", Boolean.FALSE).put("Value", "Primary email already exists");
			}
		} else {
			if (secondaryEmail != null && !secondaryEmail.trim().isEmpty()) {
				if (migratedService.validateSecondaryEmail(secondaryEmail)) {
					result.put("Key", Boolean.FALSE).put("Value", "Secondary email already exists");
				} else {
					isValidEmail = Boolean.TRUE;
				}
			} else {
				isValidEmail = Boolean.TRUE;
			}
			client.setUserId(8989);

			if (isValidEmail) {
				int petParentId = migratedService.insertClientInfo(client);
				client.setClientId(petParentId);
				result.put("Key", isValidEmail).put("PetParentAddress",
						new JSONObject(new Gson().toJson(petParentAddress)));
			}
		}
		return isValidEmail;
	}

	@Override
	public Response registerUserSendEmailVerificationCode(EmailRequest request) {
		LOGGER.debug("entered into registerUserSendEmailVerificationCode");
		String email = request.getEmail();
		JSONObject response = sendEmailVerificationCode(email, 1);
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response registerUserValidateEmailVerificationCode(EmailVerificationCodeReqeust request) {
		LOGGER.debug("entered into registerUserValidateEmailVerificationCode");
		String email = request.getEmail();
		String verificationCode = request.getVerificationCode();
		JSONObject response = validateEmailVerificationCode(email, verificationCode);
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response registerUser(RegisterUserRequest request) {
		LOGGER.debug("entered into registerUser");
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();
		ClientInfo client = new ClientInfo();
		Address petParentAddress = request.getPetParentAddress();
		if (petParentAddress != null) {
			client = migratedService.getClientInfoByEmail(request.getEmail().trim());
			if (client != null && client.getClientId() > 0) {
				encryptPetParentPassword(request, result, client);
				client.setSecondaryEmail(request.getSecondaryEmail());
				client.setNotifyToSecondaryEmail(request.getNotifyToSecondaryEmail());
				client.setPetParentAddress(petParentAddress);
				int status = migratedService.registerUser(client, request.getVerificationCode());
				boolean flag = Boolean.FALSE;
				if (status > 0) {
					flag = true;
				} else {
					if (status == -2) {
						result.put("Key", Boolean.FALSE).put("Value", "Primary email already exists");
					}

					if (status == -3) {
						result.put("Key", Boolean.FALSE).put("Value", "Secondary email already exists");
					}
				}
				result.put("Key", flag).put("Value", "");
			} else {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide email");
			}
		} else {
			result.put("Key", Boolean.FALSE).put("Value", "Please provide address");
		}

		response.put("errors", "null");
		response.put("responseCode", 0);
		response.put("responseMessage", "SUCCESS");
		response.put("result", result);
		response.put("success", true);
		response.put("warnings", "null");
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response changePassword_v2(ChangePasswordRequest request, String token) {
		LOGGER.debug("entered into changePassword_v2");
		JSONObject result = new JSONObject();
		JSONObject response = new JSONObject();
		try {

			int userId = mobileAppService.getPetParentByPetParentKey(token);
			if (userId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			String newPassword = request.getNewPassword();
			String oldPassword = request.getPassword();

			if (newPassword == null || newPassword.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide New Password");
			} else if (oldPassword == null || oldPassword.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Old Password");
			} else {

				if (userId > 0) {
					String databasePassword = migratedService.getPasswordByUserId(userId);

					if (!passwordEncoder.matches(Cryptography.petParentDecrypt(oldPassword), databasePassword)) {
						result.put("Key", Boolean.FALSE).put("Value", "In-valid old password");
					} else {
						ClientInfo clientInfo = new ClientInfo();
						clientInfo.setPassword(passwordEncoder.encode(Cryptography.petParentDecrypt(newPassword)));
						clientInfo.setUserId(userId);
						int status = migratedService.updatePassword(clientInfo);
						if (status == 0) {
							result.put("Key", Boolean.FALSE).put("Value", "Change password failed");
						} else {
							result.put("Key", true).put("Value", "");
						}
					}
				}
			}

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", result);
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response getClientInfo_v2(GetClientInfoRequest request, String token) {
		LOGGER.debug("getClientInfo_v2 called");
		JSONObject result = new JSONObject();
		JSONObject response = new JSONObject();

		int clientId = 0;

		try {
			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			clientId = request.getClientId();
			if (clientId <= 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide valid Client Id");
			} else {
				ClientInfo clientInfo = migratedService.getClientInfoById(clientId);
				if (clientInfo != null && clientInfo.getClientId() > 0) {
					result.put("Key", true).put("ClientInfo", new JSONObject(new Gson().toJson(clientInfo)));
				} else {
					result.put("Key", Boolean.FALSE).put("ClientInfo", new JSONObject(new Gson().toJson(clientInfo)));
				}
			}

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", result);
			response.put("success", true);
			response.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response changeClientInfo_v2(ClientInfoRequest request, String token) {
		LOGGER.debug("entered into changeClientInfo_v2");

		JSONObject result = new JSONObject();
		JSONObject response = new JSONObject();

		try {
			int loginUserId = mobileAppService.getPetParentByPetParentKey(token);
			if (loginUserId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			int userId = request.getUserId();
			int petParentId = request.getClientId();
			String firstName = request.getFirstName();
			String lastName = request.getLastName();
			String phoneNumber = request.getPhoneNumber();
			String secondaryEmail = request.getSecondaryEmail();
			// Boolean notifyToSecondaryEmail = request.getNotifyToSecondaryEmail();
			Address address = request.getPetParentAddress();

			Boolean isValidEmail = Boolean.FALSE;

			if (userId == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide User Id");
			} else if (firstName == null || firstName.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide First Name");
			} else if (lastName == null || lastName.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Last Name");
			} else if (phoneNumber == null || phoneNumber.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Phone Number");
			} else {

				Boolean isValidSecEmail = Boolean.TRUE;
				if (secondaryEmail != null && !secondaryEmail.trim().isEmpty()) {
					if (petParentId > 0) {
						isValidSecEmail = migratedService.validateSecondaryEmailByPetParentId(secondaryEmail,
								petParentId);
					} else {
						isValidSecEmail = migratedService.validateSecondaryEmailByUserId(secondaryEmail, userId);
					}
				}
				if (!isValidSecEmail) {
					result.put("Key", Boolean.FALSE).put("Value", "Secondary email already exists");
				} else {
					isValidEmail = Boolean.TRUE;
				}

				if (isValidEmail) {
					User userInfo = mobileAppService.getUserInfoById(userId);

					Boolean isValidAddress = Boolean.FALSE;
					JSONObject addressInDB = new JSONObject(new Gson().toJson(userInfo.getAddress()));
					if (!addressInDB.isEmpty()) {
						if (petParentId > 0 && address == null) {
							result.put("Key", Boolean.FALSE).put("Value", "Please provide Addresss");
						} else {
							isValidAddress = Boolean.TRUE;
						}
					} else {
						isValidAddress = Boolean.TRUE;
					}
					if (isValidAddress) {
						userInfo.setFirstName(firstName);
						userInfo.setLastName(lastName);
						userInfo.setFullName(prepareFullName(firstName, lastName));
						userInfo.setPhoneNumber(preparePhoneNumber(phoneNumber));
						userInfo.setPetParentId(petParentId);
						userInfo.setUserId(userId);
						userInfo.setSecondaryEmail(secondaryEmail);
						userInfo.setAddress(address);
						userInfo.setCreatedBy(loginUserId);
						userInfo.setPreferredWeightUnitId(request.getPreferredWeightUnitId());
						userInfo.setPreferredFoodRecUnitId(request.getPreferredFoodRecUnitId());
						userInfo.setPreferredFoodRecTime(request.getPreferredFoodRecTime());
						boolean flag = migratedService.updateUserInfo(userInfo);
						result.put("Key", flag).put("Value", "");
					}
				}
			}
			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", result);
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response changePassword(String payload, String token) {
		LOGGER.debug("entered into changePassword");
		JSONObject request = null;
		JSONObject response = new JSONObject();
		try {

			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			request = new JSONObject(payload);
			int clientID = request.getInt("ClientID");
			String newPassword = request.getString("NewPassword");
			String oldPassword = request.getString("Password");

			if (clientID == 0) {
				throw new ServiceValidationException("Client ID not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.CLIENT_ID_REQUIRED)));
			}

			if (newPassword == null || newPassword.trim().length() == 0) {
				throw new ServiceValidationException("New Password is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.NEW_PASSWORD_REQUIRED)));
			}

			if (oldPassword == null || oldPassword.trim().length() == 0) {
				throw new ServiceValidationException("Password is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.PASSWORD_REQUIRED)));
			}

			ClientInfo clientInfo = migratedService.getClientInfoById(clientID);
			JSONObject result = new JSONObject();
			if (clientInfo.getClientId() > 0) {
				String databasePassword = Cryptography.decrypt(
						migratedService.getPasswordByClientID(clientInfo.getClientId()),
						clientInfo.getUniqueId() == null ? Cryptography.DEFAULT_ENCRYPTION_KEY
								: clientInfo.getUniqueId());
				if (!databasePassword.equals(Cryptography.petParentDecrypt(oldPassword))) {
					result.put("Key", false);
					result.put("Value", "INVALID OLD PASSWORD");
				} else {
					String password = Cryptography.encrypt(Cryptography.petParentDecrypt(newPassword),
							clientInfo.getUniqueId());

					clientInfo.setPassword(password);
					int status = migratedService.updatePassword(clientInfo);
					if (status == 0) {
						result.put("Key", false);
						result.put("Value", "CHANGE FAILED");
					} else {
						result.put("Key", true);
						result.put("Value", "");
					}
				}
			}

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", result);
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response getClientInfo(String payload, String token) {
		LOGGER.debug("getClientInfo called");
		int clientid = 0;
		String clientID = "";
		JSONObject jsonObject = null;
		try {
			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			jsonObject = new JSONObject(payload);
			if (jsonObject.has("ClientID")) {
				clientid = jsonObject.getInt("ClientID");
			}
			if (clientid > 0) {
				clientID = Integer.toString(clientid);
			}
			if (clientID == null || clientID.trim().length() == 0) {
				throw new ServiceValidationException("Clinet ID is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.CLIENT_ID_REQUIRED)));
			}

			ClientInfo clientInfo = migratedService.getClientInfoById(clientid);
			JSONObject json = new JSONObject();
			json.put("clientID", clientInfo.getClientId());
			json.put("email", clientInfo.getEmail());
			json.put("fullName", clientInfo.getFullName());
			json.put("phoneNumber", clientInfo.getPhoneNumber());
			json.put("firstName", clientInfo.getFirstName());
			json.put("lastName", clientInfo.getLastName());

			jsonObject = new JSONObject();
			jsonObject.put("errors", "null");
			jsonObject.put("responseCode", 0);
			jsonObject.put("responseMessage", "SUCCESS");
			jsonObject.put("result", json);
			jsonObject.put("success", true);
			jsonObject.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(jsonObject.toString()).build();
	}

	@Override
	public Response changeClientInfo(String payload, String token) {
		LOGGER.debug("entered into changeClientInfo");

		JSONObject request = null;
		JSONObject response = new JSONObject();

		try {
			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			request = new JSONObject(payload);
			int petParentId = request.getInt("ClientID");
			String firstName = request.getString("FirstName");
			String lastName = request.getString("LastName");
			String phoneNumber = request.getString("PhoneNumber");

			if (petParentId == 0) {
				throw new ServiceValidationException("Client ID is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.CLIENT_ID_REQUIRED)));
			}

			if (firstName == null || firstName.trim().length() == 0) {
				throw new ServiceValidationException("First Name is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.FIRST_NAME_REQUIRED)));
			}

			if (lastName == null || lastName.trim().length() == 0) {
				throw new ServiceValidationException("Last Name is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.LAST_NAME_REQUIRED)));
			}

			if (phoneNumber == null || phoneNumber.trim().length() == 0) {
				throw new ServiceValidationException("Phone Number is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.PHONE_NUMBER_REQUIRED)));
			}

			ClientInfo clientInfo = migratedService.getClientInfoById(petParentId);
			String fullName = "";

			if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName)) {
				fullName += firstName + " " + lastName;
			} else {
				if (StringUtils.isNoneEmpty(firstName)) {
					fullName = firstName;
				}
			}
			clientInfo.setFullName(fullName);
			clientInfo.setFcmToken(null);

			clientInfo.setFullName(fullName);
			clientInfo.setFirstName(firstName);
			clientInfo.setLastName(lastName);
			clientInfo.setClientId(petParentId);

			if (phoneNumber != null && phoneNumber.trim().length() > 0) {
				if (phoneNumber.contains(" ")) {
					String[] phoneCodeArray = phoneNumber.split(" ");
					String countryCode = phoneCodeArray[0];
					String phoneCode = phoneCodeArray[1];
					if (countryCode == "+1") {
						String firstPhoneCode = " (" + phoneCode.substring(0, 3) + ") ";
						String secondPhoneCode = phoneCode.substring(3, 3);
						String thirdPhoneCode = "-" + phoneCode.substring(6);
						phoneNumber = countryCode + firstPhoneCode + secondPhoneCode + thirdPhoneCode;
					}
				}
				clientInfo.setPhoneNumber(phoneNumber);
			}

			boolean result = migratedService.updateClientInfo(clientInfo);

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", new JSONObject().put("result", result));
			response.put("success", true);
			response.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response manageMobileAppScreensFeedback(AppFeedBackReqeust request, String token) {
		LOGGER.debug("entered into manageMobileAppScreensFeedback");
		JSONObject response = new JSONObject();
		try {
			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			int petParentId = request.getClientId();
			int petId = request.getPetId();
			String pageName = request.getPageName();
			String deviceType = request.getDeviceType();
			String feedbackText = request.getFeedbackText();

			if (petParentId == 0) {
				throw new ServiceValidationException("Clinet ID is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.CLIENT_ID_REQUIRED)));
			}

			if (petId == 0) {
				throw new ServiceValidationException("Pet ID is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.PET_ID_REQUIRED)));
			}

			if (pageName == null || pageName.trim().length() == 0) {
				throw new ServiceValidationException("Page Name is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.PAGE_NAME_REQUIRED)));
			}

			if (deviceType == null || deviceType.trim().length() == 0) {
				throw new ServiceValidationException("Device Type is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_TYPE_REQUIRED)));
			}

			if (feedbackText == null || feedbackText.trim().length() == 0) {
				throw new ServiceValidationException("Feedback Text is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.FEEDBACK_TEXT_REQUIRED)));
			}

			MobileAppFeedbackDTO mobileAppFeedbackDTO = new MobileAppFeedbackDTO();
			mobileAppFeedbackDTO.setDeviceType(deviceType);
			mobileAppFeedbackDTO.setFeedbackText(feedbackText);
			mobileAppFeedbackDTO.setPageName(pageName);
			mobileAppFeedbackDTO.setPetId(Integer.toString(petId));
			mobileAppFeedbackDTO.setPetParentId(Integer.toString(petParentId));
			mobileAppFeedbackDTO.setLoginUserId(Integer.toString(petParentId));

			boolean status = migratedService.manageMobileAppScreensFeedback(mobileAppFeedbackDTO);

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", status);
			response.put("success", true);
			response.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response getPetTimerLog_v2(GetClientInfoRequest request, String token) {
		LOGGER.debug("entered into getPetTimerLog_v2");
		JSONObject result = new JSONObject();
		JSONObject response = new JSONObject();
		try {

			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			String petParentId = request.getClientId().toString();

			if (petParentId == null || petParentId.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Client Id");
			} else {
				List<TimerLog> petTimerLogs = migratedService.getPetTimerLog(Integer.parseInt(petParentId));
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(petTimerLogs);
				result.put("Key", Boolean.TRUE).put("Value", new JSONArray(json));
			}

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", result);
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response getPetTimerLog(String payload, String token) {
		LOGGER.debug("entered into getPetTimerLog");
		JSONObject request = null;
		JSONObject response = new JSONObject();
		try {

			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			request = new JSONObject(payload);

			String petParentId = request.getString("ClientID");

			if (petParentId == null || petParentId.trim().length() == 0) {
				throw new ServiceValidationException("Client ID is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.CLIENT_ID_REQUIRED)));
			}

			List<TimerLog> petTimerLogs = migratedService.getPetTimerLog(Integer.parseInt(petParentId));

			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(petTimerLogs);

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", new JSONArray(json));
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response managePetTimerLog(PetTimerLogRequest request, String token) {
		LOGGER.debug("entered into managePetTimerLog");
		JSONObject result = new JSONObject();
		JSONObject response = new JSONObject();
		try {

			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			String petParentId = request.getClientId().toString();
			String petId = request.getPetId().toString();
			String category = request.getCategory();
			String deviceNumber = request.getDeviceNumber();
			String duration = request.getDuration();
			String timerDate = request.getTimerDate();

			if (petParentId == null || petParentId.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Client Id");
			} else if (petId == null || petId.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Pet Id");
			} else if (category == null || category.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Category");
			} else if (deviceNumber == null || deviceNumber.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Device Number");
			} else if (duration == null || duration.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Duration");
			} else if (timerDate == null || timerDate.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Timer Date");
			} else {
				TimerLog timerLog = new TimerLog();
				timerLog.setCategory(category);
				timerLog.setDeviceNumber(deviceNumber);
				timerLog.setDuration(duration);
				timerLog.setPetId(Integer.parseInt(petId));
				timerLog.setPetParentId(Integer.parseInt(petParentId));
				timerLog.setTimerDate(timerDate);
				timerLog.setUserId(petParentId);

				Boolean flag = migratedService.managePetTimerLog(timerLog);
				result.put("Key", flag).put("Value", "");
			}

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", result);
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response updateSensorSetupStatus(SensorSetupStatusRequest request, String token) {
		LOGGER.debug("updateSensorSetupStatus called");
		JSONObject response = new JSONObject();
		try {
			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			String petParentId = request.getClientId().toString();
			String petId = request.getPatientId().toString();
			String setupStatus = request.getSetupStatus();
			String ssidList = request.getSsidList();
			String deviceNumber = request.getDeviceNumber();

			if (petParentId == null || petParentId.trim().length() == 0) {
				throw new ServiceValidationException("Clinet ID is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.CLIENT_ID_REQUIRED)));
			}

			if (petId == null || petId.trim().length() == 0) {
				throw new ServiceValidationException("Pet Id is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.PET_ID_REQUIRED)));
			}

			if (setupStatus == null || setupStatus.trim().length() == 0) {
				throw new ServiceValidationException("Setup Status is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.SETUP_STATUS_REQUIRED)));
			}

			if (ssidList == null || ssidList.trim().length() == 0) {
				throw new ServiceValidationException("SSID List is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.SSID_REQUIRED)));
			}

			SensorDetailsDTO sensorDetailsDTO = new SensorDetailsDTO();
			sensorDetailsDTO.setDeviceNumber(deviceNumber);
			sensorDetailsDTO.setPetId(petId);
			sensorDetailsDTO.setPetParentId(petParentId);
			sensorDetailsDTO.setSetupStatus(setupStatus);
			sensorDetailsDTO.setSsidList(ssidList);
			sensorDetailsDTO.setUserId(petParentId);

			boolean sensorUpdateStatus = migratedService.updateSensorSetupStatus(sensorDetailsDTO);
			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", new JSONObject().put("Key", sensorUpdateStatus));
			response.put("success", true);
			response.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response getSensorStatus(SensorStatusRequest request, String token) {
		LOGGER.debug("getSensorStatus called");
		JSONObject response = new JSONObject();
		try {
			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			String petParentId = request.getClientId().toString();
			String petId = request.getPetId().toString();

			if (petParentId == null || petParentId.trim().length() == 0) {
				throw new ServiceValidationException("Clinet ID is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.CLIENT_ID_REQUIRED)));
			}

			if (petId == null || petId.trim().length() == 0) {
				throw new ServiceValidationException("Pet ID is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.PET_ID_REQUIRED)));
			}
			LOGGER.debug("petParentId " + petParentId + " petId " + petId);
			SensorDetailsDTO sensorDetailsDTO = migratedService.getSensorSetupStatus(petParentId, petId);

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", new JSONObject().put("SetupStatus", sensorDetailsDTO.getSetupStatus())
					.put("SSIDList", sensorDetailsDTO.getSsidList() == null ? " " : sensorDetailsDTO.getSsidList()));
			response.put("success", true);
			response.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response manageSensorChargingNotificationSettings(SensorNotificationSettingsRequest request, String token) {
		LOGGER.debug("entered into manageSensorChargingNotificationSettings");
		JSONObject response = new JSONObject();
		try {
			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			int petParentId = request.getClientId();
			int petId = request.getPetId();
			String notificationType = request.getNotificationType();
			String notificationDay = request.getNotificationDay();
			String opt = request.getOpt();

			if (petParentId == 0) {
				throw new ServiceValidationException("Clinet ID is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.CLIENT_ID_REQUIRED)));
			}

			if (notificationType == null || notificationType.trim().length() == 0) {
				throw new ServiceValidationException("Notification Type is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.NOTIFICATION_TYPE_REQUIRED)));
			}

			SensorDetailsDTO sensorDetailsDTO = new SensorDetailsDTO();
			sensorDetailsDTO.setPetId(Integer.toString(petId));
			sensorDetailsDTO.setPetParentId(Integer.toString(petParentId));
			sensorDetailsDTO.setNotificationType(notificationType);
			sensorDetailsDTO.setOpt(opt);

			Calendar weekStartDate = Calendar.getInstance();
			if (notificationType.trim().toLowerCase().equals("weekly")) {
				OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
				int DayNo = 0;
				DayOfWeek Day = now.getDayOfWeek();
				int Days = Day.getValue() - DayOfWeek.MONDAY.getValue();
				now = now.minusDays(Days);
				weekStartDate.setTimeInMillis(now.toInstant().toEpochMilli());

				if (notificationDay != null && notificationDay.trim().length() > 0) {
					switch (notificationDay) {
					case "monday":
						DayNo = 0;
						break;
					case "tuesday":
						DayNo = 1;
						break;
					case "wednesday":
						DayNo = 2;
						break;
					case "thursday":
						DayNo = 3;
						break;
					case "friday":
						DayNo = 4;
						break;
					case "saturday":
						DayNo = 5;
						break;
					case "sunday":
						DayNo = 6;
						break;
					default:
						DayNo = 0;
						break;
					}
				}

				weekStartDate.add(Calendar.DAY_OF_MONTH, DayNo);
				Calendar notificationDate = Calendar.getInstance();
				notificationDate.setTimeInMillis(weekStartDate.getTimeInMillis());
				Calendar startTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

				if (notificationDate.get(Calendar.DAY_OF_MONTH) <= startTime.get(Calendar.DAY_OF_MONTH)) {
					notificationDate.add(Calendar.DAY_OF_MONTH, 7);
				}
				sensorDetailsDTO.setNotificationDate(notificationDate.getTime());
			}
			boolean status = Boolean.FALSE;
			if (petParentId > 0 && sensorDetailsDTO.getNotificationDate() != null) {
				status = migratedService.manageSensorChargingNotificationSettings(sensorDetailsDTO);
			}
			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", status);
			response.put("success", true);
			response.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response validateDeviceNumber(ValidateDeviceRequest request, String token) {
		LOGGER.debug("entered into validateDeviceNumber");
		JSONObject response = new JSONObject();
		try {
			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			String sensorNumber = request.getSensorNumber();
			String clientId = request.getClientId();

			if (sensorNumber == null || sensorNumber.trim().length() == 0) {
				throw new ServiceValidationException("Sensor Number is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_NUMBER_REQUIRED)));
			}

			if (clientId == null || clientId.trim().length() == 0) {
				throw new ServiceValidationException("Client ID is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.CLIENT_ID_REQUIRED)));
			}

			String responseCode = "";
			String message = "";
			boolean IsValidDeviceNumber = Boolean.FALSE;

			DeviceAssignDTO assignDTO = migratedService.getDeviceAssignInfoByDeviceNumber(sensorNumber, clientId);
			if (!assignDTO.isValidDevice()) {
				responseCode = "ERROR";
				message = "The sensor you entered is not a valid sensor. Please confirm you entered the correct device number or select a different sensor.";
			} else {
				if (!assignDTO.isIsAssign()) {
					responseCode = "SUCCESS";
					message = "SUCCESS";
					IsValidDeviceNumber = true;
				} else {
					responseCode = "ERROR";
					if (assignDTO.isIsSameClient()) {
						message = "The sensor you entered is already assigned to " + assignDTO.getPetName()
								+ " in your practice. You must un-assign the sensor from " + assignDTO.getPetName()
								+ " before it can be used with another pet.";
					} else {
						message = "The sensor you entered is already assigned to another pet. Please confirm you entered the correct device number or select a different sensor.";
					}

					IsValidDeviceNumber = Boolean.FALSE;
				}
			}

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", new JSONObject().put("isValidDeviceNumber", IsValidDeviceNumber)
					.put("message", message).put("responseCode", responseCode));
			response.put("success", true);
			response.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@SuppressWarnings("unused")
	@Override
	public Response completeOnboardingInfo(PetOnboardingRequest petOnboardingRequest, String token) {
		LOGGER.debug("entered into completeOnboardingInfo");
		JSONObject request = null;
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();
		try {

			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			PetInfo about = petOnboardingRequest.getAbout();
			PlanInfo plan = petOnboardingRequest.getPlan();
			com.hillspet.wearables.request.DeviceInfo device = petOnboardingRequest.getDevice();
			com.hillspet.wearables.request.ClientInfo client = petOnboardingRequest.getClient();
			BillingInfo billing = petOnboardingRequest.getBilling();

			Address petAddress = null;
			Boolean isPetWithPetParent = Boolean.TRUE;
			try {
				petAddress = about.getPetAddress();
				isPetWithPetParent = about.getIsPetWithPetParent() == 1 ? Boolean.TRUE : Boolean.FALSE;
			} catch (JSONException e) {
				about.setIsPetWithPetParent(1);
				LOGGER.error("Old Service called :: No address found");
			}

			if (validatePetOnboardingRequest(about, device, client, result, "Onboarding")) {

				StringBuilder sbStatus = new StringBuilder();
				int petID = 0;
				String OnboardingInfoGuid;
				String UID = "";

				OnboardingInfo onboardingInfo = new OnboardingInfo();
				onboardingInfo.setClinicID(2901); // Convert.ToInt32(AppConfig.StudyId);
				onboardingInfo.setUserID(8989); // Convert.ToInt32(AppConfig.ClinicUserID);

				if (about != null) {
					String petName = about.getPetName();
					if (petName != null && petName.trim().length() > 0) {
						onboardingInfo.setTitle(petName);
					}
				}

				onboardingInfo.setData(""); // payload
				OnboardingInfoGuid = migratedService.handleOnboardingInfo(onboardingInfo);
				OnboardingInfo onboardingData = migratedService.getOnboardingInfoByUID(OnboardingInfoGuid);
				List<MonitoringPlan> monitoringPlanList = migratedService
						.getMonitoringPlanList(onboardingData.getClinicID());
				PetInfoDTO petInfo = new PetInfoDTO();
				String petId = about.getPetId();
				if (petId != null && petId.trim().length() != 0) {
					petID = Integer.parseInt(about.getPetId());
					petInfo = migratedService.getPetInfoByID(petID);
				}

				String clientId = client.getClientId();
				if (clientId != null && clientId.trim().length() > 0) {
					petInfo.setPetParentId(Integer.parseInt(clientId));
				}
				petInfo.setPetName(about.getPetName());
				petInfo.setGender(about.getPetGender());
				if (about.getIsUnknown() != null && about.getIsUnknown().trim().length() > 0) {
					petInfo.setUnknown(Boolean.valueOf(about.getIsUnknown()));
				}
				petInfo.setBirthDay(about.getPetBirthday());
				if (about.getPetBreedId() != null && about.getPetBreedId().trim().length() > 0) {
					petInfo.setBreedId(Integer.parseInt(about.getPetBreedId()));
				}

				if (about.getIsMixed() != null && about.getIsMixed().trim().length() > 0) {
					petInfo.setIsMixed(Boolean.valueOf(about.getIsMixed()));
				}

				petInfo.setMixBreed(about.getPetMixBreed());
				petInfo.setWeightUnit(about.getWeightUnit());
				petInfo.setWeight((about.getPetWeight()));
				petInfo.setUserId(onboardingInfo.getUserID());

				if (about.getPetBFI() != null && about.getPetBFI().trim().length() > 0) {
					petInfo.setPetBFI(Integer.parseInt(about.getPetBFI()));
				}

				if (about.getIsNeutered() != null && about.getIsNeutered().trim().length() > 0) {
					petInfo.setNeutered(Boolean.valueOf(about.getIsNeutered()));
				}

				petInfo.setIsPetWithPetParent(isPetWithPetParent);
				if (isPetWithPetParent) {
					petInfo.setPetAddress(new Address());
				} else {
					petInfo.setPetAddress(petAddress);
				}

				petInfo.setBrandId(about.getBrandId());
				petInfo.setFoodIntake(about.getFoodIntake());
				petInfo.setFeedUnit(about.getFeedUnit());

				if (petId != null && petId.trim().length() != 0) {
					petInfo.setPetId(Integer.parseInt(petId));
					migratedService.updatePetInfo(petInfo); // Completed
					sbStatus.append("UPDATE PETINFO " + petId);
				} else {
					petInfo.setAlgorithmVersion("2.9.2");
					petID = migratedService.insertPetInfo(petInfo);
					petInfo.setPetId(petID);
					sbStatus.append("INSERT PETINFO " + petId);
				}
				sbStatus.append("INSERT CLIENTINFO " + clientId);

				int planID = 0;
				MonitoringPlan monitoringPlan = new MonitoringPlan();
				monitoringPlan.setPetID(petID);
				monitoringPlan.setClientID(Integer.parseInt(clientId));
				monitoringPlan.setClinicID(onboardingData.getClinicID());
				monitoringPlan.setSubscriptionID(billing.getSubscriptionId());
				monitoringPlan.setFree(onboardingData.isIsFree());
				monitoringPlan.setUserID(onboardingData.getUserID());
				Calendar startTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				monitoringPlan.setStartDate(getStringDateFormat("yyyy-MM-dd HH:mm:ss", startTime.getTime()));
				startTime.add(Calendar.YEAR, 1);
				monitoringPlan.setEndDate(getStringDateFormat("yyyy-MM-dd HH:mm:ss", startTime.getTime()));

				String PlanTypeID = plan.getPlanTypeId();
				if (PlanTypeID != null && PlanTypeID.trim().length() > 0) {
					monitoringPlan.setTypeID(Integer.parseInt(PlanTypeID));
				}
				// String comment = "onboarded new patient "+petInfo.getPetName();
				planID = migratedService.insertMonitoringPlan(monitoringPlan);

				sbStatus.append("INSERT MONITORINGPLAN " + planID);

				PetCheckedInfo petCheckedInfo = new PetCheckedInfo();
				petCheckedInfo.setPetID(petID);
				petCheckedInfo.setCheckedBy(onboardingData.getUserID());

				startTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				petCheckedInfo.setCheckedTime(getStringDateFormat("yyyy-MM-dd HH:mm:ss", startTime.getTime()));
				startTime.add(Calendar.DATE, 1);
				String petCheckStartTime = "06:00";
				String[] configTime = petCheckStartTime.split(":");
				int hours = Integer.parseInt(configTime[0]);
				int minutes = Integer.parseInt(configTime[1]);
				startTime.set(Calendar.HOUR, hours);
				startTime.set(Calendar.MINUTE, minutes);
				petCheckedInfo.setNextCheckedTime(getStringDateFormat("yyyy-MM-dd HH:mm:ss", startTime.getTime()));

				int petCheckedInfoID = migratedService.handlePetCheckedInfo(petCheckedInfo);
				sbStatus.append("INSERT PETCHECKEDINFO " + petCheckedInfoID);

				boolean IsJoinCompetition = Boolean.FALSE;
				String joinCompleted = plan.getIsJoinCompetition();
				if (joinCompleted != null && joinCompleted.trim().length() > 0) {
					IsJoinCompetition = Boolean.parseBoolean(joinCompleted);
				}

				DeviceAssignDTO deviceAssign = new DeviceAssignDTO();
				int deviceID = 0;
				String deviceNumber = "";
				if (device.getSensorNumber() != null && device.getSensorNumber().trim().length() > 0) {
					deviceNumber = device.getSensorNumber();
					DeviceInfo deviceInfo = migratedService.getDeviceInfoByDeviceNumber(deviceNumber);
					if (deviceInfo.getDeviceID() > 0) {
						deviceID = deviceInfo.getDeviceID();
					} else {
						deviceInfo = new DeviceInfo();
						deviceInfo.setDeviceNumber(deviceNumber);
						deviceInfo.setDeviceType(device.getDeviceType());
						deviceInfo.setAddDate(device.getDeviceAddDate());
						deviceInfo.setUserId(onboardingData.getUserID());
						deviceID = migratedService.insertDeviceInfo(deviceInfo);
					}

					if (deviceID > 0) {
						deviceAssign = new DeviceAssignDTO();
						deviceAssign.setUserId(onboardingData.getUserID());
						deviceAssign.setPetID(petID);
						deviceAssign.setPlanID(planID);
						deviceAssign.setDeviceID(deviceID);
						deviceAssign.setDeviceNumber(deviceNumber);
						deviceAssign.setIsAssign(true);
						deviceAssign.setAssignDate(device.getDeviceAddDate());

						int id = migratedService.insertDeviceAssign(deviceAssign);
						sbStatus.append("INSERT DEVICEASSIGN " + id);
					}
				}

				migratedService.updateOnboardingArchived(UID, onboardingData.getUserID());
				migratedService.updateOnboardingStatus(UID, sbStatus.toString(), onboardingData.getUserID());

				String emailSubject = "Welcome to Wearables Clinical Trials";
				String emailContent = EmailTemplate.getPetOnboardWelcomeEmail(client.getClientFirstName());

				// Sending mail using SendGrid
				ThreadPoolExecutor threadPoolExecutor = EmailThreadPoolExecutor.getEmailThreadPoolExecutor();
				EmailSenderThread emailSenderThread = new EmailSenderThread(client.getClientEmail(), emailSubject,
						emailContent);
				threadPoolExecutor.submit(emailSenderThread);

				try {
					// Send mail to secondary email
					ClientInfo clientInfo = migratedService.getClientInfoById(Integer.parseInt(clientId));
					String secondaryEmail = clientInfo.getSecondaryEmail();
					if (secondaryEmail != null) {
						EmailSenderThread emailSenderThread1 = new EmailSenderThread(secondaryEmail, emailSubject,
								emailContent);
						threadPoolExecutor.submit(emailSenderThread1);
					}
				} catch (Exception e) {
					LOGGER.error("Exception while sending mail to secondary email");
				}

				result.put("Key", Boolean.TRUE);
				result.put("encryptPetID", Cryptography.encryptByAES(Integer.toString(petID)));
				result.put("petID", petID);
				result.put("petParentId", client.getClientId());
				result.put("responseCode", "SUCCESS");
				result.put("uid", UID);
			}

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("result", result);
			response.put("responseMessage", "SUCCESS");
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@SuppressWarnings("unused")
	@Override
	public Response updatePetInfo(PetUpdateRequest petUpdateRequest, String token) {
		LOGGER.debug("entered into updatePetInfo");
		JSONObject request = null;
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();
		try {

			int userId = mobileAppService.getPetParentByPetParentKey(token);
			if (userId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			PetInfo about = petUpdateRequest.getAbout();
			GetClientInfoRequest client = petUpdateRequest.getClient();

			Address petAddress = null;
			Boolean isPetWithPetParent = Boolean.TRUE;
			try {
				petAddress = about.getPetAddress();
				isPetWithPetParent = about.getIsPetWithPetParent() == 1 ? Boolean.TRUE : Boolean.FALSE;
			} catch (JSONException e) {
				about.setIsPetWithPetParent(1);
				LOGGER.error("Old Service called :: No address found");
			}

			if (validatePetOnboardingRequest(about, null, null, result, "Update")) {

				StringBuilder sbStatus = new StringBuilder();
				int petID = 0;
				String OnboardingInfoGuid;
				String UID = "";

				PetInfoDTO petInfo = new PetInfoDTO();
				String petId = about.getPetId();
				if (petId != null && petId.trim().length() != 0) {
					petID = Integer.parseInt(about.getPetId());
					petInfo = migratedService.getPetInfoByID(petID);
				}

				String clientId = client.getClientId().toString();
				if (clientId != null && clientId.trim().length() > 0) {
					petInfo.setPetParentId(Integer.parseInt(clientId));
				}
				petInfo.setPetName(about.getPetName());
				petInfo.setGender(about.getPetGender());
				if (about.getIsUnknown() != null && about.getIsUnknown().trim().length() > 0) {
					petInfo.setUnknown(Boolean.valueOf(about.getIsUnknown()));
				}
				petInfo.setBirthDay(about.getPetBirthday());
				if (about.getPetBreedId() != null && about.getPetBreedId().trim().length() > 0) {
					petInfo.setBreedId(Integer.parseInt(about.getPetBreedId()));
				}

				if (about.getIsMixed() != null && about.getIsMixed().trim().length() > 0) {
					petInfo.setIsMixed(Boolean.valueOf(about.getIsMixed()));
				}

				petInfo.setMixBreed(about.getPetMixBreed());
				petInfo.setWeightUnit(about.getWeightUnit());
				petInfo.setWeight((about.getPetWeight()));
				petInfo.setUserId(userId);

				if (about.getPetBFI() != null && about.getPetBFI().trim().length() > 0) {
					petInfo.setPetBFI(Integer.parseInt(about.getPetBFI()));
				}

				if (about.getIsNeutered() != null && about.getIsNeutered().trim().length() > 0) {
					petInfo.setNeutered(Boolean.valueOf(about.getIsNeutered()));
				}

				petInfo.setIsPetWithPetParent(isPetWithPetParent);
				if (isPetWithPetParent) {
					petInfo.setPetAddress(new Address());
				} else {
					petInfo.setPetAddress(petAddress);
				}

				petInfo.setPetId(Integer.parseInt(petId));
				migratedService.updatePetInfo(petInfo); // Completed

				result.put("Key", Boolean.TRUE);
				result.put("encryptPetID", Cryptography.encryptByAES(Integer.toString(petID)));
				result.put("petID", petID);
				result.put("responseCode", "SUCCESS");
				result.put("uid", UID);
			}

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("result", result);
			response.put("responseMessage", "SUCCESS");
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response logoutUser(LogoutRequest logoutRequest, String token) {
		LOGGER.debug("entered into logoutUser");
		JSONObject response = new JSONObject();
		try {
			migratedService.logoutUser(logoutRequest.getPetParentId(), token);

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", new JSONObject().put("responseCode", "SUCCESS"));
			response.put("success", true);
			response.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response deleteAccount(String payload, String token) {
		LOGGER.debug("entered into deleteAccount");

		int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
		if (petParentKeyId == 0) {
			return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
		}

		JSONObject request = null;
		JSONObject response = new JSONObject();
		try {

			request = new JSONObject(payload);
			String petParentId = request.getString("PetParentId");

			migratedService.deleteAccount(petParentId);

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", new JSONObject().put("responseCode", "SUCCESS"));
			response.put("success", true);
			response.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	private String sendClientVerificationCode(ClientInfo clientInfo, String deliveryMethod, boolean isClinicTrial)
			throws ParseException {
		String sid = "";
		if (clientInfo.getUserId() != null && clientInfo.getUserId() > 0) {
			ClientSMSCode clientSMSCode = migratedService.getClientSMSCodeByUserId(clientInfo.getUserId());

			Calendar calendar = null;
			if (clientSMSCode.getAddDate() != null) {
				Date date = getDateStringToDate(dateFormat, clientSMSCode.getAddDate());// formatter.parse(clientSMSCode.getAddDate());
				calendar = Calendar.getInstance();
				calendar.setTimeInMillis(date.getTime());
				calendar.add(Calendar.DATE, 7);
			}

			Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

			if (clientSMSCode.getPetParentSMSCodeId() > 0 && calendar != null && calendar.compareTo(currentTime) >= 0) {
				if (deliveryMethod == "Email") {
					sid = sendClientEmailVerificationCode(clientInfo.getEmail(), clientSMSCode.getVerificationCode(),
							isClinicTrial);
				}
			} else {
				if (clientSMSCode.getPetParentSMSCodeId() > 0) {
					clientSMSCode.setExpired(true);
					clientSMSCode.setUserId(clientInfo.getUserId());
					migratedService.updateClientSMSCode(clientSMSCode);
				}

				Random random = new Random();
				clientSMSCode = new ClientSMSCode();
				clientSMSCode.setClientID(clientInfo.getClientId());
				clientSMSCode.setVerificationCode(Integer.toString(random.nextInt((1000000 - 100000) + 1) + 100000));
				clientSMSCode.setExpired(Boolean.FALSE);
				currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				Date utilDate = currentTime.getTime();
				String strDate = getStringDateFormat(dateFormat, utilDate);
				clientSMSCode.setAddDate(strDate);
				clientSMSCode.setUserId(clientInfo.getUserId());

				int result = migratedService.insertClientSMSCode(clientSMSCode);
				if (result > 0) {
					if (deliveryMethod == "Email") {
						sid = sendClientEmailVerificationCode(clientInfo.getEmail(),
								clientSMSCode.getVerificationCode(), isClinicTrial);
					}
				}
			}
		}
		return sid;
	}

	private JSONObject sendEmailVerificationCode(String email, int isNewUser) {
		LOGGER.debug("entered into sendEmailVerificationCode");
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();
		try {
			ClientInfo clientInfo = null;

			if (email == null || email.trim().length() == 0) {
				result.put("ClientID", 0);
				result.put("Key", Boolean.FALSE).put("Value", "Please provide email");
			} else {
				clientInfo = migratedService.getClientInfoByEmail(email);

				if (isNewUser == 0 && (clientInfo.getClientId() == 0 && clientInfo.getUserId() == null)) {
					result.put("ClientID", 0);
					result.put("Key", Boolean.FALSE).put("Value", "User does not exists");
				} else if (isNewUser == 0 && ((clientInfo.getClientId() != 0 || clientInfo.getUserId() != null)
						&& !clientInfo.isActive())) {
					result.put("ClientID", 0);
					result.put("Key", Boolean.FALSE).put("Value",
							"User is In-Active, Please contact support@wearablesclinicaltrials.com");
				} else {
					String sid = sendClientVerificationCode(clientInfo, "Email", true);
					boolean status = Boolean.FALSE;
					if (sid != null && sid.trim().length() > 0) {
						status = true;
					}
					result.put("Key", status).put("Value", "");
				}
			}

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", result);
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private JSONObject validateEmailVerificationCode(String email, String verificationCode) {
		LOGGER.debug("entered into validateEmailVerificationCode");
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();
		try {
			if (email == null || email.trim().length() == 0) {
				result.put("ClientID", 0);
				result.put("Key", Boolean.FALSE).put("Value", "Please provide email");
			} else if (verificationCode == null || verificationCode.trim().length() == 0) {
				result.put("ClientID", 0);
				result.put("Key", Boolean.FALSE).put("Value", "Please provide verification code");
			} else {
				ClientInfo clientInfo = migratedService.getClientInfoByEmail(email);
				System.out.println("clientInfo.getClientId() " + clientInfo.getClientId());
				ClientSMSCode clientSMSCode = migratedService.getClientSMSCodeByUserId(clientInfo.getUserId());

				boolean status = Boolean.FALSE;
				if (clientSMSCode.getPetParentSMSCodeId() > 0
						&& clientSMSCode.getVerificationCode().equals(verificationCode)) {
					status = true;
				}
				result.put("Key", status).put("Value", "");
			}
			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", result);
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private String sendClientEmailVerificationCode(String email, String verificationCode, boolean isClinicTrial) {
		String sid = "";
		String emailSubject = "Verification Code";
		String content = EmailTemplate.getSMSCodeEmail(verificationCode, isClinicTrial);

		// Sending mail using SendGrid
		ThreadPoolExecutor threadPoolExecutor = EmailThreadPoolExecutor.getEmailThreadPoolExecutor();
		EmailSenderThread emailSenderThread = new EmailSenderThread(email, emailSubject, content);
		threadPoolExecutor.submit(emailSenderThread);

		sid = "OK";
		return sid;
	}

	private String prepareFullName(String firstName, String lastName) {
		String fullName = "";
		if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName)) {
			fullName += firstName + " " + lastName;
		} else {
			if (StringUtils.isNoneEmpty(firstName)) {
				fullName = firstName;
			}
		}
		return fullName;
	}

	private String preparePhoneNumber(String phoneNumber) {
		if (phoneNumber != null && phoneNumber.trim().length() > 0) {
			if (phoneNumber.contains(" ")) {
				String[] phoneCodeArray = phoneNumber.split(" ");
				String countryCode = phoneCodeArray[0];
				String phoneCode = phoneCodeArray[1];
				if (countryCode == "+1") {
					String firstPhoneCode = " (" + phoneCode.substring(0, 3) + ") ";
					String secondPhoneCode = phoneCode.substring(3, 3);
					String thirdPhoneCode = "-" + phoneCode.substring(6);
					phoneNumber = countryCode + firstPhoneCode + secondPhoneCode + thirdPhoneCode;
				}
			}
		}
		return phoneNumber;
	}

	private void encryptPetParentPassword(ResetPasswordReqeust request, JSONObject result, ClientInfo client) {
		LOGGER.debug("entered into encryptPetParentPassword");
		try {
			String email = request.getEmail();
			String verificationCode = request.getVerificationCode();
			String password = request.getPassword();

			if (email == null || email.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide email");
			} else if (verificationCode == null || verificationCode.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide verification code");
			} else if (password == null || password.trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Password");
			} else {
				if (client != null && client.getUserId() != null && client.getUserId() > 0) {
					ClientSMSCode clientSMSCode = migratedService.getClientSMSCodeByUserId(client.getUserId());

					if (clientSMSCode != null && clientSMSCode.getPetParentSMSCodeId() > 0
							&& clientSMSCode.getVerificationCode().equals(verificationCode)) {
						String newPassword = "";
						/*
						 * newPassword = Cryptography.encrypt(Cryptography.petParentDecrypt(password),
						 * client.getUniqueId());
						 */
						newPassword = Cryptography.petParentDecrypt(password);
						client.setPassword(passwordEncoder.encode(newPassword));
					} else {
						result.put("Key", Boolean.FALSE).put("Value", "Please provide valid Verification Code");
					}
				} else {
					result.put("Key", Boolean.FALSE).put("Value", "Please provide valid Email");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Boolean validatePetOnboardingRequest(PetInfo about, com.hillspet.wearables.request.DeviceInfo device,
			com.hillspet.wearables.request.ClientInfo client, JSONObject result, String action) {

		Boolean isValidPetData = Boolean.FALSE;

		if (about != null) {
			if (action.equalsIgnoreCase("Update") && about.getPetId() == null
					&& about.getPetId().trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Pet Id");
			} else if (about.getPetName() == null || about.getPetName().trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Pet Name");
			} else if (about.getPetGender() == null || about.getPetGender().trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Pet Gender");
			} else if (about.getIsNeutered() == null || about.getIsNeutered().trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Pet IsNeutered");
			} else if (about.getPetWeight() != null && about.getPetWeight().trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Pet Weight");
			} else if (about.getWeightUnit() == null || about.getWeightUnit().trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Pet Weight Unit");
			} else if (about.getIsMixed() == null || about.getIsMixed().trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Pet IsMixed");
			} else if (about.getPetBreedId() == null || about.getPetBreedId().trim().length() == 0) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Pet Breed");
				/*
				 * } else if (action.equalsIgnoreCase("Onboarding") && (device.getSensorNumber()
				 * == null || device.getSensorNumber().trim().length() == 0)) {
				 * result.put("Key", Boolean.FALSE).put("Value",
				 * "Please provide Sensor Number");
				 */
			} else if (action.equalsIgnoreCase("Onboarding") && device.getSensorNumber().trim().length() > 0
					&& (device.getDeviceType() == null || device.getDeviceType().trim().length() == 0)) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Device Type");
			} else if (action.equalsIgnoreCase("Onboarding") && device.getSensorNumber().trim().length() > 0
					&& (device.getDeviceAddDate() == null || device.getDeviceAddDate().trim().length() == 0)) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Device Assign Date");
			} else if (action.equalsIgnoreCase("Onboarding")
					&& (client.getClientEmail() == null || client.getClientEmail().trim().length() == 0)) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Client Email");
			} else if (action.equalsIgnoreCase("Onboarding")
					&& (client.getClientFirstName() == null || client.getClientFirstName().trim().length() == 0)) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Client First Name");
			} else if (action.equalsIgnoreCase("Onboarding")
					&& (client.getClientLastName() == null || client.getClientLastName().trim().length() == 0)) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Client Last Name");
			} else if (action.equalsIgnoreCase("Onboarding")
					&& (client.getClientPhone() == null || client.getClientPhone().trim().length() == 0)) {
				result.put("Key", Boolean.FALSE).put("Value", "Please provide Client Phone Number");
			} else if (about.getIsPetWithPetParent() == 0) {
				Address address = about.getPetAddress();
				if (address == null) {
					result.put("Key", Boolean.FALSE).put("Value", "Please provide pet address");
				} else {
					isValidPetData = Boolean.TRUE;
				}
			} else {
				isValidPetData = Boolean.TRUE;
			}
		} else {
			result.put("Key", Boolean.FALSE).put("Value", "Please provide pet info");
		}

		if (action.equalsIgnoreCase("Onboarding") && isValidPetData) {
			ClientInfo clientInDb = migratedService.getClientInfoByEmail(client.getClientEmail());
			if (clientInDb != null && clientInDb.getClientId() > 0) {
				isValidPetData = Boolean.TRUE;
				client.setClientId(String.valueOf(clientInDb.getClientId()));
			} else {
				Address petParentAddress = new Address();
				ClientInfo clientInfo = new ClientInfo();
				clientInfo.setFirstName(client.getClientFirstName());
				clientInfo.setLastName(client.getClientLastName());
				clientInfo.setEmail(client.getClientEmail());
				clientInfo.setFullName(prepareFullName(client.getClientFirstName(), client.getClientLastName()));
				clientInfo.setPhoneNumber(preparePhoneNumber(client.getClientPhone()));
				boolean isValidEmail = insertOrUpdatePetParent(result, clientInfo, petParentAddress, isValidPetData);
				if (isValidEmail) {
					client.setClientId(String.valueOf(clientInfo.getClientId()));
					isValidPetData = Boolean.TRUE;
				}
			}
		}

		return isValidPetData;
	}

	private String getStringDateFormat(String dateFormat, Date date) {
		if (date != null) {
			java.text.SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			return sdf.format(date);
		} else {
			return null;
		}
	}

	private Date getDateStringToDate(String dateFormat, String date) throws ParseException {
		if (date != null && dateFormat != null) {
			return new SimpleDateFormat(dateFormat).parse(date);
		} else {
			return null;
		}
	}

	// ------------------------- Old Services ---------------------

	@Override
	public Response sendEmailVerificationCode(String payload) {
		LOGGER.debug("entered into SendEmailVerificationCode");
		JSONObject request = null;
		JSONObject response = new JSONObject();
		try {

			request = new JSONObject(payload);
			String email = request.getString("Email");

			if (email == null || email.trim().length() == 0) {
				throw new ServiceValidationException("Email informaton is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.EMAIL_REQUIRED)));
			}

			ClientInfo clientInfo = migratedService.getClientInfoByEmail(email);
			String sid = sendClientVerificationCode(clientInfo, "Email", true);
			boolean status = false;
			if (sid != null && sid.trim().length() > 0) {
				status = true;
			}
			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", new JSONObject().put("Key", status).put("Value", ""));
			response.put("success", true);
			response.put("warnings", "null");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response manageClientInfo(String payload, String token) {
		LOGGER.debug("entered into manageClientInfo");
		JSONObject request = null;
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();
		try {

			int petParentKeyId = mobileAppService.getPetParentByPetParentKey(token);
			if (petParentKeyId == 0) {
				return Response.status(Response.Status.FORBIDDEN).entity(Constants.INVALID_TOKEN).build();
			}

			request = new JSONObject(payload);

			String email = request.getString("Email");
			String firstName = request.getString("FirstName");
			String lastName = request.getString("LastName");
			String phoneNumber = request.getString("PhoneNumber");

			if (email == null || email.trim().length() == 0) {
				throw new ServiceValidationException("Email is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.EMAIL_REQUIRED)));
			}

			if (firstName == null || firstName.trim().length() == 0) {
				throw new ServiceValidationException("First Name is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.FIRST_NAME_REQUIRED)));
			}

			if (lastName == null || lastName.trim().length() == 0) {
				throw new ServiceValidationException("Last Name is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.LAST_NAME_REQUIRED)));
			}

			if (phoneNumber == null || phoneNumber.trim().length() == 0) {
				throw new ServiceValidationException("Phone Number is not provided",
						Arrays.asList(new WearablesError(WearablesErrorCode.PHONE_NUMBER_REQUIRED)));
			}

			ClientInfo client = migratedService.getClientInfoByEmail(email);

			if (client != null && client.getClientId() > 0) {
				String password = migratedService.getPasswordByClientID(client.getClientId());
				if (StringUtils.isEmpty(password)) {
					client.setFirstName(firstName);
					client.setLastName(lastName);

					String fullName = "";
					if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName)) {
						fullName += firstName + " " + lastName;
					} else {
						if (StringUtils.isNoneEmpty(firstName)) {
							fullName = firstName;
						}
					}

					client.setFullName(fullName);

					if (client.getPhoneNumber() != null && client.getPhoneNumber().trim().length() > 0) {
						if (phoneNumber != null && phoneNumber.trim().length() > 0) {
							if (phoneNumber.contains(" ")) {
								String[] phoneCodeArray = phoneNumber.split(" ");
								String countryCode = phoneCodeArray[0];
								String phoneCode = phoneCodeArray[1];
								if (countryCode == "+1") {
									String firstPhoneCode = " (" + phoneCode.substring(0, 3) + ") ";
									String secondPhoneCode = phoneCode.substring(3, 3);
									String thirdPhoneCode = "-" + phoneCode.substring(6);
									phoneNumber = countryCode + firstPhoneCode + secondPhoneCode + thirdPhoneCode;
									client.setPhoneNumber(phoneNumber);
								}
							}
						}
					}
					client.setUserId(8989);
					boolean status = migratedService.updateClientInfo(client);
					if (status) {
						result.put("ClientID", client.getClientId());
						result.put("FirstName", client.getFirstName());
						result.put("LastName", client.getLastName());
						result.put("Email", client.getEmail());
						result.put("PhoneNumber", client.getPhoneNumber());
						result.put("ResponseCode", "SUCCESS");
					} else {
						result.put("ClientID", 0);
						result.put("FirstName", "");
						result.put("LastName", "");
						result.put("Email", "");
						result.put("PhoneNumber", "");
						result.put("ResponseCode", "Please contact system administrator.");

					}
				} else {
					result.put("ClientID", 0);
					result.put("FirstName", "");
					result.put("LastName", "");
					result.put("Email", "");
					result.put("PhoneNumber", "");
					result.put("ResponseCode", "Given email address already existed in the System.");
				}
			} else {
				String fullName = "";
				if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName)) {
					fullName += firstName + " " + lastName;
				} else {
					if (StringUtils.isNoneEmpty(firstName)) {
						fullName = firstName;
					}
				}

				if (phoneNumber != null && phoneNumber.trim().length() > 0) {
					if (phoneNumber.contains(" ")) {
						String[] phoneCodeArray = phoneNumber.split(" ");
						String countryCode = phoneCodeArray[0];
						String phoneCode = phoneCodeArray[1];
						if (countryCode == "+1") {
							String firstPhoneCode = " (" + phoneCode.substring(0, 3) + ") ";
							String secondPhoneCode = phoneCode.substring(3, 3);
							String thirdPhoneCode = "-" + phoneCode.substring(6);
							phoneNumber = countryCode + firstPhoneCode + secondPhoneCode + thirdPhoneCode;
						}
					}
				}

				ClientInfo newClientInfo = new ClientInfo();
				newClientInfo.setFirstName(firstName);
				newClientInfo.setLastName(lastName);
				newClientInfo.setEmail(email);
				newClientInfo.setFullName(fullName);
				newClientInfo.setPhoneNumber(phoneNumber);
				newClientInfo.setUserId(8989);
				int clientID = migratedService.insertClientInfo(newClientInfo);
				if (clientID > 0) {
					result.put("ClientID", clientID);
					result.put("FirstName", firstName);
					result.put("LastName", lastName);
					result.put("Email", email);
					result.put("PhoneNumber", phoneNumber);
					result.put("ResponseCode", "SUCCESS");
				}
			}

			response.put("errors", "null");
			response.put("responseCode", 0);
			response.put("responseMessage", "SUCCESS");
			response.put("result", result);
			response.put("success", true);
			response.put("warnings", "null");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response validate() {
		JSONObject response = new JSONObject();
		response.put("responseMessage", "SUCCESS");
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}

	@Override
	public Response migrateDecryptPassword() {
		migratedService.migrateDecryptPassword();
		JSONObject response = new JSONObject();
		response.put("responseMessage", "SUCCESS");
		return Response.status(Response.Status.OK).entity(response.toString()).build();
	}
}
