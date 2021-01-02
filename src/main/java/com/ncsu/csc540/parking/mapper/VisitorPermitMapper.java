package com.ncsu.csc540.parking.mapper;
import com.ncsu.csc540.parking.domain.VisitorPermit;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VisitorPermitMapper implements RowMapper<VisitorPermit> {
    public static final String BASE_SQL //
            = "select * from VISITOR_PERMIT vp";

    @Override
    public VisitorPermit mapRow(ResultSet rs, int rowNum) throws SQLException {
        VisitorPermit visitorPermit = new VisitorPermit();

        visitorPermit.setPermit_id(rs.getString("PERMIT_ID"));
        visitorPermit.setLicense_plate_no(rs.getString("LICENSE_PLATE_NO"));
        visitorPermit.setStart_time(rs.getTimestamp("START_TIME"));
        visitorPermit.setExp_time(rs.getTimestamp("EXP_TIME"));
        visitorPermit.setZone_id(rs.getString("ZONE_ID"));
        visitorPermit.setDuration(rs.getInt("DURATION"));
        visitorPermit.setSpace_type(rs.getString("SPACE_TYPE"));
        visitorPermit.setOverage(rs.getLong("OVERAGE"));
        visitorPermit.setSpace_no(rs.getInt("SPACE_NO"));
        visitorPermit.setLot_name(rs.getString("LOT_NAME"));
        visitorPermit.setContact_no(rs.getString("CONTACT_NO"));

        return visitorPermit;
    }
}



