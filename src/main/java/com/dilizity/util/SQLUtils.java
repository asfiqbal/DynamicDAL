package com.dilizity.util;

import com.dilizity.dynamicDAL.DynamicPOJO;
import com.dilizity.dynamicDAL.Query;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLUtils {
    private static final Pattern PARAM_PATTERN = Pattern.compile("\\?");

    public static String getFinalSQL(Query qry, DynamicPOJO dataMap) throws SQLException {
        StringBuffer finalSQL = new StringBuffer();
        Matcher matcher = PARAM_PATTERN.matcher(qry.getSQL());
        int paramIndex = 0;

        while (matcher.find()) {
            Object paramValue = getParamValue(dataMap.getValue(paramIndex));
            matcher.appendReplacement(finalSQL, paramValue.toString());
            paramIndex++;
        }
        matcher.appendTail(finalSQL);

        finalSQL.append(";");

        return finalSQL.toString();
    }

    private static String getParamValue(Object paramValue) throws SQLException {
        try {
            if (paramValue == null) {
                return "NULL";
            }
            if (paramValue instanceof String || paramValue instanceof LocalDateTime) {
                return "'" + paramValue.toString().replace("'", "''") + "'";
            }
            return paramValue.toString();
        } catch (Exception e) {
            return "?";
        }
    }
}
