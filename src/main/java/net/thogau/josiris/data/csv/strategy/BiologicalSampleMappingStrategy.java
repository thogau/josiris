package net.thogau.josiris.data.csv.strategy;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.BeanField;
import com.opencsv.bean.BeanFieldJoinStringIndex;
import com.opencsv.bean.BeanFieldSingleValue;
import com.opencsv.bean.BeanFieldSplit;
import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindAndJoinByNames;
import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindAndSplitByNames;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvConverter;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvCustomBindByNames;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import net.thogau.josiris.data.entity.BiologicalSample;

public class BiologicalSampleMappingStrategy extends HeaderColumnNameMappingStrategy<BiologicalSample> {

	private final AutowireCapableBeanFactory beanFactory;

	public BiologicalSampleMappingStrategy(AutowireCapableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	protected void loadAnnotatedFieldMap(ListValuedMap<Class<?>, Field> fields) {
		for (Map.Entry<Class<?>, Field> classField : fields.entries()) {
			Class<?> localType = classField.getKey();
			Field localField = classField.getValue();

			// Always check for a custom converter first.
			if (localField.isAnnotationPresent(CsvCustomBindByName.class)
					|| localField.isAnnotationPresent(CsvCustomBindByNames.class)) {
				CsvCustomBindByName annotation = selectAnnotationForProfile(
						localField.getAnnotationsByType(CsvCustomBindByName.class), CsvCustomBindByName::profiles);
				if (annotation != null) {
					registerCustomBinding(annotation, localType, localField);
				}
			}

			// Then check for a collection
			else if (localField.isAnnotationPresent(CsvBindAndSplitByName.class)
					|| localField.isAnnotationPresent(CsvBindAndSplitByNames.class)) {
				CsvBindAndSplitByName annotation = selectAnnotationForProfile(
						localField.getAnnotationsByType(CsvBindAndSplitByName.class), CsvBindAndSplitByName::profiles);
				if (annotation != null) {
					registerSplitBinding(annotation, localType, localField);
				}
			}

			// Then for a multi-column annotation
			else if (localField.isAnnotationPresent(CsvBindAndJoinByName.class)
					|| localField.isAnnotationPresent(CsvBindAndJoinByNames.class)) {
				CsvBindAndJoinByName annotation = selectAnnotationForProfile(
						localField.getAnnotationsByType(CsvBindAndJoinByName.class), CsvBindAndJoinByName::profiles);
				if (annotation != null) {
					registerJoinBinding(annotation, localType, localField);
				}
			}

			// Otherwise it must be CsvBindByName.
			else {
				CsvBindByName annotation = selectAnnotationForProfile(
						localField.getAnnotationsByType(CsvBindByName.class), CsvBindByName::profiles);
				if (annotation != null) {
					registerBinding(annotation, localType, localField);
				}
			}
		}
	}

	private void registerBinding(CsvBindByName annotation, Class<?> localType, Field localField) {
		String columnName = annotation.column().toUpperCase().trim();
		String locale = annotation.locale();
		String writeLocale = annotation.writeLocaleEqualsReadLocale() ? locale : annotation.writeLocale();
		CsvConverter converter = determineConverter(localField, localField.getType(), locale, writeLocale, null);

		if (StringUtils.isEmpty(columnName)) {
			fieldMap.put(localField.getName().toUpperCase(), new BeanFieldSingleValue<>(localType, localField,
					annotation.required(), errorLocale, converter, annotation.capture(), annotation.format()));
		} else {
			fieldMap.put(columnName, new BeanFieldSingleValue<>(localType, localField, annotation.required(),
					errorLocale, converter, annotation.capture(), annotation.format()));
		}
	}

	private void registerJoinBinding(CsvBindAndJoinByName annotation, Class<?> localType, Field localField) {
		String columnRegex = annotation.column();
		String locale = annotation.locale();
		String writeLocale = annotation.writeLocaleEqualsReadLocale() ? locale : annotation.writeLocale();

		CsvConverter converter = determineConverter(localField, annotation.elementType(), locale, writeLocale,
				annotation.converter());
		if (StringUtils.isEmpty(columnRegex)) {
			fieldMap.putComplex(localField.getName(),
					new BeanFieldJoinStringIndex<>(localType, localField, annotation.required(), errorLocale, converter,
							annotation.mapType(), annotation.capture(), annotation.format()));
		} else {
			fieldMap.putComplex(columnRegex,
					new BeanFieldJoinStringIndex<>(localType, localField, annotation.required(), errorLocale, converter,
							annotation.mapType(), annotation.capture(), annotation.format()));
		}
	}

	private void registerSplitBinding(CsvBindAndSplitByName annotation, Class<?> localType, Field localField) {
		String columnName = annotation.column().toUpperCase().trim();
		String locale = annotation.locale();
		String writeLocale = annotation.writeLocaleEqualsReadLocale() ? locale : annotation.writeLocale();
		Class<?> elementType = annotation.elementType();

		CsvConverter converter = determineConverter(localField, elementType, locale, writeLocale,
				annotation.converter());
		if (StringUtils.isEmpty(columnName)) {
			fieldMap.put(localField.getName().toUpperCase(),
					new BeanFieldSplit<>(localType, localField, annotation.required(), errorLocale, converter,
							annotation.splitOn(), annotation.writeDelimiter(), annotation.collectionType(), elementType,
							annotation.capture(), annotation.format()));
		} else {
			fieldMap.put(columnName,
					new BeanFieldSplit<>(localType, localField, annotation.required(), errorLocale, converter,
							annotation.splitOn(), annotation.writeDelimiter(), annotation.collectionType(), elementType,
							annotation.capture(), annotation.format()));
		}
	}

	private void registerCustomBinding(CsvCustomBindByName annotation, Class<?> localType, Field localField) {
		String columnName = annotation.column().toUpperCase().trim();
		if (StringUtils.isEmpty(columnName)) {
			columnName = localField.getName().toUpperCase();
		}
		@SuppressWarnings("unchecked")
		Class<? extends AbstractBeanField<BiologicalSample, String>> converter = (Class<? extends AbstractBeanField<BiologicalSample, String>>) annotation
				.converter();
		BeanField<BiologicalSample, String> bean = instantiateCustomConverter(converter);
		this.beanFactory.autowireBean(bean);
		bean.setType(localType);
		bean.setField(localField);
		bean.setRequired(annotation.required());
		fieldMap.put(columnName, bean);
	}

}
