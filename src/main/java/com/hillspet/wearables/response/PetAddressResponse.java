package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.Address;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PetAddressResponse {

	private Address petAddress;

	private List<Address> petAddressList;

	public Address getPetAddress() {
		return petAddress;
	}

	public void setPetAddress(Address petAddress) {
		this.petAddress = petAddress;
	}

	public List<Address> getPetAddressList() {
		return petAddressList;
	}

	public void setPetAddressList(List<Address> petAddressList) {
		this.petAddressList = petAddressList;
	}

}
