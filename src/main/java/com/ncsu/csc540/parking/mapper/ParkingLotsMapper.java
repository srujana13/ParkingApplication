package com.ncsu.csc540.parking.mapper;

import com.ncsu.csc540.parking.domain.ParkingLots;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;



public class ParkingLotsMapper implements RowMapper<ParkingLots> {
    public static final String BASE_SQL //
            = "select * from PARKING_LOT p";

    @Override
    public ParkingLots mapRow(ResultSet rs, int rowNum) throws SQLException {

        ParkingLots parkinglots = new ParkingLots();

        parkinglots.setName(rs.getString("NAME"));
        parkinglots.setAddress(rs.getString("ADDRESS"));
        parkinglots.setSpace_count(rs.getLong("SPACE_COUNT"));
        parkinglots.setZone(rs.getString("ZONE_IDs"));
        // parkinglots.setBeginning_space_number(rs.getLong("BEGINNING_SPACE_NUMBER"));

        return parkinglots;
    }
}
