<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Galaxy^bbs - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body>

    <div class="container">
        <div class="logo-section">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png" alt="Galaxy^bbs Logo" class="logo">
            <h1>Galaxy^bbs</h1>
            <p class="subtitle">Welcome back! Please login to continue</p>
        </div>

        <% if (request.getAttribute("errorMsg") != null) { %>
            <p class="error-msg"><%= request.getAttribute("errorMsg") %></p>
        <% } %>

        <form action="${pageContext.request.contextPath}/auth/login" method="post">
            <div class="form-group">
                <label>Email</label>
                <input type="text" name="email" placeholder="Enter your email" required autofocus>
            </div>

            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" placeholder="Enter your password" required>
            </div>

            <button type="submit" class="login-btn">Log In</button>
            
            <div class="footer-links">
                <a href="#" class="forgot-password">Forgot password?</a>
                <p>Don't have an account? <a href="${pageContext.request.contextPath}/auth/register" class="create-account">Create an account</a></p>
            </div>
        </form>
        
        <div class="legal">
            <a href="#">Terms of Service</a> &bull; <a href="#">Privacy Policy</a>
        </div>
    </div>

</body>
</html>
