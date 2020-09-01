package com.myipl.domain.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class IPLMatchWinner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String match1Winner;

	private String match2Winner;

	private LocalDate matchDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMatch1Winner() {
		return match1Winner;
	}

	public void setMatch1Winner(String match1Winner) {
		this.match1Winner = match1Winner;
	}

	public String getMatch2Winner() {
		return match2Winner;
	}

	public void setMatch2Winner(String match2Winner) {
		this.match2Winner = match2Winner;
	}

	public LocalDate getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(LocalDate matchDate) {
		this.matchDate = matchDate;
	}

}
