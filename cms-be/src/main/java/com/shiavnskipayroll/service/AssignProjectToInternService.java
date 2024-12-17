package com.shiavnskipayroll.service;

import com.shiavnskipayroll.entity.Intern;

import java.util.Set;

public interface AssignProjectToInternService {
	   void assignProjectToIntern(String projectId,String internId);
	    void unassignProjectFromIntern(String projectId, String internId);
	    Set<String> getProjectsByInternId(String internId);
	    Set<Intern> getInternsByProjectId(String projectId) ;
	     Set<Intern> getInternsWhichNotOnProjectByProjectId(String projectId) ;
	    
	    
}
