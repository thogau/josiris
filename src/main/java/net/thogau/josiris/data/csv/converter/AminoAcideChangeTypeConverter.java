package net.thogau.josiris.data.csv.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import net.thogau.josiris.data.entity.conceptualDomain.AminoAcidChangeType;
import net.thogau.josiris.data.service.ConceptualDomainService;

@Component
public class AminoAcideChangeTypeConverter<T, I> extends AbstractBeanField<T, I> {

	@Autowired
	private ConceptualDomainService service;

	@Override
	protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		Object o = service.getValue(AminoAcidChangeType.class, value);
		return o != null ? o : service.getValue(AminoAcidChangeType.class, "UMLS:C0439673");
	}

}
