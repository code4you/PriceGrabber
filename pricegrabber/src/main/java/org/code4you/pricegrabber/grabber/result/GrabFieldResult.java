package org.code4you.pricegrabber.grabber.result;

import org.code4you.pricegrabber.grabber.config.selector.IFieldSelector.FieldType;

public class GrabFieldResult {
	
	private FieldType fieldType;
	private String fieldName;
	private Object value;

	public GrabFieldResult(FieldType fieldType, String fieldName, Object value) {
		this.fieldType = fieldType;
		this.fieldName = fieldName;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public Object getValue() {
		return value;
	}

}
