package com.vertx.starter.util;

import java.util.HashSet;
import java.util.Set;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

public class CORS {
	 public static void enableCorsSupport(Router router) {
		    Set<String> allowHeaders = new HashSet<>();
		    allowHeaders.add("x-requested-with");
		    allowHeaders.add("Access-Control-Allow-Origin");
		    allowHeaders.add("origin");
		    allowHeaders.add("Content-Type");
		    allowHeaders.add("accept");
		    
		    Set<HttpMethod> allowMethods = new HashSet<>();
		    allowMethods.add(HttpMethod.GET);
		    allowMethods.add(HttpMethod.PUT);
		    allowMethods.add(HttpMethod.OPTIONS);
		    allowMethods.add(HttpMethod.POST);
		    allowMethods.add(HttpMethod.DELETE);
		    allowMethods.add(HttpMethod.PATCH);

		    router.route().handler(io.vertx.ext.web.handler.CorsHandler.create("*")
		      .allowedHeaders(allowHeaders)
		      .allowedMethods(allowMethods));
		  }

}
