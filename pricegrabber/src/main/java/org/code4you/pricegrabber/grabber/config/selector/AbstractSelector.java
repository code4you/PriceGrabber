package org.code4you.pricegrabber.grabber.config.selector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.code4you.pricegrabber.grabber.exception.UnsuccessfulGrabException;
import org.code4you.pricegrabber.grabber.util.TypeConverter;
import org.w3c.dom.Document;

public abstract class AbstractSelector implements IFieldSelector {

	protected FieldType targetType;
	protected String selector;

	public AbstractSelector(FieldType targetType, String selector) {
		this.targetType = targetType;
		this.selector = selector;
	}

	public Object parseDocument(Document doc) throws Exception {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile(selector);
		String value = (String) expr.evaluate(doc, XPathConstants.STRING);
		String processedValue = processValue(value);
		if (processedValue == null) {
			throw new UnsuccessfulGrabException("Null value fetched.");
		}
		try {
			return TypeConverter.convert(processedValue, targetType);
		} catch (NumberFormatException e) {
			throw new UnsuccessfulGrabException("Not a valid number: '" + processedValue + "'.");
		}
	}

	public FieldType getTargetType() {
		return targetType;
	}

	protected abstract String processValue(String value) throws Exception;

}
