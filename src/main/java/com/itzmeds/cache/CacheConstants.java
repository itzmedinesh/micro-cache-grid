package com.itzmeds.cache;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.cache.expiry.Duration;

import org.apache.ignite.cache.CacheMode;

public interface CacheConstants {
	String CREATED_EXPIRY_POLICY = "CREATED";
	String ACCESSED_EXPIRY_POLICY = "ACCESSED";
	String MODIFIED_EXPIRY_POLICY = "MODIFIED";
	String TOUCHED_EXPIRY_POLICY = "TOUCHED";
	String LEAST_RECENTLY_USED = "LEAST_RECENTLY_USED";
	String FIRST_IN_FIRST_OUT = "FIRST_IN_FIRST_OUT";

	public List<String> EXPIRY_POLICIES = Arrays.asList(CREATED_EXPIRY_POLICY, ACCESSED_EXPIRY_POLICY,
			MODIFIED_EXPIRY_POLICY, TOUCHED_EXPIRY_POLICY);

	@SuppressWarnings("serial")
	Map<String, CacheMode> IGNITE_CACHE_MODE = Collections.unmodifiableMap(new HashMap<String, CacheMode>() {
		{
			put("LOCAL", CacheMode.LOCAL);
			put("PARTITIONED", CacheMode.PARTITIONED);
			put("REPLICATED", CacheMode.REPLICATED);
		}
	});

	@SuppressWarnings("serial")
	Map<String, Duration> CACHE_EXPIRY_DURATION = Collections.unmodifiableMap(new HashMap<String, Duration>() {
		{
			put("ETERNAL", Duration.ETERNAL);
			put("ONE_DAY", Duration.ONE_DAY);
			put("ONE_HOUR", Duration.ONE_HOUR);
			put("THIRTY_MINUTES", Duration.THIRTY_MINUTES);
			put("TWENTY_MINUTES", Duration.TWENTY_MINUTES);
			put("TEN_MINUTES", Duration.TEN_MINUTES);
			put("FIVE_MINUTES", Duration.FIVE_MINUTES);
			put("ONE_MINUTE", Duration.ONE_MINUTE);
			put("ZERO", Duration.ZERO);
		}
	});
	public List<String> CACHE_EVICTION_POLICY = Arrays.asList(LEAST_RECENTLY_USED, FIRST_IN_FIRST_OUT);
}
