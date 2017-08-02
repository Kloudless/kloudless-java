package com.kloudless.model;

import java.util.List;

public class CRMObjectCollection extends KloudlessCollection<CRMObject> {
	public Integer total;
	public Integer count;
	public Integer page;
	public List<CRMObject> objects;
	public List<CRMObject> errors;
}