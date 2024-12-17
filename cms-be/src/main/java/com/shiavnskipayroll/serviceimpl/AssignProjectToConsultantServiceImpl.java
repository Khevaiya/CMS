package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.entity.ConsultantMaster;
import com.shiavnskipayroll.entity.ConsultantProjectDetails;
import com.shiavnskipayroll.entity.ProjectMaster;
import com.shiavnskipayroll.exceptions.ConsultantNotFoundException;
import com.shiavnskipayroll.exceptions.ProjectNotFoundException;
import com.shiavnskipayroll.repository.ConsultantMasterRepository;
import com.shiavnskipayroll.repository.ConsultantProjectDetailsRepository;
import com.shiavnskipayroll.repository.ProjectRepository;
import com.shiavnskipayroll.service.AssignProjectToConsultantService;
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
public class AssignProjectToConsultantServiceImpl implements AssignProjectToConsultantService {

	private final ConsultantMasterRepository consultantMasterRepository;
	private final ProjectRepository projectRepository;
	private final ConsultantProjectDetailsRepository consultantProjectDetailsRepository;
	
	
	@Override
	public void assignProjectToConsultant(String projectId, String consultantId) {
		// Retrieve consultant
		ConsultantMaster consultant = consultantMasterRepository.findById(consultantId)
				.orElseThrow(() -> new ConsultantNotFoundException("consultant not found with id "+consultantId+"in class AssignProjectToConsultantServiceImpl,method assignProjectToConsultant "));

		// Update project IDs for consultant
		Set<String> setOfProjectIds = consultant.getProjectId();
		if (setOfProjectIds == null) {
			setOfProjectIds = new HashSet<>();
		}
		setOfProjectIds.add(projectId);
		consultant.setProjectId(setOfProjectIds);

		// Retrieve project By Id
		ProjectMaster project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project not found with id "+projectId+"in class AssignProjectToConsultantServiceImpl,method assignProjectToConsultant "));

		// Update consultant IDs for project
		Set<String> setOfConsultantIds = project.getConsultantId();

		if (setOfConsultantIds == null) {
			setOfConsultantIds = new HashSet<>();
		}

		setOfConsultantIds.add(consultantId);
		project.setConsultantId(setOfConsultantIds);

		// Saving all updates
			consultantMasterRepository.save(consultant);
			projectRepository.save(project);
			log.info("Successfully assigned project ID {} to consultant ID {}", projectId, consultantId);
		
	}

	@Override
	public void unassignProjectFromConsultant(String projectId, String consultantId) {
		// Retrieve consultant
		ConsultantMaster consultant = consultantMasterRepository.findById(consultantId)
				.orElseThrow(() -> new ConsultantNotFoundException("consultant not found with id "+consultantId+"in class AssignProjectToConsultantServiceImpl,method assignProjectToConsultant "));

		// Update project IDs for consultant (remove projectId)
		Set<String> setOfProjectIds = consultant.getProjectId();
		if (setOfProjectIds == null || !setOfProjectIds.contains(projectId)) {
			log.error("Project ID {} not found in consultant ID {}", projectId, consultantId);
			throw new IllegalArgumentException(
					"Project ID " + projectId + " not found in consultant ID " + consultantId+" in class AssignProjectToConsultantServiceImpl,method unassignProjectFromConsultant" );
		}
		setOfProjectIds.remove(projectId);
		consultant.setProjectId(setOfProjectIds);

		// Retrieve project
		ProjectMaster project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project not found with id "+projectId+"in class AssignProjectToConsultantServiceImpl,method unassignProjectFromConsultant "));

		// Update consultant IDs for project (remove consultantId)
		Set<String> setOfConsultantIds = project.getConsultantId();

		if (setOfConsultantIds == null || !setOfConsultantIds.contains(consultantId)) {
			log.error("Consultant ID {} not found in project ID {}", consultantId, projectId);
			throw new IllegalArgumentException(
					"Consultant ID " + consultantId + " not found in Projectwhos is ID " + projectId+" in class AssignProjectToConsultantServiceImpl,method unassignProjectFromConsultant" );
		}
		setOfConsultantIds.remove(consultantId);
		project.setConsultantId(setOfConsultantIds);
		ConsultantProjectDetails consultantProjectDetails=consultantProjectDetailsRepository.findByProjectId(projectId);
		if(consultantProjectDetails!=null) {
        	consultantProjectDetailsRepository.delete(consultantProjectDetails);
		}
			consultantMasterRepository.save(consultant);
			projectRepository.save(project);
			log.info("Successfully unassigned project ID {} from consultant ID {}", projectId, consultantId);
		
	}

	@Override
	public Set<String> getProjectsByConsultantId(String consultantId) {
		ConsultantMaster consultant = consultantMasterRepository.findById(consultantId)
				.orElseThrow(() -> new ConsultantNotFoundException(consultantId));

		Set<String> projects = consultant.getProjectId();
		return projects != null ? projects : new HashSet<>();
	}

	@Override
	public Set<ConsultantMaster> getConsultantsByProjectId(String projectId) {
	    ProjectMaster projectMaster = projectRepository.findById(projectId)
	            .orElseThrow(() -> new ProjectNotFoundException("Project not found with id "+projectId+"in class AssignProjectToConsultantServiceImpl,method getConsultantsByProjectId "));
	    Set<String> consultantsIds = projectMaster.getConsultantId();
		 if (consultantsIds == null || consultantsIds.isEmpty()) {
			 return Collections.emptySet();

	    }
	    return consultantMasterRepository.findAllById(consultantsIds).stream().collect(Collectors.toSet());
	}
	

	public Set<ConsultantMaster> getConsultantsWhichNotOnProjectByProjectId(String projectId)
		  {
			    Set<String> consultantIdsOnProject = projectRepository.findById(projectId)
			            .orElseThrow(() -> new ProjectNotFoundException("Project not found with id "+projectId+"in class AssignProjectToConsultantServiceImpl,method getConsultantsByProjectId "))
			     	      .getConsultantId(); // Assumes ProjectMaster has a field `consultantIds`

			    // Use an empty set if consultantIdsOnProject is null to ensure it's not null
			    final Set<String> finalConsultantIdsOnProject = consultantIdsOnProject != null ? consultantIdsOnProject : new HashSet<>();
			    // Retrieve all consultants and filter those not assigned to the project
			    Set<ConsultantMaster> consultantsNotOnProject = consultantMasterRepository.findAll().stream()
			            .filter(consultant -> !finalConsultantIdsOnProject.contains(consultant.getId()))
			            .collect(Collectors.toSet());
			    // Retrieve all consultants and filter those not assigned to the project
			    if (consultantsNotOnProject.isEmpty()) {
					return Collections.emptySet();

			 	      }

			    return consultantsNotOnProject;
          }
}
