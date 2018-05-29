package com.itzmeds.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.itzmeds.cache.config.CacheGridConfiguration;
import com.itzmeds.cache.utility.ServiceFactoryTestImpl;
import com.itzmeds.cache.utility.TestConfiguration;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class BasicCacheOperationTest {

	@BeforeClass
	public static void setUp() throws Exception {
		CacheGridConfiguration testConfiguration = (CacheGridConfiguration) TestConfiguration.load();
		CacheGridConfigurator.configureIgniteDataGrid(testConfiguration.getCacheGridConfig(),
				new ServiceFactoryTestImpl());
	}

	@Test
	public void cacheOperationIntegrationTest() throws Exception {

		CacheTemplate<String> cacheTemplateTemp = CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING");

		Assert.assertEquals("value1", cacheTemplateTemp.lookup("key1"));
		Assert.assertEquals("value2", cacheTemplateTemp.lookup("key2"));
		Assert.assertEquals("value2", cacheTemplateTemp.lookup("key2"));

		try {
			cacheTemplateTemp.lookup("none");
			Assert.fail();
		} catch (CacheLookupException e) {
			Assert.assertTrue(e.getMessage().contains("Cache lookup failed"));
		}
	}

	@Test
	public void cacheOperationIntegrationTestNoReadThru() throws Exception {

		CacheTemplate<String> cacheTemplateTemp = CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING_V2");

		try {
			cacheTemplateTemp.lookup("key1");
			Assert.fail();
		} catch (CacheLookupException e) {
			Assert.assertTrue(e.getMessage().contains("Cache lookup failed"));
		}
	}

	@Test
	public void cacheOperationIntegrationTestVariant1() throws Exception {
		CacheTemplate<String> cacheTemplateTemp = CacheGridConfigurator.getCacheLookupTemplate("TEST_MAPPING_1");
		try {
			cacheTemplateTemp.lookup("none");
			Assert.fail();
		} catch (CacheLookupException e) {
			Assert.assertTrue(e.getMessage().contains("Cache lookup failed"));
		}
	}

	@Test
	public void cacheOperationIntegrationTestVariant2() throws Exception {
		CacheTemplate<String> cacheTemplateTemp = CacheGridConfigurator.getCacheLookupTemplate("TEST_MAPPING_2");
		try {
			cacheTemplateTemp.lookup("none");
			Assert.fail();
		} catch (CacheLookupException e) {
			Assert.assertTrue(e.getMessage().contains("Cache lookup failed"));
		}
	}

	@Test
	public void cacheOperationIntegrationTestCNFException() throws Exception {
		CacheTemplate<String> cacheTemplateTemp = null;
		try {
			cacheTemplateTemp = CacheGridConfigurator.getCacheLookupTemplate("TEST_MAPPING_3");
			Assert.fail();
		} catch (CacheConfigurationException e) {
			Assert.assertTrue(e.getMessage().contains("Cache configuration exception : "));
		}
	}

	@Test
	public void cacheOperationLoadAllIntegrationTest() throws Exception {
		CacheTemplate<String> cacheTemplate = CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING");

		Set<String> keys = new HashSet<String>();
		keys.add("key1");
		keys.add("key2");
		keys.add("key3");
		keys.add("key4");
		keys.add("key5");

		Assert.assertEquals(keys.size(), cacheTemplate.lookup(keys).size());

		keys = new HashSet<String>();

		try {
			cacheTemplate.lookup(keys);
		} catch (CacheLookupException e) {
			Assert.assertTrue(e.getMessage().contains("Cache lookup failed"));
		}

		keys = new HashSet<String>();

		keys.add("key1");
		keys.add("key2");
		keys.add("key22");

		Assert.assertEquals(2, cacheTemplate.lookup(keys).size());
	}

	@Test
	public void cacheOperationLoadAllIntegrationTestWithReadThru() throws Exception {
		CacheTemplate<String> cacheTemplate = CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING");

		Set<String> keys = new HashSet<String>();
		keys.add("key100");
		keys.add("key200");

		Assert.assertEquals(keys.size(), cacheTemplate.lookup(keys).size());

		keys = new HashSet<String>();

		keys.add("key700");
		keys.add("key800");

		try {
			cacheTemplate.lookup(keys);
		} catch (CacheLookupException e) {
			Assert.assertTrue(e.getMessage().contains("Cache lookup failed"));
		}
	}

	@Test
	public void cacheOperationLoadAllIntegrationTestNoReadThru() throws Exception {
		CacheTemplate<String> cacheTemplate = CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING_V2");

		Set<String> keys = new HashSet<String>();
		keys.add("key1");
		keys.add("key2");
		keys.add("key3");
		keys.add("key4");
		keys.add("key5");

		try {
			cacheTemplate.lookup(keys);
		} catch (CacheLookupException e) {
			Assert.assertTrue(e.getMessage().contains("Cache lookup failed"));
		}

		cacheTemplate.insertOrUpdate("key1000", "value1000");
		cacheTemplate.insertOrUpdate("key2000", "value2000");

		keys = new HashSet<String>();
		keys.add("key1000");
		keys.add("key2000");
		keys.add("key400");
		keys.add("key500");
		Assert.assertEquals(2, cacheTemplate.lookup(keys).size());

	}

	@Test
	public void insertOrUpdateTest() throws CacheConfigurationException {

		CacheTemplate<String> cacheTemplate = CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING");

		try {
			cacheTemplate.insertOrUpdate("key45", "value45");

			Assert.assertEquals("value45", cacheTemplate.lookup("key45"));
		} catch (CacheWriteException e1) {
			Assert.fail();
		} catch (CacheLookupException e) {
			Assert.fail();
		}

	}

	@Test
	public void insertOrUpdateListTest() throws CacheConfigurationException {
		CacheTemplate<String> cacheTemplate = CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING");
		try {
			Map<String, String> keysList = new HashMap<String, String>();
			keysList.put("key45", "value45");
			keysList.put("key46", "value46");
			cacheTemplate.insertOrUpdate(keysList);

			Set<String> keySet = new HashSet<String>();
			keySet.add("key45");
			keySet.add("key46");

			Assert.assertEquals(2, cacheTemplate.lookup(keySet).size());
		} catch (CacheWriteException e1) {
			Assert.fail();
		} catch (CacheLookupException e) {
			Assert.fail();
		}
	}

	@Test
	public void insertOrUpdateListTest_Exception() throws CacheConfigurationException {
		CacheTemplate<String> cacheTemplate = CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING");
		try {
			cacheTemplate.insertOrUpdate(null);
		} catch (CacheWriteException e1) {
			Assert.assertTrue(true);
		}

		try {
			cacheTemplate.insertOrUpdate("Key1", null);
		} catch (CacheWriteException e1) {
			Assert.assertTrue(true);
		}

		try {
			cacheTemplate.insertOrUpdate(new HashMap());
		} catch (CacheWriteException e1) {
			Assert.assertTrue(true);
		}

		try {
			cacheTemplate.insertOrUpdate("Key1", "");
		} catch (CacheWriteException e1) {
			Assert.assertTrue(true);
		}

		try {
			cacheTemplate.insertOrUpdate("", "value1");
		} catch (CacheWriteException e1) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void cacheModeConfigExceptionTest() {
		try {
			CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING_MODE_EXCEPTION");
		} catch (CacheConfigurationException e) {
			Assert.assertTrue(e.getMessage().contains("Unsupported cache mode"));
		}

	}

	@Test
	public void cacheExpiryPolicyConfigExceptionTest() {
		try {
			CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING_EXPIRY_POLICY_EXCEPTION");
		} catch (CacheConfigurationException e) {
			System.out.println(e);
			Assert.assertTrue(e.getMessage().contains("Unsupported cache expiry policy"));
		}
	}

	@Test
	public void cacheExpiryConfigExceptionTest() {
		try {
			CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING_EXPIRY_EXCEPTION");
		} catch (CacheConfigurationException e) {
			System.out.println(e);
			Assert.assertTrue(e.getMessage().contains("Unsupported cache expiry duration"));
		}
	}

	@Test
	public void cacheLoaderConfigExceptionTest() {
		try {
			CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING_CNF_EXCEPTION");
		} catch (CacheConfigurationException e) {
			System.out.println(e);
			Assert.assertTrue(e.getMessage().contains("Unable to load the cache loader class"));
		}
	}

}
