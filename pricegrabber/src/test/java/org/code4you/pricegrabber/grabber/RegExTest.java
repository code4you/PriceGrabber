package org.code4you.pricegrabber.grabber;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

public class RegExTest extends TestCase {

	Properties props = new Properties();

	public void testRegEx() throws IOException {
		InputStream stream1 = getClass().getResourceAsStream("/selectors/musik-produktiv.ch.properties");
		props.load(stream1);
		assertEquals(replace("  123456,78 "), "123456.78");
		assertEquals(replace("123456,78"), "123456.78");
		assertEquals(replace("1,23 "), "1.23");
		assertEquals(replace("  0,12"), "0.12");
		stream1.close();

		InputStream stream2 = getClass().getResourceAsStream("/selectors/musicplace.ch.properties");
		props.load(stream2);
		assertEquals(replace("  CHF 1'234.- "), "1234.0");
		assertEquals(replace("CHF 1'234.-"), "1234.0");
		assertEquals(replace("1.-"), "1.0");
		assertEquals(replace("1234.- "), "1234.0");
		stream2.close();
		
		String regEx = "(^.+):(\\s*)([-\\w]+).*$";
		System.out.println("Abc: 2738ab3".replaceAll(regEx, "$3"));
		assertEquals("2738ab3", "Abc: 2738ab3".replaceAll(regEx, "$3"));
		assertEquals("2738ab3", " Abc:2738ab3  ".replaceAll(regEx, "$3"));
		assertEquals("2-738ab3", "Abc: 2-738ab3 ".replaceAll(regEx, "$3"));
	}

	private String replace(String testString) throws IOException {
		String regex = props.getProperty("price.regex");
		String replace = props.getProperty("price.replace_regex");
		String result = testString.replaceAll(regex, replace);
		System.out.println(testString + " -> " + result);
		return result;
	}

}
