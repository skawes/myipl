package com.myipl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myipl.api.request.IPLGroupRequest;
import com.myipl.api.request.IPLMatchWinnerRequest;
import com.myipl.api.request.LoginRequest;
import com.myipl.api.request.SchedulerRequest;
import com.myipl.api.response.APIReponse;
import com.myipl.service.IPLGroupService;
import com.myipl.service.IPLMatchWinnerService;
import com.myipl.service.LeaderboardService;
import com.myipl.service.PlayerService;
import com.myipl.service.SchedulerService;

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
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private IPLGroupService iplGroupService;
	@Autowired
	private CacheManager cacheManager;

	@ApiOperation(value = "Save winners for the day,enter the match date in yyyy-mm-dd")
	@PostMapping(value = "/saveIPLMatchWinner", produces = "application/json")
	@CacheEvict(value = "scheduler", allEntries = true)
	public APIReponse saveIPLMatchWinner(@RequestBody IPLMatchWinnerRequest iplMatchWinnerRequest) {
		APIReponse apiReponse = null;
		try {
			apiReponse = iplMatchWinnerService.saveMatchWinnerDetails(iplMatchWinnerRequest);
		} catch (Exception e) {
			apiReponse = new APIReponse("failure", e.getMessage());
		}
		return apiReponse;
	}

	@ApiOperation(value = "Update fixture, enter the match date in \"yyyy-mm-dd\" or null if TBD")
	@PostMapping(value = "/updateMatchDetails", produces = "application/json")
	@CacheEvict(value = "scheduler", allEntries = true)
	public APIReponse updateMatchDetails(@RequestBody SchedulerRequest schedulerRequest) {
		APIReponse apiReponse = null;
		try {
			apiReponse = schedulerService.updateMatchDetails(schedulerRequest);
		} catch (Exception e) {
			apiReponse = new APIReponse("failure", e.getMessage());
		}
		return apiReponse;
	}

	@ApiOperation(value = "Change Password")
	@PostMapping(value = "/changePassword", produces = "application/json")
	public APIReponse changePassword(@RequestBody LoginRequest loginRequest) {
		APIReponse apiReponse = null;
		try {
			apiReponse = playerService.changePassword(loginRequest);
		} catch (Exception e) {
			apiReponse = new APIReponse("failure", e.getMessage());
		}
		return apiReponse;
	}

	@ApiOperation(value = "Create or Modify group")
	@PostMapping(value = "/updateGroup", produces = "application/json")
	public APIReponse updateGroup(@RequestBody IPLGroupRequest iplGroupRequest) {
		APIReponse apiReponse = null;
		try {
			apiReponse = iplGroupService.updateGroup(iplGroupRequest);
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

	@ApiOperation(value = "Clear all caches. Created for backup only.")
	@GetMapping(value = "/evictAllCaches", produces = "application/json")
	public APIReponse evictAllCaches() {
		cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
		logger.info("All the caches evicted");
		return new APIReponse();
	}

}
