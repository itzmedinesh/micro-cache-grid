package com.itzmeds.cache;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.cache.integration.CacheLoader;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.itzmeds.cache.config.CacheConfig;
import com.itzmeds.cache.config.DataGridConfig;

/**
 * The class exposes operations to initialize apache-ignite data grid engine and
 * provides a template to perform cache lookups and insert/updates
 * 
 * @author itzmeds
 *
 */
public class CacheGridConfigurator implements CacheGridConstants {

	private static Ignite ignite;

	private static DataGridConfig dataGridConfig;

	private static ServiceFactory cacheServiceLocator;

	private static Map<String, CacheTemplate<?>> cacheTemplateMap = new HashMap<String, CacheTemplate<?>>();

	private static final Logger LOGGER = LogManager.getLogger(CacheGridConfigurator.class);

	/**
	 * Method to initialize apache ignite data grid
	 * 
	 * @param dataGrid
	 *            - values to configure,initialize ignite data grid
	 * @throws CacheConfigurationException
	 *             - thrown when configuration values are invalid
	 */
	public static void configureIgniteDataGrid(DataGridConfig dataGrid) throws CacheConfigurationException {
		dataGridConfig = dataGrid;
		final IgniteConfiguration igniteConfig = new IgniteConfiguration();
		igniteConfig.setGridName(dataGridConfig.getGridName());
		igniteConfig.setMetricsLogFrequency(dataGridConfig.getMetricLogFrequency());
		TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
		discoverySpi.setLocalPort(dataGridConfig.getGridDiscoveryPort());

		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();

		ipFinder.setAddresses(
				Arrays.asList(dataGridConfig.getGridHostname() + ":" + dataGridConfig.getGridDiscoveryPort() + ".."
						+ (dataGridConfig.getGridDiscoveryPort() + GRID_CLUSTER_PORT_RANGE)));
		discoverySpi.setIpFinder(ipFinder);

		TcpCommunicationSpi commSpi = new TcpCommunicationSpi();
		commSpi.setLocalPort(dataGridConfig.getGridCommunicationPort());

		igniteConfig.setDiscoverySpi(discoverySpi);

		igniteConfig.setCommunicationSpi(commSpi);

		LOGGER.info("Starting ignite data grid service...");
		ignite = Ignition.start(igniteConfig);
	}

	/**
	 * Method to initialize apache-ignite data grid
	 * 
	 * @param dataGrid
	 *            - values to configure,initialize apache ignite data grid
	 * @param serviceLocator
	 *            - factory implementation to lookup cache loader class dependencies
	 * @throws CacheConfigurationException
	 *             - thrown when configuration values are invalid
	 */
	public static void configureIgniteDataGrid(DataGridConfig dataGrid, ServiceFactory serviceLocator)
			throws CacheConfigurationException {
		cacheServiceLocator = serviceLocator;
		configureIgniteDataGrid(dataGrid);
	}

	/**
	 * Provides a cache template object used to perform cache lookups, inserts or
	 * updates
	 * 
	 * @param cacheConfigKey
	 *            - ignite data grid identifier
	 * @return CacheTemplate object to perform cache lookups, inserts or updates
	 * @throws CacheConfigurationException
	 *             - thrown when the data grid identifier provided is invalid
	 */
	@SuppressWarnings("unchecked")
	public static <T> CacheTemplate<T> getCacheLookupTemplate(String cacheConfigKey)
			throws CacheConfigurationException {
		if (cacheTemplateMap.get(cacheConfigKey) != null) {
			LOGGER.debug(
					"Cache template with the key : {} already exists; return template from cache : " + cacheConfigKey);
			return (CacheTemplate<T>) cacheTemplateMap.get(cacheConfigKey);
		} else {
			CacheConfig cacheConfig = dataGridConfig.getCacheConfig().get(cacheConfigKey);
			CacheLoader<String, T> cacheLoader = null;

			if (cacheServiceLocator != null)
				cacheLoader = (CacheLoader<String, T>) CacheFactoryBuilder
						.getCacheLoader(cacheServiceLocator, cacheConfig).create();
			else
				cacheLoader = (CacheLoader<String, T>) CacheFactoryBuilder.getCacheLoader(cacheConfig).create();

			CacheTemplate<T> cacheTempate = new CacheTemplateImpl<T>(ignite, cacheLoader, cacheConfigKey, cacheConfig);
			cacheTemplateMap.put(cacheConfigKey, cacheTempate);
			return cacheTempate;
		}
	}
}
