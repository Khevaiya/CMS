package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.dto.request.TimeSheetEmployeeRequestDto;
import com.shiavnskipayroll.dto.response.TimeSheetEmployeeResponseDto;
import com.shiavnskipayroll.entity.TimesheetEmployee;
import com.shiavnskipayroll.exceptions.TimesheetEmployeeNotFoundException;
import com.shiavnskipayroll.mapper.TimeSheetMapper;
import com.shiavnskipayroll.repository.TimeSheetEmployeeRepository;
import com.shiavnskipayroll.service.TimeSheetEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeSheetEmployeeServiceImpl implements TimeSheetEmployeeService {

	private final TimeSheetEmployeeRepository timeSheetEmployeeRepository;

	@Override
	public String createTimeSheet(TimeSheetEmployeeRequestDto requestDto) {
		log.info("Creating TimeSheetEmployee with request: {} ", requestDto);

		TimesheetEmployee timesheetEmployee = TimeSheetMapper.toEntity(requestDto);
		String id = timeSheetEmployeeRepository.save(timesheetEmployee).getId();
		log.info("TimeSheetemployee created successfully with id: {}", id);

		return "TimeSheetemployee created successfully";
	}

	@Override
	public List<TimeSheetEmployeeResponseDto> getAllTimeSheets() {
		log.info("Getting all Time Sheets of Employees");

		List<TimesheetEmployee> list = timeSheetEmployeeRepository.findAll();
		log.info("TimeSheet List: {}", list);
		List<TimeSheetEmployeeResponseDto> responseList = list.stream().map(TimeSheetMapper::toRDto).toList();
		log.info("Response List: {}", responseList);
		return responseList;

	}

	@Override
	public List<TimeSheetEmployeeResponseDto> getAllById(String id) {
		log.info("Getting All TimeSheet By Employee Id: {}", id);
		List<TimesheetEmployee> timesheetEmployee = timeSheetEmployeeRepository.findAllByEmployeeId(id);
		return timesheetEmployee.stream().map(TimeSheetMapper::toRDto).toList();
	}
	@Override
	public  void deleteTimesheetById(String id)
	{
		if(id==null)throw new IllegalArgumentException(" ID is null In class TimesheetEmployeeServiceImpl ,method deleteTimesheetById");
		timeSheetEmployeeRepository.findById(id).orElseThrow(()->new TimesheetEmployeeNotFoundException(" In class TimesheetEmployeeServiceImpl ,method deleteTimesheetById"));
        timeSheetEmployeeRepository.deleteById(id);
	}

	@Override
	public TimeSheetEmployeeResponseDto updateTimesheet(TimeSheetEmployeeRequestDto timeSheetEmployeeRequestDto,String id)
	{
		if(timeSheetEmployeeRequestDto==null)throw new IllegalArgumentException(" of timeSheetEmployeeRequestDto In class TimesheetEmployeeServiceImpl ,method updateTimesheet");
		TimesheetEmployee timesheetEmployee=timeSheetEmployeeRepository.findById(id).orElseThrow(()->new TimesheetEmployeeNotFoundException(" In class TimesheetEmployeeServiceImpl ,method updateTimesheet"));
		timesheetEmployee.setEmployeeId(timeSheetEmployeeRequestDto.getEmployeeId());
	    timesheetEmployee.setDate(timeSheetEmployeeRequestDto.getDate());

		timesheetEmployee.setTaskCompleted(timeSheetEmployeeRequestDto.getTaskCompleted());
		timesheetEmployee.setHoursWorked(timeSheetEmployeeRequestDto.getHoursWorked());
		return TimeSheetMapper.toRDto(timeSheetEmployeeRepository.save(timesheetEmployee));
	}
	public  List<TimeSheetEmployeeResponseDto >getTimesheetByEmployeeId(String id)
	{
		return timeSheetEmployeeRepository.findAllByEmployeeId(id).stream().map(TimeSheetMapper::toRDto).toList();
	}
	public  List<TimeSheetEmployeeResponseDto> getTimesheetsByDate(String date)
	{
		return timeSheetEmployeeRepository.findAllByDateStartingWith(date);
	}

	public  List<TimeSheetEmployeeResponseDto> getTimesheetsByDateAndEmployeeId(String date,String employeeId)
	{
		return timeSheetEmployeeRepository.findAllByEmployeeIdAndDateStartingWith(employeeId,date);
	}


}
