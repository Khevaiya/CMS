package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.dto.request.ConsultantMasterRequestDTO;
import com.shiavnskipayroll.dto.response.ConsultantMasterResponseDTO;
import com.shiavnskipayroll.serviceimpl.ConsultantMasterServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/consultants")
public class ConsultantMasterController {

	private final ConsultantMasterServiceImpl consultantMasterServiceImpl;

	@PostMapping
	public ResponseEntity<ConsultantMasterResponseDTO> createConsultant(
			@ModelAttribute ConsultantMasterRequestDTO consultantMasterRequestDTO,
			@RequestParam(value = "photo", required = false) MultipartFile photo,
			@RequestParam(value = "aadhaarPhoto", required = false) MultipartFile aadhaarPhoto,
			@RequestParam(value = "panPhoto", required = false) MultipartFile panPhoto,
			@RequestParam(value = "passbookPhoto", required = false) MultipartFile passbookPhoto
			) throws IOException { 
		

		ConsultantMasterResponseDTO createdConsultant = consultantMasterServiceImpl
				.createConsultant(consultantMasterRequestDTO,photo,aadhaarPhoto,panPhoto,passbookPhoto);
		return new ResponseEntity<>(createdConsultant, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ConsultantMasterResponseDTO> updateConsultant(
			@PathVariable String id,
			@ModelAttribute ConsultantMasterRequestDTO consultantMasterRequestDTO,
			@RequestParam(value = "photo", required = false) MultipartFile photo,
			@RequestParam(value = "aadhaarPhoto", required = false) MultipartFile aadhaarPhoto,
			@RequestParam(value = "panPhoto", required = false) MultipartFile panPhoto,
			@RequestParam(value = "passbookPhoto", required = false) MultipartFile passbookPhoto
			) throws IOException {

		ConsultantMasterResponseDTO updatedConsultant = consultantMasterServiceImpl.updateConsultant(id,
				consultantMasterRequestDTO,photo,aadhaarPhoto,panPhoto,passbookPhoto);
		return new ResponseEntity<>(updatedConsultant, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ConsultantMasterResponseDTO> getConsultantById(@PathVariable String id) {
		ConsultantMasterResponseDTO consultant = consultantMasterServiceImpl.getConsultantById(id);
		return new ResponseEntity<>(consultant, HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<ConsultantMasterResponseDTO>> getAllConsultants() {

		log.info("get  all consultant called");
		List<ConsultantMasterResponseDTO> consultants = consultantMasterServiceImpl.getAllConsultants();
		return new ResponseEntity<>(consultants, HttpStatus.OK);
	}
	@GetMapping("/getAllConsultantsWithoutProjects")
	public ResponseEntity<List<ConsultantMasterResponseDTO>> getAllConsultantsWithoutProjects()
	{
		return new ResponseEntity<>(consultantMasterServiceImpl.getConsultantsWithoutProjects(),HttpStatus.OK);
		
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteConsultant(@PathVariable String id) {
		consultantMasterServiceImpl.deleteConsultant(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//--------------------------------------------------------------------
	@GetMapping("/displayConsultantImages/{id}")
	public ResponseEntity<List<String>> getConsultantImagesAsBase64(@PathVariable String id) {
		try {
			List<byte[]> imageData = consultantMasterServiceImpl.getConsultantImageData(id);
			List<String> base64Images = consultantMasterServiceImpl.convertToBase64(imageData);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline")
					.contentType(MediaType.APPLICATION_JSON)
					.body(base64Images);
		} catch (Exception e) {
			throw new RuntimeException("Failed to get consultants images", e);
		}
	}
	//------------------------------------------------------------
	@GetMapping("/getConsultantsWithoutProjects")
	public List<ConsultantMasterResponseDTO> getConsultantsWithoutProjects() {
		return consultantMasterServiceImpl.getConsultantsWithoutProjects();
	}

}