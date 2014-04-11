package com.kloudless.model;

import java.util.Map;

public class Data extends KloudlessObject {
	Map<String, Object> previousAttributes;
	KloudlessObject object;

	public Map<String, Object> getPreviousAttributes() {
		return previousAttributes;
	}

	public void setPreviousAttributes(Map<String, Object> previousAttributes) {
		this.previousAttributes = previousAttributes;
	}

	public KloudlessObject getObject() {
		return object;
	}

	public void setObject(KloudlessObject object) {
		this.object = object;
	}
}
