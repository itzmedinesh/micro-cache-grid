package com.itzmeds.cache.utility;

import java.util.HashMap;
import java.util.Iterator;

import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itzmeds.cache.ServiceFactory;
import com.itzmeds.cache.ServiceLocatorException;

public class ServiceFactoryTestImpl implements ServiceFactory {

	static org.glassfish.hk2.api.ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance()
			.create("TestLocator");

	public ServiceFactoryTestImpl() {

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		final Iterator<String> testInjection1 = new HashMap<String, String>().keySet().iterator();

		ServiceLocatorUtilities.bind(serviceLocator, new AbstractBinder() {
			@Override
			protected void configure() {
				bind(objectMapper);
				bind(testInjection1).named("testInject1");
			}
		});
	}

	@Override
	public <T> T getService(Class<T> contractOrImpl) throws ServiceLocatorException {
		return serviceLocator.getService(contractOrImpl);
	}

	@Override
	public <T> T getService(Class<T> contractOrImpl, String name) throws ServiceLocatorException {
		return serviceLocator.getService(contractOrImpl, name);
	}

}
