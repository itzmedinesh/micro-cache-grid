package com.itzmeds.cache.loader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SampleCacheLoader implements CacheLoader<String, String> {

	private static final Logger LOGGER = LogManager.getLogger(SampleCacheLoader.class);

	@SuppressWarnings("unused")
	private ObjectMapper objectMapper;

	@SuppressWarnings("serial")
	private static final Map<String, String> CACHE_DATA_SOURCE = Collections
			.unmodifiableMap(new HashMap<String, String>() {
				{
					put("key1", "value1");
					put("key2", "value2");
					put("key3", "value3");
					put("key4", "value4");
					put("key5", "value5");
					put("key100", "value100");
					put("key200", "value200");
					put("key300", "value300");
					put("key400", "value400");
					put("key500", "value500");
				}
			});

	public SampleCacheLoader() {
	}

	public SampleCacheLoader(@SuppressWarnings("rawtypes") Iterator initTest) {
	}

	public SampleCacheLoader(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public String load(String key) throws CacheLoaderException {
		LOGGER.info("Cache Miss for key : {} Retrieving from external data source..." + key);
		return CACHE_DATA_SOURCE.get(key);
	}

	@Override
	public Map<String, String> loadAll(Iterable<? extends String> keys) throws CacheLoaderException {
		Map<String, String> cacheEntries = new HashMap<String, String>();
		for (String key : keys) {
			if (load(key) != null) {
				cacheEntries.put(key, load(key));
			}
		}
		return cacheEntries;
	}

}
