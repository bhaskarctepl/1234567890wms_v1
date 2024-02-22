package com.hillspet.wearables.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hillspet.wearables.dto.PetMobileAppConfig;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetMobileAppConfigResponse {

	private Map<Integer, List<PetMobileAppConfig>> mobileAppConfigs;

	private List<PetMobileAppConfig> petMobileAppConfigs;

	public Map<Integer, List<PetMobileAppConfig>> getMobileAppConfigs() {
		return mobileAppConfigs;
	}

	public void setMobileAppConfigs(Map<Integer, List<PetMobileAppConfig>> mobileAppConfigs) {
		this.mobileAppConfigs = mobileAppConfigs;
	}

	public List<PetMobileAppConfig> getPetMobileAppConfigs() {
		return petMobileAppConfigs;
	}

	public void setPetMobileAppConfigs(List<PetMobileAppConfig> petMobileAppConfigs) {
		this.petMobileAppConfigs = petMobileAppConfigs;
	}

}
