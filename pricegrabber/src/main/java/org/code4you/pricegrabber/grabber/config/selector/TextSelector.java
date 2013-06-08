package org.code4you.pricegrabber.grabber.config.selector;

public class TextSelector extends AbstractSelector {

	private String regExp;
	private String replaceRegExp;
	
	public TextSelector(FieldType targetType, String selector, String regExp, String replaceRegExp) {
		super(targetType, selector);
		this.regExp = regExp;
		this.replaceRegExp = replaceRegExp;
	}

	@Override
	protected String processValue(String value) throws Exception {
		return value.replaceAll(regExp, replaceRegExp);
	}

	
}
