package com.myipl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.myipl.domain.entity.PlayerPrediction;

public interface PlayerPredictionRepository extends JpaRepository<PlayerPrediction, Long> {

	PlayerPrediction findByUserId(String userId);

	@Query(value = "SELECT p.user_id, pd.match1,pd.match2 FROM myipl.player_prediction pd INNER JOIN myipl.player p on p.user_id = pd.user_id INNER JOIN myipl.iplgroup g ON g.id = p.group_id WHERE p.group_id = ( select p1.group_id  FROM myipl.player p1 where p1.user_id =:userId ) order by p.user_id", nativeQuery = true)
	List<Object> findPredictionsByGroup(@Param("userId") String userId);

}
