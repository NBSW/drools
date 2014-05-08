package com.zjhcsoft.qin.exchange.utils;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {

    public static void rename(Collection<Map<String, Object>> collection, Map<String, String> renames) {
        for (Map<String, Object> map : collection) {
            Object obj;
            for (String originalName : renames.keySet()) {
                if (map.containsKey(originalName)) {
                    obj = map.get(originalName);
                    //Must remove first then added after
                    map.remove(originalName);
                    map.put(renames.get(originalName), obj);
                }
            }

        }
    }

}
