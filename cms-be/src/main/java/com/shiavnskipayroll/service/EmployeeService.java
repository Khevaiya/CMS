package com.shiavnskipayroll.service;

import com.shiavnskipayroll.dto.request.EmployeeMasterRequestDTO;
import com.shiavnskipayroll.dto.response.EmployeeMasterResponseDTO;
import com.shiavnskipayroll.entity.ProjectMaster;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {

	void createEmployee(EmployeeMasterRequestDTO employeeMasterRequestDTO,MultipartFile photo, MultipartFile panPhoto,MultipartFile passbookPhoto,MultipartFile aadhaarPhoto) throws IOException ;
    List<EmployeeMasterResponseDTO> getAllEmployees();
    EmployeeMasterResponseDTO getEmployeeById(String id);
    boolean updateEmployee(String id, EmployeeMasterRequestDTO employeeMasterRequestDTO,MultipartFile photo, MultipartFile panPhoto,MultipartFile passbookPhoto,MultipartFile aadhaarPhoto) throws IOException ;
    boolean deleteEmployee(String id);
     List<ProjectMaster> getProjectsOfEmployeeById(String id);
     List<EmployeeMasterResponseDTO> getEmployeesWithoutProjects() ;
    List<byte[]> getEmployeeImageData(String id) throws IOException;

}
