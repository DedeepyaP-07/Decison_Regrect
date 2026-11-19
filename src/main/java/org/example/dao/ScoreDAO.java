
package org.example.dao;

import org.example.model.Score;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    public void saveScore(Score score) {
        String sql = "INSERT INTO scores " +
                "(alternative_id, criterion_id, score) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE score = VALUES(score)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, score.getAlternativeId());
            statement.setInt(2, score.getCriterionId());
            statement.setBigDecimal(3, score.getScore());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save score", e);
        }
    }

    public List<Score> getScoresByAlternativeId(int alternativeId) {
        List<Score> scores = new ArrayList<>();

        String sql = "SELECT id, alternative_id, criterion_id, score " +
                "FROM scores WHERE alternative_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, alternativeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Score score = new Score(
                            resultSet.getInt("alternative_id"),
                            resultSet.getInt("criterion_id"),
                            resultSet.getBigDecimal("score")
                    );

                    score.setId(resultSet.getInt("id"));
                    scores.add(score);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve scores", e);
        }

        return scores;
    }
}
