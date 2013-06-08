package org.code4you.pricegrabber.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
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
import org.code4you.pricegrabber.entities.ProductPriceRecord;
import org.code4you.pricegrabber.grabber.Grabber;
import org.code4you.pricegrabber.grabber.exception.GrabberConfigurationException;
import org.code4you.pricegrabber.grabber.exception.UnsuccessfulGrabException;
import org.code4you.pricegrabber.grabber.result.GrabResult;
import org.code4you.pricegrabber.musix.MusixGrabberConfigurationBuilder;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Start page of application pricegrabber.
 */
public class Index {

	private static Grabber grabber = new Grabber(MusixGrabberConfigurationBuilder.buildConfiguration());

	private Logger logger = LoggerFactory.getLogger(getClass());

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
	private ProductPriceRecord newItem;

	@Property
	private ProductPriceRecord item;

	@Inject
	private Session session;

	public Object onSuccess() {
		newItem.setLookupDate(new Date());
		return Confirm.class;
	}

	public void onValidateFromFormUrl() {
		try {
			new URL(newItem.getSourceUrl());
			doGrab(newItem, formUrl);
		} catch (MalformedURLException e) {
			formUrl.recordError("Invalid URL.");
		}
	}

	@CommitAfter
	public Object onActionFromDelete(ProductPriceRecord item) {
		session.delete(item);
		return listZone.getBody();
	}

	@CommitAfter
	public Object onActionFromLookup(ProductPriceRecord item) {
		if (doGrab(item, formList)) {
			item.setLookupDate(new Date());
			session.persist(item);
		}
		return listZone.getBody();
	}

	private boolean doGrab(ProductPriceRecord item, FormValidationControl form) {
		boolean result = false;
		try {
			grabber.grab(item.getSourceUrl(), item);
			result = true;
		} catch(UnsuccessfulGrabException e) {
			form.recordError("Grab operation failed: " + e.getMessage());
		} catch(GrabberConfigurationException e) {
			form.recordError("There seems to be a grabber configuration error: " + e.getMessage());
		} catch(MalformedURLException e) {
			form.recordError("Invalid URL.");
		} catch(UnknownHostException e) {
			form.recordError("Unknown Host or network down: " + e.getMessage());
		} catch(IOException e) {
			form.recordError("Network or filesystem error: " + e.getMessage());
		} catch(XPathExpressionException e) {
			form.recordError("XPath-Selector invalid: " + e.getMessage());
		} catch(SAXException e) {
			form.recordError("Webpage source imparsable: " + e.getMessage());
		} catch (Exception e) {
			form.recordError(e.getClass().getSimpleName() + ": " + e.getMessage());
			logger.error(e.getClass().getSimpleName() + ": " + e.getMessage(), e);
		}
		return result;
	}

	public Date getCurrentTime() {
		return new Date();
	}

	@SuppressWarnings("unchecked")
	public List<ProductPriceRecord> getGrabItems() {
		return session.createCriteria(ProductPriceRecord.class).addOrder(Order.desc("lookupDate")).list();
	}

	public Date getLookupDateProperty() {
		return item.getLookupDate();
	}

}
