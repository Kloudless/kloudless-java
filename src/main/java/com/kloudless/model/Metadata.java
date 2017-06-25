package com.kloudless.model;

import com.kloudless.net.APIResourceMixin;

import java.util.List;

public class Metadata extends APIResourceMixin {
	public Long account;
	public String id;
	public String raw_id;
	public String path;
	public String name;
	public Identifier parent;
	public List<Identifier> ancestors;
	public String created;
	public String modified;
	public Boolean downloadable;
	public String type;
	public String mime_type;
	public Long size;
	public Boolean can_create_folders;
	public Boolean can_upload_files;
	public Identifier owner;
	public Identifier creator;
	public Identifier last_modifier;
}
