package model.dao;

import model.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    List<Owner> findOwnerByName(String name);

    @Query("""
                SELECT DISTINCT o FROM Owner o
                LEFT JOIN FETCH o.pets p
            """)
    List<Owner> findAllWithPets();
}