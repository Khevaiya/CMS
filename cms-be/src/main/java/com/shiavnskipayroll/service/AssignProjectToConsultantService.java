package com.shiavnskipayroll.service;

import com.shiavnskipayroll.entity.ConsultantMaster;

import java.util.Set;

public interface AssignProjectToConsultantService {
    void assignProjectToConsultant(String projectId,String consultantId);
    void unassignProjectFromConsultant(String projectId, String consultantId);
    Set<String> getProjectsByConsultantId(String consultantId);
    Set<ConsultantMaster> getConsultantsByProjectId(String projectId) ;
    Set<ConsultantMaster> getConsultantsWhichNotOnProjectByProjectId(String projectId);
}
