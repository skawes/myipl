package com.myipl.api.response;

import java.util.Date;

public class SchedulerDetail {
	private Date date;

	private String match1;

	private String match2;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

}
