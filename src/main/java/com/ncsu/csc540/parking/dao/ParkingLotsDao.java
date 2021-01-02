package com.ncsu.csc540.parking.dao;

import com.ncsu.csc540.parking.domain.AppUser;
import com.ncsu.csc540.parking.domain.Citation;
import com.ncsu.csc540.parking.domain.CitationVO;
import com.ncsu.csc540.parking.domain.ParkingLots;
import com.ncsu.csc540.parking.mapper.AppUserMapper;
import com.ncsu.csc540.parking.mapper.CitationMapper;
import com.ncsu.csc540.parking.mapper.CitationVOMapper;
import com.ncsu.csc540.parking.mapper.ParkingLotsMapper;
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
public class ParkingLotsDao extends JdbcDaoSupport{
    @Autowired
    public ParkingLotsDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }


    public List<ParkingLots> findAllParking_Lots() {

        List<ParkingLots> lotlist = new ArrayList<ParkingLots>();
        String sql = "select p.NAME,ADDRESS,SPACE_COUNT,LISTAGG(ZONE_ID, ', ') WITHIN GROUP (ORDER BY p.NAME,ADDRESS,SPACE_COUNT) ZONE_IDs from PARKING_LOT p, ZONE_DESIGNATION ZD WHERE p.NAME=ZD.NAME GROUP BY p.NAME,ADDRESS,SPACE_COUNT";
        lotlist = getJdbcTemplate().query(sql,
                new ParkingLotsMapper());
        return lotlist;
    }

    public void addNewLot(ParkingLots parkinglots) {
        double V=0;

        if (parkinglots.getZone().equals("V")==true){
             V = parkinglots.getSpace_count();
        }
        String sql = "INSERT INTO PARKING_LOT (NAME, ADDRESS, SPACE_COUNT, VISITOR_SPACE_COUNT, BEGINNING_SPACE_NUMBER)"
                + " VALUES (?,?,?,?,?)";
        getJdbcTemplate().update(sql,parkinglots.getName(), parkinglots.getAddress(),
        parkinglots.getSpace_count(), V, parkinglots.getBeginning_space_number());

        sql = "INSERT INTO PARKING_SPACES_1(SPACE_NO,SPACE_TYPE,PARKING_LOT_NAME,ZONE,OCCUPIED) VALUES (?,?,?,?,?)";

        for (Long i = parkinglots.getBeginning_space_number(); i <= parkinglots.getSpace_count(); i++) {
                getJdbcTemplate().update(sql,i,"Regular", parkinglots.getName(),parkinglots.getZone(),0);

        }

        sql = "INSERT INTO ZONE_DESIGNATION (ZONE_ID, NAME) VALUES (?,?)";
        getJdbcTemplate().update(sql,parkinglots.getZone(),parkinglots.getName());
        }

    }




