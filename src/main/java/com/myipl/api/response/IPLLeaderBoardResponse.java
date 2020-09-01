package com.myipl.api.response;

import java.util.List;

public class IPLLeaderBoardResponse extends APIReponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<LeaderBoardDetail> leaderBoardDetails;

	public List<LeaderBoardDetail> getLeaderBoardDetails() {
		return leaderBoardDetails;
	}

	public void setLeaderBoardDetails(List<LeaderBoardDetail> leaderBoardDetails) {
		this.leaderBoardDetails = leaderBoardDetails;
	}

}
