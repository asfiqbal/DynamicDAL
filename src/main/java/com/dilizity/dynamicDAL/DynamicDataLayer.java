package com.dilizity.dynamicDAL;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dilizity.util.FnTraceWrap;
import com.dilizity.util.SQLUtils;
import com.dilizity.util.Utils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DynamicDataLayer {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataLayer.class);

    private final String connectionString;
    private final String providerName;

    Schema schema = null;

    public DynamicDataLayer(String schemaName)
    {
        try (FnTraceWrap tracer = new FnTraceWrap(schemaName)) {

            assert StringUtils.isBlank(schemaName) != Boolean.TRUE : "schemaName can't be empty";
            schema = MetaQueryManager.getInstance().getSchema(schemaName);

            connectionString = schema.getDataSource();
            providerName = schema.getProvider();

            assert (!StringUtils.isBlank(connectionString));
            assert (!StringUtils.isBlank(providerName));
        }
    }

    public List<DynamicPOJO> ExecuteUsingKey(String commandKey, Object... objects)
    {
        List<DynamicPOJO> returnObject = null;
        try (FnTraceWrap tracer = new FnTraceWrap()) {
            assert (!StringUtils.isBlank(commandKey));
            assert (objects != null);

            Query query = schema.getQueryMap(commandKey);
            ListOrderedMap<String, Param> tmpParams = query.getParamMap();

            //Log.Debug(this.GetType(), "commandKey={0}", commandKey);

            try (Connection connection = DriverManager.getConnection(connectionString)) {
                System.out.println("Connected to SQL Server successfully.");
                String sql = query.getSQL();
                // Create a statement object
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                PrePareStatementDataTypeHelper(preparedStatement, tmpParams, objects);

                ResultSet resultSet = preparedStatement.executeQuery();

                returnObject = DynamicDataLayer.convertToDynamicPOJO(resultSet);

            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }

        }

        return returnObject;
    }



    public int ExecuteScalarUsingKey(String commandKey, Object... objects)
    {
        try (FnTraceWrap tracer = new FnTraceWrap()) {

            assert (!StringUtils.isBlank(commandKey));
            assert (objects != null);

            Query query = schema.getQueryMap(commandKey);
            ListOrderedMap<String, Param> tmpParams = query.getParamMap();

            //Log.Debug(this.GetType(), "commandKey={0}", commandKey);

            try (Connection connection = DriverManager.getConnection(connectionString)) {
                System.out.println("Connected to SQL Server successfully.");
                String sql = query.getSQL();
                // Create a statement object
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                PrePareStatementDataTypeHelper(preparedStatement, tmpParams, objects);

                return preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    public int ExecuteScalarUsingKey(String commandKey, DynamicPOJO dataMap) throws SQLException {
        try (FnTraceWrap tracer = new FnTraceWrap()) {

            assert (!StringUtils.isBlank(commandKey));
            assert (dataMap != null);

            Connection connection = null;
            Query query = null;

            try {
                query = schema.getQueryMap(commandKey);
                connection = DriverManager.getConnection(connectionString);
                logger.info("Connected to SQL Server successfully.");

                Statement statement = connection.createStatement();
                //DynamicPOJO pojo = dataMap.get(query.getId());
                ListOrderedMap<String, Param> tmpParams = query.getParamMap();
                PreparedStatement preparedStatement = connection.prepareStatement(query.getSQL());
                PrePareStatementDataTypeHelper(preparedStatement, query, dataMap);
                return preparedStatement.executeUpdate();

            } catch (Exception  e) {
                logger.error(e.getMessage(), e);

                if (connection != null)
                    connection.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    public int ExecuteBatchUsingKey(String commandKey, Map<String, DynamicPOJO> dataMap) throws SQLException {
        try (FnTraceWrap tracer = new FnTraceWrap()) {

            assert (!StringUtils.isBlank(commandKey));
            assert (dataMap != null);

            Connection connection = null;
            Query query = null;

            try {
                query = schema.getQueryMap(commandKey);
                StringBuilder sb = new StringBuilder();
                connection = DriverManager.getConnection(connectionString);
                logger.info("Connected to SQL Server successfully.");
                //String sql = query.getSQL();
                if (query.getTransaction()) {

                    connection.setAutoCommit(false);
                    logger.info("Connection initiated Transaction successfully");
                }

                //Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(query.getSQL());
                DynamicPOJO pojo = dataMap.get(query.getId());

                PrePareStatementDataTypeHelper(preparedStatement, query, pojo);
                int count = preparedStatement.executeUpdate();

                ListOrderedMap<String, Query> tmpItr = query.getQueryMap();

                for (Map.Entry<String, Query> entry : tmpItr.entrySet()) {
                    Query qry = entry.getValue();
                    pojo = dataMap.get(qry.getId());
                    preparedStatement = connection.prepareStatement(qry.getSQL());
                    PrePareStatementDataTypeHelper(preparedStatement, qry, pojo);
                    count = preparedStatement.executeUpdate();
                }

                connection.commit();
                return count;

            } catch (Exception  e) {
                logger.error(e.getMessage(), e);

                if (connection != null)
                    connection.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    public int ExecuteBatchUsingKeyInOneRound(String commandKey, Map<String, DynamicPOJO> dataMap) throws SQLException {
        try (FnTraceWrap tracer = new FnTraceWrap()) {

            assert (!StringUtils.isBlank(commandKey));
            assert (dataMap != null);

            Connection connection = null;
            Query query = null;

            try {
                query = schema.getQueryMap(commandKey);
                StringBuilder sb = new StringBuilder();
                connection = DriverManager.getConnection(connectionString);
                logger.info("Connected to SQL Server successfully.");
                String sql = query.getSQL();
                if (query.getTransaction()) {

                    connection.setAutoCommit(false);
                    logger.info("Connection initiated Transaction successfully");
                }

                Statement statement = connection.createStatement();
                DynamicPOJO pojo = dataMap.get(query.getId());

                String tmpSQL = SQLUtils.getFinalSQL(query, pojo);
                sb.append(tmpSQL);

                ListOrderedMap<String, Query> tmpItr = query.getQueryMap();

                for (Map.Entry<String, Query> entry : tmpItr.entrySet()) {
                    Query qry = entry.getValue();
                    pojo = dataMap.get(qry.getId());
                    tmpSQL = SQLUtils.getFinalSQL(qry, pojo);
                    sb.append(tmpSQL);
                }

                String finalSQLBatch = sb.toString();
                logger.info(finalSQLBatch);
                int out =  statement.executeUpdate(finalSQLBatch);
                connection.commit();
                return out;

            } catch (Exception  e) {
                logger.error(e.getMessage(), e);

                if (connection != null)
                    connection.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    private static void PrePareStatementDataTypeHelper(PreparedStatement pPreStatement, ListOrderedMap<String, Param> tmpParams, Object... objects) throws SQLException, IOException {
        try (FnTraceWrap tracer = new FnTraceWrap()) {
            int index = 1;
            for (Map.Entry<String, Param> entry : tmpParams.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
                Param param = entry.getValue();
                String key = entry.getKey();

                if (objects[index - 1] instanceof ObjectUtils.Null) {
                    pPreStatement.setNull(index, Types.NULL);
                    index++;
                    continue;
                }
                switch (param.getType().toLowerCase()) {
                    case "int":
                        pPreStatement.setInt(index, Utils.convertToInt(objects[index - 1]));
                        break;
                    case "numeric":
                        pPreStatement.setDouble(index, Utils.convertToDouble(objects[index - 1]));
                        break;
                    case "bit":
                        pPreStatement.setBoolean(index, Utils.convertToBoolean(objects[index - 1]));
                        break;
                    case "datetime":
                        pPreStatement.setTimestamp(index, Utils.convertToTimeStamp(objects[index - 1]));
                        break;
                    case "double":
                        break;
                    case "boolean":
                        break;
                    case "string":
                        pPreStatement.setString(index, Utils.convertToString(objects[index - 1]));
                        break;
                    default:
                        pPreStatement.setString(index, Utils.convertToString(objects[index - 1]));
                        break;
                }
                // Add other types as needed
                index++;
            }
        }
    }

    private static void PrePareStatementDataTypeHelper(PreparedStatement pPreStatement, Query qry, DynamicPOJO dataMap) throws SQLException, IOException {
        try (FnTraceWrap tracer = new FnTraceWrap()) {
            int index = 1;

            ListOrderedMap<String, Param> tmpParams = qry.getParamMap();
            for (Map.Entry<String, Param> entry : tmpParams.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
                Param param = entry.getValue();
                String key = entry.getKey();

                Object data = dataMap.getProperties(key);

                if (data instanceof ObjectUtils.Null) {
                    pPreStatement.setNull(index, Types.NULL);
                    index++;
                    continue;
                }
                switch (param.getType().toLowerCase()) {
                    case "int":
                        pPreStatement.setInt(index, Utils.convertToInt(data));
                        break;
                    case "numeric":
                        pPreStatement.setDouble(index, Utils.convertToDouble(data));
                        break;
                    case "bit":
                        pPreStatement.setBoolean(index, Utils.convertToBoolean(data));
                        break;
                    case "datetime":
                        pPreStatement.setTimestamp(index, Utils.convertToTimeStamp(data));
                        break;
                    case "double":
                        break;
                    case "boolean":
                        break;
                    case "string":
                        pPreStatement.setString(index, Utils.convertToString(data));
                        break;
                    default:
                        pPreStatement.setString(index, Utils.convertToString(data));
                        break;
                }
                // Add other types as needed
                index++;
            }
        }
    }

    private static String convertToJson(ResultSet resultSet) throws SQLException, IOException {
        try (FnTraceWrap tracer = new FnTraceWrap()) {
            StringWriter writer = new StringWriter();

            JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(writer);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            jsonGenerator.writeStartArray();
            while (resultSet.next()) {
                jsonGenerator.writeStartObject();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    jsonGenerator.writeObjectField(columnName, value);
                }
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
            jsonGenerator.close();
            return writer.toString();
        }
    }

    private static List<DynamicPOJO> convertToDynamicPOJO(ResultSet resultSet) throws SQLException, IOException {
        try (FnTraceWrap tracer = new FnTraceWrap()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<DynamicPOJO> listPOJO = new ArrayList<DynamicPOJO>();


            while (resultSet.next()) {

                DynamicPOJO dPOJO = new DynamicPOJO();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    dPOJO.setProperties(columnName, value);

                }
                listPOJO.add(dPOJO);

            }

            return listPOJO;
        }
    }


}
