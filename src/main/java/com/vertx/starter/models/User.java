package com.vertx.starter.models;

import java.sql.SQLException;
import java.util.HashMap;

import com.vertx.starter.util.BaseModel;

public class User extends BaseModel {

	public User() throws SQLException, ClassNotFoundException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected HashMap<String, String> getColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getConstraint() {
		// TODO Auto-generated method stub
		return null;
	}

}
