package com.myipl.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.myipl.service.LeaderboardService;

@Component
public class LeaderboardScheduler {

	private static final Logger logger = LoggerFactory.getLogger(LeaderboardScheduler.class);

	@Autowired
	LeaderboardService leaderboardService;

	@Scheduled(cron = "0 0 3 * * *")
	public void computeLeaderBoard() {
		try {
			leaderboardService.computeLeaderBoard();
		} catch (Exception e) {
			logger.error("Exception executing LeaderBoard Job : " + e.getMessage(), e);
		}
	}

}
