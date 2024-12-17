package com.shiavnskipayroll.service.invoice;

import com.shiavnskipayroll.dto.request.InvoiceRequestDto;
import com.shiavnskipayroll.dto.response.InvoiceResponseDto;

import java.util.List;

public interface InvoiceService {
	InvoiceResponseDto createInvoice(InvoiceRequestDto requestDto);

	InvoiceResponseDto getInvoiceById(String id);

	String deleteInvoice(String id);

	List<InvoiceResponseDto> getAllInvoices();
}
