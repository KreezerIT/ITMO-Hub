import dao.persistence.OwnerDaoImpl;
import dao.persistence.PetDaoImpl;
import entity.Owner;
import entity.Pet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import util.HibernateConfig;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PetDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private PetDaoImpl petDao;
    private OwnerDaoImpl ownerDao;

    @BeforeAll
    void setup() {
        postgres.start();
        Properties props = new Properties();
        props.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        props.setProperty("hibernate.connection.username", postgres.getUsername());
        props.setProperty("hibernate.connection.password", postgres.getPassword());
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        props.setProperty("hibernate.show_sql", "false");

        HibernateConfig.rebuildSessionFactoryWith(props);
        ownerDao = new OwnerDaoImpl();
        petDao = new PetDaoImpl();
    }

    @BeforeEach
    void cleanUp() {
        petDao.deleteAll();
        ownerDao.deleteAll();
    }

    private Owner createOwner() {
        return ownerDao.save(Owner.builder()
                .name("Owner One")
                .birthDate(LocalDate.of(2000, 1, 1))
                .pets(List.of())
                .build());
    }

    @Test
    void testSaveAndGetById() {
        Owner owner = createOwner();

        Pet saved = petDao.save(Pet.builder().name("Klop")
                .breed("kosoy")
                .birthDate(LocalDate.now())
                .owner(owner)
                .build());

        Pet fetched = petDao.getById(saved.getId());
        assertNotNull(fetched);
        assertEquals("Klop", fetched.getName());
    }

    @Test
    void testUpdate() {
        Owner owner = createOwner();
        Pet pet = petDao.save(Pet.builder()
                .name("Tarelka")
                .breed("Imba")
                .birthDate(LocalDate.now())
                .owner(owner)
                .build());

        pet.setName("Tarelka new");
        Pet updated = petDao.update(pet);

        assertEquals("Tarelka new", updated.getName());
    }

    @Test
    void testDeleteById() {
        Owner owner = createOwner();
        Pet pet = petDao.save(Pet.builder()
                .name("NamedToDelete")
                .breed("ktoto")
                .birthDate(LocalDate.now())
                .owner(owner)
                .build());

        petDao.deleteById(pet.getId());
        assertNull(petDao.getById(pet.getId()));
    }

    @Test
    void testGetAll() {
        Owner owner = createOwner();

        petDao.save(Pet.builder()
                .name("Pet1")
                .breed("norm").birthDate(LocalDate.now())
                .owner(owner)
                .build());
        petDao.save(Pet.builder().name("Pet2")
                .breed("normis")
                .birthDate(LocalDate.now())
                .owner(owner)
                .build());

        List<Pet> all = petDao.getAll();
        assertEquals(2, all.size());
    }

    @Test
    void testGetByName() {
        Owner owner = createOwner();

        petDao.save(Pet.builder()
                .name("Kyper")
                .breed("Dog")
                .birthDate(LocalDate.now())
                .owner(owner)
                .build());
        petDao.save(Pet.builder()
                .name("Pit")
                .breed("Cat")
                .birthDate(LocalDate.now())
                .owner(owner)
                .build());

        List<Pet> result = petDao.getByName("Kyper");
        assertEquals(1, result.size());
        assertEquals("Kyper", result.getFirst().getName());
    }

    @Test
    void testCreateFriendship() {
        Owner owner1 = createOwner();
        Owner owner2 = createOwner();

        Pet pet1 = petDao.save(Pet.builder().name("Loshara").breed("Donniy").birthDate(LocalDate.now()).owner(owner1).build());
        Pet pet2 = petDao.save(Pet.builder().name("Chmo").breed("Obichnoe").birthDate(LocalDate.now()).owner(owner2).build());

        petDao.createFriendship(pet1, pet2);

        List<Pet> pet1Friends = petDao.getFriendsOf(pet1);
        List<Pet> pet2Friends = petDao.getFriendsOf(pet2);

        assertTrue(pet1Friends.contains(pet2));
        assertTrue(pet2Friends.contains(pet1));
    }

    @Test
    void testCreateFriendshipWithSamePet() {
        Owner owner1 = createOwner();
        Pet pet = petDao.save(Pet.builder().name("Lox").breed("Podzaborniy").birthDate(LocalDate.now()).owner(owner1).build());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> petDao.createFriendship(pet, pet));
        assertEquals("Pet cannot be friend with itself", exception.getMessage());
    }

    @Test
    void testGetFriendsOf() {
        Owner owner1 = createOwner();
        Owner owner2 = createOwner();

        Pet pet1 = petDao.save(Pet.builder().name("Chyrka").breed("lala").birthDate(LocalDate.now()).owner(owner1).build());
        Pet pet2 = petDao.save(Pet.builder().name("Okyn").breed("bulbul").birthDate(LocalDate.now()).owner(owner2).build());

        petDao.createFriendship(pet1, pet2);

        List<Pet> pet1Friends = petDao.getFriendsOf(pet1);
        assertEquals(1, pet1Friends.size());
        assertTrue(pet1Friends.contains(pet2));
    }

    @Test
    void testGetAllFriends() {
        Owner owner1 = createOwner();
        Owner owner2 = createOwner();

        Pet pet1 = petDao.save(Pet.builder().name("Lakomka").breed("Pitbulchik").birthDate(LocalDate.now()).owner(owner1).build());
        Pet pet2 = petDao.save(Pet.builder().name("Chery").breed("Yagodka").birthDate(LocalDate.now()).owner(owner2).build());
        Pet pet3 = petDao.save(Pet.builder().name("Bell").breed("Zvonok").birthDate(LocalDate.now()).owner(owner1).build());

        petDao.createFriendship(pet1, pet2);
        petDao.createFriendship(pet2, pet3);

        Map<Long, List<Pet>> allFriends = petDao.getAllFriends();

        assertTrue(allFriends.containsKey(pet1.getId()));
        assertTrue(allFriends.containsKey(pet2.getId()));
        assertTrue(allFriends.containsKey(pet3.getId()));

        assertTrue(allFriends.get(pet1.getId()).contains(pet2));
        assertTrue(allFriends.get(pet2.getId()).contains(pet1));
        assertTrue(allFriends.get(pet2.getId()).contains(pet3));
        assertTrue(allFriends.get(pet3.getId()).contains(pet2));
    }
}
