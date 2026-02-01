<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error - Galaxy BBS</title>
    <style>pre { background:#f5f5f5; padding:1em; overflow:auto; }</style>
</head>
<body>
    <h1>Something went wrong</h1>
    <% if (exception != null) { %>
    <p><strong>Exception:</strong> <%= exception.getClass().getName() %></p>
    <p><strong>Message:</strong> <%= exception.getMessage() %></p>
    <% Throwable cause = exception.getCause();
       if (cause != null) { %>
    <p><strong>Caused by:</strong> <%= cause.getClass().getName() %>: <%= cause.getMessage() %></p>
    <% } %>
    <pre><%
        java.io.PrintWriter pw = new java.io.PrintWriter(out);
        exception.printStackTrace(pw);
    %></pre>
    <% } %>
</body>
</html>
