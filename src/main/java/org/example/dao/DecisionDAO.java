
package org.example.dao;

import org.example.model.Decision;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class DecisionDAO {

    public boolean addDecision(Decision decision) {

        String sql = "INSERT INTO decisions (title, description) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, decision.getTitle());
            statement.setString(2, decision.getDescription());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to save decision to database", e
            );
        }
    }

    public List<Decision> getAllDecisions() {

        List<Decision> decisions = new ArrayList<>();

        String sql = "SELECT id, title, description, created_at " +
                "FROM decisions ORDER BY id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Decision decision = new Decision(
                        resultSet.getString("title"),
                        resultSet.getString("description")
                );
                decision.setId(resultSet.getInt("id"));
                decisions.add(decision);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Failed to retrieve decisions", e
            );
        }

        return decisions;
    }

}
