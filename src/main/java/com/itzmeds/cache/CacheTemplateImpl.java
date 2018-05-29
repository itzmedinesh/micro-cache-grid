package com.itzmeds.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.ModifiedExpiryPolicy;
import javax.cache.expiry.TouchedExpiryPolicy;
import javax.cache.integration.CacheLoader;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.eviction.fifo.FifoEvictionPolicy;
import org.apache.ignite.cache.eviction.lru.LruEvictionPolicy;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.itzmeds.cache.config.CacheConfig;

public class CacheTemplateImpl<M> implements CacheTemplate<M> {

	private static final Logger LOGGER = LogManager.getLogger(CacheTemplateImpl.class);

	IgniteCache<String, M> igniteCache;
	CacheLoader<String, M> cacheLoader;
	CacheConfig cacheConfig;

	public CacheTemplateImpl(Ignite ignite, CacheLoader<String, M> cacheLoader, String cacheConfigKey,
			CacheConfig cacheConfig) throws CacheConfigurationException {
		this.cacheConfig = cacheConfig;
		this.cacheLoader = cacheLoader;
		this.igniteCache = ignite.createCache(createIgniteConfiguration(cacheConfigKey));
	}

	@Override
	public M lookup(String key) throws CacheLookupException {
		M value = igniteCache.get(key);
		boolean lookupFailed = false;

		if (value == null) {
			if (cacheConfig.isReadThroughEnabled()) {
				value = cacheLoader.load(key);
				if (value == null) {
					lookupFailed = true;
				} else {
					igniteCache.put(key, value);
				}
			} else {
				lookupFailed = true;
			}
		}

		if (lookupFailed) {
			LOGGER.error("Cache lookup failed, value not found for key :" + key);
			throw new CacheLookupException("Cache lookup failed, value not found for key :" + key);
		}

		return value;
	}

	public Map<String, M> lookup(Set<String> keys) throws CacheLookupException {

		boolean lookupFailed = false;

		if (keys == null || keys != null && keys.isEmpty()) {
			throw new CacheLookupException("Cache lookup failed : Keys : " + keys);
		}

		Map<String, M> keyValues = igniteCache.getAll(keys);

		if (MapUtils.isEmpty(keyValues)) {
			if (cacheConfig.isReadThroughEnabled()) {
				LOGGER.info("Cache miss for keys : " + keys + " calling loadAll()");
				keyValues = cacheLoader.loadAll(keys);
				LOGGER.info("Data loaded from  store : " + keyValues);
				if (MapUtils.isEmpty(keyValues)) {
					lookupFailed = true;
				} else {
					igniteCache.putAll(keyValues);
				}
			} else {
				lookupFailed = true;
			}
		} else if (keyValues.size() < keys.size()) {
			if (cacheConfig.isReadThroughEnabled()) {
				Set<String> cacheMissKeys = new HashSet<String>();

				for (String key : keys) {
					if (keyValues.get(key) == null) {
						cacheMissKeys.add(key);
					}
				}
				LOGGER.info("Cache miss for keys : " + cacheMissKeys + " calling loadAll()");
				Map<String, M> cacheMissValues = cacheLoader.loadAll(cacheMissKeys);
				LOGGER.info("Data loaded from store : " + cacheMissValues);

				if (MapUtils.isNotEmpty(cacheMissValues)) {
					igniteCache.putAll(cacheMissValues);
					keyValues.putAll(cacheMissValues);
				}
			}
		}

		if (lookupFailed) {
			LOGGER.error("Cache lookup failed, value not found for keys :" + keys);
			throw new CacheLookupException("Cache lookup failed, values not found for keys :" + keys);
		}

		return keyValues;
	}

	@Override
	public void insertOrUpdate(String key, M value) throws CacheWriteException {
		if (StringUtils.isEmpty(key) || value == null) {
			LOGGER.error("Bad Cache Entries Key: " + key + " Value: " + value);
			throw new CacheWriteException("Bad Cache Entries Key: " + key + " Value: " + value);
		}
		igniteCache.put(key, value);
	}

