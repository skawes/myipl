package com.myipl.controller;

import java.time.LocalDate;
import java.time.ZoneId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
import com.myipl.service.PredictionService;
import com.myipl.service.SchedulerService;
import com.myipl.utility.Constants;

import io.swagger.annotations.Api;

@Api(tags = { "Player related apis" })
@RestController
@RequestMapping("/player")
public class PlayerController {
	private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
	@Autowired
	private PlayerService playerService;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private LeaderboardService leaderboardService;
	@Autowired
	private PredictionService predictionService;

	@RequestMapping(value = "/register", produces = "application/json", method = RequestMethod.POST)
	public APIReponse registerPlayer(@RequestBody RegisterRequest registerRequest, HttpServletRequest httpRequest,
			HttpServletResponse httResponse) {
		APIReponse response = null;
		try {
			// registration closes after 1 week of ipl start date
			if (LocalDate.now(ZoneId.of("Asia/Kolkata")).isAfter(LocalDate.parse(Constants.REGISTRATION_END_DATE)))
				return new APIReponse("failure", "Sorry, registration closed");
			if (registerRequest.getUserId() == null || registerRequest.getUserId().isEmpty()
					|| registerRequest.getPassword() == null || registerRequest.getPassword().isEmpty())
				return new APIReponse("failure", "username or password cannot be empty");
			response = playerService.registerPlayer(registerRequest);
		} catch (Exception e) {
			logger.error("Registration error: " + e.getMessage());
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
			logger.error("Login error: " + e.getMessage());
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
			response = predictionService.savePredictionOfPlayer(predictionRequest);
		} catch (Exception e) {
			logger.error("Save prediction error: " + e.getMessage());
			response = new APIReponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}

		return response;
	}

	@RequestMapping(value = "/predictions/{userId}", produces = "application/json", method = RequestMethod.GET)
	public APIReponse getPredictions(@PathVariable("userId") String userId) {
		APIReponse response = null;
		try {
			response = predictionService.getPredictions(userId);
		} catch (Exception e) {
			response = new APIReponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@Cacheable(value = "scheduler")
	@RequestMapping(value = "/scheduler", produces = "application/json", method = RequestMethod.GET)
	public APIReponse getScheduler() {
		APIReponse response = null;
		try {
			response = schedulerService.getScheduler();
		} catch (Exception e) {
			response = new APIReponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/schedulerForToday", produces = "application/json", method = RequestMethod.GET)
	public APIReponse schedulerForToday() {
		APIReponse response = null;
		try {
			response = schedulerService.getSchedulerForToday();
		} catch (Exception e) {
			response = new APIReponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/leaderboard/{userId}", produces = "application/json", method = RequestMethod.GET)
	public APIReponse getLeaderBoard(@PathVariable("userId") String userId) {
		APIReponse response = null;
		try {
			response = leaderboardService.getLeaderBoard(userId);
		} catch (Exception e) {
			response = new APIReponse();
			response.setAction("failure");
			response.setMessage(e.getMessage());
		}
		return response;
	}

}
