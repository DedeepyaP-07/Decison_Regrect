

package org.example.servlet;

import org.example.dao.AlternativeDAO;
import org.example.dao.DecisionDAO;
import org.example.model.Alternative;
import org.example.model.Decision;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/manageAlternatives")
public class ManageAlternativesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int decisionId = Integer.parseInt(
                    request.getParameter("decisionId"));

            DecisionDAO decisionDAO = new DecisionDAO();
            AlternativeDAO alternativeDAO = new AlternativeDAO();

            List<Decision> decisions = decisionDAO.getAllDecisions();
            Decision selectedDecision = null;

            for (Decision decision : decisions) {
                // Find the selected decision by ID.
                // This requires the Decision model to expose getId().
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

            List<Alternative> alternatives =
                    alternativeDAO.getAlternativesByDecisionId(decisionId);

            request.setAttribute("decision", selectedDecision);
            request.setAttribute("alternatives", alternatives);

            request.getRequestDispatcher("/alternatives.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "A valid decision ID is required.");
        } catch (RuntimeException e) {
            throw new ServletException(
                    "Unable to load alternatives.", e);
        }
    }
}
