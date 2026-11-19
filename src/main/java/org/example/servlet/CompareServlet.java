
package org.example.servlet;

import org.example.dao.AlternativeDAO;
import org.example.dao.CriterionDAO;
import org.example.dao.ScoreDAO;
import org.example.model.Alternative;
import org.example.model.Criterion;
import org.example.model.Score;
import org.example.service.ScoringService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/compare")
public class CompareServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

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
            ScoringService scoringService = new ScoringService();

            Map<Integer, BigDecimal> totals = new HashMap<>();
            List<String> errors = new ArrayList<>();

            for (Alternative alternative : alternatives) {
                List<Score> scores =
                        scoreDAO.getScoresByAlternativeId(
                                alternative.getId());

                try {
                    BigDecimal total =
                            scoringService.calculateWeightedScore(
                                    criteria, scores);

                    totals.put(alternative.getId(), total);
                } catch (IllegalArgumentException e) {
                    errors.add(alternative.getName()
                            + ": " + e.getMessage());
                }
            }

            request.setAttribute("alternatives", alternatives);
            request.setAttribute("criteria", criteria);
            request.setAttribute("totals", totals);
            request.setAttribute("errors", errors);
            request.setAttribute("decisionId", decisionId);

            request.getRequestDispatcher("/comparison.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "A valid decision ID is required.");
        } catch (RuntimeException e) {
            throw new ServletException(
                    "Unable to compare alternatives.", e);
        }
    }
}
