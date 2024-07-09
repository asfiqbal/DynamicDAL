import com.dilizity.dynamicDAL.DynamicDataLayer;
import com.dilizity.dynamicDAL.DynamicPOJO;
import com.dilizity.dynamicDAL.MetaQueryManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import com.dilizity.util.Utils;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DynamicDataLayerTest {

    private DynamicDataLayer dynamicDataLayer = null;
    MetaQueryManager metaQueryManager = null;

    @BeforeClass
    public void setUp() {
        //dynamicDataLayer = null; //new DynamicDataLayer("Security");
        metaQueryManager = MetaQueryManager.getInstance();
        metaQueryManager.Load();
        dynamicDataLayer = new DynamicDataLayer("Security");
    }

    @Test
    public void testConstructor() {

        DynamicDataLayer tmpLayer = new DynamicDataLayer("Security");
        Assert.assertEquals(1, 1);
    }

    @Test
    public void testExecuteUsingKey() throws ClassNotFoundException {
        //dynamicDataLayer = new DynamicDataLayer("Security");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        List<DynamicPOJO> listObjects = dynamicDataLayer.ExecuteUsingKey("GetAllComplaints");

        Assert.assertFalse(listObjects.isEmpty());
    }

    @Test
    public void testExecuteWithMultipleStringFilter() throws ClassNotFoundException {
        //dynamicDataLayer = new DynamicDataLayer("Security");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        List<DynamicPOJO> listObjects = dynamicDataLayer.ExecuteUsingKey("GetSpecificComplaint", "C0101180001", "117");

        Assert.assertFalse(listObjects.isEmpty());
    }

    @Test
    public void testExecuteWithDateFilters() throws ClassNotFoundException {
        //dynamicDataLayer = new DynamicDataLayer("Security");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        String isoFromDateTime = "2023-05-01T00:00:00";
        String isoToDateTime = "2023-05-31T23:59:59";

        // Define the formatter for ISO local date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        // Parse the ISO local date-time string to LocalDateTime
        LocalDateTime fromDateTime = LocalDateTime.parse(isoFromDateTime, formatter);
        LocalDateTime toDateTime = LocalDateTime.parse(isoToDateTime, formatter);


        LocalDateTime fromDate;
        LocalDateTime toDate ;

        List<DynamicPOJO> listObjects = dynamicDataLayer.ExecuteUsingKey("GetComplaintFiltersbyDate", fromDateTime, toDateTime);

        Assert.assertFalse(listObjects.isEmpty());
    }

    @Test
    public void testExecuteWithBitFilter() throws ClassNotFoundException {
        //dynamicDataLayer = new DynamicDataLayer("Security");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Boolean isActive = true;
        List<DynamicPOJO> listObjects = dynamicDataLayer.ExecuteUsingKey("GetComplaintFiltersbyBIT", isActive);

        Assert.assertFalse(listObjects.isEmpty());
    }

    @Test
    public void testExecuteScalerInsert() throws ClassNotFoundException {
        //dynamicDataLayer = new DynamicDataLayer("Security");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Boolean isActive = true;

        int impactedRows = dynamicDataLayer.ExecuteScalarUsingKey("InsertActivityType", "000001", ObjectUtils.NULL, "1322", "Test", "Test", "Testng", "Manual", "0", ObjectUtils.NULL, true, 1, 1, ObjectUtils.NULL, "2024-07-01T00:00:00", "System", "2024-07-01T00:00:00", "System", "0", ObjectUtils.NULL, ObjectUtils.NULL, ObjectUtils.NULL, false, true);

        Assert.assertTrue(impactedRows > 0);
    }

    @Test
    public void testExecuteScalerDelete() throws ClassNotFoundException {
        //dynamicDataLayer = new DynamicDataLayer("Security");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        int impactedRows = dynamicDataLayer.ExecuteScalarUsingKey("DeleteActivityType", "000001");

        Assert.assertTrue(impactedRows > -1);
    }

    @Test
    public void testExecuteBatch() throws ClassNotFoundException, SQLException, IOException {
        //dynamicDataLayer = new DynamicDataLayer("Security");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        DynamicPOJO pojoObject = Utils.convertToDynamicPOJO("BatchId",9999, "Status",1, "Description", "ATest" );
        Map<String, DynamicPOJO> dataCollection = new HashMap<String,DynamicPOJO>();
        dataCollection.put("BatchProcess", pojoObject);
        pojoObject =Utils.convertToDynamicPOJO("BatchId",9999, "ChildId",999, "Description", "ADescription");
        dataCollection.put("ComplainBatchProcessStep1", pojoObject);
        pojoObject =Utils.convertToDynamicPOJO("StatusId", 3, "BatchId", 9999);
        dataCollection.put("ComplainBatchProcessStep2", pojoObject);
        int impactedRows = dynamicDataLayer.ExecuteBatchUsingKey("BatchProcess", dataCollection);

        Assert.assertTrue(impactedRows > 0);
    }

    @Test
    public void testExecuteBatchRollback() throws ClassNotFoundException, SQLException, IOException {
        //dynamicDataLayer = new DynamicDataLayer("Security");
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            DynamicPOJO pojoObject = Utils.convertToDynamicPOJO("BatchId", 9999, "Status", 1, "Description", "ATest");
            Map<String, DynamicPOJO> dataCollection = new HashMap<String, DynamicPOJO>();
            dataCollection.put("BatchProcess", pojoObject);
            pojoObject = Utils.convertToDynamicPOJO("BatchId", 9999, "ChildId", 999, null, null);
            dataCollection.put("ComplainBatchProcessStep1", pojoObject);
            int impactedRows = dynamicDataLayer.ExecuteBatchUsingKey("BatchProcess", dataCollection);

        }
        catch (Exception e) {
            //Assert. verifyException("com.dilizity.dynamicDAL.DynamicDataLayer", e);
        }
    }


    @Test
    public void TestExecuteBatchUsingKeyInOneRound() throws ClassNotFoundException, SQLException, IOException {
        //dynamicDataLayer = new DynamicDataLayer("Security");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        DynamicPOJO pojoObject = Utils.convertToDynamicPOJO("BatchId",1, "Status",1, "Description", "Test" );
        Map<String, DynamicPOJO> dataCollection = new HashMap<String,DynamicPOJO>();
        dataCollection.put("BatchProcess", pojoObject);
        pojoObject =Utils.convertToDynamicPOJO("BatchId",1, "ChildId",2, "Description", "ChildDescription");
        dataCollection.put("ComplainBatchProcessStep1", pojoObject);
        pojoObject =Utils.convertToDynamicPOJO("StatusId", 2, "BatchId", 1);
        dataCollection.put("ComplainBatchProcessStep2", pojoObject);
        int impactedRows = dynamicDataLayer.ExecuteBatchUsingKeyInOneRound("BatchProcess", dataCollection);

        Assert.assertTrue(impactedRows > 0);
    }

    @Test
    public void testExecuteScalarWithLoad() throws ClassNotFoundException, SQLException, IOException {
        //dynamicDataLayer = new DynamicDataLayer("Security");
        //dynamicDataLayer = new DynamicDataLayer("Security");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Boolean isActive = true;

        int impactedRows = -1;
        for (int i=1;i <= 100; i++){
            DynamicPOJO pojoObject = Utils.convertToDynamicPOJO("BatchId", i, "Status", i+100, "Description", "Description" + Integer.toString(i));
            impactedRows = dynamicDataLayer.ExecuteScalarUsingKey("LoadTest", pojoObject);
        }

        Assert.assertTrue(impactedRows > 0);
    }

    // Additional test cases...
}
