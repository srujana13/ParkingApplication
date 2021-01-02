package com.ncsu.csc540.parking.service;

import com.ncsu.csc540.parking.dao.*;
import com.ncsu.csc540.parking.domain.NonVisitorPermit;
import com.ncsu.csc540.parking.domain.SampleQueryModel;
import com.ncsu.csc540.parking.domain.Vehicle;
import com.ncsu.csc540.parking.domain.ZoneDesignation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleQueryService {
    @Autowired
    private CitationDao citationDao;

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private AppUserDao appUserDAO;

    @Autowired
    private ParkingLotsDao parkingLotsDao;

    @Autowired
    private ZoneDesignationsDao zoneDesignationsDao;

    @Autowired
    private ParkingSpaceDao parkingSpaceDao;

    @Autowired
    private VisitorPermitDao visitorPermitDao;

    @Autowired
    private NonVisitorPermitDao nonVisitorPermitDao;

    @Autowired
    private VehicleDao vehicleDao;

    public List<ZoneDesignation> query1() {
        List<ZoneDesignation> zone_designation = this.zoneDesignationsDao.findAllZone_DesignationPairs();
        return zone_designation;
    }

    public List<NonVisitorPermit> query2(String univid) {
        List<NonVisitorPermit> nvPermits = this.nonVisitorPermitDao.findVisitorPermitByUNIVID(Long.valueOf(univid));
        return nvPermits;
    }
    public List<Vehicle> query3(String univid) {
        List<Vehicle> vehicles = this.vehicleDao.getVehicleByUnivid(Long.valueOf(univid));
        return vehicles;
    }

    public Integer query4(String lotName, String type) {
        Integer availableSpace = parkingSpaceDao.findAvailableVisitorSpace(lotName,type);
        return  availableSpace;
    }

    public List<String> query5() {
        List<String> violations = citationDao.findCarsInViolation();
        return  violations;
    }
    public Integer query6(String zone) {
        Integer userCount = nonVisitorPermitDao.findNoOfUsersForZone(zone);
        return  userCount;
    }

    public List<SampleQueryModel> query7(String from, String to) {
        List<SampleQueryModel> rowSet = citationDao.findCitationPerLot(from, to);
        StringBuffer sb = new StringBuffer();

        return  rowSet;
    }

    public List<SampleQueryModel> query8(String lot, String from, String to) {
        List<SampleQueryModel> rowSet = visitorPermitDao.findPermits(lot,from, to);

        return  rowSet;
    }

    public List<SampleQueryModel> query9(String startMonth) {
        List<SampleQueryModel> rowSet = citationDao.findRevenuePerDay(startMonth);

        return  rowSet;
    }
}


