package com.shiavnskipayroll.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.shiavnskipayroll.dto.request.SalarySlipRequestDTO;
import com.shiavnskipayroll.entity.CompanyDetails;
import com.shiavnskipayroll.exceptions.ResourceNotFoundException;
import com.shiavnskipayroll.repository.CompanyDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PdfGeneratorService {
	@Autowired
	private CompanyDetailsRepository companyDetailsRepository;

	public MultipartFile generateSalarySlipPdf(SalarySlipRequestDTO salarySlip) throws IOException {
		Optional<CompanyDetails> companyDetailsOptional = companyDetailsRepository.findAll().stream().findFirst();
		if (!companyDetailsOptional.isPresent())
			throw new ResourceNotFoundException(
					"CompanyDetails not found in  class PdfGeneratorService ,method generateSalarySlipPdf");
		CompanyDetails companyDetails = companyDetailsOptional.get();
		String filePath = salarySlip.getFirstName() + ".pdf";

		// Initialize PDF writer and document
		PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
		PdfDocument pdfDoc = new PdfDocument(writer);
		Document document = new Document(pdfDoc);

		// Company Name
		Paragraph title = new Paragraph(companyDetails.getCompanyName()).setFontSize(20)
				.setTextAlignment(TextAlignment.CENTER).setBold().setFixedLeading(12);
		document.add(title);

		// Company Email `s
		Paragraph email = new Paragraph(
				"Email:" + companyDetails.getCompanyEmail1() + " | " + companyDetails.getCompanyEmail2())
				.setTextAlignment(TextAlignment.CENTER).setFixedLeading(12);
		document.add(email);

		// company Address
		Paragraph address = new Paragraph("Address:" + companyDetails.getCompanyAddress())
				.setTextAlignment(TextAlignment.JUSTIFIED).setMarginLeft(50).setMarginRight(50).setFixedLeading(12);

		document.add(address);

		Table table = new Table(new float[] { 1, 1, 1, 1 }); // Adjust column widths as needed
		table.setWidth(UnitValue.createPercentValue(100));

		table.addCell(new Cell(1, 3).add(new Paragraph("Salary Slip")).setBackgroundColor(ColorConstants.LIGHT_GRAY)
				.setTextAlignment(TextAlignment.CENTER));

		// Create a new table for Month and MonthDate
		Table monthTable = new Table(UnitValue.createPercentArray(new float[] { 1, 1 }));
		monthTable.setWidth(UnitValue.createPercentValue(100));

		monthTable.addCell(new Cell().add(new Paragraph("Month")).setTextAlignment(TextAlignment.CENTER)
				.setBackgroundColor(ColorConstants.LIGHT_GRAY));
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		String formattedDate = today.format(formatter);
		monthTable.addCell(new Cell().add(new Paragraph(formattedDate)).setTextAlignment(TextAlignment.CENTER)
				.setBackgroundColor(ColorConstants.LIGHT_GRAY));

		// Add the monthTable as a single cell to the main table
		table.addCell(new Cell(1, 1).add(monthTable)); // Month and MonthDate in one cell

		table.addCell(new Cell().add(new Paragraph("Employee Name")));
		table.addCell(new Cell().add(new Paragraph(salarySlip.getFirstName())));
		table.addCell(new Cell().add(new Paragraph("Date of Joining")));
		table.addCell(new Cell().add(new Paragraph(salarySlip.getDateofjoining())));

		table.addCell(new Cell().add(new Paragraph(" Employee Code")));
		table.addCell(new Cell().add(new Paragraph(salarySlip.getEmployeeCode())));
		table.addCell(new Cell().add(new Paragraph("Total Working Days")));
		table.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getTotalWorkingDays()))));

		table.addCell(new Cell().add(new Paragraph("Designation")));
		table.addCell(new Cell().add(new Paragraph(salarySlip.getDesignation())));

		table.addCell(new Cell().add(new Paragraph("Number of Working Days Attended")));
		table.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getWorkingDaysAttended()))));

		table.addCell(new Cell().add(new Paragraph("PAN")));
		table.addCell(new Cell().add(new Paragraph(salarySlip.getPan())));

		Table leavesTable = new Table(UnitValue.createPercentArray(new float[] { 1, 1, 1 }));
		leavesTable.setWidth(UnitValue.createPercentValue(100));

		// Add header row for "Leaves", "P", and "S"
		leavesTable.addCell(new Cell().add(new Paragraph("Leaves")));
		leavesTable.addCell(new Cell().add(new Paragraph("P").setTextAlignment(TextAlignment.CENTER)));
		leavesTable.addCell(new Cell().add(new Paragraph("S").setTextAlignment(TextAlignment.CENTER)));

		table.addCell(new Cell(1, 2).add(leavesTable));

		table.addCell(new Cell().add(new Paragraph("Bank Acc. N.")));
		table.addCell(new Cell().add(new Paragraph(salarySlip.getBankAccountNumber())));
		Table leavesTable1 = new Table(UnitValue.createPercentArray(new float[] { 1, 1, 1 }));
		leavesTable1.setWidth(UnitValue.createPercentValue(100));

		leavesTable1.addCell(new Cell().add(new Paragraph("Leaves taken")));
		leavesTable1.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getLeavesTakenP())))); // Value for
																											// "P"
																											// column
		leavesTable1.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getLeavesTakenS())))); // Value for
																											// "S"
																											// column

		table.addCell(new Cell(1, 2).add(leavesTable1));

		table.addCell(new Cell().add(new Paragraph("Bank Name")));
		table.addCell(new Cell().add(new Paragraph(salarySlip.getBankName())));

		Table leavesTable2 = new Table(UnitValue.createPercentArray(new float[] { 1, 1, 1 }));
		leavesTable2.setWidth(UnitValue.createPercentValue(100));

		leavesTable2.addCell(new Cell().add(new Paragraph("Balance Leaves")));
		leavesTable2.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getBalanceLeavesP())))); // Value
																												// for
																												// "P"
		leavesTable2.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getBalanceLeavesS())))); // Value
																												// for
																												// "S"

		table.addCell(new Cell(1, 2).add(leavesTable2));

		document.add(table);

		// Income and deductions table
		Table incomeTable = new Table(UnitValue.createPercentArray(new float[] { 1, 1, 1, 1 }));

		incomeTable.setWidth(UnitValue.createPercentValue(100));

		incomeTable.addCell(new Cell(1, 2).add(new Paragraph("Income")).setTextAlignment(TextAlignment.CENTER)
				.setBackgroundColor(ColorConstants.LIGHT_GRAY));
		incomeTable.addCell(new Cell(1, 2).add(new Paragraph("Deductions")).setTextAlignment(TextAlignment.CENTER)
				.setBackgroundColor(ColorConstants.LIGHT_GRAY));

		incomeTable.addCell(
				new Cell(1, 1).add(new Paragraph("Particulars")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
		incomeTable.addCell(new Cell(1, 1).add(new Paragraph("Amount")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
		incomeTable.addCell(
				new Cell(1, 1).add(new Paragraph("Particulars")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
		incomeTable.addCell(new Cell(1, 1).add(new Paragraph("Amount")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

		// Income section
		incomeTable.addCell(new Cell().add(new Paragraph("Basic Salary")));
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getBasicSalary()))));
		incomeTable.addCell(new Cell().add(new Paragraph("PF")));
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getPf()))));

		incomeTable.addCell(new Cell().add(new Paragraph("Dearness Allowance")));
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getDearnessAllowance()))));
		incomeTable.addCell(new Cell().add(new Paragraph("Professional Tax")));
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getProfessionalTax()))));

		incomeTable.addCell(new Cell().add(new Paragraph("HRA")));
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getHra()))));
		incomeTable.addCell(new Cell().add(new Paragraph("TDS")));
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getTds()))));

		incomeTable.addCell(new Cell().add(new Paragraph("Conveyance Allowance")));
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getConveyanceAllowance()))));
		incomeTable.addCell(new Cell().add(new Paragraph("Other Deductions")));
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getOtherDeductions()))));

		incomeTable.addCell(new Cell().add(new Paragraph("Medical Allowances")));
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getMedicalAllowance()))));

		// space
		incomeTable.addCell(new Cell().add(new Paragraph("")).setBold());
		incomeTable.addCell(new Cell().add(new Paragraph("")));
		// space end

		incomeTable.addCell(new Cell().add(new Paragraph("Special Allowances")));
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getSpecialAllowance()))));

		// space
		incomeTable.addCell(new Cell().add(new Paragraph("")));
		incomeTable.addCell(new Cell().add(new Paragraph("")));
		// space end

		incomeTable.addCell(new Cell().add(new Paragraph("Other Receipts")));
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getOtherReceipts()))));

		// space
		incomeTable.addCell(new Cell().add(new Paragraph("")));
		incomeTable.addCell(new Cell().add(new Paragraph("")));
		// space end

		incomeTable.addCell(new Cell().add(new Paragraph("Total Income")).setBold());
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getTotalIncome()))));
		incomeTable.addCell(new Cell().add(new Paragraph("Total Deductions")).setBold());
		incomeTable.addCell(new Cell().add(new Paragraph(String.valueOf(salarySlip.getTotalDeductions()))));

		document.add(incomeTable);
		BigDecimal netSalaryInBigDecimal = salarySlip.getTotalIncome().subtract(salarySlip.getTotalDeductions());

		// Add net salary
		Paragraph netSalary = new Paragraph("Net Salary:" + netSalaryInBigDecimal).setFontSize(12).setBold()
				.setTextAlignment(TextAlignment.RIGHT);
		document.add(netSalary);

		// Add signature lines
		Paragraph signatures = new Paragraph("\nEmployee Signature: ___________       Employer Signature: ___________")
				.setFontSize(10).setTextAlignment(TextAlignment.CENTER);
		document.add(signatures);

		// Close the document
		document.close();
		System.out.println("PDF generated successfully and saved at: " + filePath);

		File file = new File(filePath);
		byte[] pdfBytes = Files.readAllBytes(file.toPath());

		// Create a MultipartFile from the byte array
		MultipartFile multipartFile = new MockMultipartFile(file.getName(), // Original filename
				file.getName(), // Content-type inferred (can be "application/pdf" explicitly)
				"application/pdf", pdfBytes);

		
		file.delete();

		return multipartFile;

	}

}