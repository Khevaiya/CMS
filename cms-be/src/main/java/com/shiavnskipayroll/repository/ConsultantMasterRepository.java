package com.shiavnskipayroll.repository;

import com.shiavnskipayroll.entity.ConsultantMaster;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsultantMasterRepository extends MongoRepository<ConsultantMaster, String> {
	
	ConsultantMaster findByEmail(String email);
}
