package org.code4you.pricegrabber.parser;

import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import junit.framework.TestCase;

import org.code4you.pricegrabber.entities.GrabItem;
import org.code4you.pricegrabber.exception.UnsuccessfulGrabException;
import org.xml.sax.SAXException;

public class ContentParserTest extends TestCase {

	private static final String TEST_URL = "http://www.musik-produktiv.ch/boss-loop-station-rc-300.aspx";
	private static final String TEST_URL2 = "http://www.musicplace.ch/drums/drum-sets/d2-set-midnight-black.html";
	private static final String TEST_URL3 = "http://www.musicplace.ch/studio/diverses/42020-rackwagen-19-schwarz.html";

	private ContentParser parser;
	private GrabItem item;

	@Override
	protected void setUp() throws Exception {
	}

	public void testExtraction1() throws XPathExpressionException, SAXException, IOException, UnsuccessfulGrabException {
		item = new GrabItem();
		item.setSourceUrl(TEST_URL);
		parser = new ContentParser(item.getDomain());

		boolean result = parser.parseHtml(item.getSourceUrl(), item);
		assertTrue(result);
		System.out.println("ItemNumber=" + item.getItemNumber());
		System.out.println("Title=" + item.getTitle());
		System.out.println("Price=" + item.getPrice());
	}

	public void testExtraction2() throws XPathExpressionException, SAXException, IOException, UnsuccessfulGrabException {
		item = new GrabItem();
		item.setSourceUrl(TEST_URL2);
		parser = new ContentParser(item.getDomain());

		boolean result = parser.parseHtml(item.getSourceUrl(), item);
		assertTrue(result);
		System.out.println("ItemNumber=" + item.getItemNumber());
		System.out.println("Title=" + item.getTitle());
		System.out.println("Price=" + item.getPrice());
	}
	
	public void testExtraction3() throws XPathExpressionException, SAXException, IOException, UnsuccessfulGrabException {
		item = new GrabItem();
		item.setSourceUrl(TEST_URL3);
		parser = new ContentParser(item.getDomain());

		boolean result = parser.parseHtml(item.getSourceUrl(), item);
		assertTrue(result);
		System.out.println("ItemNumber=" + item.getItemNumber());
		System.out.println("Title=" + item.getTitle());
		System.out.println("Price=" + item.getPrice());
	}
}
