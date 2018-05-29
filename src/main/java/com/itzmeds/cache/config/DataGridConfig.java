package com.itzmeds.cache.config;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Ignite cache grid configuration class
 * 
 * @author itzmeds
 */
@SuppressWarnings("serial")
public class DataGridConfig implements Serializable {

	/**
	 * Unique name to identify the ignite data grid
	 */
	@JsonProperty("grid.name")
	private String gridName;

	/**
	 * Hostname of IP address of the machine on which the data grid will be running
	 */
	@JsonProperty("grid.hostname")
	private String gridHostname;

	/**
	 * Port used to communicate with data grids in distributed mode
	 */
	@JsonProperty("grid.communication.port")
	private int gridCommunicationPort;

	/**
	 * Port exposed by the grid for discovery when it is running in distributed mode
	 */
	@JsonProperty("grid.discovery.port")
	private int gridDiscoveryPort;

	/**
	 * Time interval to log data grid metrics. value 0 indicates never log metrics
	 */
	@JsonProperty("grid.metricslog.frequency")
	private int metricLogFrequency;

	/**
	 * Configuration for initialization of individual cache regions within the data
	 * grid
	 */
	@JsonProperty("grid.cache")
	private Map<String, CacheConfig> cacheConfig;

	public String getGridName() {
		return gridName;
	}

	public String getGridHostname() {
		return gridHostname;
	}

	public int getGridCommunicationPort() {
		return gridCommunicationPort;
	}

	public int getGridDiscoveryPort() {
		return gridDiscoveryPort;
	}

	public int getMetricLogFrequency() {
		return metricLogFrequency;
	}

	public Map<String, CacheConfig> getCacheConfig() {
		return cacheConfig;
	}

	@Override
	public String toString() {
		return "DataGridConfig [gridName=" + gridName + ", gridHostname=" + gridHostname + ", gridCommunicationPort="
				+ gridCommunicationPort + ", gridDiscoveryPort=" + gridDiscoveryPort + ", metricLogFrequency="
				+ metricLogFrequency + ", cacheConfig=" + cacheConfig + "]";
	}

}
