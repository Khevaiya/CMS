package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.entity.EmployeeMaster;
import com.shiavnskipayroll.serviceimpl.AssignProjectToEmployeeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class AssignProjectToEmployeeController {


	private  final AssignProjectToEmployeeServiceImpl assignProjectToEmployeeServiceImpl;

	@PostMapping("/assignProjectToEmployee")
	ResponseEntity<String> assignProjectToEmployee( @RequestParam String projectId, @RequestParam String employeeId) {

		try {
			assignProjectToEmployeeServiceImpl.assignProjectToEmployee(projectId, employeeId);
			return new ResponseEntity<>("Project assign to employee", HttpStatus.OK);
		} catch (Exception exception) {

			return new ResponseEntity<>("Error while assigning project to employee", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/unassignProjectFromEmployee")
	public ResponseEntity<String> unassignProjectFromEmployee(@RequestParam String projectId,
			@RequestParam String employeeId) {
		try {
			assignProjectToEmployeeServiceImpl.unassignProjectFromEmployee(projectId, employeeId);
			return new ResponseEntity<>("Project unassigned from employee", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error while unassigning project from employee",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/projectsByEmployee/{employeeId}")
	public ResponseEntity<Set<String>> getProjectsByEmployeeId(@PathVariable String employeeId) {

		try {
			Set<String> projects = assignProjectToEmployeeServiceImpl.getProjectsByEmployeeId(employeeId);
			return new ResponseEntity<>(projects, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/employeesByProject/id/{projectId}")
	public ResponseEntity<Set<EmployeeMaster>> getEmployeesByProjectId(@PathVariable String projectId) {
		try {
			Set<EmployeeMaster> employees = assignProjectToEmployeeServiceImpl.getEmployeesByProjectId(projectId);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/employeesByProjects/{projectId}")
	public ResponseEntity<Set<EmployeeMaster>> getEmployeesWhichNotOnProjectByProjectId(@PathVariable String projectId) {
		try {

			Set<EmployeeMaster> employees = assignProjectToEmployeeServiceImpl.getEmployeesWhichNotOnProjectByProjectId(projectId);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	

}
