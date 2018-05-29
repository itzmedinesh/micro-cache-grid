package com.itzmeds.cache;

public interface ServiceFactory {
	<T> T getService(Class<T> contractOrImpl) throws ServiceLocatorException;

	<T> T getService(Class<T> contractOrImpl, String name) throws ServiceLocatorException;
}
