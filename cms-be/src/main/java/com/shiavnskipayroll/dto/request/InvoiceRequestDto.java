package com.shiavnskipayroll.dto.request;

import com.shiavnskipayroll.entity.invoice.InvoiceItem;
import lombok.Data;

import java.util.List;

@Data
public class InvoiceRequestDto {

	private String invoiceDate;
	private String supplierRef;
	private String resourcesName;
	private String modeOfPayment;
	private String invoiceNo;
	private String serviceMonth;
	private String country;
	private String underTakingYear;
	private String underTakingDate;
	private Long totalAmount;

	private List<InvoiceItem> items;
	private String clientId;
}
