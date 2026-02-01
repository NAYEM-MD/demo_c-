package controller;

import dao.NotificationDAO;
import model.Notification;
import model.User;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/notifications/*")
public class NotificationServlet extends HttpServlet {
    private NotificationDAO notificationDAO = new NotificationDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        User user = (User) session.getAttribute("user");

        String path = request.getPathInfo();
        if (path == null || "/".equals(path)) {
            List<Notification> notifications = notificationDAO.getNotificationsByUserId(user.getId());
            request.setAttribute("notifications", notifications);
            
            // Mark all as read when viewing the list (optional, or do it via explicit action)
            // For better UX, usually reading the list marks them as read, or clicking individual ones.
            // Let's mark all as read when opening the page for simplicity.
            notificationDAO.markAsRead(user.getId());
            
            request.getRequestDispatcher("/WEB-INF/views/notifications/list.jsp").forward(request, response);
        } else if ("/count".equals(path)) {
            // API endpoint for AJAX to get count
            int count = notificationDAO.getUnreadCount(user.getId());
            response.setContentType("application/json");
            response.getWriter().write("{\"count\": " + count + "}");
        }
    }
}
