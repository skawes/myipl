package com.myipl.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myipl.api.request.PredictionRequest;
import com.myipl.api.response.APIReponse;
import com.myipl.api.response.PredictionDetail;
import com.myipl.api.response.PredictionResponse;
import com.myipl.domain.entity.PlayerPrediction;
import com.myipl.domain.entity.Scheduler;
import com.myipl.repository.PlayerPredictionRepository;
import com.myipl.repository.SchedulerRepository;

@Service
public class PredictionService {
	private static final Logger logger = LoggerFactory.getLogger(PredictionService.class);

	@Autowired
	private PlayerPredictionRepository playerPredictionRepository;
	@Autowired
	private SchedulerRepository schedulerRepository;

	@Transactional
	public APIReponse savePredictionOfPlayer(PredictionRequest predictionRequest) {
		if (LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.of(2, 0))) { // IF request comes before 2AM
			return new APIReponse("failure", "Predictions will be enabled from 2:00 AM to 2:00 PM");
		}
		if (LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.of(14, 0))) { // If request before after 2pm
			PlayerPrediction predPlayer = playerPredictionRepository.findByUserId(predictionRequest.getUserId());
			if (predPlayer != null) {
				if ((predPlayer.getMatch1() == null || predPlayer.getMatch1().trim().isEmpty())
						&& predictionRequest.getMatch1() != null) {
					return findByDateAndMatch(predPlayer, LocalDate.now(ZoneId.of("Asia/Kolkata")),
							predictionRequest.getMatch1(), true);
				} else if ((predPlayer.getMatch2() == null || predPlayer.getMatch2().trim().isEmpty())
						&& predictionRequest.getMatch2() != null) {
					return findByDateAndMatch(predPlayer, LocalDate.now(ZoneId.of("Asia/Kolkata")),
							predictionRequest.getMatch2(), false);
				}
				return new APIReponse("failure", "Your prediction has already been recorded");
			}
			return new APIReponse("failure", "UserID does not exist");
		}
		return new APIReponse("failure", "Oops! Looks like you missed the deadline of 2:00 PM"); // If request comes
																									// after 2pm
	}

	// set match prediction depending on the match1/match2
	private APIReponse findByDateAndMatch(PlayerPrediction predPlayer, LocalDate date, String match, boolean isMatch1) {
		Scheduler scheduler = isMatch1
				? schedulerRepository.findByDateAndMatch1OrMatch2(LocalDate.now(ZoneId.of("Asia/Kolkata")), match)
				: schedulerRepository.findByDateAndMatch1OrMatch2(LocalDate.now(ZoneId.of("Asia/Kolkata")), match);
		if (scheduler == null)
			return new APIReponse("failure", "Invalid match or date");
		if (isMatch1)
			predPlayer.setMatch1(match);
		else
			predPlayer.setMatch2(match);
		playerPredictionRepository.save(predPlayer);
		logger.info("Prediction saved " + predPlayer.getUserId());
		return new APIReponse("success", "Prediction Saved");
	}

	public PredictionResponse getPredictions(String userId) {
		PredictionResponse response = null;
		try {
			response = new PredictionResponse();
			List<PredictionDetail> predictions = new ArrayList<PredictionDetail>();
			// send prediction w.r.t userId
			if (LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.of(14, 0))
					&& LocalTime.now(ZoneId.of("Asia/Kolkata")).isAfter(LocalTime.of(2, 0))) {
				PlayerPrediction playerPrediction = playerPredictionRepository.findByUserId(userId);
				PredictionDetail predictionDetail = new PredictionDetail();
				predictionDetail.setUserId(playerPrediction.getUserId());
				predictionDetail.setMatch1(playerPrediction.getMatch1());
				predictionDetail.setMatch2(playerPrediction.getMatch2());
				predictions.add(predictionDetail);
				response.setPredictions(predictions);
				response.setAction("success");
				response.setMessage("Prediction of all the group members will be visible from 14:00");
				return response;
			}
			// send predictions of the whole group
			List<Object> predictionsFromDB = playerPredictionRepository.findPredictionsByGroup(userId);
			for (Object playerPrediction : predictionsFromDB) {
				PredictionDetail predictionDetail = new PredictionDetail();
				Object[] detail = (Object[]) playerPrediction;
				predictionDetail.setUserId(String.valueOf(detail[0]));
				if (detail[1] != null)
					predictionDetail.setMatch1(String.valueOf(detail[1]));
				if (detail[2] != null)
					predictionDetail.setMatch2(String.valueOf(detail[2]));
				predictions.add(predictionDetail);
			}
			response.setPredictions(predictions);
		} catch (RuntimeException e) {
			response = new PredictionResponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@Transactional
	public void savePoints(List<PredictionDetail> predictionDetails) {
		if (null == predictionDetails || predictionDetails.isEmpty())
			return;
		List<PlayerPrediction> playerPredictionsFromDb = playerPredictionRepository.findAll();
		if (playerPredictionsFromDb == null || playerPredictionsFromDb.isEmpty())
			return;
		// Update the points and set predictions for both the matches to null
		for (PredictionDetail predictionDetail : predictionDetails) {
			PlayerPrediction playerPrediction = playerPredictionsFromDb.stream()
					.filter(prediction -> prediction.getUserId().equals(predictionDetail.getUserId())).findFirst()
					.orElse(null);
			if (playerPrediction != null) {
				playerPrediction.setMatch1(null);
				playerPrediction.setMatch2(null);
				playerPrediction.setPoints(predictionDetail.getPoints());
			}
		}
		playerPredictionRepository.saveAll(playerPredictionsFromDb);
	}

}