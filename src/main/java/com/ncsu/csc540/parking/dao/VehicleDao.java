package com.ncsu.csc540.parking.dao;

import com.ncsu.csc540.parking.domain.Vehicle;
import com.ncsu.csc540.parking.mapper.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class VehicleDao extends JdbcDaoSupport {
    @Autowired
    public VehicleDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public void save(Vehicle vehicle) {

        String sql = "INSERT INTO VEHICLE (MANUFACTURER, MODEL, YEAR, COLOR, LICENSE_PLATE_NO) " +
                "VALUES (?,?,?,?,?)";
        getJdbcTemplate().update(sql,vehicle.getManufacturer(), vehicle.getModel(), vehicle.getYear(), vehicle.getColor(),
                vehicle.getLicense_plate_no());
    }

    public List<Vehicle> getVehicleByUnivid(Long univid) {

            String sql = "SELECT V.MANUFACTURER, V.LICENSE_PLATE_NO, V.COLOR, V.MODEL, V.YEAR FROM VEHICLE V, APP_USER A, NON_VISITOR_PERMIT N WHERE V.LICENSE_PLATE_NO=N.LICENSE_PLATE_NO AND N.UNIVID = A.UNIVID AND N.UNIVID=?";
            Object[] vehicleParams = new Object[] { univid };
            List<Vehicle> vehicles = getJdbcTemplate().query(sql,vehicleParams,new VehicleMapper());
            return vehicles;
        }

}
