package controller;

import dao.CommentDAO;
import dao.PostDAO;
import dao.TagDAO;
import model.Comment;
import model.Post;
import model.Tag;
import model.User;
import model.Poll;
import model.PollOption;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/posts/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class PostServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAO();
    private CommentDAO commentDAO = new CommentDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) path = "/feed";
        
        HttpSession session = request.getSession(false);
        int currentUserId = 0;
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            currentUserId = user.getId();
        }
        
        switch (path) {
            case "/feed":
                List<Post> posts = postDAO.findAll(currentUserId);
                request.setAttribute("posts", posts);
                request.getRequestDispatcher("/WEB-INF/views/posts/feed.jsp").forward(request, response);
                break;
            case "/create":
                if (currentUserId == 0) {
                    response.sendRedirect(request.getContextPath() + "/auth/login");
                    return;
                }
                request.getRequestDispatcher("/WEB-INF/views/posts/create.jsp").forward(request, response);
                break;
            case "/detail":
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    Post post = postDAO.findById(id, currentUserId);
                    List<Comment> comments = commentDAO.findByPostId(id);
                    request.setAttribute("post", post);
                    request.setAttribute("comments", comments);
                    request.getRequestDispatcher("/WEB-INF/views/posts/detail.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/posts/feed");
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if ("/create".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/auth/login");
                return;
            }
            User user = (User) session.getAttribute("user");
            
            String title = request.getParameter("title");
            String content = request.getParameter("content"); // This is the rich text body
            String postType = request.getParameter("postType");
            int tagId = 1; // Default to "General" since selection is removed from UI
            
            String mediaUrl = null;
            String mediaType = null;
            Poll poll = null;
            
            if ("media".equals(postType)) {
                Part filePart = request.getPart("mediaFile");
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = getSubmittedFileName(filePart);
                    String contentType = filePart.getContentType();
                    
                    // Determine file extension
                    String extension = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension = fileName.substring(i);
                    }
                    
                    // Generate unique filename
                    String uniqueFileName = UUID.randomUUID().toString() + extension;
                    
                    // Get upload directory
                    String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdir();
                    
                    // Save file
                    filePart.write(uploadPath + File.separator + uniqueFileName);
                    
                    mediaUrl = request.getContextPath() + "/uploads/" + uniqueFileName;
                    
                    if (contentType.startsWith("image/")) {
                        mediaType = "image";
                    } else if (contentType.startsWith("video/")) {
                        mediaType = "video";
                    }
                }
            } else if ("poll".equals(postType)) {
                String pollQuestion = request.getParameter("pollQuestion");
                String[] pollOptions = request.getParameterValues("pollOption");
                
                if (pollQuestion != null && !pollQuestion.trim().isEmpty() && 
                    pollOptions != null && pollOptions.length >= 2) {
                    
                    poll = new Poll();
                    poll.setQuestion(pollQuestion);
                    for (String optionText : pollOptions) {
                        if (optionText != null && !optionText.trim().isEmpty()) {
                            poll.addOption(new PollOption(optionText));
                        }
                    }
                    mediaType = "poll";
                    // Clear content if it's a poll, or keep it if we want hybrid posts. 
                    // User UI shows separate tabs, so likely separate.
                    content = null; 
                }
            }
            
            Post post = new Post(title, content, tagId, user.getId(), mediaUrl, mediaType);
            post.setPoll(poll);
            postDAO.save(post);
            
            response.sendRedirect(request.getContextPath() + "/posts/feed");
        }
    }
    
    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
