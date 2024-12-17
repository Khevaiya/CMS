package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.dto.request.TimeSheetEmployeeRequestDto;
import com.shiavnskipayroll.dto.response.TimeSheetEmployeeResponseDto;
import com.shiavnskipayroll.serviceimpl.TimeSheetEmployeeServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/timesheetEmployee")
@CrossOrigin(origins = "http://localhost:5173")
public class TimesheetEmployeeController {

	private final TimeSheetEmployeeServiceImpl serviceIpl;

	@GetMapping("/all")
	public ResponseEntity<List<TimeSheetEmployeeResponseDto>> getAllTimeSheet() throws MessagingException {
		return ResponseEntity.ok().body(serviceIpl.getAllTimeSheets());
	}

	@PostMapping("/create")
	public ResponseEntity<String> createTimeSheet(@RequestBody TimeSheetEmployeeRequestDto requestDto) {
		log.info("Creating TimeSheetEmployee With request: {}", requestDto);
		return ResponseEntity.ok().body(serviceIpl.createTimeSheet(requestDto));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<List<TimeSheetEmployeeResponseDto>> getAllById(@PathVariable String id){
		return ResponseEntity.ok().body(serviceIpl.getAllById(id));
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteTimesheetById(@PathVariable  String id)
	{
		serviceIpl.deleteTimesheetById(id);
		return new ResponseEntity<>( HttpStatus.NO_CONTENT);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity< TimeSheetEmployeeResponseDto> updateTimesheetById(@ModelAttribute TimeSheetEmployeeRequestDto requestDto ,@PathVariable String id)
	{
		log.info("requestDto"+requestDto);
		return new ResponseEntity<>(serviceIpl.updateTimesheet(requestDto,id),HttpStatus.OK);
	}
	@GetMapping("/getTimesheetByEmployeeId/{id}")
public 	ResponseEntity< List<TimeSheetEmployeeResponseDto>>getTimesheetByEmployeeId(@PathVariable String id)
	{
		return new ResponseEntity<>(serviceIpl.getTimesheetByEmployeeId(id),HttpStatus.OK);
	}
	@GetMapping("/getTimesheetsByDate")
	public  ResponseEntity< List<TimeSheetEmployeeResponseDto>> getTimesheetsByDate(@RequestParam String year,@RequestParam String month)
	{
		String yearMonth = year + "-" + month; // Format: "YYYY-MM"
		return new ResponseEntity<>(serviceIpl.getTimesheetsByDate(yearMonth),HttpStatus.OK);
	}
	@GetMapping("/getTimesheetsByDateAndEmployeeId")
	public  ResponseEntity< List<TimeSheetEmployeeResponseDto>> getTimesheetsByDateAndEmployeeId(@RequestParam String year,@RequestParam String month,@RequestParam String employeeId)
	{
		System.out.println("getTimesheetsByDateAndEmployeeId called "+year+month+"    "+employeeId);
		String yearMonth = year + "-" + month; // Format: "YYYY-MM"
		return new ResponseEntity<>(serviceIpl.getTimesheetsByDateAndEmployeeId(yearMonth,employeeId),HttpStatus.OK);
	}
}