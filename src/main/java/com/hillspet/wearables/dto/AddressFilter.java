package com.hillspet.wearables.dto;

import javax.ws.rs.QueryParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Contains all information that needed to validate user address", value = "AddressFilter")
public class AddressFilter {

	@ApiModelProperty(value = "address1", required = true, example = "street no 1")
	@QueryParam("address1")
	private String address1;

	@ApiModelProperty(value = "address2", required = false, example = "Main Street")
	@QueryParam("address2")
	private String address2;

	@ApiModelProperty(value = "city", required = true, example = "New York")
	@QueryParam("city")
	private String city;

	@ApiModelProperty(value = "state", required = true, example = "New York")
	@QueryParam("state")
	private String state;

	@ApiModelProperty(value = "country", required = true, example = "US")
	@QueryParam("country")
	private String country;

	@ApiModelProperty(value = "zipCode", required = true, example = "12345")
	@QueryParam("zipCode")
	private String zipCode;

	public AddressFilter() {

	}

	public AddressFilter(String address1, String address2, String city, String state, String country, String zipCode) {
		super();
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipCode = zipCode;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
