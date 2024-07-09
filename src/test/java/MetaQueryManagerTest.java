import com.dilizity.dynamicDAL.MetaQueryManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MetaQueryManagerTest {

    MetaQueryManager metaQueryManager = null;
    @BeforeClass
    public void setUp() {
        //dynamicDataLayer = null; //new DynamicDataLayer("Security");
        metaQueryManager = MetaQueryManager.getInstance();
        metaQueryManager.Load();
    }

    @Test
    public void testLoadSchema() {
        Assert.assertTrue(metaQueryManager.getLoadedSchemaCount() > 0);
    }

    @Test
    public void testLoadQuery() {

        Assert.assertTrue(metaQueryManager.getLoadedQueryCount() > 0);
    }


}
