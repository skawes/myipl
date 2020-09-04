package com.myipl.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myipl.api.response.PredictionDetail;
import com.myipl.domain.entity.IPLGroup;
import com.myipl.domain.entity.IPLMatchWinner;
import com.myipl.repository.IPLGroupRepository;
import com.myipl.repository.IPLMatchWinnerRepository;
import com.myipl.repository.PlayerRepository;

@Service
public class LeaderboardService {

	@Autowired
	IPLGroupRepository iplGroupRepository;
	@Autowired
	PlayerRepository playerRepository;
	@Autowired
	IPLMatchWinnerRepository iplMatchWinnerRepository;
	@Autowired
	PlayerService playerService;

	private static final int FIXED_MAX_SCORE = 30;

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

			// save new points
			playerService.savePoints(playersPredictions);

		}

	}

}
