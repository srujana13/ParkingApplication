package com.ncsu.csc540.parking.dao;



import com.ncsu.csc540.parking.domain.ZoneDesignation;

import com.ncsu.csc540.parking.mapper.ZoneDesignationMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class ZoneDesignationsDao extends JdbcDaoSupport {
    @Autowired
    public ZoneDesignationsDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public List<ZoneDesignation> findAllZone_Designations() {

        List<ZoneDesignation> lotlist = new ArrayList<ZoneDesignation>();
        String sql = "select NAME,LISTAGG(ZONE_ID, ', ') WITHIN GROUP (ORDER BY NAME) ZONE_IDs from ZONE_DESIGNATION ZD GROUP BY NAME";
        lotlist = getJdbcTemplate().query(sql,
                new ZoneDesignationMapper());
        return lotlist;
    }
    public List<ZoneDesignation> findAllZone_DesignationPairs() {

        List<ZoneDesignation> lotlist = new ArrayList<ZoneDesignation>();
        String sql = "select NAME,ZONE_ID AS ZONE_IDs from ZONE_DESIGNATION";
        lotlist = getJdbcTemplate().query(sql,
                new ZoneDesignationMapper());
        return lotlist;
    }

    public void saveOrUpdateZoneDesignations(ZoneDesignation zonedesignation) {
        String sql1 = "UPDATE PARKING_SPACES_1 SET ZONE = ? WHERE  PARKING_LOT_NAME= ? AND SPACE_NO BETWEEN ? AND ?";
        getJdbcTemplate().update(sql1,zonedesignation.getZone(),zonedesignation.getLot_name(),zonedesignation.getFrom(),zonedesignation.getTo());

        String sql = "INSERT INTO ZONE_DESIGNATION (NAME, ZONE_ID) VALUES (?,?)";
        getJdbcTemplate().update(sql,zonedesignation.getLot_name(), zonedesignation.getZone());
        }
}

