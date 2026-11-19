
package org.example.dao;

import org.example.model.RegretRecord;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegretDAO {

    public boolean saveRegret(RegretRecord record) {
        String sql = "INSERT INTO regret_records " +
                "(decision_id, chosen_alternative_id, best_score, " +
                "chosen_score, regret_value) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, record.getDecisionId());
            statement.setInt(2, record.getChosenAlternativeId());
            statement.setBigDecimal(3, record.getBestScore());
            statement.setBigDecimal(4, record.getChosenScore());
            statement.setBigDecimal(5, record.getRegretValue());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save regret record", e);
        }
    }

    public List<RegretRecord> getRegretsByDecisionId(int decisionId) {
        List<RegretRecord> records = new ArrayList<>();

        String sql = "SELECT id, decision_id, chosen_alternative_id, " +
                "best_score, chosen_score, regret_value " +
                "FROM regret_records WHERE decision_id = ? ORDER BY id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, decisionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    RegretRecord record = new RegretRecord(
                            resultSet.getInt("decision_id"),
                            resultSet.getInt("chosen_alternative_id"),
                            resultSet.getBigDecimal("best_score"),
                            resultSet.getBigDecimal("chosen_score"),
                            resultSet.getBigDecimal("regret_value")
                    );

                    record.setId(resultSet.getInt("id"));
                    records.add(record);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve regret records", e);
        }

        return records;
    }
}
