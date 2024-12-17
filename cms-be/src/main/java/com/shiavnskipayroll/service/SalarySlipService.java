package com.shiavnskipayroll.service;


import com.shiavnskipayroll.dto.request.SalarySlipRequestDTO;
import com.shiavnskipayroll.dto.response.SalarySlipResponseDTO;

import java.io.IOException;
import java.util.List;



public interface SalarySlipService {
    
    SalarySlipResponseDTO createSalarySlip(SalarySlipRequestDTO salarySlipRequestDTO);
    
    SalarySlipResponseDTO getSalarySlipById(String id);
    
    List<SalarySlipResponseDTO> getAllSalarySlips();
    
    SalarySlipResponseDTO updateSalarySlip(String id, SalarySlipRequestDTO salarySlipRequestDTO);
    
    void deleteSalarySlip(String id);
    String getPayslipByDate(String id, int year, int month) throws IOException;
    List<String >getPayslipByYearAndMonth(String year, String month) throws IOException;
}
