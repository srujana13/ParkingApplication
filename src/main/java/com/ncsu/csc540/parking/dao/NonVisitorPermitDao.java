package com.ncsu.csc540.parking.dao;
import com.ncsu.csc540.parking.config.AppException;
import com.ncsu.csc540.parking.domain.NonVisitorPermit;
import com.ncsu.csc540.parking.mapper.NonVisitorPermitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.ncsu.csc540.parking.utils.AppUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;

@Repository
@Transactional
public class NonVisitorPermitDao extends JdbcDaoSupport {
    @Autowired
    public NonVisitorPermitDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    @Autowired
    private AppRoleDao appRoleDao;

    public List<NonVisitorPermit> findAllPermit() {

        List<NonVisitorPermit> nonVisitorPermits = new ArrayList<NonVisitorPermit>();
        String sql = NonVisitorPermitMapper.BASE_SQL;
        nonVisitorPermits = getJdbcTemplate().query(sql,
                new NonVisitorPermitMapper());
        return nonVisitorPermits;
    }

    public List<NonVisitorPermit> findVisitorPermitByUNIVID(Long univid) {

        String sql = NonVisitorPermitMapper.BASE_SQL + " where  nvp.UNIVID= ? ";
        Object[] params = new Object[] {univid};
        NonVisitorPermitMapper mapper = new NonVisitorPermitMapper();
        try {
            List<NonVisitorPermit> permits = this.getJdbcTemplate().query(sql, params, mapper);

            return permits;
        } catch (EmptyResultDataAccessException e) {
            System.out.println("its in the catch...");
            return null;
        }
    }
    public Integer findNoOfUsersForZone(String zone) {

        String sql = "SELECT COUNT(distinct (UNIVID)) FROM NON_VISITOR_PERMIT WHERE ZONE_ID=?";
        Object[] params = new Object[] {zone};
        NonVisitorPermitMapper mapper = new NonVisitorPermitMapper();
        try {
            Integer count = this.getJdbcTemplate().queryForObject(sql, params, Integer.class);

            return count;
        } catch (EmptyResultDataAccessException e) {
            System.out.println("its in the catch...");
            return null;
        }
    }

