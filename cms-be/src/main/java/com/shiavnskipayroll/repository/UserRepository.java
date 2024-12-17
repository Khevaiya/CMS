package com.shiavnskipayroll.repository;

import com.shiavnskipayroll.entity.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<Admin, String> {
}
