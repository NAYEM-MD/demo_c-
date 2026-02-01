<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Context info</title></head>
<body>
<h1>Galaxy BBS – deployment check</h1>
<p><strong>Context path:</strong> <%= request.getContextPath() %></p>
<p><strong>Request URI:</strong> <%= request.getRequestURI() %></p>
<p>Use this as the app root: <code><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= request.getContextPath() %></code></p>
<p>Login page: <a href="<%= request.getContextPath() %>/auth/login"><%= request.getContextPath() %>/auth/login</a></p>
</body>
</html>
