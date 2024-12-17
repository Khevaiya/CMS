package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.controller.AuthController;
import com.shiavnskipayroll.dto.request.AdminRegisterRequestDTO;
import com.shiavnskipayroll.dto.request.ConsultantMasterRequestDTO;
import com.shiavnskipayroll.dto.response.ConsultantMasterResponseDTO;
import com.shiavnskipayroll.dto.response.ProjectMasterResponseDTO;
import com.shiavnskipayroll.entity.ConsultantMaster;
import com.shiavnskipayroll.entity.ConsultantProjectDetails;
import com.shiavnskipayroll.entity.ProjectMaster;
import com.shiavnskipayroll.exceptions.ConsultantNotFoundException;
import com.shiavnskipayroll.repository.ConsultantMasterRepository;
import com.shiavnskipayroll.repository.ConsultantProjectDetailsRepository;
import com.shiavnskipayroll.repository.ProjectRepository;
import com.shiavnskipayroll.service.ConsultantMasterService;
import com.shiavnskipayroll.service.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ConsultantMasterServiceImpl implements ConsultantMasterService {

	private final  ProjectMasterServiceImpl projectMasterServiceImpl;

	private final  ConsultantMasterRepository consultantRepository;

	private final ProjectRepository projectRepository ;

	private final AssignProjectToConsultantServiceImpl assignProjectToConsultantServiceImpl;

	private final ConsultantProjectDetailsRepository consultantProjectDetailsRepository;

	private final  S3Service s3Service;
	private final AuthController auth;

	// Assuming MongoDB repository for ConsultantMaster entity
	private String generateConsultantUniqueId() {
		long count = consultantRepository.count(); // Count number of employees in the DB
		long nextId = count + 1001; // Start from 1001 and increment with each new employee
		return "CON_ST_" + nextId;
	}

	// Create a new consultant
	@Override
	public ConsultantMasterResponseDTO createConsultant(ConsultantMasterRequestDTO consultantMasterRequestDTO, MultipartFile photo, MultipartFile adharPhoto, MultipartFile panPhoto, MultipartFile passbookPhoto) throws IOException {
		ConsultantMaster consultant = new ConsultantMaster();

		String consultantUniqueId = generateConsultantUniqueId();
		consultant.setConsultantUniqueId(consultantUniqueId); 
		consultant.setFirstName(consultantMasterRequestDTO.getFirstName());
		consultant.setLastName(consultantMasterRequestDTO.getLastName());
		consultant.setPassword(consultantMasterRequestDTO.getPassword());
		consultant.setEmail(consultantMasterRequestDTO.getEmail());
		consultant.setContactNo(consultantMasterRequestDTO.getContactNo());
		consultant.setAddress(consultantMasterRequestDTO.getAddress());

		consultant.setEmergencyContactNo(consultantMasterRequestDTO.getEmergencyContactNo());
		consultant.setEmergencyEmail(consultantMasterRequestDTO.getEmergencyEmail());
		consultant.setPersonalEmail(consultantMasterRequestDTO.getPersonalEmail());   

		consultant.setDateOfJoining(consultantMasterRequestDTO.getDateofjoining());
		consultant.setIsActive(consultantMasterRequestDTO.getIsActive());

		consultant.setPhotoUrl(s3Service.uploadFiles(photo, "Consultant/"+consultant.getConsultantUniqueId()+"/Photo/"+"photo.png")); //store in s3 and s3 will return photo url path of s3
		consultant.setAadhaarNo(consultantMasterRequestDTO.getAadhaarNo());
		consultant.setAadhaarPhotoUrl(s3Service.uploadFiles(adharPhoto, "Consultant/"+consultant.getConsultantUniqueId()+"/AdharPhoto/"+"adharPhoto.png"));//store in s3 and s3 will return photo url path of s3
		consultant.setPanNo(consultantMasterRequestDTO.getPanNo());
		consultant.setPanPhotoUrl(s3Service.uploadFiles(panPhoto, "Consultant/"+consultant.getConsultantUniqueId()+"/panPhoto/"+"PanPhoto.png"));//store in s3 and s3 will return photo url path of s3

		consultant.setBankAccountNo(consultantMasterRequestDTO.getBankAccountNo());
		consultant.setIfscCode(consultantMasterRequestDTO.getIfscCode());
		consultant.setBankName(consultantMasterRequestDTO.getBankName());
	    consultant.setPassbookPhotoUrl(s3Service.uploadFiles(passbookPhoto, "Consultant/"+consultant.getConsultantUniqueId()+"/PassBookPhoto/"+"passbookPhoto.png"));//store in s3 and s3 will return photo url path of s3
		consultant.setConsultantProjectDetailsId(consultantMasterRequestDTO.getConsultantProjectDetailsId());
		
		AdminRegisterRequestDTO adminRegisterRequestDTO = new AdminRegisterRequestDTO();
		adminRegisterRequestDTO.setRole("user");
		adminRegisterRequestDTO.setEmail(consultantMasterRequestDTO.getEmail());
		adminRegisterRequestDTO.setFirstName(consultantMasterRequestDTO.getFirstName());
		adminRegisterRequestDTO.setLastName(consultantMasterRequestDTO.getLastName());
		adminRegisterRequestDTO.setUsername(consultantUniqueId);
		adminRegisterRequestDTO.setPassword(consultantMasterRequestDTO.getPassword());
		consultant.setKeycloackId(auth.registerUser(adminRegisterRequestDTO));
		ConsultantMaster savedConsultant = consultantRepository.save(consultant);
		
		return mapToConsultantResponseDTO(savedConsultant);
		
		
	}

	// Update an existing consultant by ID
	@Override
	public ConsultantMasterResponseDTO updateConsultant(String id,
			ConsultantMasterRequestDTO consultantMasterRequestDTO,MultipartFile photo,MultipartFile adharPhoto,MultipartFile panPhoto,MultipartFile passbookPhoto) throws IOException 
		 {
		ConsultantMaster consultant = consultantRepository.findById(id)
				.orElseThrow(() -> new ConsultantNotFoundException(id));
		
		 
		if (photo != null && !photo.isEmpty()) {
			String photoPathForStorageInS3 ="Consultant/"+consultant.getConsultantUniqueId()+"/Photo/"+"photo.png";
			consultant.setPhotoUrl(s3Service.uploadFiles(photo, photoPathForStorageInS3));
		}
		if (panPhoto != null && !panPhoto.isEmpty()) {
			String panPhotoForStorageInS3 ="Consultant/"+consultant.getConsultantUniqueId()+"/AdharPhoto/"+"adharPhoto.png" ;
			consultant.setPanPhotoUrl(s3Service.uploadFiles(panPhoto, panPhotoForStorageInS3));
		}
		
		if (adharPhoto != null && !adharPhoto.isEmpty()) {
			String aadhaarPhotoForStorageInS3 ="Consultant/"+consultant.getConsultantUniqueId()+"/PassBookPhoto/"+"passbookPhoto.png";
			consultant
					.setAadhaarPhotoUrl(s3Service.uploadFiles(adharPhoto, aadhaarPhotoForStorageInS3));
		}
		if (passbookPhoto != null && !passbookPhoto.isEmpty()) {
			String passbookPhotoForStorageInS3 =  "Consultant/"+consultant.getConsultantUniqueId()+"/panPhoto/"+"PanPhoto.png";
			consultant.setPassbookPhotoUrl(s3Service.uploadFiles(passbookPhoto, passbookPhotoForStorageInS3));
		}
		consultant.setFirstName(consultantMasterRequestDTO.getFirstName());
		consultant.setLastName(consultantMasterRequestDTO.getLastName());
		consultant.setEmail(consultantMasterRequestDTO.getEmail());
		consultant.setContactNo(consultantMasterRequestDTO.getContactNo());
		consultant.setAddress(consultantMasterRequestDTO.getAddress());
		consultant.setEmergencyContactNo(consultantMasterRequestDTO.getEmergencyContactNo());
		consultant.setEmergencyEmail(consultantMasterRequestDTO.getEmergencyEmail());
		consultant.setPersonalEmail(consultantMasterRequestDTO.getPersonalEmail());
		consultant.setDateOfJoining(consultantMasterRequestDTO.getDateofjoining());
		consultant.setIsActive(consultantMasterRequestDTO.getIsActive());
		consultant.setAadhaarNo(consultantMasterRequestDTO.getAadhaarNo());
		consultant.setPanNo(consultantMasterRequestDTO.getPanNo());
		consultant.setBankAccountNo(consultantMasterRequestDTO.getBankAccountNo());
		consultant.setIfscCode(consultantMasterRequestDTO.getIfscCode());
		consultant.setBankName(consultantMasterRequestDTO.getBankName());
		consultant.setConsultantProjectDetailsId(consultantMasterRequestDTO.getConsultantProjectDetailsId());
		
		if (consultantMasterRequestDTO.getPassword() != null && !consultantMasterRequestDTO.getPassword().isEmpty()) {
			consultant.setPassword(consultantMasterRequestDTO.getPassword());
		}
		AdminRegisterRequestDTO adminRegisterRequestDTO = new AdminRegisterRequestDTO();

		adminRegisterRequestDTO.setEmail(consultantMasterRequestDTO.getEmail());
		adminRegisterRequestDTO.setFirstName(consultantMasterRequestDTO.getFirstName());
		adminRegisterRequestDTO.setLastName(consultantMasterRequestDTO.getLastName());
		adminRegisterRequestDTO.setPassword(consultantMasterRequestDTO.getPassword());

		auth.updateUser(consultant.getKeycloackId(), adminRegisterRequestDTO);

		ConsultantMaster updatedConsultant = consultantRepository.save(consultant);
		return mapToConsultantResponseDTO(updatedConsultant);
	}

	// Get consultant by ID
	@Override
	public ConsultantMasterResponseDTO getConsultantById(String id) {
		ConsultantMaster consultant = consultantRepository.findById(id)
				.orElseThrow(() -> new ConsultantNotFoundException(id));
		return mapToConsultantResponseDTO(consultant);
	}

	// Get all consultants
	@Override
	public List<ConsultantMasterResponseDTO> getAllConsultants() {
		List<ConsultantMaster> consultants = consultantRepository.findAll();
		return consultants.stream().map(this::mapToConsultantResponseDTO).toList();
	}

	// Delete a consultant by ID
	@Override
	public void deleteConsultant(String id) {
		 // Find the consultant by ID
	    Optional<ConsultantMaster> optionalConsultant = consultantRepository.findById(id);
	    if (!optionalConsultant.isPresent()) {
	        throw new ConsultantNotFoundException("Consultant with ID " + id + " not found");
	    }

	    // Get the consultant master object
	    ConsultantMaster consultantMaster = optionalConsultant.get();
	    Set<String> projectIds = consultantMaster.getProjectId(); // Assuming this returns a Set of project IDs

	    // Remove consultant from associated projects
	    for (String projectId : projectIds) {
	        // Find the project by ID
	        ProjectMaster project = projectRepository.findById(projectId)
	                .orElseThrow(() -> new RuntimeException("Project with ID " + projectId + " not found"));

	        // Remove the consultant from the project's list of consultant IDs
	        Set<String> consultantIdsOnProject = project.getConsultantId(); // Assuming this returns a Set<String>
	        consultantIdsOnProject.remove(id); // Remove the consultant's ID from the project
	        
	        // Update the project with the new list of consultant IDs
	        project.setConsultantId(consultantIdsOnProject); // Set the updated list
	        projectRepository.save(project); // Save the updated project
	    }
	    // Now, retrieve and delete all associated ConsultantProjectDetails by ID
	    List<String> projectDetailsIds = consultantMaster.getConsultantProjectDetailsId(); // Retrieve associated project details IDs
	    for (String detailsId : projectDetailsIds) {
	        // Find the ConsultantProjectDetails by ID
	        ConsultantProjectDetails details = consultantProjectDetailsRepository.findById(detailsId)
	                .orElseThrow(() -> new RuntimeException("ConsultantProjectDetails with ID " + detailsId + " not found"));

	        // Delete each project details object from the repository
	        consultantProjectDetailsRepository.delete(details); // Delete from the repository
	    }
	    // Finally, delete the consultant
	    auth.deleteUser(consultantMaster.getKeycloackId());
	   
	    consultantRepository.deleteById(id);
	}

	// Manual mapping from entity to response DTO
	 ConsultantMasterResponseDTO mapToConsultantResponseDTO(ConsultantMaster consultant) {
		ConsultantMasterResponseDTO dto = new ConsultantMasterResponseDTO();
		dto.setId(consultant.getId());
		dto.setFirstName(consultant.getFirstName());
		dto.setLastName(consultant.getLastName());
		dto.setEmail(consultant.getEmail());
		dto.setContactNo(consultant.getContactNo());
		dto.setAddress(consultant.getAddress());
		dto.setEmergencyContactNo(consultant.getEmergencyContactNo());
		dto.setEmergencyEmail(consultant.getEmergencyEmail());
		dto.setPersonalEmail(consultant.getPersonalEmail());
		dto.setDateofjoining(consultant.getDateOfJoining());
		dto.setIsActive(consultant.getIsActive());
		dto.setPhotoUrl(consultant.getPhotoUrl());
		dto.setAadhaarNo(consultant.getAadhaarNo());
		dto.setAadhaarPhotoUrl(consultant.getAadhaarPhotoUrl());
		dto.setPanNo(consultant.getPanNo());
		dto.setPanPhotoUrl(consultant.getPanPhotoUrl());
		dto.setBankAccountNo(consultant.getBankAccountNo());
		dto.setIfscCode(consultant.getIfscCode());
		dto.setBankName(consultant.getBankName());
		dto.setPassbookPhotoUrl(consultant.getPassbookPhotoUrl());
		dto.setConsultantProjectDetailsId(consultant.getConsultantProjectDetailsId());//set of consultant project details Id
		dto.setProjectId(consultant.getProjectId());//set of consultant project Ids
		dto.setConsultantUniqueId(consultant.getConsultantUniqueId());

		return dto;
	}
	//------------------------------------------------
	@Override
	public List<ConsultantMasterResponseDTO> getConsultantsWithoutProjects() {

		List<ConsultantMasterResponseDTO> allConsultants = this.getAllConsultants();
		if (allConsultants == null) {
			return null;
		}
		List<ProjectMasterResponseDTO> allProjectDTOs = projectMasterServiceImpl.getAllProjects();
		// Flatten the list of consultant IDs involved in projects, ignoring any null values
		Set<String> consultantsWithProjects = allProjectDTOs.stream()
				.map(ProjectMasterResponseDTO::getConsultantId)
				.filter(Objects::nonNull) // Filter out null sets
				.flatMap(Set::stream)
				.collect(Collectors.toSet());
		// Filter consultants who are not in the consultantsWithProjects set
		return allConsultants.stream()
				.filter(consultant -> !consultantsWithProjects.contains(consultant.getId()))
				.toList();
	}

	//----------------------------31------------------------------
	@Override
	public List<byte[]> getConsultantImageData(String id) throws IOException {
		List<byte[]> consultantFiles = new ArrayList<>();
		Optional<ConsultantMaster> consultantMasterOpt = consultantRepository.findById(id);

		if (consultantMasterOpt.isPresent()) {
			ConsultantMaster consultantMaster = consultantMasterOpt.get();

			if (consultantMaster.getPhotoUrl() != null) {
				consultantFiles.add(getFileBytes(consultantMaster.getPhotoUrl()));
			}
			if (consultantMaster.getPanPhotoUrl() != null) {
				consultantFiles.add(getFileBytes(consultantMaster.getPanPhotoUrl()));
			}
			if (consultantMaster.getAadhaarPhotoUrl() != null) {
				consultantFiles.add(getFileBytes(consultantMaster.getAadhaarPhotoUrl()));
			}
			if (consultantMaster.getPassbookPhotoUrl() != null) {
				consultantFiles.add(getFileBytes(consultantMaster.getPassbookPhotoUrl()));
			}

		}

		return consultantFiles;
	}
	//------------------------------31oct--------------------------
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
	//-----------------------------31oct---------------------------------------
	public List<String> convertToBase64(List<byte[]> imageFiles) {
		return imageFiles.stream()
				.map(Base64.getEncoder()::encodeToString)
				.toList();
	}

}
