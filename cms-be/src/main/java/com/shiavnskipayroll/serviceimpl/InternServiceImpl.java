package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.controller.AuthController;
import com.shiavnskipayroll.dto.request.InternRequestDTO;
import com.shiavnskipayroll.dto.request.AdminRegisterRequestDTO;
import com.shiavnskipayroll.dto.response.InternResponseDTO;
import com.shiavnskipayroll.dto.response.ProjectMasterResponseDTO;
import com.shiavnskipayroll.entity.Intern;
import com.shiavnskipayroll.entity.ProjectMaster;
import com.shiavnskipayroll.exceptions.InternNotFoundException;
import com.shiavnskipayroll.exceptions.ResourceNotFoundException;
import com.shiavnskipayroll.repository.InternRepository;
import com.shiavnskipayroll.repository.ProjectRepository;
import com.shiavnskipayroll.service.InternService;
import com.shiavnskipayroll.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternServiceImpl implements InternService {
	Logger logger = Logger.getLogger(getClass().getName());

	private final InternRepository internRepository;
	private final ProjectRepository projectRepository;
	private final S3Service s3Service;
	private final ProjectMasterServiceImpl projectMasterServiceImpl;
	private final AuthController auth;

	// Utility method to generate unique intern ID (only for interns)
	private String generateInternUniqueId() {
		long count = internRepository.count(); // Count the number of interns in the DB
		long nextId = count + 1001; // Start from 1001 and increment with each new intern
		return "INT_ST_" + nextId;
	}

	@Override
	public InternResponseDTO createIntern(InternRequestDTO internRequestDTO, MultipartFile photo,
			MultipartFile recentMarksheetOrDegreePhoto, MultipartFile panPhoto, MultipartFile aadhaarPhoto,
			MultipartFile passbookPhoto) throws IOException {

		Intern intern = new Intern();
		String username = generateInternUniqueId();
		intern.setInternUniqueId(username);
		intern.setLastName(internRequestDTO.getLastName());
		intern.setInternName(internRequestDTO.getInternName());
		intern.setPassword(internRequestDTO.getPassword());
		intern.setPersonalEmail(internRequestDTO.getPersonalEmail());
		intern.setContactNo(internRequestDTO.getContactNo());
		intern.setAddress(internRequestDTO.getAddress());
		intern.setInternType(internRequestDTO.getInternType());
		intern.setDateOfJoining(internRequestDTO.getDateOfJoining());
		intern.setIsActive(internRequestDTO.getIsActive());
		intern.setPhotoUrl(
				s3Service.uploadFiles(photo, "Intern/" + intern.getInternUniqueId() + "/Photo/" + "photo.png")); // filUploads
		intern.setAadhaarNo(internRequestDTO.getAadhaarNo());
		intern.setAadhaarPhotoUrl(s3Service.uploadFiles(aadhaarPhoto,
				"Intern/" + intern.getInternUniqueId() + "/AadhaarPhoto/" + "aadhaarPhoto.png"));
		intern.setPanNo(internRequestDTO.getPanNo());
		intern.setPanPhotoUrl(s3Service.uploadFiles(panPhoto,
				"Intern/" + intern.getInternUniqueId() + "/PanPhoto/" + "panPhoto.png"));
		
		intern.setRecentMarksheetOrDegreePhotoUrl(s3Service.uploadFiles(recentMarksheetOrDegreePhoto, "Intern/"
				+ intern.getInternUniqueId() + "/RecentMarksheetOrDegreePhoto/" + "recentMarksheetOrDegreePhoto.png"));
		intern.setBankAccountNo(internRequestDTO.getBankAccountNo());
		intern.setIfscCode(internRequestDTO.getIfscCode());
		intern.setBankName(internRequestDTO.getBankName());
		intern.setPassbookPhotoUrl(s3Service.uploadFiles(passbookPhoto,
				"Intern/" + intern.getInternUniqueId() + "/PassBookPhoto/" + "passbookPhoto.png"));
		intern.setStipend(internRequestDTO.getStipend());
		intern.setBasic(internRequestDTO.getBasic());
		intern.setPf(internRequestDTO.getPf());
		intern.setBonusAmount(internRequestDTO.getBonusAmount());
		intern.setProjectId(null);

		AdminRegisterRequestDTO adminRegisterRequestDTO = new AdminRegisterRequestDTO();
		adminRegisterRequestDTO.setRole("user");
		adminRegisterRequestDTO.setEmail(internRequestDTO.getPersonalEmail());
		adminRegisterRequestDTO.setFirstName(internRequestDTO.getInternName());
		adminRegisterRequestDTO.setLastName(internRequestDTO.getLastName());
		adminRegisterRequestDTO.setUsername(username);
		adminRegisterRequestDTO.setPassword(internRequestDTO.getPassword());
		intern.setKeycloackId(auth.registerUser(adminRegisterRequestDTO));

		// Save intern in the repository
		Intern intern1 = internRepository.save(intern);
		return convertToResponseDTO(intern1);

	}

	@Override
	public List<InternResponseDTO> getAllInterns() {
		List<Intern> interns = internRepository.findAll();
		return interns.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
	}

	public List<String> getProjectIdsByInternId(String internId) {
		Intern intern = internRepository.findById(internId).orElse(null);
		if (intern != null) {
			return List.copyOf(intern.getProjectId());
		}
		return List.of(); // Return an empty list if intern not found
	}

	@Override
	public boolean updateIntern(String id, InternRequestDTO internRequestDTO, MultipartFile photo,
			MultipartFile recentMarksheetOrDegreePhoto, MultipartFile panPhoto, MultipartFile aadhaarPhoto,
			MultipartFile passbookPhoto) throws IOException {
		Intern intern = internRepository.findById(id).orElseThrow(() -> new InternNotFoundException(id));
		intern.setInternName(internRequestDTO.getInternName());
		intern.setLastName(internRequestDTO.getLastName());
		intern.setPersonalEmail(internRequestDTO.getPersonalEmail());
		intern.setContactNo(internRequestDTO.getContactNo());
		intern.setAddress(internRequestDTO.getAddress());
		intern.setInternType(internRequestDTO.getInternType());
		intern.setDateOfJoining(internRequestDTO.getDateOfJoining());
		intern.setIsActive(internRequestDTO.getIsActive());
		if (photo != null) {
			intern.setPhotoUrl(
					s3Service.uploadFiles(photo, "Intern/" + intern.getInternUniqueId() + "/Photo/" + "photo.png"));
		}
		intern.setAadhaarNo(internRequestDTO.getAadhaarNo());
		if (aadhaarPhoto != null) {
			intern.setAadhaarPhotoUrl(s3Service.uploadFiles(aadhaarPhoto,
					"Intern/" + intern.getInternUniqueId() + "/AadhaarPhoto/" + "aadhaarPhoto.png"));
		}
		intern.setPanNo(internRequestDTO.getPanNo());
		if (panPhoto != null) {
			intern.setPanPhotoUrl(s3Service.uploadFiles(panPhoto,
					"Intern/" + intern.getInternUniqueId() + "/PanPhoto/" + "panPhoto.png"));
		}
		
		if (recentMarksheetOrDegreePhoto != null) {
			intern.setRecentMarksheetOrDegreePhotoUrl(
					s3Service.uploadFiles(recentMarksheetOrDegreePhoto, "Intern/" + intern.getInternUniqueId()
							+ "/RecentMarksheetOrDegreePhoto/" + "recentMarksheetOrDegreePhoto.png"));
		}
		intern.setBankAccountNo(internRequestDTO.getBankAccountNo());
		intern.setIfscCode(internRequestDTO.getIfscCode());
		intern.setBankName(internRequestDTO.getBankName());
		if (passbookPhoto != null) {
			intern.setPassbookPhotoUrl(s3Service.uploadFiles(passbookPhoto,
					"Intern/" + intern.getInternUniqueId() + "/PassBookPhoto/" + "passbookPhoto.png"));
		}
		intern.setStipend(internRequestDTO.getStipend());
		intern.setBasic(internRequestDTO.getBasic());
		intern.setPf(internRequestDTO.getPf());
		intern.setBonusAmount(internRequestDTO.getBonusAmount());
		intern.setProjectId(null);
		if (internRequestDTO.getPassword() != null && !internRequestDTO.getPassword().isEmpty()) {
			intern.setPassword(internRequestDTO.getPassword());
		}
		AdminRegisterRequestDTO adminRegisterRequestDTO = new AdminRegisterRequestDTO();

		adminRegisterRequestDTO.setEmail(internRequestDTO.getPersonalEmail());
		adminRegisterRequestDTO.setFirstName(internRequestDTO.getInternName());
		adminRegisterRequestDTO.setLastName(internRequestDTO.getLastName());

		adminRegisterRequestDTO.setPassword(internRequestDTO.getPassword());

		adminRegisterRequestDTO.setPassword(internRequestDTO.getPassword());

		auth.updateUser(intern.getKeycloackId(), adminRegisterRequestDTO);

		internRepository.save(intern);
		return true;
	}

	// while deleting Intern its Association with project get un-assign then delete
	// it from DB
	@Override
	public boolean deleteIntern(String id) {
		// Check if the intern exists
		if (!internRepository.existsById(id)) {
			return false; // Intern doesn't exist
		}

		// Retrieve the intern by ID
		Intern intern = internRepository.findById(id).orElseThrow(() -> new RuntimeException("Intern not found"));

		// Remove intern ID from associated projects
		Set<String> projectIds = intern.getProjectId(); // Assuming intern has a set of associated project IDs
		for (String projectId : projectIds) {
			ProjectMaster project = projectRepository.findById(projectId)
					.orElseThrow(() -> new RuntimeException("Project with ID " + projectId + " not found"));

			// Remove intern ID from the project's intern list
			Set<String> internIdsOnProject = project.getInternId(); // Assuming getInternIds() exists
			internIdsOnProject.remove(id);
			project.setInternId(internIdsOnProject); // Update the project
			projectRepository.save(project); // Save the updated project
		}

		auth.deleteUser(intern.getKeycloackId());
		internRepository.deleteById(id);
		return true;
	}

	@Override
	public List<ProjectMaster> getProjectsOfInternById(String internId) {
		// Fetch intern data
		Intern intern = internRepository.findById(internId)
				.orElseThrow(() -> new ResourceNotFoundException("Intern not found with ID: " + internId));

		// Get project IDs associated with the intern
		Set<String> listOfProjectIds = intern.getProjectId();
		logger.info("listOfProjectIds" + listOfProjectIds.toString());
		if (listOfProjectIds == null || listOfProjectIds.isEmpty()) {
			return Collections.emptyList(); // No projects associated with this intern
		}

		// Fetch projects using the project repository
		List<ProjectMaster> projects = projectRepository.findAllByIdIn(listOfProjectIds);
		logger.info(
				"getProjectsOfInternById  --/ Fetch projects using the project repository" + projects.toString());
		// Log any missing projects
		if (projects.size() < listOfProjectIds.size()) {
			Set<String> foundProjectIds = projects.stream().map(ProjectMaster::getId).collect(Collectors.toSet());
			Set<String> missingProjectIds = listOfProjectIds.stream()
					.filter(projectId -> !foundProjectIds.contains(projectId)).collect(Collectors.toSet());
			log.info("Missing projects: {}", missingProjectIds);
		}

		return projects;
	}

	// Utility method to convert Intern entity to DTO
	InternResponseDTO convertToResponseDTO(Intern intern) {
		InternResponseDTO dto = new InternResponseDTO();
		dto.setId(intern.getId());
		dto.setInternUniqueId(intern.getInternUniqueId());
		dto.setInternName(intern.getInternName());
		dto.setLastName(intern.getLastName());
		dto.setPersonalEmail(intern.getPersonalEmail());
		dto.setContactNo(intern.getContactNo());
		dto.setAddress(intern.getAddress());
		dto.setInternType(intern.getInternType());
		dto.setDateOfJoining(intern.getDateOfJoining());
		dto.setIsActive(intern.getIsActive());
		dto.setPhotoUrl(intern.getPhotoUrl());
		dto.setAadhaarNo(intern.getAadhaarNo());
		dto.setAadhaarPhotoUrl(intern.getAadhaarPhotoUrl());
		dto.setPanNo(intern.getPanNo());
		dto.setPanPhotoUrl(intern.getPanPhotoUrl());
		
		dto.setRecentMarksheetOrDegreePhotoUrl(intern.getRecentMarksheetOrDegreePhotoUrl());
		dto.setBankAccountNo(intern.getBankAccountNo());
		dto.setIfscCode(intern.getIfscCode());
		dto.setBankName(intern.getBankName());
		dto.setPassbookPhotoUrl(intern.getPassbookPhotoUrl());
		dto.setStipend(intern.getStipend());
		dto.setBasic(intern.getBasic());
		dto.setPf(intern.getPf());
		dto.setBonusAmount(intern.getBonusAmount());
		dto.setProjectId(intern.getProjectId());
		return dto;
	}

	@Override
	public InternResponseDTO getInternById(String id) {
		Optional<Intern> optionalIntern = internRepository.findById(id);
		if (optionalIntern.isEmpty()) {
			log.info("Intern not found by id in Optional");
			return null;
		}

		Intern intern = optionalIntern.get();
		log.info("Intern not found by id in Optional" + intern.toString());
		return this.convertToResponseDTO(intern);
	}

//------------------------------------------------------------------------------------------------
	public List<InternResponseDTO> getInternWithoutProjects() {

		List<InternResponseDTO> allInterns = getAllInterns();
		if (allInterns == null) {
			return null;
		}

		List<ProjectMasterResponseDTO> allProjectDTOs = projectMasterServiceImpl.getAllProjects();

		// Flatten the list of intern IDs from projects into a single set, ignoring null
		// values
		Set<String> internsWithProjects = allProjectDTOs.stream().map(ProjectMasterResponseDTO::getInternId)
				.filter(Objects::nonNull) // Filter out null sets
				.flatMap(Set::stream) // Flatten each set of intern IDs into a single stream
				.collect(Collectors.toSet());

		// Return only interns who are not in internsWithProjects set
		return allInterns.stream().filter(intern -> !internsWithProjects.contains(intern.getId())).toList();
	}

	// -----------------------------------------310ct-----------------------------------
	@Override
	public List<byte[]> getInternImageData(String id) throws IOException {
		List<byte[]> internFiles = new ArrayList<>();
		Optional<Intern> optionalIntern = internRepository.findById(id);

		if (optionalIntern.isPresent()) {
			Intern intern = optionalIntern.get();

			if (intern.getPhotoUrl() != null) {
				internFiles.add(getFileBytes(intern.getPhotoUrl()));
			}
			if (intern.getPanPhotoUrl() != null) {
				internFiles.add(getFileBytes(intern.getPanPhotoUrl()));
			}
			if (intern.getAadhaarPhotoUrl() != null) {
				internFiles.add(getFileBytes(intern.getAadhaarPhotoUrl()));
			}
			if (intern.getRecentMarksheetOrDegreePhotoUrl() != null) {
				internFiles.add(getFileBytes(intern.getRecentMarksheetOrDegreePhotoUrl()));
			}
			if (intern.getPassbookPhotoUrl() != null) {
				internFiles.add(getFileBytes(intern.getPassbookPhotoUrl()));
			}

		}

		return internFiles;
	}

	// ------------------------------31oct--------------------------
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

	// -----------------------------31oct---------------------------------------
	public List<String> convertToBase64(List<byte[]> imageFiles) {
		return imageFiles.stream().map(Base64.getEncoder()::encodeToString).toList();
	}

}
