package org.code4you.pricegrabber.entities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.StringTokenizer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.tapestry5.beaneditor.NonVisual;
import org.code4you.pricegrabber.model.Grabbable;
import org.code4you.pricegrabber.parser.GrabField;

@Entity
public class GrabItem implements Grabbable {

	@Id
	@NonVisual
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String sourceUrl;

	@NonVisual
	@GrabField
	private String itemNumber;

	@NonVisual
	@GrabField
	private String title;

	@NonVisual
	@GrabField
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
	@NonVisual
	public String getDomain() {
		String result = "Invalid domain";
		if (sourceUrl != null) {
			try {
				URL url = new URL(sourceUrl);
				result = url.getHost();
				StringTokenizer tok = new StringTokenizer(result, ".");
				int c;
				if ((c = tok.countTokens()) > 2) {
					for (int i = 0; i < c - 2; i++)
						tok.nextToken();
					result = new StringBuilder(tok.nextToken()).append('.').append(tok.nextToken()).toString();
				}
			} catch (MalformedURLException e) {
			}
		}
		return result;
	}

}
