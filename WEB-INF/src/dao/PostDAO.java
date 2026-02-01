package dao;

import model.Post;
import model.Poll;
import model.PollOption;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    
    public List<Post> findAll() {
        return findAll(0);
    }

    public List<Post> findAll(int currentUserId) {
        List<Post> posts = new ArrayList<>();
        // Join with users to get author name, join tags for tag name, subquery for vote count and user vote
        // Explicit columns for ORA-17056 fix
        String sql = "SELECT p.id, p.title, p.content, p.tag_id, p.media_url, p.media_type, p.user_id, p.created_at, " +
                     "u.username as author_name, t.name as tag_name, " +
                     "(SELECT COALESCE(SUM(v.vote_value), 0) FROM votes v WHERE v.post_id = p.id) as vote_count, " +
                     "(SELECT v2.vote_value FROM votes v2 WHERE v2.post_id = p.id AND v2.user_id = ?) as user_vote " +
                     "FROM posts p JOIN users u ON p.user_id = u.id LEFT JOIN tags t ON p.tag_id = t.id ORDER BY p.created_at DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, currentUserId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Post post = mapResultSetToPost(rs);
                    // Load poll if applicable
                    if ("poll".equals(post.getMediaType())) {
                        post.setPoll(getPollByPostId(conn, post.getId()));
                    }
                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
    
    public Post findById(int id) {
        return findById(id, 0);
    }

    public Post findById(int id, int currentUserId) {
        Post post = null;
        String sql = "SELECT p.id, p.title, p.content, p.tag_id, p.media_url, p.media_type, p.user_id, p.created_at, " +
                     "u.username as author_name, t.name as tag_name, " +
                     "(SELECT COALESCE(SUM(v.vote_value), 0) FROM votes v WHERE v.post_id = p.id) as vote_count, " +
                     "(SELECT v2.vote_value FROM votes v2 WHERE v2.post_id = p.id AND v2.user_id = ?) as user_vote " +
                     "FROM posts p JOIN users u ON p.user_id = u.id LEFT JOIN tags t ON p.tag_id = t.id WHERE p.id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, currentUserId);
            pstmt.setInt(2, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    post = mapResultSetToPost(rs);
                    // Load poll if applicable
                    if ("poll".equals(post.getMediaType())) {
                        post.setPoll(getPollByPostId(conn, post.getId()));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
        Post post = new Post();
        // Use indices to avoid ORA-17056
        int i = 1;
        post.setId(rs.getInt(i++));        // 1: p.id
        post.setTitle(rs.getString(i++));  // 2: p.title
        post.setContent(rs.getString(i++));// 3: p.content
        post.setTagId(rs.getInt(i++));     // 4: p.tag_id
        post.setMediaUrl(rs.getString(i++)); // 5: p.media_url
        post.setMediaType(rs.getString(i++)); // 6: p.media_type
        post.setUserId(rs.getInt(i++));    // 7: p.user_id
        post.setCreatedAt(rs.getTimestamp(i++)); // 8: p.created_at
        post.setAuthorName(rs.getString(i++)); // 9: u.username
        post.setTagName(rs.getString(i++)); // 10: t.name
        post.setVoteCount(rs.getInt(i++)); // 11: vote_count
        post.setUserVote(rs.getInt(i++));  // 12: user_vote
        return post;
    }

    public void save(Post post) {
        String sql = "INSERT INTO posts (title, content, tag_id, user_id, media_url, media_type, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"ID"})) { // Explicitly request generated key column "ID"
            
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setInt(3, post.getTagId());
            pstmt.setInt(4, post.getUserId());
            pstmt.setString(5, post.getMediaUrl());
            pstmt.setString(6, post.getMediaType());
            if (post.getCreatedAt() != null) {
                pstmt.setTimestamp(7, new Timestamp(post.getCreatedAt().getTime()));
            } else {
                pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            }
            pstmt.executeUpdate();
            
            // Get generated ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getInt(1));
                    
                    // Save Poll if exists
                    if (post.getPoll() != null) {
                        savePoll(conn, post.getPoll(), post.getId());
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean votePoll(int userId, int pollId, int optionId) {
        // Use explicit columns for checkSql too
        String checkSql = "SELECT id FROM poll_votes WHERE poll_id = ? AND user_id = ?";
        String insertSql = "INSERT INTO poll_votes (poll_id, option_id, user_id) VALUES (?, ?, ?)";
        String updateCountSql = "UPDATE poll_options SET vote_count = vote_count + 1 WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection()) {
            // Check if user already voted
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, pollId);
                checkStmt.setInt(2, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        return false; // Already voted
                    }
                }
            }
            
            conn.setAutoCommit(false);
            try {
                // Record vote
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, pollId);
                    insertStmt.setInt(2, optionId);
                    insertStmt.setInt(3, userId);
                    insertStmt.executeUpdate();
                }
                
                // Update count
                try (PreparedStatement updateStmt = conn.prepareStatement(updateCountSql)) {
                    updateStmt.setInt(1, optionId);
                    updateStmt.executeUpdate();
                }
                
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void savePoll(Connection conn, Poll poll, int postId) throws SQLException {
        String sql = "INSERT INTO polls (post_id, question) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"ID"})) { // Explicitly request "ID"
            pstmt.setInt(1, postId);
            pstmt.setString(2, poll.getQuestion());
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int pollId = rs.getInt(1);
                    for (PollOption option : poll.getOptions()) {
                        savePollOption(conn, option, pollId);
                    }
                }
            }
        }
    }

    private void savePollOption(Connection conn, PollOption option, int pollId) throws SQLException {
        String sql = "INSERT INTO poll_options (poll_id, option_text) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pollId);
            pstmt.setString(2, option.getOptionText());
            pstmt.executeUpdate();
        }
    }
    
    public Poll getPoll(int pollId) {
        try (Connection conn = DBUtil.getConnection()) {
            Poll poll = new Poll();
            // Explicit columns
            String sql = "SELECT id, post_id, question FROM polls WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, pollId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        poll.setId(rs.getInt(1));
                        poll.setPostId(rs.getInt(2));
                        poll.setQuestion(rs.getString(3));
                        poll.setOptions(getPollOptions(conn, pollId));
                    }
                }
            }
            return poll;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Poll getPollByPostId(Connection conn, int postId) throws SQLException {
        Poll poll = null;
        // Explicit columns
        String sql = "SELECT id, post_id, question FROM polls WHERE post_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    poll = new Poll();
                    poll.setId(rs.getInt(1));
                    poll.setPostId(rs.getInt(2));
                    poll.setQuestion(rs.getString(3));
                    poll.setOptions(getPollOptions(conn, poll.getId()));
                }
            }
        }
        return poll;
    }

    private List<PollOption> getPollOptions(Connection conn, int pollId) throws SQLException {
        List<PollOption> options = new ArrayList<>();
        // Explicit columns
        String sql = "SELECT id, poll_id, option_text, vote_count FROM poll_options WHERE poll_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pollId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PollOption option = new PollOption();
                    option.setId(rs.getInt(1));
                    option.setPollId(rs.getInt(2));
                    option.setOptionText(rs.getString(3));
                    option.setVoteCount(rs.getInt(4));
                    options.add(option);
                }
            }
        }
        return options;
    }
}
