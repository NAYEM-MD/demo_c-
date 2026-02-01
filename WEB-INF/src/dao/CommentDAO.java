package dao;

import model.Comment;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {
    
    public List<Comment> findByPostId(int postId) {
        List<Comment> comments = new ArrayList<>();
        // Explicit columns to avoid ORA-17056
        String sql = "SELECT c.id, c.content, c.post_id, c.user_id, c.created_at, u.username as author_name " +
                     "FROM comments c JOIN users u ON c.user_id = u.id WHERE c.post_id = ? ORDER BY c.created_at ASC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    int i = 1;
                    comment.setId(rs.getInt(i++));        // 1: c.id
                    comment.setContent(rs.getString(i++));// 2: c.content
                    comment.setPostId(rs.getInt(i++));    // 3: c.post_id
                    comment.setUserId(rs.getInt(i++));    // 4: c.user_id
                    comment.setCreatedAt(rs.getTimestamp(i++)); // 5: c.created_at
                    comment.setAuthorName(rs.getString(i++));   // 6: u.username
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public void save(Comment comment) {
        String sql = "INSERT INTO comments (content, post_id, user_id, created_at) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"ID"})) { // Explicitly request generated key "ID"
            
            pstmt.setString(1, comment.getContent());
            pstmt.setInt(2, comment.getPostId());
            pstmt.setInt(3, comment.getUserId());
            if (comment.getCreatedAt() != null) {
                pstmt.setTimestamp(4, new Timestamp(comment.getCreatedAt().getTime()));
            } else {
                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            }
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comment.setId(generatedKeys.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
