package com.ncsu.csc540.parking.mapper;
import com.ncsu.csc540.parking.domain.ZoneDesignation;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ZoneDesignationMapper implements RowMapper<ZoneDesignation>  {
    public static final String BASE_SQL //
    = "select * from ZONE_DESIGNATION zd";

    @Override
    public ZoneDesignation mapRow(ResultSet rs, int rowNum) throws SQLException {

        ZoneDesignation zondedesignation = new ZoneDesignation();

        zondedesignation.setZone(rs.getString("ZONE_IDs"));
        zondedesignation.setLot_name(rs.getString("NAME"));

        return zondedesignation;
    }
}




