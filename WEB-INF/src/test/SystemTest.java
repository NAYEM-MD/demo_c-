package test;

import dao.UserDAO;
import dao.PostDAO;
import dao.CommentDAO;
import dao.NotificationDAO;
import model.User;
import model.Post;
import model.Comment;
import model.Notification;
import util.DBUtil;

import java.io.File;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class SystemTest {

    private static boolean checkFile(String relativePath) {
        File file = new File("C:/tomcat10/webapps/galaxy_test/" + relativePath);
        boolean exists = file.exists();
        System.out.println((exists ? "[PASS]" : "[FAIL]") + " File exists: " + relativePath);
        return exists;
    }

    public static void main(String[] args) {
        System.out.println("========== System Integration Test ==========");
        boolean allPassed = true;

        // 1. File Structure Check
        System.out.println("\n[1] Checking File Structure (JSP Locations)...");
        allPassed &= checkFile("auth/login.jsp");
        allPassed &= checkFile("auth/register.jsp");
        allPassed &= checkFile("posts/feed.jsp");
        allPassed &= checkFile("posts/create.jsp");
        allPassed &= checkFile("posts/detail.jsp");
        allPassed &= checkFile("notifications/list.jsp");
        
        // 2. Database Connection Check
        System.out.println("\n[2] Checking Database Connection...");
        try (Connection conn = DBUtil.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("[PASS] Database connection established.");
            } else {
                System.out.println("[FAIL] Database connection failed.");
                allPassed = false;
            }
        } catch (Exception e) {
            System.out.println("[FAIL] Database connection error: " + e.getMessage());
            e.printStackTrace();
            allPassed = false;
        }

        // 3. Logic Flow Test
        System.out.println("\n[3] Testing Core Logic Flow (DAO Layer)...");
        try {
            // User
            UserDAO userDAO = new UserDAO();
            String testEmail = "test_" + System.currentTimeMillis() + "@example.com";
            User user = new User("TestUser", "password", testEmail);
            userDAO.save(user);
            user = userDAO.findByEmail(testEmail);
            
            if (user != null) {
                System.out.println("[PASS] User creation and retrieval.");
            } else {
                System.out.println("[FAIL] User creation failed.");
                allPassed = false;
            }

            // Post
            if (user != null) {
                PostDAO postDAO = new PostDAO();
                Post post = new Post();
                post.setTitle("System Test Post");
                post.setContent("This is a test post.");
                post.setUserId(user.getId());
                post.setTagId(1); // Default tag
                postDAO.save(post);
                
                List<Post> posts = postDAO.findAll(user.getId());
                if (!posts.isEmpty() && posts.get(0).getTitle().equals("System Test Post")) {
                    System.out.println("[PASS] Post creation and retrieval.");
                    
                    // Comment
                    CommentDAO commentDAO = new CommentDAO();
                    Comment comment = new Comment();
                    comment.setContent("Test comment");
                    comment.setPostId(posts.get(0).getId());
                    comment.setUserId(user.getId());
                    comment.setCreatedAt(new Date()); // FIX: Initialize createdAt
                    commentDAO.save(comment);
                    
                    List<Comment> comments = commentDAO.findByPostId(posts.get(0).getId());
                    if (!comments.isEmpty()) {
                        System.out.println("[PASS] Comment creation and retrieval.");
                    } else {
                        System.out.println("[FAIL] Comment creation failed.");
                        allPassed = false;
                    }
                    
                    // Notification
                    NotificationDAO notificationDAO = new NotificationDAO();
                    Notification n = new Notification();
                    n.setUserId(user.getId());
                    n.setActorId(user.getId());
                    n.setPostId(posts.get(0).getId());
                    n.setType("like");
                    notificationDAO.createNotification(n);
                    
                    if (!notificationDAO.getNotificationsByUserId(user.getId()).isEmpty()) {
                        System.out.println("[PASS] Notification creation and retrieval.");
                    } else {
                        System.out.println("[FAIL] Notification creation failed.");
                        allPassed = false;
                    }

                } else {
                    System.out.println("[FAIL] Post creation failed.");
                    allPassed = false;
                }
            }

        } catch (Exception e) {
            System.out.println("[FAIL] Logic test error: " + e.getMessage());
            e.printStackTrace();
            allPassed = false;
        }

        System.out.println("\n========== Test Summary ==========");
        if (allPassed) {
            System.out.println("ALL TESTS PASSED");
        } else {
            System.out.println("SOME TESTS FAILED");
        }
    }
}
