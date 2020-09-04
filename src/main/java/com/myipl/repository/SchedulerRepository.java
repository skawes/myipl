package com.myipl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myipl.domain.entity.Scheduler;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

}
