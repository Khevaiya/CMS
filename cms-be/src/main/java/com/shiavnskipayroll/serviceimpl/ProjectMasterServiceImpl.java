package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.dto.request.ProjectMasterRequestDTO;
import com.shiavnskipayroll.dto.response.ConsultantMasterResponseDTO;
import com.shiavnskipayroll.dto.response.InternResponseDTO;
import com.shiavnskipayroll.dto.response.ProjectMasterResponseDTO;
import com.shiavnskipayroll.entity.*;
import com.shiavnskipayroll.exceptions.ClientNotFoundException;
import com.shiavnskipayroll.exceptions.EmployeeNotFoundException;
import com.shiavnskipayroll.exceptions.ResourceNotFoundException;
import com.shiavnskipayroll.exceptions.TimesheetProjectNotFoundException;
import com.shiavnskipayroll.repository.*;
import com.shiavnskipayroll.service.ProjectMasterService;
import com.shiavnskipayroll.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectMasterServiceImpl implements ProjectMasterService {


	private final ProjectRepository projectMasterRepository;
	private final ClientRepository clientRepository;
	private final EmployeeRepository employeeRepository;
	private final InternRepository internRepository;
	private final ConsultantMasterRepository consultantMasterRepository;
	private final S3Service s3Service;
	private final AssignProjectToEmployeeServiceImpl assignProjectToEmployeeServiceImpl;
	private final AssignProjectToConsultantServiceImpl assignProjectToConsultantServiceImpl;
	private final AssignProjectToInternServiceImpl assignProjectToInternServiceImpl;

    private  final  ConsultantProjectDetailsRepository consultantProjectDetailsRepository;
	@Lazy
	@Autowired
	private  ConsultantMasterServiceImpl consultantMasterServiceImpl;
	@Lazy
	@Autowired
	private    InternServiceImpl internServiceImpl;
	@Lazy
	@Autowired
	private      EmployeeMasterServiceImpl employeeMasterServiceImp;

	@Override
	public ProjectMasterResponseDTO createProject(ProjectMasterRequestDTO requestDTO, MultipartFile file1,
			MultipartFile file2) throws IOException {

		// Create and save the new project
		ProjectMaster projectMaster = new ProjectMaster();
		projectMaster.setClientId(requestDTO.getClientId());
		projectMaster.setProjectName(requestDTO.getProjectName());
		projectMaster.setDescription(requestDTO.getDescription());
		projectMaster.setStartDate(requestDTO.getStartDate());
		projectMaster.setEndDate(requestDTO.getEndDate());
		projectMaster.setRateCard(requestDTO.getRateCard());
		projectMaster.setResourceAllocation(requestDTO.getResourceAllocation());
		projectMaster.setReportingManager(requestDTO.getReportingManager());
		projectMaster.setConsultantId(null);
		projectMaster.setEmployeeId(null);
		projectMaster.setInternId(null);
		projectMaster.setCandidateType(requestDTO.getCandidateType());
		projectMaster.setInterviewdClearedCandidateId(requestDTO.getInterviewdClearedCandidateId());
		projectMaster.setLastWorkingDayOfMonth(requestDTO.getLastWorkingDayOfMonth());

		// Upload files to S3 and store their URLs
		if(file1!=null)
		{
		projectMaster.setProjectFileUrl1(s3Service.uploadFiles(file1,
				"ProjectsDocuments/" + projectMaster.getProjectName() + "/" + "project_details1.pdf"));
		}
		if(file2!=null)
		{
		projectMaster.setProjectFileUrl2(s3Service.uploadFiles(file2,
				"ProjectsDocuments/" + projectMaster.getProjectName() + "/" + "project_details2.pdf"));
		}
		ProjectMaster savedProject = projectMasterRepository.save(projectMaster);

		// Find the client by ID
		ClientMaster clientMaster = clientRepository.findById(savedProject.getClientId())
				.orElseThrow(() -> new ClientNotFoundException(savedProject.getClientId()));

		// Update the list of project IDs in the client
		List<String> listOfProjectIds = clientMaster.getListOfProjectIds();
		if (listOfProjectIds == null) {
			listOfProjectIds = new ArrayList<>();
		}

		// Add the newly created project's ID to the client's list of project IDs
		listOfProjectIds.add(savedProject.getId()); // Assuming getId() returns the project's ID
		clientMaster.setListOfProjectIds(listOfProjectIds);

		// Save the updated client
		clientRepository.save(clientMaster);

		return convertToResponseDTO(savedProject);
	}

	@Override
	public List<ProjectMasterResponseDTO> getAllProjects() {
		return projectMasterRepository.findAll().stream().map(this::convertToResponseDTO).toList();
	}

	@Override
	public ProjectMasterResponseDTO getProjectById(String id) {
		ProjectMaster project = projectMasterRepository.findById(id)
				.orElseThrow(() -> new TimesheetProjectNotFoundException("Project not found with id: " + id +" in class ProjectMasterServiceImpl ,method getProjectById"));
		return convertToResponseDTO(project);
	}

	@Override
	public ProjectMasterResponseDTO updateProject(String id, ProjectMasterRequestDTO requestDTO, MultipartFile file1,
			MultipartFile file2) throws IOException {

		ProjectMaster projectMaster = projectMasterRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Project not found"));

		projectMaster.setClientId(requestDTO.getClientId());
		projectMaster.setProjectName(requestDTO.getProjectName());
		projectMaster.setDescription(requestDTO.getDescription());
		projectMaster.setStartDate(requestDTO.getStartDate());
		projectMaster.setEndDate(requestDTO.getEndDate());
		projectMaster.setRateCard(requestDTO.getRateCard());
		projectMaster.setResourceAllocation(requestDTO.getResourceAllocation());
		projectMaster.setReportingManager(requestDTO.getReportingManager());
		projectMaster.setEmployeeId(requestDTO.getEmployeeId());

		// Upload files to S3 and store their URLs
		if (file1 != null || file2 != null) {
			if (file1 != null) {
				projectMaster.setProjectFileUrl1(s3Service.uploadFiles(file1,
						"ProjectsDocuments/" + projectMaster.getProjectName() + "/" + "project_details1.pdf"));
			}
			if (file2 != null) {
				projectMaster.setProjectFileUrl2(s3Service.uploadFiles(file2,
						"ProjectsDocuments/" + projectMaster.getProjectName() + "/" + "project_details2.pdf"));
			}
		}
		ProjectMaster updatedProject = projectMasterRepository.save(projectMaster);
		return convertToResponseDTO(updatedProject);
	}

	@Override
	public void deleteProject(String id) {
		ProjectMaster projectMaster = projectMasterRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Project not found"));
		        Set<String> setOfEmployeeIds=projectMaster.getEmployeeId();
		        if(setOfEmployeeIds!=null && !setOfEmployeeIds.isEmpty())
		        {
			    for (String employeeId : setOfEmployeeIds) {
				assignProjectToEmployeeServiceImpl.unassignProjectFromEmployee(id, employeeId);
			    }
		        }
			    Set<String> setOfConsultantIds=projectMaster.getConsultantId();
				if(setOfConsultantIds!=null && !setOfConsultantIds.isEmpty())
				{
			    for(String consultantId: setOfConsultantIds)
				{
					assignProjectToConsultantServiceImpl.unassignProjectFromConsultant(id,consultantId);
				}
				}
				Set<String> setOfInternIds=projectMaster.getInternId();
				if(setOfInternIds!=null && !setOfInternIds.isEmpty())
				{
				for(String internId:setOfInternIds)
				{
					assignProjectToInternServiceImpl.unassignProjectFromIntern(id,internId);
				}
				}
				projectMasterRepository.deleteById(id);

		}




	//--------------------------------------------------------------------
	@Override
	public List<InternResponseDTO> getInternsOfProjectById(String projectId) {
		// Fetch project data
		System.out.println("intern project project id");
		ProjectMaster projectMaster = projectMasterRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

		// Get intern IDs
		Set<String> listOfInternIds = projectMaster.getInternId();
		System.out.println("intern project project id listOfInternIds"+listOfInternIds);
		if (listOfInternIds==null ||listOfInternIds.isEmpty()) {
			return Collections.emptyList(); // Return empty list if no interns are associated
		}

		// Fetch interns using bulk operation
		List<Intern> interns = internRepository.findAllById(listOfInternIds);
		System.out.println("intern project project id"+interns);

		// Check for missing interns
		if (interns.size() < listOfInternIds.size()) {
			// Log missing interns for debugging
			Set<String> foundIds = interns.stream().map(Intern::getId).collect(Collectors.toSet());
			Set<String> missingIds = listOfInternIds.stream().filter(id -> !foundIds.contains(id))
					.collect(Collectors.toSet());
			log.info("Missing interns for project id: {} : {} ", projectId, missingIds);
		}

		return interns.stream().map(internServiceImpl::convertToResponseDTO).toList();
	}
	@Override
	public List<ConsultantMasterResponseDTO> getConsultantsOfProjectById(String projectId) {
		// Fetch project data
		ProjectMaster projectMaster = projectMasterRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

		// Get consultant IDs
		Set<String> listOfConsultantIds = projectMaster.getConsultantId();

		if (listOfConsultantIds ==null||listOfConsultantIds.isEmpty()) {
			return Collections.emptyList(); // Return empty list if no consultants are associated
		}

		// Fetch consultants using bulk operation
		List<ConsultantMaster> consultants = consultantMasterRepository.findAllById(listOfConsultantIds);

		// Check for missing consultants
		if (consultants.size() < listOfConsultantIds.size()) {
			// Log missing consultants for debugging
			Set<String> foundIds = consultants.stream().map(ConsultantMaster::getId).collect(Collectors.toSet());
			Set<String> missingIds = listOfConsultantIds.stream().filter(id -> !foundIds.contains(id))
					.collect(Collectors.toSet());
			log.info("Missing consultants for project id: {} : {} ", projectId, missingIds);
		}

		return consultants.stream()
				.map(consultantMasterServiceImpl::mapToConsultantResponseDTO)
				.toList();
	}

	//------------------------------------------------------------------------

	@Override
	public List<EmployeeMaster> getEmployeesOfProjectById(String projectId) {
		// Fetch project data
		ProjectMaster projectMaster = projectMasterRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

		// Get employee IDs
		Set<String> listOfEmployeeIds = projectMaster.getEmployeeId();
		System.out.println("12"+listOfEmployeeIds);
		if ( listOfEmployeeIds==null ||listOfEmployeeIds.isEmpty()  ) {

			return Collections.emptyList(); // Return empty list if no employees are associated
		}

		// Fetch employees using bulk operation
		List<EmployeeMaster> employees = employeeRepository.findAllById(listOfEmployeeIds);

		// Check for missing employees
		if (employees.size() < listOfEmployeeIds.size()) {
			// Log missing employees for debugging
			Set<String> foundIds = employees.stream().map(EmployeeMaster::getId).collect(Collectors.toSet());
			Set<String> missingIds = listOfEmployeeIds.stream().filter(id -> !foundIds.contains(id))
					.collect(Collectors.toSet());
			log.info("Missing employees for project id: {} : {} ", projectId, missingIds);
		}

		return employees;
	}

	private ProjectMasterResponseDTO convertToResponseDTO(ProjectMaster projectMaster) {
		ProjectMasterResponseDTO responseDTO = new ProjectMasterResponseDTO();
		responseDTO.setId(projectMaster.getId());
		responseDTO.setClientId(projectMaster.getClientId());
		responseDTO.setProjectName(projectMaster.getProjectName());
		responseDTO.setDescription(projectMaster.getDescription());
		responseDTO.setStartDate(projectMaster.getStartDate());
		responseDTO.setEndDate(projectMaster.getEndDate());
		responseDTO.setCandidateType(projectMaster.getCandidateType());
		responseDTO.setInterviewdClearedCandidateId(projectMaster.getInterviewdClearedCandidateId());
		responseDTO.setLastWorkingDayOfMonth(projectMaster.getLastWorkingDayOfMonth());
		responseDTO.setRateCard(projectMaster.getRateCard());
		responseDTO.setResourceAllocation(projectMaster.getResourceAllocation());
		responseDTO.setReportingManager(projectMaster.getReportingManager());
		responseDTO.setEmployeeId(projectMaster.getEmployeeId());
		responseDTO.setConsultantId(projectMaster.getConsultantId());
		responseDTO.setInternId(projectMaster.getInternId());
		return responseDTO;
	}

	// New method to fetch projects by employee ID
	@Override
	public List<ProjectMasterResponseDTO> getProjectsByEmployeeId(String employeeId) {
		List<ProjectMaster> projects = projectMasterRepository.findByEmployeeId(employeeId);
		return projects.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
	}
	public List<ProjectMaster> getProjectsByIds(List<String> ids) {
		List<ProjectMaster> projectMasterList = new ArrayList<>();

		for (String id : ids) {
			Optional<ProjectMaster> optionalProjectMaster = projectMasterRepository.findById(id);

			// Check if the Optional contains a value before accessing it
			if (optionalProjectMaster.isPresent()) {
				projectMasterList.add(optionalProjectMaster.get());
			} else {
				// Handle the case where the project is not found
				// You could log this or handle it as needed
				// For now, we just skip this project
				log.info("Project with ID {} not found.", id);
			}
		}

		return projectMasterList;
	}

	public List<ProjectMasterResponseDTO> getEmployeeProjectsByEmployeeId(String empId) {
		List<ProjectMaster> listOfProjectMaster = projectMasterRepository.findByEmployeeId(empId);
		if (listOfProjectMaster != null)
			return  listOfProjectMaster.stream()
					.map(this::convertToResponseDTO)
					.collect(Collectors.toList());
		else
			return null;// employee with not projects
	}
	//-----------------------------31oct---------------------------------

	@Override
	public List<byte[]> getProjectPdfData(String id) throws IOException {
		List<byte[]> projectFiles = new ArrayList<>();
		Optional<ProjectMaster> projectMasterMasterOpt = projectMasterRepository.findById(id);

		if (projectMasterMasterOpt.isPresent()) {
			ProjectMaster projectMaster = projectMasterMasterOpt.get();

			if (projectMaster.getProjectFileUrl1() != null) {
				projectFiles.add(getFileBytes(projectMaster.getProjectFileUrl1()));
			}
			if (projectMaster.getProjectFileUrl2() != null) {
				projectFiles.add(getFileBytes(projectMaster.getProjectFileUrl2()));
			}
		}

		return projectFiles;
	}
	//------------------------------30oct--------------------------
	public byte[] getFileBytes(String s3url) throws IOException {
		try(InputStream inputStream = s3Service.displayS3FileContent(s3url);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){

			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			return outputStream.toByteArray();
		}
	}
	//-----------------------------30oct---------------------------------------
	public List<String> convertToBase64(List<byte[]> imageFiles) {
		return imageFiles.stream()
				.map(Base64.getEncoder()::encodeToString)
				.toList();
	}
	//------------------------------------------------------------------------
 public Object getInterClearedCandidateById(String id,String candidateType)
{
	if(candidateType.equals("employee"))return employeeMasterServiceImp.getEmployeeById(id);
	else return 	consultantMasterServiceImpl.getConsultantById(id);
}

}
