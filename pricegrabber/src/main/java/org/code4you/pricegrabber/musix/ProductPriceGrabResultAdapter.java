package org.code4you.pricegrabber.musix;

import org.code4you.pricegrabber.entities.ProductPriceRecord;
import org.code4you.pricegrabber.grabber.result.GrabFieldResult;
import org.code4you.pricegrabber.grabber.result.GrabResult;
import org.code4you.pricegrabber.grabber.result.IGrabResultAdapter;

public class ProductPriceGrabResultAdapter implements IGrabResultAdapter {

	public void apply(GrabResult result, Object model) {
		if (!(model instanceof ProductPriceRecord)) {
			throw new IllegalArgumentException("Argument 'model' must be of type ProductPriceRecord");
		}

		ProductPriceRecord record = (ProductPriceRecord) model;
		GrabFieldResult f;
		record.setItemNumber((f = result.getField("itemNumber")) == null ? null : (String) f.getValue());
		record.setPrice((f = result.getField("price")) == null ? null : (Float) f.getValue());
		record.setTitle((f = result.getField("title")) == null ? null : (String) f.getValue());
	}
}
