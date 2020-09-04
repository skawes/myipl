package com.myipl.api.response;

import java.io.Serializable;

public class APIReponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String action = "success";

	private String message;

	public APIReponse() {
		super();
	}

	public APIReponse(String action, String message) {
		super();
		this.action = action;
		this.message = message;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
