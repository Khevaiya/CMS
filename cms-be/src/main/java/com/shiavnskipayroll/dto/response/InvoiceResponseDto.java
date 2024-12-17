package com.shiavnskipayroll.dto.response;

import com.shiavnskipayroll.entity.ClientMaster;
import com.shiavnskipayroll.entity.invoice.InvoiceItem;
import lombok.Data;

import java.util.List;

@Data
public class InvoiceResponseDto {
	private String id;

	private String invoiceNo;
	private String invoiceDate;
	private String resourcesName;
	private String modeOfPayment;
	private String serviceMonth;
	private String country;
	private String underTakingYear;
	private String underTakingDate;
	
	private ClientMaster client;

	private List<InvoiceItem> items;
	private Long totalAmount;
	private Long taxAmount;
	private String amountInWords;
	private double outputCgst;
	private double outputSgst;
	private double outputIgst;
}
