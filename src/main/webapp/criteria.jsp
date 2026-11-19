
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.model.Decision" %>
<%@ page import="org.example.model.Criterion" %>

<%
    Decision decision = (Decision) request.getAttribute("decision");
    List<Criterion> criteria =
        (List<Criterion>) request.getAttribute("criteria");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Decision Criteria</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6fb;
               padding: 30px; color: #20243a; }
        .container { max-width: 800px; margin: auto; background: white;
                     padding: 28px; border-radius: 14px; }
        h1 { color: #4338ca; }
        input, button { width: 100%; box-sizing: border-box;
                        padding: 12px; margin: 8px 0; }
        button { background: #4338ca; color: white; border: 0;
                 border-radius: 6px; cursor: pointer; }
        .item { background: #f4f6fb; padding: 12px;
                margin-top: 10px; border-radius: 8px; }
    </style>
</head>
<body>
<div class="container">
    <h1>Decision Criteria</h1>
    <h3><%= decision.getTitle() %></h3>

    <p>Enter a criterion and its weight. All weights should total 100.</p>

    <form action="addCriterion" method="post">
        <input type="hidden" name="decisionId"
               value="<%= decision.getId() %>">

        <label>Criterion name</label>
        <input type="text" name="name"
               placeholder="e.g. Price" required>

        <label>Weight (0–100)</label>
        <input type="number" name="weight" min="0.01" max="100"
               step="0.01" placeholder="e.g. 40" required>

        <button type="submit">Save Criterion</button>
    </form>

    <h3>Saved criteria</h3>

    <% if (criteria != null && !criteria.isEmpty()) {
           for (Criterion criterion : criteria) { %>
        <div class="item">
            <strong><%= criterion.getName() %></strong>
            — Weight: <%= criterion.getWeight() %>
        </div>
    <%     }
       } else { %>
        <p>No criteria added yet.</p>
    <% } %>

    <p><a href="viewDecisions">Back to decisions</a></p>
</div>
</body>
</html>
