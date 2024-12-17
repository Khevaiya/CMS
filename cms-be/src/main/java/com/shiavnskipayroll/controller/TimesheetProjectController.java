package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.dto.request.TimesheetProjectRequestDTO;

import com.shiavnskipayroll.dto.response.TimesheetProjectResponseDTO;
import com.shiavnskipayroll.entity.ConsultantMaster;
import com.shiavnskipayroll.entity.EmployeeMaster;
import com.shiavnskipayroll.entity.Intern;
import com.shiavnskipayroll.entity.TimesheetProject;
import com.shiavnskipayroll.repository.ConsultantMasterRepository;
import com.shiavnskipayroll.repository.EmployeeRepository;
import com.shiavnskipayroll.repository.InternRepository;
import com.shiavnskipayroll.repository.TimesheetProjectRepository;
import com.shiavnskipayroll.service.TimesheetProjectService;
import com.shiavnskipayroll.serviceimpl.TimesheetProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/timesheetProject")
@CrossOrigin(origins = "http://localhost:5173")
public class TimesheetProjectController {


	private final TimesheetProjectService timesheetProjectService;

	private  final TimesheetProjectRepository timesheetProjectRepository;

	private final  TimesheetProjectServiceImpl timesheetProjectServiceImpl;


	private final InternRepository internRepository;


	private final ConsultantMasterRepository consultantMasterRepository;


	private final  EmployeeRepository employeeRepository;


	//@PreAuthorize("hasRole('client_user')")
	@PostMapping("/create")
	public ResponseEntity<TimesheetProjectResponseDTO> createTimesheet(
			@RequestBody TimesheetProjectRequestDTO timesheetProjectRequestDTO) {
		TimesheetProjectResponseDTO responseDTO = timesheetProjectService.createTimesheet(timesheetProjectRequestDTO);
		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateTimesheetProject(@RequestBody TimesheetProjectRequestDTO timesheetProjectRequestDTO,@PathVariable String id)
	{

		TimesheetProjectResponseDTO timesheet=timesheetProjectServiceImpl.updateTimesheet(id,timesheetProjectRequestDTO);
		if(timesheet!=null)return new ResponseEntity<>(timesheet,HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@GetMapping("/{id}")
	public ResponseEntity<TimesheetProjectResponseDTO> getTimesheetById(@PathVariable String id) {
		TimesheetProjectResponseDTO timesheet = timesheetProjectService.getTimesheetById(id);
		return ResponseEntity.ok(timesheet);
	}

	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<List<TimesheetProjectResponseDTO>> getTimesheetsByEmployeeId(
			@PathVariable String employeeId) {


		List<TimesheetProjectResponseDTO> timesheets = timesheetProjectService.getTimesheetsByEmployeeId(employeeId);
		return ResponseEntity.ok(timesheets);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteTimesheetById(@PathVariable String id) {
		timesheetProjectService.deleteTimesheetById(id);
		return ResponseEntity.noContent().build();
	}

	//@PreAuthorize("hasRole('client_user')")
	@GetMapping("/getLoginUserMongoAutoUniqueIdIdByAccessToken")
	public ResponseEntity<?> getLoginUserMongoAutoUniqueIdIdByAccessToken(
			@RequestHeader("Authorization") String token) {

System.out.println("getLoginUserMongoAutoUniqueIdIdByAccessToken"+token);
		// Extract the actual token, removing "Bearer " if it's present
		String accessTokenString = token.startsWith("Bearer ") ? token.substring(7) : token;

		try {
			// Log the raw token for debugging

			// Decode the access token to extract user details
			AccessToken accessToken = TokenVerifier.create(accessTokenString, AccessToken.class).getToken();

			// Get user details from the token
			String email = accessToken.getEmail();// User's username

			EmployeeMaster empMaster = employeeRepository.findByEmail(email);
			if (empMaster != null) {
				System.out.println("EMployee id"+empMaster.getId());
				return ResponseEntity.ok(empMaster.getId());
			}
			ConsultantMaster consultantMaster = consultantMasterRepository.findByEmail(email);
			if (consultantMaster != null)
				return ResponseEntity.ok(consultantMaster.getId());

			Intern intern = internRepository.findByPersonalEmail(email);
			if (intern != null)
				return ResponseEntity.ok(intern.getId());

			// No user found
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email: " + email);
		} catch (VerificationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token: " + e.getMessage());
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving user information: " + e.getMessage());
		}

	}
	@GetMapping("/getTimeSheetsByProjectId/{id}")
	ResponseEntity<List<TimesheetProject>> getTimeSheetsByProjectId(@PathVariable String id)
	{
		List<TimesheetProject>timeSheets= timesheetProjectServiceImpl.getTimeSheetsByProjectId(id) ;
		return new ResponseEntity<>(timeSheets,HttpStatus.OK);
	}

	@GetMapping("/all")
	ResponseEntity<List<TimesheetProject>> getALlTimesheetProject()
	{
		List<TimesheetProject> timesheetProjectList=timesheetProjectRepository.findAll();
		return new ResponseEntity<>(timesheetProjectList,HttpStatus.OK);
	}
	@GetMapping("/getTimesheetsByDateAndProjectId")
	public  ResponseEntity< List<TimesheetProjectResponseDTO>> getTimesheetsByDateAndProjectId(@RequestParam String year, @RequestParam String month, @RequestParam String projectId)
	{
		System.out.println("getTimesheetsByDateAndProjectId called "+year+month+"    "+projectId);
		String yearMonth = year + "-" + month; // Format: "YYYY-MM"
		return new ResponseEntity<>(timesheetProjectServiceImpl.findAllByProjectIdAndDateStartingWith(projectId,yearMonth),HttpStatus.OK);
	}


}
