package org.code4you.pricegrabber.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.apache.tapestry5.FormValidationControl;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.code4you.pricegrabber.entities.GrabItem;
import org.code4you.pricegrabber.exception.UnsuccessfulGrabException;
import org.code4you.pricegrabber.parser.ContentParser;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.xml.sax.SAXException;

/**
 * Start page of application pricegrabber.
 */
public class Index {
	@Property
	@Inject
	@Symbol(SymbolConstants.TAPESTRY_VERSION)
	private String tapestryVersion;

	@InjectComponent
	private Zone listZone;

	@Inject
	private AlertManager alertManager;

	@InjectComponent
	private BeanEditForm formUrl;
	
	@InjectComponent
	private Form formList;

	@SessionState
	@Property
	private GrabItem newItem;

	@Property
	private GrabItem item;

	@Inject
	private Session session;

	public Object onSuccess() {
		newItem.setLookupDate(new Date());
		return Confirm.class;
	}

	public void onValidateFromFormUrl() {
		try {
			new URL(newItem.getSourceUrl());
			grab(newItem, formUrl);
		} catch (MalformedURLException e) {
			formUrl.recordError("Invalid URL");
		}
	}
	
	@CommitAfter
	public Object onActionFromDelete(GrabItem item) {
		session.delete(item);
		return listZone.getBody();
	}
	
	@CommitAfter
	public Object onActionFromLookup(GrabItem item) {
		if (grab(item, formList)) {
			item.setLookupDate(new Date());
			session.persist(item);
		}
		return listZone.getBody();
	}

	private boolean grab(GrabItem item, FormValidationControl form) {
		try {
			ContentParser parser = new ContentParser(item.getDomain());
			parser.parseHtml(item.getSourceUrl(), item);
			return true;
		} catch (IOException e) {
			form.recordError("IOError: " + e.getMessage());
		} catch (XPathExpressionException e) {
			form.recordError("XPath error (bad selector definition in properties file): " + e.getMessage());
		} catch (SAXException e) {
			form.recordError("SAX Error: " + e.getMessage());
		} catch (UnsuccessfulGrabException e) {
			form.recordError("Unsuccessful grab: " + e.getMessage());
		}
		return false;
	}
	
	public Date getCurrentTime() {
		return new Date();
	}

	@SuppressWarnings("unchecked")
	public List<GrabItem> getGrabItems() {
		return session.createCriteria(GrabItem.class).addOrder(Order.desc("lookupDate")).list();
	}

	public Date getLookupDateProperty() {
        return item.getLookupDate();
	}

}
