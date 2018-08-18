package com.vertx.starter.controllers;

import com.vertx.starter.util.BaseController;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class IndexController extends BaseController {

	public static void handler(RoutingContext rc) {
		JsonObject obj = new JsonObject();
		obj.put("greatings", "Hello World");
		sendResponds(rc, obj);
	}
}
