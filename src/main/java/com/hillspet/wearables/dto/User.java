package com.hillspet.wearables.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

	private Integer userId;
	private String email;
	private String secondaryEmail;
	private String firstName;
	private String lastName;
	@JsonProperty
	private String password;
	@JsonProperty
	private String newPassword;
	private String fullName;
	private String phoneNumber;
	private Integer status;
	private Integer petParentId;
	private Integer roleId;

	private List<Role> role;
	private Address address;
	private Integer createdBy;

	private Integer preferredWeightUnitId;
	private String preferredWeightUnit;
	private Integer preferredFoodRecUnitId;
	private String preferredFoodRecUnit;
	private String preferredFoodRecTime;

	public User() {

	}

	public User(User user) {
		this.status = user.status;
		this.email = user.email;
		this.userId = user.userId;
		this.firstName = user.firstName;
		this.lastName = user.lastName;
		this.password = user.password;
		this.role = user.role;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSecondaryEmail() {
		return secondaryEmail;
	}

	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public List<Role> getRole() {
		return role;
	}

	public void setRole(List<Role> role) {
		this.role = role;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getPreferredWeightUnitId() {
		return preferredWeightUnitId;
	}

	public void setPreferredWeightUnitId(Integer preferredWeightUnitId) {
		this.preferredWeightUnitId = preferredWeightUnitId;
	}

	public Integer getPreferredFoodRecUnitId() {
		return preferredFoodRecUnitId;
	}

	public void setPreferredFoodRecUnitId(Integer preferredFoodRecUnitId) {
		this.preferredFoodRecUnitId = preferredFoodRecUnitId;
	}

	public String getPreferredFoodRecTime() {
		return preferredFoodRecTime;
	}

	public void setPreferredFoodRecTime(String preferredFoodRecTime) {
		this.preferredFoodRecTime = preferredFoodRecTime;
	}

	public String getPreferredWeightUnit() {
		return preferredWeightUnit;
	}

	public void setPreferredWeightUnit(String preferredWeightUnit) {
		this.preferredWeightUnit = preferredWeightUnit;
	}

	public String getPreferredFoodRecUnit() {
		return preferredFoodRecUnit;
	}

	public void setPreferredFoodRecUnit(String preferredFoodRecUnit) {
		this.preferredFoodRecUnit = preferredFoodRecUnit;
	}

}
