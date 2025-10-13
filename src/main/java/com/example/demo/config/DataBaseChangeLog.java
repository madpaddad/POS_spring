package com.example.demo.config;
import java.util.Arrays;
import java.util.List;

import com.example.demo.model.ExpenseCategory;
import io.mongock.api.annotations.*;
import io.mongock.api.annotations.ChangeUnit;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.example.demo.model.Expense;
import com.example.demo.model.Order;
@ChangeUnit(id="DataBaseChangeLog", order = "001", author = "mongock")
    public class DataBaseChangeLog {


    private final MongoTemplate mongoTemplate;

    private static final String CLIENTS_COLLECTION_NAME = "clients";

    public DataBaseChangeLog(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<String> tables = Arrays.asList("expense", "category", "guest", "order", "order_item", "product");

    @BeforeExecution
    public void before() {
        tables.forEach(mongoTemplate::createCollection);
    }

    @Execution
    public void migrationMethod() {
        List<Expense> expenses = getExpense();
        expenses.forEach(expense -> mongoTemplate.save(expense, "expense"));
        List<Order> orders = Order.seedOrder();
    }

    @RollbackBeforeExecution
    public void rollbackBefore() {
        tables.forEach(mongoTemplate::dropCollection);
    }

    @RollbackExecution
    public void rollback() {
        tables.forEach(mongoTemplate::remove);
    }

     /** This is the method with the migration code **/

     private List<Expense> getExpense() {
         return Arrays.asList(
                 new Expense(2000, ExpenseCategory.FOOD, "alice","1"),
                 new Expense(9000, ExpenseCategory.UTILITIES, "bob", "2")
         );
     }
     /**
      This method is mandatory even when transactions are enabled.
      They are used in the undo operation and any other scenario where transactions are not an option.
      However, note that when transactions are avialble and Mongock need to rollback, this method is ignored.
      **/

}
