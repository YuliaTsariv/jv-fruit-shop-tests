package operations;

import static model.FruitTransaction.Operation.PURCHASE;
import static org.testng.Assert.assertEquals;

import dao.FruitImplemDao;
import db.Storage;
import model.FruitTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PurchaseOperationTest {
    private static OperationHandler purchaseHandler;

    @BeforeClass
    public static void beforeClass() {
        purchaseHandler = new PurchaseOperation(new FruitImplemDao());
    }

    @Before
    public void setUp() {
        Storage.fruits.put("peach", 73);
        Storage.fruits.put("pear", 27);
    }

    @Test
    public void handle_purchaseTransaction_Ok() {
        FruitTransaction fruitTransaction = FruitTransaction.of(PURCHASE, "peach", 12);
        int expected = 61;
        purchaseHandler.handle(fruitTransaction);
        int actual = Storage.fruits.get("peach");
        assertEquals(expected, actual);
    }

    @After
    public void afterEachTest() {
        Storage.fruits.clear();
    }
}