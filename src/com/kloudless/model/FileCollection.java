package com.kloudless.model;

import java.util.List;

public class FileCollection extends KloudlessCollection<Metadata> {
	public Integer total;
	public Integer count;
	public Integer page;
	public List<Metadata> files;
}
