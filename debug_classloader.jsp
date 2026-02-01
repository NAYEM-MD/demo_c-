<%@ page import="java.io.*,java.util.*,java.net.*" %>
<html>
<head><title>Debug ClassLoader</title></head>
<body>
<h1>ClassLoader Debug</h1>
<h2>Context Info</h2>
Context Path: <%= request.getContextPath() %><br>
Servlet Path: <%= request.getServletPath() %><br>

<h2>Class Availability</h2>
<%
    String[] classesToCheck = {
        "controller.AuthServlet",
        "model.User",
        "util.DBUtil"
    };
    
    for(String className : classesToCheck) {
        try {
            Class.forName(className);
            out.println("<div style='color:green'>Found: " + className + "</div>");
        } catch(ClassNotFoundException e) {
            out.println("<div style='color:red'>Not Found: " + className + " (" + e.getMessage() + ")</div>");
        } catch(Exception e) {
             out.println("<div style='color:red'>Error loading " + className + ": " + e.toString() + "</div>");
        }
    }
%>

<h2>Classpath Resources</h2>
<pre>
<%
    ClassLoader cl = this.getClass().getClassLoader();
    out.println("ClassLoader: " + cl.getClass().getName());
    
    // Check WEB-INF/classes
    URL resource = cl.getResource("/");
    out.println("Root resource: " + resource);
    
    URL servletClass = cl.getResource("controller/AuthServlet.class");
    out.println("AuthServlet.class resource: " + servletClass);
%>
</pre>
</body>
</html>