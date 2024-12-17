package com.shiavnskipayroll.repository;

import com.shiavnskipayroll.dto.response.ConsultantProjectDetailsResponseDTO;
import com.shiavnskipayroll.entity.ConsultantProjectDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ConsultantProjectDetailsRepository extends MongoRepository<ConsultantProjectDetails, String> {
	ConsultantProjectDetails findByProjectId(String projectId);
	List<ConsultantProjectDetailsResponseDTO> findByProjectIdIn(List<String> projectIds);
}
