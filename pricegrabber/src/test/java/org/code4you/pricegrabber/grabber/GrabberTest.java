package org.code4you.pricegrabber.grabber;

import junit.framework.TestCase;

import org.code4you.pricegrabber.entities.ProductPriceRecord;
import org.code4you.pricegrabber.musix.MusixGrabberConfigurationBuilder;

public class GrabberTest extends TestCase {

	private static final String TEST_URL = "http://www.musik-produktiv.ch/boss-loop-station-rc-300.aspx";
	private static final String TEST_URL2 = "http://www.musicplace.ch/drums/drum-sets/d2-set-midnight-black.html";
	private static final String TEST_URL3 = "http://www.musicplace.ch/studio/diverses/42020-rackwagen-19-schwarz.html";

	private ProductPriceRecord item;
	private Grabber grabber;

	@Override
	protected void setUp() throws Exception {
		grabber = new Grabber(MusixGrabberConfigurationBuilder.buildConfiguration());
	}
	
	public void testExtraction1() throws Exception {
		item = new ProductPriceRecord();
		item.setSourceUrl(TEST_URL);
		grabber.grab(item.getSourceUrl(), item);

		System.out.println("ItemNumber=" + item.getItemNumber());
		System.out.println("Title=" + item.getTitle());
		System.out.println("Price=" + item.getPrice());
		assertEquals("100067041", item.getItemNumber());
		assertTrue(Math.abs(455.0-item.getPrice()) < 0.0001);
	}

	public void testExtraction2() throws Exception {
		item = new ProductPriceRecord();
		item.setSourceUrl(TEST_URL2);
		grabber.grab(item.getSourceUrl(), item);

		System.out.println("ItemNumber=" + item.getItemNumber());
		System.out.println("Title=" + item.getTitle());
		System.out.println("Price=" + item.getPrice());
		assertEquals("3649", item.getItemNumber());
		assertTrue(Math.abs(478.0-item.getPrice()) < 0.0001);
	}
	
	public void testExtraction3() throws Exception {
		item = new ProductPriceRecord();
		item.setSourceUrl(TEST_URL3);
		grabber.grab(item.getSourceUrl(), item);

		System.out.println("ItemNumber=" + item.getItemNumber());
		System.out.println("Title=" + item.getTitle());
		System.out.println("Price=" + item.getPrice());
		assertEquals("1645", item.getItemNumber());
		assertTrue(Math.abs(425.0-item.getPrice()) < 0.0001);
	}
}