	@Override
	public void insertOrUpdate(Map<String, M> data) throws CacheWriteException {
		if (data != null && !data.isEmpty()) {
			igniteCache.putAll(data);
		} else {
			LOGGER.error("Bad Cache Entries ; Input Data: " + data);
			throw new CacheWriteException("Bad Cache Entries ; Input Data: " + data);
		}
	}

	private CacheConfiguration<String, M> createIgniteConfiguration(String cacheConfigId)
			throws CacheConfigurationException {

		LOGGER.info("Configuring cache for cache config id " + cacheConfigId);
		CacheConfiguration<String, M> igniteCacheConfig = new CacheConfiguration<String, M>();

		igniteCacheConfig.setName(cacheConfigId);

		if (IGNITE_CACHE_MODE.get(cacheConfig.getMode()) == null) {
			throw new CacheConfigurationException(
					"Unsupported cache mode, currently supported modes : " + IGNITE_CACHE_MODE);
		} else {
			igniteCacheConfig.setCacheMode(IGNITE_CACHE_MODE.get(cacheConfig.getMode()));
		}

		if (!CACHE_EVICTION_POLICY.contains(cacheConfig.getEvictionpolicy())) {
			throw new CacheConfigurationException(
					"Unsupported cache eviction policy, currently supported policy: " + CACHE_EVICTION_POLICY);
		} else {

			switch (cacheConfig.getEvictionpolicy()) {

			case LEAST_RECENTLY_USED:
				LruEvictionPolicy<Object, Object> lruEvictionPolicy = new LruEvictionPolicy<Object, Object>();
				lruEvictionPolicy.setMaxSize(cacheConfig.getMaxsize());
				igniteCacheConfig.setEvictionPolicy(lruEvictionPolicy);
				break;

			case FIRST_IN_FIRST_OUT:
				FifoEvictionPolicy<Object, Object> fifoEvictionPolicy = new FifoEvictionPolicy<Object, Object>();
				fifoEvictionPolicy.setMaxSize(cacheConfig.getMaxsize());
				igniteCacheConfig.setEvictionPolicy(fifoEvictionPolicy);
				break;
			default:
				break;

			}
		}

		if (CACHE_EXPIRY_DURATION.get(cacheConfig.getExpiryDuration()) == null) {
			throw new CacheConfigurationException(
					"Unsupported cache expiry duration, currently supported durations : " + CACHE_EXPIRY_DURATION);
		} else {

			switch (cacheConfig.getExpiryPolicy()) {

			case TOUCHED_EXPIRY_POLICY:
				igniteCacheConfig.setExpiryPolicyFactory(
						TouchedExpiryPolicy.factoryOf(CACHE_EXPIRY_DURATION.get(cacheConfig.getExpiryDuration())));
				break;

			case MODIFIED_EXPIRY_POLICY:
				igniteCacheConfig.setExpiryPolicyFactory(
						ModifiedExpiryPolicy.factoryOf(CACHE_EXPIRY_DURATION.get(cacheConfig.getExpiryDuration())));
				break;

			case ACCESSED_EXPIRY_POLICY:
				igniteCacheConfig.setExpiryPolicyFactory(
						AccessedExpiryPolicy.factoryOf(CACHE_EXPIRY_DURATION.get(cacheConfig.getExpiryDuration())));
				break;

			case CREATED_EXPIRY_POLICY:
				igniteCacheConfig.setExpiryPolicyFactory(
						CreatedExpiryPolicy.factoryOf(CACHE_EXPIRY_DURATION.get(cacheConfig.getExpiryDuration())));
				break;
			default:
				throw new CacheConfigurationException(
						"Unsupported cache expiry policy, currently supported durations : " + EXPIRY_POLICIES);
			}

		}

		igniteCacheConfig.setReadThrough(false);

		igniteCacheConfig.setManagementEnabled(cacheConfig.isManagementEnabled());

		igniteCacheConfig.setStoreByValue(true);

		return igniteCacheConfig;
	}

}
