package com.itzmeds.cache.config;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Cache loader class constructor arguments that can be looked up from the
 * provided service factory implementation class
 *
 * @author itzmeds
 *
 */
@SuppressWarnings("serial")
public class ServiceLookupConfig implements Serializable {

	public ServiceLookupConfig() {
	}

	/**
	 * CLASS name used to retrieve service class singletons from the service factory
	 */
	@JsonProperty("service.type")
	private String serviceType;

	/**
	 * Used to retrieve named service class singletons from the service factory
	 */
	@JsonProperty("service.name")
	private String serviceName;

	public String getServiceType() {
		return serviceType;
	}

	public String getServiceName() {
		return serviceName;
	}

	@Override
	public String toString() {
		return "ServiceLookupConfig [serviceType=" + serviceType + ", serviceName=" + serviceName + "]";
	}

}
