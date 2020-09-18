//package com.myipl.scheduler;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.myipl.service.LeaderboardService;
//
//@Component
//public class ScheduledTasks {
//	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
//
//	@Autowired
//	private LeaderboardService leaderboardService;
//
//	/**
//	 * This method is scheduled to compute the leader board at 1:30 a.m everyday
//	 */
//	@Scheduled(cron = "0 30 1 * * ?", zone = "IST")
//	public void computeLeaderBoard() {
//		try {
//			leaderboardService.computeLeaderBoard();
//		} catch (Exception e) {
//			logger.error("Exception executing LeaderBoard Job : " + e.getMessage(), e);
//		}
//	}
//
//}
