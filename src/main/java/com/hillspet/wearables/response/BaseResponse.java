package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {

	@ApiModelProperty(value = "success", required = true, example = "true")
	private Boolean success;

	@ApiModelProperty(value = "warnings", required = true, example = "")
	private String warnings;

	@ApiModelProperty(value = "responseMessage", required = true, example = "SUCCESS")
	private String responseMessage;

	@ApiModelProperty(value = "errors", required = true, example = "")
	private String errors;

	@ApiModelProperty(value = "responseCode", required = true, example = "1")
	private Integer responseCode;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getWarnings() {
		return warnings;
	}

	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

}
