package DatabaseReader;

import Database.DatabaseManager;
import DatabaseReader.DatabaseReader;

import java.sql.*;
import java.util.*;


public class DatabaseReader{
    private DatabaseManager dbManager;

    public DatabaseReader(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<Map<String, Object>> getAllRows(String query) {
        List<Map<String, Object>> rows = new ArrayList<>();

        try (Connection connection = dbManager.getDbConnector();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue);
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
      dbManager.closeConnection();
    }

        return rows;
    }

    public String queryBuilder(String tableName, String[] columns) {
        StringBuilder query = new StringBuilder("SELECT ");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(" FROM ").append(tableName);
        return query.toString();
    }

    public List<Map<String, Object>> getAllRowsWithParameters(String query, Object[] params) {
        List<Map<String, Object>> rows = new ArrayList<>();

        try (Connection connection = dbManager.getDbConnector();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object columnValue = resultSet.getObject(i);
                        row.put(columnName, columnValue);
                    }
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
      dbManager.closeConnection();
    }
        return rows;
    }
}
