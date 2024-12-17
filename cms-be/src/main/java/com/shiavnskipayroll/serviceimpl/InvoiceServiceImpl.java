package com.shiavnskipayroll.serviceimpl;

import com.shiavnskipayroll.dto.request.InvoiceRequestDto;
import com.shiavnskipayroll.dto.response.InvoiceResponseDto;
import com.shiavnskipayroll.entity.ClientMaster;
import com.shiavnskipayroll.entity.invoice.Invoice;
import com.shiavnskipayroll.exceptions.ClientNotFoundException;
import com.shiavnskipayroll.exceptions.InvoiceNotFound;
import com.shiavnskipayroll.mapper.InvoiceMapper;
import com.shiavnskipayroll.repository.ClientRepository;
import com.shiavnskipayroll.repository.invoice.InvoiceRepository;
import com.shiavnskipayroll.service.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

	private final ClientRepository clientRepository;
	private final InvoiceRepository invoiceRepository;

	@Override
	public InvoiceResponseDto createInvoice(InvoiceRequestDto requestDto) {
		log.info("Creating Invoice With Request: {}", requestDto);

		Invoice invoice = InvoiceMapper.toEntity(requestDto);

		ClientMaster buyer = clientRepository.findById(requestDto.getClientId()).orElseThrow(
				() -> new ClientNotFoundException("Client Not Found With this id: " + requestDto.getClientId()));
		invoice.setClient(buyer);

		String id = invoiceRepository.save(invoice).getId();
		log.info("Invoice Created successfully with id: {}", id);

		return InvoiceMapper.toRDto(invoice);
	}

	@Override
	public InvoiceResponseDto getInvoiceById(String id) {
		Invoice invoice = invoiceRepository.findById(id).orElseThrow(NotFoundException::new);
		return InvoiceMapper.toRDto(invoice);
	}

	@Override
	public String deleteInvoice(String id) {
		log.info("Deleting Invice With Id: {}", id);

		if (!invoiceRepository.existsById(id)) {
			throw new InvoiceNotFound("Invoice Not Found With Id: " + id);
		}
		invoiceRepository.deleteById(id);
		return "Invoice Deleted Successfully..";
	}

	@Override
	public List<InvoiceResponseDto> getAllInvoices() {
		log.info("Getting All the invoices..");
		List<Invoice> listOfInvoices = invoiceRepository.findAll();
		return listOfInvoices.stream().map(InvoiceMapper::toRDto).toList();
	}
}
