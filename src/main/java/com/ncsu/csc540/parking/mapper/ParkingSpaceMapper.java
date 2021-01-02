package com.ncsu.csc540.parking.mapper;
import com.ncsu.csc540.parking.domain.ParkingSpace;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingSpaceMapper implements RowMapper<ParkingSpace>{
    public static final String BASE_SQL //
    = "select * from PARKING_SPACES_1 ps";

    @Override
    public ParkingSpace mapRow(ResultSet rs, int rowNum) throws SQLException {

        ParkingSpace parkingspace = new ParkingSpace();

        parkingspace.setSpace_no(rs.getInt("SPACE_NO"));
        parkingspace.setParking_lot_name(rs.getString("PARKING_LOT_NAME"));
        parkingspace.setSpace_type(rs.getString("SPACE_TYPE"));
        parkingspace.setZone(rs.getString("ZONE"));
        parkingspace.setOccupied(rs.getInt("OCCUPIED"));

        return parkingspace;
    }

}

