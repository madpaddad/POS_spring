package com.example.demo.config;
import java.util.Arrays;
import java.util.List;

import com.example.demo.model.*;
import io.mongock.api.annotations.*;
import io.mongock.api.annotations.ChangeUnit;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id="migration_new", order = "002", author = "mongock")
    public class DataBaseChangeLog {


    private final MongoTemplate mongoTemplate;

    private static final String CLIENTS_COLLECTION_NAME = "clients";

    public DataBaseChangeLog(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<String> tables = Arrays.asList("expense", "category", "guest", "order", "order_items", "product");

    @BeforeExecution
    public void before() {
//        mongoTemplate.createCollection("category");
        tables.forEach(mongoTemplate::createCollection);
    }

    @Execution
    public void migrationMethod() {
        //extract seed
        List<Expense> expenses = getExpense();
        List<Category> categories = Category.seedCategory();
        List<Product> products = Product.seedProduct();
        List<OrderItem> orderItems = OrderItem.seedOrder();
        List<Order> orders = Order.seedOrder();

        //data seeding
        expenses.forEach(expense -> mongoTemplate.save(expense, "expense"));
        categories.forEach(category -> mongoTemplate.save(category, "category"));
        products.forEach(product -> mongoTemplate.save(product, "product"));
        orderItems.forEach(order_item -> mongoTemplate.save(order_item, "order_items"));
        orders.forEach(order -> mongoTemplate.save(order, "order"));
    }

    @RollbackBeforeExecution
    public void rollbackBefore() {
//        mongoTemplate.dropCollection("expense");
        tables.forEach(mongoTemplate::dropCollection);
    }

    @RollbackExecution
    public void rollback() {
        tables.forEach(mongoTemplate::remove);
//        tables.forEach(mongoTemplate::deleteMany);
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
