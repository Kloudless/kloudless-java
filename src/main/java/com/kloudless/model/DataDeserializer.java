package com.kloudless.model;

import com.google.gson.*;
import com.kloudless.net.APIResource;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataDeserializer implements JsonDeserializer<Data> {
	@SuppressWarnings("rawtypes")
	static Map<String, Class> objectMap = new HashMap<String, Class>();

	static {
		objectMap.put("account", Account.class);
		objectMap.put("file", File.class);
		objectMap.put("folder", Folder.class);
		objectMap.put("link", Link.class);
		objectMap.put("metadata", Metadata.class);
	}

	private Object deserializeJsonPrimitive(JsonPrimitive element) {
		if (element.isBoolean()) {
			return element.getAsBoolean();
		} else if (element.isNumber()) {
			return element.getAsNumber();
		} else {
			return element.getAsString();
		}
	}

	private Object[] deserializeJsonArray(JsonArray arr) {
		Object[] elems = new Object[arr.size()];
		Iterator<JsonElement> elemIter = arr.iterator();
		int i = 0;
		while (elemIter.hasNext()) {
			JsonElement elem = elemIter.next();
			elems[i++] = deserializeJsonElement(elem);
		}
		return elems;
	}

	private Object deserializeJsonElement(JsonElement element) {
		if (element.isJsonNull()) {
			return null;
		} else if (element.isJsonObject()) {
			Map<String, Object> valueMap = new HashMap<String, Object>();
			populateMapFromJSONObject(valueMap, element.getAsJsonObject());
			return valueMap;
		} else if (element.isJsonPrimitive()) {
			return deserializeJsonPrimitive(element.getAsJsonPrimitive());
		} else if (element.isJsonArray()) {
			return deserializeJsonArray(element.getAsJsonArray());
		} else {
			System.err.println("Unknown JSON element type for element " + element + ". " +
					"If you're seeing this messaage, it's probably a bug in the Kloudless Java " +
					"library. Please contact us by email at support@kloudless.com.");
			return null;
		}
	}

	private void populateMapFromJSONObject(Map<String, Object> objMap, JsonObject jsonObject) {
		for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			String key = entry.getKey();
			JsonElement element = entry.getValue();
			objMap.put(key, deserializeJsonElement(element));
		}
	}

	@SuppressWarnings("unchecked")
	public Data deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		Data data = new Data();
		JsonObject jsonObject = json.getAsJsonObject();
		for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			String key = entry.getKey();
			JsonElement element = entry.getValue();
			if ("previous_attributes".equals(key)) {
				Map<String, Object> previousAttributes = new HashMap<String, Object>();
				populateMapFromJSONObject(previousAttributes, element.getAsJsonObject());
				data.setPreviousAttributes(previousAttributes);
			} else if ("object".equals(key)) {
				String type = element.getAsJsonObject().get("object").getAsString();
				Class<KloudlessObject> cl = objectMap.get(type);
				KloudlessObject object = APIResource.GSON.fromJson(entry.getValue(), cl != null ? cl : KloudlessRawJsonObject.class);
				data.setObject(object);
			}
		}
		return data;
	}
}
