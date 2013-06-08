package org.code4you.pricegrabber.grabber.config;

import java.util.HashMap;
import java.util.Map;

import org.code4you.pricegrabber.grabber.config.selector.IFieldSelector;
import org.code4you.pricegrabber.grabber.result.GrabFieldResult;
import org.code4you.pricegrabber.grabber.result.GrabResult;
import org.code4you.pricegrabber.grabber.result.GrabResult.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class GrabDefinition {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Map<String, IFieldSelector> selectorMap = new HashMap<String, IFieldSelector>();

	public void addFieldSelector(String name, IFieldSelector selector) {
		selectorMap.put(name, selector);
	}

	public GrabResult applyTo(Document document) throws Exception {
		GrabResult result = new GrabResult();

		for (String name : selectorMap.keySet()) {
			IFieldSelector selector = selectorMap.get(name);
			Object value = selector.parseDocument(document);
			logger.debug("Selector result for '" + name + "' = '" + value == null ? "<null>" : value.toString());
			if (value == null) {
				result.setStatus(Status.ERROR);
				result.addStatusMessage("Null value fetched for field '" + name + "'.");
			} else {
				result.addField(new GrabFieldResult(selector.getTargetType(), name, value));
			}
		}
		return result;
	}

}
