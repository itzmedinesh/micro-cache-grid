package com.itzmeds.cache.utility;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.itzmeds.cache.config.CacheGridConfiguration;

public class TestConfiguration extends CacheGridConfiguration {

	private static final Logger LOGGER = LogManager
			.getLogger(TestConfiguration.class);

	public TestConfiguration() {
	}

	public static CacheGridConfiguration load() {

		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

		try {
			return objectMapper.readValue(new File("test.yml"),
					CacheGridConfiguration.class);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("Error in TestConfiguration", e);
			return null;
		}
	}

}
