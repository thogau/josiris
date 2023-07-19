package net.thogau.josiris.data.csv.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import net.thogau.josiris.data.entity.conceptualDomain.StorageTemperature;
import net.thogau.josiris.data.service.ConceptualDomainService;

@Component
public class StorageTemperatureConverter<T, I> extends AbstractBeanField<T, I> {

	@Autowired
	private ConceptualDomainService service;

	@Override
	protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		Object o = service.getValue(StorageTemperature.class, value);
		return o != null ? o : service.getValue(StorageTemperature.class, "UMLS:C0439673");
	}

}
