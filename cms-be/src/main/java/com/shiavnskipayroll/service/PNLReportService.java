package com.shiavnskipayroll.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiavnskipayroll.dto.response.EmployeeMasterResponseDTO;
import com.shiavnskipayroll.dto.response.PNLReportResponseDTO;
import com.shiavnskipayroll.entity.ConsultantMaster;
import com.shiavnskipayroll.entity.ConsultantProjectDetails;
import com.shiavnskipayroll.entity.EmployeeMaster;
import com.shiavnskipayroll.entity.ProjectMaster;
import com.shiavnskipayroll.exceptions.ConsultantNotFoundException;
import com.shiavnskipayroll.repository.CompanyDetailsRepository;
import com.shiavnskipayroll.repository.ConsultantMasterRepository;
import com.shiavnskipayroll.repository.ConsultantProjectDetailsRepository;
import com.shiavnskipayroll.repository.EmployeeRepository;
import com.shiavnskipayroll.repository.ProjectRepository;
import com.shiavnskipayroll.serviceimpl.EmployeeMasterServiceImpl;

@Service
public class PNLReportService {
	    @Autowired
		private ProjectRepository projectRepository;
		@Autowired
		private ConsultantMasterRepository consultantMasterRepository;
		@Autowired
		private ConsultantProjectDetailsRepository consultantProjectDetailsRepository;
		@Autowired
		private CompanyDetailsRepository companyDetailsRepository;
		@Autowired
		private EmployeeRepository employeeRepository;
		@Autowired
		private EmployeeMasterServiceImpl employeeMasterServiceImpl;

      public double  calculatePAndLAmount(double companyExpenses)
      {
    	  double leaveDeductionAmount=0.0;//remove when we take variable in ProjectMaster
    	  //replace CTC of employee with payable amount per month
         double totalPnl = 0.0;
         double totalProjectAmountAfterConsultantCommison=0.0;
         double projectRateCardMinusConsultantCommison=0.0;
         double totalProjectsAmountAfterAllConsultantCommison_MinusCompanyExpenses=0.0;
         double  totalAmountOfprojectAfterCommsionExpenseEmployeeSalary=0.0;
         double projeectRateCardMinusProjectLeavesDeductionAmount=0.0;
         
         //get ListOf consultantprojectDetailsWhich so that we can minus consultantcommison and project Leaves amount deduction according to its project Rate Card
        List<ConsultantProjectDetails> listOfConsultantProjectDetails=consultantProjectDetailsRepository.findAll();
        if (listOfConsultantProjectDetails == null || listOfConsultantProjectDetails.isEmpty()) {
            return 0.0; //throw exception
        }
        for(ConsultantProjectDetails consultantProjectDetail: listOfConsultantProjectDetails)
        {
    	Optional<ProjectMaster> optionalProjectMaster=projectRepository.findById(consultantProjectDetail.getProjectId());
    	if(optionalProjectMaster.isEmpty())throw new IllegalArgumentException("Project with ID " + consultantProjectDetail.getProjectId() + " not found in method calculatePAndLAmount,class PNLReportService");//throw exception here 
    	ProjectMaster pm=optionalProjectMaster.get(); 	
    	projeectRateCardMinusProjectLeavesDeductionAmount=Double.parseDouble(pm.getRateCard())-leaveDeductionAmount;//pm.leaveDeductionAmount  
    	projectRateCardMinusConsultantCommison=projeectRateCardMinusProjectLeavesDeductionAmount-Double.parseDouble(consultantProjectDetail.getCommission());
    	totalProjectAmountAfterConsultantCommison=totalProjectAmountAfterConsultantCommison+projectRateCardMinusConsultantCommison;
        }
        //minus comapanyExpenses
         totalProjectsAmountAfterAllConsultantCommison_MinusCompanyExpenses =totalProjectAmountAfterConsultantCommison-companyExpenses;
         //loop start get all employees which are on projects so that we can minus salary from totalProjectsAmountAfterAllConsultantCommison_MinusCompanyExpenses
        List<EmployeeMasterResponseDTO> employeesWhichAreOnProjects=employeeRepository.findByProjectIdIsNotNullAndProjectIdIsNotEmpty(); //test this method first ,its non-tested method 

        for(EmployeeMasterResponseDTO employeeWhichAreOnProjects: employeesWhichAreOnProjects)
    	{
    		totalProjectsAmountAfterAllConsultantCommison_MinusCompanyExpenses=totalProjectsAmountAfterAllConsultantCommison_MinusCompanyExpenses-getTotalAmountPayableToEmployeeAccordingToEmployeeProjects(employeeWhichAreOnProjects);
    	}
    	totalAmountOfprojectAfterCommsionExpenseEmployeeSalary=totalProjectsAmountAfterAllConsultantCommison_MinusCompanyExpenses;

    	//loop to get all employee which are not on project so that we can minus Employee  salary from totalAmountOfprojectAfterCommsionExpenseEmployeeSalary
    	List<EmployeeMasterResponseDTO> employeesWithoutProjects=employeeMasterServiceImpl.getEmployeesWithoutProjects();
    	for( EmployeeMasterResponseDTO employeeWithoutProjects:employeesWithoutProjects)
        {
    		totalAmountOfprojectAfterCommsionExpenseEmployeeSalary=totalAmountOfprojectAfterCommsionExpenseEmployeeSalary-getSalaryOfEmployeeWhichNotOnProject(employeeWithoutProjects);
	    }
    	totalPnl=totalAmountOfprojectAfterCommsionExpenseEmployeeSalary;
    	return totalPnl;
    }

      
    //Helper methods
	double getTotalAmountPayableToEmployeeAccordingToEmployeeProjects(EmployeeMasterResponseDTO em) {
		// totalAmountPayByEachProjectToEmployee
		 if (em.getProjectId() == null || em.getProjectId().isEmpty()) {
	            return 0.0;
	        }
		return em.getCtc() / em.getProjectId().size();
	}

