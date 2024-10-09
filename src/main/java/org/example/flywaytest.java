package org.example;

import org.flywaydb.core.Flyway;

public class flywaytest {
    private static flywaytest TestDatabaseQueryService;

    public static void main(String[] args) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:mysql://localhost:3306/ваша_база_даних", "ваш_користувач", "ваш_пароль")
                .locations("classpath:db/migration")
                .load();

        flyway.migrate();

        TestDatabaseQueryService.main(args);
    }
}
