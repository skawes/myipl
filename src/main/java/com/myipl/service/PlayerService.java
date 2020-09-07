package com.myipl.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myipl.api.request.LoginRequest;
import com.myipl.api.request.PredictionRequest;
import com.myipl.api.request.RegisterRequest;
import com.myipl.api.response.APIReponse;
import com.myipl.api.response.IPLLeaderBoardResponse;
import com.myipl.api.response.LeaderBoardDetail;
//import com.myipl.api.response.LeaderboardResponse;
import com.myipl.api.response.LoginResponse;
import com.myipl.api.response.PredictionDetail;
//import com.myipl.api.response.PredictionDetail;
import com.myipl.api.response.PredictionResponse;
import com.myipl.api.response.SchedulerDetail;
import com.myipl.api.response.SchedulerResponse;
import com.myipl.domain.entity.IPLGroup;
import com.myipl.domain.entity.Player;
import com.myipl.domain.entity.PlayerPrediction;
import com.myipl.domain.entity.Scheduler;
import com.myipl.repository.IPLGroupRepository;
import com.myipl.repository.PlayerRepository;
import com.myipl.repository.PlayerPredictionRepository;
import com.myipl.repository.SchedulerRepository;

@Service
public class PlayerService {

	@Autowired
	PlayerRepository playerRepository;
	@Autowired
	PlayerPredictionRepository playerPredictionRepository;
	@Autowired
	IPLGroupRepository iplGroupRepository;
	@Autowired
	SchedulerRepository schedulerRepository;

	public APIReponse registerPlayer(RegisterRequest registerRequest) {
		// validate group name;
		if (null == registerRequest.getGroupName() || registerRequest.getGroupName().trim().isEmpty()) {
			return new APIReponse("failure", "Group Name cannot empty.");
		}
		IPLGroup iplGroup = iplGroupRepository.findByGroupName(registerRequest.getGroupName());
		if (null == iplGroup)
			return new APIReponse("failure", "Group Name does not exist.");
		Player playerFromDb = playerRepository.findByUserId(registerRequest.getUserId());
		if (playerFromDb != null)
			return new APIReponse("failure", "username already exists");
		Player player = new Player();
		player.setName(registerRequest.getName());
		player.setUserId(registerRequest.getUserId());
		player.setPassword(registerRequest.getPassword());
		player.setContactNumber(registerRequest.getContactNumber());
		player.setGroupId(iplGroup);
		// Save the players entry in prediction table also
		PlayerPrediction playerPrediction = new PlayerPrediction();
		playerPrediction.setUserId(registerRequest.getUserId());
		playerPredictionRepository.save(playerPrediction);
		playerRepository.save(player);
		return new APIReponse("success", "Registration successful");
	}

	public LoginResponse loginPlayer(LoginRequest loginRequest) {
		LoginResponse response = null;
		try {
			// step 1:
			// TO DO validate user id and password cannot be null
			/*
			 * if(loginRequest.getUserId()==null) { response=new LoginResponse();
			 * response.setAction("failure");
			 * response.setErrorMessage("UserID cannot be null"); }
			 */

			// step 2 : retrieve and check if user exist
			Player player = playerRepository.findByUserId(loginRequest.getUserId());
			if (player == null) {
				// TO DO return with error that user does not exist
				response = new LoginResponse();
				response.setAction("failure");
				response.setMessage("UserID does not exist");
			}

			// step 3 : is password given correct
			else if (!loginRequest.getPassword().equals(player.getPassword())) {
				response = new LoginResponse();
				response.setAction("failure");
				response.setMessage("Incorrect password");

			}

			else// step 4 : here means user exist and password matches so return
				// success
			{
				response = new LoginResponse();
				response.setName(player.getName());
				response.setUserId(player.getUserId());
			}

		} catch (RuntimeException e) {
			response = new LoginResponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}

	public APIReponse savePredictionOfPlayer(PredictionRequest predictionRequest) {
		APIReponse response = null;
		try {
			PlayerPrediction predPlayer = playerPredictionRepository.findByUserId(predictionRequest.getUserId());
			if ((predPlayer.getMatch1() == null || predPlayer.getMatch1().trim().isEmpty())
					&& predictionRequest.getMatch1() != null) {
				predPlayer.setMatch1(predictionRequest.getMatch1());
				playerPredictionRepository.save(predPlayer);
				response = new APIReponse();
				response.setAction("success");
			} else if ((predPlayer.getMatch2() == null || predPlayer.getMatch2().trim().isEmpty())
					&& predictionRequest.getMatch2() != null) {
				predPlayer.setMatch2(predictionRequest.getMatch2());
				playerPredictionRepository.save(predPlayer);
				response = new APIReponse();
				response.setAction("success");
			} else {
				response = new APIReponse();
				response.setAction("failure");
				response.setMessage("Your prediction has already been recorded");
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
	public SchedulerResponse getScheduler() {
		SchedulerResponse response = null;
		try {
			response = new SchedulerResponse();
			List<SchedulerDetail> schedulerDetails = new ArrayList<SchedulerDetail>();
			List<Scheduler> schedulerFromDb = schedulerRepository.findAll();
			for (Scheduler scheduler : schedulerFromDb) {
				SchedulerDetail schedulerDetail = new SchedulerDetail();
				schedulerDetail.setDate(scheduler.getDate());
				schedulerDetail.setMatch1(scheduler.getMatch1());
				schedulerDetail.setMatch2(scheduler.getMatch2());
				if (null != scheduler.getWinner())
					schedulerDetail.setWinner(scheduler.getWinner().toString());
				schedulerDetails.add(schedulerDetail);
			}
			response.setScheduler(schedulerDetails);
		} catch (RuntimeException e) {
			response = new SchedulerResponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}

	public IPLLeaderBoardResponse getLeaderBoard(String userId) {
		IPLLeaderBoardResponse response = null;
		try {
			response = new IPLLeaderBoardResponse();
			List<LeaderBoardDetail> leaderBoardDetails = new ArrayList<LeaderBoardDetail>();
			List<Object> leaderBoardDetailsFromDb = playerRepository.getLeaderBoardDetailForGroup(userId);
			int rank = 1;
			for (Object object : leaderBoardDetailsFromDb) {
				LeaderBoardDetail leaderBoardDetail = new LeaderBoardDetail();
				leaderBoardDetail.setRank(rank);
				Object[] detail = (Object[]) object;
				leaderBoardDetail.setUserId(String.valueOf(detail[0]));
				if (detail[1] != null)
					leaderBoardDetail.setPoints(Double.valueOf(String.valueOf(detail[1])));
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