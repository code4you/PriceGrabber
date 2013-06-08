package org.code4you.pricegrabber.grabber.config;

import java.util.HashMap;
import java.util.Map;

import org.code4you.pricegrabber.grabber.parser.ISimpleParser;
import org.code4you.pricegrabber.grabber.result.IGrabResultAdapter;

public class GrabberConfiguration {

	private Map<String, GrabDefinition> domainMap = new HashMap<String, GrabDefinition>();
	private ISimpleParser parser;
	private IGrabResultAdapter modelAdapter;

	public GrabberConfiguration(ISimpleParser parser, IGrabResultAdapter adapter) {
		this.parser = parser;
		this.modelAdapter = adapter;
	}
	
	public void addDomain(String domainName, GrabDefinition definition) {
		domainMap.put(domainName, definition);
	}
	
	public GrabDefinition getDefinition(String domainName) {
		return domainMap.get(domainName);
	}
	
	public ISimpleParser getParser() {
		return parser;
	}

	public IGrabResultAdapter getModelAdapter() {
		return modelAdapter;
	}
	
}
