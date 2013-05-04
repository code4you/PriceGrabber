package org.code4you.pricegrabber.parser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.code4you.pricegrabber.exception.UnsuccessfulGrabException;
import org.code4you.pricegrabber.model.Grabbable;
import org.cyberneko.html.parsers.DOMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.asprise.util.ocr.OCR;

@Named
public class ContentParser {

	private Logger _log = LoggerFactory.getLogger(this.getClass());

	public static final String SELECTORS = "/selectors/";

	public static final String FIELD_TYPE = ".type";
	public static final String SELECTOR_DEF = ".selector";
	public static final String SPLITTER = ".splitter";
	public static final String TOKEN_INDEX = ".tokenIndex";
	public static final String DECIMAL_SEPARATOR = ".decimalSeparator";
	public static final String GROUPING_SEPARATOR = ".groupingSeparator";

	public static final String FIELD_TYPE_IMAGE = "image";
	public static final String FIELD_TYPE_BODY_TEXT = "bodyText";

	public static final String STRIP_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'?-/\\ "; 
	public static String UTF8_CHARS;

	static {
		try {
			UTF8_CHARS = new String(new byte[] { (byte) 0xc2, (byte) 0xa0, (byte) 0xe2, (byte) 0x80, (byte) 0x93 }, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			
		}
	}
	
	private Properties props = new Properties();

	public ContentParser(String domain) throws IOException, UnsuccessfulGrabException {
		InputStream stream = this.getClass().getResourceAsStream(SELECTORS + domain + ".properties");
		if (stream == null)
			throw new UnsuccessfulGrabException("No properties file found for '" + domain + "'.");
		props.load(stream);
	}

	public boolean parseHtml(String url, Grabbable item) throws SAXException, IOException, XPathExpressionException,
			UnsuccessfulGrabException {
		DOMParser parser = new DOMParser();
		parser.setFeature("http://xml.org/sax/features/namespaces", false); // important! Turn off namespace processing!!
		parser.setProperty("http://cyberneko.org/html/properties/default-encoding", "utf8"); // set default encoding

		parser.parse(new InputSource(new URL(url).openStream()));
		Document doc = parser.getDocument();
		processGrabbable(doc, item);
		return true;
	}

	private boolean processGrabbable(Document doc, Grabbable item) throws XPathExpressionException, UnsuccessfulGrabException,
			MalformedURLException, IOException {
		boolean result = false;
		XPathFactory xPathfactory = XPathFactory.newInstance();
		for (Field f : item.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(GrabField.class)) {
				result = true;
				String fieldName = f.getName();
				_log.debug("Grabbable field found: " + fieldName);
				XPath xpath = xPathfactory.newXPath();
				String xPath, fieldType;
				if ((xPath = props.getProperty(fieldName + SELECTOR_DEF)) != null) {
					_log.debug("Processing selector '" + xPath + "' for field '" + fieldName + "'.");
					XPathExpression expr = xpath.compile(xPath);
					String value = (String) expr.evaluate(doc, XPathConstants.STRING);
					if ((fieldType = props.getProperty(fieldName + FIELD_TYPE)) != null) {
						String fieldValue;
						if (fieldType.equals(FIELD_TYPE_IMAGE)) {
							fieldValue = extractImageData(f, value);
						} else if (fieldType.equals(FIELD_TYPE_BODY_TEXT)) {
							fieldValue = extractBodyTextData(f, value);
						} else {
							throw new IllegalArgumentException("FieldType '" + fieldType + "' found for '" + fieldName + "' @"
									+ item.getDomain() + " is not implemented");
						}
						setGrabbedFieldValue(f, item, fieldValue);
					}
				} else {
					throw new UnsuccessfulGrabException("No selector definition found for '" + fieldName + "'.");
				}
			}
		}
		return result;
	}

	private void setGrabbedFieldValue(Field f, Grabbable item, String fieldValue) throws UnsuccessfulGrabException {
		try {
			f.setAccessible(true);
			if (f.getType().equals(String.class)) {
				f.set(item, fieldValue);
			} else if (f.getType().equals(float.class)) {
				fieldValue = StringUtils.strip(fieldValue, STRIP_CHARS);
				fieldValue = StringUtils.strip(fieldValue, UTF8_CHARS);
				DecimalFormat df = new DecimalFormat();
				DecimalFormatSymbols sm = new DecimalFormatSymbols();
				sm.setDecimalSeparator(props.getProperty(f.getName() + DECIMAL_SEPARATOR, String.valueOf(sm.getDecimalSeparator()))
						.toCharArray()[0]);
				sm.setGroupingSeparator(props.getProperty(f.getName() + GROUPING_SEPARATOR, String.valueOf(sm.getGroupingSeparator()))
						.toCharArray()[0]);
				df.setDecimalFormatSymbols(sm);
				f.set(item, df.parse(fieldValue));
			} else {
				throw new UnsuccessfulGrabException("Unsupported field type '" + f.getType().getSimpleName() + "' for '" + f.getName()
						+ "'.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnsuccessfulGrabException("Could not set field value for '" + f.getName() + "'.");
		}
	}

	private String extractBodyTextData(Field f, String value) throws UnsuccessfulGrabException {
		String fieldName = f.getName();
		if (value == null || value.trim().length() == 0) {
			throw new UnsuccessfulGrabException("No value found for field '" + fieldName + "'.");
		}
		_log.debug("Extracting BodyText data from '" + value + "' for '" + fieldName + "'.");
		value = value.trim();
		if (props.containsKey(fieldName + SPLITTER)) {
			StringTokenizer tok = new StringTokenizer(value, props.getProperty(fieldName + SPLITTER));
			String indexStr;
			if ((indexStr = props.getProperty(fieldName + TOKEN_INDEX)) != null) {
				int index = new Integer(indexStr).intValue();
				for (int i = 0; i < index && tok.hasMoreTokens(); i++) {
					tok.nextToken();
				}
				if (!tok.hasMoreTokens()) {
					throw new UnsuccessfulGrabException("Field '" + fieldName + "' has not enough tokens (" + index + ").");
				}
				value = tok.nextToken().trim();
			}
		}
		return value;
	}

	private String extractImageData(Field f, String value) throws UnsuccessfulGrabException, MalformedURLException, IOException {
		String fieldName = f.getName();
		_log.debug("Extracting Image data from '" + value + "' for '" + fieldName + "'.");
		if (value == null) {
			throw new UnsuccessfulGrabException("No value (image URL) found for field '" + fieldName + "'.");
		}
		// loads the image.
		BufferedImage image = ImageIO.read(new URL(value).openStream());

		// recognizes both characters and barcodes
		String s = new OCR().recognizeCharacters(image);
		_log.debug("OCR output: " + s);
		if (s.contains("=== ASPRISE OCR SDK EVALUATION ===")) { // strip out licence information
			StringTokenizer tok = new StringTokenizer(s, ".");
			tok.nextToken();
			tok.nextToken();
			if (tok.hasMoreTokens()) {
				s = tok.nextToken();
			}
		}
		if (s == null || s.length() == 0) {
			throw new UnsuccessfulGrabException("OCR recocgnition unsuccessfull for '" + value + "'.");
		}
		return StringUtils.replace(s.trim(), "O", "0");
	}
	
}
