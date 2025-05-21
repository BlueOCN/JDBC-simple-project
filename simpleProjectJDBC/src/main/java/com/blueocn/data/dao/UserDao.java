package main.java.com.blueocn.data.dao;

import main.java.com.blueocn.data.entity.User;
import main.java.com.blueocn.data.util.DatabaseUtils;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class UserDao implements Dao<User, Integer> {

    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

    private static final String GET_ALL = "SELECT user_id, username, email, password, role, created_at, updated_at FROM Users";
    private static final String GET_BY_ID = "SELECT user_id, username, email, password, role, created_at, updated_at FROM Users WHERE user_id = ?";
    private static final String CREATE = "INSERT INTO Users (user_id, username, email, password, role, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE Users SET username = ?, email = ?, password = ?, role = ? WHERE user_id = ?";
    private static final String DELETE = "DELETE FROM Users WHERE user_id = ? LIMIT 1";

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        Connection connection = DatabaseUtils.getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(GET_ALL);
            users = this.processResultSet(rs);
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("UserDao.getAll", e, LOGGER);
        }
        return users;
    }

    @Override
    public User create(User entity) {
        int id = 23;
        Connection connection = DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setInt(1, id);
            statement.setString(2, entity.getUsername());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            statement.setString(5, entity.getRole());
            statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            statement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            statement.execute();
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSqlException("UserDao.create.rollback", sqle, LOGGER);
            }
            DatabaseUtils.handleSqlException("UserDao.create", e, LOGGER);
        }
        Optional<User> user = this.getOne(id);
        if (!user.isPresent()) {
            return null;
        }
        return user.get();
    }

    @Override
    public Optional<User> getOne(Integer id) {
        try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_BY_ID)){
            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();
            List<User> users = this.processResultSet(rs);
            if (users.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(users.get(0));
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("UserDao.getOne", e, LOGGER);
        }
        return Optional.empty();
    }

    @Override
    public User update(User entity) {
        Connection connection = DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getEmail());
            statement.setString(3, entity.getPassword());
            statement.setString(4, entity.getRole());
            statement.setInt(5, entity.getId().intValue());
            statement.execute();
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSqlException("UserDao.update.rollback", sqle, LOGGER);
            }
            DatabaseUtils.handleSqlException("UserDao.update", e, LOGGER);
        }
        return this.getOne(entity.getId().intValue()).get();
    }

    @Override
    public void delete(Integer id) {
        Connection connection = DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSqlException("UserDao.delete.rollback", sqle, LOGGER);
            }
            DatabaseUtils.handleSqlException("UserDao.delete", e, LOGGER);
        }
    }

    private List<User> processResultSet(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setId(BigInteger.valueOf((int) rs.getObject("user_id")));
            user.setUsername((String) rs.getObject("username"));
            user.setEmail((String) rs.getObject("email"));
            user.setPassword((String) rs.getObject("password"));
            user.setRole((String) rs.getObject("role"));
            user.setCreated_at((Timestamp) rs.getObject("created_at"));
            user.setUpdated_at((Timestamp) rs.getObject("updated_at"));
            users.add(user);
        }
        return users;
    }

}
