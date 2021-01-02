package com.ncsu.csc540.parking.mapper;

import com.ncsu.csc540.parking.domain.AppUser;
import com.ncsu.csc540.parking.domain.Citation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CitationMapper implements RowMapper<Citation> {

    public static final String BASE_SQL //
            = "Select * From CITATION c ";

    @Override
    public Citation mapRow(ResultSet rs, int rowNum) throws SQLException {

        Citation citation = new Citation();
        citation.setCNo(rs.getLong("C_NO"));
        citation.setLicenseNumber(rs.getString("LICENSE_NUMBER"));
        citation.setModel(rs.getString("MODEL"));
        citation.setColor(rs.getString("COLOR"));
        citation.setCDate(rs.getTimestamp("C_DATE"));
        citation.setLot(rs.getString("LOT"));
        citation.setViolationId(rs.getInt("VIOLATION_ID"));
        citation.setDue(rs.getTimestamp("DUE"));
        citation.setStatus(rs.getString("STATUS"));

        return citation;
    }

}