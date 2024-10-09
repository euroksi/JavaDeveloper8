package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseQueryService {

    public List<Map<String, Object>> executeSelectQuery(String query) {
        List<Map<String, Object>> result = new ArrayList<>();

        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(query);

            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new java.util.HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    row.put(columnName, rs.getObject(i));
                }
                result.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Помилка під час виконання запиту: " + e.getMessage());
        }

        return result;
    }

    public List<MaxProjectCountClient> findMaxProjectsClient() {
        String query = "SELECT name, COUNT(project_id) AS project_count " +
                "FROM clients " +
                "JOIN projects ON clients.client_id = projects.client_id " +
                "GROUP BY name " +
                "ORDER BY project_count DESC;";

        List<MaxProjectCountClient> result = new ArrayList<>();
        List<Map<String, Object>> queryResult = executeSelectQuery(query);

        for (Map<String, Object> row : queryResult) {
            String name = (String) row.get("name");
            int projectCount = ((Number) row.get("project_count")).intValue();
            result.add(new MaxProjectCountClient(name, projectCount));
        }

        return result;
    }
}
