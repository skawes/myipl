package com.myipl.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myipl.api.response.PredictionDetail;
import com.myipl.domain.entity.PredictionEventAudit;
import com.myipl.repository.EventAuditRepository;

@Service
public class EventAuditService {
	@Autowired
	private EventAuditRepository eventAuditRepository;

	public void savePredictionEvent(List<PredictionDetail> playersPredictions, Long groupId, LocalDate todayMatchDate) {
		List<PredictionEventAudit> predictionEventAudits = new ArrayList<PredictionEventAudit>();
		for (PredictionDetail predictionDetail : playersPredictions) {
			PredictionEventAudit predictionEventAudit = new PredictionEventAudit();
			predictionEventAudit.setUserId(predictionDetail.getUserId());
			predictionEventAudit.setMatch1(predictionDetail.getMatch1());
			predictionEventAudit.setPoints(predictionDetail.getPoints());
			predictionEventAudit.setGroupId(groupId);
			predictionEventAudit.setPredictionDate(todayMatchDate);
			predictionEventAudits.add(predictionEventAudit);
		}
		eventAuditRepository.saveAll(predictionEventAudits);
	}

}
