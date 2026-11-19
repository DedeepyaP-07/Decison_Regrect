
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.model.Decision" %>

<%
    List<Decision> decisions =
        (List<Decision>) request.getAttribute("decisions");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Saved Decisions</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6fb;
            margin: 0;
            padding: 40px 20px;
            color: #20243a;
        }

        .container {
            max-width: 1100px;
            margin: auto;
            background: white;
            padding: 30px;
            border-radius: 14px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
            overflow-x: auto;
        }

        h1 {
            color: #4338ca;
        }

        .add-link {
            display: inline-block;
            margin-bottom: 20px;
            color: #4338ca;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }

        th, td {
            padding: 14px;
            border-bottom: 1px solid #e5e7eb;
            text-align: left;
            vertical-align: middle;
        }

        th {
            background: #4338ca;
            color: white;
        }

        td a {
            color: #4338ca;
            line-height: 1.6;
        }

        .empty {
            padding: 20px 0;
            color: #626779;
        }
    </style>
</head>

<body>
<div class="container">

    <h1>My Saved Decisions</h1>

    <a class="add-link" href="index.html">+ Add New Decision</a>

    <% if (decisions != null && !decisions.isEmpty()) { %>

        <table>
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Compare Options</th>
                    <th>Criteria</th>
                    <th>Compare Scores</th>
                    <th>Regret Tracker</th>
                </tr>
            </thead>

            <tbody>
            <% for (Decision decision : decisions) { %>

                <tr>
                    <td><%= decision.getTitle() %></td>

                    <td><%= decision.getDescription() %></td>

                    <td>
                        <a href="manageAlternatives?decisionId=<%= decision.getId() %>">
                            Manage Alternatives
                        </a>
                    </td>

                    <td>
                        <a href="manageCriteria?decisionId=<%= decision.getId() %>">
                            Manage Criteria
                        </a>
                    </td>

                    <td>
                        <a href="compare?decisionId=<%= decision.getId() %>">
                            Compare Scores
                        </a>
                    </td>
                    <td>
                        <a href="trackRegret?decisionId=<%= decision.getId() %>">
                            Track Regret
                        </a>
                    </td>
                </tr>

            <% } %>
            </tbody>
        </table>

    <% } else { %>

        <p class="empty">No decisions saved yet.</p>

    <% } %>

</div>
</body>
</html>
