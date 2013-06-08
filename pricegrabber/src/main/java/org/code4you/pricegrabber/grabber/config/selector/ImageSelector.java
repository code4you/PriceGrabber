package org.code4you.pricegrabber.grabber.config.selector;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asprise.util.ocr.OCR;

public class ImageSelector extends TextSelector {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public ImageSelector(FieldType targetType, String selector, String regExp, String replaceRegExp) {
		super(targetType, selector, regExp, replaceRegExp);
	}
	
	@Override
	protected String processValue(String value) throws Exception {
		
		// loads the image.
		BufferedImage image = ImageIO.read(new URL(value).openStream());

		// recognizes only characters
		String recognizedValue;
		recognizedValue = new OCR().recognizeCharacters(image);
		recognizedValue = removeLicenseInformation(recognizedValue);
		if (recognizedValue == null || recognizedValue.length() == 0) {
			logger.warn("OCR recocgnition unsuccessfull for '" + value + "'.");
		}
		if (targetType != FieldType.STRING) {
			recognizedValue = StringUtils.replace(recognizedValue, "O", "0");
		}
		logger.debug("OCR recognized: " + recognizedValue);
		return super.processValue(recognizedValue);
	}

	// TODO only for testing purposes
	private String removeLicenseInformation(String s) {
		if (s.contains("=== ASPRISE OCR SDK EVALUATION ===")) { // strip out licence information
			StringTokenizer tok = new StringTokenizer(s, ".");
			tok.nextToken();
			tok.nextToken();
			if (tok.hasMoreTokens()) {
				s = tok.nextToken();
			}
		}
		return s.trim();
	}

}
