package org.code4you.pricegrabber.grabber.config.selector;

import org.w3c.dom.Document;

public interface IFieldSelector {
	
	enum FieldType {
		STRING,
		INT,
		FLOAT
	}

	public Object parseDocument(Document doc) throws Exception;
	public FieldType getTargetType();

}