    public List<NonVisitorPermit> findNonVisitorPermitsById(String permitId, Long univid) {

        List<NonVisitorPermit> nonVisitorPermits = new ArrayList<NonVisitorPermit>();
        String sql = NonVisitorPermitMapper.BASE_SQL + " where nvp.PERMIT_ID = ? AND nvp.UNIVID = ?";
        Object[] params = new Object[] {permitId, univid};
        NonVisitorPermitMapper mapper = new NonVisitorPermitMapper();
        try {
            nonVisitorPermits = this.getJdbcTemplate().query(sql, params, mapper);
            return nonVisitorPermits;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    public NonVisitorPermit findSinglePermitById(String permitId, Long univid, String l_no) {

        NonVisitorPermit nonVisitorPermit = new NonVisitorPermit();
        String sql = NonVisitorPermitMapper.BASE_SQL + " where nvp.PERMIT_ID = ? AND nvp.UNIVID = ? AND nvp.LICENSE_PLATE_NO = ?";
        Object[] params = new Object[] {permitId, univid, l_no};
        NonVisitorPermitMapper mapper = new NonVisitorPermitMapper();
        try {
            nonVisitorPermit = this.getJdbcTemplate().queryForObject(sql, params, mapper);
            return nonVisitorPermit;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void insertNewVehicle(NonVisitorPermit nonVisitorPermit){
        String sql = "INSERT INTO NON_VISITOR_PERMIT (PERMIT_ID, LICENSE_PLATE_NO, START_TIME, EXP_TIME, " +
                "ZONE_ID, UNIVID, SPACE_TYPE) VALUES (?,?,?,?,?,?,?)";
        Object[] params = new Object[] {nonVisitorPermit.getPermit_id(), nonVisitorPermit.getLicense_plate_no(),
        nonVisitorPermit.getStart_time(), nonVisitorPermit.getExp_time(), nonVisitorPermit.getZone_id(), nonVisitorPermit.getUnivid(),
        nonVisitorPermit.getSpace_type()};

        this.getJdbcTemplate().update(sql, params);
    }

    public List<NonVisitorPermit> checkNVParking(String req) {

        List<NonVisitorPermit> permits = new ArrayList<NonVisitorPermit>();
        Object[] params = new Object[] {req};
        String sql = "SELECT * FROM NON_VISITOR_PERMIT where PERMIT_ID=?";
        permits = getJdbcTemplate().query(sql,params,
                new NonVisitorPermitMapper());
        return permits;
    }

    public void deleteVehicle(String permitId, Long univid, String l_no){
        String sql = "DELETE FROM NON_VISITOR_PERMIT WHERE PERMIT_ID = ? AND LICENSE_PLATE_NO = ? AND UNIVID = ?";

        Object[] params = new Object[] {permitId, l_no, univid};
        this.getJdbcTemplate().update(sql, params);
    }

    public void updateVehicle(String permitId, Long univId, String l_no_old, String l_no_new){
        String sql = "UPDATE NON_VISITOR_PERMIT SET LICENSE_PLATE_NO = ? WHERE PERMIT_ID = ? AND UNIVID = ? AND LICENSE_PLATE_NO = ?";

        Object[] params = new Object[] {l_no_new, permitId, univId, l_no_old};
        try{
            this.getJdbcTemplate().update(sql, params);
        }catch (Exception e){
            throw new AppException(e.getMessage());
        }

    }

    public void issuePermit(NonVisitorPermit nonvisitorpermit) {


        Long univid = nonvisitorpermit.getUnivid();
        String zone_id = nonvisitorpermit.getZone_id();
        String space_type = nonvisitorpermit.getSpace_type();
        String vehicle_no = nonvisitorpermit.getLicense_plate_no();

        String sqlForVehicle = "SELECT COUNT(*) FROM VEHICLE WHERE LICENSE_PLATE_NO = ?";
         Object[] vehicleParams = new Object[] { vehicle_no };
         if (getJdbcTemplate().queryForObject(sqlForVehicle, vehicleParams, Integer.class) == 0){
             throw new AppException("Vehicle Information not saved. Please save the vehicle information before you request for permit");
        }

        String permit_id = AppUtils.generatePermitID(zone_id);

        List<String> roles = appRoleDao.getRoleNames(univid);
        String role = roles.get(0);

        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp start_time = new java.sql.Timestamp(now.getTime());
        java.sql.Timestamp end_time = null;
        if (role.equals("S")){
            if (zone_id.equals("AS") == false && zone_id.equals("BS") == false && zone_id.equals("CS") == false && zone_id.equals("DS") == false ){
                throw new AppException("Please enter a correct parking zone for student it should be AS or BS or CS or DS");
            }
            else{
                calendar.add(Calendar.MONTH, 4);
                calendar.set(Calendar.HOUR_OF_DAY, 23);            
                calendar.set(Calendar.MINUTE, 59);                 
                calendar.set(Calendar.SECOND, 0);                 
                calendar.set(Calendar.MILLISECOND, 0);  
                end_time = new java.sql.Timestamp(calendar.getTime().getTime());
            }
        }

        if (role.equals("E")){
            if (zone_id.equals("A") == false && zone_id.equals("B") == false && zone_id.equals("C") == false && zone_id.equals("D") == false ){
                throw new AppException("Please enter a correct parking zone for Employee it should be A or B or C or D");
            }
            else{
                calendar.add(Calendar.MONTH, 12);
                calendar.set(Calendar.HOUR_OF_DAY, 23);            
                calendar.set(Calendar.MINUTE, 59);                 
                calendar.set(Calendar.SECOND, 0);                 
                calendar.set(Calendar.MILLISECOND, 0);  
                end_time = new java.sql.Timestamp(calendar.getTime().getTime());
            }
        }

        String sql2 = "INSERT INTO NON_VISITOR_PERMIT (PERMIT_ID, LICENSE_PLATE_NO, START_TIME, EXP_TIME, ZONE_ID, UNIVID, SPACE_TYPE) " +
                     "VALUES (?,?,?,?,?,?,?)";
        Object[] params1 = {permit_id, vehicle_no, start_time, end_time, zone_id, univid, space_type};
        getJdbcTemplate().update(sql2, params1);


        }

    public  List<NonVisitorPermit> findPermitByLicense(String licenseNumber) {
        List<NonVisitorPermit> nonVisitorPermits = new ArrayList<NonVisitorPermit>();
        String sql = NonVisitorPermitMapper.BASE_SQL + " where nvp.LICENSE_PLATE_NO = ?";
        Object[] params = new Object[] {licenseNumber};
        NonVisitorPermitMapper mapper = new NonVisitorPermitMapper();
        try {
            nonVisitorPermits = this.getJdbcTemplate().query(sql, params, mapper);
            return nonVisitorPermits;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
