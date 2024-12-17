package com.shiavnskipayroll.service;

import com.shiavnskipayroll.dto.request.ConsultantMasterRequestDTO;
import com.shiavnskipayroll.dto.response.ConsultantMasterResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ConsultantMasterService {
	ConsultantMasterResponseDTO createConsultant(ConsultantMasterRequestDTO consultantMasterRequestDTO,
			MultipartFile photo, MultipartFile adharPhoto, MultipartFile panPhoto, MultipartFile passbookPhoto)
			throws IOException;

	ConsultantMasterResponseDTO updateConsultant(String id, ConsultantMasterRequestDTO consultantMasterRequestDTO,
			MultipartFile photo, MultipartFile adharPhoto, MultipartFile panPhoto, MultipartFile passbookPhoto)
			throws IOException;

	ConsultantMasterResponseDTO getConsultantById(String id);

	List<ConsultantMasterResponseDTO> getAllConsultants();

	void deleteConsultant(String id);
	List<ConsultantMasterResponseDTO> getConsultantsWithoutProjects() ;
	List<byte[]> getConsultantImageData(String id) throws IOException;
}