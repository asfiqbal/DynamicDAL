package com.dilizity.util;
import com.dilizity.dynamicDAL.DynamicPOJO;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static int convertToInt(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        if (obj instanceof String) {
            return Integer.parseInt((String) obj);
        } else if (obj instanceof Integer) {
            return (int) obj;
        } else {
            throw new IllegalArgumentException("Unsupported type for conversion: " + obj.getClass().getName());
        }
    }

    public static Boolean convertToBoolean(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        if (obj instanceof String) {
            return Boolean.parseBoolean((String) obj);
        } else if (obj instanceof Boolean) {
            return (Boolean)obj;
        } else {
            throw new IllegalArgumentException("Unsupported type for conversion: " + obj.getClass().getName());
        }
    }

    public static String convertToString(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        if (obj instanceof String) {
            return (String)obj;
        }
        else {
            throw new IllegalArgumentException("Unsupported type for conversion: " + obj.getClass().getName());
        }
    }

    public static LocalDateTime convertToDateTime(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        if (obj instanceof LocalDateTime) {
            return (LocalDateTime)obj;
        }
        else if (obj instanceof String) {
            DateTimeFormatter isoFormat = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime date = LocalDateTime.parse((String)obj);
            return date;
        }
        else {
            throw new IllegalArgumentException("Unsupported type for conversion: " + obj.getClass().getName());
        }
    }

    public static Timestamp convertToTimeStamp(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        if (obj instanceof Timestamp) {
            return (Timestamp)obj;
        }
        else if (obj instanceof LocalDateTime) {
            //DateTimeFormatter isoFormat = DateTimeFormatter.ISO_DATE_TIME;
            //LocalDateTime date = LocalDateTime.parse((String)obj);
            return Timestamp.valueOf((LocalDateTime)obj);
        }
        else if (obj instanceof String) {
            DateTimeFormatter isoFormat = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime date = LocalDateTime.parse((String)obj);
            java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(date);
            return timestamp;
        }
        else {
            throw new IllegalArgumentException("Unsupported type for conversion: " + obj.getClass().getName());
        }
    }

    public static Double convertToDouble(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (obj instanceof String) {
            return Double.parseDouble((String)obj);
        } else if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).doubleValue();
        } else if (obj instanceof Double) {
            return ((Double) obj);
        } else {
            throw new IllegalArgumentException("Unsupported type for conversion: " + obj.getClass().getName());
        }
    }

    public static DynamicPOJO convertToDynamicPOJO(Object... objects) throws SQLException, IOException {

        DynamicPOJO pojoObject = new DynamicPOJO();
        int count = objects.length;
        for (int ndx = 0 ; ndx < objects.length; ndx+=2 ) {
            String key = (String) objects[ndx];
            Object value = objects[ndx+1];
            pojoObject.setProperties(key, value);
        }

        return pojoObject;
    }

}
