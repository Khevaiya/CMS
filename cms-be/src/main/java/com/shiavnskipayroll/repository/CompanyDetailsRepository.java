package com.shiavnskipayroll.repository;

import com.shiavnskipayroll.entity.CompanyDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDetailsRepository extends MongoRepository<CompanyDetails,String>{
    CompanyDetails findFirstBy();
}
