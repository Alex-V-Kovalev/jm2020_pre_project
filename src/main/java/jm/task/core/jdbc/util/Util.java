package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/jm2020_pre_project";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "dbadmin";

    private static Connection connection;

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
                settings.put(Environment.URL, DB_URL);
                settings.put(Environment.USER, DB_USER);
                settings.put(Environment.PASS, DB_PASS);
                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL10Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "update");
                configuration.setProperties(settings);

                configuration.addAnnotatedClass(User.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());

                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.err.println("Ошибка при подключении к СУБД");
            }
        }
        return sessionFactory;
    }

    public static Connection getConnection() {
        boolean connectionClosed = true;
        try {
            if (connection != null) {
                connectionClosed = connection.isClosed();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection == null || connectionClosed) {
            try {
                connection = DriverManager
                        .getConnection(DB_URL, DB_USER, DB_PASS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return connection;
    }

}
