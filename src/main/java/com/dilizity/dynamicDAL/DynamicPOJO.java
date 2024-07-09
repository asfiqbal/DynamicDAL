package com.dilizity.dynamicDAL;

import org.apache.commons.collections4.map.ListOrderedMap;

public class DynamicPOJO {
    ListOrderedMap<String, Object> properties = new ListOrderedMap<String, Object>();

    public Object getProperties(String key) {
        return properties.get(key);
    }

    public void setProperties(String key, Object object) {
        properties.put(key, object);
    }

    public Object getValue(int index){
        return properties.getValue(index);
    }
}
