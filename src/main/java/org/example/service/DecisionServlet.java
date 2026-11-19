
package org.example.servlet;

import org.example.dao.DecisionDAO;
import org.example.model.Decision;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/addDecision")
public class DecisionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        String title = request.getParameter("title");
        String description = request.getParameter("description");

        if (title == null || title.trim().isEmpty()) {
            response.sendError(400, "Decision title is required.");
            return;
        }

        try {
            Decision decision = new Decision(
                    title.trim(),
                    description == null ? "" : description.trim()
            );

            DecisionDAO dao = new DecisionDAO();

            if (dao.addDecision(decision)) {
                response.sendRedirect(
                        request.getContextPath() + "/index.html?success=1"
                );
            } else {
                response.sendError(500, "Decision was not saved.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            getServletContext().log("ERROR while saving decision", e);

            response.setStatus(500);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println("Error: "
                    + e.getClass().getSimpleName());
            Throwable cause = e;

            while (cause.getCause() != null) {
                cause = cause.getCause();
            }

            response.getWriter().println(
                    "Actual error: " + cause.getClass().getName()
            );

            response.getWriter().println(
                    "Details: " + cause.getMessage()
            );
        }
    }
}
