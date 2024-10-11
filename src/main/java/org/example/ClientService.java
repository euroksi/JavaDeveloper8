package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {

    public long create(String name) throws SQLException {
        validateName(name);

        String sql = "INSERT INTO clients (name) VALUES (?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("Не вдалося створити клієнта.");
            }
        }
    }

    public String getById(long id) throws SQLException {
        validateId(id);

        String sql = "SELECT name FROM clients WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            } else {
                throw new SQLException("Клієнт з ідентифікатором " + id + " не знайдений.");
            }
        }
    }

    public void setName(long id, String name) throws SQLException {
        validateId(id);
        validateName(name);

        String sql = "UPDATE clients SET name = ? WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setLong(2, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Клієнт з ідентифікатором " + id + " не знайдений.");
            }
        }
    }

    public void deleteById(long id) throws SQLException {
        validateId(id);

        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Клієнт з ідентифікатором " + id + " не знайдений.");
            }
        }
    }

    public List<Client> listAll() throws SQLException {
        List<Client> clients = new ArrayList<>();

        String sql = "SELECT id, name FROM clients";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                clients.add(new Client(id, name));
            }
        }

        return clients;
    }

    private void validateName(String name) {
        if (name == null || name.length() < 3 || name.length() > 100) {
            throw new IllegalArgumentException("Ім'я клієнта повинно містити від 3 до 100 символів.");
        }
    }

    private void validateId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID клієнта має бути більше 0.");
        }
    }

    public static class Client {
        private long id;
        private String name;

        public Client(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Client{id=" + id + ", name='" + name + "'}";
        }
    }
}
