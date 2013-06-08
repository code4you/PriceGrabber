package org.code4you.pricegrabber.grabber.util;

import org.code4you.pricegrabber.grabber.config.selector.IFieldSelector.FieldType;

public class TypeConverter {
	
	public static Object convert(String value, FieldType type) {
		switch(type) {
		case STRING:
			return value == null ? null : value.trim();
		case FLOAT:
			return value == null ? null : Float.valueOf(value);
		case INT:
			return value == null ? null : Integer.valueOf(value);
		}
		throw new IllegalArgumentException("Unhandled FieldType.");
	}

}
