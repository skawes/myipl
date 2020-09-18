package com.myipl.api.request;

import java.io.Serializable;
import java.time.LocalDate;

public class IPLMatchWinnerRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String match1Winner;

	private String match2Winner;

	private LocalDate matchDate;

	private boolean isComputeLeaderboard=false;

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

	public boolean isComputeLeaderboard() {
		return isComputeLeaderboard;
	}

	public void setComputeLeaderboard(boolean isComputeLeaderboard) {
		this.isComputeLeaderboard = isComputeLeaderboard;
	}

}
