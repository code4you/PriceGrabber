package org.code4you.pricegrabber.pages;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.code4you.pricegrabber.entities.ProductPriceRecord;
import org.code4you.pricegrabber.grabber.result.GrabResult;
import org.hibernate.Session;

/**
 * Start page of application pricegrabber.
 */
public class Confirm {

	@Property
	@Inject
	@Symbol(SymbolConstants.TAPESTRY_VERSION)
	private String tapestryVersion;

	@SessionState
	@Property
	private ProductPriceRecord newItem;

	@Inject
	private Session session;

	@CommitAfter
	public Object onSuccess() {
		if (newItem != null && newItem.isPopulatedAndNew()) {
			session.persist(newItem);
		}
		newItem = new ProductPriceRecord();
		return Index.class;
	}

	public Object onActionFromCancel() {
		newItem = new ProductPriceRecord();
		return Index.class;
	}

}
