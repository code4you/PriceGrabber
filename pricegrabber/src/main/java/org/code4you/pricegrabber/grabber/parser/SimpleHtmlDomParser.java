package org.code4you.pricegrabber.grabber.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Named;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Named
public class SimpleHtmlDomParser implements ISimpleParser {

	public Document parse(String url) throws MalformedURLException, SAXException, IOException {
		DOMParser parser = new DOMParser();
		parser.setFeature("http://xml.org/sax/features/namespaces", false); // important! Turn off namespace processing!!
		parser.setProperty("http://cyberneko.org/html/properties/default-encoding", "utf8"); // set default encoding

		InputStream stream = new URL(url).openStream();
		parser.parse(new InputSource(stream));
		return parser.getDocument();
	}

}
