package com.dilizity.dynamicDAL;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.ConcurrentHashMap;

public class RootNode {

    @JsonProperty("Schema")
    private ConcurrentHashMap<String, Schema> schemaMap;

    // Getters and Setters

    public ConcurrentHashMap<String, Schema> getSchemaMap() {
        return this.schemaMap;
    }

    public void setSchemaMap(ConcurrentHashMap<String, Schema> pSchemaMap) {
        this.schemaMap = pSchemaMap;
    }

    public Schema getSchema(String key) {
        return schemaMap.get(key);
    }

}
