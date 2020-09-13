package com.myipl.api.request;

import java.io.Serializable;

public class IPLGroupRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String groupName;

	private boolean status;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
