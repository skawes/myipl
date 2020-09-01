package com.myipl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.myipl.domain.entity.IPLGroup;

@Repository
public class IPLGroupRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final String GET_ALL_IPL_GROUP = "SELECT * FROM myipl.ipl_group where status = 1";
	private static final String GET_GROUP_ID_BY_NAME = "SELECT * FROM myipl.ipl_group where group_name = ?";

	public List<IPLGroup> getAllIPLGroup() {
		List<IPLGroup> groups = jdbcTemplate.query(GET_ALL_IPL_GROUP, new RowMapper<IPLGroup>() {
			@Override
			public IPLGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
				IPLGroup group = new IPLGroup();
				group.setId(rs.getInt("id"));
				group.setGroupName(rs.getString("group_name"));
				return group;
			}
		});
		return groups;
	}

	public IPLGroup getGroupByName(String groupName) {
		IPLGroup group = (IPLGroup) jdbcTemplate.queryForObject(GET_GROUP_ID_BY_NAME, new Object[] { groupName },
				new RowMapper<IPLGroup>() {
					@Override
					public IPLGroup mapRow(ResultSet rs, int rownum) throws SQLException {
						IPLGroup group = new IPLGroup();
						group.setId(rs.getInt("id"));
						group.setGroupName(rs.getString("group_name"));
						return group;
					}
				});
		return group;
	}
}
