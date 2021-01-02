package com.ncsu.csc540.parking.controller;

import com.ncsu.csc540.parking.config.AppException;
import com.ncsu.csc540.parking.domain.NonVisitorPermit;
import com.ncsu.csc540.parking.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    AppService userService;

    @RequestMapping(value = "/user/addVehicle", method = RequestMethod.GET)
    public ModelAndView addVehicle(ModelAndView model) {

        NonVisitorPermit nonVisitorPermit = new NonVisitorPermit();

        model.addObject("nonVisitorPermit", nonVisitorPermit);

        model.setViewName("AddVehiclePage");

        return model;
    }

    @RequestMapping(value = "/user/addVehicle/add", method = RequestMethod.POST)
    public ModelAndView addVehicleExecute(@ModelAttribute NonVisitorPermit nonVisitorPermit) {
        Integer result = userService.addVehicle(nonVisitorPermit);

        if (result == 0){
            return new ModelAndView("MaximumVehicleLimit", "message", "Cannot add vehicle, " +
                    "maximum vehicle limit has reached");
        }
        return new ModelAndView("ExitLotDone", "message",
                "Vehicle Added Successfully!");
    }

    @RequestMapping(value = "/user/removeVehicle", method = RequestMethod.GET)
    public ModelAndView removeVehicle(ModelAndView model) {

        NonVisitorPermit nonVisitorPermit = new NonVisitorPermit();

        model.addObject("nonVisitorPermit", nonVisitorPermit);

        model.setViewName("RemoveVehiclePage");

        return model;
    }

    @RequestMapping(value = "/user/removeVehicle/remove", method = RequestMethod.POST)
    public ModelAndView removeVehicleExecute(@ModelAttribute NonVisitorPermit nonVisitorPermit) {
        Integer result = userService.removeVehicle(nonVisitorPermit);
        if (result == 0){
            return new ModelAndView("ExitLotDone", "message", "Cannot remove only " +
                    "one vehicle present in permit");
        }
        return new ModelAndView("ExitLotDone", "message",
                "Vehicle Removed Successfully!");
    }

    @RequestMapping(value = "/user/replaceVehicle", method = RequestMethod.GET)
    public ModelAndView replaceVehicle(ModelAndView model) {

        model.setViewName("ReplaceVehiclePage");

        return model;
    }
    @RequestMapping(value = "/user/replaceVehicle/replace", method = RequestMethod.POST)
    public ModelAndView replaceVehicleExecute(@RequestParam (value = "permit_id", required = true)  String permit_id,
                                              @RequestParam(value = "univid", required = true)  Long univid,
                                              @RequestParam(value = "l_no_old", required = true)  String l_no_old,
                                              @RequestParam(value = "l_no_new", required = true)  String l_no_new)  {
        Integer result = userService.replaceVehicle(permit_id, univid, l_no_old, l_no_new);
        if (result == 0){
            return new ModelAndView("ExitLotDone", "message", "Record does not exist!");
        }
        return new ModelAndView("ExitLotDone", "message",
                "Vehicle Replaced Successfully!");
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
