package com.vertx.starter.routes;

import com.vertx.starter.controllers.*;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class Routes {
	Router router;

	public Routes(Vertx vertx) {
		router = Router.router(vertx);
		
		//routes 
		router.get("/").handler(IndexController::handler);
	}
	
	public Router getRoutes() {
		return this.router;
	}

}
