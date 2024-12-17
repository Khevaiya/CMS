package com.shiavnskipayroll.repository;

import   com.shiavnskipayroll.entity.SalarySlip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalarySlipRepository extends MongoRepository<SalarySlip, String> {
    List<SalarySlip> findByYearAndMonth(String year, String month);
   SalarySlip findByEmployeeCodeAndYearAndMonth(String employeeCode,String year,String month);
}
