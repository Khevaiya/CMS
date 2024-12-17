package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.dto.request.SalarySlipRequestDTO;
import com.shiavnskipayroll.dto.response.SalarySlipResponseDTO;
import com.shiavnskipayroll.entity.EmployeeMaster;
import com.shiavnskipayroll.entity.SalarySlip;
import com.shiavnskipayroll.exceptions.EmployeeNotFoundException;
import com.shiavnskipayroll.exceptions.ResourceNotFoundException;
import com.shiavnskipayroll.repository.EmployeeRepository;
import com.shiavnskipayroll.repository.SalarySlipRepository;
import com.shiavnskipayroll.service.PdfGeneratorService;
import com.shiavnskipayroll.service.S3Service;
import com.shiavnskipayroll.service.SalarySlipService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SalarySlipServiceImpl implements SalarySlipService {

    private final SalarySlipRepository salarySlipRepository;
    private final PdfGeneratorService pdfGeneratorService; // Inject PdfGeneratorService
    private final S3Service s3Service; // Inject S3Service
    private final   EmployeeRepository employeeRepository;


    @Override
    public SalarySlipResponseDTO createSalarySlip(SalarySlipRequestDTO salarySlipRequestDTO) {
        SalarySlip alreadyExistSameMonthSalarySlip  =salarySlipRepository.findByEmployeeCodeAndYearAndMonth(
                salarySlipRequestDTO.getEmployeeCode(),
                salarySlipRequestDTO.getYear(),
                salarySlipRequestDTO.getMonth()
        );
        if (alreadyExistSameMonthSalarySlip != null) {
            this.deleteSalarySlip(alreadyExistSameMonthSalarySlip.getId());
        }
        SalarySlip salarySlip = new SalarySlip();
        mapRequestToEntity(salarySlipRequestDTO, salarySlip);
        try {
            MultipartFile pdfData = pdfGeneratorService.generateSalarySlipPdf(salarySlipRequestDTO); // Generate PDF from the saved entity
            LocalDate currentDate = LocalDate.now();
            // Extract the current year and month
            int currentYear = currentDate.getYear();
            int currentMonth = LocalDate.now().getMonthValue();
            String salarySlipPath = s3Service.uploadFiles(pdfData, "Payslip/Employee/" + salarySlipRequestDTO.getEmployeeCode() + "/" + currentYear + "/" + currentMonth + "/salaryslip.pdf");
            salarySlip.setSalarySlipUrlOfS3(salarySlipPath);
            SalarySlip savedSalarySlip = salarySlipRepository.save(salarySlip);

            return mapEntityToResponse(savedSalarySlip);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (log it, rethrow it, etc.)
            return mapEntityToResponse(salarySlip); // Return DTO with no PDF URL
        }
    }

    @Override
    public SalarySlipResponseDTO getSalarySlipById(String id) {
        Optional<SalarySlip> optionalSalarySlip = salarySlipRepository.findById(id);
        return optionalSalarySlip.map(this::mapEntityToResponse)
                .orElse(null); // Or throw an exception if not found
    }

    @Override
    public List<SalarySlipResponseDTO> getAllSalarySlips() {
        List<SalarySlip> salarySlips = salarySlipRepository.findAll();
        return salarySlips.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SalarySlipResponseDTO updateSalarySlip(String id, SalarySlipRequestDTO salarySlipRequestDTO) {
        Optional<SalarySlip> optionalSalarySlip = salarySlipRepository.findById(id);
        if (optionalSalarySlip.isPresent()) {
            SalarySlip salarySlip = optionalSalarySlip.get();
            mapRequestToEntity(salarySlipRequestDTO, salarySlip);
            SalarySlip updatedSalarySlip = salarySlipRepository.save(salarySlip);

            // Generate PDF after updating
            try {
                MultipartFile pdfData = pdfGeneratorService.generateSalarySlipPdf(salarySlipRequestDTO);
                String pdfUrl = s3Service.uploadFiles(pdfData, "salary_slips/" + updatedSalarySlip.getEmployeeCode() + "_salary_slip.pdf"); // Upload to S3

                // Set the PDF URL in the response DTO
                return mapEntityToResponse(updatedSalarySlip);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception (log it, rethrow it, etc.)
                return mapEntityToResponse(updatedSalarySlip); // Return DTO with no PDF URL
            }
        }
        return null; // Or throw an exception if not found
    }

    @Override
    public void deleteSalarySlip(String id) {
        salarySlipRepository.deleteById(id);
    }

    // Helper method to map from Request DTO to Entity
    private void mapRequestToEntity(SalarySlipRequestDTO requestDTO, SalarySlip salarySlip) {
        salarySlip.setFirstName(requestDTO.getFirstName());
        salarySlip.setEmployeeCode(requestDTO.getEmployeeCode());
        salarySlip.setDesignation(requestDTO.getDesignation());
        salarySlip.setPan(requestDTO.getPan());
        salarySlip.setBankAccountNumber(requestDTO.getBankAccountNumber());
        salarySlip.setBankName(requestDTO.getBankName());
        salarySlip.setDateofjoining(requestDTO.getDateofjoining());
        salarySlip.setMonth(requestDTO.getMonth());
        salarySlip.setYear(requestDTO.getYear());
        salarySlip.setTotalWorkingDays(requestDTO.getTotalWorkingDays());
        salarySlip.setWorkingDaysAttended(requestDTO.getWorkingDaysAttended());
        salarySlip.setLeavesTakenP(requestDTO.getLeavesTakenP());
        salarySlip.setLeavesTakenS(requestDTO.getLeavesTakenS());
        salarySlip.setBalanceLeavesP(requestDTO.getBalanceLeavesP());
        salarySlip.setBalanceLeavesS(requestDTO.getBalanceLeavesS());
        salarySlip.setBasicSalary(requestDTO.getBasicSalary());
        salarySlip.setDearnessAllowance(requestDTO.getDearnessAllowance());
        salarySlip.setHra(requestDTO.getHra());
        salarySlip.setConveyanceAllowance(requestDTO.getConveyanceAllowance());
        salarySlip.setMedicalAllowance(requestDTO.getMedicalAllowance());
        salarySlip.setSpecialAllowance(requestDTO.getSpecialAllowance());
        salarySlip.setOtherReceipts(requestDTO.getOtherReceipts());
        salarySlip.setTotalIncome(requestDTO.getTotalIncome());
        salarySlip.setPf(requestDTO.getPf());
        salarySlip.setProfessionalTax(requestDTO.getProfessionalTax());
        salarySlip.setTds(requestDTO.getTds());
        salarySlip.setOtherDeductions(requestDTO.getOtherDeductions());
        salarySlip.setTotalDeductions(requestDTO.getTotalDeductions());
        salarySlip.setNetSalary(requestDTO.getNetSalary());
    }

    // Helper method to map from Entity to Response DTO
    private SalarySlipResponseDTO mapEntityToResponse(SalarySlip salarySlip) {
        return SalarySlipResponseDTO.builder()
                .id(salarySlip.getId())
                .firstName(salarySlip.getFirstName())
                .employeeCode(salarySlip.getEmployeeCode())
                .designation(salarySlip.getDesignation())
                .pan(salarySlip.getPan())
                .bankAccountNumber(salarySlip.getBankAccountNumber())
                .bankName(salarySlip.getBankName())
                .dateofjoining(salarySlip.getDateofjoining())
                .month(salarySlip.getMonth())
                .year(salarySlip.getYear())
                .totalWorkingDays(salarySlip.getTotalWorkingDays())
                .workingDaysAttended(salarySlip.getWorkingDaysAttended())
                .leavesTakenP(salarySlip.getLeavesTakenP())
                .leavesTakenS(salarySlip.getLeavesTakenS())
                .balanceLeavesS(salarySlip.getBalanceLeavesP())
                .balanceLeavesS(salarySlip.getBalanceLeavesS())
                .basicSalary(salarySlip.getBasicSalary())
                .dearnessAllowance(salarySlip.getDearnessAllowance())
                .hra(salarySlip.getHra())
                .conveyanceAllowance(salarySlip.getConveyanceAllowance())
                .medicalAllowance(salarySlip.getMedicalAllowance())
                .specialAllowance(salarySlip.getSpecialAllowance())
                .otherReceipts(salarySlip.getOtherReceipts())
                .totalIncome(salarySlip.getTotalIncome())
                .pf(salarySlip.getPf())
                .professionalTax(salarySlip.getProfessionalTax())
                .tds(salarySlip.getTds())
                .otherDeductions(salarySlip.getOtherDeductions())
                .totalDeductions(salarySlip.getTotalDeductions())
                .netSalary(salarySlip.getNetSalary())
                // .pdfUrl(pdfUrl) // Add the PDF URL to the response DTO
                .build();
    }

    //-----------------------------Download File From S3-----------------------------------------------
    public byte[] downloadFile(String id) throws IOException {

        Optional<SalarySlip> salarySlipOptional = salarySlipRepository.findById(id);
        if (salarySlipOptional.isEmpty())
            throw new ResourceNotFoundException("SalarySlip not found in class SalarySlipServiceImpl ,method downloadFile");
        SalarySlip salarySlip = salarySlipOptional.get();
        return s3Service.downloadFile(salarySlip.getSalarySlipUrlOfS3());
    }
    //-----------------------------31 OCT----------------------------------------
    public String getPayslipByDate(String id, int year, int month) throws IOException {
        Optional<EmployeeMaster> optionalEmployee=employeeRepository.findById(id);
        if(optionalEmployee.isEmpty())throw new EmployeeNotFoundException("Employee not found with id "+id+" in class SalarySlipServiceImpl, method getPayslipByDate");
        EmployeeMaster em=optionalEmployee.get();
        String employeeCode = em.getEmployeeUniqueId();
        String filePath = "Payslip/Employee/" + employeeCode + "/" + year + "/" + month + "/salaryslip.pdf";
        String path= "https://shiavsnkimanagementbucket.s3.  .amazonaws.com/"+filePath;
     try(InputStream inputStream = s3Service.displayS3FileContent(path);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] payslipData= outputStream.toByteArray();
         return Base64.getEncoder().encodeToString(payslipData);
        }

    }
    //-------------------------------------
    public List<String >getPayslipByYearAndMonth(String year, String month) throws IOException
    {
        List<SalarySlip> listOfSalarySlips=salarySlipRepository.findByYearAndMonth(year,month);
        List<byte[]> fileBytes=new ArrayList<>();
        for(SalarySlip salarySlip:listOfSalarySlips)
        {
            fileBytes.add(getFileBytes(salarySlip.getSalarySlipUrlOfS3()));
        }
        return convertToBase64(fileBytes);
    }
    public byte[] getFileBytes(String s3url) throws IOException {
        try(InputStream inputStream = s3Service.displayS3FileContent(s3url);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }
    //-----------------------------30oct---------------------------------------
    public List<String> convertToBase64(List<byte[]> imageFiles) {
        return imageFiles.stream()
                .map(Base64.getEncoder()::encodeToString)
                .toList();
    }
    //-----------------------------
}
