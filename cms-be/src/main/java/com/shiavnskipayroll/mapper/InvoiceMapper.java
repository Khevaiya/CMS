package com.shiavnskipayroll.mapper;

import com.shiavnskipayroll.dto.request.InvoiceRequestDto;
import com.shiavnskipayroll.dto.response.InvoiceResponseDto;
import com.shiavnskipayroll.entity.invoice.Invoice;
import com.shiavnskipayroll.exceptions.RequestDtoNull;
import com.shiavnskipayroll.utility.NumberToWordsConverter;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
// utility class for object creation
public class InvoiceMapper {

	private static final AtomicInteger counter = new AtomicInteger(1);

	public static Invoice toEntity(InvoiceRequestDto requestDto) {

		if (requestDto == null)
			throw new RequestDtoNull("Invoice Request Dto can not be null...");

		Invoice invoice = new Invoice();

		Long totalAmount = requestDto.getTotalAmount();
		Long taxAmount = totalAmount * 18 / 100;
		invoice.setAmountInWords(NumberToWordsConverter.convert(totalAmount + taxAmount));
		invoice.setInvoiceDate(requestDto.getInvoiceDate());
		invoice.setInvoiceNo(generateInvoiceId());
		invoice.setItems(requestDto.getItems());
		invoice.setModeOfPayment(requestDto.getModeOfPayment());
		invoice.setOutputCgst(9);
		invoice.setOutputIgst(18);
		invoice.setOutputSgst(9);
		invoice.setUnderTakingDate(requestDto.getUnderTakingDate());
		invoice.setUnderTakingYear(requestDto.getUnderTakingYear());
		invoice.setResourcesName(requestDto.getResourcesName());
		invoice.setCountry(requestDto.getCountry());
		invoice.setServiceMonth(requestDto.getServiceMonth());
		invoice.setTotalAmount(totalAmount + taxAmount);
		invoice.setTaxAmount(taxAmount);

		return invoice;
	}

	public static InvoiceResponseDto toRDto(Invoice requestDto) {

		if (requestDto == null)
			throw new RequestDtoNull("Invoice Request Dto can not be null...");

		InvoiceResponseDto invoice = new InvoiceResponseDto();

		invoice.setId(requestDto.getId());
		invoice.setAmountInWords(requestDto.getAmountInWords());
		invoice.setClient(requestDto.getClient());
		invoice.setTaxAmount(requestDto.getTaxAmount());
		invoice.setModeOfPayment(requestDto.getModeOfPayment());
		invoice.setServiceMonth(requestDto.getServiceMonth());
		invoice.setCountry(requestDto.getCountry());
		invoice.setUnderTakingDate(requestDto.getUnderTakingDate());
		invoice.setUnderTakingYear(requestDto.getUnderTakingYear());
		invoice.setInvoiceDate(requestDto.getInvoiceDate());
		invoice.setInvoiceNo(requestDto.getInvoiceNo());
		invoice.setItems(requestDto.getItems());
		invoice.setTotalAmount(requestDto.getTotalAmount());
		invoice.setOutputCgst(requestDto.getOutputCgst());
		invoice.setOutputIgst(requestDto.getOutputIgst());
		invoice.setOutputSgst(requestDto.getOutputSgst());
		invoice.setResourcesName(requestDto.getResourcesName());

		return invoice;
	}

	public static String generateInvoiceId() {
		String prefix = "ST";
		String separator = "/";
		LocalDate today = LocalDate.now();
		int currentYear = today.getYear() % 100;
		int nextYear = (today.getYear() + 1) % 100;
		String fiscalYear = String.format("%02d-%02d", currentYear, nextYear);
		String uniqueNumber = String.format("%03d", counter.getAndIncrement());

		return prefix + separator + fiscalYear + separator + uniqueNumber;
	}

}
