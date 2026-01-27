package com.example.demo.config;
import java.util.Arrays;
import java.util.List;

import com.example.demo.model.*;
import io.mongock.api.annotations.*;
import io.mongock.api.annotations.ChangeUnit;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;

@ChangeUnit(id="reseed1", order = "002", author = "mongock")
    public class DataBaseChangeLog {

    private final PasswordEncoder passwordEncoder;

//    @Autowired
//    private PasswordEncoder passwordEncoder;
    private final MongoTemplate mongoTemplate;


    public DataBaseChangeLog(MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public List<String> tables = Arrays.asList("expense", "category", "guest", "order", "order_items", "product", "user");

    @BeforeExecution
    public void before() {
        mongoTemplate.createCollection(Category.class);
        mongoTemplate.createCollection(User.class);
        mongoTemplate.createCollection(Product.class);
        mongoTemplate.createCollection(Category.class);
        mongoTemplate.createCollection(OrderItem.class);
        mongoTemplate.createCollection(Order.class);
    }


    @Execution
    public void migrationMethod() {
        //extract seed
        List<Category> categories = Category.seedCategory();
        List<Product> products = Product.seedProduct();
        List<OrderItem> orderItems = OrderItem.seedOrder();
        List<Order> orders = Order.seedOrder();
        List<User> users = User.seedUser(passwordEncoder);
        //data seeding
        categories.forEach(category -> mongoTemplate.save(category, "category"));
        products.forEach(product -> mongoTemplate.save(product, "product"));
        orderItems.forEach(order_item -> mongoTemplate.save(order_item, "order_items"));
        orders.forEach(order -> mongoTemplate.save(order, "order"));
        users.forEach((user -> mongoTemplate.save(user, "user")));

    }

    @RollbackBeforeExecution
    public void rollbackBefore() {

        mongoTemplate.remove(new Query(),User.class);
        mongoTemplate.remove(new Query(),Product.class);
        mongoTemplate.remove(new Query(),Category.class);
        mongoTemplate.remove(new Query(),OrderItem.class);
        mongoTemplate.remove(new Query(),Order.class);
//        mongoTemplate.dropCollection("expense");
        tables.forEach(mongoTemplate::dropCollection);
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Product.class);
        mongoTemplate.dropCollection(Category.class);
        mongoTemplate.dropCollection(OrderItem.class);
        mongoTemplate.dropCollection(Order.class);
    }

    @RollbackExecution
    public void rollback() {
        mongoTemplate.remove(new Query(),User.class);
        mongoTemplate.remove(new Query(),Product.class);
        mongoTemplate.remove(new Query(),Category.class);
        mongoTemplate.remove(new Query(),OrderItem.class);
        mongoTemplate.remove(new Query(),Order.class);
//        tables.forEach(mongoTemplate::deleteMany);
    }

     /** This is the method with the migration code **/

//     private List<Expense> getExpense() {
//         return Arrays.asList(
//                 new Expense(2000, ExpenseCategory.FOOD, "alice","1"),
//                 new Expense(9000, ExpenseCategory.UTILITIES, "bob", "2")
//         );
//     }
     /**
      This method is mandatory even when transactions are enabled.
      They are used in the undo operation and any other scenario where transactions are not an option.
      However, note that when transactions are avialble and Mongock need to rollback, this method is ignored.
      **/

}
