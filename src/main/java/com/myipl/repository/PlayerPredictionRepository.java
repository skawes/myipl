package com.myipl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myipl.domain.entity.PlayerPrediction;

public interface PlayerPredictionRepository extends JpaRepository<PlayerPrediction, Long> {

	PlayerPrediction findByUserId(String userId);

}
