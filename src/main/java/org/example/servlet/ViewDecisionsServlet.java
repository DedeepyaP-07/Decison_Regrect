
package org.example.servlet;

import org.example.dao.DecisionDAO;
import org.example.model.Decision;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/viewDecisions")
public class ViewDecisionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            DecisionDAO dao = new DecisionDAO();
            List<Decision> decisions = dao.getAllDecisions();

            request.setAttribute("decisions", decisions);

            request.getRequestDispatcher("/decisions.jsp")
                    .forward(request, response);

        } catch (RuntimeException e) {
            throw new ServletException(
                    "Unable to retrieve decisions", e
            );
        }
    }
}
