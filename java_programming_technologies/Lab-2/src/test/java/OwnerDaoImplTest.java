import dao.persistence.OwnerDaoImpl;
import entity.Owner;
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
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OwnerDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

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
    }

    @BeforeEach
    void cleanUp() {
        ownerDao.deleteAll();
    }

    @Test
    void testSaveAndGetById() {
        Owner saved = ownerDao.save(Owner.builder()
                .name("Test User")
                .birthDate(LocalDate.now())
                .pets(List.of())
                .build());
        Owner fetched = ownerDao.getById(saved.getId());

        assertNotNull(fetched);
        assertEquals("Test User", fetched.getName());
    }

    @Test
    void testUpdate() {
        Owner saved = ownerDao.save(Owner.builder()
                .name("Old Name")
                .birthDate(LocalDate.now())
                .pets(List.of())
                .build());

        saved.setName("New Name");
        Owner updated = ownerDao.update(saved);

        assertEquals("New Name", updated.getName());
    }

    @Test
    void testDeleteById() {
        Owner saved = ownerDao.save(Owner.builder()
                .name("A").birthDate(LocalDate.now())
                .pets(List.of())
                .build());
        ownerDao.deleteById(saved.getId());

        assertNull(ownerDao.getById(saved.getId()));
    }

    @Test
    void testGetAll() {
        ownerDao.save(Owner.builder()
                .name("A")
                .birthDate(LocalDate.now())
                .pets(List.of())
                .build());
        ownerDao.save(Owner.builder().name("B").birthDate(LocalDate.now()).pets(List.of()).build());

        List<Owner> all = ownerDao.getAll();
        assertEquals(2, all.size());
    }

    @Test
    void testGetByName() {
        ownerDao.save(Owner.builder()
                .name("Alice")
                .birthDate(LocalDate.now())
                .pets(List.of())
                .build());
        ownerDao.save(Owner.builder()
                .name("Bob")
                .birthDate(LocalDate.now())
                .pets(List.of())
                .build());

        List<Owner> result = ownerDao.getByName("Alice");
        assertEquals(1, result.size());
        assertEquals("Alice", result.getFirst().getName());
    }


}
