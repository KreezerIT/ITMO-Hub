package dao.persistence;

import dao.Dao;
import entity.Owner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class OwnerDaoImpl implements Dao<Owner> {

    @Override
    public Owner save(Owner entity) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return entity;
        }
    }

    @Override
    public void deleteById(long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Owner owner = session.get(Owner.class, id);
            if (owner != null) {
                session.remove(owner);
            }
            transaction.commit();
        }
    }

    @Override
    public void deleteByEntity(Owner entity) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        }
    }

    @Override
    public void deleteAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeMutationQuery("TRUNCATE TABLE owner CASCADE").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Deleting all owners failed: " + e.getMessage());
        }
    }


    @Override
    public Owner update(Owner entity) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
            return entity;
        }
    }

    @Override
    public Owner getById(long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT o FROM Owner o LEFT JOIN FETCH o.pets WHERE o.id = :id", Owner.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }


    @Override
    public List<Owner> getAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("SELECT o FROM Owner o LEFT JOIN FETCH o.pets", Owner.class).list();
        }
    }

    @Override
    public List<Owner> getByName(String name) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT o FROM Owner o LEFT JOIN FETCH o.pets WHERE o.name = :name", Owner.class)
                    .setParameter("name", name)
                    .list();
        }
    }
}
