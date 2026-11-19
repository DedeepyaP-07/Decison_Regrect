
package org.example.servlet;

import org.example.dao.AlternativeDAO;
import org.example.dao.CriterionDAO;
import org.example.dao.ScoreDAO;
import org.example.model.Alternative;
import org.example.model.Criterion;
import org.example.model.Score;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/saveScores")
public class SaveScoresServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int decisionId = Integer.parseInt(
                    request.getParameter("decisionId"));

            List<Alternative> alternatives =
                    new AlternativeDAO()
                            .getAlternativesByDecisionId(decisionId);

            List<Criterion> criteria =
                    new CriterionDAO()
                            .getCriteriaByDecisionId(decisionId);

            ScoreDAO scoreDAO = new ScoreDAO();

            for (Alternative alternative : alternatives) {
                for (Criterion criterion : criteria) {
                    String fieldName = "score_"
                            + alternative.getId() + "_"
                            + criterion.getId();

                    String scoreText = request.getParameter(fieldName);

                    if (scoreText == null || scoreText.trim().isEmpty()) {
                        response.sendError(
                                HttpServletResponse.SC_BAD_REQUEST,
                                "Enter a score for every alternative and criterion.");
                        return;
                    }

                    BigDecimal value = new BigDecimal(scoreText);

                    if (value.compareTo(BigDecimal.ONE) < 0
                            || value.compareTo(BigDecimal.TEN) > 0) {
                        response.sendError(
                                HttpServletResponse.SC_BAD_REQUEST,
                                "Scores must be between 1 and 10.");
                        return;
                    }
                }
            }

            // Save only after all submitted scores pass validation.
            for (Alternative alternative : alternatives) {
                for (Criterion criterion : criteria) {
                    String fieldName = "score_"
                            + alternative.getId() + "_"
                            + criterion.getId();

                    BigDecimal value = new BigDecimal(
                            request.getParameter(fieldName));

                    scoreDAO.saveScore(new Score(
                            alternative.getId(),
                            criterion.getId(),
                            value));
                }
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/compare?decisionId=" + decisionId);

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid decision ID or score.");
        } catch (RuntimeException e) {
            throw new ServletException("Unable to save scores.", e);
        }
    }
}
