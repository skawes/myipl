package com.myipl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.myipl.domain.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

	Player findByUserId(String userId);

	@Query(value = "SELECT p.user_id, FORMAT(pd.points,2) FROM myipl.player_prediction pd INNER JOIN myipl.player p on p.user_id = pd.user_id INNER JOIN myipl.iplgroup g ON g.id = p.group_id WHERE p.group_id = ( select p1.group_id  FROM myipl.player p1 where p1.user_id =:userId ) order by pd.points desc, p.user_id", nativeQuery = true)
	List<String[]> getLeaderBoardDetailForGroup(@Param("userId") String userId);

	@Query(value = "SELECT pd.match1, pd.match2, pd.user_id, pd.points,pd.id FROM myipl.player_prediction pd INNER JOIN myipl.player p on p.user_id = pd.user_id  WHERE p.group_id =:id", nativeQuery = true)
	List<Object> findPlayersByGroup(@Param("id") Long id);

}
