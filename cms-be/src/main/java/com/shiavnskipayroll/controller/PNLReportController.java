package com.shiavnskipayroll.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.shiavnskipayroll.dto.response.PNLReportResponseDTO;

import com.shiavnskipayroll.repository.EmployeeRepository;
import com.shiavnskipayroll.service.PNLReportService;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/pAndLReport")
public class PNLReportController {
     private EmployeeRepository employeeRepository;
     private final PNLReportService pnlReportService;
     
     @GetMapping("/getPAndLAmount")
     ResponseEntity<Double> PAndLAmount(@RequestParam double companyExpenses)
     {	
    	 	 return new ResponseEntity<>(pnlReportService.calculatePAndLAmount(companyExpenses),HttpStatus.OK);
     }
     @GetMapping("/generatePNLReportForAllProjects")
     ResponseEntity<List<PNLReportResponseDTO>>generatePNLReportOfAllProjects(@RequestParam double companyExpenses)
     {	
    	 	 return new ResponseEntity<>(pnlReportService.generatePNLReportForAllProjects(companyExpenses),HttpStatus.OK);
     }
     
 }
