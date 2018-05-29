package com.itzmeds.cache;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.itzmeds.cache.config.CacheGridConfiguration;
import com.itzmeds.cache.utility.TestConfiguration;

@Ignore
public class BasicCacheOperationTest2 {

	@BeforeClass
	public static void setUp() throws Exception {
		CacheGridConfiguration testConfiguration = (CacheGridConfiguration) TestConfiguration.load();
		CacheGridConfigurator.configureIgniteDataGrid(testConfiguration.getCacheGridConfig());
	}

	@Test
	public void cacheOperationIntegrationTest() throws Exception {

		CacheTemplate<String> cacheTemplateTemp = CacheGridConfigurator.getCacheLookupTemplate("TEST_MAPPING_1");

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

}
