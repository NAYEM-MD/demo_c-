package controller;

import dao.VoteDAO;
import model.User;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/vote")
public class VoteServlet extends HttpServlet {
    private VoteDAO voteDAO = new VoteDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Not logged in\"}");
            return;
        }

        User user = (User) session.getAttribute("user");
        
        try {
            int postId = Integer.parseInt(request.getParameter("postId"));
            int value = Integer.parseInt(request.getParameter("value"));
            
            // Validate value (must be 1 or -1)
            if (value != 1 && value != -1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid vote value\"}");
                return;
            }

            int newCount = voteDAO.vote(user.getId(), postId, value);
            int userVote = voteDAO.getUserVote(user.getId(), postId);
            
            response.getWriter().write("{\"success\": true, \"newCount\": " + newCount + ", \"userVote\": " + userVote + "}");
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid parameters\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error\"}");
        }
    }
}
