package com.myipl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.myipl.domain.entity.IPLMatchWinner;

@Repository
public class IPLMatchWinnerRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final String GET_IPL_WINNER_DATE = "SELECT * FROM myipl.ipl_match_winner WHERE match_date = ? ";

	public IPLMatchWinner getIPLMatchWinnerForDate(LocalDate date) {
		IPLMatchWinner iplMatchWinner = (IPLMatchWinner) jdbcTemplate.queryForObject(GET_IPL_WINNER_DATE,
				new Object[] { date.toString() }, new RowMapper<IPLMatchWinner>() {
					@Override
					public IPLMatchWinner mapRow(ResultSet rs, int rownum) throws SQLException {
						IPLMatchWinner winner = new IPLMatchWinner();
						winner.setId(rs.getInt("id"));
						winner.setMatch1Winner(rs.getString("match_one_winner"));
						winner.setMatch2Winner(rs.getString("match_two_winner"));
						winner.setMatchDate(date);
						return winner;
					}
				});
		return iplMatchWinner;
	}

}
