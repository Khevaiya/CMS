package com.shiavnskipayroll.service;

import com.shiavnskipayroll.dto.request.TimesheetProjectRequestDTO;
import com.shiavnskipayroll.dto.response.TimesheetProjectResponseDTO;
import com.shiavnskipayroll.entity.TimesheetProject;

import java.util.List;

public interface TimesheetProjectService {
    TimesheetProjectResponseDTO createTimesheet(TimesheetProjectRequestDTO timesheetProjectRequestDTO);
    TimesheetProjectResponseDTO updateTimesheet(String id, TimesheetProjectRequestDTO timesheetProjectRequestDTO);
    TimesheetProjectResponseDTO getTimesheetById(String id);
    List<TimesheetProjectResponseDTO> getTimesheetsByEmployeeId(String employeeId);
    void deleteTimesheetById(String id);
    List<TimesheetProject> getTimeSheetsByProjectId(String id);
}
