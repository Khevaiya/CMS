package com.shiavnskipayroll.repository.invoice;

import com.shiavnskipayroll.entity.invoice.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
	boolean existsById(String id);
}
