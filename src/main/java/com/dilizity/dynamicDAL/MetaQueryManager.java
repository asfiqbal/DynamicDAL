package com.dilizity.dynamicDAL;

import com.dilizity.util.PropertiesWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dilizity.util.FnTraceWrap;
import org.apache.commons.collections4.map.ListOrderedMap;

public class MetaQueryManager {
    private static final Logger logger = LoggerFactory.getLogger(MetaQueryManager.class);

    // Private static instance variable
    private static MetaQueryManager instance;

    RootNode rootNode = new RootNode();
    //ConcurrentHashMap<String, Schema> schemaMap = new ConcurrentHashMap<String, Schema>();
    Boolean isFullyLoaded = false;
    // Private constructor to prevent instantiation from other classes
    private MetaQueryManager() {
        try (FnTraceWrap trace = new FnTraceWrap()) {
            Load();
        }
    }

    // Public static method to provide global access to the singleton instance
    public static MetaQueryManager getInstance() {
        // Lazy initialization - create instance if not yet initialized
        try (FnTraceWrap trace = new FnTraceWrap()) {
            if (instance == null) {
                instance = new MetaQueryManager();
            }
            //logger.info("MetaQueryManager->getInstance End.");
            return instance;
        }
        //logger.info("MetaQueryManager->getInstance started.");

    }

    public void Load() {
        try (FnTraceWrap trace = new FnTraceWrap()) {
            //logger.info("MetaQueryManager->Load Started.");
            if (isFullyLoaded)
                return; // No Need to reload as its already loaded
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String sqlConfigPath = PropertiesWrapper.getInstance().getProperty("SQLConfig.filename");
                JsonNode root = objectMapper.readTree(new File(sqlConfigPath)); // read as JsonNode
                JsonNode rNode = root.get("root");
                JsonNode schemaNodeArray = rNode.get("Schema");
                rootNode = new RootNode();

                ConcurrentHashMap<String, Schema> schemaMap = new ConcurrentHashMap<String, Schema>();

                int countSchemaNodeArray = schemaNodeArray.size();

                for (int index = 0; index < countSchemaNodeArray; index++) {
                    JsonNode tschema = schemaNodeArray.get(index);

                    Schema schema = new Schema();
                    String id = tschema.get("Name").asText();
                    schema.setName(id);
                    schema.setProvider(tschema.get("Provider").asText());
                    schema.setDataSource(tschema.get("DataSource").asText());

                    schemaMap.put(id, schema);

                    JsonNode queryList = tschema.get("Query");
                    int arraySize = queryList.size();

                    ListOrderedMap<String, Query> queryMap = new ListOrderedMap<String, Query>();

                    for (int i = 0; i < arraySize; i++) {
                        JsonNode qNode = queryList.get(i);
                        LoadQuery(qNode, queryMap);
                   }

                    schema.setQueryMap(queryMap);
                }
                rootNode.setSchemaMap(schemaMap);
                isFullyLoaded = true;

            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void LoadQuery(JsonNode qNode, ListOrderedMap<String, Query> queryMap){
        try (FnTraceWrap trace = new FnTraceWrap()) {

            Query q = new Query();
            q.setId(qNode.get("ID").asText());
            q.setSQL(qNode.get("SQL").asText());
            if (qNode.get("Transaction") != null)
                q.setTransaction(qNode.get("Transaction").asBoolean());
            queryMap.put(q.getId(), q);
            JsonNode qParamArray = qNode.get("Param");

            if (qParamArray != null) {

                int paramArraySize = qParamArray.size();
                if (paramArraySize > 0) {
                    ListOrderedMap<String, Param> paramMap = new ListOrderedMap<String, Param>();

                    for (int k = 0; k < paramArraySize; k++) {
                        JsonNode qParamNode = qParamArray.get(k);
                        Param p = new Param();
                        p.setName(qParamNode.get("Name").asText());
                        p.setType(qParamNode.get("Type").asText());
                        p.setSize(qParamNode.get("Size").asText());
                        p.setOrder(qParamNode.get("Order").asText());
                        paramMap.put(p.getName(), p);
                    }
                    q.setParamMap(paramMap);
                }
            }

            JsonNode innerQueryList = qNode.get("Query");

            if (innerQueryList != null) {
                int innerArraySize = innerQueryList.size();

                if (innerArraySize > 0) {
                    ListOrderedMap<String, Query> innerQueryMap = new ListOrderedMap<String, Query>();

                    for (int i = 0; i < innerArraySize; i++) {
                        JsonNode innerQNode = innerQueryList.get(i);
                        LoadQuery(innerQNode, innerQueryMap);
                    }
                    q.setQueryMap(innerQueryMap);
                }
            }
        }
    }


    public Schema getSchema(String schemakey)  {
        return rootNode.getSchema(schemakey);
    }

    public int getLoadedSchemaCount() {
        return rootNode.getSchemaMap().size();
    }

    public int getLoadedQueryCount() {
        int totalMapEntriesCount = 0;
        for (Map.Entry<String, Schema> entry : rootNode.getSchemaMap().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
            Schema schema = entry.getValue();
            totalMapEntriesCount += schema.getQueryMap().size();
        }
        return totalMapEntriesCount;
    }

}