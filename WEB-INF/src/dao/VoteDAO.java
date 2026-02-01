package dao;

import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteDAO {
    
    // Cast a vote (insert or update)
    // Returns the new total vote count for the post
    public int vote(int userId, int postId, int value) {
        // First check if vote exists
        String checkSql = "SELECT vote_value FROM votes WHERE user_id = ? AND post_id = ?";
        String insertSql = "INSERT INTO votes (user_id, post_id, vote_value) VALUES (?, ?, ?)";
        String updateSql = "UPDATE votes SET vote_value = ? WHERE user_id = ? AND post_id = ?";
        String deleteSql = "DELETE FROM votes WHERE user_id = ? AND post_id = ?";
        
        try (Connection conn = DBUtil.getConnection()) {
            int existingValue = 0;
            boolean exists = false;
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userId);
                checkStmt.setInt(2, postId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        existingValue = rs.getInt("vote_value");
                        exists = true;
                    }
                }
            }
            
            if (exists) {
                if (existingValue == value) {
                    // Clicking same vote again removes it (toggle off)
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                        deleteStmt.setInt(1, userId);
                        deleteStmt.setInt(2, postId);
                        deleteStmt.executeUpdate();
                    }
                } else {
                    // Change vote (up to down or down to up)
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, value);
                        updateStmt.setInt(2, userId);
                        updateStmt.setInt(3, postId);
                        updateStmt.executeUpdate();
                    }
                }
            } else {
                // New vote
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, postId);
                    insertStmt.setInt(3, value);
                    insertStmt.executeUpdate();
                }
            }
            
            return getVoteCount(postId);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int getVoteCount(int postId) {
        String sql = "SELECT SUM(vote_value) as total FROM votes WHERE post_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int getUserVote(int userId, int postId) {
        String sql = "SELECT vote_value FROM votes WHERE user_id = ? AND post_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("vote_value");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
