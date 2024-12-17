package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.dto.request.ProjectMasterRequestDTO;
import com.shiavnskipayroll.dto.response.ConsultantMasterResponseDTO;
import com.shiavnskipayroll.dto.response.InternResponseDTO;
import com.shiavnskipayroll.dto.response.ProjectMasterResponseDTO;
import com.shiavnskipayroll.entity.EmployeeMaster;
import com.shiavnskipayroll.entity.ProjectMaster;
import com.shiavnskipayroll.exceptions.ResourceNotFoundException;
import com.shiavnskipayroll.serviceimpl.ProjectMasterServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/project")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectMasterController {

	private  final ProjectMasterServiceImpl projectMasterServiceImpl;

	@GetMapping("/allByProjectIds")
	public ResponseEntity<List<ProjectMaster>> getProjectsByIds(@RequestParam List<String> ids) {
		System.out.println("allByProjectIds"+ids.toString());
		return new ResponseEntity<>(projectMasterServiceImpl.getProjectsByIds(ids), HttpStatus.OK);
	}

	@PostMapping("/create")
	
	public ResponseEntity<ProjectMasterResponseDTO> createProject(
			@ModelAttribute ProjectMasterRequestDTO projectMasterRequestDTO,
			@RequestParam(value = "file1", required = false) MultipartFile file1,
			@RequestParam(value = "file2", required = false) MultipartFile file2) {
		try {
			
//log.info("Create project called with Data "+projectMasterRequestDTO.toString()+" File1"+file1.getOriginalFilename()+"  file2"+file2.getOriginalFilename());
			ProjectMasterResponseDTO createdProject = projectMasterServiceImpl.createProject(projectMasterRequestDTO,
					file1,file2);
			return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@PreAuthorize("hasRole('client_admin')")
	@GetMapping("/all")
	public ResponseEntity<List<ProjectMasterResponseDTO>> getAllProjects() {
		try {
			List<ProjectMasterResponseDTO> projects = projectMasterServiceImpl.getAllProjects();
			return new ResponseEntity<>(projects, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProjectMasterResponseDTO> getProjectById(@PathVariable String id) {
		try {
			System.out.println("getProjectById called");
			ProjectMasterResponseDTO project = projectMasterServiceImpl.getProjectById(id);
			return new ResponseEntity<>(project, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<ProjectMasterResponseDTO> updateProject(@PathVariable String id,
			@ModelAttribute ProjectMasterRequestDTO projectMasterRequestDTO,
			@RequestParam(value = "file1", required = false) MultipartFile file1,
			@RequestParam(value = "file2", required = false) MultipartFile file2) {
		try {
			ProjectMasterResponseDTO updatedProject = projectMasterServiceImpl.updateProject(id,
					projectMasterRequestDTO, file1,file2);
			return new ResponseEntity<>(updatedProject, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProject(@PathVariable String id) {
		try {
			projectMasterServiceImpl.deleteProject(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("getEmployeesOfProjectById/{id}")
	public ResponseEntity<List<EmployeeMaster>> getEmployeesOfProjectById(@PathVariable String id) {
		try {
			List<EmployeeMaster> employees = projectMasterServiceImpl.getEmployeesOfProjectById(id);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			// Handle case where project or employees are not found
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			// Handle any other unexpected errors
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@PreAuthorize("hasRole('client_user')")
	@GetMapping("/getEmployeeProjectsByEmployeeId/{id}")
	public ResponseEntity<List<ProjectMasterResponseDTO>> getEmployeeProjectsByEmployeeId(@PathVariable String id) {
		try {

			List<ProjectMasterResponseDTO> listOfEmployeeProjects = projectMasterServiceImpl.getEmployeeProjectsByEmployeeId(id);
			return new ResponseEntity<>(listOfEmployeeProjects, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			// Handle case where project or employees are not found
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			// Handle any other unexpected errors
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//------------------------------31oct------------------------------------
	@GetMapping("/getProjectPdfData/{id}")
	public ResponseEntity<List<String>> getProjectPdfData(@PathVariable String id) {
		try {

			List<byte[]> listOfByte= projectMasterServiceImpl.getProjectPdfData(id);
			List<String>  base64Payslip=projectMasterServiceImpl.convertToBase64(listOfByte);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"projectDetail.pdf\"")
					.contentType(MediaType.APPLICATION_JSON)
					.body(base64Payslip);
		} catch (Exception e) {
			throw new RuntimeException("Failed to get employee payslip for the specified date", e);
		}
	}

	@GetMapping("/getInternsOfProjectByProjectId/{projectId}/interns")
	public ResponseEntity<List<InternResponseDTO>> getInternsOfProjectByProjectId(@PathVariable String projectId) {
		List<InternResponseDTO> interns = projectMasterServiceImpl.getInternsOfProjectById(projectId);
		return ResponseEntity.ok(interns);
	}

	// Endpoint to get consultants of a project by project ID
	@GetMapping("/getConsultantsOfProjectByProjectId/{projectId}/consultants")
	public ResponseEntity<List<ConsultantMasterResponseDTO>> getConsultantsOfProjectByProjectId(@PathVariable String projectId) {
		List<ConsultantMasterResponseDTO> consultants = projectMasterServiceImpl.getConsultantsOfProjectById(projectId);
		return ResponseEntity.ok(consultants);
	}

	@GetMapping("/getInterClearedCandidateById/{interviewClearedCandidateId}")
	public ResponseEntity<Object> getInterClearedCandidateById(@PathVariable String interviewClearedCandidateId,@RequestParam  String candidateType)
	{
		return new ResponseEntity<>(projectMasterServiceImpl.getInterClearedCandidateById(interviewClearedCandidateId,candidateType),HttpStatus.OK);
	}

}
