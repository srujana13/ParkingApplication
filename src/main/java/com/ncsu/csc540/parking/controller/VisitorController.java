package com.ncsu.csc540.parking.controller;

import com.ncsu.csc540.parking.config.AppException;
import com.ncsu.csc540.parking.domain.VisitorPermit;
import com.ncsu.csc540.parking.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class VisitorController {

    @Autowired
    AppService visitorService;

    @RequestMapping(value = "/visitor/getVisitorPermit", method = RequestMethod.GET)
    public ModelAndView newVisitorPermit(ModelAndView model) {

        VisitorPermit visitorPermit = new VisitorPermit();

        model.addObject("visitorPermit", visitorPermit);
        model.setViewName("NewVisitorPermitPage");

        return model;
    }

    @RequestMapping(value = "/visitor/getVisitorPermit/add", method = RequestMethod.POST)
    public ModelAndView addVisitorPermit(@ModelAttribute VisitorPermit visitorPermit) {

        visitorPermit.setStart_time(Timestamp.valueOf(visitorPermit.getStartTime().
                replace("T"," ") + ":00.000"));

        visitorService.issueVisitorPermit(visitorPermit);

        return new ModelAndView("VisitorPermitCreated", "message",
                "Permit Created Successfully!");
    }

    @RequestMapping(value = "/visitor/exitLot", method = RequestMethod.GET)
    public ModelAndView exitLot(ModelAndView model) {
        VisitorPermit visitorPermit = new VisitorPermit();
        model.addObject("visitorPermit", visitorPermit);
        model.setViewName("exitLotForm");
        return model;
    }

    @RequestMapping(value = "/visitor/exitLot/exit", method = RequestMethod.POST)
    public ModelAndView exitLotExecute(@ModelAttribute VisitorPermit visitorPermit) {
        String permitId = visitorPermit.getPermit_id();
        visitorService.exitLot(permitId);
        return new ModelAndView("ExitLotDone", "message", "Lot Exited Successfully!");
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
