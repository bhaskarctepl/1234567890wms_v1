package com.hillspet.wearables.jaxrs.resource;

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
import com.hillspet.wearables.objects.common.response.CommonResponse;
import com.hillspet.wearables.request.PetBfiImageScoreRequest;
import com.hillspet.wearables.request.PetBfiImagesRequest;
import com.hillspet.wearables.response.BfiImageScoreResponse;
import com.hillspet.wearables.response.PetBfiInstructionResponse;
import com.hillspet.wearables.response.PetFoodBrandResponse;
import com.hillspet.wearables.response.PetImagePositionResponse;
import com.hillspet.wearables.response.PetsResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author vvodyaram
 *
 */
@Path("/petBfi")
@Api(value = "RESTful service that performs pet bfi images related operations", tags = { "Pet BFI Management" })
@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
@Consumes({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
public interface PetBfiResource {

	@GET
	@Path("/getPetBfiInstructions/{instructionType}")
	@ApiOperation(value = "To Get Pet Bfi Instructions", notes = "To get pet Bfi Instructions")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetBfiInstructionResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetBfiInstructions(@PathParam("instructionType") int instructionType,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetImagePositions")
	@ApiOperation(value = "To Get Pet Image Positions", notes = "To get pet image positions")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetImagePositionResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetImagePositions(@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getBfiImageScores/{speciesId}")
	@ApiOperation(value = "To Get Pet Image Scores", notes = "To get the image scores for a pet based on speciesId(Feline/Canine")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = BfiImageScoreResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getBfiImageScores(@PathParam("speciesId") int speciesId, @HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetFoodBrands/{speciesId}")
	@ApiOperation(value = "To Get Pet Food Brands", notes = "To get the pet food brands for a pet based on speciesId(Feline/Canine")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetFoodBrandResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetFoodBrands(@PathParam("speciesId") int speciesId, @HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetsToCaptureBfiImages/")
	@ApiOperation(value = "To Get Pets for Capture the Bfi Images", notes = "To Get Pets for Capture the Bfi Images based on search criteria")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getPetsToCaptureBfiImages(@QueryParam("petParentId") int petParentId,
			@QueryParam("speciesId") int speciesId, @QueryParam("pageNo") int pageNo,
			@QueryParam("pageLength") int pageLength, @QueryParam("searchText") String searchText,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/savePetBfiImages")
	@ApiOperation(value = "Save Pet BFI Images ", notes = "Save Pet Bfi Images")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response savePetBfiImages(
			@Valid @ApiParam(name = "savePetBfiImagesRequest", required = true) PetBfiImagesRequest petBfiImagesRequest,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getBfiPets/{pageNo}/{pageLength}")
	@ApiOperation(value = "Get BFI Pet info details", notes = "Get Bfi Pet info details")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getBfiPets(@PathParam("pageNo") int pageNo, @PathParam("pageLength") int pageLength,
			@QueryParam("searchText") String searchText, @QueryParam("isScored") boolean isScored,
			@HeaderParam("ClientToken") String token);

	@GET
	@Path("/getPetBfiImages/{petId}")
	@ApiOperation(value = "Get BFI Pet Images info details by petId", notes = "Get Bfi Pet Images info details petId")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetBfiImages(@PathParam("petId") int pageNo,
			@QueryParam("petBfiImageSetIds") String petBfiImageSetIds, @QueryParam("isScored") boolean isScored,
			@HeaderParam("ClientToken") String token);

	@POST
	@Path("/savePetBfiImageScore")
	@ApiOperation(value = "Save Pet BFI Image Scores ", notes = "Save Pet Bfi Image Scores")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response savePetBfiImageScore(
			@ApiParam(name = "savePetBfiImageScoresRequest", required = true) PetBfiImageScoreRequest petBfiImageScoreRequest,
			@HeaderParam("ClientToken") String token);
}
