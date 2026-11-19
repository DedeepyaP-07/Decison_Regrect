
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="org.example.model.Alternative" %>
<%@ page import="org.example.model.Criterion" %>

<%
    List<Alternative> alternatives =
        (List<Alternative>) request.getAttribute("alternatives");
    List<Criterion> criteria =
        (List<Criterion>) request.getAttribute("criteria");
    Map<Integer, BigDecimal> totals =
        (Map<Integer, BigDecimal>) request.getAttribute("totals");
    List<String> errors =
        (List<String>) request.getAttribute("errors");
    Integer decisionId =
        (Integer) request.getAttribute("decisionId");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Decision Comparison</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6fb;
            padding: 30px;
            color: #20243a;
        }
        .container {
            max-width: 900px;
            margin: auto;
            background: white;
            padding: 28px;
            border-radius: 14px;
        }
        h1 { color: #4338ca; }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            border-bottom: 1px solid #e5e7eb;
            text-align: left;
        }
        th { background: #4338ca; color: white; }
        .winner { color: #15803d; font-weight: bold; }
        .error { color: #b91c1c; }
        a { color: #4338ca; }
    </style>
</head>
<body>
<div class="container">
    <h1>Decision Comparison</h1>

    <h3>Weighted scores</h3>

<h3>Enter scores (1–10)</h3>

<% if (alternatives != null && !alternatives.isEmpty()
        && criteria != null && !criteria.isEmpty()) { %>

<form action="saveScores" method="post">
    <input type="hidden" name="decisionId"
           value="<%= decisionId %>">

    <table>
        <thead>
            <tr>
                <th>Alternative</th>
                <% for (Criterion criterion : criteria) { %>
                    <th>
                        <%= criterion.getName() %>
                        (<%= criterion.getWeight() %>%)
                    </th>
                <% } %>
            </tr>
        </thead>
        <tbody>
            <% for (Alternative alternative : alternatives) { %>
                <tr>
                    <td><%= alternative.getName() %></td>

                    <% for (Criterion criterion : criteria) {
                        String fieldName = "score_"
                                + alternative.getId() + "_"
                                + criterion.getId();
                    %>
                        <td>
                            <input
                                type="number"
                                name="<%= fieldName %>"
                                min="1"
                                max="10"
                                step="0.1"
                                required
                                style="width:75px;"
                            >
                        </td>
                    <% } %>
                </tr>
            <% } %>
        </tbody>
    </table>

    <button type="submit"
            style="margin-top:16px;padding:12px;background:#4338ca;color:white;border:0;border-radius:6px;cursor:pointer;">
        Calculate Comparison
    </button>
</form>

<% } else { %>
    <p>Add at least one alternative and one criterion before entering scores.</p>
<% } %>


    <% if (alternatives != null && !alternatives.isEmpty()) { %>
        <table>
            <thead>
                <tr>
                    <th>Alternative</th>
                    <th>Weighted Score / 10</th>

                </tr>
            </thead>
            <tbody>
            <%
                BigDecimal bestScore = null;

                for (Alternative alternative : alternatives) {
                    BigDecimal total = totals.get(alternative.getId());

                    if (total != null &&
                        (bestScore == null || total.compareTo(bestScore) > 0)) {
                        bestScore = total;
                    }
                }

                for (Alternative alternative : alternatives) {
                    BigDecimal total = totals.get(alternative.getId());
            %>
                <tr>
                    <td><%= alternative.getName() %></td>
                    <td>
                        <% if (total != null) { %>
                            <%= total %>
                            <% if (total.equals(bestScore)) { %>
                                <span class="winner"> — Highest score</span>
                            <% } %>
                        <% } else { %>
                            Not calculated
                        <% } %>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    <% } else { %>
        <p>No alternatives found. Add alternatives before comparing.</p>
    <% } %>

    <% if (errors != null && !errors.isEmpty()) { %>
        <h3>Items requiring attention</h3>
        <% for (String error : errors) { %>
            <p class="error"><%= error %></p>
        <% } %>
    <% } %>

    <p><a href="viewDecisions">Back to decisions</a></p>
</div>
</body>
</html>
