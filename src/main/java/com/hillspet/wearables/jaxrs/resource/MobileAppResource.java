
/**
 * Created Date: 07-Jan-2021
 * Class Name  : QuestionnaireResource.java
 * Description : Description of the package.
 *
 * © Copyright 2020 Cambridge Technology Enterprises(India) Pvt. Ltd.,All rights reserved.
 *
 * * * * * * * * * * * * * * * Change History *  * * * * * * * * * * *
 * <Defect Tag>        <Author>        <Date>        <Comments on Change>
 * ID                sgorle          07-Jan-2021        Mentions the comments on change, for the new file it's not required.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.hillspet.wearables.jaxrs.resource;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;

import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.response.Message;
import com.hillspet.wearables.common.response.SuccessResponse;
import com.hillspet.wearables.dto.AddressFilter;
import com.hillspet.wearables.request.AssignSensorRequest;
import com.hillspet.wearables.request.QuestionAnswerRequest;
import com.hillspet.wearables.request.ValidateAddressRequest;
import com.hillspet.wearables.response.AppVersionResponse;
import com.hillspet.wearables.response.CampaignListResponse;
import com.hillspet.wearables.response.DeviceModelsResponse;
import com.hillspet.wearables.response.DeviceTypesResponse;
import com.hillspet.wearables.response.LeaderBoardResponse;
import com.hillspet.wearables.response.MeasurementUnitResponse;
import com.hillspet.wearables.response.MobileAppFeedbackResponse;
import com.hillspet.wearables.response.PetCampaignListResponse;
import com.hillspet.wearables.response.PetCampaignResponse;
import com.hillspet.wearables.response.PetDevicesResponse;
import com.hillspet.wearables.response.PetParentAddressResponse;
import com.hillspet.wearables.response.PetRedemptionHistoryResponse;
import com.hillspet.wearables.response.QuestionnaireListResponse;
import com.hillspet.wearables.response.StudyResponse;
import com.hillspet.wearables.response.TimeZoneResponse;
import com.hillspet.wearables.response.UserResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Enter detailed explanation of the class here..
 * <p>
 * This class implementation of the <tt>Interface or class Name</tt> interface
 * or class. In addition to implementing the <tt>Interface Name</tt> interface,
 * this class provides methods to do other operations. (Mention other methods
 * purpose)
 *
 * <p>
 * More Description about the class need to be entered here.
 *
 * @author vvodyaram
 * @version Wearables Portal Mobile Release Version..
 * @since Available Since Wearables Portal Version.
 * @see New line seperated Classes or Interfaces related to this class.
 */
@Path("/")
@Api(value = "RESTful service that performs Mobile App Services operations", tags = {
		"Mobile App Services Management" })
