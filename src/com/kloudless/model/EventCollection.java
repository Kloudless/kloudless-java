package com.kloudless.model;

import java.util.List;

public class EventCollection extends KloudlessCollection<Event> {
	public Integer cursor;
	public List<Event> objects;
}
