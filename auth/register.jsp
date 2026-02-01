<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Galaxy^bbs - Sign Up</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body>

    <div class="container">
        <div class="logo-section">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png" alt="Galaxy^bbs Logo" class="logo">
            <h1>Galaxy^bbs</h1>
            <p class="subtitle">Create an account to join the community</p>
        </div>

        <% if (request.getAttribute("errorMsg") != null) { %>
            <p class="error-msg"><%= request.getAttribute("errorMsg") %></p>
        <% } %>

        <form action="${pageContext.request.contextPath}/auth/register" method="post">
            <div class="form-group">
                <label>Email</label>
                <input type="text" name="email" placeholder="Enter your email" required>
            </div>

            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" placeholder="Choose a username" required>
            </div>

            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" placeholder="Create a password" required>
            </div>

            <button type="submit" class="login-btn">Sign Up</button>
            
            <div class="footer-links">
                <p>Already have an account? <a href="${pageContext.request.contextPath}/auth/login" class="create-account">Log in</a></p>
            </div>
        </form>
        
        <div class="legal">
            <a href="#">Terms of Service</a> &bull; <a href="#">Privacy Policy</a>
        </div>
    </div>

</body>
</html>
