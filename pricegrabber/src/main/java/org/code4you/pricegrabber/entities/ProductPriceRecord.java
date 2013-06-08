package org.code4you.pricegrabber.entities;

import java.net.URISyntaxException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.tapestry5.beaneditor.NonVisual;
import org.code4you.pricegrabber.grabber.util.DomainUtil;

@Entity
public class ProductPriceRecord {

	@Id
	@NonVisual
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String sourceUrl;

	@NonVisual
	private String itemNumber;

	@NonVisual
	private String title;

	@NonVisual
	private float price;

	@NonVisual
	private Date lookupDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getLookupDate() {
		return lookupDate;
	}

	public void setLookupDate(Date lookupDate) {
		this.lookupDate = lookupDate;
	}

	@Transient
	public String getDomain() {
		try {
			return DomainUtil.getDomainName(sourceUrl);
		} catch (URISyntaxException e) {
			return "Invalid URL";
		}
	}

	@Transient
	public boolean isPopulatedAndNew() {
		return id == 0 && sourceUrl != null && !sourceUrl.isEmpty();
	}
	
}
