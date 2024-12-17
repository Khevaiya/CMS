package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.dto.request.EmployeeMasterRequestDTO;
import com.shiavnskipayroll.dto.response.EmployeeMasterResponseDTO;
import com.shiavnskipayroll.entity.ProjectMaster;
import com.shiavnskipayroll.exceptions.ResourceNotFoundException;
import com.shiavnskipayroll.serviceimpl.EmployeeMasterServiceImpl;
import com.shiavnskipayroll.serviceimpl.ProjectMasterServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {


	private final  EmployeeMasterServiceImpl employeeMasterServiceImpl;

	private final  ProjectMasterServiceImpl projectMasterServiceImpl;

	// ----------------------------------------------------------------------------------

	@PostMapping("/create")
	public ResponseEntity<?> createEmployee(@ModelAttribute EmployeeMasterRequestDTO employeeMasterRequestDTO,
			@RequestParam(value = "photo", required = false) MultipartFile photo,
			@RequestParam(value = "panPhoto", required = false) MultipartFile panPhoto,
			@RequestParam(value = "passbookPhoto", required = false) MultipartFile passbookPhoto,
			@RequestParam(value = "aadhaarPhoto", required = false) MultipartFile aadhaarPhoto) {
		try {
			
			employeeMasterServiceImpl.createEmployee(employeeMasterRequestDTO, photo, panPhoto, passbookPhoto,
					aadhaarPhoto);
			return new ResponseEntity<>("Employee created successfully", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Error creating employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ------------------------------------------------------------------------------------------
	@GetMapping("/all")
public ResponseEntity<List<EmployeeMasterResponseDTO>> getAllEmployees() {
		List<EmployeeMasterResponseDTO> employees = employeeMasterServiceImpl.getAllEmployees();
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	// ----------------------------------------------------------------------------------------
	//@PreAuthorize("hasRole('client_user')")
	@GetMapping("/getById/{id}")
	public ResponseEntity<EmployeeMasterResponseDTO> getEmployeeById(@PathVariable String id) {

		EmployeeMasterResponseDTO employee = employeeMasterServiceImpl.getEmployeeById(id);
		return employee != null ? new ResponseEntity<>(employee, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// ---------------------------------------------------------------------------------------------
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable String id,
			@ModelAttribute EmployeeMasterRequestDTO employeeMasterRequestDTO,
			@RequestParam(value = "photo", required = false) MultipartFile photo,
			@RequestParam(value = "panPhoto", required = false) MultipartFile panPhoto,
			@RequestParam(value = "passbookPhoto", required = false) MultipartFile passbookPhoto,
			@RequestParam(value = "aadhaarPhoto", required = false) MultipartFile aadhaarPhoto) {
		try {
			//System.out.println(" addhaar photot"+aadhaarPhoto.getOriginalFilename());
			
			//System.out.println("photo"+photo.getOriginalFilename()+"panPhoto"+panPhoto.getOriginalFilename()+"passbookPhoto"+passbookPhoto.getOriginalFilename()+"addhaphot"+aadhaarPhoto.getOriginalFilename());
			boolean isUpdated = employeeMasterServiceImpl.updateEmployee(id, employeeMasterRequestDTO, photo, panPhoto,
					passbookPhoto, aadhaarPhoto);
			return isUpdated ? new ResponseEntity<>("Employee updated successfully", HttpStatus.OK)
					: new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Error updating employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ------------------------------------------------------------------------------------------------
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
		try {
			employeeMasterServiceImpl.deleteEmployee(id);
			return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error deleting employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ----------------------------------------------------------------------------------------------------
	@GetMapping("getProjectsOfEmployeeById/{id}")
	public ResponseEntity<List<ProjectMaster>> getProjectsOfEmployeeById(@PathVariable String id) {
		try {
			List<ProjectMaster> projects = employeeMasterServiceImpl.getProjectsOfEmployeeById(id); // id is fine here
			return new ResponseEntity<>(projects, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Employee not found
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Unexpected error
		}
	}

	// -----------------------------------------------------------------------------------------------
	@GetMapping("/withoutProjects")
	public ResponseEntity<List<EmployeeMasterResponseDTO>> getEmployeesWithoutProjects() {
		try {

			List<EmployeeMasterResponseDTO> employeesWithoutProjects = employeeMasterServiceImpl
					.getEmployeesWithoutProjects();
			return new ResponseEntity<>(employeesWithoutProjects, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle error appropriately
		}
	}
	//----------------------------------30oct---------------------------------------------------------------
	@GetMapping("/displayEmployeeImages/{id}")
	public ResponseEntity<List<String>> getEmployeeImagesAsBase64(@PathVariable String id) {
		try {
			List<byte[]> imageData = employeeMasterServiceImpl.getEmployeeImageData(id);
			List<String> base64Images = employeeMasterServiceImpl.convertToBase64(imageData);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline")
					.contentType(MediaType.APPLICATION_JSON)
					.body(base64Images);
		} catch (Exception e) {
			throw new RuntimeException("Failed to get employee images", e);
		}
	}


}
