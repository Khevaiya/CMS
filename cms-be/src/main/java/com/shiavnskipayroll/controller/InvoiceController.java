package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.dto.request.InvoiceRequestDto;
import com.shiavnskipayroll.dto.response.InvoiceResponseDto;
import com.shiavnskipayroll.service.invoice.InvoiceService;
import com.shiavnskipayroll.utility.InvoicePdfGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoice")
public class InvoiceController {

	private final InvoiceService invoiceService;

	@PostMapping
	public ResponseEntity<InvoiceResponseDto> createInvoice(@RequestBody InvoiceRequestDto requestDto) {
		log.info("Creating Invoice With Request: {}", requestDto);
		return ResponseEntity.ok().body(invoiceService.createInvoice(requestDto));
	}

	@GetMapping
	public ResponseEntity<List<InvoiceResponseDto>> getAllInvoices() {
		log.info("Getting All the invoices..");
		return ResponseEntity.ok().body(invoiceService.getAllInvoices());
	}


	@GetMapping("/{id}/pdf")
	public ResponseEntity<byte[]> getInvoicePDF(@PathVariable String id) {

		InvoiceResponseDto invoice = invoiceService.getInvoiceById(id);
		log.info("{}", invoice);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		log.info("{}", outputStream);

		InvoicePdfGenerator.generateInvoicePdf(invoice, outputStream);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "invoice_" + invoice.getInvoiceNo() + ".pdf");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	@GetMapping("/{id}")
	public ResponseEntity<InvoiceResponseDto> getInvoiceById(@PathVariable String id) {
		return ResponseEntity.ok().body(invoiceService.getInvoiceById(id));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteInvoiceById(@PathVariable String id) {
		return ResponseEntity.ok().body(invoiceService.deleteInvoice(id));
	}

}
