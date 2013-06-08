package org.code4you.pricegrabber.grabber.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrabResult {

	public enum Status {
		OK, ERROR, EXCEPTION
	};

	private Status status;
	private Map<String, GrabFieldResult> fieldsMap;
	private List<String> statusMessages = new ArrayList<String>();

	public GrabResult() {
		status = Status.OK;
		fieldsMap = new HashMap<String, GrabFieldResult>();
	}

	public void addField(GrabFieldResult field) {
		fieldsMap.put(field.getFieldName(), field);
	}

	public Status getStatus() {
		return status;
	}

	public Collection<GrabFieldResult> getFields() {
		return Collections.unmodifiableCollection(fieldsMap.values());
	}

	public GrabFieldResult getField(String name) {
		GrabFieldResult result = fieldsMap.get(name);
		return result;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void addStatusMessage(String message) {
		this.statusMessages.add(message);
	}

	public String getMultiLineStatusMessage() {
		if (status == Status.OK) {
			return "Ok";
		}
		StringBuilder sb = new StringBuilder();
		for (String msg : statusMessages) {
			sb.append(msg).append("\n");
		}
		return sb.toString();
	}

	public Collection<String> getStatusMessages() {
		return Collections.unmodifiableCollection(statusMessages);
	}
}
