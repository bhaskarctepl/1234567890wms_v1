package com.hillspet.wearables.jaxrs.resource;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.http.MediaType;

import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.response.SuccessResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/fileUpload")
@Api(value = "RESTful service that performs upload Files to GCP Storage", tags = { "File Management" })
@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
@Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)

public interface FileUploadResource {

	@POST
	@Path("/uploadPetPhoto")
	@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
	@Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiOperation(value = "To Add/update the Pet Image", notes = "To Add/update the Pet Image")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response uploadPetPhoto(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("petId") int petId,
			@FormDataParam("petParentId") int petParentId, @FormDataParam("file") FormDataBodyPart bodyPart);

	// -----Old or Unused Services / not to include in swagger -------------
	@POST
	@Path("/uploadFile")
	@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
	@Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiOperation(value = "Upload File to GCP Storage", notes = "Upload File to GCP Storage", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("moduleName") String moduleName, @FormDataParam("file") FormDataBodyPart bodyPart);

	@GET
	@Path("/getFileUrlByName")
	@ApiOperation(value = "Get File Url By Name", notes = "Get file url by name.", hidden = true)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SuccessResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request"),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found"),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden"),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error") })
	public Response getFileUrlByName(@QueryParam("fileName") String fileName);

}
