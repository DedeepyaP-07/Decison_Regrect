
package org.example.servlet;

import org.example.dao.CriterionDAO;
import org.example.model.Criterion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/addCriterion")
public class AddCriterionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int decisionId = Integer.parseInt(
                    request.getParameter("decisionId"));

            String name = request.getParameter("name");
            String weightText = request.getParameter("weight");

            if (name == null || name.trim().isEmpty()) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Criterion name is required.");
                return;
            }

            BigDecimal weight = new BigDecimal(weightText);

            if (weight.compareTo(BigDecimal.ZERO) <= 0
                    || weight.compareTo(new BigDecimal("100")) > 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Weight must be greater than 0 and at most 100.");
                return;
            }

            Criterion criterion = new Criterion(
                    decisionId, name.trim(), weight);

            new CriterionDAO().addCriterion(criterion);

            response.sendRedirect(
                    request.getContextPath()
                            + "/manageCriteria?decisionId=" + decisionId);

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid decision ID or weight.");
        } catch (RuntimeException e) {
            throw new ServletException("Unable to save criterion.", e);
        }
    }
}
