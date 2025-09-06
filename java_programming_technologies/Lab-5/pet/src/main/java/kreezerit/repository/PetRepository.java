package kreezerit.repository;


import kreezerit.Mcommon.util.PetColor;
import kreezerit.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("""
                SELECT DISTINCT p FROM Pet p
                LEFT JOIN FETCH p.friends f
                WHERE p.id IN (
                    SELECT f.id FROM Pet pet
                    JOIN pet.friends f
                    WHERE pet.id = :petId
                )
                OR p.id IN (
                    SELECT pet.id FROM Pet pet
                    JOIN pet.friends f
                    WHERE f.id = :petId
                )
            """)
    List<Pet> findAllFriendsWithOwnerAndFriends(@Param("petId") Long petId);


    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.friends WHERE p.name = :name")
    List<Pet> getPetByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = """
            
                INSERT INTO pet_friends (pet_id, friend_id)
            VALUES (:first, :second)
            ON CONFLICT DO NOTHING
            """, nativeQuery = true)
    void createFriendship(@Param("first") Long first, @Param("second") Long second);


    @Query("""
                SELECT DISTINCT p FROM Pet p
                LEFT JOIN FETCH p.friends f
            """)
    List<Pet> getAllPetsFriends();

    @Query("""
                SELECT DISTINCT p FROM Pet p
                LEFT JOIN FETCH p.friends
            """)
    List<Pet> findAllWithOwnerAndFriends();

    @Query("""
                SELECT DISTINCT p FROM Pet p
                LEFT JOIN FETCH p.friends f
                WHERE (:color IS NULL OR p.color = :color)
                  AND (:breed IS NULL OR p.breed LIKE CONCAT('%', :breed, '%'))
            """)
    List<Pet> searchWithFetch(@Param("color") PetColor color,
                              @Param("breed") String breed);


    @Query("""
                SELECT COUNT(p) FROM Pet p
                WHERE (:color IS NULL OR p.color = :color)
                  AND (:breed IS NULL OR p.breed LIKE CONCAT('%', :breed, '%'))
            """)
    long countSearch(@Param("color") PetColor color,
                     @Param("breed") String breed);

    @Query("SELECT p.ownerId FROM Pet p WHERE p.id = :petId")
    Long findOwnerIdByPetId(@Param("petId") Long petId);
}
