package com.ncsu.csc540.parking.dao;

import com.ncsu.csc540.parking.config.AppException;
import com.ncsu.csc540.parking.domain.ParkingSpace;
import com.ncsu.csc540.parking.mapper.ParkingSpaceMapper;

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
public class ParkingSpaceDao extends JdbcDaoSupport {
    @Autowired
    public ParkingSpaceDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public List<ParkingSpace> findAllParking_Space() {

        List<ParkingSpace> lotlist = new ArrayList<ParkingSpace>();
        String sql = ParkingSpaceMapper.BASE_SQL;
        lotlist = getJdbcTemplate().query(sql,
                new ParkingSpaceMapper());
        return lotlist;
    }

    public Integer findAvailableVisitorSpace(String lot_name, String space_type) {

        String sql = "SELECT SPACE_NO FROM PARKING_SPACES_1 WHERE OCCUPIED = 0 AND ZONE = 'V' AND PARKING_LOT_NAME = ? AND SPACE_TYPE = ?";
        Object[] params = new Object[] { lot_name, space_type };

        List<Integer> space_nos = getJdbcTemplate().queryForList(sql, params, Integer.class);
        if (space_nos.size() == 0){
            throw new AppException("No spaces available in this lot!");
        }
        else {

        return space_nos.get(0);
        }
    }

    public void saveOrUpdateTypetoSpace(ParkingSpace parkingspace) {

        String sql = "UPDATE PARKING_SPACES_1 SET SPACE_TYPE = ? WHERE SPACE_NO=? AND PARKING_LOT_NAME=?";
        getJdbcTemplate().update(sql,parkingspace.getSpace_type(), parkingspace.getSpace_no(), parkingspace.getParking_lot_name());
        }

    public void saveOrUpdateOccupied(ParkingSpace parkingspace) {
        String sql = "UPDATE PARKING_SPACES_1 SET OCCUPIED = ? WHERE SPACE_NO=? AND PARKING_LOT_NAME=?";
        getJdbcTemplate().update(sql,parkingspace.getOccupied(), parkingspace.getSpace_no(), parkingspace.getParking_lot_name());
    }
}








