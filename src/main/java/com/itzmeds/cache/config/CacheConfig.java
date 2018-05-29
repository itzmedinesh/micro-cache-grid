package com.itzmeds.cache.config;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration for initialization of individual cache regions within the data
 * grid
 * 
 * @author itzmeds
 */
@SuppressWarnings("serial")
public class CacheConfig implements Serializable {

	/**
	 * Accepted values 'LOCAL', 'PARTITIONED', 'REPLICATED' <br>
	 * LOCAL - In this mode caches residing on different grid nodes will not know
	 * about each other <br>
	 * PARTITIONED - In this mode the overall key set will be divided into
	 * partitions and all partitions will be split equally between participating
	 * nodes <br>
	 * REPLICATED - In this mode the overall key set will be divided into partitions
	 * and all partitions will be split equally between participating nodes
	 */
	@JsonProperty("cache.mode")
	private String mode;

	/**
	 * Accepted values 'CREATED' , 'ACCESSED' , 'MODIFIED' , 'TOUCHED' <br>
	 * CREATED - expires after the configured expiry duration starting from the time
	 * the entry was created <br>
	 * ACCESSED - expires after the configured expiry duration starting from the
	 * time the entry was last accessed <br>
	 * MODIFIED - expires after the configured expiry duration starting from the
	 * time the entry was last modified <br>
	 * TOUCHED - expires after the configured expiry duration starting from the time
	 * the entry was last read or modified
	 */
	@JsonProperty("cache.expirypolicy")
	private String expiryPolicy;

	/**
	 * Accepted values 'ETERNAL', 'ONE_DAY', 'ONE_HOUR','THIRTY_MINUTES',
	 * TWENTY_MINUTES, 'TEN_MINUTES', 'FIVE_MINUTES', 'ONE_MINUTE', 'ZERO'
	 */
	@JsonProperty("cache.expiryduration")
	private String expiryDuration;

	/**
	 * Accepted values 'LEAST_RECENTLY_USED', 'FIRST_IN_FIRST_OUT' <br>
	 * LEAST_RECENTLY_USED - removes the least recently used entry from the cache
	 * FIRST_IN_FIRST_OUT - removes the entry in FIFO basis
	 */
	@JsonProperty("cache.evictionpolicy")
	private String evictionpolicy;

	/**
	 * Maximum size of the cache after which evication starts
	 */
	@JsonProperty("cache.maxsize")
	private Integer maxsize;

	/**
	 * Cache loader implementation class used to load values into cache when
	 * 'read-through' is enabled
	 */
	@JsonProperty("cache.loader.class")
	private String loaderClass;

	/**
	 * Cache loader class constructor arguments
	 */
	@JsonProperty("cache.loader.class.parameters")
	private ServiceLookupConfig[] loaderClassParameters;

	/**
	 * When set to 'true', uses the cache loader class to load the value for the
	 * given key from the backend data store
	 */
	@JsonProperty("cache.readthrough")
	private boolean readThroughEnabled;

	/**
	 * When set to 'true', opens up a port on the cache jvm for remote management
	 */
	@JsonProperty("cache.management.enabled")
	private boolean managementEnabled;

	public String getMode() {
		return mode;
	}

	public String getExpiryPolicy() {
		return expiryPolicy;
	}

	public String getExpiryDuration() {
		return expiryDuration;
	}

	public String getEvictionpolicy() {
		return evictionpolicy;
	}

	public Integer getMaxsize() {
		return maxsize;
	}

	public String getLoaderClass() {
		return loaderClass;
	}

	public ServiceLookupConfig[] getLoaderClassParameters() {
		return loaderClassParameters;
	}

	public boolean isReadThroughEnabled() {
		return readThroughEnabled;
	}

	public boolean isManagementEnabled() {
		return managementEnabled;
	}

	@Override
	public String toString() {
		return "CacheConfig [mode=" + mode + ", expiryDuration=" + expiryDuration + ", loaderClass=" + loaderClass
				+ ", loaderClassParameters=" + Arrays.toString(loaderClassParameters) + ", readThroughEnabled="
				+ readThroughEnabled + ", managementEnabled=" + managementEnabled + ", evictionpolicy=" + evictionpolicy
				+ "]";
	}

}
