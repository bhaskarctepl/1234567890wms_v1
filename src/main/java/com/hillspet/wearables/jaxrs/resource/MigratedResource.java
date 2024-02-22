package com.hillspet.wearables.jaxrs.resource;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.response.SuccessResponse;
import com.hillspet.wearables.request.AppFeedBackReqeust;
import com.hillspet.wearables.request.ChangePasswordRequest;
import com.hillspet.wearables.request.ClientInfoRequest;
import com.hillspet.wearables.request.EmailRequest;
import com.hillspet.wearables.request.EmailVerificationCodeReqeust;
import com.hillspet.wearables.request.GetClientInfoRequest;
import com.hillspet.wearables.request.LoginRequest;
import com.hillspet.wearables.request.LogoutRequest;
import com.hillspet.wearables.request.PetOnboardingRequest;
import com.hillspet.wearables.request.PetTimerLogRequest;
import com.hillspet.wearables.request.PetUpdateRequest;
import com.hillspet.wearables.request.RegisterUserRequest;
import com.hillspet.wearables.request.ResetPasswordReqeust;
import com.hillspet.wearables.request.SensorNotificationSettingsRequest;
import com.hillspet.wearables.request.SensorSetupStatusRequest;
import com.hillspet.wearables.request.SensorStatusRequest;
import com.hillspet.wearables.request.ValidateDeviceRequest;
import com.hillspet.wearables.request.ValidateUserEmailReqeust;
import com.hillspet.wearables.response.CommonResponse;
import com.hillspet.wearables.response.LoginResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/migrated")
@Api(value = "RESTful service that have all the .net migrated services", tags = { "Migrated Services Management" })
@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
@Consumes({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
public interface MigratedResource {

	@POST
	@Path("/ClientLogin/v2")
	@ApiOperation(value = "To Log in the user into the mobile app", notes = "To Log in the user into the mobile app")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = LoginResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response clientLogin_v2(@Valid @ApiParam(name = "loginRequest", required = true) LoginRequest loginRequest);

	@POST
	@Path("/ForgotPasswordValidateEmail")
	@ApiOperation(value = "To validate User's email in the forgot password screen", notes = "To validate User's email in the forgot password screen")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response forgotPasswordValidateEmail(
			@Valid @ApiParam(name = "validateEmailRequest", required = true) EmailRequest validateEmailRequest);

	@POST
	@Path("/ForgotPasswordValidateEmailVerificationCode")
	@ApiOperation(value = "To validate verification code entered by the User", notes = "To validate verification code entered by the User")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response forgotPasswordValidateEmailVerificationCode(
			@Valid @ApiParam(name = "emailVerificationCodeReqeust", required = true) EmailVerificationCodeReqeust emailVerificationCodeReqeust);

	@POST
	@Path("/ResetForgotPassword")
	@ApiOperation(value = "To reset the password of the User's account from the Forgot password section", notes = "To reset the password of the User's account from the Forgot password section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response resetForgotPassword(
			@Valid @ApiParam(name = "resetPasswordReqeust", required = true) ResetPasswordReqeust resetPasswordReqeust);

	@POST
	@Path("/RegisterUserValidateEmail")
	@ApiOperation(value = "To validate User's email in the registration section", notes = "To validate User's email in the registration section")
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response registerUserValidateEmail(
			@Valid @ApiParam(name = "validateUserEmailReqeust", required = true) ValidateUserEmailReqeust validateUserEmailReqeust);

	@POST
	@Path("/RegisterUserSendEmailVerificationCode")
	@ApiOperation(value = "Send Email Verification Code", notes = "Send Email Verification Code")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response registerUserSendEmailVerificationCode(
			@Valid @ApiParam(name = "sendEmailVerificationCodeRequest", required = true) EmailRequest sendEmailVerificationCodeRequest);

	@POST
	@Path("/RegisterUserValidateEmailVerificationCode")
	@ApiOperation(value = "To validate the verification code entered by the User in the registration section", notes = "To validate the verification code entered by the User in the registration section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response registerUserValidateEmailVerificationCode(
			@Valid @ApiParam(name = "emailVerificationCodeReqeust", required = true) EmailVerificationCodeReqeust emailVerificationCodeReqeust);

	@POST
	@Path("/RegisterUser")
	@ApiOperation(value = "To resgister the user in the mobile app", notes = "To resgister the user in the mobile app")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response registerUser(
			@Valid @ApiParam(name = "registerUserRequest", required = true) RegisterUserRequest registerUserRequest);

	@POST
	@Path("/ChangePassword/v2")
	@ApiOperation(value = "To change User's account password from the account section", notes = "To change User's account password from the account section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response changePassword_v2(
			@Valid @ApiParam(name = "changePasswordRequest", required = true) ChangePasswordRequest changePasswordRequest,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/GetClientInfo/v2")
	@ApiOperation(value = "To get user' s details", notes = "To get user' s details")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getClientInfo_v2(
			@Valid @ApiParam(name = "getClientInfoRequest", required = true) GetClientInfoRequest getClientInfoRequest,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/ChangeClientInfo/v2")
	@ApiOperation(value = "To update User details in the account section", notes = "To update User details in the account section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response changeClientInfo_v2(
			@Valid @ApiParam(name = "clientInfoRequest", required = true) ClientInfoRequest ClientInfoRequest,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/ManageMobileAppScreensFeedback")
	@ApiOperation(value = "To submit the feedback entered by the Pet parent in the feedback section", notes = "To submit the feedback entered by the Pet parent in the feedback section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response manageMobileAppScreensFeedback(
			@Valid @ApiParam(name = "appFeedBackReqeust", required = true) AppFeedBackReqeust appFeedBackReqeust,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/GetPetTimerLog/v2")
	@ApiOperation(value = "To get the Timer logs in the Timer section", notes = "To get the Timer logs in the Timer section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetTimerLog_v2(
			@Valid @ApiParam(name = "getPetTimerLogRequest", required = true) GetClientInfoRequest getPetTimerLogRequest,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/ManagePetTimerLog")
	@ApiOperation(value = "To submit timer log in the Timer section", notes = "To submit timer log in the Timer section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response managePetTimerLog(
			@Valid @ApiParam(name = "petTimerLogRequest", required = true) PetTimerLogRequest petTimerLogRequest,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/UpdateSensorSetupStatus")
	@ApiOperation(value = "To update the sensor setup status after the setup in the Sensor setup section", notes = "To update the sensor setup status after the setup in the Sensor setup section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response updateSensorSetupStatus(
			@Valid @ApiParam(name = "sensorSetupStatusRequest", required = true) SensorSetupStatusRequest sensorSetupStatusRequest,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/ManageSensorChargingNotificationSettings")
	@ApiOperation(value = "To set the notification frequency after the sensor setup from the Sensor setup section", notes = "To set the notification frequency after the sensor setup from the Sensor setup section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response manageSensorChargingNotificationSettings(
			@Valid @ApiParam(name = "sensorNotificationSettingsRequest", required = true) SensorNotificationSettingsRequest sensorNotificationSettingsRequest,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/ValidateDeviceNumber")
	@ApiOperation(value = "To validate the Device number entered by the User in the Pet onboard section", notes = "To validate the Device number entered by the User in the Pet onboard section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response validateDeviceNumber(
			@Valid @ApiParam(name = "validateDeviceRequest", required = true) ValidateDeviceRequest validateDeviceRequest,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/CompleteOnboardingInfo")
	@ApiOperation(value = "To submit the pet details in the Onboard section", notes = "To submit the pet details in the Onboard section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response completeOnboardingInfo(
			@Valid @ApiParam(name = "petOnboardingRequest", required = true) PetOnboardingRequest petOnboardingRequest,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/UpdatePetInfo")
	@ApiOperation(value = "To update the pet's info from the Pet details section", notes = "To update the pet's info from the Pet details section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response updatePetInfo(
			@Valid @ApiParam(name = "petUpdateRequest", required = true) PetUpdateRequest petUpdateRequest,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/logout")
	@ApiOperation(value = "To log out the User from the mobile app", notes = "To log out the User from the mobile app")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response logoutUser(@Valid @ApiParam(name = "logoutRequest", required = true) LogoutRequest logoutRequest,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/deleteAccount")
	@ApiOperation(value = "Logout User", notes = "Delete the user account from application")
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response deleteAccount(@RequestBody String payload, @HeaderParam("ClientToken") String token);

	// -----Old or Unused Services / not to include in swagger -------------
	@POST
	@Path("ClientLogin")
	@ApiOperation(value = "Clent Login", notes = "Clent Login", hidden = true)
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response clientLogin(@RequestBody String payload);

	@POST
	@Path("/SendEmailVerificationCode")
	@ApiOperation(value = "Send Email Verification Code", notes = "Send Email Verification Code", hidden = true)
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response sendEmailVerificationCode(@RequestBody String payload);

	@POST
	@Path("/ManageClientInfo")
	@ApiOperation(value = "Manage Client Info", notes = "Manage Client Info", hidden = true)
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response manageClientInfo(@RequestBody String payload, @HeaderParam("ClientToken") String token);

	@POST
	@Path("/GetClientInfo")
	@ApiOperation(value = "Get Client Info", notes = "Get Pet Parent Info By Pet Parent Id", hidden = true)
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getClientInfo(@RequestBody String payload, @HeaderParam("ClientToken") String token);

	@POST
	@Path("/ChangeClientInfo")
	@ApiOperation(value = "Change Client Info", notes = "Change Client Info", hidden = true)
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response changeClientInfo(@RequestBody String payload, @HeaderParam("ClientToken") String token);

	@POST
	@Path("/ChangePassword")
	@ApiOperation(value = "Change Password", notes = "Change Password", hidden = true)
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response changePassword(@RequestBody String payload, @HeaderParam("ClientToken") String token);

	@POST
	@Path("/GetPetTimerLog")
	@ApiOperation(value = "Get Pet TimerLog", notes = "Get Pet TimerLog", hidden = true)
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetTimerLog(@RequestBody String payload, @HeaderParam("ClientToken") String token);

	@POST
	@Path("/GetSensorSetupStatus")
	@ApiOperation(value = "To get the sensor setup status", notes = "To get the sensor setup status", hidden = true)
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response"),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getSensorStatus(
			@Valid @ApiParam(name = "sensorStatusRequest", required = true) SensorStatusRequest sensorStatusRequest,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/validate")
	@ApiOperation(value = "Validate App", notes = "check application health", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response validate();

	@GET
	@Path("/migrateDecryptPassword")
	public Response migrateDecryptPassword();

}
