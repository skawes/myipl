package com.myipl.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myipl.domain.entity.Scheduler;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

	public List<Scheduler> findByDate(LocalDate currentDate);

}
