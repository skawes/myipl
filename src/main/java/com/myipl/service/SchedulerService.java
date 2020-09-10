package com.myipl.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myipl.api.response.SchedulerDetail;
import com.myipl.api.response.SchedulerResponse;
import com.myipl.domain.entity.Scheduler;
import com.myipl.repository.SchedulerRepository;

@Service
public class SchedulerService {

	@Autowired
	private SchedulerRepository schedulerRepository;

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

	public SchedulerResponse getSchedulerForToday() {
		SchedulerResponse response = null;
		LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Kolkata"));
		try {
			response = new SchedulerResponse();
			List<SchedulerDetail> schedulerDetails = new ArrayList<SchedulerDetail>();
			List<Scheduler> schedulerFromDb = schedulerRepository.findByDate(currentDate);
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
	
}