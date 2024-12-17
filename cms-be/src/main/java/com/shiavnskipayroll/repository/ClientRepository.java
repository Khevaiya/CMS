package com.shiavnskipayroll.repository;

import   com.shiavnskipayroll.entity.ClientMaster;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<ClientMaster, String> {
}
