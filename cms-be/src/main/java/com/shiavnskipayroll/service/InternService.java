package com.shiavnskipayroll.service;

import com.shiavnskipayroll.dto.request.InternRequestDTO;
import com.shiavnskipayroll.dto.response.InternResponseDTO;
import com.shiavnskipayroll.entity.ProjectMaster;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface InternService {
	InternResponseDTO createIntern(InternRequestDTO internRequestDTO, MultipartFile photo,
			MultipartFile recentMarksheetOrDegreePhoto, MultipartFile panPhoto, MultipartFile aadhaarPhoto,
			MultipartFile passbookPhoto) throws IOException;
	List<InternResponseDTO> getAllInterns();
	InternResponseDTO getInternById(String id);
	boolean updateIntern(String id, InternRequestDTO internRequestDTO, MultipartFile photo,
			MultipartFile recentMarksheetOrDegreePhoto, MultipartFile panPhoto, MultipartFile aadhaarPhoto,
			MultipartFile passbookPhoto) throws IOException;
	boolean deleteIntern(String id);
	List<ProjectMaster> getProjectsOfInternById(String internId);
	List<InternResponseDTO> getInternWithoutProjects();
	List<byte[]> getInternImageData(String id) throws IOException;
}