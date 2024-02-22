package com.hillspet.wearables.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all the information that needed to onboard a pet", value = "PetOnboardingRequest")
@JsonInclude(value = Include.NON_NULL)
public class PetOnboardingRequest {

	@ApiModelProperty(value = "petInfo", required = true)
	@JsonProperty("About")
	private PetInfo about;

	@ApiModelProperty(value = "plan", required = true)
	@JsonProperty("Plan")
	private PlanInfo plan;

	@ApiModelProperty(value = "device", required = true)
	@JsonProperty("Device")
	private DeviceInfo device;

	@ApiModelProperty(value = "client", required = true)
	@JsonProperty("Client")
	private ClientInfo client;

	@ApiModelProperty(value = "billing", required = true)
	@JsonProperty("Billing")
	private BillingInfo billing;

	public PetInfo getAbout() {
		return about;
	}

	public void setAbout(PetInfo about) {
		this.about = about;
	}

	public PlanInfo getPlan() {
		return plan;
	}

	public void setPlan(PlanInfo plan) {
		this.plan = plan;
	}

	public DeviceInfo getDevice() {
		return device;
	}

	public void setDevice(DeviceInfo device) {
		this.device = device;
	}

	public ClientInfo getClient() {
		return client;
	}

	public void setClient(ClientInfo client) {
		this.client = client;
	}

	public BillingInfo getBilling() {
		return billing;
	}

	public void setBilling(BillingInfo billing) {
		this.billing = billing;
	}

}
