package com.shiavnskipayroll.repository;

import   com.shiavnskipayroll.entity.ProjectMaster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProjectRepository extends MongoRepository<ProjectMaster,String> {
  List<ProjectMaster> findByEmployeeId(String employeeId);
  List<ProjectMaster> findAllByIdIn(Set<String> projectIds);

}
