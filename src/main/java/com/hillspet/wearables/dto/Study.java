package com.hillspet.wearables.dto;

import java.time.LocalDateTime;

public class Study {
	private int studyId;
	private String studyName;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private Boolean status;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public int getStudyId() {
		return studyId;
	}

	public void setStudyId(int studyId) {
		this.studyId = studyId;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("StudyName{");
		sb.append("studyId=").append(studyId);
		sb.append(", studyName='").append(studyName).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
