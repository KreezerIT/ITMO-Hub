package kreezerit.Mowner.repository;

import kreezerit.Mowner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    @Query("""
                SELECT DISTINCT o FROM Owner o
            """)
    List<Owner> findAllWithoutPets();

    @Query("""
                SELECT o FROM Owner o WHERE o.name = :name
            """)
    Optional<Owner> getOwnerByName(String name);
}