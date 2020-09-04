package com.myipl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myipl.api.response.APIReponse;

import io.swagger.annotations.Api;

@Api(tags = { "Health check api" })
@RestController
public class HealthController {

	@RequestMapping(value = "/health-check", method = RequestMethod.GET)
	public APIReponse registerPlayer() {
		APIReponse response = new APIReponse();
		response.setAction("success");
		return response;
	}

}
