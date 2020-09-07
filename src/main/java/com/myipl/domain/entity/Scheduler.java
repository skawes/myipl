package com.myipl.domain.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.myipl.enums.IPLTeamName;

@Entity
public class Scheduler {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;

	private String match1;

	private String match2;

	@Enumerated(EnumType.STRING)
	private IPLTeamName winner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMatch1() {
		return match1;
	}

	public void setMatch1(String match1) {
		this.match1 = match1;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getMatch2() {
		return match2;
	}

	public void setMatch2(String match2) {
		this.match2 = match2;
	}

	public IPLTeamName getWinner() {
		return winner;
	}

	public void setWinner(IPLTeamName winner) {
		this.winner = winner;
	}

}
