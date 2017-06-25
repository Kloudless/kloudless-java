package com.kloudless.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class KloudlessRawJsonObjectDeserializer implements JsonDeserializer<KloudlessRawJsonObject> {
	public KloudlessRawJsonObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		KloudlessRawJsonObject object = new KloudlessRawJsonObject();
		object.json = json.getAsJsonObject();
		return object;
	}
}
