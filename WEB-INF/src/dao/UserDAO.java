package dao;

import model.User;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    
    public User findByUsername(String username) {
        User user = null;
        String sql = "SELECT id, username, password, email FROM users WHERE username = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt(1));
                    user.setUsername(rs.getString(2));
                    String pwd = rs.getString(3);
                    user.setPassword(pwd != null ? pwd.trim() : null);
                    user.setEmail(rs.getString(4));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    public User findByEmail(String email) {
        User user = null;
        String sql = "SELECT id, username, password, email FROM users WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email != null ? email : "");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt(1));
                    user.setUsername(rs.getString(2));
                    String pwd = rs.getString(3);
                    user.setPassword(pwd != null ? pwd.trim() : null);
                    user.setEmail(rs.getString(4));
                    System.out.println("User found via email: " + email);
                } else {
                    System.out.println("User not found via email: " + email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void save(User user) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public User checkLogin(String email, String password) {
        System.out.println("Checking login for: " + email);
        User user = findByEmail(email);
        if (user != null) {
            System.out.println("User exists. Checking password...");
            String stored = user.getPassword();
            String inputPwd = (password != null) ? password.trim() : "";
            if (stored != null && stored.equals(inputPwd)) {
                System.out.println("Password match!");
                return user;
            } else {
                System.out.println("Password mismatch!");
            }
        }
        return null;
    }
}
