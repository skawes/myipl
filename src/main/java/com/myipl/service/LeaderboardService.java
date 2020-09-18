package com.myipl.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myipl.api.response.IPLLeaderBoardResponse;
import com.myipl.api.response.LeaderBoardDetail;
import com.myipl.api.response.PredictionDetail;
import com.myipl.domain.entity.IPLGroup;
import com.myipl.domain.entity.IPLMatchWinner;
import com.myipl.repository.IPLGroupRepository;
import com.myipl.repository.IPLMatchWinnerRepository;
import com.myipl.repository.PlayerRepository;
import com.myipl.utility.Constants;

@Service
public class LeaderboardService {
	private static final Logger logger = LoggerFactory.getLogger(LeaderboardService.class);

	@Autowired
	private IPLGroupRepository iplGroupRepository;
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private IPLMatchWinnerRepository iplMatchWinnerRepository;
	@Autowired
	private PredictionService predictionService;
	@Autowired
	private EventAuditService eventAuditService;

	private static int FIXED_MAX_SCORE;

	public IPLLeaderBoardResponse getLeaderBoard(String userId) {
		IPLLeaderBoardResponse response = null;
		try {
			response = new IPLLeaderBoardResponse();
			List<LeaderBoardDetail> leaderBoardDetails = new ArrayList<LeaderBoardDetail>();
			List<String[]> leaderBoardDetailsFromDb = playerRepository.getLeaderBoardDetailForGroup(userId);
			int rank = 1;
			for (String[] detail : leaderBoardDetailsFromDb) {
				LeaderBoardDetail leaderBoardDetail = new LeaderBoardDetail();
				leaderBoardDetail.setRank(rank);
				leaderBoardDetail.setUserId(detail[0]);
				leaderBoardDetail.setPoints(Double.valueOf(detail[1]));
				leaderBoardDetails.add(leaderBoardDetail);
				rank++;
			}
			response.setLeaderBoardDetails(leaderBoardDetails);
		} catch (RuntimeException e) {
			response = new IPLLeaderBoardResponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@Transactional
	public void computeLeaderBoard() {
		LocalDate todayMatchDate = LocalDate.now(ZoneId.of("Asia/Kolkata")).minusDays(1);
		IPLMatchWinner iplMatchWinner = iplMatchWinnerRepository.findByMatchDate(todayMatchDate);
		if (null == iplMatchWinner || ((null == iplMatchWinner.getMatch1Winner()
				|| iplMatchWinner.getMatch1Winner().trim().isEmpty())
				&& (null == iplMatchWinner.getMatch2Winner() || iplMatchWinner.getMatch2Winner().trim().isEmpty())))
			return;

		List<IPLGroup> groups = iplGroupRepository.findByStatusTrue();
		if (null == groups || groups.isEmpty())
			return;
		FIXED_MAX_SCORE = getMaxScoreByMatchType(todayMatchDate);
		for (IPLGroup group : groups) {
			List<Object> playersPredictionsFromDb = playerRepository.findPlayersByGroup(group.getId());
			List<PredictionDetail> playersPredictions = new ArrayList<PredictionDetail>();
			for (Object object : playersPredictionsFromDb) {
				PredictionDetail predictionDetail = new PredictionDetail();
				Object[] detail = (Object[]) object;
				if (detail[0] != null)
					predictionDetail.setMatch1(String.valueOf(detail[0]));
				if (detail[1] != null)
					predictionDetail.setMatch2(String.valueOf(detail[1]));
				predictionDetail.setUserId(String.valueOf(detail[2]));
				if (detail[3] != null)
					predictionDetail.setPoints(Double.valueOf(String.valueOf(detail[3])));
				playersPredictions.add(predictionDetail);
			}
			if (null == playersPredictions || playersPredictions.isEmpty())
				continue;

			int totalPlayers = playersPredictions.size();

			// match 1
			if (null != iplMatchWinner.getMatch1Winner() && !iplMatchWinner.getMatch1Winner().trim().isEmpty()) {
				List<PredictionDetail> winnerMatch1 = playersPredictions.parallelStream()
						.filter(p -> iplMatchWinner.getMatch1Winner().equalsIgnoreCase(p.getMatch1()))
						.collect(Collectors.toList());
				if (totalPlayers != winnerMatch1.size() && totalPlayers != (totalPlayers - winnerMatch1.size())) {
					// some loser and some winner
					double pointsForEachMatch1Winner = (FIXED_MAX_SCORE * (totalPlayers - winnerMatch1.size()))
							/ winnerMatch1.size();
					for (PredictionDetail playerPD : playersPredictions) {
						if (iplMatchWinner.getMatch1Winner().equalsIgnoreCase(playerPD.getMatch1())) {
							playerPD.setPoints(playerPD.getPoints() + pointsForEachMatch1Winner);
						} else {
							playerPD.setPoints(playerPD.getPoints() - FIXED_MAX_SCORE);
						}
					}
				}
			}

			// match 2
			if (null != iplMatchWinner.getMatch2Winner() && !iplMatchWinner.getMatch2Winner().trim().isEmpty()) {
				List<PredictionDetail> winnerMatch2 = playersPredictions.parallelStream()
						.filter(p -> iplMatchWinner.getMatch2Winner().equalsIgnoreCase(p.getMatch2()))
						.collect(Collectors.toList());
				if (totalPlayers != winnerMatch2.size() && totalPlayers != (totalPlayers - winnerMatch2.size())) {
					// some loser and some winner
					double pointsForEachMatch2Winner = (FIXED_MAX_SCORE * (totalPlayers - winnerMatch2.size()))
							/ winnerMatch2.size();
					for (PredictionDetail playerPD : playersPredictions) {
						if (iplMatchWinner.getMatch2Winner().equalsIgnoreCase(playerPD.getMatch2())) {
							playerPD.setPoints(playerPD.getPoints() + pointsForEachMatch2Winner);
						} else {
							playerPD.setPoints(playerPD.getPoints() - FIXED_MAX_SCORE);
						}
					}
				}
			}
			// save in the audit table before updating
			eventAuditService.savePredictionEvent(playersPredictions, group.getId(), todayMatchDate);
			// save new points
			predictionService.savePoints(playersPredictions);
			logger.info("Leaderboard computed for :" + "groupName : " + group.getGroupName() + LocalDateTime.now());
		}
	}

	private int getMaxScoreByMatchType(LocalDate todayMatchDate) {
		int max_score = 30;
		// for the league matches max_score is 30,if match is play offs then max_score
		// is 100 and for finals max_score is 200
		if (todayMatchDate.isAfter(LocalDate.parse(Constants.LAST_LEAGUE_MATCH))
				&& todayMatchDate.isBefore(LocalDate.parse(Constants.FINAL_MATCH))) {
			max_score = 100;
		} else if (todayMatchDate.isEqual(LocalDate.parse(Constants.FINAL_MATCH)))
			max_score = 200;
		return max_score;
	}

}
