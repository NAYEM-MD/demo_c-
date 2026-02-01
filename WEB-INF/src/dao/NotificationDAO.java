package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Notification;
import util.DBUtil;

public class NotificationDAO {

    public void createNotification(Notification notification) {
        String sql = "INSERT INTO notifications (user_id, actor_id, post_id, comment_id, type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, notification.getUserId());
            pstmt.setInt(2, notification.getActorId());
            pstmt.setInt(3, notification.getPostId());
            if (notification.getCommentId() > 0) {
                pstmt.setInt(4, notification.getCommentId());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            pstmt.setString(5, notification.getType());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUnreadCount(int userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = 0";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        // Explicit columns to avoid ORA-17056
        String sql = "SELECT n.id, n.user_id, n.actor_id, n.post_id, n.comment_id, n.type, n.is_read, n.created_at, " +
                     "u.username as actor_name, p.title as post_title, c.content as comment_content " +
                     "FROM notifications n " +
                     "JOIN users u ON n.actor_id = u.id " +
                     "JOIN posts p ON n.post_id = p.id " +
                     "LEFT JOIN comments c ON n.comment_id = c.id " +
                     "WHERE n.user_id = ? " +
                     "ORDER BY n.created_at DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Notification n = new Notification();
                    int i = 1;
                    n.setId(rs.getInt(i++));        // 1: n.id
                    n.setUserId(rs.getInt(i++));    // 2: n.user_id
                    n.setActorId(rs.getInt(i++));   // 3: n.actor_id
                    n.setPostId(rs.getInt(i++));    // 4: n.post_id
                    n.setCommentId(rs.getInt(i++)); // 5: n.comment_id
                    n.setType(rs.getString(i++));   // 6: n.type
                    n.setRead(rs.getBoolean(i++));  // 7: n.is_read
                    n.setCreatedAt(rs.getTimestamp(i++)); // 8: n.created_at
                    n.setActorName(rs.getString(i++));    // 9: actor_name
                    n.setPostTitle(rs.getString(i++));    // 10: post_title
                    n.setCommentContent(rs.getString(i++)); // 11: comment_content
                    notifications.add(n);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public void markAsRead(int userId) {
        String sql = "UPDATE notifications SET is_read = 1 WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
