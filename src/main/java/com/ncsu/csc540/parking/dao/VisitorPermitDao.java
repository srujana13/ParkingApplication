package com.ncsu.csc540.parking.dao;

import com.ncsu.csc540.parking.config.AppException;
import com.ncsu.csc540.parking.domain.NonVisitorPermit;
import com.ncsu.csc540.parking.domain.ParkingSpace;
import com.ncsu.csc540.parking.domain.SampleQueryModel;
import com.ncsu.csc540.parking.domain.VisitorPermit;
import com.ncsu.csc540.parking.mapper.NonVisitorPermitMapper;
import com.ncsu.csc540.parking.mapper.VisitorPermitMapper;

import com.ncsu.csc540.parking.utils.AppUtils;
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
import java.util.Calendar;
import java.util.List;

@Repository
@Transactional
public class VisitorPermitDao extends JdbcDaoSupport {
    @Autowired
    public VisitorPermitDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    @Autowired
    private ParkingSpaceDao parkingSpaceDao;

    public VisitorPermit findVisitorPermitById(String permitId) {

        String sql = VisitorPermitMapper.BASE_SQL + " where vp.PERMIT_ID = ? ";
        Object[] params = new Object[] {permitId};
        System.out.println("Permit id is........");
        System.out.println(permitId);
        VisitorPermitMapper mapper = new VisitorPermitMapper();
        try {
            VisitorPermit permit = this.getJdbcTemplate().queryForObject(sql, params, mapper);
            return permit;
        } catch (EmptyResultDataAccessException e) {
            System.out.println("its in the catch...");
            return null;
        }
    }

    public List<VisitorPermit> findAllPermit() {
        System.out.println("Inside VisitorPermitDao");
        List<VisitorPermit> visitorPermits = new ArrayList<VisitorPermit>();
        String sql = VisitorPermitMapper.BASE_SQL;
        visitorPermits = getJdbcTemplate().query(sql,
                new VisitorPermitMapper());
        return visitorPermits;
    }

    public void saveOrUpdateTimeOverage(VisitorPermit visitorPermit) {
        String sql = "UPDATE VISITOR_PERMIT SET OVERAGE = ? WHERE PERMIT_ID=?";
        getJdbcTemplate().update(sql,visitorPermit.getOverage(), visitorPermit.getPermit_id());
    }

    public void issuePermit(VisitorPermit visitorPermit) {

        String lot_name = visitorPermit.getLot_name();
        Integer duration = visitorPermit.getDuration();
        String space_type = visitorPermit.getSpace_type();
        String license_plate_no = visitorPermit.getLicense_plate_no();
        String contact_no = visitorPermit.getContact_no();
        String permit_id = AppUtils.generatePermitID("V");
        java.sql.Timestamp start_time = visitorPermit.getStart_time();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, duration);
        java.util.Date exp_date = cal.getTime();
        java.sql.Timestamp exp_time = new java.sql.Timestamp(exp_date.getTime());

        String sqlForVehicle = "SELECT COUNT(*) FROM VEHICLE WHERE LICENSE_PLATE_NO = ?";
        Object[] vehicleParams = new Object[] { license_plate_no };
        if (getJdbcTemplate().queryForObject(sqlForVehicle, vehicleParams, Integer.class) == 0){
            throw new AppException("Vehicle Information not saved. Please save the vehicle information before you request for permit");
        }


        String sql = "SELECT SPACE_NO FROM PARKING_SPACES_1 WHERE OCCUPIED = 0 AND ZONE = 'V' AND PARKING_LOT_NAME = ? AND SPACE_TYPE = ?";
        Object[] params = new Object[] { lot_name, space_type };

        List<Integer> space_nos = getJdbcTemplate().queryForList(sql, params, Integer.class);

        if (space_nos.size() == 0){
            throw new AppException("No spaces available in this lot!");
        }
        else
        {
            String sql2 = "INSERT INTO VISITOR_PERMIT (PERMIT_ID, LOT_NAME, DURATION, SPACE_TYPE, LICENSE_PLATE_NO, START_TIME, EXP_TIME, SPACE_NO, ZONE_ID, CONTACT_NO) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
            Object[] params1 = {permit_id, lot_name, duration, space_type, license_plate_no, start_time, exp_time, space_nos.get(0), "V", contact_no};
            getJdbcTemplate().update(sql2, params1);

            ParkingSpace parkingSpace = new ParkingSpace();
            parkingSpace.setParking_lot_name(lot_name);
            parkingSpace.setSpace_no(space_nos.get(0));
            parkingSpace.setSpace_type(space_type);
            parkingSpace.setOccupied(1);
            parkingSpace.setZone("V");

            parkingSpaceDao.saveOrUpdateOccupied(parkingSpace);
        }
    }

    public List<SampleQueryModel> findPermits(String lot, String from, String to) {
        String sql = "SELECT SPACE_TYPE,COUNT(*) FROM VISITOR_PERMIT\n" +
                "WHERE LOT_NAME=? AND START_TIME >=  to_timestamp(?, 'yyyy-mm-dd')\n" +
                "  AND EXP_TIME<= to_timestamp(?, 'yyyy-mm-dd') GROUP BY SPACE_TYPE";
        Object[] params = new Object[] { lot,from,to };
        List<SampleQueryModel> violations = getJdbcTemplate().query(sql, params, new RowMapper<SampleQueryModel>() {
            @Override
            public SampleQueryModel mapRow(ResultSet rs, int i) throws SQLException {
                SampleQueryModel actor = new SampleQueryModel();
                actor.setF1(rs.getString(1));
                actor.setF2(rs.getString(2));
                return actor;
            }
        });
        return violations;

    }

    public List<VisitorPermit> checkVVParking(String space, String lot, String license) {
        List<VisitorPermit> permits = new ArrayList<VisitorPermit>();
        Object[] params = new Object[] {space,lot,license};
        String sql = "SELECT * FROM VISITOR_PERMIT where SPACE_NO=? AND LOT_NAME=? AND LICENSE_PLATE_NO=?";
        permits = getJdbcTemplate().query(sql,params,
                new VisitorPermitMapper());
        return permits;
    }

    public List<VisitorPermit> findVisitorPermitByLicense(String licenseNumber) {
        List<VisitorPermit> nonVisitorPermits = new ArrayList<VisitorPermit>();
        String sql = VisitorPermitMapper.BASE_SQL + " where vp.LICENSE_PLATE_NO = ?";
        Object[] params = new Object[] {licenseNumber};
        VisitorPermitMapper mapper = new VisitorPermitMapper();
        try {
            nonVisitorPermits = this.getJdbcTemplate().query(sql, params, mapper);
            return nonVisitorPermits;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
