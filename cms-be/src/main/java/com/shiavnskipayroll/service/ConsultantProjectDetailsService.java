package com.shiavnskipayroll.service;

import com.shiavnskipayroll.dto.request.ConsultantProjectDetailsRequestDTO;
import com.shiavnskipayroll.dto.response.ConsultantProjectDetailsResponseDTO;

import java.util.List;

public interface ConsultantProjectDetailsService {
    ConsultantProjectDetailsResponseDTO createConsultantProject(ConsultantProjectDetailsRequestDTO requestDTO);
    List<ConsultantProjectDetailsResponseDTO> getAllConsultantProjects();
    ConsultantProjectDetailsResponseDTO getConsultantProjectById(String id);
    ConsultantProjectDetailsResponseDTO updateConsultantProject(String id, ConsultantProjectDetailsRequestDTO requestDTO);
    void deleteConsultantProject(String id);
}
