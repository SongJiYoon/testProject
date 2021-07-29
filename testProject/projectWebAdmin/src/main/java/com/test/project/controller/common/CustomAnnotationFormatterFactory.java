package com.test.project.controller.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

@Component
public class CustomAnnotationFormatterFactory implements AnnotationFormatterFactory<DateTimeFormat> {

	@Override
	public Set<Class<?>> getFieldTypes() {
		return new HashSet<Class<?>>(Arrays.asList(new Class<?>[] { Date.class }));
	}

	@Override
	public Printer<?> getPrinter(final DateTimeFormat dateTimeFormat, Class<?> fieldType) {

		return new Printer<Date>() {
			@Override
			public String print(Date date, Locale locale) {
				if(dateTimeFormat != null
						&& date != null){
					SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat.pattern());
					return sdf.format(date);
				}
				return date.toString();
			}
		};
	}

	@Override
	public Parser<?> getParser(final DateTimeFormat dateTimeFormat, Class<?> fieldType) {
		return new Parser<Date>(){
			@Override
			public Date parse(String text, Locale locale) throws ParseException {
				if(dateTimeFormat != null
						&& text != null){
					SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat.pattern());
					return sdf.parse(text);
				}

				return null;
			}
		};
	}

}
