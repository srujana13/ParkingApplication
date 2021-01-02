package com.ncsu.csc540.parking.mapper;

import com.ncsu.csc540.parking.domain.CitationVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CitationVOMapper implements RowMapper<CitationVO> {

public static final String BASE_SQL //
        = "Select * From CITATION c ";

@Override
public CitationVO mapRow(ResultSet rs, int rowNum) throws SQLException {

        CitationVO citation = new CitationVO();
        citation.setCNo(rs.getLong("C_NO"));
        citation.setLicenseNumber(rs.getString("LICENSE_NUMBER"));
        citation.setModel(rs.getString("MODEL"));
        citation.setColor(rs.getString("COLOR"));
        citation.setCDate(rs.getDate("C_DATE"));
        citation.setLot(rs.getString("LOT"));
        citation.setViolationCategory(rs.getString("CATEGORY"));
        citation.setFee(rs.getFloat("FEE"));
        citation.setDue(rs.getTimestamp("DUE"));
        citation.setStatus(rs.getString("STATUS"));

        return citation;
    }
}