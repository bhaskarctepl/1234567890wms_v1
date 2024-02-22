package com.hillspet.wearables.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author sgorle
 *
 */
@ApiModel(description = "Contains all information that needed to assign a device to pet", value = "AssignSensorRequest")
@JsonInclude(value = Include.NON_NULL)
public class AssignSensorRequest {

	@ApiModelProperty(value = "petId", required = true, example = "1")
	private Integer petId;

	@ApiModelProperty(value = "petParentId", required = true, example = "21")
	private Integer petParentId;

	@ApiModelProperty(value = "deviceNumber", required = true, example = "0101023")
	private String deviceNumber;

	@ApiModelProperty(value = "deviceType", required = true, example = "Sensor###AGL2")
	private String deviceType;

	@ApiModelProperty(value = "assignedDate", required = true, example = "2022-02-11")
	private LocalDate assignedDate;

	@ApiModelProperty(value = "setupStatus", example = "Setup Success")
	private String setupStatus;

	@ApiModelProperty(value = "ssidList", example = "87-34-23-43-34")
	private String ssidList;

	@ApiModelProperty(value = "oldDeviceNumber", example = "0108223")
	private String oldDeviceNumber;

	private int createdBy;

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public LocalDate getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(LocalDate assignedDate) {
		this.assignedDate = assignedDate;
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

	public String getOldDeviceNumber() {
		return oldDeviceNumber;
	}

	public void setOldDeviceNumber(String oldDeviceNumber) {
		this.oldDeviceNumber = oldDeviceNumber;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

}
