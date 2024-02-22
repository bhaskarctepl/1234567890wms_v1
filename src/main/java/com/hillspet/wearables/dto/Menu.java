package com.hillspet.wearables.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu {

	private Integer menuId;
	private String menuName;
	private Integer parentMenuId;
	private String parentMenuName;
	private Integer menuActionId;
	private String menuActionName;
	private Integer mobileAppConfigId;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(Integer parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public String getParentMenuName() {
		return parentMenuName;
	}

	public void setParentMenuName(String parentMenuName) {
		this.parentMenuName = parentMenuName;
	}

	public Integer getMenuActionId() {
		return menuActionId;
	}

	public void setMenuActionId(Integer menuActionId) {
		this.menuActionId = menuActionId;
	}

	public String getMenuActionName() {
		return menuActionName;
	}

	public void setMenuActionName(String menuActionName) {
		this.menuActionName = menuActionName;
	}

	public Integer getMobileAppConfigId() {
		return mobileAppConfigId;
	}

	public void setMobileAppConfigId(Integer mobileAppConfigId) {
		this.mobileAppConfigId = mobileAppConfigId;
	}

}
