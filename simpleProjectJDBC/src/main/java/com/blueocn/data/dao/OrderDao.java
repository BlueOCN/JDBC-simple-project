package main.java.com.blueocn.data.dao;

import main.java.com.blueocn.data.entity.Order;
import main.java.com.blueocn.data.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class OrderDao implements Dao<Order, Integer> {

    private static final Logger LOGGER = Logger.getLogger(OrderDao.class.getName());

    private static final String GET_ALL = "SELECT order_id, user_id, order_date, total_amount, order_status FROM Orders";
    private static final String CREATE = "INSERT INTO Orders (order_id, user_id, order_date, total_amount, order_status) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_BY_ID = "SELECT order_id, user_id, order_date, total_amount, order_status FROM Orders WHERE order_id = ?";
    private static final String UPDATE = "UPDATE Orders SET user_id = ?, order_date = ?, total_amount = ?, order_status = ?  WHERE order_id = ?";
    private static final String DELETE = "DELETE FROM Orders WHERE order_id = ? LIMIT 1";

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        Connection connection = DatabaseUtils.getConnection();
        try (Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery(GET_ALL);
            orders = this.processResultSet(rs);
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("OrderDao.getAll", e, LOGGER);
        }
        return orders;
    }

    @Override
    public Order create(Order entity) {
        int id = UUID.randomUUID().hashCode();
        Connection connection = DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setInt(1, id);
            statement.setInt(2, entity.getUser_id());
            statement.setTimestamp(3, entity.getOrder_date());
            statement.setDouble(4, entity.getTotal_amount());
            statement.setString(5, entity.getOrder_status());
            statement.execute();
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSqlException("OrderDao.create.rollback", sqle, LOGGER);
            }
            DatabaseUtils.handleSqlException("OrderDao.create", e, LOGGER);
        }
        Optional<Order> order = this.getOne(id);
        if (!order.isPresent()) {
            return null;
        }
        return order.get();
    }

    @Override
    public Optional<Order> getOne(Integer id) {
        try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_BY_ID)) {
            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();
            List<Order> orders = this.processResultSet(rs);
            if (orders.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(orders.get(0));
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("OrderDao.getOne", e, LOGGER);
        }
        return Optional.empty();
    }

    @Override
    public Order update(Order entity) {
        Connection connection = DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setInt(1, entity.getUser_id());
            statement.setTimestamp(2, entity.getOrder_date());
            statement.setDouble(3, entity.getTotal_amount());
            statement.setString(4, entity.getOrder_status());
            statement.setInt(5, entity.getOrder_id());
            statement.execute();
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSqlException("OrderDao.update.rollback", sqle, LOGGER);
            }
            DatabaseUtils.handleSqlException("OrderDao.update", e, LOGGER);
        }
        return this.getOne(entity.getOrder_id()).get();
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
                DatabaseUtils.handleSqlException("OrderDao.delete.rollback", sqle, LOGGER);
            }
            DatabaseUtils.handleSqlException("OrderDao.delete", e, LOGGER);
        }
    }

    private List<Order> processResultSet(ResultSet rs) throws SQLException {
        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            Order order = new Order();
            order.setOrder_id(rs.getInt("order_id"));
            order.setUser_id(rs.getInt("user_id"));
            order.setOrder_date(rs.getTimestamp("order_date"));
            order.setTotal_amount(rs.getDouble("total_amount"));
            order.setOrder_status(rs.getString("order_status"));
            orders.add(order);
        }
        return orders;
    }
}
