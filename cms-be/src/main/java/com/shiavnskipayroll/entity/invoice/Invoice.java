package com.shiavnskipayroll.entity.invoice;

import com.shiavnskipayroll.entity.ClientMaster;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Invoice {

	@Id
	private String id;

	@NotBlank(message = "Invoice number cannot be blank")
	private String invoiceNo;

	@NotBlank(message = "Service month cannot be blank")
	private String serviceMonth;

	@NotBlank(message = "Invoice date cannot be blank")
	private String invoiceDate;

	@NotBlank(message = "Country cannot be blank")
	private String country;

	@NotBlank(message = "Resource name cannot be blank")
	private String resourcesName;

	@NotBlank(message = "Mode of payment cannot be blank")
	private String modeOfPayment;

	@NotBlank(message = "Undertaking year cannot be blank")
	private String underTakingYear;

	@NotBlank(message = "Undertaking date cannot be blank")
	private String underTakingDate;

	@NotNull(message = "Client information is required")
	private ClientMaster client;

	@NotEmpty(message = "Invoice items list cannot be empty")
	private List<InvoiceItem> items;

	@NotNull(message = "Total amount cannot be null")
	private Long totalAmount;

	@NotBlank(message = "Amount in words cannot be blank")
	private String amountInWords;

	@NotNull(message = "CGST amount cannot be null")
	private double outputCgst;

	@NotNull(message = "SGST amount cannot be null")
	private double outputSgst;

	@NotNull(message = "IGST amount cannot be null")
	private double outputIgst;

	@NotNull(message = "Tax amount cannot be null")
	private Long taxAmount;

}
