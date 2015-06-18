package com.kloudless.model;

import java.util.List;

public class UserCollection extends KloudlessCollection<User> {
	public Integer count;
	public Integer next_page;
	public List<User> objects;
	public Integer page;
}
