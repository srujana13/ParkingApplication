package com.ncsu.csc540.parking.controller;

import com.ncsu.csc540.parking.domain.*;
import com.ncsu.csc540.parking.domain.NonVisitorPermit;
import com.ncsu.csc540.parking.config.AppException;
import com.ncsu.csc540.parking.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdminFunctionalityController {

    @Autowired
    AppService appService;

    // Adding parking Lots
    @RequestMapping(value = "/admin/addLot", method = RequestMethod.GET)
    public ModelAndView getparkinglots(ModelAndView model) {


        List<ParkingLots> parkinglots = appService.getAll_parkinglots();

        model.addObject("parkinglots", parkinglots);
        model.setViewName("ParkingLotsPage");

        return model;
    }

    @RequestMapping(value = "/admin/addLot/new", method = RequestMethod.GET)
    public ModelAndView newParkingLotForm(ModelAndView model) {

        ParkingLots parkinglots = new ParkingLots();
        model.addObject("parkinglots", parkinglots);
        model.setViewName("newParkingLotForm");

        return model;

    }

    @RequestMapping(value = "/admin/addLot/add", method = RequestMethod.POST)
    public ModelAndView addParkingLot(@ModelAttribute ParkingLots parkinglots) {


        appService.issueLot(parkinglots);

        List<ParkingLots> parkinglots1 = appService.getAll_parkinglots();
        return new ModelAndView("redirect:/admin/addLot","parkinglots1",parkinglots1);
    }

    // Add a Zone to a Parking Lot

    @RequestMapping(value = "/admin/addZonetoLot", method = RequestMethod.GET)
    public ModelAndView getzonedesignations(ModelAndView model) {


        List<ZoneDesignation> zonedesignation = appService.getAll_zonedesignations();

        model.addObject("zonedesignation", zonedesignation);
        model.setViewName("ZoneDesignationPage");

        return model;
    }

    @RequestMapping(value = "/admin/addZonetoLot/new", method = RequestMethod.GET)
    public ModelAndView newZoneDesignationForm(ModelAndView model) {

        ZoneDesignation zonedesignation = new ZoneDesignation();
        model.addObject("zonedesignation", zonedesignation);
        model.setViewName("newZoneDesignationForm");

        return model;

    }

    @RequestMapping(value = "/admin/addZonetoLot/add", method = RequestMethod.POST)
    public ModelAndView addZoneDesignation (@ModelAttribute ZoneDesignation zonedesignation) {

        appService.issueZoneDesignation(zonedesignation);

        List<ZoneDesignation> zonedesignation1 = appService.getAll_zonedesignations();
        return new ModelAndView("redirect:/admin/addZonetoLot","zonedesignation1",zonedesignation1);
    }


    // Assign a type to space

    @RequestMapping(value = "/admin/AssignTypeToSpace", method = RequestMethod.GET)
    public ModelAndView getparkingspace(ModelAndView model) {


        List<ParkingSpace> parkingspace = appService.getAll_parkingspaces();

        model.addObject("parkingspace", parkingspace);
        model.setViewName("ParkingSpacePage");

        return model;
    }

    @RequestMapping(value = "/admin/AssignTypeToSpace/new", method = RequestMethod.GET)
    public ModelAndView newParkingSpace(ModelAndView model) {

        ParkingSpace parkingspace = new ParkingSpace();
        model.addObject("parkingspace", parkingspace);
        model.setViewName("AssignTypeToSpaceForm");

        return model;
    }

    @RequestMapping(value = "/admin/AssignTypeToSpace/add", method = RequestMethod.POST)
    public ModelAndView addTypetoSpace (@ModelAttribute ParkingSpace parkingspace) {

        appService.issueTypetoSpace(parkingspace);

        List<ParkingSpace> parkingspace1 = appService.getAll_parkingspaces();
        return new ModelAndView("redirect:/admin/AssignTypeToSpace","parkingspace1",parkingspace1);
    }

    @RequestMapping(value = "/admin/AssignPermit", method = RequestMethod.GET)
    public ModelAndView getpermits(ModelAndView model) {

        List<VisitorPermit> visitorPermits = appService.getAllVisitorPermits();
        List<NonVisitorPermit> nonVisitorPermits = appService.getAllNonVisitorPermits();
        model.addObject("visitorPermits", visitorPermits);
        model.addObject("nonVisitorPermits", nonVisitorPermits);
        model.setViewName("PermitPage");

        return model;
    }

    @RequestMapping(value = "/admin/AssignPermit/new", method = RequestMethod.GET)
    public ModelAndView newPermit(ModelAndView model) {

        NonVisitorPermit permit = new NonVisitorPermit();
        model.addObject("permit", permit);
        model.setViewName("PermitForm");

        return model;
    }

    @RequestMapping(value = "/admin/AssignPermit/add", method = RequestMethod.POST)
    public ModelAndView addTypetoSpace (@ModelAttribute NonVisitorPermit nonvisitorpermit) {

        appService.addNonVisitorPermit(nonvisitorpermit);

        List<NonVisitorPermit> nonvisitorpermit1 = appService.getAllNonVisitorPermits();

        return new ModelAndView("redirect:/admin/AssignPermit","nonvisitorpermit1",nonvisitorpermit1);
    }

    @RequestMapping(value = "/admin/CheckNVParking", method = RequestMethod.GET)
    public ModelAndView CheckNValidParking(ModelAndView model) {

        model.setViewName("ValidNVForm");
        return model;
    }

    @RequestMapping(value = "/admin/CheckNVParking/check", method = RequestMethod.POST)
    public ModelAndView CheckNValidParkingPost( @RequestParam(value = "permitID", required = false)  String permitID) {

       if(appService.checkNVValidity(permitID)){
           return new ModelAndView("ValidPermitCheck", "message",
                   "Permit Valid!");
       }

        return new ModelAndView("ValidPermitCheck", "message",
                "Permit Not Valid!");
    }

    @RequestMapping(value = "/admin/CheckVVParking", method = RequestMethod.GET)
    public ModelAndView CheckVValidParking(ModelAndView model) {

        model.setViewName("ValidVVForm");
        return model;
    }

    @RequestMapping(value = "/admin/CheckVVParking/check", method = RequestMethod.POST)
    public ModelAndView CheckVValidParkingPost( @RequestParam(value = "space", required = false)  String space,@RequestParam(value = "lot", required = false)  String lot,@RequestParam(value = "license", required = false)  String license) {

        if(appService.checkVVValidity(space,lot,license)){
            return new ModelAndView("ValidPermitCheck", "message",
                    "Permit Valid!");
        }

        return new ModelAndView("ValidPermitCheck", "message",
                "Permit Not Valid!");
    }

    @ExceptionHandler(AppException.class)
    public ModelAndView handleError(HttpServletRequest req, AppException ex) {
        ModelAndView mav = new ModelAndView();
        System.out.println(ex.getMessage());
        mav.addObject("message", ex.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error/errorPage");
        return mav;
    }
}
