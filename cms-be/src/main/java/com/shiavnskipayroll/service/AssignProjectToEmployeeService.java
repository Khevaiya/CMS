package com.shiavnskipayroll.service;

import com.shiavnskipayroll.entity.EmployeeMaster;

import java.util.Set;


public interface AssignProjectToEmployeeService {
    void assignProjectToEmployee(String projectId,String employeeId);
    void unassignProjectFromEmployee(String projectId, String employeeId);
    Set<String> getProjectsByEmployeeId(String employeeId);
    Set<EmployeeMaster> getEmployeesByProjectId(String projectId);
    Set<EmployeeMaster> getEmployeesWhichNotOnProjectByProjectId(String projectId);
}
