
package org.example.dao;

import org.example.model.Criterion;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CriterionDAO {

    public boolean addCriterion(Criterion criterion) {
        String sql = "INSERT INTO criteria " +
                "(decision_id, name, weight) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, criterion.getDecisionId());
            statement.setString(2, criterion.getName());
            statement.setBigDecimal(3, criterion.getWeight());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save criterion", e);
        }
    }

    public List<Criterion> getCriteriaByDecisionId(int decisionId) {
        List<Criterion> criteria = new ArrayList<>();

        String sql = "SELECT id, decision_id, name, weight " +
                "FROM criteria WHERE decision_id = ? ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, decisionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Criterion criterion = new Criterion(
                            resultSet.getInt("decision_id"),
                            resultSet.getString("name"),
                            resultSet.getBigDecimal("weight")
                    );

                    criterion.setId(resultSet.getInt("id"));
                    criteria.add(criterion);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve criteria", e);
        }

        return criteria;
    }
}
