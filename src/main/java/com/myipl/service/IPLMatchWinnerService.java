package com.myipl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myipl.api.request.IPLMatchWinnerRequest;
import com.myipl.api.response.APIReponse;
import com.myipl.domain.entity.IPLMatchWinner;
import com.myipl.repository.IPLMatchWinnerRepository;

@Service
public class IPLMatchWinnerService {

	@Autowired
	private IPLMatchWinnerRepository iplMatchWinnerRepository;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private LeaderboardService leaderboardService;

	public APIReponse saveMatchWinnerDetails(IPLMatchWinnerRequest iplMatchWinnerRequest) {
		String message = null;
		IPLMatchWinner iplMatchWinner = iplMatchWinnerRepository.findByMatchDate(iplMatchWinnerRequest.getMatchDate());
		if (iplMatchWinner != null) {
			iplMatchWinner.setMatch1Winner(iplMatchWinnerRequest.getMatch1Winner());
			iplMatchWinner.setMatch2Winner(iplMatchWinnerRequest.getMatch2Winner());
			iplMatchWinnerRepository.saveAndFlush(iplMatchWinner);
			message = "Match winner updated";
		} else {
			iplMatchWinner = new IPLMatchWinner();
			iplMatchWinner.setMatchDate(iplMatchWinnerRequest.getMatchDate());
			iplMatchWinner.setMatch1Winner(iplMatchWinnerRequest.getMatch1Winner());
			iplMatchWinner.setMatch2Winner(iplMatchWinnerRequest.getMatch2Winner());
			iplMatchWinnerRepository.saveAndFlush(iplMatchWinner);
			message = "Match winner saved";
		}
		schedulerService.updateWinnerForScheduler(iplMatchWinnerRequest.getMatchDate(),
				iplMatchWinner.getMatch1Winner());
		if (null != iplMatchWinnerRequest.getMatch2Winner())
			schedulerService.updateWinnerForScheduler(iplMatchWinnerRequest.getMatchDate(),
					iplMatchWinner.getMatch2Winner());
		// call the compute leaderboard after the match winners are set if the flag is
		// true
		if (iplMatchWinnerRequest.isComputeLeaderboard())
			leaderboardService.computeLeaderBoard();
		return new APIReponse("success", message);
	}

}