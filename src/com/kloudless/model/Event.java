package com.kloudless.model;

import com.kloudless.net.APIResourceMixin;

public class Event extends APIResourceMixin {
	public String id;
	public String action;
	public Integer account;
	public Metadata metadata;
}
