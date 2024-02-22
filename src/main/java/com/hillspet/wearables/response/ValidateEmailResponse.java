package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateEmailResponse extends BaseResponse {

	private Result result;

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
