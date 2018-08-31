package com.vertx.starter;

import com.vertx.starter.models.User;
import com.vertx.starter.routes.Routes;
import com.vertx.starter.util.CORS;
import com.vertx.starter.util.Constant;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {
	@Override
	public void start(Future<Void> startFuture) {
		Routes routes = new Routes(vertx);

		CORS.enableCorsSupport(routes.getRoutes());

		Future<Void> steps = dbInit().compose(v -> createHttpServer(routes.getRoutes(),
				config().getString("base.host"), config().getInteger("base.port")));
		steps.setHandler(ar -> {
			if (ar.succeeded()) {
				Constant.LOGGER.info("Server started on http://" + config().getString("base.host") + ":"
						+ config().getInteger("base.port"));
				startFuture.complete();
			} else {
				Constant.LOGGER.error("Server could not start", ar.cause());
				startFuture.fail(ar.cause());
			}
		});
	}

	protected Future<Void> createHttpServer(Router router, String host, int port) {
		Future<HttpServer> httpServerFuture = Future.future();
		vertx.createHttpServer().requestHandler(router::accept).listen(port, host, httpServerFuture.completer());
		return httpServerFuture.mapEmpty();
	}

	private Future<Void> dbInit() {
		Future<Void> future = Future.future();
		JsonObject postgreSQLClientConfig = new JsonObject().put("host", config().getString("db.host"));
		postgreSQLClientConfig.put("post", config().getString("db.post"));
		postgreSQLClientConfig.put("username", config().getString("db.username"));
		postgreSQLClientConfig.put("password", config().getString("db.password"));
		postgreSQLClientConfig.put("database", config().getString("db.database"));

		Constant.postgreSQLClient = PostgreSQLClient.createShared(vertx, postgreSQLClientConfig);

		Constant.postgreSQLClient.getConnection(ar -> {
			if (ar.failed()) {
				Constant.LOGGER.error("Could not open a database", ar.cause());
				future.fail(ar.cause());
			} else if (ar.succeeded()) {
				Constant.LOGGER.error("Database connected");
				
				(new User()).create();
				future.complete();
			}
		});

		return future;
	}

	@Override
	public void stop() throws Exception {
		Constant.postgreSQLClient.close();
	}

}
