package com.myipl.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.myipl.domain.entity.Scheduler;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

	public List<Scheduler> findByDate(LocalDate currentDate);
	
	@Query(value = "SELECT * FROM scheduler s WHERE date=:date AND (match1=:teamName OR match2=:teamName)", nativeQuery = true)
	public Scheduler findByDateAndMatch1OrMatch2(LocalDate date, String teamName);
	
	public Scheduler findByDateAndMatch1AndMatch2(LocalDate date, String team1, String team2);
	
	public Scheduler findByDateAndMatch1(LocalDate date, String teamName);
	
	public Scheduler findByDateAndMatch2(LocalDate date, String teamName);

}
