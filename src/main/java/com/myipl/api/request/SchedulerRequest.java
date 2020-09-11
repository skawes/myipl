package com.myipl.api.request;

import java.io.Serializable;
import java.time.LocalDate;

public class SchedulerRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private LocalDate oldMatchDate;
	private String oldMatch1;
	private String oldMatch2;
	private LocalDate newMatchDate;
	private String newMatch1;
	private String newMatch2;

	public LocalDate getOldMatchDate() {
		return oldMatchDate;
	}

	public void setOldMatchDate(LocalDate oldMatchDate) {
		this.oldMatchDate = oldMatchDate;
	}

	public String getOldMatch1() {
		return oldMatch1;
	}

	public void setOldMatch1(String oldMatch1) {
		this.oldMatch1 = oldMatch1;
	}

	public String getOldMatch2() {
		return oldMatch2;
	}

	public void setOldMatch2(String oldMatch2) {
		this.oldMatch2 = oldMatch2;
	}

	public LocalDate getNewMatchDate() {
		return newMatchDate;
	}

	public void setNewMatchDate(LocalDate newMatchDate) {
		this.newMatchDate = newMatchDate;
	}

	public String getNewMatch1() {
		return newMatch1;
	}

	public void setNewMatch1(String newMatch1) {
		this.newMatch1 = newMatch1;
	}

	public String getNewMatch2() {
		return newMatch2;
	}

	public void setNewMatch2(String newMatch2) {
		this.newMatch2 = newMatch2;
	}

}
