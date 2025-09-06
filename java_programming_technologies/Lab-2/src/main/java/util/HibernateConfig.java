package util;

import entity.Owner;
import entity.Pet;
import io.github.cdimascio.dotenv.Dotenv;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateConfig {
    private static SessionFactory sessionFactory;

    private HibernateConfig() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Dotenv dotenv = dotenvLoad();
                if (Boolean.parseBoolean(dotenv.get("FLYWAY_ENABLED", "true"))) {
                    runFlywayMigrations();
                }

                Configuration configuration = new Configuration();
                configuration.setProperties(getDefaultProperties());
                configuration.addAnnotatedClass(entity.Pet.class);
                configuration.addAnnotatedClass(entity.Owner.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                System.err.println("Hibernate build error: " + e);
                throw new RuntimeException(e);
            }
        }
        return sessionFactory;
    }

    public static void rebuildSessionFactoryWith(Properties customProps) {
        if (sessionFactory != null) {
            sessionFactory.close();
        }

        if (!customProps.containsKey("hibernate.hbm2ddl.auto")) {
            Dotenv dotenv = dotenvLoad();
            if (Boolean.parseBoolean(dotenv.get("FLYWAY_ENABLED", "true"))) {
                runFlywayMigrations();
            }
        }

        Configuration configuration = new Configuration();
        configuration.setProperties(customProps);
        configuration.addAnnotatedClass(Pet.class);
        configuration.addAnnotatedClass(Owner.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    private static Properties getDefaultProperties() {
        Dotenv dotenv = dotenvLoad();

        Properties props = new Properties();
        props.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.setProperty("hibernate.connection.url", dotenv.get("DB_CONNECTION_URL"));
        props.setProperty("hibernate.connection.username", dotenv.get("DB_USERNAME"));
        props.setProperty("hibernate.connection.password", dotenv.get("DB_PASSWORD"));

        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.format_sql", "false");
        return props;
    }

    private static void runFlywayMigrations() {
        Dotenv dotenv = dotenvLoad();

        Flyway flyway = Flyway.configure()
                .dataSource(dotenv.get("DB_MIGRATION_CONNECTION_URL"),
                        dotenv.get("DB_MIGRATION_USERNAME"),
                        dotenv.get("DB_MIGRATION_PASSWORD"))
                .load();

        flyway.migrate();
    }

    private static Dotenv dotenvLoad() {
        return Dotenv.configure()
                .filename("db.env")
                .directory("src/main/resources/db")
                .load();
    }
}