package com.hillspet.wearables.jaxrs.resource;

import java.io.IOException;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import com.hillspet.wearables.dto.PetFeedingEnthusiasmScale;
import com.hillspet.wearables.dto.PetObservation;
import com.hillspet.wearables.objects.common.response.CommonResponse;
import com.hillspet.wearables.request.AddPetWeight;
import com.hillspet.wearables.request.BehaviorHistoryRequest;
import com.hillspet.wearables.request.AssignSensorRequest;
import com.hillspet.wearables.request.FMGoalSettingRequest;
import com.hillspet.wearables.request.PetAddFeedingPreferences;
import com.hillspet.wearables.request.PetAddImageScoring;
import com.hillspet.wearables.request.PetIds;
import com.hillspet.wearables.request.UpdatePet;
import com.hillspet.wearables.request.UpdatePetWeight;
import com.hillspet.wearables.request.ValidateDuplicatePetRequest;
import com.hillspet.wearables.response.BehaviorHistoryResponse;
import com.hillspet.wearables.response.BehaviorVisualizationResponse;
import com.hillspet.wearables.response.EatingEnthusiasmScaleResponse;
import com.hillspet.wearables.response.ImageScoringScalesResponse;
import com.hillspet.wearables.response.PetAddressResponse;
import com.hillspet.wearables.response.PetBehaviorsResponse;
import com.hillspet.wearables.response.PetBreedResponse;
import com.hillspet.wearables.response.PetFeedingEnthusiasmScaleResponse;
import com.hillspet.wearables.response.PetFeedingPreferenceResponse;
import com.hillspet.wearables.response.PetFeedingTimeResponse;
import com.hillspet.wearables.response.PetMobileAppConfigResponse;
import com.hillspet.wearables.response.PetObservationResponse;
import com.hillspet.wearables.response.PetObservationsResponse;
import com.hillspet.wearables.response.PetSpeciesResponse;
import com.hillspet.wearables.response.PetWeightHistoryResponse;
import com.hillspet.wearables.response.PetWeightResponse;
import com.hillspet.wearables.response.PetsResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This class providing Pet details.
 * 
 * @author sgorle
 * @since w2.0
 * @version w2.0
 * @version Dec 8, 2021
 */
