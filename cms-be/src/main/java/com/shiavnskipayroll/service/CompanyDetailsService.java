package com.shiavnskipayroll.service;


import com.shiavnskipayroll.dto.request.CompanyDetailsRequestDTO;
import com.shiavnskipayroll.dto.response.CompanyDetailsResponseDTO;


public interface CompanyDetailsService  {
	CompanyDetailsResponseDTO createOrUpdateCompanyDetails(CompanyDetailsRequestDTO companyDetails);
    CompanyDetailsResponseDTO getCompanyDetails();
}
