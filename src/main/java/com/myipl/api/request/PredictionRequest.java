package com.myipl.api.request;

import java.io.Serializable;

public class PredictionRequest implements Serializable{
	private static final long serialVersionUID = 1L;
    private String userId;
    private String match1;
    private String match2;

    public String getUserId() {
	return userId;
      }

    public String getMatch1() {
		return match1;
	}

	public void setMatch1(String match1) {
		this.match1 = match1;
	}

	public String getMatch2() {
		return match2;
	}

	public void setMatch2(String match2) {
		this.match2 = match2;
	}

	public void setUserId(String userId) {
	this.userId = userId;
       }
   
   
 }
