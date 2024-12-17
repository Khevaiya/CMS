package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.entity.Intern;
import com.shiavnskipayroll.entity.ProjectMaster;
import com.shiavnskipayroll.exceptions.InternNotFoundException;
import com.shiavnskipayroll.exceptions.ProjectNotFoundException;
import com.shiavnskipayroll.repository.InternRepository;
import com.shiavnskipayroll.repository.ProjectRepository;
import com.shiavnskipayroll.service.AssignProjectToInternService;
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
public class AssignProjectToInternServiceImpl implements AssignProjectToInternService {

	private final InternRepository internRepository;

	private final ProjectRepository projectRepository;

	@Override
	public void assignProjectToIntern(String projectId, String internId) {
		// Retrieve intern
		Intern internMaster = internRepository.findById(internId)
				.orElseThrow(() -> new InternNotFoundException(internId));

		// Update project IDs for intern
		Set<String> setOfProjectIds = internMaster.getProjectId();
		if (setOfProjectIds == null) {
			setOfProjectIds = new HashSet<>();
		}
		setOfProjectIds.add(projectId);
		internMaster.setProjectId(setOfProjectIds);

		// Retrieve project
		ProjectMaster projectMaster = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException(projectId));

		// Update intern IDs for project
		Set<String> setOfInternIds = projectMaster.getInternId();
		if (setOfInternIds == null) {
			setOfInternIds = new HashSet<>();
		}
		setOfInternIds.add(internId);
		projectMaster.setInternId(setOfInternIds);

		// Save updates
			internRepository.save(internMaster);
			projectRepository.save(projectMaster);
			log.info("Successfully assigned project ID {} to intern ID {}", projectId, internId);
	}

	@Override
	public void unassignProjectFromIntern(String projectId, String internId) {
		// Retrieve intern
		Intern internMaster = internRepository.findById(internId)
				.orElseThrow(() -> new InternNotFoundException(internId));

		// Remove project ID from intern
		Set<String> setOfProjectIds = internMaster.getProjectId();
		if (setOfProjectIds != null && setOfProjectIds.contains(projectId)) {
			setOfProjectIds.remove(projectId);
			internMaster.setProjectId(setOfProjectIds);

			// Save intern update
		internRepository.save(internMaster);
		} else {
			log.warn("Project ID {} not found in intern ID {}", projectId, internId);
		}

		// Retrieve project
		ProjectMaster projectMaster = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException(projectId));

		// Remove intern ID from project
		Set<String> setOfInternIds = projectMaster.getInternId();
		if (setOfInternIds != null && setOfInternIds.contains(internId)) {
			setOfInternIds.remove(internId);
			projectMaster.setInternId(setOfInternIds);
			// Save project update
		projectRepository.save(projectMaster);
		} else {
			log.warn("Intern ID {} not found in project ID {}", internId, projectId);
		}
	}

	@Override
	public Set<String> getProjectsByInternId(String internId) {
		Intern internMaster = internRepository.findById(internId)
				.orElseThrow(() -> new InternNotFoundException(internId));

		Set<String> projects = internMaster.getProjectId();
		return projects != null ? projects : new HashSet<>();
	}


	@Override
	public Set<Intern> getInternsByProjectId(String projectId) {
	    ProjectMaster projectMaster = projectRepository.findById(projectId)
	            .orElseThrow(() -> new ProjectNotFoundException(projectId));
	    Set<String> internsIds = projectMaster.getInternId();
	    return internsIds != null
	            ? internRepository.findAllById(internsIds).stream().collect(Collectors.toSet())
	            : new HashSet<>();
	}
	//assign
	 public Set<Intern> getInternsWhichNotOnProjectByProjectId(String projectId)
	  {
		 // Get intern IDs associated with the project
		   Set<String> internIdsOnProject = projectRepository.findById(projectId)
		            .orElseThrow(() -> new ProjectNotFoundException(projectId))
		            .getInternId();
			if(internIdsOnProject==null)
			{
				internIdsOnProject=new HashSet<>();
			}
final  Set<String> finalInternIdsOnProject=internIdsOnProject;
		    // Retrieve all interns and filter those not assigned to the project
		    Set<Intern> internsNotOnProject = internRepository.findAll().stream()
		            .filter(intern -> !finalInternIdsOnProject.contains(intern.getId()))
		            .collect(Collectors.toSet());
			if(internsNotOnProject==null)
			{
				return Collections.emptySet();

			}

		    return internsNotOnProject;
		  
	  }
	

}
