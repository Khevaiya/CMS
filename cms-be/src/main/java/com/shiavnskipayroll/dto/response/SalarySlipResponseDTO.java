package com.shiavnskipayroll.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalarySlipResponseDTO {

    private String id;                    // ID of the salary slip
    private String firstName;          // Name of the employee
    private String employeeCode;          // Unique employee code
    private String designation;           // Job designation
    private String pan;                   // Permanent Account Number
    private String bankAccountNumber;     // Bank account number for salary deposits
    private String bankName;              // Name of the bank
    private String dateofjoining;      // Date when the employee joined
    private String month;                 // Month for which the salary slip is generated
    private String year;
    private Integer totalWorkingDays;     // Total working days in the month
    private Integer workingDaysAttended;  // Number of days the employee attended work
    private Integer leavesTakenS;          // Number of S leaves taken by the employee
    private Integer leavesTakenP;          // Number of P leaves taken by the employee
    private Integer balanceLeavesS;        // Remaining S leaves available
    private Integer balanceLeavesP;        // Remaining P leaves available

    // Income Fields
    private BigDecimal basicSalary;       // Basic salary of the employee
    private BigDecimal dearnessAllowance; // Dearness allowance
    private BigDecimal hra;                // House rent allowance
    private BigDecimal conveyanceAllowance; // Conveyance allowance
    private BigDecimal medicalAllowance;   // Medical allowance
    private BigDecimal specialAllowance;   // Special allowances
    private BigDecimal otherReceipts;      // Any other receipts
    private BigDecimal totalIncome;        // Total income calculated

    // Deductions Fields
    private BigDecimal pf;                 // Provident fund deduction
    private BigDecimal professionalTax;    // Professional tax
    private BigDecimal tds;                // Tax deducted at source
    private BigDecimal otherDeductions;    // Any other deductions
    private BigDecimal totalDeductions;    // Total deductions calculated

    // Net Salary
    private BigDecimal netSalary;          // Net salary after deductions

    private String message;                // Optional message or status
  
}
