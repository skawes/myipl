package com.myipl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myipl.api.response.APIReponse;
import com.myipl.api.response.IPLMatchWinnerResponse;
import com.myipl.service.IPLMatchWinnerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "Admin useful apis" })
@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private IPLMatchWinnerService iplMatchWinnerService;

	@ApiOperation(value = "Save winners for the day,enter the match date in yyyy-mm-dd")
	@PostMapping
	public APIReponse saveIPLMatchWinner(@RequestBody IPLMatchWinnerResponse iplMatchWinnerResponse) {
		APIReponse apiReponse = null;
		try {
			apiReponse = iplMatchWinnerService.saveMatchWinnerDetails(iplMatchWinnerResponse);
		} catch (Exception e) {
			apiReponse = new APIReponse("failure", e.getMessage());
		}
		return apiReponse;

	}
}
