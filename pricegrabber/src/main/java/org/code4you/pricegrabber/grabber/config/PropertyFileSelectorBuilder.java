package org.code4you.pricegrabber.grabber.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.code4you.pricegrabber.grabber.config.selector.IFieldSelector;
import org.code4you.pricegrabber.grabber.config.selector.IFieldSelector.FieldType;
import org.code4you.pricegrabber.grabber.config.selector.ImageSelector;
import org.code4you.pricegrabber.grabber.config.selector.TextSelector;
import org.code4you.pricegrabber.grabber.exception.GrabberConfigurationException;

public class PropertyFileSelectorBuilder {

	public static final String TYPE = ".type";
	public static final String SELECTOR = ".selector";
	public static final String REGEXP = ".regex";
	public static final String REPLACE_REGEXP = ".replace_regex";
	public static final String DECIMAL_SEPARATOR = ".decimalSeparator";
	public static final String GROUPING_SEPARATOR = ".groupingSeparator";

	public static final String CONTENT_TYPE_IMAGE = "image";
	public static final String CONTENT_TYPE_TEXT = "text";

	private Properties props = new Properties();
	private String filename;

	public PropertyFileSelectorBuilder(String filename) throws IOException {
		this.filename = filename;
		InputStream stream = getClass().getResourceAsStream(filename);
		if (stream == null)
			throw new IOException("File '" + filename + "' not found.");
		props.load(stream);
		stream.close();
	}

	public IFieldSelector buildSelector(String fieldname, FieldType targetType) throws GrabberConfigurationException {
		IFieldSelector result;
		String type = props.getProperty(fieldname + TYPE);
		if (type == null) {
			throw new GrabberConfigurationException("No property value for field '" + fieldname + TYPE + "' found in file '"
					+ filename + "'.");
		}
		String selector = props.getProperty(fieldname + SELECTOR, "/");
		if (selector == null) {
			throw new GrabberConfigurationException("No property value for field '" + fieldname + SELECTOR + "' found in file '"
					+ filename + "'.");
		}
		String regExp = props.getProperty(fieldname + REGEXP, "");
		String replaceRegExp = props.getProperty(fieldname + REPLACE_REGEXP, "");
		if (type.equals(CONTENT_TYPE_TEXT)) {
			result = new TextSelector(targetType, selector, regExp, replaceRegExp);
		} else if (type.equals(CONTENT_TYPE_IMAGE)) {
			result = new ImageSelector(targetType, selector, regExp, replaceRegExp);
		} else {
			throw new IllegalArgumentException("Unknown field type for '" + fieldname + "' in file '" + filename + "'.");
		}
		return result;

	}
}
