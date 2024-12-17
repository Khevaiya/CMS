package com.shiavnskipayroll.utility;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.shiavnskipayroll.dto.response.InvoiceResponseDto;
import com.shiavnskipayroll.entity.invoice.InvoiceItem;
import com.shiavnskipayroll.exceptions.CustomException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
@UtilityClass
// PDF Generate utility Class
public class InvoicePdfGenerator {
	Logger logger = Logger.getLogger(InvoicePdfGenerator.class.getName());

	public static void generateInvoicePdf(InvoiceResponseDto invoice, ByteArrayOutputStream outputStream) {
		logger.info("Invoive dnlaod "+invoice);
		try (PdfWriter writer = new PdfWriter(outputStream);
				PdfDocument pdf = new PdfDocument(writer);
				Document document = new Document(pdf, PageSize.A4)) {
			logger.info("1");
			document.add(new Paragraph("Tax Invoice").setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(14));
			document.add(new Paragraph(
					"(SUPPLY MEANT FOR EXPORT UNDER LETTER OF UNDERTAKING DTD " + invoice.getUnderTakingDate()
							+ " FOR FY " + invoice.getUnderTakingYear() + " WITHOUT PAYMENT OF IGST)")
					.setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(8));

			Table detailsTable = new Table(new float[] { 1, 227 });
			detailsTable.setWidth(UnitValue.createPercentValue(100));

			detailsTable.addCell(createCell(
					"SHIAVNSKI TECHNOLOGIES LLP\n80,Tilakpath,Ist Floor Sakun Shankar, Rambagh,\nIndore-(MP)-452007 \nGSTIN/UIN: 23ADYFS3937M1Z7\nState Name: Madhya Pradesh, Code: 23",
					TextAlignment.LEFT, true));
			detailsTable.addCell(createCell(
					invoice.getClient().getClientName() + "\n " + invoice.getClient().getClientAddress() + "\n"
							+ invoice.getClient().getCity() + ", " + invoice.getClient().getState(),
					TextAlignment.LEFT, true));

			document.add(detailsTable);  logger.info("2");
			Table invoiceDetailsTable = new Table(new float[] { 295, 1, 1 });
			invoiceDetailsTable.setWidth(UnitValue.createPercentValue(100)).setMarginTop(5);

			invoiceDetailsTable.addCell(
					new Cell().add(new Paragraph().add("Invoice No: ").add(new Text(invoice.getInvoiceNo()).setBold()))
							.setTextAlignment(TextAlignment.LEFT));

			invoiceDetailsTable
					.addCell(createCell("Mode of Payment:\n" + invoice.getModeOfPayment(), TextAlignment.LEFT, false));
			invoiceDetailsTable
					.addCell(createCell("Resources Name: \n" + invoice.getResourcesName(), TextAlignment.LEFT, false));

			invoiceDetailsTable.addCell(new Cell()
					.add(new Paragraph().add("Invoice Date: ").add(new Text(invoice.getInvoiceDate()).setBold()))
					.setTextAlignment(TextAlignment.LEFT));

			invoiceDetailsTable.addCell(createCell("Country:\n " + invoice.getCountry(), TextAlignment.LEFT, false));
			invoiceDetailsTable
					.addCell(createCell("Service Month: \n" + invoice.getServiceMonth(), TextAlignment.LEFT, false));

			document.add(invoiceDetailsTable);

			Table itemTable = new Table(new float[] { 1, 103, 2, 1, 1, 1, 1 });
			itemTable.setWidth(UnitValue.createPercentValue(100)).setMarginTop(2).setFontSize(11)
					.setTextAlignment(TextAlignment.CENTER);

			itemTable.addHeaderCell("SI No.");
			itemTable.addHeaderCell("Particulars");
			itemTable.addHeaderCell("HSN/SAC");
			itemTable.addHeaderCell("Project Cost");
			itemTable.addHeaderCell("Total\nWorking Days");
			itemTable.addHeaderCell("Present\nWorking Hrs");
			itemTable.addHeaderCell("Amount");

			// Adding invoice items
			List<InvoiceItem> items = invoice.getItems();
			for (int i = 0; i < items.size(); i++) {
				InvoiceItem item = items.get(i);
				itemTable.addCell(String.valueOf(i + 1));
				itemTable.addCell(item.getParticulars());
				itemTable.addCell(item.getHsnSac());
				itemTable.addCell("" + item.getProjectCost());
				itemTable.addCell("" + item.getTotalWorkingDays());
				itemTable.addCell("" + item.getPresentWorkingDays());
				itemTable.addCell("" + item.getAmount());
			}
			logger.info("3");
			document.add(itemTable);

			Table gstTable = new Table(new float[] { 1, 174.5f });
			gstTable.setWidth(UnitValue.createPercentValue(100));
			gstTable.setTextAlignment(TextAlignment.LEFT);

			if (invoice.getClient().getState().toLowerCase().replace(" ", "").equals("madhyapradesh")) {

				logger.info("A");
				gstTable.addCell(new Cell().add(new Paragraph(" CGST (9%)").setTextAlignment(TextAlignment.LEFT)));
				gstTable.addCell(new Cell().add(new Paragraph("Temp").setTextAlignment(TextAlignment.CENTER)));
				gstTable.addCell(new Cell().add(new Paragraph(" SGST (9%)").setTextAlignment(TextAlignment.LEFT)));
				gstTable.addCell(new Cell().add(new Paragraph("Temp").setTextAlignment(TextAlignment.CENTER)));
			} else {
				logger.info("AAAA");
				gstTable.addCell(new Cell().add(new Paragraph(" IGST (18%)").setTextAlignment(TextAlignment.LEFT)));
				gstTable.addCell(new Cell()
						.add(new Paragraph("" + invoice.getTaxAmount()).setTextAlignment(TextAlignment.CENTER)));
			}
			logger.info("333");
			document.add(gstTable);
			logger.info("4");
			Table totalAmount = new Table(new float[] { 1, 1 });
			totalAmount.setWidth(UnitValue.createPercentValue(100));
			totalAmount.addCell(new Cell().add(new Paragraph("Total Amount").setTextAlignment(TextAlignment.LEFT)));
			totalAmount.addCell(new Cell().add(new Paragraph("" + (invoice.getTotalAmount() - invoice.getTaxAmount()))
					.setTextAlignment(TextAlignment.CENTER)));
			document.add(totalAmount);

			Table amountInWord = new Table(new float[] { 1 });
			amountInWord.setWidth(UnitValue.createPercentValue(100));
			amountInWord.addCell(new Cell()
					.add(new Paragraph("Amount in Words: \t" + NumberToWordsConverter.convert(invoice.getTotalAmount()))
							.setTextAlignment(TextAlignment.LEFT)));

			document.add(amountInWord);

			Table bankDetails = new Table(new float[] { 1, 200 });
			bankDetails.setWidth(UnitValue.createPercentValue(100));
			bankDetails.addCell(
					"Company's PAN: ADYSF3937M\nBank Details:\nA/C Holder Name: SHIAVNSKI TECHNOLOGIES LLP\nBank Name: ICICI Bank\nA/C Number: 237105009940\nBranch & IFSC: VAISHALI NAGAR & ICIC0002371")
					.setMarginTop(5);
			bankDetails.addCell((new Cell().add(
					new Paragraph("Authorized Signatory").setMarginTop(10).setTextAlignment(TextAlignment.CENTER))));
			document.add(bankDetails);

			document.add(new Paragraph("This is a Computer Generated Invoice").setTextAlignment(TextAlignment.CENTER));
			document.add(new Paragraph(""));
			document.add(new Paragraph("|| Thanks For Business ||").setTextAlignment(TextAlignment.CENTER)
					.setFontColor(new DeviceRgb(135, 206, 235)));

			document.close();
			logger.info("5");
		} catch (Exception e) {
			log.error("Exception during PDF generation: {}", e.getMessage());
			throw new CustomException("Exception: " + e.getMessage());
		}
	}

	private static Cell createCell(String content, TextAlignment alignment, boolean isBold) {
		Paragraph paragraph = new Paragraph(content).setTextAlignment(alignment).setPadding(5);
		if (isBold) {
			paragraph.setBold();
		}
		return new Cell().add(paragraph);
	}
}
