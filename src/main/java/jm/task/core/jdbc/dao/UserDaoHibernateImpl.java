package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String SQL = "CREATE TABLE IF NOT EXISTS public.users" +
                "(id serial," +
                " name character varying(128)," +
                " lastName character varying(128)," +
                " age integer," +
                " PRIMARY KEY (id));" +
                "ALTER TABLE public.users OWNER to postgres;";
        executeSQLQuery(SQL);
    }

    @Override
    public void dropUsersTable() {
        String SQL = "DROP TABLE IF EXISTS public.users;";
        executeSQLQuery(SQL);
    }

    private void executeSQLQuery(String SQL) {
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.createSQLQuery(SQL).executeUpdate();
        transaction.commit();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(new User(name, lastName, age));
        transaction.commit();
        session.close();
    }

    public User findUserById(Long id) {
        return Util.getSessionFactory().openSession().get(User.class, id);
    }

    @Override
    public void removeUserById(long id) {
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(findUserById(id));
        transaction.commit();
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>)  Util.getSessionFactory().openSession().createQuery("From User").list();
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("Delete from User").executeUpdate();
        transaction.commit();
        session.close();
    }
}
