package com.myipl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myipl.api.response.APIReponse;
import com.myipl.api.response.IPLMatchWinnerResponse;
import com.myipl.domain.entity.IPLMatchWinner;
import com.myipl.repository.IPLMatchWinnerRepository;

@Service
public class IPLMatchWinnerService {
	@Autowired
	private IPLMatchWinnerRepository iplMatchWinnerRepository;

	public APIReponse saveMatchWinnerDetails(IPLMatchWinnerResponse iplMatchWinnerResponse) {
		IPLMatchWinner iplMatchWinner = iplMatchWinnerRepository.findByMatchDate(iplMatchWinnerResponse.getMatchDate());
		if (iplMatchWinner != null) {
			iplMatchWinner.setMatch1Winner(iplMatchWinnerResponse.getMatch1Winner());
			iplMatchWinner.setMatch2Winner(iplMatchWinnerResponse.getMatch2Winner());
			iplMatchWinnerRepository.save(iplMatchWinner);
			return new APIReponse("success", "Match winner updated");
		}
		iplMatchWinner = new IPLMatchWinner();
		iplMatchWinner.setMatchDate(iplMatchWinnerResponse.getMatchDate());
		iplMatchWinner.setMatch1Winner(iplMatchWinnerResponse.getMatch1Winner());
		iplMatchWinner.setMatch2Winner(iplMatchWinnerResponse.getMatch2Winner());
		iplMatchWinnerRepository.save(iplMatchWinner);
		return new APIReponse("success", "Match winner saved");

	}
}
