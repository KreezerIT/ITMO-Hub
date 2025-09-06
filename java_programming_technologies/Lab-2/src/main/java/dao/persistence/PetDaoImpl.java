package dao.persistence;

import dao.DaoWithFriendship;
import entity.Pet;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PetDaoImpl implements DaoWithFriendship<Pet> {

    @Override
    public Pet save(Pet entity) {
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
            Pet pet = session.get(Pet.class, id);
            if (pet != null) {
                session.remove(pet);
            }
            transaction.commit();
        }
    }

    @Override
    public void deleteByEntity(Pet entity) {
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
            session.createNativeMutationQuery("TRUNCATE TABLE pet CASCADE").executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public Pet update(Pet entity) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
            return entity;
        }
    }

    @Override
    public Pet getById(long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.get(Pet.class, id);
        }
    }

    @Override
    public List<Pet> getAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("SELECT p FROM Pet p JOIN FETCH p.owner", Pet.class).list();
        }
    }

    @Override
    public List<Pet> getByName(String name) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM Pet WHERE name = :name", Pet.class)
                    .setParameter("name", name)
                    .list();
        }
    }

    public void createFriendship(Pet pet1, Pet pet2) {
        Long id1 = pet1.getId();
        Long id2 = pet2.getId();

        if (id1.equals(id2)) {
            throw new IllegalArgumentException("Pet cannot be friend with itself");
        }

        Long first = Math.min(id1, id2);
        Long second = Math.max(id1, id2);

        Session session = HibernateConfig.getSessionFactory().openSession();
        session.beginTransaction();

        session.createNativeMutationQuery("""
                        INSERT INTO pet_friends (pet_id, friend_id)
                        VALUES (:first, :second)
                        ON CONFLICT DO NOTHING
                        """)
                .setParameter("first", first)
                .setParameter("second", second)
                .executeUpdate();

        session.getTransaction().commit();
        session.close();
    }

    public List<Pet> getFriendsOf(Pet pet) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<Pet> directFriends = session.createQuery("""
                                SELECT DISTINCT f FROM Pet p
                                JOIN p.friends f
                                LEFT JOIN FETCH f.owner
                                LEFT JOIN FETCH f.friends
                                WHERE p.id = :petId
                            """, Pet.class)
                    .setParameter("petId", pet.getId())
                    .getResultList();

            List<Pet> reverseFriends = session.createQuery("""
                                SELECT DISTINCT p FROM Pet p
                                JOIN p.friends f
                                LEFT JOIN FETCH p.owner
                                LEFT JOIN FETCH p.friends
                                WHERE f.id = :petId
                            """, Pet.class)
                    .setParameter("petId", pet.getId())
                    .getResultList();

            Set<Pet> allFriends = new HashSet<>(directFriends);
            allFriends.addAll(reverseFriends);

            for (Pet friend : allFriends) {
                if (!friend.getFriends().contains(pet)) {
                    friend.getFriends().add(pet);
                }
            }

            return new ArrayList<>(allFriends);
        }
    }

    public Map<Long, List<Pet>> getAllFriends() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<Pet> petsWithFriends = session.createQuery(
                    "SELECT DISTINCT p FROM Pet p " +
                            "LEFT JOIN FETCH p.friends f " +
                            "LEFT JOIN FETCH f.owner " +
                            "LEFT JOIN FETCH p.owner", Pet.class
            ).getResultList();

            Map<Long, List<Pet>> map = new HashMap<>();

            for (Pet pet : petsWithFriends) {
                map.computeIfAbsent(pet.getId(), k -> new ArrayList<>());

                for (Pet friend : pet.getFriends()) {
                    if (!map.get(pet.getId()).contains(friend)) {
                        map.get(pet.getId()).add(friend);
                    }

                    map.computeIfAbsent(friend.getId(), k -> new ArrayList<>());
                    if (!map.get(friend.getId()).contains(pet)) {
                        map.get(friend.getId()).add(pet);
                    }
                }
            }

            return map;
        }
    }
}
