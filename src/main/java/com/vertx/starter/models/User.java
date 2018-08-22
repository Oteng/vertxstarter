package com.vertx.starter.models;

import java.sql.SQLException;
import java.util.HashMap;

import com.vertx.starter.util.BaseModel;

public class User extends BaseModel {

	@Override
	protected String getClassName() {
		return "user";
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected HashMap<String, String> getColumns() {
		HashMap<String, String> columns = new HashMap<>();
		columns.put("id", "serial not null");
		columns.put("name", "varchar(100) not null");
		columns.put("address", "varchar(170) not null");
		columns.put("created_at", "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP");
		columns.put("updated_at", "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP");
		return columns;
	}

	@Override
	protected String getConstraint() {
		return "PRIMARY KEY (id),";
	}

}
