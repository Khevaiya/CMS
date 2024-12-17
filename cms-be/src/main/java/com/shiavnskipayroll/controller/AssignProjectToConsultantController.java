package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.entity.ConsultantMaster;
import com.shiavnskipayroll.service.AssignProjectToConsultantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/consultants")
public class AssignProjectToConsultantController {


	private final AssignProjectToConsultantService assignProjectToConsultantService;

	@PostMapping("/{consultantId}/projects/{projectId}")
	public ResponseEntity<String> assignProject(@PathVariable String consultantId, @PathVariable String projectId) {
		assignProjectToConsultantService.assignProjectToConsultant(projectId, consultantId);
		return ResponseEntity.ok("Project assigned successfully.");
	}

	@DeleteMapping("/unassignProjectFromConsultant")
	public ResponseEntity<String> unassignProject(@RequestParam String consultantId, @RequestParam String projectId) {
		assignProjectToConsultantService.unassignProjectFromConsultant(projectId, consultantId);
		return ResponseEntity.ok("Project unassigned successfully.");
	}

	@GetMapping("/{consultantId}/projects")
	public ResponseEntity<Set<String>> getProjects(@PathVariable String consultantId) {
		Set<String> projects = assignProjectToConsultantService.getProjectsByConsultantId(consultantId);
		return ResponseEntity.ok(projects);
	}
	@GetMapping("/projects/{projectId}/consultants")
	public ResponseEntity<Set<ConsultantMaster>> getConsultants(@PathVariable String projectId) {
		Set<ConsultantMaster> consultants = assignProjectToConsultantService.getConsultantsByProjectId(projectId);
		return ResponseEntity.ok(consultants);
	}
	@GetMapping("/projects/{projectId}")
	public ResponseEntity<Set<ConsultantMaster>> getConsultantsWhichNotOnProjectByProjectId(@PathVariable String projectId){
		Set<ConsultantMaster> consultants = assignProjectToConsultantService.getConsultantsWhichNotOnProjectByProjectId(projectId);
		return ResponseEntity.ok(consultants);
	}
	
	
}
