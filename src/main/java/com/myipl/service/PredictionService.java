package com.myipl.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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

	@Autowired
	private PlayerPredictionRepository playerPredictionRepository;
	@Autowired
	private SchedulerRepository schedulerRepository;

	@Transactional
	public APIReponse savePredictionOfPlayer(PredictionRequest predictionRequest) {
		APIReponse response = new APIReponse();
		try {
			if (LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.of(2, 0))) {
				response.setAction("failure");
				response.setMessage("Predictions will be enabled from 2:00 AM to 2:00 PM");
			} else if (LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.of(14, 0))) {
				PlayerPrediction predPlayer = playerPredictionRepository.findByUserId(predictionRequest.getUserId());
				if (predPlayer != null) {
					if ((predPlayer.getMatch1() == null || predPlayer.getMatch1().trim().isEmpty())
							&& predictionRequest.getMatch1() != null) {
						Scheduler scheduler = schedulerRepository.findByDateAndMatch1(
								LocalDate.now(ZoneId.of("Asia/Kolkata")), predictionRequest.getMatch1());
						if (scheduler != null) {
							predPlayer.setMatch1(predictionRequest.getMatch1());
							playerPredictionRepository.save(predPlayer);
						} else {
							response.setAction("failure");
							response.setMessage("Invalid match or date");
						}
					} else if ((predPlayer.getMatch2() == null || predPlayer.getMatch2().trim().isEmpty())
							&& predictionRequest.getMatch2() != null) {
						Scheduler scheduler = schedulerRepository.findByDateAndMatch2(
								LocalDate.now(ZoneId.of("Asia/Kolkata")), predictionRequest.getMatch2());
						if (scheduler != null) {
							predPlayer.setMatch2(predictionRequest.getMatch2());
							playerPredictionRepository.save(predPlayer);
						} else {
							response.setAction("failure");
							response.setMessage("Invalid match or date");
						}
					} else {
						response.setAction("failure");
						response.setMessage("Your prediction has already been recorded");
					}
				} else {
					response.setAction("failure");
					response.setMessage("UserID does not exist");
				}
			} else {
				response.setAction("failure");
				response.setMessage("Oops! Looks like you missed the deadline of 2:00 PM");
			}
		} catch (RuntimeException e) {
			response = new APIReponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}

	public PredictionResponse getPredictions(String userId) {
		PredictionResponse response = null;
		try {
			response = new PredictionResponse();
			List<PredictionDetail> predictions = new ArrayList<PredictionDetail>();
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

	/*
	 * public void pointsUpdation() { int total=playerRepository.getLoosers(); }
	 */

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