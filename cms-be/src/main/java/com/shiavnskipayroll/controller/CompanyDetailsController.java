package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.dto.request.CompanyDetailsRequestDTO;
import com.shiavnskipayroll.dto.response.CompanyDetailsResponseDTO;
import com.shiavnskipayroll.service.CompanyDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RequestMapping("/api/v1/companydetails")
@RestController
public class CompanyDetailsController {


	private final CompanyDetailsService companyDetailsService;

	@PostMapping("/createOrUpdateCompanyDetails")
	public ResponseEntity<CompanyDetailsResponseDTO> createOrUpdateCompanyDetails(
			@ModelAttribute CompanyDetailsRequestDTO requestDTO) {
		System.out.println("company details "+requestDTO.toString());
		CompanyDetailsResponseDTO responseDTO = companyDetailsService.createOrUpdateCompanyDetails(requestDTO);
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@GetMapping("/getCompanyDetails")
	public ResponseEntity<CompanyDetailsResponseDTO> getCompanyDetails() {
		CompanyDetailsResponseDTO responseDTO = companyDetailsService.getCompanyDetails();
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

}
