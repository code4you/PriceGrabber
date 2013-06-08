package org.code4you.pricegrabber.musix;

import java.io.IOException;

import org.code4you.pricegrabber.grabber.config.GrabDefinition;
import org.code4you.pricegrabber.grabber.config.GrabberConfiguration;
import org.code4you.pricegrabber.grabber.config.PropertyFileSelectorBuilder;
import org.code4you.pricegrabber.grabber.config.selector.IFieldSelector.FieldType;
import org.code4you.pricegrabber.grabber.exception.GrabberConfigurationException;
import org.code4you.pricegrabber.grabber.parser.SimpleHtmlDomParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusixGrabberConfigurationBuilder {

	public static final String[] DOMAINS = { "musik-produktiv.ch", "musicplace.ch" };
	public static final String[] FIELDS = { "itemNumber", "title", "price" };
	public static final FieldType[] FIELD_TYPES = { FieldType.STRING, FieldType.STRING, FieldType.FLOAT };

	public static final String BASE_DIR = "/selectors/";
	public static final String EXTENSION = ".properties";

	private static Logger logger = LoggerFactory.getLogger(MusixGrabberConfigurationBuilder.class);

	public static GrabberConfiguration buildConfiguration() {
		GrabberConfiguration config = new GrabberConfiguration(new SimpleHtmlDomParser(), new ProductPriceGrabResultAdapter());
		for (String domain : DOMAINS) {
			try {
				config.addDomain(domain, readDefinitionForDomain(domain));
			} catch (Exception e) {
				logger.error("Could not read definition file for '" + domain + "'.", e);
				// TODO further error handling suppressed because of static allocation of Grabber in Webapp
			}
		}
		return config;
	}

	private static GrabDefinition readDefinitionForDomain(String domain) throws IOException, GrabberConfigurationException {
		GrabDefinition definition = new GrabDefinition();
		PropertyFileSelectorBuilder builder = new PropertyFileSelectorBuilder(BASE_DIR + domain + EXTENSION);
		for (int i = 0; i < FIELDS.length; i++) {
			definition.addFieldSelector(FIELDS[i], builder.buildSelector(FIELDS[i], FIELD_TYPES[i]));
		}
		return definition;
	}

}
