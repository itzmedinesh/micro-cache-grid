package com.itzmeds.cache;

import java.util.Map;
import java.util.Set;

/**
 * Exposes methods perform cache lookups, inserts or updates
 * 
 * @author itzmeds
 *
 * @param <M>
 *            - represents the cache entry value TYPE
 */
public interface CacheTemplate<M> extends CacheConstants {

	/**
	 * Method to retrieve single value from the cache matching the input key
	 * 
	 * @param key
	 *            - cache entry lookup key
	 * @return cache entry value of TYPE M
	 * @throws CacheLookupException
	 *             - thrown when none of the cache entries matches the input key
	 */
	public M lookup(String key) throws CacheLookupException;

	/**
	 * Method to retrieve multiple values from the cache matching the input key set
	 * 
	 * @param keys
	 *            - cache entry lookup keys
	 * @return key-value pairs; Key is of TYPE STRING and value is of type M
	 * @throws CacheLookupException
	 *             - thrown when none of the cache entries matches the input key set
	 */
	public Map<String, M> lookup(Set<String> keys) throws CacheLookupException;

	/**
	 * Method to insert or update a single key-value entry in the cache
	 * 
	 * @param key
	 *            - cache entry key
	 * @param value
	 *            - cache entry value of type M
	 * @throws CacheWriteException
	 *             - thrown when the cache entry key or value is invalid
	 */
	public void insertOrUpdate(String key, M value) throws CacheWriteException;

	/**
	 * Method to insert or update multiple key-value entries in the cache
	 * 
	 * @param data
	 *            - key-value pairs; key is of TYPE String and value is of TYPE M
	 * @throws CacheWriteException-
	 *             thrown when the cache entry key set or value set is invalid
	 */
	public void insertOrUpdate(Map<String, M> data) throws CacheWriteException;

}
