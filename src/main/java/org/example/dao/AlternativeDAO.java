
package org.example.dao;

import org.example.model.Alternative;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlternativeDAO {

    public boolean addAlternative(Alternative alternative) {
        String sql = "INSERT INTO alternatives " +
                "(decision_id, name, description) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, alternative.getDecisionId());
            statement.setString(2, alternative.getName());
            statement.setString(3, alternative.getDescription());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save alternative", e);
        }
    }

    public List<Alternative> getAlternativesByDecisionId(int decisionId) {
        List<Alternative> alternatives = new ArrayList<>();

        String sql = "SELECT id, decision_id, name, description " +
                "FROM alternatives WHERE decision_id = ? ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, decisionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Alternative alternative = new Alternative(
                            resultSet.getInt("decision_id"),
                            resultSet.getString("name"),
                            resultSet.getString("description")
                    );

                    alternative.setId(resultSet.getInt("id"));
                    alternatives.add(alternative);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve alternatives", e);
        }

        return alternatives;
    }
}