@Path("/pets")
@Api(value = "RESTful service that performs pets related operations", tags = { "Pet Management" })
@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
@Consumes({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
public interface PetResource {

	@POST
	@Path("/addWeight")
	@ApiOperation(value = "To add weight for a selected pet", notes = "To add weight for a selected pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetWeightResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response addWeight(@Valid @ApiParam(name = "addPetWeight", required = true) AddPetWeight addPetWeight,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/updateWeight")
	@ApiOperation(value = "To update the weight of the pet", notes = "To update the weight of the pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetWeightResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response updateWeight(
			@Valid @ApiParam(name = "updatePetWeight", required = true) UpdatePetWeight updatePetWeight,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/weightHistory/{petId}")
	@ApiOperation(value = "To get the weight history for a pet", notes = "To get the weight history for a pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetWeightHistoryResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetWeightHistory(@PathParam("petId") int petId, @QueryParam("fromDate") String fromDate,
			@QueryParam("toDate") String toDate, @HeaderParam("ClientToken") String token) throws IOException;

	@GET
	@Path("/getPetSpecies")
	@ApiOperation(value = "To get species", notes = "To get species")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetSpeciesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetSpecies(@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetBreeds/{speciesId}")
	@ApiOperation(value = "To get the breeds for a pet based on speciesId(Feline/Canine) ", notes = "To get the breeds for a pet based on speciesId(Feline/Canine")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetBreedResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetBreeds(@PathParam("speciesId") int speciesId, @HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetBehaviors/{speciesId}")
	@ApiOperation(value = "To get the behaviors for a pet based on speciesId(Feline/Canine)", notes = "To get the behaviors for a pet based on speciesId(Feline/Canine)")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetBehaviorsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetBehaviors(@PathParam("speciesId") int speciesId, @HeaderParam("ClientToken") String token,
			@QueryParam("behaviorTypeId") int behaviorTypeId);

	@GET
	@Path("/getPetEatingEnthusiasmScale")
	@ApiOperation(value = "To get the Eating enthusiasm scales for a pet", notes = "To get the Eating enthusiasm scales for a pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = EatingEnthusiasmScaleResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetEatingEnthusiasmScale(@HeaderParam("ClientToken") String token,
			@QueryParam("speciesId") int speciesId);

	@GET
	@Path("/getPetFeedingTime")
	@ApiOperation(value = "To get the Feeding time for a pet in Enthusiasm section", notes = "To get the Feeding time for a pet in Enthusiasm section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetFeedingTimeResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetFeedingTime(@HeaderParam("ClientToken") String token);

	@POST
	@Path("/getMobileAppConfigs")
	@ApiOperation(value = "To get feature permissions for pets", notes = "To get feature permissions for pets")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetMobileAppConfigResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getMobileAppConfigs(@Valid @ApiParam(name = "petIds", required = true) PetIds petIds,
			@HeaderParam("ClientToken") String token) throws IOException;

	@POST
	@Path("/addPetFeedingTime")
	@ApiOperation(value = "To add the feeding time for a pet in Eating Enthusiasm section", notes = "To add the feeding time for a pet in Eating Enthusiasm section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetFeedingEnthusiasmScaleResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response addPetFeedingTime(
			@Valid @ApiParam(name = "addPetFeedingTime", required = true) PetFeedingEnthusiasmScale petFeedingEnthusiasmScale,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("getPetObservations/{petId}")
	@ApiOperation(value = "To get observation of a pet based on Pet Id", notes = "To get observation of a pet based on Pet Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetObservationsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetObservationsByPetId(@PathParam("petId") int petId, @HeaderParam("ClientToken") String token);

	@POST
	@Path("/savePetObservation")
	@ApiOperation(value = "To add/edit the observation of a pet", notes = "To add/edit the observation of a pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetObservationResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response savePetObservation(
			@Valid @ApiParam(name = "savePetObservation", required = true) PetObservation savePetObservation,
			@HeaderParam("ClientToken") String token);

	@DELETE
	@Path("/{observationId}/{petId}/{petParentId}")
	@ApiOperation(value = "To delete a pet observation", notes = "To delete a pet observation")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response deletePetObservation(@PathParam("observationId") int observationId, @PathParam("petId") int petId,
			@PathParam("petParentId") int petParentId, @HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetImageScoringScales/{petId}")
	@ApiOperation(value = "To get the scales for a pet based on Pet Id", notes = "To get the scales for a pet based on Pet Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = ImageScoringScalesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetImageScoringScales(@PathParam("petId") int petId, @HeaderParam("ClientToken") String token);

	@POST
	@Path("/addPetImageScoring")
	@ApiOperation(value = "To add a score for pet image in Image scoring section", notes = "To add a score for pet image in Image scoring section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response addPetImageScoring(
			@Valid @ApiParam(name = "addPetImageScoring", required = true) PetAddImageScoring addPetImageScorings,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetFeedingPreferences")
	@ApiOperation(value = "To get Feeding preferences for a pet in Onboarding pet section", notes = "To get Feeding preferences for a pet in Onboarding pet section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetFeedingPreferenceResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetFeedingPreferences(@HeaderParam("ClientToken") String token);

	@POST
	@Path("/addPetFeedingPreferences")
	@ApiOperation(value = "To add the Feeding preferences for a pet in Onboarding section", notes = "To add the Feeding preferences for a pet in Onboarding section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response addPetFeedingPreferences(
			@Valid @ApiParam(name = "petAddFeedingPreferences", required = true) PetAddFeedingPreferences petAddFeedingPreferences,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/validateDuplicatePet")
	@ApiOperation(value = "Checks wether the pet is already in the system in Onboarding pet section", notes = "Checks wether the pet is already in the system in Onboarding pet section")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response validateDuplicatePet(
			@Valid @ApiParam(name = "validateDuplicatePetRequest", required = true) ValidateDuplicatePetRequest validateDuplicatePetRequest,
			@HeaderParam("ClientToken") String token);

	// -----Old or Unused Services / not to include in swagger -------------
	@GET
	@Path("/getMobileAppConfigs/{petId}")
	@ApiOperation(value = "To get mobile app configs for a pet", notes = "To get mobile app configs for a pet", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetMobileAppConfigResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getMobileAppConfigs(@PathParam("petId") int petId, @HeaderParam("ClientToken") String token)
			throws IOException;

	@PUT
	@Path("/updatePet")
	@ApiOperation(value = "To update pet details", notes = "To update pet details", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetWeightResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response updatePet(@Valid @ApiParam(name = "updatePet", required = true) UpdatePet updatePet,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/petAddressHistory/{id}")
	@ApiOperation(value = "To get the Pet Address history by pet id", notes = "To get the Pet Address history by pet id", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetAddressResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetAddressHistoryById(@PathParam("id") int petId, @HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetsByPetParentIdAndMobileAppConfigId/{petParentId}/{mobileAppConfigId}")
	@ApiOperation(value = "To get the pet details based on menu and user id", notes = "To get the pet details based on menu and user id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetsByPetParentIdAndMobileAppConfigId(@PathParam("petParentId") int petParentId,
			@PathParam("mobileAppConfigId") int mobileAppConfigId, @HeaderParam("ClientToken") String token);
	@GET
	@Path("/getBehaviorVisualization/{petId}")
	@ApiOperation(value = "Get Behavior details for Pet by behaviorType", notes = "Get Behavior details for Pet by behaviorType")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = BehaviorVisualizationResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getBehaviorVisualization(@PathParam("petId") int petId,@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getBehaviorHistoryByType/{petId}/{behaviorType}")
	@ApiOperation(value = "Get Behavior history for Pet by behaviorType", notes = "Get Behavior history for Pet by behaviorType")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = BehaviorHistoryResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getBehaviorHistoryByType(@PathParam("petId") int petId,
			@PathParam("behaviorType") String behaviorType,
			@BeanParam BehaviorHistoryRequest behaviorHistoryRequest,@HeaderParam("ClientToken") String token);
	
	

	@POST
	@Path("/replaceSensorToPet")
	@ApiOperation(value = "To replace a sensor to a pet in case of damaged/lost", notes = "To replace a sensor to a pet in case of damaged/lost")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response replaceSensorToPet(
			@Valid @ApiParam(name = "replaceSensorRequest", required = true) AssignSensorRequest assignSensorRequest,
			@HeaderParam("ClientToken") String token);

	@PUT
	@Path("/saveForwardMotionGoal")
	@ApiOperation(value = "To update forward motion goal of the pet", notes = "To update forward motion goal of the pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response saveForwardMotionGoal(
			@Valid @ApiParam(name = "saveForwardMotionGoalRequest", required = true) FMGoalSettingRequest fmGoalSettingRequest,
			@HeaderParam("ClientToken") String token);
}
