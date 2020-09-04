package com.myipl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myipl.domain.entity.IPLGroup;

public interface IPLGroupRepository extends JpaRepository<IPLGroup, Long> {

	IPLGroup findByGroupName(String groupName);

	List<IPLGroup> findByStatusTrue();

}
