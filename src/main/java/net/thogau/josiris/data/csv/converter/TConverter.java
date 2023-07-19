package net.thogau.josiris.data.csv.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import net.thogau.josiris.data.service.ConceptualDomainService;

@Component
public class TConverter<T, I> extends AbstractBeanField<T, I> {

	@Autowired
	private ConceptualDomainService service;

	@Override
	protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		if (value.startsWith("T2")) {
			return service.getValue(net.thogau.josiris.data.entity.conceptualDomain.T.class, "T2");
		} else {
			Object o = service.getValue(net.thogau.josiris.data.entity.conceptualDomain.T.class, value);
			return o != null ? o
					: service.getValue(net.thogau.josiris.data.entity.conceptualDomain.T.class, "UMLS:C0439673");
		}
	}

}
