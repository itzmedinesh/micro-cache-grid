package com.itzmeds.cache;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.cache.configuration.Factory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.itzmeds.cache.config.CacheConfig;

public final class CacheFactoryBuilder {

	private static final Logger LOGGER = LogManager.getLogger(CacheFactoryBuilder.class);

	private static ServiceFactory cacheLoaderDependencyLocator;

	protected static <T extends Serializable> Factory<T> getCacheLoader(CacheConfig cacheConfig)
			throws CacheConfigurationException {
		LOGGER.info("Loading cache loader class : " + cacheConfig.getLoaderClass());

		ClassFactory<T> cacheLoaderFactory;
		try {
			cacheLoaderFactory = new ClassFactory<T>(
					Thread.currentThread().getContextClassLoader().loadClass(cacheConfig.getLoaderClass()),
					cacheConfig);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Error loading cache loader class : " + e);
			throw new CacheConfigurationException("Unable to load the cache loader class : " + e);
		}

		if (!cacheLoaderFactory.validate()) {
			throw new CacheConfigurationException("Cache configuration exception : ");
		}

		return cacheLoaderFactory;
	}

	protected static <T extends Serializable> Factory<T> getCacheLoader(ServiceFactory cacheLoaderDependencyLocatorIn,
			CacheConfig cacheConfig) throws CacheConfigurationException {
		LOGGER.info("Loading cache loader class : " + cacheConfig.getLoaderClass());

		cacheLoaderDependencyLocator = cacheLoaderDependencyLocatorIn;

		return getCacheLoader(cacheConfig);
	}

	@SuppressWarnings("serial")
	public static class ClassFactory<T extends Object> implements Factory<T>, Serializable {

		private CacheConfig cacheConfig;

		private Class<?> cacheLoaderClass;

		public ClassFactory(Class<?> cacheLoaderClass, CacheConfig cacheConfig) {
			this.cacheLoaderClass = cacheLoaderClass;
			this.cacheConfig = cacheConfig;
		}

		public boolean validate() {
			try {
				buildCacheLoaderFactory(cacheConfig);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| ServiceLocatorException e) {
				LOGGER.error("Configuration validation error : " + cacheConfig.toString() + "\n" + e);
				return false;
			}
			return true;
		}

		@Override
		public T create() {
			T cacheLoader = null;
			try {
				cacheLoader = buildCacheLoaderFactory(cacheConfig);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| ServiceLocatorException e) {
				LOGGER.error("Error initializing cache loader class : " + e);
			}

			return cacheLoader;
		}

		@SuppressWarnings("unchecked")
		private T buildCacheLoaderFactory(CacheConfig cacheConfig)
				throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
				IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServiceLocatorException {
			Object instance = null;

			Constructor<?> cacheLoaderConstructor = null;

			int noOfParams = cacheConfig.getLoaderClassParameters() != null
					? cacheConfig.getLoaderClassParameters().length
					: -1;

			if (cacheLoaderDependencyLocator == null && noOfParams > 0) {
				throw new IllegalArgumentException(
						"Service locator invalid/notfound for the cache loader class arguments");
			} else if (cacheLoaderDependencyLocator != null && noOfParams > 0) {
				Class<?> parameterTypes[] = null;

				Object parameterValues[] = null;

				parameterTypes = new Class<?>[noOfParams];

				parameterValues = new Object[noOfParams];

				for (int classindx = 0; classindx < noOfParams; classindx++) {

					parameterTypes[classindx] = Thread.currentThread().getContextClassLoader()
							.loadClass(cacheConfig.getLoaderClassParameters()[classindx].getServiceType());

					if (cacheConfig.getLoaderClassParameters()[classindx].getServiceName() != null) {
						parameterValues[classindx] = cacheLoaderDependencyLocator.getService(parameterTypes[classindx],
								cacheConfig.getLoaderClassParameters()[classindx].getServiceName());
					} else {
						parameterValues[classindx] = cacheLoaderDependencyLocator.getService(parameterTypes[classindx]);
					}

				}
				cacheLoaderConstructor = cacheLoaderClass.getConstructor(parameterTypes);

				instance = cacheLoaderConstructor.newInstance(parameterValues);
			} else {
				cacheLoaderConstructor = cacheLoaderClass.getConstructor();
				instance = cacheLoaderConstructor.newInstance();
			}

			return (T) instance;
		}
	}

}