	double getSalaryOfEmployeeWhichNotOnProject(EmployeeMasterResponseDTO em) {
		return em.getCtc();
	}

//-------------------------------------------------------------------------------
	 public List<PNLReportResponseDTO> generatePNLReportForAllProjects(double officeExpanditure) {
		    List<ProjectMaster> projects = projectRepository.findAll();
		    double officeShare = officeExpanditure / projects.size();
		    double pnl = 0;
		    double LeaveDeductionsOfProject=0.0;//replace this  by taking variable in ProjectMaster class leave deduction
		    
		    List<PNLReportResponseDTO> report = new ArrayList<>();

		    for (ProjectMaster p : projects) {
		        double baseAmountOfProject = Double.parseDouble(p.getRateCard());
		        double totalCostsOfProject = 0.0; // Reset at the start of each project
		        double adjustedBilling = baseAmountOfProject;

		        // Subtract leave deductions
		        double perticularProjectLeavesDeductions =LeaveDeductionsOfProject ;//p.getLeaveDeductions(); // Get leave deductions from the project
		        adjustedBilling -= perticularProjectLeavesDeductions;

		        // Consultant Costs
		        ConsultantProjectDetails cpd = consultantProjectDetailsRepository.findByProjectId(p.getId());
		        double consultantCosts = Double.parseDouble(cpd.getCommission());
		        adjustedBilling -= consultantCosts;
		        totalCostsOfProject += consultantCosts;

		        // Office Expenditure Allocation
		        adjustedBilling -= officeShare;
		        totalCostsOfProject += officeShare;

		        // Employee-Specific Costs
		        double employeeCosts = 0.0;
		        for (EmployeeMasterResponseDTO emp : employeeRepository.findByProjectIdIsNotNullAndProjectIdIsNotEmpty()) {
		            employeeCosts += getTotalAmountPayableToEmployeeAccordingToEmployeeProjects(emp);
		            adjustedBilling -= getTotalAmountPayableToEmployeeAccordingToEmployeeProjects(emp);
		        }
		        totalCostsOfProject += employeeCosts;

		        // Calculate net earnings for this project
		        double netEarningsOfProject = adjustedBilling;

		        // Add to overall P&L if profitable
		        if (netEarningsOfProject > 0) {
		            pnl += netEarningsOfProject;
		        }

		        // Add project data to report
		        PNLReportResponseDTO projectReport = new PNLReportResponseDTO(
		            p.getProjectName(),
		            p.getId(),
		            baseAmountOfProject,
		            totalCostsOfProject,
		            netEarningsOfProject,
		            netEarningsOfProject > 0
		        );
		        report.add(projectReport);
		    }
		    
		    return report;
		}

	   

}