@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
@Consumes({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
public interface MobileAppResource {

	@GET
	@Path("/getFeedbackQuestionnaireByPetId/{petId}/{deviceModel}")
	@ApiOperation(value = "To get the feedback questionnaire in the Sensor setup section based on device model", notes = "To get the feedback questionnaire in the Sensor setup section based on device model")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = QuestionnaireListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getFeedbackQuestionnaireByPetId(@PathParam("petId") int petId,
			@PathParam("deviceModel") String deviceModel, @QueryParam("isDateSupported") String isDateSupported,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getQuestionnaireByPetId/{petId}")
	@ApiOperation(value = "To get all the list of questionnaire available for the pet based on pet id", notes = "To get all the list of questionnaire available for the pet based on pet id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = QuestionnaireListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getQuestionnaireByPetId(@PathParam("petId") int petId,
			@QueryParam("isDateSupported") String isDateSupported, @HeaderParam("ClientToken") String token);

	@POST
	@Path("/saveQuestionAnswers/v2")
	@ApiOperation(value = "To save the answers submitted by the Pet parent in the Questionnaire section", notes = "To save the answers submitted by the Pet parent in the Questionnaire section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response saveQuestionAnswers_v2(
			@Valid @ApiParam(name = "questionAnswerRequest", required = true) QuestionAnswerRequest questionAnswerRequest,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetCampaignPoints/{petId}")
	@ApiOperation(value = "To get the points obtained by the pet in a campaign based on pet id", notes = "To get the points obtained by the pet in a campaign based on pet id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetCampaignResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetCampaignPoints(@PathParam("petId") int petId, @HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetCampaignPointsList/{petId}")
	@ApiOperation(value = "To get list of points list in a campaign based on pet id in the Leaderboard section", notes = "To get list of points list in a campaign based on pet id in the Leaderboard section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetCampaignListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetCampaignPointsList(@PathParam("petId") int petId, @HeaderParam("ClientToken") String token);

	@GET
	@Path("/getCampaignListByPet/{petId}")
	@ApiOperation(value = "To obtain the campaign list based on pet ID", notes = "To obtain the campaign list based on pet ID")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CampaignListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getCampaignListByPet(@PathParam("petId") int petId, @HeaderParam("ClientToken") String token);

	@GET
	@Path("/getLeaderBoardByCampaignId/{campaignId}/{petId}")
	@ApiOperation(value = "Get Leader board By campaign Id", notes = "Get the Leader board By campaign Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = LeaderBoardResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getLeaderBoardByCampaignId(@PathParam("campaignId") int campaignId, @PathParam("petId") int petId,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetRedemptionHistory/{petId}")
	@ApiOperation(value = "To get the leaderboard details in a campaign based on pet id and campaign id", notes = "To get the leaderboard details in a campaign based on pet id and campaign id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetRedemptionHistoryResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetRedemptionHistory(@PathParam("petId") int petId, @HeaderParam("ClientToken") String token);

	@POST
	@Path("/assignSensorToPet")
	@ApiOperation(value = "To assign a sensor to a pet in the Device missing section", notes = "To assign a sensor to a pet in the Device missing section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response assignSensorToPet(
			@Valid @ApiParam(name = "assignSensorRequest", required = true) AssignSensorRequest assignSensorRequest,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetDevicesByPetParent/{petParentId}")
	@ApiOperation(value = "To get the pet details based on pet parent id", notes = "To get the pet details based on pet parent id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetDevicesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetDevicesByPetParent(@PathParam("petParentId") int petParentId,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getFeedbackByPetParent/{petParentId}")
	@ApiOperation(value = "To get the feedback submitted by the pet parent based on pet parent ID", notes = "To get the feedback submitted by the pet parent based on pet parent ID")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = MobileAppFeedbackResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getFeedbackByPetParent(@PathParam("petParentId") int petParentId,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/validateAddress")
	@ApiOperation(value = "To validate the address entered by the User in the address sections", notes = "To validate the address entered by the User in the address sections")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TimeZoneResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response validateAddress(@BeanParam AddressFilter addressFilter);
	
	@POST
	@Path("/validateAddress")
	@ApiOperation(value = "Validate the address and find the timezone", notes = "Validate the given address and if address is valid get the timezone of that address",  hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TimeZoneResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response validateUserAddress(@Valid @ApiParam(name = "validateAddressRequest", required = true) ValidateAddressRequest validateAddressRequest);

	@GET
	@Path("/getAppLatestVersion/{appOSId}/{appVersion}")
	@ApiOperation(value = "To get the app latest version for force updating the app if required", notes = "To get the app latest version for force updating the app if required")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = AppVersionResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getAppLatestVersion(@PathParam("appOSId") int appOSId, @PathParam("appVersion") String appVersion);

	// -----Old or Unused Services / not to include in swagger -------------
	@GET
	@Path("/getQuestionnaireAnswers/{petId}/{questionnaireId}")
	@ApiOperation(value = "To get the questionnaire answers submitted by the User based on pet id and questionnaire id", notes = "To get the questionnaire answers submitted by the User based on pet id and questionnaire id", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = QuestionnaireListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getQuestionnaireAnswers(@PathParam("petId") int petId,
			@PathParam("questionnaireId") int questionnaireId, @HeaderParam("ClientToken") String token);

	@POST
	@Path("/saveQuestionAnswers")
	@ApiOperation(value = "To save the answers submitted by the Pet parent in the Questionnaire section", notes = "To save the answers submitted by the Pet parent in the Questionnaire section", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response saveQuestionAnswers_v1(
			@Valid @ApiParam(name = "questionAnswerRequest", required = true) QuestionAnswerRequest questionAnswerRequest,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getDeviceTypes")
	@ApiOperation(value = "To get all device types", notes = "To get all device types", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = DeviceTypesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getDeviceTypes(@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getDeviceModels/{deviceType}")
	@ApiOperation(value = "To get device models by device type", notes = "To get device models by device type", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = DeviceModelsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getDeviceModels(@PathParam("deviceType") String deviceType,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getStudyByPetId/{petId}")
	@ApiOperation(value = "To get Study details by pet Id", notes = "To get Study details by pet Id", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getStudyByPetId(@PathParam("petId") int petId, @HeaderParam("ClientToken") String token);

	@GET
	@Path("/petParentAddressHistory/{petParentId}")
	@ApiOperation(value = "To get pet parent address history by pet parent id", notes = "To get pet parent address history by pet parent id", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetParentAddressResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetParentAddressHistoryById(@PathParam("petParentId") int petParentId,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getProfileInfo")
	@ApiOperation(value = "Get User Info", notes = "Get User Info By User Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = UserResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getProfileInfo(@HeaderParam("ClientToken") String token);
	
	@GET
	@Path("/getMeasurementUnits")
	@ApiOperation(value = "Get Measurement Units List Lookup", notes = "Get Measurement Units List Lookup")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = MeasurementUnitResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getMeasurementUnits(@QueryParam("unitCategory") String unitCategory, @HeaderParam("ClientToken") String token);
}
