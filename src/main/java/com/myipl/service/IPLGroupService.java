package com.myipl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.myipl.api.request.IPLGroupRequest;
import com.myipl.api.response.APIReponse;
import com.myipl.domain.entity.IPLGroup;
import com.myipl.repository.IPLGroupRepository;

@Service
public class IPLGroupService {

	@Autowired
	private IPLGroupRepository iplGroupRepository;

	public APIReponse updateGroup(@RequestBody IPLGroupRequest iplGroupRequest) {
		APIReponse response = new APIReponse();
		IPLGroup iplGroup = iplGroupRepository.findByGroupName(iplGroupRequest.getGroupName());
		if (null == iplGroup) {
			iplGroup = new IPLGroup();
			iplGroup.setGroupName(iplGroupRequest.getGroupName());
			iplGroup.setStatus(iplGroupRequest.getStatus());
			iplGroupRepository.save(iplGroup);
			response.setMessage("Group created");
		} else {
			iplGroup.setGroupName(iplGroupRequest.getGroupName());
			iplGroup.setStatus(iplGroupRequest.getStatus());
			iplGroupRepository.save(iplGroup);
			response.setMessage("Group updated");
		}
		return response;
	}

}