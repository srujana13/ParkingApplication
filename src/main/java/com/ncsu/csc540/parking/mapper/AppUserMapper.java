package com.ncsu.csc540.parking.mapper;

import com.ncsu.csc540.parking.domain.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppUserMapper implements RowMapper<AppUser> {

    public static final String BASE_SQL //
            = "Select * From App_User u ";

    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {

        String userName = rs.getString("User_Name");
        String encrytedPassword = rs.getString("Encryted_Password");
        Long univid = rs.getLong("univid");

        return new AppUser(userName, encrytedPassword,univid);
    }

}