package impl;

import static model.FruitTransaction.Operation.BALANCE;
import static model.FruitTransaction.Operation.PURCHASE;
import static model.FruitTransaction.Operation.RETURN;
import static model.FruitTransaction.Operation.SUPPLY;
import static org.junit.Assert.assertEquals;

import dao.DaoFruit;
import dao.FruitImplemDao;
import db.Storage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.FruitTransaction;
import operations.BalanceOperation;
import operations.OperationHandler;
import operations.PurchaseOperation;
import operations.ReturnOperation;
import operations.SupplyOperation;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import service.FruitService;
import strategy.Strategy;

public class FruitTransactionServiceImplTest {
    private static FruitService service;

    @BeforeClass
    public static void beforeClass() {
        DaoFruit fruitDao = new FruitImplemDao();
        Map<FruitTransaction.Operation, OperationHandler> operationHandlersMap = new HashMap<>();
        operationHandlersMap.put(BALANCE, new BalanceOperation(fruitDao));
        operationHandlersMap.put(SUPPLY, new SupplyOperation(fruitDao));
        operationHandlersMap.put(PURCHASE, new PurchaseOperation(fruitDao));
        operationHandlersMap.put(RETURN, new ReturnOperation(fruitDao));
        Strategy strategy = new Strategy(operationHandlersMap);
        service = new FruitTransactionServiceImpl(strategy);
    }

    @Test
    public void process_validData_Ok() {
        service.process(getListOfFruitTransactions());
        Map<String, Integer> actual = Storage.fruits;
        Map<String, Integer> expected = Map.of("mango", 69, "orange", 40);
        assertEquals(expected, actual);
    }

    @Test
    public void process_emptyList_Ok() {
        service.process(new ArrayList<>());
        Map<String, Integer> actual = Storage.fruits;
        Map<String, Integer> expected = Map.of();
        assertEquals(expected, actual);
    }

    @Test (expected = RuntimeException.class)
    public void process_nullTransaction_NotOk() {
        List<FruitTransaction> list = getListOfFruitTransactions();
        list.set(4, null);
        service.process(list);
    }

    @After
    public void afterEachTest() {
        Storage.fruits.clear();
    }

    private List<FruitTransaction> getListOfFruitTransactions() {
        List<FruitTransaction> fruitTransactionList = new ArrayList<>();
        fruitTransactionList.add(FruitTransaction.of(BALANCE, "mango", 80));
        fruitTransactionList.add(FruitTransaction.of(SUPPLY, "mango", 24));
        fruitTransactionList.add(FruitTransaction.of(RETURN, "mango", 8));
        fruitTransactionList.add(FruitTransaction.of(PURCHASE, "mango", 43));
        fruitTransactionList.add(FruitTransaction.of(BALANCE, "orange", 52));
        fruitTransactionList.add(FruitTransaction.of(SUPPLY, "orange", 64));
        fruitTransactionList.add(FruitTransaction.of(RETURN, "orange", 0));
        fruitTransactionList.add(FruitTransaction.of(PURCHASE, "orange", 76));
        return fruitTransactionList;
    }
}
