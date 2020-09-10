package com.myipl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myipl.api.request.IPLMatchWinnerRequest;
import com.myipl.api.response.APIReponse;
import com.myipl.service.IPLMatchWinnerService;
import com.myipl.service.LeaderboardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "Admin useful apis" })
@RestController
@RequestMapping("/admin")
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private IPLMatchWinnerService iplMatchWinnerService;
	@Autowired
	private LeaderboardService leaderboardService;

	@ApiOperation(value = "Save winners for the day,enter the match date in yyyy-mm-dd")
	@PostMapping(value = "/saveIPLMatchWinner", produces = "application/json")
	public APIReponse saveIPLMatchWinner(@RequestBody IPLMatchWinnerRequest iplMatchWinnerRequest) {
		APIReponse apiReponse = null;
		try {
			apiReponse = iplMatchWinnerService.saveMatchWinnerDetails(iplMatchWinnerRequest);
		} catch (Exception e) {
			apiReponse = new APIReponse("failure", e.getMessage());
		}
		return apiReponse;

	}

	@ApiOperation(value = "If cron job is not working compute leaderboard manually")
	@GetMapping(value = "/compute-leaderboard", produces = "application/json")
	public void computeLeaderBoard() {
		try {
			leaderboardService.computeLeaderBoard();
		} catch (Exception e) {
			logger.error("Exception executing LeaderBoard Job : " + e.getMessage(), e);
		}
	}
}
