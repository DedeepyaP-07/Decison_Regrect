
package org.example.servlet;

import org.example.dao.AlternativeDAO;
import org.example.dao.RegretDAO;
import org.example.dao.ScoreDAO;
import org.example.dao.CriterionDAO;
import org.example.model.Alternative;
import org.example.model.Criterion;
import org.example.model.RegretRecord;
import org.example.model.Score;
import org.example.service.ScoringService;
import org.example.service.RegretService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/trackRegret")
public class RegretServlet extends HttpServlet {

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

            Map<Integer, BigDecimal> totals = new HashMap<>();
            ScoringService scoringService = new ScoringService();
            ScoreDAO scoreDAO = new ScoreDAO();

            for (Alternative alternative : alternatives) {
                List<Score> scores = scoreDAO
                        .getScoresByAlternativeId(alternative.getId());

                try {
                    BigDecimal total = scoringService
                            .calculateWeightedScore(criteria, scores);
                    totals.put(alternative.getId(), total);
                } catch (IllegalArgumentException ignored) {
                    // Skip alternatives whose scores are incomplete.
                }
            }

            request.setAttribute("decisionId", decisionId);
            request.setAttribute("alternatives", alternatives);
            request.setAttribute("totals", totals);

            RegretDAO regretDAO = new RegretDAO();
            request.setAttribute(
                    "regretRecords",
                    regretDAO.getRegretsByDecisionId(decisionId)
            );

            request.getRequestDispatcher("/regret.jsp")
                    .forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "A valid decision ID is required.");
        } catch (RuntimeException e) {
            throw new ServletException(
                    "Unable to load regret tracker.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int decisionId = Integer.parseInt(
                    request.getParameter("decisionId"));
            int chosenId = Integer.parseInt(
                    request.getParameter("chosenAlternativeId"));

            List<Alternative> alternatives =
                    new AlternativeDAO()
                            .getAlternativesByDecisionId(decisionId);

            List<Criterion> criteria =
                    new CriterionDAO()
                            .getCriteriaByDecisionId(decisionId);

            ScoringService scoringService = new ScoringService();
            ScoreDAO scoreDAO = new ScoreDAO();

            Map<Integer, BigDecimal> totals = new HashMap<>();

            for (Alternative alternative : alternatives) {
                List<Score> scores =
                        scoreDAO.getScoresByAlternativeId(
                                alternative.getId());

                try {
                    totals.put(alternative.getId(),
                            scoringService.calculateWeightedScore(
                                    criteria, scores));
                } catch (IllegalArgumentException ignored) {
                    // Incomplete alternatives are not eligible.
                }
            }

            Alternative chosen = null;
            for (Alternative alternative : alternatives) {
                if (alternative.getId() == chosenId) {
                    chosen = alternative;
                    break;
                }
            }

            if (chosen == null || !totals.containsKey(chosenId)
                    || totals.isEmpty()) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Choose an alternative with complete scores.");
                return;
            }

            BigDecimal bestScore = totals.values().stream()
                    .max(BigDecimal::compareTo)
                    .orElseThrow();

            RegretRecord record = new RegretService()
                    .calculateRegret(
                            decisionId,
                            chosenId,
                            bestScore,
                            totals.get(chosenId));

            new RegretDAO().saveRegret(record);

            response.sendRedirect(
                    request.getContextPath()
                            + "/trackRegret?decisionId=" + decisionId
                            + "&saved=1");

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid decision or alternative ID.");
        } catch (RuntimeException e) {
            throw new ServletException(
                    "Unable to save regret record.", e);
        }
    }
}
