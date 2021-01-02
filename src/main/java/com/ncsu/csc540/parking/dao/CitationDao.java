package com.ncsu.csc540.parking.dao;

import com.ncsu.csc540.parking.domain.Citation;
import com.ncsu.csc540.parking.domain.CitationVO;
import com.ncsu.csc540.parking.domain.SampleQueryModel;
import com.ncsu.csc540.parking.mapper.CitationMapper;
import com.ncsu.csc540.parking.mapper.CitationVOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CitationDao extends JdbcDaoSupport {
    @Autowired
    public CitationDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public Citation findCitationById(Long c_no) {

        String sql = CitationMapper.BASE_SQL + " where c.C_NO = ? ";
        Object[] params = new Object[] {c_no};
        CitationMapper mapper = new CitationMapper();
        try {
            Citation citation = this.getJdbcTemplate().queryForObject(sql, params, mapper);
            return citation;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }
    public List<CitationVO> findAllCitations() {

        List<CitationVO> citList = new ArrayList<CitationVO>();
        String sql = CitationMapper.BASE_SQL + " JOIN VIOLATION_FEE VF on c.VIOLATION_ID = VF.ID";
        citList = getJdbcTemplate().query(sql,
                new CitationVOMapper());
        return citList;
    }

    public void saveOrUpdate(Citation citation) {
        if (citation.getCNo() != null) {
            // update
            String sql = "UPDATE CITATION SET LICENSE_NUMBER  =?, MODEL=?, COLOR=?, "
                    + "C_DATE=?, LOT=?,VIOLATION_ID=?,DUE=?,STATUS=? WHERE C_NO=?";
            getJdbcTemplate().update(sql, citation.getLicenseNumber(), citation.getModel(),
                    citation.getColor(), citation.getCDate(), citation.getLot(),
                    citation.getViolationId(),citation.getDue(),citation.getStatus(),
                    citation.getCNo());
        } else {
            // insert
            String sql = "INSERT INTO CITATION (LICENSE_NUMBER, MODEL, COLOR, C_DATE, LOT,  VIOLATION_ID, DUE, STATUS)"
                    + " VALUES (?,?,?,?,?,?,?,?)";
            getJdbcTemplate().update(sql,citation.getLicenseNumber(), citation.getModel(),
                    citation.getColor(), citation.getCDate(), citation.getLot(),
                    citation.getViolationId(),citation.getDue(),citation.getStatus());
        }

    }

    public List<String> findCarsInViolation() {
        String sql = "SELECT distinct(LICENSE_NUMBER) FROM CITATION WHERE STATUS = 'UNPAID'";
        List<String> violations = getJdbcTemplate().queryForList(sql,String.class);
        return violations;

    }

    public List<SampleQueryModel> findCitationPerLot(String from, String to) {
        String sql = "SELECT LOT, COUNT(*) FROM CITATION WHERE C_DATE >= to_timestamp(?, 'yyyy-mm-dd') AND C_DATE <= to_timestamp(?, 'yyyy-mm-dd') GROUP BY LOT";
        Object[] params = new Object[] {from, to};
         return getJdbcTemplate().query(sql, params, new RowMapper<SampleQueryModel>() {
             public SampleQueryModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                 SampleQueryModel actor = new SampleQueryModel();
                 actor.setF1(rs.getString(1));
                 actor.setF2(rs.getString(2));
                 return actor;
             }
         });
    }
    public List<SampleQueryModel> findRevenuePerDay(String startMonth) {
        String sql = "SELECT to_char(DUE, 'yyyy-mm-dd'),SUM(FEE)  FROM CITATION,VIOLATION_FEE VF WHERE VIOLATION_ID=VF.ID AND STATUS='PAID' AND to_char(DUE,'yyyy-mm')=? GROUP BY to_char(DUE, 'yyyy-mm-dd')";
        Object[] params = new Object[] {startMonth};
        return getJdbcTemplate().query(sql, params, new RowMapper<SampleQueryModel>() {
            public SampleQueryModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                SampleQueryModel actor = new SampleQueryModel();
                actor.setF1(rs.getString(1));
                actor.setF2(rs.getString(2));
                return actor;
            }
        });
    }
}
