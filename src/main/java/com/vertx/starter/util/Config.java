package com.vertx.starter.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.vertx.core.json.JsonObject;

public class Config {

	private static JsonObject jsonObj = null;

	public static JsonObject config() {
		try {
			if (jsonObj != null)
				return jsonObj;

			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			String jsonStr = new String(Files.readAllBytes(Paths.get(classloader.getResource("config.json").getPath())));
			return new JsonObject(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
			return new JsonObject();
		}
	}
}
