# micro-cache-grid
Distributed java cache for micro service based systems using apache ignite data grid

# Versions on maven
http://central.maven.org/maven2/com/github/itzmedinesh/micro-cache-grid/

# Sample YAML configuration
***********************************
```html
cache.grid:
  grid.name: "ServiceCacheGrid"
  grid.hostname: "127.0.0.1"
  grid.communication.port: 47100
  grid.discovery.port: 47500
  grid.metricslog.frequency: 0
  grid.cache:
    TPNB_CATID_MAPPING:
      cache.mode: "LOCAL"
      cache.expirypolicy: "TOUCHED"
      cache.expiryduration: "ONE_MINUTE"
      cache.readthrough: true
      cache.management.enabled: false
      cache.evictionpolicy: "FIRST_IN_FIRST_OUT"
      cache.maxsize: 4
      cache.loader.class: "com.itzmeds.cache.loader.SampleCacheLoader"
      cache.loader.class.parameters:
      -
        service.type: "com.fasterxml.jackson.databind.ObjectMapper"
    TPNB_CATID_MAPPING_V2:
      cache.mode: "LOCAL"
      cache.expirypolicy: "MODIFIED"
      cache.expiryduration: "ONE_MINUTE"
      cache.readthrough: false
      cache.management.enabled: false
      cache.evictionpolicy: "FIRST_IN_FIRST_OUT"
      cache.maxsize: 4
      cache.loader.class: "com.itzmeds.cache.loader.SampleCacheLoader"
      cache.loader.class.parameters:
      -
        service.type: "com.fasterxml.jackson.databind.ObjectMapper"
```

# Initialization of apache ignite cache grid and creation of cache access template
************************************************************************************
```html

CacheGridConfigurator.configureIgniteDataGrid(testConfiguration.getCacheGridConfig());

Use when cache loader class constructor has arguments that needs to be fetched from factory classes or DI containers 

CacheGridConfigurator.configureIgniteDataGrid(testConfiguration.getCacheGridConfig(), new ServiceFactoryTestImpl());

CacheTemplate<String> cacheTemplate = CacheGridConfigurator.getCacheLookupTemplate("TPNB_CATID_MAPPING");

```

# lookup, insert or delete from cache using cache template
**********************************************************
```html

Single value lookup

	cacheTemplate.lookup("key1")
	
Multiple values lookup	

	Set<String> keys = new HashSet<String>();
	keys.add("key1");
	keys.add("key2");
	
	Assert.assertEquals(keys.size(), cacheTemplate.lookup(keys).size());

Insert or update single key-value pair

	cacheTemplate.insertOrUpdate("key1000", "value1000");

Insert or update set of key-value pairs

	Map<String, String> keysList = new HashMap<String, String>();
	keysList.put("key45", "value45");
	keysList.put("key46", "value46");
	
	cacheTemplate.insertOrUpdate(keysList);	
									
```

Note: Refer 'com.itzmeds.cache.loader.SampleCacheLoader' for sample read through cache loader implementation