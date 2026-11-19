
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.model.Decision" %>
<%@ page import="org.example.model.Alternative" %>

<%
    Decision decision = (Decision) request.getAttribute("decision");
    List<Alternative> alternatives =
        (List<Alternative>) request.getAttribute("alternatives");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Alternatives</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6fb;
               padding: 30px; color: #20243a; }
        .container { max-width: 800px; margin: auto; background: white;
                     padding: 28px; border-radius: 14px; }
        h1 { color: #4338ca; }
        input, textarea, button { width: 100%; box-sizing: border-box;
                                  padding: 12px; margin: 8px 0; }
        button { background: #4338ca; color: white; border: 0;
                 border-radius: 6px; cursor: pointer; }
        .item { padding: 12px; margin-top: 10px;
                background: #f4f6fb; border-radius: 8px; }
    </style>
</head>
<body>
<div class="container">
    <h1>Manage Alternatives</h1>

    <h3>Decision: <%= decision.getTitle() %></h3>
    <p><%= decision.getDescription() %></p>

    <h3>Add a comparison option</h3>
    <form action="addAlternative" method="post">
        <input type="hidden" name="decisionId"
               value="<%= decision.getId() %>">

        <label>Alternative name</label>
        <input type="text" name="name" placeholder="e.g. Laptop A" required>

        <label>Description</label>
        <textarea name="description"
                  placeholder="Describe this option"></textarea>

        <button type="submit">Save Alternative</button>
    </form>

    <h3>Saved alternatives</h3>
    <% if (alternatives != null && !alternatives.isEmpty()) {
           for (Alternative alternative : alternatives) { %>
        <div class="item">
            <strong><%= alternative.getName() %></strong>
            <p><%= alternative.getDescription() == null
                    ? "" : alternative.getDescription() %></p>
        </div>
    <%     }
       } else { %>
        <p>No alternatives added yet.</p>
    <% } %>

    <p><a href="viewDecisions">Back to decisions</a></p>
</div>
</body>
</html>
