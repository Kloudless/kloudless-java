package com.kloudless.model;

import java.util.List;

public class APIKeyCollection extends KloudlessCollection<APIKey> {
	public Integer total;
	public Integer count;
	public Integer page;
	public List<APIKey> objects;
}
