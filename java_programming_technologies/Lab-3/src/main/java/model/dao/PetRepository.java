package model.dao;

import model.entity.Pet;
import model.util.PetColor;
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
                LEFT JOIN FETCH p.owner o
                LEFT JOIN FETCH p.friends f
                LEFT JOIN FETCH f.owner fo
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


    @Query(value = "SELECT p.id, p.name, p.birth_date, p.breed, p.color, p.owner_id, p.tail_length from pet p where p.name = :name", nativeQuery = true)
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
                LEFT JOIN FETCH p.owner o
                LEFT JOIN FETCH p.friends f
                LEFT JOIN FETCH f.owner fo
            """)
    List<Pet> getAllPetsFriends();

    @Query("""
                SELECT DISTINCT p FROM Pet p
                LEFT JOIN FETCH p.owner
                LEFT JOIN FETCH p.friends
            """)
    List<Pet> findAllWithOwnerAndFriends();

    @Query("""
                SELECT DISTINCT p FROM Pet p
                LEFT JOIN FETCH p.owner o
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
}
