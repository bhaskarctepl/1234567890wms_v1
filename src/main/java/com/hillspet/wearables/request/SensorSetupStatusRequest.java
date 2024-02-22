package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to update sensor setup status", value = "SensorSetupStatusRequest")
@JsonInclude(value = Include.NON_NULL)
public class SensorSetupStatusRequest {

	@ApiModelProperty(value = "clientId", required = true, example = "3332")
	@JsonProperty("ClientID")
	private Integer clientId;

	@ApiModelProperty(value = "patientId", required = true, example = "33")
	@JsonProperty("PatientID")
	private Integer patientId;

	@ApiModelProperty(value = "deviceNumber", required = true, example = "0075E84")
	@JsonProperty("DeviceNumber")
	private String deviceNumber;

	@ApiModelProperty(value = "setupStatus", required = true, example = "")
	@JsonProperty("SetupStatus")
	private String setupStatus;

	@ApiModelProperty(value = "ssidList", required = true, example = "")
	@JsonProperty("SSIDList")
	private String ssidList;

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getSetupStatus() {
		return setupStatus;
	}

	public void setSetupStatus(String setupStatus) {
		this.setupStatus = setupStatus;
	}

	public String getSsidList() {
		return ssidList;
	}

	public void setSsidList(String ssidList) {
		this.ssidList = ssidList;
	}

}
