package dao;

import model.Tag;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {

    public List<Tag> findAll() {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT * FROM tags ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                tags.add(new Tag(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }
    
    public void initTags() {
        // Helper to ensure tags exist
        String countSql = "SELECT COUNT(*) FROM tags";
        String insertSql = "INSERT INTO tags (name) VALUES (?)";
        
        try (Connection conn = DBUtil.getConnection()) {
            boolean empty = true;
            try (PreparedStatement pstmt = conn.prepareStatement(countSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    empty = false;
                }
            }
            
            if (empty) {
                String[] defaultTags = {"General", "Technology", "Gaming", "Music", "News"};
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    for (String tag : defaultTags) {
                        pstmt.setString(1, tag);
                        pstmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
