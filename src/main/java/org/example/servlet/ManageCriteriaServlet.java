
package org.example.servlet;

import org.example.dao.CriterionDAO;
import org.example.dao.DecisionDAO;
import org.example.model.Criterion;
import org.example.model.Decision;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/manageCriteria")
public class ManageCriteriaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int decisionId = Integer.parseInt(
                    request.getParameter("decisionId"));

            List<Decision> decisions =
                    new DecisionDAO().getAllDecisions();

            Decision selectedDecision = null;

            for (Decision decision : decisions) {
                if (decision.getId() == decisionId) {
                    selectedDecision = decision;
                    break;
                }
            }

            if (selectedDecision == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Decision not found.");
                return;
            }

            List<Criterion> criteria =
                    new CriterionDAO().getCriteriaByDecisionId(decisionId);

            request.setAttribute("decision", selectedDecision);
            request.setAttribute("criteria", criteria);

            request.getRequestDispatcher("/criteria.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "A valid decision ID is required.");
        } catch (RuntimeException e) {
            throw new ServletException(
                    "Unable to load criteria.", e);
        }
    }
}
