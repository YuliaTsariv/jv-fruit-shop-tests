package operations;

import static model.FruitTransaction.Operation.RETURN;
import static org.testng.Assert.assertEquals;

import dao.FruitImplemDao;
import db.Storage;
import model.FruitTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReturnOperationTest {
    private static OperationHandler returnHandler;

    @BeforeClass
    public static void beforeClass() {
        returnHandler = new ReturnOperation(new FruitImplemDao());
    }

    @Before
    public void setUp() {
        Storage.fruits.put("peach", 73);
        Storage.fruits.put("pear", 27);
    }

    @Test
    public void handle_returnTransaction_Ok() {
        FruitTransaction fruitTransaction = FruitTransaction.of(RETURN, "peach", 12);
        int expected = 85;
        returnHandler.handle(fruitTransaction);
        int actual = Storage.fruits.get("peach");
        assertEquals(expected, actual);
    }

    @After
    public void afterEachTest() {
        Storage.fruits.clear();
    }
}
