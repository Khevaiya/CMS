package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.controller.AuthController;
import com.shiavnskipayroll.dto.request.EmployeeMasterRequestDTO;
import com.shiavnskipayroll.dto.request.AdminRegisterRequestDTO;
import com.shiavnskipayroll.dto.response.EmployeeMasterResponseDTO;
import com.shiavnskipayroll.dto.response.ProjectMasterResponseDTO;
import com.shiavnskipayroll.entity.EmployeeMaster;
import com.shiavnskipayroll.entity.ProjectMaster;
import com.shiavnskipayroll.exceptions.ResourceNotFoundException;
import com.shiavnskipayroll.exceptions.UserAlreadyExistsException;
import com.shiavnskipayroll.repository.EmployeeRepository;
import com.shiavnskipayroll.repository.ProjectRepository;
import com.shiavnskipayroll.service.EmployeeService;
import com.shiavnskipayroll.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class EmployeeMasterServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	private final ProjectRepository projectRepository;

	private final S3Service s3Service;

	private final ProjectMasterServiceImpl projectMasterServiceImpl;
	private final AuthController auth;

	// Utility method to generate custom employeeUniqueId
	private String generateEmployeeUniqueId() {
		long count = employeeRepository.count(); // Count number of employees in the DB
		long nextId = count + 1001; // Start from 1001 and increment with each new employee
		return "EMP_ST_" + nextId;
	}

	@Override
	public void createEmployee(EmployeeMasterRequestDTO employeeMasterRequestDTO, MultipartFile photo,
			MultipartFile panPhoto, MultipartFile passbookPhoto, MultipartFile aadhaarPhoto) throws IOException {

		if (employeeRepository.existsByEmail(employeeMasterRequestDTO.getEmail())) {
			throw new UserAlreadyExistsException("email already exits in employee");
		}
		System.out.println("create empl"+employeeMasterRequestDTO.toString());
		EmployeeMaster employee = new EmployeeMaster();
		String username = generateEmployeeUniqueId();
		employee.setEmployeeUniqueId(username);
		if (photo != null && !photo.isEmpty()) {
			String photoPathForStorageInS3 = "Employee" + "/" + employee.getEmployeeUniqueId() + "/photo" + "/"
					+ "photo.png";
			employeeMasterRequestDTO.setPhotoUrl(s3Service.uploadFiles(photo, photoPathForStorageInS3));
		}
		if (panPhoto != null && !panPhoto.isEmpty()) {
			String panPhotoForStorageInS3 = "Employee" + "/" + employee.getEmployeeUniqueId() + "/panPhoto" + "/"
					+ "panPhoto.png";
			employeeMasterRequestDTO.setPanPhotoUrl(s3Service.uploadFiles(panPhoto, panPhotoForStorageInS3));
		}
		if (passbookPhoto != null && !passbookPhoto.isEmpty()) {
			String passbookPhotoForStorageInS3 = "Employee" + "/" + employee.getEmployeeUniqueId() + "/passbookPhoto"
					+ "/" + "passbookPhoto.png";
			employeeMasterRequestDTO
					.setPassbookPhotoUrl(s3Service.uploadFiles(passbookPhoto, passbookPhotoForStorageInS3));
		}
		if (aadhaarPhoto != null && !aadhaarPhoto.isEmpty()) {
			String aadhaarPhotoForStorageInS3 = "Employee" + "/" + employee.getEmployeeUniqueId() + "/aadhaarPhoto"
					+ "/" + "aadhaarPhoto.png";
			employeeMasterRequestDTO
					.setAadhaarPhotoUrl(s3Service.uploadFiles(aadhaarPhoto, aadhaarPhotoForStorageInS3));
		}

		employee.setFirstName(employeeMasterRequestDTO.getFirstName());
		employee.setLastName(employeeMasterRequestDTO.getLastName());
		employee.setPassword(employeeMasterRequestDTO.getPassword());
		employee.setEmail(employeeMasterRequestDTO.getEmail());
		employee.setContactNo(employeeMasterRequestDTO.getContactNo());
		employee.setAddress(employeeMasterRequestDTO.getAddress());
		employee.setState(employeeMasterRequestDTO.getState());
		employee.setCity(employeeMasterRequestDTO.getCity());
		employee.setCountry(employeeMasterRequestDTO.getCountry());
		employee.setEmergencyContactNo(employeeMasterRequestDTO.getEmergencyContactNo());
		employee.setEmergencyEmail(employeeMasterRequestDTO.getEmergencyEmail());
		employee.setPersonalEmail1(employeeMasterRequestDTO.getPersonalEmail1());
		employee.setPersonalEmail2(employeeMasterRequestDTO.getPersonalEmail2());
		employee.setPosition(employeeMasterRequestDTO.getPosition());
		employee.setDateOfJoining(employeeMasterRequestDTO.getDateofjoining());
		employee.setIsActive(employeeMasterRequestDTO.getIsActive());
		employee.setPhotoUrl(employeeMasterRequestDTO.getPhotoUrl());
		employee.setAadhaarNo(employeeMasterRequestDTO.getAadhaarNo());
		employee.setAadhaarPhotoUrl(employeeMasterRequestDTO.getAadhaarPhotoUrl());
		employee.setPanNo(employeeMasterRequestDTO.getPanNo());
		employee.setPanPhotoUrl(employeeMasterRequestDTO.getPanPhotoUrl());
		employee.setPassportNo(employeeMasterRequestDTO.getPassportNo());
		employee.setDrivingLicenseNo(employeeMasterRequestDTO.getDrivingLicenseNo());
		employee.setVoterId(employeeMasterRequestDTO.getVoterId());
		employee.setBankAccountNo(employeeMasterRequestDTO.getBankAccountNo());
		employee.setBankName(employeeMasterRequestDTO.getBankName());
		employee.setIfscCode(employeeMasterRequestDTO.getIfscCode());
		employee.setPassbookPhotoUrl(employeeMasterRequestDTO.getPassbookPhotoUrl());
		employee.setCtc(employeeMasterRequestDTO.getCtc());
		employee.setPliAmount(employeeMasterRequestDTO.getPliAmount());
		employee.setBasic(employeeMasterRequestDTO.getBasic());
		employee.setPf(employeeMasterRequestDTO.getPf());
		employee.setBonusAmount(employeeMasterRequestDTO.getBonusAmount());

		AdminRegisterRequestDTO adminRegisterRequestDTO = new AdminRegisterRequestDTO();
		adminRegisterRequestDTO.setRole("user");
		adminRegisterRequestDTO.setEmail(employeeMasterRequestDTO.getEmail());
		adminRegisterRequestDTO.setFirstName(employeeMasterRequestDTO.getFirstName());
		adminRegisterRequestDTO.setLastName(employeeMasterRequestDTO.getLastName());
		adminRegisterRequestDTO.setUsername(username);
		adminRegisterRequestDTO.setPassword(employeeMasterRequestDTO.getPassword());
		
		employee.setKeycloackId(auth.registerUser(adminRegisterRequestDTO));
		
		
		employeeRepository.save(employee);

	}

	@Override
	public List<EmployeeMasterResponseDTO> getAllEmployees() {

		List<EmployeeMaster> employees = employeeRepository.findAll();

		return employees.stream().map(this::convertToResponseDTO).toList();
	}

	@Override
	public EmployeeMasterResponseDTO getEmployeeById(String id) {
		Optional<EmployeeMaster> employee = employeeRepository.findById(id);
		return employee.map(this::convertToResponseDTO).orElse(null);
	}

	@Override
	public boolean updateEmployee(String id, EmployeeMasterRequestDTO employeeMasterRequestDTO, MultipartFile photo,
			MultipartFile panPhoto, MultipartFile passbookPhoto, MultipartFile aadhaarPhoto) throws IOException {
		//System.out.println(" addhaar photot"+aadhaarPhoto.getOriginalFilename());
		if (photo != null && !photo.isEmpty()) {
			String photoPathForStorageInS3 = "Employee" + "/" + employeeMasterRequestDTO.getEmployeeUniqueId()
					+ "/photo" + "/" + "photo.png";
			employeeMasterRequestDTO.setPhotoUrl(s3Service.uploadFiles(photo, photoPathForStorageInS3));
		}
		if (panPhoto != null && !panPhoto.isEmpty()) {
			String panPhotoForStorageInS3 = "Employee" + "/" + employeeMasterRequestDTO.getEmployeeUniqueId()
					+ "/panPhoto" + "/" + "panPhoto.png";
			employeeMasterRequestDTO.setPanPhotoUrl(s3Service.uploadFiles(panPhoto, panPhotoForStorageInS3));
		}
		if (passbookPhoto != null && !passbookPhoto.isEmpty()) {
			String passbookPhotoForStorageInS3 = "Employee" + "/" + employeeMasterRequestDTO.getEmployeeUniqueId()
					+ "/passbookPhoto" + "/" + "passbookPhoto.png";
			employeeMasterRequestDTO
					.setPassbookPhotoUrl(s3Service.uploadFiles(passbookPhoto, passbookPhotoForStorageInS3));
		}
		if (aadhaarPhoto != null && !aadhaarPhoto.isEmpty()) {
			String aadhaarPhotoForStorageInS3 = "Employee" + "/" + employeeMasterRequestDTO.getEmployeeUniqueId()
					+ "/aadhaarPhoto" + "/" + "aadhaarPhoto.png";
			employeeMasterRequestDTO
					.setAadhaarPhotoUrl(s3Service.uploadFiles(aadhaarPhoto, aadhaarPhotoForStorageInS3));
		}
		Optional<EmployeeMaster> optionalEmployee = employeeRepository.findById(id);
		if (optionalEmployee.isPresent()) {
			EmployeeMaster employee = optionalEmployee.get();
			employee.setEmployeeUniqueId(employeeMasterRequestDTO.getEmployeeUniqueId());
			employee.setFirstName(employeeMasterRequestDTO.getFirstName());
			employee.setLastName(employeeMasterRequestDTO.getLastName());

			employee.setEmail(employeeMasterRequestDTO.getEmail());
			employee.setContactNo(employeeMasterRequestDTO.getContactNo());
			employee.setAddress(employeeMasterRequestDTO.getAddress());
			employee.setState(employeeMasterRequestDTO.getState());
			employee.setCity(employeeMasterRequestDTO.getCity());
			employee.setDateOfJoining(employeeMasterRequestDTO.getDateofjoining());
			employee.setCountry(employeeMasterRequestDTO.getCountry());
			employee.setEmergencyContactNo(employeeMasterRequestDTO.getEmergencyContactNo());
			employee.setEmergencyEmail(employeeMasterRequestDTO.getEmergencyEmail());
			employee.setPersonalEmail1(employeeMasterRequestDTO.getPersonalEmail1());
			employee.setPersonalEmail2(employeeMasterRequestDTO.getPersonalEmail2());
			employee.setPosition(employeeMasterRequestDTO.getPosition());
			employee.setIsActive(employeeMasterRequestDTO.getIsActive());
			employee.setPhotoUrl(employeeMasterRequestDTO.getPhotoUrl());
			employee.setAadhaarNo(employeeMasterRequestDTO.getAadhaarNo());
			employee.setAadhaarPhotoUrl(employeeMasterRequestDTO.getAadhaarPhotoUrl());
			employee.setPanNo(employeeMasterRequestDTO.getPanNo());
			employee.setPanPhotoUrl(employeeMasterRequestDTO.getPanPhotoUrl());
			employee.setPassportNo(employeeMasterRequestDTO.getPassportNo());
			employee.setDrivingLicenseNo(employeeMasterRequestDTO.getDrivingLicenseNo());
			employee.setVoterId(employeeMasterRequestDTO.getVoterId());
			employee.setBankAccountNo(employeeMasterRequestDTO.getBankAccountNo());
			employee.setIfscCode(employeeMasterRequestDTO.getIfscCode());
			employee.setPassbookPhotoUrl(employeeMasterRequestDTO.getPassbookPhotoUrl());
			employee.setCtc(employeeMasterRequestDTO.getCtc());
			employee.setPliAmount(employeeMasterRequestDTO.getPliAmount());
			employee.setBasic(employeeMasterRequestDTO.getBasic());
			employee.setPf(employeeMasterRequestDTO.getPf());
			employee.setBonusAmount(employeeMasterRequestDTO.getBonusAmount());
			employee.setBankName(employeeMasterRequestDTO.getBankName());
			if (employeeMasterRequestDTO.getPassword() != null && !employeeMasterRequestDTO.getPassword().isEmpty()) {
				employee.setPassword(employeeMasterRequestDTO.getPassword());
			}

			AdminRegisterRequestDTO adminRegisterRequestDTO = new AdminRegisterRequestDTO();

			adminRegisterRequestDTO.setEmail(employeeMasterRequestDTO.getEmail());
			adminRegisterRequestDTO.setFirstName(employeeMasterRequestDTO.getFirstName());
			adminRegisterRequestDTO.setLastName(employeeMasterRequestDTO.getLastName());
			adminRegisterRequestDTO.setPassword(employeeMasterRequestDTO.getPassword());

			auth.updateUser(employee.getKeycloackId(), adminRegisterRequestDTO);

			employeeRepository.save(employee);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteEmployee(String id) {
		 // Check if the employee exists
	    if (!employeeRepository.existsById(id)) 
	        return false;
	    EmployeeMaster employee = employeeRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Employee not found"));
		 // Remove employee from associated projects
	    Set<String> projectIds = employee.getProjectId();
		if(projectIds!=null) {
			//we can call unassign-project employeeId projectId
			for (String projectId : projectIds) {
				ProjectMaster project = projectRepository.findById(projectId)
						.orElseThrow(() -> new RuntimeException("Project with ID " + projectId + " not found"));

				// Remove employee ID from the project's employee list
				Set<String> employeeIdsOnProject = project.getEmployeeId(); // Assuming getEmployeeIds() exists
				employeeIdsOnProject.remove(id);
				project.setEmployeeId(employeeIdsOnProject); // Update the project
				projectRepository.save(project); // Save the updated project
			}
		}
	    
		auth.deleteUser(employee.getKeycloackId());
	    employeeRepository.deleteById(id);
	    return true;
	}
	    

	EmployeeMasterResponseDTO convertToResponseDTO(EmployeeMaster employee) {
		EmployeeMasterResponseDTO dto = new EmployeeMasterResponseDTO();
		dto.setId(employee.getId());
		dto.setEmployeeUniqueId(employee.getEmployeeUniqueId());
		dto.setFirstName(employee.getFirstName());
		dto.setLastName(employee.getLastName());
		dto.setEmail(employee.getEmail());
		dto.setContactNo(employee.getContactNo());
		dto.setAddress(employee.getAddress());
		dto.setState(employee.getState());
		dto.setCity(employee.getCity());
		dto.setCountry(employee.getCountry());
		dto.setEmergencyContactNo(employee.getEmergencyContactNo());
		dto.setEmergencyEmail(employee.getEmergencyEmail());
		dto.setPersonalEmail1(employee.getPersonalEmail1());
		dto.setPersonalEmail2(employee.getPersonalEmail2());
		dto.setPosition(employee.getPosition());
		dto.setDateofjoining(employee.getDateOfJoining());
		dto.setIsActive(employee.getIsActive());
		dto.setPhotoUrl(employee.getPhotoUrl());
		dto.setAadhaarNo(employee.getAadhaarNo());
		dto.setAadhaarPhotoUrl(employee.getAadhaarPhotoUrl());
		dto.setPanNo(employee.getPanNo());
		dto.setPanPhotoUrl(employee.getPanPhotoUrl());
		dto.setPassportNo(employee.getPassportNo());
		dto.setDrivingLicenseNo(employee.getDrivingLicenseNo());
		dto.setVoterId(employee.getVoterId());
		dto.setBankAccountNo(employee.getBankAccountNo());
		dto.setIfscCode(employee.getIfscCode());
		dto.setBankName(employee.getBankName());
		dto.setPassbookPhotoUrl(employee.getPassbookPhotoUrl());
		dto.setCtc(employee.getCtc());
		dto.setPliAmount(employee.getPliAmount());
		dto.setBasic(employee.getBasic());
		dto.setPf(employee.getPf());
		dto.setBonusAmount(employee.getBonusAmount());
		dto.setProjectId(employee.getProjectId());
		return dto;
	}

	@Override
	public List<ProjectMaster> getProjectsOfEmployeeById(String employeeId) {
		// Fetch employee data
		EmployeeMaster employeeMaster = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

		// Get project IDs
		Set<String> listOfProjectIds = employeeMaster.getProjectId();
		if (listOfProjectIds == null || listOfProjectIds.isEmpty()) {
			return Collections.emptyList(); // No projects associated with this employee
		}

		// Fetch projects using bulk operation
		List<ProjectMaster> projects = projectRepository.findAllById(listOfProjectIds);

		// Log any missing projects
		if (projects.size() < listOfProjectIds.size()) {
			Set<String> foundProjectIds = projects.stream().map(ProjectMaster::getId).collect(Collectors.toSet());
			Set<String> missingProjectIds = listOfProjectIds.stream()
					.filter(projectId -> !foundProjectIds.contains(projectId)).collect(Collectors.toSet());

			log.info("Missing projects: {}", missingProjectIds);
		}

		return projects;
	}

//---------------------------------------------------------------------
	public List<EmployeeMasterResponseDTO> getEmployeesWithoutProjects() {
		// Fetch all employees
		List<EmployeeMasterResponseDTO> allEmployees = getAllEmployees();
		if (allEmployees == null) {
			return null;
		}

		// Fetch all projects
		List<ProjectMasterResponseDTO> allProjectDTOs = projectMasterServiceImpl.getAllProjects();

		// Flatten the list of employee IDs from projects into a single set, ignoring
		// null values
		Set<String> employeesWithProjects = allProjectDTOs.stream().map(ProjectMasterResponseDTO::getEmployeeId)
				.filter(Objects::nonNull) // Filter out null sets
				.flatMap(Set::stream) // Flatten each set of employee IDs into a single stream
				.collect(Collectors.toSet());

		// Return only employees who are not in employeesWithProjects set
		return allEmployees.stream().filter(employee -> !employeesWithProjects.contains(employee.getId())).toList();
	}

	// -----------------------30oct---------------------------
	@Override
	public List<byte[]> getEmployeeImageData(String id) throws IOException {
		List<byte[]> employeeFiles = new ArrayList<>();
		Optional<EmployeeMaster> employeeMasterOpt = employeeRepository.findById(id);

		if (employeeMasterOpt.isPresent()) {
			EmployeeMaster employeeMaster = employeeMasterOpt.get();

			if (employeeMaster.getPhotoUrl() != null) {
				employeeFiles.add(getFileBytes(employeeMaster.getPhotoUrl()));
			}
			if (employeeMaster.getPanPhotoUrl() != null) {
				employeeFiles.add(getFileBytes(employeeMaster.getPanPhotoUrl()));
			}
			if (employeeMaster.getPassbookPhotoUrl() != null) {
				employeeFiles.add(getFileBytes(employeeMaster.getPassbookPhotoUrl()));
			}
			if (employeeMaster.getAadhaarPhotoUrl() != null) {
				employeeFiles.add(getFileBytes(employeeMaster.getAadhaarPhotoUrl()));
			}
			
		}

		return employeeFiles;
	}

	// ------------------------------30oct--------------------------
	public byte[] getFileBytes(String s3url) throws IOException {
		try (InputStream inputStream = s3Service.displayS3FileContent(s3url);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			return outputStream.toByteArray();
		}
	}

	// -----------------------------30oct---------------------------------------
	public List<String> convertToBase64(List<byte[]> imageFiles) {
		return imageFiles.stream().map(Base64.getEncoder()::encodeToString).toList();
	}

}
