package com.myipl.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myipl.api.request.LoginRequest;
import com.myipl.api.request.PredictionRequest;
import com.myipl.api.request.RegisterRequest;
import com.myipl.api.response.APIReponse;
import com.myipl.api.response.LoginResponse;
import com.myipl.service.LeaderboardService;
import com.myipl.service.PlayerService;

@RestController
@RequestMapping("/player")
public class PlayerController {

	private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

	@Autowired
	PlayerService playerService;
	@Autowired
	LeaderboardService leaderboardService;

	@RequestMapping(value = "/register", produces = "application/json", method = RequestMethod.POST)
	public APIReponse registerPlayer(@RequestBody RegisterRequest registerRequest, HttpServletRequest httpRequest,
			HttpServletResponse httResponse) {
		APIReponse response = null;
		try {
			logger.info("Register player : " + registerRequest.getContactNumber());
			if (registerRequest.getUserId() == null || registerRequest.getUserId().isEmpty()
					|| registerRequest.getPassword() == null || registerRequest.getPassword().isEmpty())
				return new APIReponse("failure", "username or password cannot be empty");
			response = playerService.registerPlayer(registerRequest);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = new APIReponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}

		return response;
	}

	@RequestMapping(value = "/login", produces = "application/json", method = RequestMethod.POST)
	public APIReponse loginPlayer(@RequestBody LoginRequest loginRequest, HttpServletRequest httpRequest,
			HttpServletResponse httResponse) {
		LoginResponse response = null;
		try {

			response = playerService.loginPlayer(loginRequest);
		} catch (Exception e) {
			response = new LoginResponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}

		return response;
	}

	@RequestMapping(value = "/prediction", produces = "application/json", method = RequestMethod.POST)
	public APIReponse predictionPlayer(@RequestBody PredictionRequest predictionRequest) {
		APIReponse response = null;
		try {

			response = playerService.savePredictionOfPlayer(predictionRequest);

		} catch (Exception e) {
			response = new APIReponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}

		return response;
	}

	@RequestMapping(value = "/predictions", produces = "application/json", method = RequestMethod.GET)
	public APIReponse getPredictions() {
		APIReponse response = null;
		try {
			response = playerService.getPredictions();
		} catch (Exception e) {
			response = new APIReponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/scheduler", produces = "application/json", method = RequestMethod.GET)
	public APIReponse getScheduler() {
		APIReponse response = null;
		try {
			response = playerService.getScheduler();
		} catch (Exception e) {
			response = new APIReponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/compute-leaderboard", produces = "application/json", method = RequestMethod.GET)
	public void computeLeaderBoard() {
		try {
			leaderboardService.computeLeaderBoard();
		} catch (Exception e) {
			logger.error("Exception executing LeaderBoard Job : " + e.getMessage(), e);
		}
	}

	@RequestMapping(value = "/leaderboard/{userId}", produces = "application/json", method = RequestMethod.GET)
	public APIReponse getLeaderBoard(@PathVariable("userId") String userId) {
		APIReponse response = null;
		try {
			response = playerService.getLeaderBoard(userId);
		} catch (Exception e) {
			response = new APIReponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}
}
