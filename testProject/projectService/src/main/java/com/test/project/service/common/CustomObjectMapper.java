package com.test.project.service.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

public class CustomObjectMapper extends ObjectMapper {
	
	private static final long serialVersionUID = 9209889616317395767L;

	public CustomObjectMapper() {
		super();

		configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		setSerializationInclusion(Include.NON_NULL);

		configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		setDateFormat(new SimpleDateFormat(CommonConstants.DEFAULT_DATEFORMAT));

		SimpleModule dateModule = new SimpleModule();
		dateModule.addDeserializer(Date.class, new DateJsonDeserializer());
		dateModule.addSerializer(Date.class, new DateJsonSerializer());
		registerModule(dateModule);
	}
	
	class DateJsonDeserializer extends DateDeserializer {
		private static final long serialVersionUID = -7569756001991580560L;

		@Override
		public JsonDeserializer<?> createContextual(
				DeserializationContext ctxt, BeanProperty property)
				throws JsonMappingException {
			DateTimeFormat dateTimeFormat = property.getAnnotation(DateTimeFormat.class);
			if(dateTimeFormat != null){
				String formatString = dateTimeFormat.pattern();
	            return new DateDeserializer(this, new SimpleDateFormat(formatString), formatString);
			}

			return super.createContextual(ctxt, property);
		}
	}
	
	class DateJsonSerializer extends DateSerializer {
		private static final long serialVersionUID = 4135154841571069474L;

	    @Override
	    public JsonSerializer<?> createContextual(SerializerProvider serializers,
	            BeanProperty property) throws JsonMappingException	    {

	    	DateTimeFormat dateTimeFormat = property.getAnnotation(DateTimeFormat.class);
	    	if(dateTimeFormat != null){
	    		return new DateSerializer(false, new SimpleDateFormat(dateTimeFormat.pattern()));
	    	}

	    	return super.createContextual(serializers, property);
	    }
	}

}
