package org.code4you.pricegrabber.grabber;

import org.code4you.pricegrabber.grabber.config.GrabDefinition;
import org.code4you.pricegrabber.grabber.config.GrabberConfiguration;
import org.code4you.pricegrabber.grabber.exception.GrabberConfigurationException;
import org.code4you.pricegrabber.grabber.parser.ISimpleParser;
import org.code4you.pricegrabber.grabber.result.GrabResult;
import org.code4you.pricegrabber.grabber.result.GrabResult.Status;
import org.code4you.pricegrabber.grabber.result.IGrabResultAdapter;
import org.code4you.pricegrabber.grabber.util.DomainUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class Grabber {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private GrabberConfiguration configuration;
	private GrabResult lastResult;

	public Grabber(GrabberConfiguration configuration) {
		this.configuration = configuration;
	}

	public void grab(String url, Object model) throws Exception {
		try {
			ISimpleParser parser = configuration.getParser();
			IGrabResultAdapter adapter = configuration.getModelAdapter();
			String domain = DomainUtil.getDomainName(url);
			GrabDefinition definition = configuration.getDefinition(domain);
			if (definition != null) {
				Document document = parser.parse(url);
				GrabResult result = definition.applyTo(document);
				adapter.apply(result, model);
				setGrabResult(result);
			} else {
				throw new GrabberConfigurationException("No config for domain '" + domain + "'.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setExceptionGrabResult(e);
			throw e;
		}
	}

	private synchronized void setExceptionGrabResult(Exception e) {
		lastResult = new GrabResult();
		lastResult.setStatus(Status.EXCEPTION);
		lastResult.addStatusMessage(e.getMessage());
	}

	private synchronized void setGrabResult(GrabResult result) {
		this.lastResult = result;
	}

	public GrabResult getGrabResult() {
		return lastResult;
	}
}
