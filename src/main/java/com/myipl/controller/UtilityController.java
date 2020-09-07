package com.myipl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myipl.api.response.APIReponse;
import com.myipl.service.UtilityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "Utility related apis" })
@RestController
@RequestMapping("/utility")
public class UtilityController {

	@Autowired
	private UtilityService 	utilityService;

	@ApiOperation(value = "Get current date-time in yyyy-MM-dd HH:mm")
	@RequestMapping(value = "/getCurrentDateTime", method = RequestMethod.GET, produces = "application/json")
	public APIReponse getCurrentTime() {
		APIReponse response = new APIReponse();
		response.setMessage(utilityService.getCurrentDateTime());
		return response;
	}
}