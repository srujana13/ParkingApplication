package com.ncsu.csc540.parking.mapper;
import com.ncsu.csc540.parking.domain.NonVisitorPermit;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NonVisitorPermitMapper implements RowMapper<NonVisitorPermit> {
    public static final String BASE_SQL //
    = "select * from NON_VISITOR_PERMIT nvp";

    @Override
    public NonVisitorPermit mapRow(ResultSet rs, int rowNum) throws SQLException {

        NonVisitorPermit nonVisitorPermit = new NonVisitorPermit();

        nonVisitorPermit.setPermit_id(rs.getString("PERMIT_ID"));
        nonVisitorPermit.setLicense_plate_no(rs.getString("LICENSE_PLATE_NO"));
        nonVisitorPermit.setStart_time(rs.getTimestamp("START_TIME"));
        nonVisitorPermit.setExp_time(rs.getTimestamp("EXP_TIME"));
        nonVisitorPermit.setZone_id(rs.getString("ZONE_ID"));
        nonVisitorPermit.setSpace_type(rs.getString("SPACE_TYPE"));
        nonVisitorPermit.setUnivid(rs.getLong("UNIVID"));
        return nonVisitorPermit;
    }
}



