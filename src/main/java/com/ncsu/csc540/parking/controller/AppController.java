package com.ncsu.csc540.parking.controller;

import com.ncsu.csc540.parking.config.AppException;
import com.ncsu.csc540.parking.domain.*;
import com.ncsu.csc540.parking.service.AppService;
import com.ncsu.csc540.parking.service.SampleQueryService;
import com.ncsu.csc540.parking.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class AppController {
    @Autowired
    SampleQueryService queryService;

    @Autowired
    private AppService appService;

    @RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "welcomePage";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) throws Exception{

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "adminPage";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {

        return "loginPage";
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {

        String userName = principal.getName();

        System.out.println("User Name: " + userName);

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "userInfoPage";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();

            String userInfo = WebUtils.toString(loginedUser);

            model.addAttribute("userInfo", userInfo);

            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);

        }

        return "error/403";
    }
    @RequestMapping(value = { "/visitor" }, method = RequestMethod.GET)
    public String visitorWelcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is Visitor welcome page!");
        return "visitorWelcomePage";
    }

    @RequestMapping(value = { "/sampleQueries" }, method = RequestMethod.GET)
    public String sampleQueryPage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is Sample Query page!");
        return "sampleQueryPage";
    }
    @RequestMapping(value = { "/sampleQueries/1" }, method = RequestMethod.GET)
    public String sampleQuery1(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is Sample Query page!");
        List<ZoneDesignation> zones = queryService.query1();
        model.addAttribute("zonedesignation", zones);

        return "ZoneDesignationPage";
    }
    @RequestMapping(value = { "/sampleQueries/2" }, method = RequestMethod.GET)
    public ModelAndView sampleQuery2(@RequestParam(value = "univid", required = true)  String univid) {
        List<NonVisitorPermit> permits = queryService.query2(univid);

        return new ModelAndView("PermitPage","nonVisitorPermits",permits);
    }
    @RequestMapping(value = { "/sampleQueries/3" }, method = RequestMethod.GET)
    public ModelAndView sampleQuery3(@RequestParam(value = "univid", required = true)  String univid) {
        List<Vehicle> vehicles = queryService.query3(univid);

        return new ModelAndView("VehiclePage","vehicles",vehicles);
    }
    @RequestMapping(value = { "/sampleQueries/4" }, method = RequestMethod.GET)
    public ModelAndView sampleQuery4(@RequestParam(value = "lotName", required = true)  String lotName, @RequestParam(value = "type", required = true)  String type) {
        Integer space = queryService.query4(lotName,type);
        ModelAndView mav = new ModelAndView();

        mav.addObject("message", "Available Visitor Space:");
        mav.addObject("output", space);
        mav.setViewName("SampleOutput");

        return mav;
    }
    @RequestMapping(value = { "/sampleQueries/5" }, method = RequestMethod.GET)
    public ModelAndView sampleQuery5(Model model) {
        List<String> violations = queryService.query5();
        ModelAndView mav = new ModelAndView();

        mav.addObject("message", "Cars In Violation:");
        mav.addObject("output", violations);
        mav.setViewName("SampleOutput");

        return mav;
    }
    @RequestMapping(value = { "/sampleQueries/6" }, method = RequestMethod.GET)
    public ModelAndView sampleQuery6(@RequestParam(value = "zone", required = true)  String zone) {
        Integer count = queryService.query6(zone);

        ModelAndView mav = new ModelAndView();

        mav.addObject("message", "No of Employees having Permits for Zone:");
        mav.addObject("output", count);
        mav.setViewName("SampleOutput");

        return mav;
    }

    @RequestMapping(value = { "/sampleQueries/7" }, method = RequestMethod.GET)
    public ModelAndView sampleQuery7(@RequestParam(value = "from", required = true)  String from, @RequestParam(value = "to", required = true)  String to) {

        List<SampleQueryModel> output = queryService.query7(from,to);
        ModelAndView mav = new ModelAndView();

        mav.addObject("col1", "LOT");
        mav.addObject("col2", "CITATIONS");
        mav.addObject("message", "Citations Per Lot:");
        mav.addObject("outputs", output);
        mav.setViewName("ReportingOutput");

        return mav;
    }

    @RequestMapping(value = { "/sampleQueries/8" }, method = RequestMethod.GET)
    public ModelAndView sampleQuery8(@RequestParam(value = "lot", required = true)  String lot, @RequestParam(value = "from", required = true)  String from, @RequestParam(value = "to", required = true)  String to) {

        List<SampleQueryModel> output = queryService.query8(lot,from,to);
        ModelAndView mav = new ModelAndView();

        mav.addObject("col1", "TYPE");
        mav.addObject("col2", "PERMITS");
        mav.addObject("message", "Permits per SpaceType:");
        mav.addObject("outputs", output);
        mav.setViewName("ReportingOutput");

        return mav;
    }

    @RequestMapping(value = { "/sampleQueries/9" }, method = RequestMethod.GET)
    public ModelAndView sampleQuery9(@RequestParam(value = "startMonth", required = true)  String startMonth) {

        List<SampleQueryModel> output = queryService.query9(startMonth);
        ModelAndView mav = new ModelAndView();

        mav.addObject("col1", "DATE");
        mav.addObject("col2", "REVENUE");
        mav.addObject("message", "Permits per SpaceType:");
        mav.addObject("outputs", output);
        mav.setViewName("ReportingOutput");

        return mav;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String userWelcome(Model model, Principal principal) {

        String userName = principal.getName();

        System.out.println("User Name: " + userName);

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is User welcome page!");
        return "userWelcomePage";

    }

    @RequestMapping(value = "/registerVehicle", method = RequestMethod.GET)
    public ModelAndView registerVehicle(ModelAndView model){
        Vehicle vehicle = new Vehicle();
        model.addObject("vehicle", vehicle);

        model.setViewName("NewVehicleForm");

        return model;
    }

    @RequestMapping(value = "/registerVehicle/save", method = RequestMethod.POST)
    public ModelAndView saveVehicleInfo(@ModelAttribute Vehicle vehicle){

        this.appService.saveVehicleInfo(vehicle);

        return new ModelAndView("ExitLotDone", "message",
                "Vehicle Registered Successfully!");
    }

    @RequestMapping(value = "/user/citations/pay", method = RequestMethod.GET)
    public ModelAndView payCitation(@RequestParam(value = "c_no", required = false)  String citationNumber) {
        Citation citation = appService.get(Long.valueOf(citationNumber));

        appService.payCitation(citation);

        //REDIRECT TO CITATION PAGE

        ModelAndView model = new ModelAndView("userWelcomePage");

        return model;
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
