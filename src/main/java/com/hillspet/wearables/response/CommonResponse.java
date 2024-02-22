package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonResponse {

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

	@ApiModelProperty(value = "result", required = true, example = "")
	private Result result;

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

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	static class Result {

		@ApiModelProperty(value = "key", required = true, example = "true")
		@JsonProperty("Key")
		private Boolean key;

		@ApiModelProperty(value = "value", required = true, example = "")
		@JsonProperty("Value")
		private String value;

		public Boolean getKey() {
			return key;
		}

		public void setKey(Boolean key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}
}
