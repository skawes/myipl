package com.myipl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.myipl.api.request.PredictionRequest;
import com.myipl.api.response.LeaderBoardDetail;
//import in.myipl.api.response.LeaderboardDetail;
import com.myipl.api.response.PredictionDetail;
import com.myipl.api.response.SchedulerDetail;
import com.myipl.domain.entity.Player;
import com.myipl.domain.entity.PredPlayer;

@Repository
public class PlayerRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final String INSERT_PLAYER = "insert into myipl.players(p_name, p_username, p_password, p_contact, p_group_id) values(?, ?, ?, ?, ?)";
	private static final String GET_PLAYER_USING_USERID = "SELECT * FROM myipl.players  WHERE p_username = ? ";
	private static final String GET_ALL_PLAYERS_PREDICTIONS_WITHIN_GROUP = "SELECT pd.match1, pd.match2, p.p_username, pd.points FROM myipl.predictions pd INNER JOIN myipl.players p on p.p_username = pd.p_username  WHERE p.p_group_id = ? ";
	private static final String GET_PREDPLAYER_USING_USERID = "SELECT * FROM myipl.predictions WHERE p_username = ? ";
	private static final String INSERT_USERID = "insert into myipl.predictions(p_username) values(?)";
	private static final String UPDATE_PREDICTION1 = "UPDATE myipl.predictions SET Match1=? WHERE p_username=?";
	private static final String UPDATE_PREDICTION2 = "UPDATE myipl.predictions SET Match2=? WHERE p_username=?";
	private static final String GET_ALL_PREDICTIONS = "Select p_username,Match1,Match2 from  myipl.predictions";
	private static final String GET_SCHEDULE = "Select datee,match1,match2 from  myipl.scheduler";
	private static final String UPDATE_POINTS = "UPDATE myipl.predictions SET Match1= null, Match2 = null , points = ? WHERE p_username=? ";
	private static final String GET_LEADERBOARD_FOR_GROUP = "SELECT p.p_username, pd.points FROM myipl.predictions pd INNER JOIN myipl.players p on p.p_username = pd.p_username INNER JOIN myipl.ipl_group g ON g.id = p.p_group_id WHERE p.p_group_id = ( select p1.p_group_id  FROM myipl.players p1 where p1.p_username = ? ) order by pd.points, p.p_username ";

	public void savePlayer(Player player) {
		jdbcTemplate.update(INSERT_PLAYER, new Object[] { player.getName(), player.getUserId(), player.getPassword(),
				player.getContactNumber(), player.getGroupId() });
		jdbcTemplate.update(INSERT_USERID, player.getUserId());
	}

	public List<PredictionDetail> getPredictions() {
		List<PredictionDetail> predictionDetails = jdbcTemplate.query(GET_ALL_PREDICTIONS,
				new RowMapper<PredictionDetail>() {
					@Override
					public PredictionDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
						PredictionDetail predictionDetail = new PredictionDetail();
						predictionDetail.setUserId(rs.getString("p_username"));
						predictionDetail.setMatch1(rs.getString("Match1"));
						predictionDetail.setMatch2(rs.getString("Match2"));
						return predictionDetail;
					}
				});
		return predictionDetails;
	}

	public List<SchedulerDetail> getScheduler() {
		List<SchedulerDetail> schedulerDetail = jdbcTemplate.query(GET_SCHEDULE, new RowMapper<SchedulerDetail>() {
			@Override
			public SchedulerDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
				SchedulerDetail schedulerDetail = new SchedulerDetail();
				schedulerDetail.setDate(rs.getString("datee"));
				schedulerDetail.setMatch1(rs.getString("match1"));
				schedulerDetail.setMatch2(rs.getString("match2"));
				return schedulerDetail;
			}
		});
		return schedulerDetail;
	}

	/*
	 * public class PlayerMapper implements RowMapper<Player> {
	 * 
	 * @Override public Player mapRow(ResultSet rs, int rownum) throws SQLException
	 * { Player player = new Player(); player.setName(rs.getString("p_name"));
	 * player.setUserId(rs.getString("p_username"));
	 * player.setPassword(rs.getString("p_password"));
	 * player.setContactNumber(rs.getString("p_contact"));
	 * player.setGroupId(rs.getInt("p_group_id"));
	 * player.setGroupName(rs.getString("group_name")); return player; } }
	 */

	public List<PredictionDetail> getPlayersByGroup(int groupId) {
		List<PredictionDetail> playersPrediction = jdbcTemplate.query(GET_ALL_PLAYERS_PREDICTIONS_WITHIN_GROUP,
				new Object[] { groupId }, new RowMapper<PredictionDetail>() {
					@Override
					public PredictionDetail mapRow(ResultSet rs, int rownum) throws SQLException {
						PredictionDetail predictionDetail = new PredictionDetail();
						predictionDetail.setUserId(rs.getString("p_username"));
						predictionDetail.setMatch1(rs.getString("Match1"));
						predictionDetail.setMatch2(rs.getString("Match2"));
						predictionDetail.setPoints(rs.getDouble("points"));
						return predictionDetail;
					}
				});
		return playersPrediction;
	}

	public Player getPlayer(String userId) {
		Player player = (Player) jdbcTemplate.queryForObject(GET_PLAYER_USING_USERID, new Object[] { userId },
				new RowMapper<Player>() {
					@Override
					public Player mapRow(ResultSet rs, int rownum) throws SQLException {
						Player player = new Player();
						player.setName(rs.getString("p_name"));
						player.setUserId(rs.getString("p_username"));
						player.setPassword(rs.getString("p_password"));
						player.setContactNumber(rs.getString("p_contact"));
						player.setGroupId(rs.getInt("p_group_id"));
						return player;
					}
				});
		return player;
	}

	public void savePrediction1(PredictionRequest predictionRequest) {
		jdbcTemplate.update(UPDATE_PREDICTION1, predictionRequest.getMatch1(), predictionRequest.getUserId());
	}

	public void savePrediction2(PredictionRequest predictionRequest) {
		jdbcTemplate.update(UPDATE_PREDICTION2, predictionRequest.getMatch2(), predictionRequest.getUserId());
	}

	public class PredPlayerMapper implements RowMapper<PredPlayer> {
		@Override
		public PredPlayer mapRow(ResultSet rs, int rownum) throws SQLException {
			PredPlayer predPlayer = new PredPlayer();
			predPlayer.setUserId(rs.getString("p_username"));
			predPlayer.setMatch1(rs.getString("Match1"));
			predPlayer.setMatch2(rs.getString("Match2"));
			return predPlayer;
		}
	}

	public PredPlayer getPredPlayer(String userId) {

		PredPlayer predPlayer = (PredPlayer) jdbcTemplate.queryForObject(GET_PREDPLAYER_USING_USERID,
				new Object[] { userId }, new PredPlayerMapper());

		return predPlayer;

	}

	public void savePoints(List<PredictionDetail> predictionDetails) {
		if (null == predictionDetails || predictionDetails.isEmpty())
			return;
		for (PredictionDetail predictionDetail : predictionDetails) {
			jdbcTemplate.update(UPDATE_POINTS,
					new Object[] { predictionDetail.getPoints(), predictionDetail.getUserId() });
		}
	}

	public List<LeaderBoardDetail> getLeaderBoardDetailForGroup(String userId) {
		List<LeaderBoardDetail> leaderBoardDetails = jdbcTemplate.query(GET_LEADERBOARD_FOR_GROUP,
				new Object[] { userId }, new RowMapper<LeaderBoardDetail>() {
					@Override
					public LeaderBoardDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
						LeaderBoardDetail leaderBoardDetail = new LeaderBoardDetail();
						leaderBoardDetail.setRank(rowNum);
						leaderBoardDetail.setUserId(rs.getString("p_username"));
						leaderBoardDetail.setPoints(rs.getDouble("points"));
						return leaderBoardDetail;
					}
				});
		return leaderBoardDetails;
	}

}