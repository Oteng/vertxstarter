package com.vertx.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vertx.starter.routes.Routes;
import com.vertx.starter.util.CORS;
import com.vertx.starter.util.Config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);
	
	@Override
	public void start() {
		Routes routes = new Routes(vertx);
		
		CORS.enableCorsSupport(routes.getRoutes());
		
		Future<Void> server = createHttpServer(routes.getRoutes(), Config.config().getString("base.host"), Config.config().getInteger("base.port"));
		if(server.succeeded()) {
			LOGGER.info("SERVER started at: http://"+ Config.config().getString("base.host") +":"+ Config.config().getInteger("base.port"));
		}else
			LOGGER.info("SERVER error: " + server.cause());
	}

	protected Future<Void> createHttpServer(Router router, String host, int port) {
		Future<HttpServer> httpServerFuture = Future.future();
		vertx.createHttpServer().requestHandler(router::accept).listen(port, host, httpServerFuture.completer());
		return httpServerFuture.map(r -> null);
	}

}
