package com.kloudless.model;

import java.util.List;


public class AccountCollection extends KloudlessCollection<Account> {
	public Integer total;
	public Integer count;
	public Integer page;
	public List<Account> objects;
}