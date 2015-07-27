package com.kloudless.model;

import com.kloudless.net.APIResourceMixin;

public class Event extends APIResourceMixin {
	public String id;
	public Integer event_id;
	public Integer account;
	public String action;
	public String modified;
	public String type;
	public String user_id;
	public String ip;
	public Metadata metadata;
}
