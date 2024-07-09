package com.dilizity.dynamicDAL;

import org.apache.commons.collections4.map.ListOrderedMap;

public class Query {

    private String id;

    private String SQL;



    private Boolean transaction = false;

    private ListOrderedMap <String, Param> paramMap = new ListOrderedMap <String, Param>();

    private ListOrderedMap<String, Query> queryMap = new ListOrderedMap<String, Query>();
      // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSQL() {
        return SQL;
    }

    public void setSQL(String cdata) {
        this.SQL = cdata;
    }

    public ListOrderedMap <String, Param> getParamMap() {
        return paramMap;
    }

    public void setParamMap(ListOrderedMap <String, Param> paramMap) {
        this.paramMap = paramMap;
    }

    public ListOrderedMap<String, Query> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(ListOrderedMap<String, Query> queryMap) {
        this.queryMap = queryMap;
    }

    public Boolean getTransaction() {
        return transaction;
    }

    public void setTransaction(Boolean transaction) {
        this.transaction = transaction;
    }

}
