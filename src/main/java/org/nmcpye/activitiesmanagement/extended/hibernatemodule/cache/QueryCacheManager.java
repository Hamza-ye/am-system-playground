package org.nmcpye.activitiesmanagement.extended.hibernatemodule.cache;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.hibernate.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QueryCacheManager {
    private final Logger log = LoggerFactory.getLogger(QueryCacheManager.class);

    private final Map<String, Set<String>> regionNameMap = new ConcurrentHashMap<>();

    private final HashFunction sessionIdHasher = Hashing.sha256();

    public String generateRegionName(Class<?> klass, String queryString) {
        String queryStringHash = sessionIdHasher.newHasher().putString(queryString, StandardCharsets.UTF_8).hash()
            .toString();
        String regionName = klass.getName() + "_" + queryStringHash;

        Set<String> allQueriesOnKlass = regionNameMap.computeIfAbsent(klass.getName(), s -> new HashSet<>());
        allQueriesOnKlass.add(queryStringHash);

        return regionName;
    }

    public void evictQueryCache(Cache cache, Class<?> klass) {
        Set<String> hashes = regionNameMap.getOrDefault(klass.getName(), Collections.emptySet());

        for (String regionNameHash : hashes) {
            String key = klass.getName() + "_" + regionNameHash;

            cache.evictQueryRegion(key);
        }
    }
}
