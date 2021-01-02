package com.ncsu.csc540.parking.controller;

import com.ncsu.csc540.parking.config.AppException;
import com.ncsu.csc540.parking.domain.Citation;
import com.ncsu.csc540.parking.domain.CitationVO;
import com.ncsu.csc540.parking.domain.ParkingLots;
import com.ncsu.csc540.parking.service.AppService;
import com.ncsu.csc540.parking.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class CitationController {

    @Autowired
    AppService citationService;

    @RequestMapping(value = "/admin/citations", method = RequestMethod.GET)
    public ModelAndView get(ModelAndView model) {


        List<CitationVO> citations =citationService.getAll();

        model.addObject("citations", citations);
        model.setViewName("citationAdminPage");

        return model;
    }

    @RequestMapping(value = "/admin/citations/new", method = RequestMethod.GET)
    public ModelAndView newCitationForm(ModelAndView model) {

        Citation citation = new Citation();
        model.addObject("citation", citation);
        model.setViewName("newCitationForm");

        return model;

    }

    @RequestMapping(value = "/admin/citations/add", method = RequestMethod.POST)
    public ModelAndView addCitation(@ModelAttribute Citation citation) {
        citation.setCDate(Timestamp.valueOf(citation.getCDate_String().
                replace("T"," ") + ":00.000"));
        citationService.issueCitation(citation);
        List<CitationVO> citations =citationService.getAll();
        return new ModelAndView("redirect:/admin/citations","citations",citations);
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
