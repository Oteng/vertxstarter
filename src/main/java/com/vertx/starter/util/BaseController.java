package com.vertx.starter.util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class BaseController {
	
	protected static void sendResponds(RoutingContext context, JsonObject data, int statusCode) {
		context.response().setStatusCode((statusCode)).putHeader("content-type", "application/json")
		.end(data.encodePrettily());
	}
	
	protected static void sendResponds(RoutingContext context, JsonObject data) {
		context.response().setStatusCode((200)).putHeader("content-type", "application/json")
		.end(data.encodePrettily());
	}

	protected static void badRequest(RoutingContext context, Throwable ex) {
		context.response().setStatusCode(400).putHeader("content-type", "application/json")
				.end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
	}

	protected static void notFound(RoutingContext context) {
		context.response().setStatusCode(404).putHeader("content-type", "application/json")
				.end(new JsonObject().put("message", "not_found").encodePrettily());
	}

	protected static void internalError(RoutingContext context, Throwable ex) {
		context.response().setStatusCode(500).putHeader("content-type", "application/json")
				.end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
	}

	protected static void notImplemented(RoutingContext context) {
		context.response().setStatusCode(501).putHeader("content-type", "application/json")
				.end(new JsonObject().put("message", "not_implemented").encodePrettily());
	}

	protected static void badGateway(Throwable ex, RoutingContext context) {
		ex.printStackTrace();
		context.response().setStatusCode(502).putHeader("content-type", "application/json")
				.end(new JsonObject().put("error", "bad_gateway").encodePrettily());
	}

	protected static void serviceUnavailable(RoutingContext context) {
		context.fail(503);
	}

	protected static void serviceUnavailable(RoutingContext context, Throwable ex) {
		context.response().setStatusCode(503).putHeader("content-type", "application/json")
				.end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
	}

	protected static void serviceUnavailable(RoutingContext context, String cause) {
		context.response().setStatusCode(503).putHeader("content-type", "application/json")
				.end(new JsonObject().put("error", cause).encodePrettily());
	}
}
