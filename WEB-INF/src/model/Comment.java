package model;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String content;
    private int postId;
    private int userId;
    private String authorName; // Transient field for display
    private Date createdAt;

    public Comment() {}

    public Comment(String content, int postId, int userId) {
        this.content = content;
        this.postId = postId;
        this.userId = userId;
        this.createdAt = new Date();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
