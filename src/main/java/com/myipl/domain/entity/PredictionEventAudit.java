package com.myipl.domain.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PredictionEventAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String userId;
	private String match1;
	private String match2;
	private Double points;
	private LocalDate predictionDate;
	private Long groupId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMatch1() {
		return match1;
	}

	public void setMatch1(String match1) {
		this.match1 = match1;
	}

	public String getMatch2() {
		return match2;
	}

	public void setMatch2(String match2) {
		this.match2 = match2;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public LocalDate getPredictionDate() {
		return predictionDate;
	}

	public void setPredictionDate(LocalDate predictionDate) {
		this.predictionDate = predictionDate;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
