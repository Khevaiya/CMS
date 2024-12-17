package com.shiavnskipayroll.service;

import com.shiavnskipayroll.dto.request.ProjectMasterRequestDTO;
import com.shiavnskipayroll.dto.response.ConsultantMasterResponseDTO;
import com.shiavnskipayroll.dto.response.InternResponseDTO;
import com.shiavnskipayroll.dto.response.ProjectMasterResponseDTO;
import com.shiavnskipayroll.entity.EmployeeMaster;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProjectMasterService {
    ProjectMasterResponseDTO createProject(ProjectMasterRequestDTO projectMasterRequestDTO, MultipartFile file1,MultipartFile file2) throws IOException;
    List<ProjectMasterResponseDTO> getAllProjects();
    ProjectMasterResponseDTO getProjectById(String id);
    ProjectMasterResponseDTO updateProject(String id, ProjectMasterRequestDTO requestDTO,
			MultipartFile file1,MultipartFile file2) throws IOException;
    void deleteProject(String id);
    List<EmployeeMaster> getEmployeesOfProjectById(String projectId);
    List<ProjectMasterResponseDTO> getProjectsByEmployeeId(String employeeId);
    List<byte[]> getProjectPdfData(String id) throws IOException;
    List<InternResponseDTO> getInternsOfProjectById(String projectId);
    List<ConsultantMasterResponseDTO> getConsultantsOfProjectById(String projectId);
}
