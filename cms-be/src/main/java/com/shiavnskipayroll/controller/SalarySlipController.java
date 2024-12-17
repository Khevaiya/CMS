package com.shiavnskipayroll.controller;


import com.shiavnskipayroll.dto.request.SalarySlipRequestDTO;
import com.shiavnskipayroll.dto.response.SalarySlipResponseDTO;
import com.shiavnskipayroll.service.SalarySlipService;
import com.shiavnskipayroll.serviceimpl.SalarySlipServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/salary-slips")
public class SalarySlipController {


    private final SalarySlipService salarySlipService;

    private final SalarySlipServiceImpl salarySlipServiceImpl;

    // Get salary slip by ID
    @GetMapping("/{id}")
    public ResponseEntity<SalarySlipResponseDTO> getSalarySlipById(@PathVariable String id) {
        SalarySlipResponseDTO salarySlip = salarySlipService.getSalarySlipById(id);
        return ResponseEntity.ok(salarySlip);
    }

    // Get all salary slips
    @GetMapping
    public ResponseEntity<List<SalarySlipResponseDTO>> getAllSalarySlips() {
        List<SalarySlipResponseDTO> salarySlips = salarySlipService.getAllSalarySlips();
        return ResponseEntity.ok(salarySlips);
    }

    // Create a new salary slip
    @PostMapping
    public ResponseEntity<SalarySlipResponseDTO> createSalarySlip(@RequestBody SalarySlipRequestDTO salarySlipRequestDTO) {
        System.out.println("createSalarySlip callled "+salarySlipRequestDTO);
        SalarySlipResponseDTO createdSalarySlip = salarySlipService.createSalarySlip(salarySlipRequestDTO);
        return new ResponseEntity<>(createdSalarySlip, HttpStatus.CREATED);
    }

    // Update an existing salary slip by ID
    @PutMapping("/{id}")
    public ResponseEntity<SalarySlipResponseDTO> updateSalarySlip(@PathVariable String id, @RequestBody SalarySlipRequestDTO salarySlipRequestDTO) {
        SalarySlipResponseDTO updatedSalarySlip = salarySlipService.updateSalarySlip(id, salarySlipRequestDTO);
        return ResponseEntity.ok(updatedSalarySlip);
    }

    // Delete salary slip by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSalarySlip(@PathVariable String id) {
        salarySlipService.deleteSalarySlip(id);
        return ResponseEntity.ok("Salary slip deleted successfully.");
    }
    @GetMapping("/downloadFile/{id}")
    ResponseEntity<byte[] >downloadFile(@PathVariable String  id) throws IOException 
    {
    	byte[] fileBytes=salarySlipServiceImpl.downloadFile(id);
    	return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=demopath")
                .contentType(MediaType.APPLICATION_PDF)  // Set content type to PDF
                .body(fileBytes);
    }

    //--------------------------------------------------------------------
    @GetMapping("/displayPayslipByIdYearMonth/{id}/{year}/{month}")
    public ResponseEntity<String> getConsultantPayslipByDate(
            @PathVariable String id,
            @PathVariable int year,
            @PathVariable int month)  {
        try {
            System.out.println("displayPayslipByYearAndMonth called");
            String base64Payslip = salarySlipService.getPayslipByDate(id, year, month);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"salaryslip.pdf\"")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(base64Payslip);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get employee payslip for the specified date", e);
        }
    }
    //------------------------------------------------------------
    @GetMapping("/displayPayslipByYearAndMonth/{year}/{month}")
    public ResponseEntity<List<String>> getConsultantPayslipByDate(
            @PathVariable String year,
            @PathVariable String month) {
        try {
            System.out.println("displayPayslipByYearAndMonth called");
          List<String>  base64Payslip = salarySlipService.getPayslipByYearAndMonth( year, month);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"salaryslip.pdf\"")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(base64Payslip);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get employee payslip for the specified date", e);
        }
    }


}
