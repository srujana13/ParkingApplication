package com.ncsu.csc540.parking.service;

import com.ncsu.csc540.parking.config.AppException;
import com.ncsu.csc540.parking.dao.*;
import com.ncsu.csc540.parking.domain.*;
import com.ncsu.csc540.parking.domain.VisitorPermit;
import com.ncsu.csc540.parking.domain.NonVisitorPermit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
public class AppService {
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
    private AppRoleDao appRoleDao;

    @Autowired
    private VehicleDao vehicleDao;

    public Citation get(Long c_no ) {
        Citation citation = this.citationDao.findCitationById(c_no);

        return citation;
    }
    public List<CitationVO> getAll() {
        List<CitationVO> citations = this.citationDao.findAllCitations();

        return citations;
    }

    public void issueCitation(Citation citation) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(citation.getCDate().getTime());
        calendar.add(Calendar.DATE,30);
        citation.setDue(new Timestamp(calendar.getTimeInMillis()));
        this.citationDao.saveOrUpdate(citation);
        Notification notification = new Notification();
        notification.setNId(UUID.randomUUID().toString());
        notification.setMessage(String.format("Citation Issued: License: %s On: %s",citation.getLicenseNumber(),citation.getCDate()));

        List<NonVisitorPermit> nvp = this.nonVisitorPermitDao.findPermitByLicense(citation.getLicenseNumber());
        if(nvp!=null && !nvp.isEmpty()){
            notification.setIssuedTo(nvp.get(0).getUnivid().toString());
        }else{
            List<VisitorPermit> vp = this.visitorPermitDao.findVisitorPermitByLicense(citation.getLicenseNumber());
            if(vp!=null && !vp.isEmpty()){
                notification.setIssuedTo(vp.get(0).getContact_no());
            }
        }
        notificationDao.save(notification);

    }

    public void payCitation(Citation citation) {
        citation.setStatus("PAID");
        this.citationDao.saveOrUpdate(citation);

    }

    public List<ParkingLots> getAll_parkinglots() {
        List<ParkingLots> parking_lots = this.parkingLotsDao.findAllParking_Lots();
        return parking_lots;
    }

    public void issueLot(ParkingLots parkinglots) {
        this.parkingLotsDao.addNewLot(parkinglots);

    }

    public void issueZoneDesignation(ZoneDesignation zonedesignation) {
        this.zoneDesignationsDao.saveOrUpdateZoneDesignations(zonedesignation);

    }

    public List<ZoneDesignation> getAll_zonedesignations() {
        List<ZoneDesignation> zone_designation = this.zoneDesignationsDao.findAllZone_Designations();
        return zone_designation;
    }

    public List<ParkingSpace> getAll_parkingspaces() {
        List<ParkingSpace> parking_space = this.parkingSpaceDao.findAllParking_Space();
        return parking_space;
    }

    public void issueTypetoSpace(ParkingSpace parkingspace) {
        this.parkingSpaceDao.saveOrUpdateTypetoSpace(parkingspace);

    }

    public List<VisitorPermit> getAllVisitorPermits() {
        return this.visitorPermitDao.findAllPermit();
    }

    public List<NonVisitorPermit> getAllNonVisitorPermits() {
        return this.nonVisitorPermitDao.findAllPermit();
    }

    public void issueVisitorPermit(VisitorPermit visitorPermit){
        this.visitorPermitDao.issuePermit(visitorPermit);
    }

    public void exitLot(String permitId){
        VisitorPermit visitorPermit = this.visitorPermitDao.findVisitorPermitById(permitId);

        if (visitorPermit == null){
            throw new AppException("Permit id does not exist");
        }

        Timestamp exitTime = new Timestamp(System.currentTimeMillis());

        if (exitTime.compareTo(visitorPermit.getExp_time()) > 0){
            Long diff = exitTime.getTime() - visitorPermit.getExp_time().getTime();
            visitorPermit.setOverage(diff);
            this.visitorPermitDao.saveOrUpdateTimeOverage(visitorPermit);
        }

        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setSpace_no(visitorPermit.getSpace_no());
        parkingSpace.setParking_lot_name(visitorPermit.getLot_name());
        parkingSpace.setOccupied(0);

        this.parkingSpaceDao.saveOrUpdateOccupied(parkingSpace);
    }

    public Integer addVehicle(NonVisitorPermit nonVisitorPermit){

        String permitId = nonVisitorPermit.getPermit_id();
        Long univid = nonVisitorPermit.getUnivid();
        String l_no = nonVisitorPermit.getLicense_plate_no();

        // identifying if it's a student or an employee
        List<String> roles = this.appRoleDao.getRoleNames(univid);

        // identifying current number of vehicles on permit
        List<NonVisitorPermit> nonVisitorPermits =
                this.nonVisitorPermitDao.findNonVisitorPermitsById(permitId, univid);

        if (roles.get(0).equals("S") && nonVisitorPermits.size() > 0){
            return 0;
        }

        if (roles.get(0).equals("E") && nonVisitorPermits.size() > 1){
            return 0;
        }

        NonVisitorPermit newVehiclePermit = new NonVisitorPermit();
        NonVisitorPermit temp = nonVisitorPermits.get(0);

        newVehiclePermit.setPermit_id(permitId);
        newVehiclePermit.setLicense_plate_no(l_no);
        newVehiclePermit.setUnivid(univid);
        newVehiclePermit.setZone_id(temp.getZone_id());
        newVehiclePermit.setSpace_type(temp.getSpace_type());
        newVehiclePermit.setExp_time(temp.getExp_time());
        newVehiclePermit.setStart_time(temp.getStart_time());

        this.nonVisitorPermitDao.insertNewVehicle(newVehiclePermit);
        return 1;
    }

    public Integer removeVehicle(NonVisitorPermit nonVisitorPermit){

        String permitId = nonVisitorPermit.getPermit_id();
        Long univid = nonVisitorPermit.getUnivid();
        String l_no = nonVisitorPermit.getLicense_plate_no();

        // identifying current number of vehicles on permit
        List<NonVisitorPermit> nonVisitorPermits =
                this.nonVisitorPermitDao.findNonVisitorPermitsById(permitId, univid);

        if (nonVisitorPermits.size() < 2){
            return 0;
        }

        this.nonVisitorPermitDao.deleteVehicle(permitId, univid, l_no);
        return 1;
    }

    public boolean checkNVValidity(String req) {
        List<NonVisitorPermit> permits = nonVisitorPermitDao.checkNVParking(req);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        if (permits != null && !permits.isEmpty()) {
            return permits.stream().anyMatch(p -> currentTime.before(p.getExp_time()));
        } else {
            throw new AppException("No Permit found");
        }
    }

    public boolean checkVVValidity(String space,String lot, String license) {
        List<VisitorPermit> permits = visitorPermitDao.checkVVParking(space,lot,license);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        if (permits != null && !permits.isEmpty()) {
            return permits.stream().anyMatch(p -> currentTime.before(p.getExp_time()));
        } else {
            throw new AppException("No Permit found");
        }
    }

    public Integer replaceVehicle(String permitId, Long univId, String l_no_old, String l_no_new){

           NonVisitorPermit nonVisitorPermit =
                   this.nonVisitorPermitDao.findSinglePermitById(permitId, univId, l_no_old);

        if (nonVisitorPermit == null){
            return 0;
        }
        this.nonVisitorPermitDao.updateVehicle(permitId, univId, l_no_old, l_no_new);
        return 1;
    }

    public void saveVehicleInfo(Vehicle vehicle){
        this.vehicleDao.save(vehicle);
    }

    

    public void addNonVisitorPermit(NonVisitorPermit nonvisitorpermit){
        this.nonVisitorPermitDao.issuePermit(nonvisitorpermit);
    }



}


