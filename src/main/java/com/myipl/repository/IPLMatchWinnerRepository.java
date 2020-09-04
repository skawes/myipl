package com.myipl.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myipl.domain.entity.IPLMatchWinner;

public interface IPLMatchWinnerRepository extends JpaRepository<IPLMatchWinner, Long> {

	IPLMatchWinner findByMatchDate(LocalDate todayMatchDate);

}
