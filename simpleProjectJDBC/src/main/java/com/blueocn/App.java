package main.java.com.blueocn;

import main.java.com.blueocn.data.dao.OrderDao;
import main.java.com.blueocn.data.dao.UserDao;
import main.java.com.blueocn.data.entity.Order;
import main.java.com.blueocn.data.entity.User;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class App {
    public static void main(String[] args) {


        // --- User DAO ---
        System.out.println("**** USERS ****");
        UserDao userDao = new UserDao();

        // Read
        List<User> users = userDao.getAll();
        System.out.println("\n*** GET_ALL ***");
        users.forEach(System.out::println);

        // Read one
        Optional<User> user = userDao.getOne(users.get(0).getId().intValue());
        System.out.println("\n*** GET_BY_ID ***");
        user.ifPresent(System.out::println);

        // Create one
        User newUser = new User();
        newUser.setId(BigInteger.valueOf(123));
        newUser.setUsername("John Doe");
        newUser.setEmail("johndoe@email.com");
        newUser.setPassword("password");
        newUser.setRole("customer");
        newUser.setCreated_at(new Timestamp(System.currentTimeMillis()));
        newUser.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        newUser = userDao.create(newUser);
        System.out.println("\n*** CREATE ***");
        System.out.println(newUser);

        // Update one
        newUser.setUsername("Mary Smith");
        newUser.setEmail("marysmith@email.com");
        newUser.setPassword("password2");
        newUser.setRole("customer");
        newUser = userDao.update(newUser);
        System.out.println("\n*** UPDATE ***");
        System.out.println(newUser);

        // Delete one
        userDao.delete(newUser.getId().intValue());
        users = userDao.getAll();
        System.out.println("\n*** DELETE_BY_ID ***");
        users.forEach(System.out::println);

        // Get all limit
        System.out.println("\n*** LIMIT ***");
        userDao.getAllLimit(2).forEach(System.out::println);

        System.out.println("\n*** PAGED ***");
        for (int i = 1; i < 11; i++) {
            System.out.println("Page number " + i);
            userDao.getAllPaged(i, 2).forEach(System.out::println);
        }

        // --- Order DAO ---
        System.out.println("\n**** ORDERS ****");
        OrderDao orderDao = new OrderDao();

        // Read all
        System.out.println("\n*** GET_ALL ***");
        List<Order> orders = orderDao.getAll();
        orders.forEach(System.out::println);

        // Read one
        Optional<Order> order = orderDao.getOne(orders.getFirst().getOrder_id());
        System.out.println("\n*** GET_BY_ID ***");
        order.ifPresent(System.out::println);

        // Create one
        System.out.println("\n*** CREATE ***");
        Order newOrder = new Order();
        newOrder.setUser_id(1);
        newOrder.setOrder_date(new Timestamp(System.currentTimeMillis()));
        newOrder.setTotal_amount(312.312);
        newOrder.setOrder_status("Pending");
        newOrder = orderDao.create(newOrder);
        System.out.println(newOrder);

        // Update one
        System.out.println("\n*** UPDATE ***");
        newOrder.setUser_id(2);
        newOrder.setOrder_date(new Timestamp(System.currentTimeMillis()));
        newOrder.setTotal_amount(78.45);
        newOrder.setOrder_status("Processing");
        newOrder = orderDao.update(newOrder);
        System.out.println(newOrder);

        // Delete one
        System.out.println("\n*** DELETE_BY_ID ***");
        orderDao.delete(newOrder.getOrder_id());
        orders = orderDao.getAll();
        orders.forEach(System.out::println);

    }
}
