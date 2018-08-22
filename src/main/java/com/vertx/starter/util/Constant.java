package com.vertx.starter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vertx.starter.MainVerticle;

import io.vertx.ext.asyncsql.AsyncSQLClient;

public class Constant {
	public static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);
	
	public static AsyncSQLClient postgreSQLClient = null;
}
