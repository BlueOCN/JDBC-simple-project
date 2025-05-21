package main.java.com.blueocn;

import main.java.com.blueocn.data.dao.UserDao;
import main.java.com.blueocn.data.entity.User;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class App {
    public static void main(String[] args) {

        UserDao userDao = new UserDao();

        // Read
        List<User> users = userDao.getAll();
        System.out.println("**** USERS ****");
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
    }
}
