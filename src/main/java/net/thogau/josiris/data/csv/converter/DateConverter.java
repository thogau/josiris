package net.thogau.josiris.data.csv.converter;

import java.text.SimpleDateFormat;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class DateConverter<T, I> extends AbstractBeanField<T, I> {

	@Override
	protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(value);
		} catch (Exception e) {
			return null;
		}
	}

}
