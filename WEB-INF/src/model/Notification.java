package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private int actorId;
    private int postId;
    private int commentId;
    private String type;
    private boolean isRead;
    private Timestamp createdAt;
    
    // Additional fields for display
    private String actorName;
    private String postTitle;
    private String commentContent;

    public Notification() {}

    public Notification(int userId, int actorId, int postId, Integer commentId, String type) {
        this.userId = userId;
        this.actorId = actorId;
        this.postId = postId;
        this.commentId = commentId != null ? commentId : 0;
        this.type = type;
        this.isRead = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getActorId() { return actorId; }
    public void setActorId(int actorId) { this.actorId = actorId; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getActorName() { return actorName; }
    public void setActorName(String actorName) { this.actorName = actorName; }

    public String getPostTitle() { return postTitle; }
    public void setPostTitle(String postTitle) { this.postTitle = postTitle; }

    public String getCommentContent() { return commentContent; }
    public void setCommentContent(String commentContent) { this.commentContent = commentContent; }
}
