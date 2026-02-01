package controller;

import dao.UserDAO;
import model.User;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

public class AuthServlet extends HttpServlet {
    private UserDAO userDAO; // lazy init in doPost so doGet never touches DB

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath(); // exact: /auth/login, /auth/register, /auth/logout
        switch (path) {
            case "/auth/login":
                forwardTo(request, response, "/auth/login.jsp");
                break;
            case "/auth/register":
                forwardTo(request, response, "/auth/register.jsp");
                break;
            case "/auth/logout":
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                response.sendRedirect(request.getContextPath() + "/auth/login");
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void forwardTo(HttpServletRequest request, HttpServletResponse response, String jspPath) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher(jspPath);
        if (rd == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        rd.forward(request, response);
    }

    private UserDAO getUserDAO() {
        if (userDAO == null) userDAO = new UserDAO();
        return userDAO;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/auth/login".equals(path)) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            if (email != null) email = email.trim();
            if (password != null) password = password.trim();
            if (email == null) email = "";
            if (password == null) password = "";
            
            System.out.println("Login attempt for: " + email); // Debug log
            
            User user = getUserDAO().checkLogin(email, password);
            if (user != null) {
                System.out.println("Login successful for: " + email); // Debug log
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/posts/feed");
            } else {
                System.out.println("Login failed for: " + email); // Debug log
                request.setAttribute("errorMsg", "Invalid email or password");
                forwardTo(request, response, "/auth/login.jsp");
            }
        } else if ("/auth/register".equals(path)) {
            String email = request.getParameter("email");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (email != null) email = email.trim();
            if (username != null) username = username.trim();
            if (password != null) password = password.trim();
            if (email == null) email = "";
            if (username == null) username = "";
            if (password == null) password = "";

            System.out.println("Registering user: " + email); // Debug log

            if (getUserDAO().findByEmail(email) != null) {
                request.setAttribute("errorMsg", "Email already registered");
                forwardTo(request, response, "/auth/register.jsp");
                return;
            }

            User user = new User(username, password, email);
            getUserDAO().save(user);
            System.out.println("User registered successfully: " + email); // Debug log
            
            // Auto login or redirect to login
            response.sendRedirect(request.getContextPath() + "/auth/login");
        }
    }
}
