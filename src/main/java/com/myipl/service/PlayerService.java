package com.myipl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myipl.api.request.LoginRequest;
import com.myipl.api.request.RegisterRequest;
import com.myipl.api.response.APIReponse;
import com.myipl.api.response.LoginResponse;
import com.myipl.domain.entity.IPLGroup;
import com.myipl.domain.entity.Player;
import com.myipl.domain.entity.PlayerPrediction;
import com.myipl.repository.IPLGroupRepository;
import com.myipl.repository.PlayerPredictionRepository;
import com.myipl.repository.PlayerRepository;

@Service
public class PlayerService {

	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private PlayerPredictionRepository playerPredictionRepository;
	@Autowired
	private IPLGroupRepository iplGroupRepository;

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
	
}