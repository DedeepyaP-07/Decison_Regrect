
package org.example.servlet;

import org.example.dao.AlternativeDAO;
import org.example.model.Alternative;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/addAlternative")
public class AlternativeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int decisionId = Integer.parseInt(
                    request.getParameter("decisionId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            if (name == null || name.trim().isEmpty()) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Alternative name is required.");
                return;
            }

            Alternative alternative = new Alternative(
                    decisionId, name.trim(), description);

            new AlternativeDAO().addAlternative(alternative);

            response.sendRedirect(
                    request.getContextPath()
                            + "/manageAlternatives?decisionId=" + decisionId);

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid decision ID.");
        } catch (RuntimeException e) {
            throw new ServletException("Unable to save alternative.", e);
        }
    }
}
