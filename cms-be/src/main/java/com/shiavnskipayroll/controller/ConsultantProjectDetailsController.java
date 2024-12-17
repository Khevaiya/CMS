package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.dto.request.ConsultantProjectDetailsRequestDTO;
import com.shiavnskipayroll.dto.response.ConsultantProjectDetailsResponseDTO;
import com.shiavnskipayroll.serviceimpl.ConsultantProjectDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/consultant-projects")
public class ConsultantProjectDetailsController {


	private final  ConsultantProjectDetailsServiceImpl consultantProjectDetailsService;

	@PostMapping
	public ResponseEntity<ConsultantProjectDetailsResponseDTO> createConsultantProject(
			@RequestBody ConsultantProjectDetailsRequestDTO requestDTO) {
		ConsultantProjectDetailsResponseDTO response = consultantProjectDetailsService
				.createConsultantProject(requestDTO);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<List<ConsultantProjectDetailsResponseDTO>> getAllConsultantProjects() {
		List<ConsultantProjectDetailsResponseDTO> projects = consultantProjectDetailsService.getAllConsultantProjects();
		return new ResponseEntity<>(projects, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ConsultantProjectDetailsResponseDTO> getConsultantProjectById(@PathVariable String id) {
		ConsultantProjectDetailsResponseDTO project = consultantProjectDetailsService.getConsultantProjectById(id);
		return project != null ? new ResponseEntity<>(project, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ConsultantProjectDetailsResponseDTO> updateConsultantProject(@PathVariable String id,
			@RequestBody ConsultantProjectDetailsRequestDTO requestDTO) {
		ConsultantProjectDetailsResponseDTO updatedProject = consultantProjectDetailsService.updateConsultantProject(id,
				requestDTO);
		return updatedProject != null ? new ResponseEntity<>(updatedProject, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteConsultantProject(@PathVariable String id) {
		consultantProjectDetailsService.deleteConsultantProject(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/getConsultantProjectDetailsByProjectIds")
	ResponseEntity<List<ConsultantProjectDetailsResponseDTO>> getConsultantProjectDetailsByProjectIds(@RequestParam List<String> ids)
	{
		return new ResponseEntity<>(consultantProjectDetailsService.consultantProjectDetailsByProjectIds(ids), HttpStatus.OK);
	}
}
