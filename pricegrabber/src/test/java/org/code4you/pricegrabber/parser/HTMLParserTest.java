package org.code4you.pricegrabber.parser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import junit.framework.TestCase;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.asprise.util.ocr.OCR;

public class HTMLParserTest extends TestCase {

	public static String SIMPLE_URL = "http://code4you.org";
	public static String TEST_URL = "http://www.musik-produktiv.ch/boss-loop-station-rc-300.aspx";
	private boolean childrenPresent;

	@Override
	protected void setUp() throws Exception {
		childrenPresent = false;
	}

	public void testParser() throws MalformedURLException, SAXException, IOException {
		DOMParser parser = new DOMParser();
		parser.setFeature("http://xml.org/sax/features/namespaces", false);
		parser.parse(new InputSource(new URL(TEST_URL).openStream()));
		Document doc = parser.getDocument();
		childrenPresent = false;
		print(doc, "");
		assertTrue(childrenPresent);

	}

	public void testXPath() throws MalformedURLException, SAXException, IOException, XPathExpressionException,
			ParserConfigurationException {
		DOMParser parser = new DOMParser();
		parser.setFeature("http://xml.org/sax/features/namespaces", false);

		parser.parse(new InputSource(new URL(TEST_URL).openStream()));
		Document doc = parser.getDocument();
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		XPathExpression expression = xPath.compile("//HEAD/TITLE");
		NodeList nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		assertTrue(nodeList != null && nodeList.getLength() > 0);
		String value = expression.evaluate(doc);
		System.out.println(value);
		expression = xPath.compile("//DIV[@class='fliess08']/DIV[@style='position:absolute; left:0; bottom:0;']");
		nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		assertTrue(nodeList != null && nodeList.getLength() > 0);
		value = expression.evaluate(doc);
		System.out.println(value);
		expression = xPath.compile("//DIV[@id='main_center']/H2");
		nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		assertTrue(nodeList != null && nodeList.getLength() > 0);
		System.out.println("Length=" + nodeList.getLength());
		value = expression.evaluate(doc);
		System.out.println(value);
		expression = xPath.compile("//IMG[@alt='Preis']/@src");
		nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		assertTrue(nodeList != null && nodeList.getLength() > 0);
		System.out.println("Length=" + nodeList.getLength());
		value = expression.evaluate(doc);
		
		// loads the image.
		BufferedImage image = ImageIO.read(new URL(value).openStream());

		// recognizes both characters and barcodes
		String s = new OCR().recognizeEverything(image);

		// prints the results.
		System.out.println("RESULTS: " + s);

		System.out.println(value);
	}

	private void print(Node node, String indent) {
		System.out.println(indent + node.getClass().getName() + " '" + node.getNamespaceURI() + ":" + node.getNodeName() + "'");
		if (node.hasAttributes()) {
			NamedNodeMap map = node.getAttributes();
			System.out.println(indent + "[" + attributesAsString(map) + "]");
		}
		Node child = node.getFirstChild();
		while (child != null) {
			childrenPresent = true;
			print(child, indent + " ");
			child = child.getNextSibling();
		}
	}

	private String attributesAsString(NamedNodeMap map) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < map.getLength(); i++) {
			Node node = map.item(i);
			sb.append(" ").append(node.getNodeName()).append(":").append(node.getNodeValue());
		}
		return sb.toString();
	}
}
