package com.kloudless.model;

import java.util.List;

public class MetadataCollection extends KloudlessCollection<Metadata> {
	public Integer count;
	public Integer page;
	public Boolean has_next;
	public List<Metadata> objects;
}
