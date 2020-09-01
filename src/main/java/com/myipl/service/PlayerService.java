package com.myipl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.myipl.api.request.LoginRequest;
import com.myipl.api.request.PredictionRequest;
import com.myipl.api.request.RegisterRequest;
import com.myipl.api.response.APIReponse;
import com.myipl.api.response.IPLLeaderBoardResponse;
//import com.myipl.api.response.LeaderboardResponse;
import com.myipl.api.response.LoginResponse;
//import com.myipl.api.response.PredictionDetail;
import com.myipl.api.response.PredictionResponse;
import com.myipl.api.response.SchedulerResponse;
import com.myipl.domain.entity.IPLGroup;
import com.myipl.domain.entity.Player;
import com.myipl.domain.entity.PredPlayer;
import com.myipl.exception.ValidationException;
import com.myipl.repository.IPLGroupRepository;
import com.myipl.repository.PlayerRepository;

@Service
public class PlayerService {

	@Autowired
	PlayerRepository playerRepository;
	@Autowired
	IPLGroupRepository iplGroupRepository;

	public void registerPlayer(RegisterRequest registerRequest) {

		try {

			// validate group name;
			if (null == registerRequest.getGroupName() || registerRequest.getGroupName().trim().isEmpty())
				throw new ValidationException("Group Name cannot empty.");
			IPLGroup iplGroup = iplGroupRepository.getGroupByName(registerRequest.getGroupName());
			if (null == iplGroup)
				throw new ValidationException("Group Name does not exist.");
			Player player = new Player();
			player.setName(registerRequest.getName());
			player.setUserId(registerRequest.getUserId());
			player.setPassword(registerRequest.getPassword());
			player.setContactNumber(registerRequest.getContactNumber());
			player.setGroupId(iplGroup.getId());

			playerRepository.savePlayer(player);
		} catch (DataAccessException e) {
			System.out.println(" Exception occured in saving : " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
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
			Player player = playerRepository.getPlayer(loginRequest.getUserId());
			if (player == null) {
				// TO DO return with error that user does not exist
				response = new LoginResponse();
				response.setAction("failure");
				response.setErrorMessage("UserID does not exist");
			}

			// step 3 : is password given correct
			else if (!loginRequest.getPassword().equals(player.getPassword())) {
				response = new LoginResponse();
				response.setAction("failure");
				response.setErrorMessage("Incorrect password");

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
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}

	public APIReponse predictionPlayer(PredictionRequest predictionRequest) {
		APIReponse response = null;
		try {
			PredPlayer predPlayer = playerRepository.getPredPlayer(predictionRequest.getUserId());
			if ((predPlayer.getMatch1() == null || predPlayer.getMatch1().trim().isEmpty())
					&& predictionRequest.getMatch1() != null) {
				playerRepository.savePrediction1(predictionRequest);
				response = new APIReponse();
				response.setAction("success");
			} else if ((predPlayer.getMatch2() == null || predPlayer.getMatch2().trim().isEmpty())
					&& predictionRequest.getMatch2() != null) {
				playerRepository.savePrediction2(predictionRequest);
				response = new APIReponse();
				response.setAction("success");
			} else {
				response = new APIReponse();
				response.setAction("failure");
				response.setErrorMessage("Your prediction has already been recorded");
			}

		} catch (RuntimeException e) {
			response = new APIReponse();
			response.setAction("failure");
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}

	public PredictionResponse getPredictions() {
		PredictionResponse response = null;
		try {

			response = new PredictionResponse();
			response.setPredictions(playerRepository.getPredictions());
		} catch (RuntimeException e) {
			response = new PredictionResponse();
			response.setAction("failure");
			response.setErrorMessage(e.getMessage());
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
			response.setScheduler(playerRepository.getScheduler());
		} catch (RuntimeException e) {
			response = new SchedulerResponse();
			response.setAction("failure");
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}

	public IPLLeaderBoardResponse getLeaderBoard(String userId) {
		IPLLeaderBoardResponse response = null;
		try {
			response = new IPLLeaderBoardResponse();
			response.setLeaderBoardDetails(playerRepository.getLeaderBoardDetailForGroup(userId));
		} catch (RuntimeException e) {
			response = new IPLLeaderBoardResponse();
			response.setAction("failure");
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}

}