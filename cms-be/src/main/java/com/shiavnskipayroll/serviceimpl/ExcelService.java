package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.entity.*;
import com.shiavnskipayroll.exceptions.*;
import com.shiavnskipayroll.repository.*;
import com.shiavnskipayroll.service.S3Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {

	@Value("${spring.mail.username}")
	private String fromEmail;

	@Value("${spring.mail.hr}")
	private String toEmail;


	private final CompanyDetailsRepository companyDetailsRepository;
	private final TimesheetProjectRepository timesheetProjectRepository;
	private final JavaMailSender mailSender;
	private final ProjectRepository projectRepository;
	private final EmployeeRepository employeeRepository;
	private final ConsultantMasterRepository consultantMasterRepository;
	private final S3Service s3Service;
	private final TimeSheetEmployeeRepository timeSheetEmployeeRepository;
	private final InternRepository internRepository;


	public List<TimesheetProject> getTimesheetsByProjectId(String projectId) {
		log.info("project id: {}", projectId);
		return timesheetProjectRepository.findByProjectId(projectId);
	}

	//Schedule email tasks for each project based on its last working day
	@Scheduled(cron = "0 0 9 * * *") // Cron expression: at 8:00 AM every day
	public void scheduleEmailTasks() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);


		List<ProjectMaster> allProjects = projectRepository.findAll();// Retrieve all projects

		for (ProjectMaster project : allProjects) {
			try {
				log.info("scheduleEmailTasks called");
				String lastWorkingDayString = project.getLastWorkingDayOfMonth(); // Get the last working day
				log.info("lastWorkingDay: {}", lastWorkingDayString);
				// Parse the string to an integer day
				int lastWorkingDayOfMonth = Integer.parseInt(lastWorkingDayString);
				LocalDate today = LocalDate.now();
				int todayDayOfMonth = today.getDayOfMonth();
				int maxDaysInMonth = today.lengthOfMonth();
				if (lastWorkingDayOfMonth > maxDaysInMonth) {
					lastWorkingDayOfMonth = maxDaysInMonth;
				}
				// Check if today is the specified last working day of the month
				if (todayDayOfMonth == lastWorkingDayOfMonth) {
					log.info("scheduleEmailTasks called2");
					sendTimesheetForProject(project); // Call the function to send the Time-sheet
				}
			} catch (Exception e) {
				log.info("Error processing project name: {} and message: {} ", project.getProjectName(),
						e.getMessage());
			}
		}

	}

	// Send Time-sheet for a project via email
	public void sendTimesheetForProject(ProjectMaster project) {
		List<TimesheetProject> timesheets = getTimesheetsByProjectId(project.getId()); // Get Time-sheet for the project
		String interviewClearedCandidateName;
		if (project.getCandidateType() == "consultant") {
			Optional<ConsultantMaster> optionalConsultantMaster = consultantMasterRepository.findById(project.getInterviewdClearedCandidateId());
			if (!optionalConsultantMaster.isPresent()) {
				throw new ConsultantNotFoundException("Consultant Not found in class ExcelService ,method sendTimesheetForProject");
			}
			ConsultantMaster consultantMaster = optionalConsultantMaster.get();
			interviewClearedCandidateName = consultantMaster.getFirstName();
		} else {

			Optional<EmployeeMaster> optionalEmployeeMaster = employeeRepository.findById(project.getInterviewdClearedCandidateId());
			if (!optionalEmployeeMaster.isPresent()) {
				throw new EmployeeNotFoundException("Employee Not found in class ExcelService ,method sendTimesheetForProject");

			}
			EmployeeMaster employeeMaster = optionalEmployeeMaster.get();
			interviewClearedCandidateName = employeeMaster.getFirstName();
		}


		log.info("timesheets Data size: {}", timesheets.size());
		try {

			// Generate XLS file for the project
			File file = generateXlsFileForProject(timesheets, project.getId(), interviewClearedCandidateName);

			if (file == null)
				log.info("file is null");


			if (file != null) {
				MultipartFile multipartFile = new MockMultipartFile(
						"file", // name of the parameter
						file.getName(), // original file name
						Files.probeContentType(file.toPath()), // content type
						Files.readAllBytes(file.toPath()) // file content as byte array
				);
				LocalDateTime now = LocalDateTime.now();
				int currentYear = now.getYear();
				int currentMonth = now.getMonthValue();
				s3Service.uploadFiles(multipartFile, "Timesheet/" + project.getProjectName() + "/" + interviewClearedCandidateName + "/" + currentYear + "/" + currentMonth + "/" + "timesheet_" + project.getProjectName() + ".xls");
				sendEmailWithAttachment(toEmail, "Timesheet for " + project.getProjectName(),
						"Please find the attached timesheet for Project " + project.getProjectName(), file);
			}
			if (file.delete()) {
				log.info("File deleted successfully after sending the email.");
			} else {
				log.warn("Failed to delete the file after sending the email.");
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log any exceptions that occur
		}
	}


	// Generate XLS file for Time-sheet data of a project
	public File generateXlsFileForProject(List<TimesheetProject> timesheets, String projectId, String interviewClearedCandidateName) {
		HSSFWorkbook workbook = null;
		FileOutputStream fileOut = null;
		File file = null; // Initialize File object
		try {
			workbook = new HSSFWorkbook(); // Create a new workbook
			// name of interviewClearedCandidateName
			HSSFSheet sheet = workbook.createSheet(interviewClearedCandidateName);

			// Create header row
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("Date");
			headerRow.createCell(1).setCellValue("Hours Worked");
			headerRow.createCell(2).setCellValue("Task Completed");

			// Populate sheet with Time-sheet data
			int rowNum = 1;

			log.info("generateXlsFileForProject called");
			for (TimesheetProject timesheet : timesheets) {

				log.info("Timeshet1" + "Date" + timesheet.getDate() + "hourworked" + timesheet.getHoursWorked()
						+ "getTaskCompleted" + timesheet.getTaskCompleted());


				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(timesheet.getDate().toString());
				row.createCell(1).setCellValue(timesheet.getHoursWorked());
				row.createCell(2).setCellValue(timesheet.getTaskCompleted());
			}

			// Fetch project details
			ProjectMaster pm = projectRepository.findById(projectId)
					.orElseThrow(() -> new ProjectNotFoundException(projectId));

			String projectName = pm.getProjectName().replaceAll("[^a-zA-Z0-9]", "_"); // Sanitize project name
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String formattedDate = currentDate.format(formatter);
			// Write the output to a file
			file = new File(projectName + "_timesheet_" + formattedDate + ".xls"); // Create a File object
			fileOut = new FileOutputStream(file); // Initialize FileOutputStream with the file
			workbook.write(fileOut); // Write the workbook to the output stream
		} catch (IOException e) {
			// Handle exception during file writing
			e.printStackTrace(); // You may want to log this properly
		} finally {
			// Close the FileOutputStream if it's not null
			if (fileOut != null) {
				try {
					fileOut.close(); // Close the file output stream
				} catch (IOException e) {
					e.printStackTrace(); // Handle exception during file closing
				}
			}

			// Close the workbook in the finally block to ensure it's closed regardless of
			// exceptions
			if (workbook != null) {
				try {
					workbook.close(); // Close the workbook
				} catch (IOException e) {
					e.printStackTrace(); // Handle exception during workbook closing
				}
			}
		}
		return file; // Return the File object
	}

	// Send email with attachment using JavaMailSender
	public void sendEmailWithAttachment(String to, String subject, String body, File file) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(fromEmail);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body + "<br><br><br><br>Thank You", true);
		helper.addAttachment(file.getName(), file);
		mailSender.send(message);
		log.info("send mail successfully!");
	}

	//----------------------------------------------EMPLOYEE INTERN CONSULTANTS------------------------------------------------
	@Scheduled(cron = "0 0 9 * * ?") // Runs every day at 9 AM
	public void sendLastWorkingDayEmailsAndTimesheets() {
		LocalDate today = LocalDate.now();

		// Fetch the single company details
		CompanyDetails company = companyDetailsRepository.findFirstBy();
		if (company != null) {
			int lastWorkingDay = Integer.parseInt(company.getCompanyLastWorkingDay());
			if (today.getDayOfMonth() == lastWorkingDay) {
				try {
					sendTimesheetForAllEmployees(); // Send consolidated timesheet
				} catch (MessagingException e) {
					log.error("Error while sending timesheets: ", e);
				}
			}
		} else {
			log.warn("No company details found.");
		}
	}

	public void sendTimesheetForAllEmployees() throws MessagingException {
		List<EmployeeMaster> employees = employeeRepository.findAll(); // Fetch all employees
		List<Intern> listOfInterns = internRepository.findAll();
		List<ConsultantMaster> listOfConsultantMasters = consultantMasterRepository.findAll();

		// Generate XLS file for all employee timesheets
		File employeesFile = generateXlsFileForAllEmployees(employees);
		File cunsultantFile = generateXlsFileForAllConsultants(listOfConsultantMasters);
		File internFile = generateXlsFileForAllIntern(listOfInterns);

		if (employeesFile != null) {
			// Send email with the attachment
			this.sendEmailWithAttachment(toEmail, "Consolidated Timesheet",
					"Please find the attached consolidated timesheet for all employees.", employeesFile);

			// Attempt to delete the file
			if (!employeesFile.delete()) {
				throw new FileProcessingException("Failed to delete file: " + employeesFile.getName());
			} else {
				log.info("File deleted successfully after sending the email.");
			}
		} else {
			throw new ExcelFileCreationException("Failed to generate file for timesheets.");
		}

		if (cunsultantFile != null) {
			// Send email with the attachment
			this.sendEmailWithAttachment(toEmail, "Consolidated Timesheet",
					"Please find the attached consolidated timesheet for all cunsultants.", cunsultantFile);

			// Attempt to delete the file
			if (!cunsultantFile.delete()) {
				throw new FileProcessingException("Failed to delete file: " + cunsultantFile.getName());
			} else {
				log.info("File deleted successfully after sending the email.");
			}
		} else {
			throw new ExcelFileCreationException("Failed to generate file for timesheets.");
		}

		if (internFile != null) {
			// Send email with the attachment
			this.sendEmailWithAttachment(toEmail, "Consolidated Timesheet",
					"Please find the attached consolidated timesheet for all interns.", internFile);

			// Attempt to delete the file
			if (!internFile.delete()) {
				throw new FileProcessingException("Failed to delete file: " + internFile.getName());
			} else {
				log.info("File deleted successfully after sending the email.");
			}
		} else {
			throw new ExcelFileCreationException("Failed to generate file for timesheets.");
		}
	}

	public File generateXlsFileForAllEmployees(List<EmployeeMaster> employees) {
		String fileName = String.format("Shiavnski_Employees_Timesheet_Internal_%s.xls", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		File file = new File(fileName);

		try (HSSFWorkbook workbook = new HSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(file)) {

			// Create a sheet for each employee and add their timesheet data
			for (EmployeeMaster employee : employees) {
				List<TimesheetEmployee> timesheets = timeSheetEmployeeRepository.findAllByEmployeeId(employee.getId());
				HSSFSheet sheet = workbook.createSheet(employee.getFirstName()); // Create a new sheet for the employee

				// Create header row for each employee's sheet
				Row headerRow = sheet.createRow(0);
				headerRow.createCell(0).setCellValue("Date");
				headerRow.createCell(1).setCellValue("Hours Worked");
				headerRow.createCell(2).setCellValue("Task Completed");
				//	headerRow.createCell(3).setCellValue("Employee ID");

				// Populate each employee's sheet with their timesheet data
				int rowNum = 1;
				for (TimesheetEmployee timesheet : timesheets) {
					Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(timesheet.getDate());
					row.createCell(1).setCellValue(timesheet.getHoursWorked().toString());
					row.createCell(2).setCellValue(timesheet.getTaskCompleted());
					//row.createCell(3).setCellValue(employee.getId()); // Display the employee ID
				}
			}

			workbook.write(fileOut); // Write the workbook to the output stream

		} catch (IOException e) {
			log.error("Error while generating the timesheet file", e);
			return null; // or throw a custom exception
		}
		return file; // Return the File object
	}


	public File generateXlsFileForAllIntern(List<Intern> listOfInterns) {
		String fileName = String.format("Shiavnski_Intern_Timesheet_Internal_%s.xls", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		File file = new File(fileName);

		try (HSSFWorkbook workbook = new HSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(file)) {

			// Create a sheet for each employee and add their timesheet data
			for (Intern intern : listOfInterns) {

				List<TimesheetEmployee> timesheets = timeSheetEmployeeRepository.findAllByEmployeeId(intern.getId());
				HSSFSheet sheet = workbook.createSheet(intern.getInternName()); // Create a new sheet for the employee

				// Create header row for each employee's sheet
				Row headerRow = sheet.createRow(0);
				headerRow.createCell(0).setCellValue("Date");
				headerRow.createCell(1).setCellValue("Hours Worked");
				headerRow.createCell(2).setCellValue("Task Completed");

				int rowNum = 1;
				for (TimesheetEmployee timesheet : timesheets) {
					Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(timesheet.getDate());
					row.createCell(1).setCellValue(timesheet.getHoursWorked().toString());
					row.createCell(2).setCellValue(timesheet.getTaskCompleted());
					//row.createCell(3).setCellValue(employee.getId()); // Display the employee ID
				}
			}

			workbook.write(fileOut); // Write the workbook to the output stream

		} catch (IOException e) {
			log.error("Error while generating the timesheet file", e);
			return null; // or throw a custom exception
		}
		return file; // Return the File object
	}
	public File generateXlsFileForAllConsultants(List<ConsultantMaster> listOfConsultantMasters ) {
		String fileName = String.format("Shiavnski_Intern_Timesheet_Internal_%s.xls", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		File file = new File(fileName);

		try (HSSFWorkbook workbook = new HSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(file)) {

			// Create a sheet for each employee and add their timesheet data
			for (ConsultantMaster consultant : listOfConsultantMasters) {

				List<TimesheetEmployee> timesheets = timeSheetEmployeeRepository.findAllByEmployeeId(consultant.getId());
				HSSFSheet sheet = workbook.createSheet(consultant.getFirstName()); // Create a new sheet for the employee

				// Create header row for each employee's sheet
				Row headerRow = sheet.createRow(0);
				headerRow.createCell(0).setCellValue("Date");
				headerRow.createCell(1).setCellValue("Hours Worked");
				headerRow.createCell(2).setCellValue("Task Completed");

				int rowNum = 1;
				for (TimesheetEmployee timesheet : timesheets) {
					Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(timesheet.getDate());
					row.createCell(1).setCellValue(timesheet.getHoursWorked().toString());
					row.createCell(2).setCellValue(timesheet.getTaskCompleted());
					//row.createCell(3).setCellValue(employee.getId()); // Display the employee ID
				}
			}

			workbook.write(fileOut); // Write the workbook to the output stream

		} catch (IOException e) {
			log.error("Error while generating the timesheet file", e);
			return null; // or throw a custom exception
		}
		return file; // Return the File object
	}

}


