package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.entity.Intern;
import com.shiavnskipayroll.serviceimpl.AssignProjectToInternServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class AssignProjectToInternController {
	

	private  final AssignProjectToInternServiceImpl assignProjectToInternServiceImpl;

	// POST method to assign project to an intern
	@PostMapping("/assignProjectToIntern")
	public ResponseEntity<String> assignProjectToIntern(@RequestParam String projectId, @RequestParam String internId) {
		try {
			assignProjectToInternServiceImpl.assignProjectToIntern(projectId, internId);
			return new ResponseEntity<>("Project assigned to intern", HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>("Error while assigning project to intern", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// DELETE method to unassign project from an intern
	@DeleteMapping("/unassignProjectFromIntern")
	public ResponseEntity<String> unassignProjectFromIntern(@RequestParam String projectId, @RequestParam String internId) {
		try {
			assignProjectToInternServiceImpl.unassignProjectFromIntern(projectId, internId);
			return new ResponseEntity<>("Project unassigned from intern", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error while unassigning project from intern",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// GET method to retrieve projects assigned to an intern by internId
	@GetMapping("/projectsByIntern/{internId}")
	public ResponseEntity<Set<String>> getProjectsByInternId(@PathVariable String internId) {
		try {
			Set<String> projects = assignProjectToInternServiceImpl.getProjectsByInternId(internId);
			return new ResponseEntity<>(projects, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// GET method to retrieve interns assigned to a project by projectId
	@GetMapping("/internsByProject/{projectId}")
	public ResponseEntity<Set<Intern>> getInternsByProjectId(@PathVariable String projectId) {
		try {
			Set<Intern> interns = assignProjectToInternServiceImpl.getInternsByProjectId(projectId);
			return new ResponseEntity<>(interns, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/getInternsWhichNotOnProjectByProjectId/{projectId}")
	public ResponseEntity<Set<Intern>>getInternsWhichNotOnProjectByProjectId(@PathVariable String projectId)
	{
		try {
			Set<Intern> interns = assignProjectToInternServiceImpl.getInternsWhichNotOnProjectByProjectId(projectId);
			return new ResponseEntity<>(interns, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		 
		
	}
}
