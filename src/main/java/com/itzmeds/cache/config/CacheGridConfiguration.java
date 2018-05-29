package com.itzmeds.cache.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration class that holds values to initialize apache-ignite cache grid
 * 
 * @author itzmeds
 */
public class CacheGridConfiguration {

	/**
	 * Ignite data grid configuration object
	 */
	@JsonProperty("cache.grid")
	DataGridConfig dataGridConfig;

	public DataGridConfig getCacheGridConfig() {
		return dataGridConfig;
	}

	@Override
	public String toString() {
		return "CacheGridConfiguration [dataGridConfig=" + dataGridConfig + "]";
	}

}
