package org.code4you.pricegrabber.grabber.util;

import java.net.URI;
import java.net.URISyntaxException;

public class DomainUtil {

	public static String getDomainName(String url) throws URISyntaxException {
		if (url == null) {
			return null;
		}
	    URI uri = new URI(url);
	    String domain = uri.getHost();
	    return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
	
}
