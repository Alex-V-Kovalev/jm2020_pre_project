package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection;

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String SQL = "CREATE TABLE IF NOT EXISTS public.users" +
                "(id serial," +
                " name character varying(128)," +
                " lastName character varying(128)," +
                " age integer," +
                " PRIMARY KEY (id));" +
                "ALTER TABLE public.users OWNER to postgres;";
        executeSQLStatement(SQL);
    }

    public void dropUsersTable() {
        String SQL = "DROP TABLE IF EXISTS public.users;";
        executeSQLStatement(SQL);
    }

    public void saveUser(String name, String lastName, byte age) {
        String SQL = String.format("INSERT INTO public.users (name, lastName, age)" +
                "VALUES ('%s', '%s', %d);", name, lastName, age);
        executeSQLStatement(SQL);
    }

    public void removeUserById(long id) {
        String SQL = String.format("DELETE FROM public.users WHERE id=%d;", id);
        executeSQLStatement(SQL);
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        Statement statement = null;
        String SQL = "SELECT * FROM public.users;";
        connection = Util.getConnection();

        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"), resultSet.getString("lastName"), resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
            closeConnection();
        }
        return userList;
    }

    public void cleanUsersTable() {
        String SQL = "TRUNCATE TABLE public.users;";
        executeSQLStatement(SQL);
    }

    private void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeSQLStatement(String SQL) {
        connection  = Util.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
            closeConnection();
        }
    }
}
