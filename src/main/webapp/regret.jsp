
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="org.example.model.Alternative" %>
<%@ page import="org.example.model.RegretRecord" %>

<%
    Integer decisionId = (Integer) request.getAttribute("decisionId");

    List<Alternative> alternatives =
            (List<Alternative>) request.getAttribute("alternatives");

    Map<Integer, BigDecimal> totals =
            (Map<Integer, BigDecimal>) request.getAttribute("totals");

    List<RegretRecord> regretRecords =
            (List<RegretRecord>) request.getAttribute("regretRecords");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Regret Tracker</title>

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
            padding: 30px;
            border-radius: 14px;
            box-shadow: 0 8px 25px rgba(0,0,0,.08);
        }

        h1 { color: #4338ca; }

        select, button {
            width: 100%;
            box-sizing: border-box;
            padding: 12px;
            margin: 12px 0;
        }

        button {
            background: #4338ca;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }

        button:hover {
            background: #3730a3;
        }

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

        th {
            background: #4338ca;
            color: white;
        }

        .note { color: #626779; }
        .success { color: #15803d; font-weight: bold; }

        .regret-value {
            font-weight: bold;
            color: #4338ca;
        }

        .history {
            margin-top: 35px;
        }
    </style>
</head>

<body>
<div class="container">

    <h1>Regret Tracker</h1>

    <p class="note">
        Select the alternative you would choose. The system compares
        its weighted score with the highest available score.
    </p>

    <% if ("1".equals(request.getParameter("saved"))) { %>
        <p class="success">Regret record saved successfully!</p>
    <% } %>

    <h3>Available Alternatives</h3>

    <% if (alternatives != null && !alternatives.isEmpty()) { %>

        <table>
            <thead>
                <tr>
                    <th>Alternative</th>
                    <th>Weighted Score</th>
                </tr>
            </thead>
            <tbody>
                <% for (Alternative alternative : alternatives) {
                    BigDecimal total = totals.get(alternative.getId());
                %>
                    <tr>
                        <td><%= alternative.getName() %></td>
                        <td>
                            <%= total != null ? total : "Scores incomplete" %>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>

        <form action="trackRegret" method="post">
            <input type="hidden" name="decisionId"
                   value="<%= decisionId %>">

            <label for="chosenAlternativeId">
                Which alternative would you choose?
            </label>

            <select id="chosenAlternativeId"
                    name="chosenAlternativeId" required>
                <option value="">Select an alternative</option>

                <% for (Alternative alternative : alternatives) {
                    if (totals.containsKey(alternative.getId())) {
                %>
                    <option value="<%= alternative.getId() %>">
                        <%= alternative.getName() %>
                    </option>
                <%  }
                   }
                %>
            </select>

            <button type="submit">Calculate and Save Regret</button>
        </form>

    <% } else { %>
        <p>No alternatives found. Add alternatives and scores first.</p>
    <% } %>

    <div class="history">
        <hr>
        <h3>Regret History</h3>

        <% if (regretRecords != null && !regretRecords.isEmpty()) { %>

            <table>
                <thead>
                    <tr>
                        <th>Chosen Alternative ID</th>
                        <th>Best Score</th>
                        <th>Chosen Score</th>
                        <th>Regret Value</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (RegretRecord record : regretRecords) { %>
                        <tr>
                            <td>
                                <%
                                    String alternativeName = "Unknown";
                                    for (Alternative alternative : alternatives) {
                                        if (alternative.getId() == record.getChosenAlternativeId()) {
                                            alternativeName = alternative.getName();
                                            break;
                                        }
                                    }
                                %>
                                <%= alternativeName %>
                            </td>
                            <td><%= record.getBestScore() %></td>
                            <td><%= record.getChosenScore() %></td>
                            <td class="regret-value">
                                <%= record.getRegretValue() %>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>

        <% } else { %>
            <p class="note">No regret records saved yet.</p>
        <% } %>
    </div>

    <p><a href="viewDecisions">Back to saved decisions</a></p>

</div>
</body>
</html>
