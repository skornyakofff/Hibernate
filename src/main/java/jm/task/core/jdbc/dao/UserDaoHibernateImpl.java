package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        String sql = "create table if not exists users "
                + "(id bigint auto_increment primary key, "
                + "name varchar(255) not null, "
                + "lastName varchar(255) not null, "
                + "age tinyint not null);";
        Transaction transaction = null;
        try (Session session = Util.getSessions().openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "drop table if exists users;";
        Transaction transaction = null;
        try (Session session = Util.getSessions().openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Transaction transaction = null;
        try (Session session = Util.getSessions().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }
            System.out.printf(e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        User user;
        Transaction transaction = null;
        try (Session session = Util.getSessions().openSession()) {
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        try (Session session = Util.getSessions().openSession()) {
            users = session.createQuery("from User").list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessions().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }
    }
}