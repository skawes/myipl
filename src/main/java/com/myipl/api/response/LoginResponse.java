package com.myipl.api.response;

public class LoginResponse extends APIReponse {

	private static final long serialVersionUID = 1L;

	private String name;

	private String userId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
