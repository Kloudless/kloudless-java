package com.kloudless.model;

import java.util.List;


public class TokenCollection extends KloudlessCollection<Account> {
	public Integer total;
	public Integer count;
	public Integer page;
	public List<Token> objects;
}