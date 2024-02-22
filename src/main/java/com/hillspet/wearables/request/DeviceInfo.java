package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(value = Include.NON_NULL)
public class DeviceInfo {

	@ApiModelProperty(value = "sensorNumber", required = false, example = "1100E87")
	@JsonProperty("SensorNumber")
	private String sensorNumber;

	@ApiModelProperty(value = "deviceType", required = false, example = "sensor")
	@JsonProperty("DeviceType")
	private String deviceType;

	@ApiModelProperty(value = "deviceAddDate", required = false, example = "2023-10-20")
	@JsonProperty("DeviceAddDate")
	private String deviceAddDate;

	public String getSensorNumber() {
		return sensorNumber;
	}

	public void setSensorNumber(String sensorNumber) {
		this.sensorNumber = sensorNumber;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceAddDate() {
		return deviceAddDate;
	}

	public void setDeviceAddDate(String deviceAddDate) {
		this.deviceAddDate = deviceAddDate;
	}

}
