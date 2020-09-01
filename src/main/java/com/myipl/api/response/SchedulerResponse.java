package com.myipl.api.response;

import java.util.List;

public class SchedulerResponse extends APIReponse{
	private static final long serialVersionUID = 1L;
	
	private List<SchedulerDetail> scheduler;

	public List<SchedulerDetail> getScheduler() {
		return scheduler;
	}

	public void setScheduler(List<SchedulerDetail> scheduler) {
		this.scheduler = scheduler;
	}

	
}
