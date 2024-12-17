package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.entity.EmployeeMaster;
import com.shiavnskipayroll.entity.ProjectMaster;
import com.shiavnskipayroll.exceptions.EmployeeNotFoundException;
import com.shiavnskipayroll.exceptions.ProjectNotFoundException;
import com.shiavnskipayroll.repository.EmployeeRepository;
import com.shiavnskipayroll.repository.ProjectRepository;
import com.shiavnskipayroll.service.AssignProjectToEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignProjectToEmployeeServiceImpl implements AssignProjectToEmployeeService {

	private final EmployeeRepository employeeRepository;
	private final ProjectRepository projectRepository;

	@Override
	public void assignProjectToEmployee(String projectId, String employeeId) {
		// Retrieve employee
		EmployeeMaster employeeMaster = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new EmployeeNotFoundException(employeeId));

		// Update project IDs for employee
		Set<String> setOfProjectIds = employeeMaster.getProjectId();
		if (setOfProjectIds == null) {
			setOfProjectIds = new HashSet<>();
		}
		setOfProjectIds.add(projectId);
		employeeMaster.setProjectId(setOfProjectIds);

		// Retrieve project
		ProjectMaster projectMaster = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException(projectId));

		// Update employee IDs for project
		Set<String> setOfEmployeeIds = projectMaster.getEmployeeId();
		if (setOfEmployeeIds == null) {
			setOfEmployeeIds = new HashSet<>();
		}
		setOfEmployeeIds.add(employeeId);
		projectMaster.setEmployeeId(setOfEmployeeIds);

		// Save updates

		employeeRepository.save(employeeMaster);
		projectRepository.save(projectMaster);
		log.info("Successfully assigned project ID {} to employee ID {}", projectId, employeeId);
	}

	@Override
	public void unassignProjectFromEmployee(String projectId, String employeeId) {
		// Retrieve employee
		EmployeeMaster employeeMaster = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new EmployeeNotFoundException(employeeId));

		// Remove project ID from employee
		Set<String> setOfProjectIds = employeeMaster.getProjectId();
		if (setOfProjectIds != null && setOfProjectIds.contains(projectId)) {
			setOfProjectIds.remove(projectId);
			employeeMaster.setProjectId(setOfProjectIds);

			// Save employee update
			employeeRepository.save(employeeMaster);
		} else {
			log.warn("Project ID {} not found in employee ID {}", projectId, employeeId);
		}

		// Retrieve project
		ProjectMaster projectMaster = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException(projectId));

		// Remove employee ID from project
		Set<String> setOfEmployeeIds = projectMaster.getEmployeeId();
		if (setOfEmployeeIds != null && setOfEmployeeIds.contains(employeeId)) {
			setOfEmployeeIds.remove(employeeId);
			projectMaster.setEmployeeId(setOfEmployeeIds);
			// Save project update
			projectRepository.save(projectMaster);

		} else {
			log.warn("Employee ID {} not found in project ID {}", employeeId, projectId);
		}
	}

	@Override
	public Set<String> getProjectsByEmployeeId(String employeeId) {
		EmployeeMaster employeeMaster = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new EmployeeNotFoundException(employeeId));

		Set<String> projects = employeeMaster.getProjectId();
		return projects != null ? projects : new HashSet<>();
	}

	@Override
	public Set<EmployeeMaster> getEmployeesByProjectId(String projectId) {
		ProjectMaster projectMaster = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException(projectId));

		Set<String> employeeIds = projectMaster.getEmployeeId();

		return employeeIds != null
				? employeeRepository.findAllById(employeeIds).stream().collect(Collectors.toSet())
				: new HashSet<>();
	}


	//assign
	public Set<EmployeeMaster> getEmployeesWhichNotOnProjectByProjectId(String projectId) {


		// Retrieve employee IDs associated with the project
		Set<String> employeeIdsOnProject = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException(projectId))
				.getEmployeeId();

		// Ensure employeeIdsOnProject is effectively final by initializing if null
		if (employeeIdsOnProject == null) {
			employeeIdsOnProject = Collections.emptySet();
		}


		// Retrieve all employees and filter out those assigned to the project
		Set<String> finalEmployeeIdsOnProject = employeeIdsOnProject;
		Set<EmployeeMaster> employeesNotOnProject = employeeRepository.findAll().stream()
				.filter(employee -> !finalEmployeeIdsOnProject.contains(employee.getId()))
				.collect(Collectors.toSet());



		if (employeesNotOnProject.isEmpty()) {
			return Collections.emptySet();

		}

		return employeesNotOnProject;
	}





}
