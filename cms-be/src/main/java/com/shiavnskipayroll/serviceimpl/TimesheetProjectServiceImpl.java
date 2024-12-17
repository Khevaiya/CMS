package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.dto.request.TimesheetProjectRequestDTO;
import com.shiavnskipayroll.dto.response.TimesheetProjectResponseDTO;
import com.shiavnskipayroll.entity.TimesheetProject;
import com.shiavnskipayroll.exceptions.TimesheetProjectNotFoundException;
import com.shiavnskipayroll.repository.TimesheetProjectRepository;
import com.shiavnskipayroll.service.TimesheetProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimesheetProjectServiceImpl implements TimesheetProjectService {
	
	
	private final TimesheetProjectRepository timesheetProjectRepository;

	@Override
	public TimesheetProjectResponseDTO createTimesheet(TimesheetProjectRequestDTO requestDTO) {
		return convertToResponseDTO(timesheetProjectRepository.save(convertToEntity(requestDTO)));
	}

	@Override
	public TimesheetProjectResponseDTO updateTimesheet(String id, TimesheetProjectRequestDTO requestDTO) {
		TimesheetProject timesheetProject = timesheetProjectRepository.findById(id)
				.orElseThrow(() -> new TimesheetProjectNotFoundException("Timesheet Project not found in class TimesheetProjectServiceImpl,method updateTimesheet"));
		timesheetProject.setDate(requestDTO.getDate());

		timesheetProject.setHoursWorked(requestDTO.getHoursWorked());
		timesheetProject.setTaskCompleted(requestDTO.getTaskCompleted());
		timesheetProject.setEmployeeId(requestDTO.getEmployeeId());
		timesheetProject.setProjectId(requestDTO.getProjectId());
		TimesheetProject updatedTimesheetProject = timesheetProjectRepository.save(timesheetProject);
		return convertToResponseDTO(updatedTimesheetProject);
	}

	@Override
	public TimesheetProjectResponseDTO getTimesheetById(String id) {
		TimesheetProject timesheetProject = timesheetProjectRepository.findById(id)
				.orElseThrow(() -> new TimesheetProjectNotFoundException("Timesheet Project not found in class TimesheetProjectServiceImpl,method getTimesheetById"));
				return convertToResponseDTO(timesheetProject);
	}

	@Override
	public List<TimesheetProjectResponseDTO> getTimesheetsByEmployeeId(String employeeId) {
		List<TimesheetProject> timesheetProjects = timesheetProjectRepository.findByEmployeeId(employeeId);
		return timesheetProjects.stream().map(this::convertToResponseDTO).toList();
	}

	@Override
	public void deleteTimesheetById(String id) {
		timesheetProjectRepository.deleteById(id);
	}
	
	@Override
	public List<TimesheetProject> getTimeSheetsByProjectId(String id)
	{
		return timesheetProjectRepository.findByProjectId(id);
	}
	
	private TimesheetProject convertToEntity(TimesheetProjectRequestDTO timesheetProjectRequestDTO)
	{
		TimesheetProject timesheetProject = new TimesheetProject();

		timesheetProject.setDate(timesheetProjectRequestDTO.getDate());
		timesheetProject.setHoursWorked(timesheetProjectRequestDTO.getHoursWorked());
		timesheetProject.setProjectId(timesheetProjectRequestDTO.getProjectId());
		timesheetProject.setTaskCompleted(timesheetProjectRequestDTO.getTaskCompleted());
		return timesheetProject;
	}

	private TimesheetProjectResponseDTO convertToResponseDTO(TimesheetProject timesheetProject) {
		TimesheetProjectResponseDTO responseDTO = new TimesheetProjectResponseDTO();
		responseDTO.setId(timesheetProject.getId());
		responseDTO.setDate(timesheetProject.getDate());
		responseDTO.setHoursWorked(timesheetProject.getHoursWorked());
		responseDTO.setProjectId(timesheetProject.getProjectId());
		responseDTO.setTaskCompleted(timesheetProject.getTaskCompleted());
		return responseDTO;
	}
	public List<TimesheetProjectResponseDTO> findAllByProjectIdAndDateStartingWith(String projectId,String yearMonth)
	{
		return timesheetProjectRepository.findAllByProjectIdAndDateStartingWith(projectId,yearMonth);
	}
}
