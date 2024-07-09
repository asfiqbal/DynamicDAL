package com.dilizity.dynamicDAL;

import java.util.HashMap;
import java.util.Map;

public class Schema {

    private Map<String, Query> queryMap = new HashMap<String, Query>();

     private String name;

     private String dataSource;

    private String provider;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Map<String, Query> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(Map<String, Query> queryMap) {
        this.queryMap = queryMap;
    }

    public Query getQueryMap(String key) {
        return queryMap.get(key);
    }
}
