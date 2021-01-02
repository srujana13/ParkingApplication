package com.ncsu.csc540.parking.mapper;

import com.ncsu.csc540.parking.domain.Vehicle;
import com.ncsu.csc540.parking.domain.VisitorPermit;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleMapper implements RowMapper<Vehicle> {
    public static final String BASE_SQL //
            = "select * from VEHICLE v";

    @Override
    public Vehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
        Vehicle vehicle = new Vehicle();

        vehicle.setManufacturer(rs.getString("MANUFACTURER"));
        vehicle.setModel(rs.getString("MODEL"));
        vehicle.setYear(rs.getString("YEAR"));
        vehicle.setColor(rs.getString("COLOR"));
        vehicle.setLicense_plate_no(rs.getString("LICENSE_PLATE_NO"));

        return vehicle;
    }
}