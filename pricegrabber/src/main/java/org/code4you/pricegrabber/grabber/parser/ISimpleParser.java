package org.code4you.pricegrabber.grabber.parser;

import java.io.IOException;
import java.net.MalformedURLException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public interface ISimpleParser {

	public Document parse(String url) throws MalformedURLException, SAXException, IOException;
}
