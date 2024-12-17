package com.shiavnskipayroll.entity.invoice;

import lombok.Data;

@Data
public class InvoiceItem {
	private String particulars;
	private String hsnSac;
	private double projectCost;

	private int totalWorkingDays;
	private int presentWorkingDays;
	private double amount;
}
