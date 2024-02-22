package com.hillspet.wearables.jaxrs.resource;

import java.io.IOException;

import javax.validation.Valid;
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
import com.hillspet.wearables.dto.FoodIntakeDTO;
import com.hillspet.wearables.response.FoodIntakeHistoryGraphResponse;
import com.hillspet.wearables.response.FoodIntakeLookUpResponse;
import com.hillspet.wearables.response.FoodIntakeResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This class providing Pet intake details.
 * 
 * @author akumarkhaspa
 * @since Dec 8, 2023
 */
@Path("/intake")
@Api(value = "RESTful service that performs pets intake related operations", tags = { "Food Intake" })
@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
@Consumes({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
public interface FoodIntakeResource {

	@GET
	@Path("/getPetFoodIntakeConfigData/{petId}/{petParentId}/{intakeDate}")
	@ApiOperation(value = "Get Food Intake Configuration Data", notes = "Gets the Food Intake Configuration Data")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = FoodIntakeLookUpResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetFoodIntakeConfigData(@PathParam("petId") int petId, @PathParam("petParentId") int petParentId,
			@PathParam("intakeDate") String intakeDate, @HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetIntakeList/{petId}/{petParentId}/{intakeDate}")
	@ApiOperation(value = "To get the intake list for a pet", notes = "To get the intake list for a pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = FoodIntakeResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetIntakeList(@PathParam("petId") int petId, @PathParam("petParentId") int petParentId,
			@PathParam("intakeDate") String intakeDate, @HeaderParam("ClientToken") String token) throws IOException;

	@GET
	@Path("/getPetIntakeById/{intakeId}")
	@ApiOperation(value = "To get the intake by Id", notes = "To get the intake by Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = FoodIntakeDTO.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetIntakeById(@PathParam("intakeId") int intakeId, @HeaderParam("ClientToken") String token)
			throws IOException;

	@POST
	@Path("/saveOrUpdatePetFoodIntake")
	@ApiOperation(value = "To add/edit the intake of a pet", notes = "To add/edit the intake of a pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = FoodIntakeDTO.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response saveOrUpdatePetFoodIntake(
			@Valid @ApiParam(name = "saveOrUpdatePetFoodIntake", required = true) FoodIntakeDTO foodIntake,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/studyDietIntakeHistory")
	@ApiOperation(value = "To get the study Diet Intake History for a pet", notes = "To get the study Diet Intake History for a pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = FoodIntakeHistoryGraphResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response studyDietIntakeHistory(@Valid @QueryParam("petId") int petId, @Valid @QueryParam("petParentId") int petParentId,
			@Valid @QueryParam("dietId") int dietId, @Valid @QueryParam("fromDate") String fromDate,
			@Valid @QueryParam("toDate") String toDate, @HeaderParam("ClientToken") String token) throws IOException;

}