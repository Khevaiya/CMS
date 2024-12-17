package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.dto.request.CompanyDetailsRequestDTO;
import com.shiavnskipayroll.dto.response.CompanyDetailsResponseDTO;
import com.shiavnskipayroll.entity.CompanyDetails;
import com.shiavnskipayroll.exceptions.ResourceNotFoundException;
import com.shiavnskipayroll.repository.CompanyDetailsRepository;
import com.shiavnskipayroll.service.CompanyDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CompanyDetailsServiceImpl implements CompanyDetailsService {
	

	    private final CompanyDetailsRepository companyDetailsRepository;

	    @Override
	    public CompanyDetailsResponseDTO createOrUpdateCompanyDetails(CompanyDetailsRequestDTO requestDTO) {
	    	Optional<CompanyDetails> existingCompanyDetails = companyDetailsRepository.findAll().stream().findFirst();
	        CompanyDetails companyDetails;
	        if (existingCompanyDetails.isPresent()) {
	            companyDetails = existingCompanyDetails.get();
	            updateEntityFromDTO(requestDTO,companyDetails);
	        } else {
	            companyDetails = convertToEntity(requestDTO); // Convert DTO to entity
	        }
	        return convertToResponseDTOFromEntity(companyDetailsRepository.save(companyDetails)); 
	    }
	
	    @Override
	    public CompanyDetailsResponseDTO getCompanyDetails() {
	        CompanyDetails companyDetails = companyDetailsRepository.findAll().stream().findFirst()
	                .orElseThrow(() -> new ResourceNotFoundException("Company details not found"));
	        return convertToResponseDTOFromEntity(companyDetails);
	    }

	    private void  updateEntityFromDTO(CompanyDetailsRequestDTO requestDTO,CompanyDetails existingCompanyDetails) {
	        
	    	existingCompanyDetails.setCompanyName(requestDTO.getCompanyName());
	    	existingCompanyDetails.setCompanyAddress(requestDTO.getCompanyAddress());
	    	existingCompanyDetails.setCity(requestDTO.getCity());
	    	existingCompanyDetails.setState(requestDTO.getState());
	        existingCompanyDetails.setCountry(requestDTO.getCountry());
	        existingCompanyDetails.setPinCode(requestDTO.getPinCode());
	        existingCompanyDetails.setContactNumber1(requestDTO.getContactNumber1());
	        existingCompanyDetails.setContactNumber2(requestDTO.getContactNumber2());
	        existingCompanyDetails.setCompanyEmail1(requestDTO.getCompanyEmail1());
	        existingCompanyDetails.setCompanyEmail2(requestDTO.getCompanyEmail2());
	        existingCompanyDetails.setWebsiteUrl(requestDTO.getWebsiteUrl());
	        existingCompanyDetails.setRegistrationNumber(requestDTO.getRegistrationNumber());
	        existingCompanyDetails.setGstNumber(requestDTO.getGstNumber());
	        existingCompanyDetails.setPanNumber(requestDTO.getPanNumber());
	        existingCompanyDetails.setDescription(requestDTO.getDescription());
			existingCompanyDetails.setCompanyLastWorkingDay(requestDTO.getCompanyLastWorkingDay());
			existingCompanyDetails.setCompanyMonthlyExpenditure(requestDTO.getCompanyMonthlyExpenditure());
	       
	    }
	    private CompanyDetails convertToEntity(CompanyDetailsRequestDTO requestDTO) {
	        CompanyDetails companyDetails = new CompanyDetails();
	        companyDetails.setCompanyName(requestDTO.getCompanyName());
	        companyDetails.setCompanyAddress(requestDTO.getCompanyAddress());
	        companyDetails.setCity(requestDTO.getCity());
	        companyDetails.setState(requestDTO.getState());
	        companyDetails.setCountry(requestDTO.getCountry());
	        companyDetails.setPinCode(requestDTO.getPinCode());
	        companyDetails.setContactNumber1(requestDTO.getContactNumber1());
	        companyDetails.setContactNumber2(requestDTO.getContactNumber2());
	        companyDetails.setCompanyEmail1(requestDTO.getCompanyEmail1());
	        companyDetails.setCompanyEmail2(requestDTO.getCompanyEmail2());
	        companyDetails.setWebsiteUrl(requestDTO.getWebsiteUrl());
	        companyDetails.setRegistrationNumber(requestDTO.getRegistrationNumber());
	        companyDetails.setGstNumber(requestDTO.getGstNumber());
	        companyDetails.setPanNumber(requestDTO.getPanNumber());
	        companyDetails.setDescription(requestDTO.getDescription());
			companyDetails.setCompanyMonthlyExpenditure(requestDTO.getCompanyMonthlyExpenditure());
			companyDetails.setCompanyLastWorkingDay(requestDTO.getCompanyLastWorkingDay());
	        return companyDetails;
	    }

	    private CompanyDetailsResponseDTO convertToResponseDTOFromEntity(CompanyDetails companyDetails) {
	        CompanyDetailsResponseDTO dto = new CompanyDetailsResponseDTO();
	        dto.setId(companyDetails.getId());
	        dto.setCompanyName(companyDetails.getCompanyName());
	        dto.setCompanyAddress(companyDetails.getCompanyAddress());
	        dto.setCity(companyDetails.getCity());
	        dto.setState(companyDetails.getState());
	        dto.setCountry(companyDetails.getCountry());
	        dto.setPinCode(companyDetails.getPinCode());
	        dto.setContactNumber1(companyDetails.getContactNumber1());
	        dto.setContactNumber2(companyDetails.getContactNumber2());
	        dto.setCompanyEmail1(companyDetails.getCompanyEmail1());
	        dto.setCompanyEmail2(companyDetails.getCompanyEmail2());
	        dto.setWebsiteUrl(companyDetails.getWebsiteUrl());
	        dto.setRegistrationNumber(companyDetails.getRegistrationNumber());
	        dto.setGstNumber(companyDetails.getGstNumber());
	        dto.setPanNumber(companyDetails.getPanNumber());
	        dto.setDescription(companyDetails.getDescription());
			dto.setCompanyLastWorkingDay(companyDetails.getCompanyLastWorkingDay());
			dto.setCompanyMonthlyExpenditure(companyDetails.getCompanyMonthlyExpenditure());
	        return dto;
	    }

}
