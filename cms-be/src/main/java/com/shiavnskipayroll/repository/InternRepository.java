package com.shiavnskipayroll.repository;

import com.shiavnskipayroll.entity.Intern;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InternRepository   extends MongoRepository<Intern,String>{
	
	Intern findByPersonalEmail(String email);
	
}

	
