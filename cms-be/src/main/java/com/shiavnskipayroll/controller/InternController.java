package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.dto.request.InternRequestDTO;
import com.shiavnskipayroll.dto.response.InternResponseDTO;
import com.shiavnskipayroll.entity.ProjectMaster;
import com.shiavnskipayroll.serviceimpl.InternServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/interns")
public class InternController {

	private final InternServiceImpl internService;


	@PostMapping("/create")
	public ResponseEntity<InternResponseDTO> createIntern(@ModelAttribute InternRequestDTO internRequestDTO,
			@RequestParam(value = "photo", required = false) MultipartFile photo,
			@RequestParam(value = "recentMarksheetOrDegreePhoto", required = false) MultipartFile recentMarksheetOrDegreePhoto,
			@RequestParam(value = "panPhoto", required = false) MultipartFile panPhoto,
			@RequestParam(value = "aadhaarPhoto", required = false) MultipartFile aadhaarPhoto,
			@RequestParam(value = "passbookPhoto", required = false) MultipartFile passbookPhoto) {
		try {
			log.info("createIntern called ");
			log.info("photo" + photo.getOriginalFilename());
			log.info("markSheetOrDegreePhoto" + recentMarksheetOrDegreePhoto.getOriginalFilename());
			log.info("panPhoto" + panPhoto.getOriginalFilename());
			log.info("aadhaarPhoto" + aadhaarPhoto.getOriginalFilename());
			log.info("passbookPhoto" + passbookPhoto.getOriginalFilename());
			InternResponseDTO i = internService.createIntern(internRequestDTO, photo, recentMarksheetOrDegreePhoto,
					panPhoto, aadhaarPhoto, passbookPhoto);
			return new ResponseEntity<>(i, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<InternResponseDTO>> getAllInterns() {
		try {
			List<InternResponseDTO> interns = internService.getAllInterns();
			return new ResponseEntity<>(interns, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<InternResponseDTO> getInternById(@PathVariable String id) {
		try {

			InternResponseDTO intern = internService.getInternById(id);
			if (intern != null) {
				return new ResponseEntity<>(intern, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateIntern(@PathVariable String id,
			@ModelAttribute InternRequestDTO internRequestDTO,
			@RequestParam(value = "photo", required = false) MultipartFile photo,
			@RequestParam(value = "recentMarksheetOrDegreePhoto", required = false) MultipartFile recentMarksheetOrDegreePhoto,
			@RequestParam(value = "panPhoto", required = false) MultipartFile panPhoto,
			@RequestParam(value = "aadhaarPhoto", required = false) MultipartFile aadhaarPhoto,
			@RequestParam(value = "passbookPhoto", required = false) MultipartFile passbookPhoto) {
		log.info("updateIntern called ");
		try {
			//System.out.println("photo"+photo.getOriginalFilename());
			boolean isUpdated = internService.updateIntern(id, internRequestDTO, photo, recentMarksheetOrDegreePhoto,
					panPhoto, aadhaarPhoto, passbookPhoto);
			log.info("boolean " + isUpdated);
			if (isUpdated) {
				return new ResponseEntity<>("Intern updated successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Intern not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error updating intern", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteIntern(@PathVariable String id) {
		try {
			boolean isDeleted = internService.deleteIntern(id);
			if (isDeleted) {
				return new ResponseEntity<>("Intern deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Intern not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error deleting intern", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}/projects")
	public ResponseEntity<List<ProjectMaster>> getProjectsOfInternById(@PathVariable String id) {
		try {

			List<ProjectMaster> projects = internService.getProjectsOfInternById(id);
			if (projects != null && !projects.isEmpty()) {

				return new ResponseEntity<>(projects, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//------------------------------31oct----------------------------
	@GetMapping("/displayInternImages/{id}")
	public ResponseEntity<List<String>> getInternImagesAsBase64(@PathVariable String id) {
		try {
			List<byte[]> imageData = internService.getInternImageData(id);
			List<String> base64Images = internService.convertToBase64(imageData);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline")
					.contentType(MediaType.APPLICATION_JSON)
					.body(base64Images);
		} catch (Exception e) {
			throw new RuntimeException("Failed to get intern images", e);
		}
	}
	//------------------------------------------------
	@GetMapping("/getInternsWithoutProjects")
	public List<InternResponseDTO> getInternsWithoutProjects() {
		return internService.getInternWithoutProjects();
	}
}