package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppVersion {

	private String appVersion;
	private Boolean isForceUpdate;
	private String appOperatingSystem;
	private String versionDescription;

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Boolean getIsForceUpdate() {
		return isForceUpdate;
	}

	public void setIsForceUpdate(Boolean isForceUpdate) {
		this.isForceUpdate = isForceUpdate;
	}

	public String getAppOperatingSystem() {
		return appOperatingSystem;
	}

	public void setAppOperatingSystem(String appOperatingSystem) {
		this.appOperatingSystem = appOperatingSystem;
	}

	public String getVersionDescription() {
		return versionDescription;
	}

	public void setVersionDescription(String versionDescription) {
		this.versionDescription = versionDescription;
	}

}
