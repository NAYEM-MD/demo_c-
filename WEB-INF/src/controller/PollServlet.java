package controller;

import dao.PostDAO;
import model.Poll;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/polls/vote")
public class PollServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        User user = (User) session.getAttribute("user");

        try {
            int pollId = Integer.parseInt(request.getParameter("pollId"));
            int optionId = Integer.parseInt(request.getParameter("optionId"));

            boolean success = postDAO.votePoll(user.getId(), pollId, optionId);

            if (success) {
                // Return updated poll data as JSON
                Poll updatedPoll = postDAO.getPoll(pollId);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                
                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"status\":\"success\",");
                json.append("\"totalVotes\":").append(updatedPoll.getOptions().stream().mapToInt(o -> o.getVoteCount()).sum()).append(",");
                json.append("\"options\":[");
                
                for (int i = 0; i < updatedPoll.getOptions().size(); i++) {
                    var opt = updatedPoll.getOptions().get(i);
                    json.append("{");
                    json.append("\"id\":").append(opt.getId()).append(",");
                    json.append("\"count\":").append(opt.getVoteCount());
                    json.append("}");
                    if (i < updatedPoll.getOptions().size() - 1) json.append(",");
                }
                
                json.append("]}");
                response.getWriter().write(json.toString());
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT); // Already voted
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Already voted\"}");
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
