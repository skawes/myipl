package com.myipl.api.response;

import java.util.List;

public class PredictionResponse extends APIReponse {
	private static final long serialVersionUID = 1L;

	private List<PredictionDetail> predictions;

	public List<PredictionDetail> getPredictions() {
		return predictions;
	}

	public void setPredictions(List<PredictionDetail> predictions) {
		this.predictions = predictions;
	}
}
