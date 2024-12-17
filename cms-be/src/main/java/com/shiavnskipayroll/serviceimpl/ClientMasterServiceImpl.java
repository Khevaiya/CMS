package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.dto.request.ClientMasterRequestDTO;
import com.shiavnskipayroll.dto.response.ClientMasterResponseDTO;
import com.shiavnskipayroll.entity.ClientMaster;
import com.shiavnskipayroll.entity.ConsultantMaster;
import com.shiavnskipayroll.entity.EmployeeMaster;
import com.shiavnskipayroll.entity.Intern;
import com.shiavnskipayroll.exceptions.ClientNotFoundException;
import com.shiavnskipayroll.repository.*;
import com.shiavnskipayroll.service.ClientService;
import com.shiavnskipayroll.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientMasterServiceImpl implements ClientService {
	
	private final ProjectRepository projectMasterRepository;
	private final ClientRepository clientRepository;
	private final EmployeeRepository employeeRepository;
	private final InternRepository internRepository;
	private final ConsultantMasterRepository consultantMasterRepository;
	private final S3Service s3Service;
	private final AssignProjectToEmployeeServiceImpl assignProjectToEmployeeServiceImpl;
	private final AssignProjectToConsultantServiceImpl assignProjectToConsultantServiceImpl;
	private final AssignProjectToInternServiceImpl assignProjectToInternServiceImpl;
	private final ConsultantProjectDetailsRepository consultantProjectDetailsRepository;
	

	@Override
	public void createClient(ClientMasterRequestDTO clientMasterRequestDTO, MultipartFile clientSowSigned)
			throws Exception {
		// Optional fields are null BY-DEFAULT ,Required fields are checked if null then
		// throw IllegalArgumentException
		//validateClient(clientMasterRequestDTO, clientSowSigned);

		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientName(clientMasterRequestDTO.getClientName());
		clientMaster.setClientContactEmailId1(clientMasterRequestDTO.getClientContactEmailId1());
		clientMaster.setClientContactEmailId2(clientMasterRequestDTO.getClientContactEmailId2());
		clientMaster.setClientContactPhone1(clientMasterRequestDTO.getClientContactPhone1());
		clientMaster.setClientContactPhone2(clientMasterRequestDTO.getClientContactPhone2());
		clientMaster.setClientAddress(clientMasterRequestDTO.getClientAddress());
		clientMaster.setPoc1ContactNo(clientMasterRequestDTO.getPoc1ContactNo());
		clientMaster.setPoc1Email(clientMasterRequestDTO.getPoc1Email());
		clientMaster.setState(clientMasterRequestDTO.getState());
		clientMaster.setCity(clientMasterRequestDTO.getCity());
		clientMaster.setPoc2ContactNo(clientMasterRequestDTO.getPoc2ContactNo());
		clientMaster.setPoc2Email(clientMasterRequestDTO.getPoc2Email());
		clientMaster.setGstNumber(clientMasterRequestDTO.getGstNumber());
		clientMaster.setPanNumber(clientMasterRequestDTO.getPanNumber());
		clientMaster.setCompanyWebsite(clientMasterRequestDTO.getCompanyWebsite());
		clientMaster.setIsActive(clientMasterRequestDTO.getIsActive());
		clientMaster.setTdsPercentage(clientMasterRequestDTO.getTdsPercentage());
		clientMaster.setGstPercentage(clientMasterRequestDTO.getGstPercentage());
		clientMaster.setCreatedBy(clientMasterRequestDTO.getCreatedBy());
		clientMaster.setCreatedDate(clientMasterRequestDTO.getCreatedDate());
		clientMaster.setLastUpdatedBy(clientMasterRequestDTO.getLastUpdatedBy());
		clientMaster.setLastUpdatedDate(clientMasterRequestDTO.getLastUpdatedDate());
		clientMaster.setListOfProjectIds(null);// Set null when client created its projectList is null ,we set its
												// projectList when its project is created
												// "Client/clientSowSigned/"+clientMasterRequestDTO.getClientName()+"/"+"clientSowSigned.pdf"
		if (clientSowSigned != null && !clientSowSigned.isEmpty())
			clientMaster.setClientSowSignedUrl(s3Service.uploadFiles(clientSowSigned,
					"Client/" + clientMasterRequestDTO.getClientName() + "/" + "clientSowSigned.png"));
		clientRepository.save(clientMaster);
	}

	@Override
	public List<ClientMasterResponseDTO> getAllClient() {
		List<ClientMaster> clientMasters = clientRepository.findAll();
		return clientMasters.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
	}

	@Override
	public ClientMasterResponseDTO getClientById(String id) {
		Optional<ClientMaster> clientMasterOpt = clientRepository.findById(id);
		ClientMaster clientMaster = clientMasterOpt.get();
		return this.convertToResponseDTO(clientMaster);
	}

	// ------------------------------------------------------------------------
	public byte[] getAllFileDataOfClient(String id) {
		byte[] fileBytes = null;
		Optional<ClientMaster> clientMasterOpt = clientRepository.findById(id);
		if (clientMasterOpt.isPresent()) {
			ClientMaster clientMaster = clientMasterOpt.get();
			try (InputStream inputStream = s3Service.displayS3FileContent(clientMaster.getClientSowSignedUrl());
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				fileBytes = outputStream.toByteArray();
				return fileBytes;

			} catch (IOException e) {
				throw new RuntimeException("Failed to get file", e);
			}
		}
		return fileBytes;


	}
	// ---------------------------------------------------------------------------------------

	@Override
	public void updateClient(String id, ClientMasterRequestDTO clientMasterRequestDTO, MultipartFile clientSowSigned)
			throws IOException {

		
		//validateClientForUpdate(clientMasterRequestDTO, clientSowSigned);
		System.out.println("below validateupdate");
		Optional<ClientMaster> clientMasterOpt = clientRepository.findById(id);
		if (clientMasterOpt.isPresent()) {
			ClientMaster clientMaster = clientMasterOpt.get();
//			if (clientMasterRequestDTO.getClientName() != null) {
//				MultipartFile existingFileFromS3 = s3Service.getFileFromS3(clientMaster.getClientSowSignedUrl());
//				if(existingFileFromS3!=null)
//				{
//				String newPath = "Client/" + clientMasterRequestDTO.getClientName() + "/clientSowSigned.pdf";
//				s3Service.deleteOldFileFromS3(clientMaster.getClientSowSignedUrl());
//				clientMaster.setClientSowSignedUrl(s3Service.uploadFiles(existingFileFromS3, newPath));
//				}
//				}
			clientMaster.setClientName(clientMasterRequestDTO.getClientName());
			clientMaster.setClientContactEmailId1(clientMasterRequestDTO.getClientContactEmailId1());
			clientMaster.setClientContactEmailId2(clientMasterRequestDTO.getClientContactEmailId2());
			clientMaster.setClientContactPhone1(clientMasterRequestDTO.getClientContactPhone1());
			clientMaster.setClientContactPhone2(clientMasterRequestDTO.getClientContactPhone2());
			clientMaster.setClientAddress(clientMasterRequestDTO.getClientAddress());
			clientMaster.setState(clientMasterRequestDTO.getState());
			clientMaster.setCity(clientMasterRequestDTO.getCity());
			clientMaster.setPoc1ContactNo(clientMasterRequestDTO.getPoc1ContactNo());
			clientMaster.setPoc1Email(clientMasterRequestDTO.getPoc1Email());
			clientMaster.setPoc2ContactNo(clientMasterRequestDTO.getPoc2ContactNo());
			clientMaster.setPoc2Email(clientMasterRequestDTO.getPoc2Email());
			clientMaster.setGstNumber(clientMasterRequestDTO.getGstNumber());
			clientMaster.setPanNumber(clientMasterRequestDTO.getPanNumber());
			clientMaster.setCompanyWebsite(clientMasterRequestDTO.getCompanyWebsite());
			clientMaster.setIsActive(clientMasterRequestDTO.getIsActive());
			clientMaster.setTdsPercentage(clientMasterRequestDTO.getTdsPercentage());
			clientMaster.setGstPercentage(clientMasterRequestDTO.getGstPercentage());
			clientMaster.setCreatedBy(clientMasterRequestDTO.getCreatedBy());
			clientMaster.setCreatedDate(clientMasterRequestDTO.getCreatedDate());
			clientMaster.setLastUpdatedBy(clientMasterRequestDTO.getLastUpdatedBy());
			clientMaster.setLastUpdatedDate(clientMasterRequestDTO.getLastUpdatedDate());
			clientMaster.setListOfProjectIds(clientMasterRequestDTO.getListOfProjectMasterRequestDTOIds());// set
																											// project
																											// ids if
																											// its null
																											// set null
			if (clientSowSigned != null && !clientSowSigned.isEmpty())
				clientMaster.setClientSowSignedUrl(s3Service.uploadFiles(clientSowSigned,
						"Client/" + clientMasterRequestDTO.getClientName() + "/" + "clientSowSigned.png"));
			clientRepository.save(clientMaster);
			System.out.println("updateClient");
		}
	}

	@Override
	public void deleteClient(String id) {
		Optional<ClientMaster>optionalClient=clientRepository.findById(id);

		if(optionalClient.isEmpty())throw new ClientNotFoundException("Clinet Not found with id :"+id+" in class ClientMasterServiceImpl ,method deleteClient");
		ClientMaster clientMaster=optionalClient.get();
		List<String > listOfProjectIds=clientMaster.getListOfProjectIds();
		if (listOfProjectIds != null && !listOfProjectIds.isEmpty()) {
			// Remove project ID from associated employees, consultants, and interns
			for (String projectId : listOfProjectIds) {
				Set<EmployeeMaster> setOfEmployeesOnProject = assignProjectToEmployeeServiceImpl.getEmployeesByProjectId(projectId);
				if(!setOfEmployeesOnProject.isEmpty()) {
					for (EmployeeMaster employee : setOfEmployeesOnProject) {
						Set<String> projectIds = employee.getProjectId();
						if (projectIds != null) {
							projectIds.remove(projectId); // Remove the project ID
							employee.setProjectId(projectIds); // Update the employee's project IDs
							employeeRepository.save(employee); // Save changes to the employee
						}
					}
				}

				// Remove project ID from consultants
				Set<ConsultantMaster> setOfConsultantsOnProject = assignProjectToConsultantServiceImpl.getConsultantsByProjectId(projectId);
				if(!setOfConsultantsOnProject.isEmpty()) {
					for (ConsultantMaster consultant : setOfConsultantsOnProject) {
						Set<String> projectIds = consultant.getProjectId();
						if (projectIds != null) {
							projectIds.remove(projectId); // Remove the project ID
							consultant.setProjectId(projectIds); // Update the consultant's project IDs
							consultantMasterRepository.save(consultant); // Save changes to the consultant
						}
						// Remove any associated ConsultantProjectDetails for each consultant
						consultant.getConsultantProjectDetailsId().forEach(consultantProjectDetailsRepository::deleteById
						);
					}
				}

				// Remove project ID from interns
				Set<Intern> setOfInternsOnProject = assignProjectToInternServiceImpl.getInternsByProjectId(projectId);
				if(!setOfInternsOnProject.isEmpty()) {
					for (Intern intern : setOfInternsOnProject) {
						Set<String> projectIds = intern.getProjectId();
						if (projectIds != null) {
							projectIds.remove(projectId); // Remove the project ID
							intern.setProjectId(projectIds); // Update the intern's project IDs
							internRepository.save(intern); // Save changes to the intern
						}
					}
				}
				projectMasterRepository.deleteById(projectId); // Delete the project associated with the client
			}
		}
		clientRepository.deleteById(id);// if client deleted its projects and association of projects must be removed
										// must be deleted
	}

	private ClientMasterResponseDTO convertToResponseDTO(ClientMaster clientMaster) {
		if (clientMaster == null) {
			return null; // Return null or throw an exception based on your application's design.
		}
		ClientMasterResponseDTO dto = new ClientMasterResponseDTO();
		dto.setId(clientMaster.getId());
		dto.setClientName(clientMaster.getClientName());
		dto.setClientContactEmailId1(clientMaster.getClientContactEmailId1());
		dto.setClientContactEmailId2(clientMaster.getClientContactEmailId2());
		dto.setClientContactPhone1(clientMaster.getClientContactPhone1());
		dto.setClientContactPhone2(clientMaster.getClientContactPhone2());
		dto.setClientAddress(clientMaster.getClientAddress());
		dto.setPoc1ContactNo(clientMaster.getPoc1ContactNo());
		dto.setPoc1Email(clientMaster.getPoc1Email());
		dto.setPoc2ContactNo(clientMaster.getPoc2ContactNo());
		dto.setPoc2Email(clientMaster.getPoc2Email());
		dto.setGstNumber(clientMaster.getGstNumber());
		dto.setPanNumber(clientMaster.getPanNumber());
		dto.setCompanyWebsite(clientMaster.getCompanyWebsite());
		dto.setIsActive(clientMaster.getIsActive());
		dto.setState(clientMaster.getState());
		dto.setCity(clientMaster.getCity());
		dto.setClientSowSignedUrl(clientMaster.getClientSowSignedUrl());
		dto.setTdsPercentage(clientMaster.getTdsPercentage());
		dto.setGstPercentage(clientMaster.getGstPercentage());
		dto.setCreatedBy(clientMaster.getCreatedBy());
		dto.setCreatedDate(clientMaster.getCreatedDate());
		dto.setLastUpdatedDate(clientMaster.getLastUpdatedDate());
		dto.setLastUpdatedBy(clientMaster.getLastUpdatedBy());
		dto.setListOfProjectIds(clientMaster.getListOfProjectIds());
		if (clientMaster.getListOfProjectIds() != null) {
			dto.setListOfProjectIds(clientMaster.getListOfProjectIds());
		} else {
			dto.setListOfProjectIds(null); // Set null
		}
		return dto;
	}

	private ClientMaster convertToEntity(ClientMasterRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null; // Return null or throw an exception based on your application's design.
		}

		ClientMaster clientMaster = new ClientMaster();

		clientMaster.setClientName(requestDTO.getClientName());
		clientMaster.setClientContactEmailId1(requestDTO.getClientContactEmailId1());
		clientMaster.setClientContactEmailId2(requestDTO.getClientContactEmailId2());
		clientMaster.setClientContactPhone1(requestDTO.getClientContactPhone1());
		clientMaster.setClientContactPhone2(requestDTO.getClientContactPhone2());
		clientMaster.setClientAddress(requestDTO.getClientAddress());
		clientMaster.setState(requestDTO.getState());
		clientMaster.setCity(requestDTO.getCity());
		clientMaster.setPoc1Email(requestDTO.getPoc1Email());
		clientMaster.setPoc1ContactNo(requestDTO.getPoc1ContactNo());
		clientMaster.setPoc2Email(requestDTO.getPoc2Email());
		clientMaster.setPoc2ContactNo(requestDTO.getPoc2ContactNo());
		clientMaster.setGstNumber(requestDTO.getGstNumber());
		clientMaster.setPanNumber(requestDTO.getPanNumber());
		clientMaster.setCompanyWebsite(requestDTO.getCompanyWebsite());
		clientMaster.setIsActive(requestDTO.getIsActive());
		clientMaster.setTdsPercentage(requestDTO.getTdsPercentage());
		clientMaster.setGstPercentage(requestDTO.getGstPercentage());
		clientMaster.setCreatedBy(requestDTO.getCreatedBy());
		clientMaster.setLastUpdatedBy(requestDTO.getLastUpdatedBy());
		clientMaster.setLastUpdatedBy(requestDTO.getLastUpdatedBy());
		clientMaster.setLastUpdatedDate(requestDTO.getLastUpdatedDate());
		clientMaster.setListOfProjectIds(null); // Set null when client created its projectList is null ,we set its
												// projectList when its project is created
		return clientMaster;
	}

	private void validateClient(ClientMasterRequestDTO clientMasterRequestDTO, MultipartFile clientSowSigned) {
		if (clientMasterRequestDTO == null) {
			throw new IllegalArgumentException("ClientMasterRequestDTO cannot be null");
		}

		// Check if required string variables are not null or empty
		if (isStringNullOrEmpty(clientMasterRequestDTO.getClientName())) {
			throw new IllegalArgumentException("Client Name cannot be null or empty");
		}

		if (isStringNullOrEmpty(clientMasterRequestDTO.getClientContactEmailId1())) {
			throw new IllegalArgumentException("Client Contact Email ID 1 cannot be null or empty");
		}

		if (isStringNullOrEmpty(clientMasterRequestDTO.getClientContactPhone1())) {
			throw new IllegalArgumentException("Client Contact Phone 1 cannot be null or empty");
		}

		if (isStringNullOrEmpty(clientMasterRequestDTO.getClientAddress())) {
			throw new IllegalArgumentException("Client Address cannot be null or empty");
		}

		if (isStringNullOrEmpty(clientMasterRequestDTO.getPoc1Email())) {
			throw new IllegalArgumentException("POC 1 Email cannot be null or empty");
		}

		if (isStringNullOrEmpty(clientMasterRequestDTO.getPoc1ContactNo())) {
			throw new IllegalArgumentException("POC 1 Contact No cannot be null or empty");
		}

		// Optional fields - no error if they are null or empty
		if (clientMasterRequestDTO.getClientContactEmailId2() != null
				&& !isValidEmail(clientMasterRequestDTO.getClientContactEmailId2())) {
			throw new IllegalArgumentException("Client Contact Email ID 2 is invalid");
		}

		if (clientMasterRequestDTO.getClientContactPhone2() != null
				&& !isValidPhone(clientMasterRequestDTO.getClientContactPhone2())) {
			throw new IllegalArgumentException("Client Contact Phone 2 is invalid");
		}
		// Validate MultipartFile clientSowSigned (Required Field)
		if (clientSowSigned == null || clientSowSigned.isEmpty()) {
			throw new IllegalArgumentException("Client SOW Signed document is required and cannot be null or empty");
		}

		// Example: Check file size (you can adjust the size limit based on your needs)
		long maxFileSize = 5 * 1024 * 1024; // 5 MB
		if (clientSowSigned.getSize() > maxFileSize) {
			throw new IllegalArgumentException("Client SOW Signed document size exceeds the maximum limit of 5 MB");
		}

		// Example: Check file type (accept only PNG files)
		String fileType = clientSowSigned.getContentType();
		if (!"image/png".equals(fileType)) {
			throw new IllegalArgumentException("Client SOW Signed document must be a PNG file");
		}

	}

	private void validateClientForUpdate(ClientMasterRequestDTO clientMasterRequestDTO, MultipartFile clientSowSigned) {
		if (clientMasterRequestDTO == null) {
			throw new IllegalArgumentException("ClientMasterRequestDTO cannot be null");
		}

		// Check if required string variables are not null or empty
		if (isStringNullOrEmpty(clientMasterRequestDTO.getClientName())) {
			throw new IllegalArgumentException("Client Name cannot be null or empty");
		}

		if (isStringNullOrEmpty(clientMasterRequestDTO.getClientContactEmailId1())) {
			throw new IllegalArgumentException("Client Contact Email ID 1 cannot be null or empty");
		}

		if (isStringNullOrEmpty(clientMasterRequestDTO.getClientContactPhone1())) {
			throw new IllegalArgumentException("Client Contact Phone 1 cannot be null or empty");
		}

		if (isStringNullOrEmpty(clientMasterRequestDTO.getClientAddress())) {
			throw new IllegalArgumentException("Client Address cannot be null or empty");
		}

		if (isStringNullOrEmpty(clientMasterRequestDTO.getPoc1Email())) {
			throw new IllegalArgumentException("POC 1 Email cannot be null or empty");
		}

		if (isStringNullOrEmpty(clientMasterRequestDTO.getPoc1ContactNo())) {
			throw new IllegalArgumentException("POC 1 Contact No cannot be null or empty");
		}

		// Optional fields - no error if they are null or empty
		if (clientMasterRequestDTO.getClientContactEmailId2() != null
				&& !isValidEmail(clientMasterRequestDTO.getClientContactEmailId2())) {
			throw new IllegalArgumentException("Client Contact Email ID 2 is invalid");
		}

		if (clientMasterRequestDTO.getClientContactPhone2() != null
				&& !isValidPhone(clientMasterRequestDTO.getClientContactPhone2())) {
			throw new IllegalArgumentException("Client Contact Phone 2 is invalid");
		}

	}

	// Helper method to check if a string is null or empty
	private boolean isStringNullOrEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	// Example email validation method (can be customized as per your requirements)
	private boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		return email.matches(emailRegex);
	}

	// Example phone number validation method (can be customized as per your
	// requirements)
	private boolean isValidPhone(String phone) {
		String phoneRegex = "^\\+?[0-9]{10,15}$"; // Accepts international format as well
		return phone.matches(phoneRegex);
	}

}
