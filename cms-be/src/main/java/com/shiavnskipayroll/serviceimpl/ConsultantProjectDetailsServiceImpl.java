// src/main/java/com/shiavnskiPayroll/service/impl/ConsultantProjectDetailsServiceImpl.java
package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.dto.request.ConsultantProjectDetailsRequestDTO;
import com.shiavnskipayroll.dto.response.ConsultantProjectDetailsResponseDTO;
import com.shiavnskipayroll.entity.ConsultantMaster;
import com.shiavnskipayroll.entity.ConsultantProjectDetails;
import com.shiavnskipayroll.exceptions.ConsultantNotFoundException;
import com.shiavnskipayroll.repository.ConsultantMasterRepository;
import com.shiavnskipayroll.repository.ConsultantProjectDetailsRepository;
import com.shiavnskipayroll.service.ConsultantProjectDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultantProjectDetailsServiceImpl implements ConsultantProjectDetailsService {

	private final ConsultantProjectDetailsRepository repository;
	private final ConsultantMasterRepository consultantMasterRepository;
	private  final AssignProjectToConsultantServiceImpl assignProjectToConsultantServiceImpl;

	@Override
	public ConsultantProjectDetailsResponseDTO createConsultantProject(ConsultantProjectDetailsRequestDTO requestDTO) {
		ConsultantProjectDetails consultantProjectDetails = new ConsultantProjectDetails();
		consultantProjectDetails.setPosition(requestDTO.getPosition());
		consultantProjectDetails.setDuration(requestDTO.getDuration());
		consultantProjectDetails.setCompensationType(requestDTO.getCompensationType());
		consultantProjectDetails.setCommission(requestDTO.getCommission());
		consultantProjectDetails.setTds(requestDTO.getTds());
		consultantProjectDetails.setBonusAmount(requestDTO.getBonusAmount());
		consultantProjectDetails.setRemark(requestDTO.getRemark());
		consultantProjectDetails.setConsultantId(requestDTO.getConsultantId());
		consultantProjectDetails.setProjectId(requestDTO.getProjectId());

		ConsultantProjectDetails savedDetails = repository.save(consultantProjectDetails);

		ConsultantMaster consultantMaster = consultantMasterRepository.findById(requestDTO.getConsultantId())
				.orElseThrow(() -> new ConsultantNotFoundException(requestDTO.getConsultantId()));

		List<String> listOfConsultantIds = consultantMaster.getConsultantProjectDetailsId();

		if (listOfConsultantIds == null) {
			listOfConsultantIds = new ArrayList<>();
		}
		listOfConsultantIds.add(savedDetails.getId());
		consultantMaster.setConsultantProjectDetailsId(listOfConsultantIds);
		consultantMasterRepository.save(consultantMaster);
		assignProjectToConsultantServiceImpl.assignProjectToConsultant(savedDetails.getProjectId(),savedDetails.getConsultantId());
//-----------------------------------------------------------------------------------------

		// if consultant is present so inside it, its List of project ids is also
		// present
//		Set<String> setOfProjectIds = consultantMaster.getProjectId();
//		if (setOfProjectIds == null) {
//			setOfProjectIds = new HashSet<>();
//		}
//		System.out.println("ProjectId in create savedDetails.getProjectId());  cinsultant project details :"+savedDetails.getProjectId());
//		setOfProjectIds.add(savedDetails.getProjectId());
//		consultantMaster.setProjectId(setOfProjectIds);
//		System.out.println("ProjectId in create cinsultant project details new :"+consultantMaster.getProjectId());
//
//		consultantMasterRepository.save(consultantMaster); // Save the updated consultant

		return mapToResponseDTO(savedDetails);
	}

	@Override
	public List<ConsultantProjectDetailsResponseDTO> getAllConsultantProjects() {
		return repository.findAll().stream().map(this::mapToResponseDTO).toList();
	}

	@Override
	public ConsultantProjectDetailsResponseDTO getConsultantProjectById(String id) {
		return repository.findById(id).map(this::mapToResponseDTO).orElse(null);
	}

	@Override
	public ConsultantProjectDetailsResponseDTO updateConsultantProject(String id,
			ConsultantProjectDetailsRequestDTO requestDTO) {
		ConsultantProjectDetails existingDetails = repository.findById(id).orElse(null);
		if (existingDetails != null) {
			existingDetails.setPosition(requestDTO.getPosition());
			existingDetails.setDuration(requestDTO.getDuration());
			existingDetails.setCompensationType(requestDTO.getCompensationType());
			existingDetails.setCommission(requestDTO.getCommission());
			existingDetails.setTds(requestDTO.getTds());
			existingDetails.setBonusAmount(requestDTO.getBonusAmount());
			existingDetails.setRemark(requestDTO.getRemark());
			existingDetails.setConsultantId(requestDTO.getConsultantId());
			ConsultantProjectDetails updatedDetails = repository.save(existingDetails);
			return mapToResponseDTO(updatedDetails);
		}
		return null;
	}

	@Override
	public void deleteConsultantProject(String id) {
		repository.deleteById(id);
	}

	private ConsultantProjectDetailsResponseDTO mapToResponseDTO(ConsultantProjectDetails details) {
		return new ConsultantProjectDetailsResponseDTO(details.getId(), details.getPosition(), details.getDuration(),
				details.getCompensationType(), details.getCommission(), details.getTds(), details.getBonusAmount(),
				details.getRemark(), details.getConsultantId(), details.getProjectId());
	}
	public List<ConsultantProjectDetailsResponseDTO> consultantProjectDetailsByProjectIds(List<String> projectIds )
	{
		return repository.findByProjectIdIn(projectIds);

	}
}
