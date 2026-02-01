package model;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String content;
    private int tagId; // Replaces community
    private String tagName; // Transient field for display
    private int userId;
    private String authorName; // Transient field for display
    private String mediaUrl;
    private String mediaType;
    private int voteCount; // Transient field
    private int userVote; // Transient field: 1 (up), -1 (down), 0 (none)
    private Poll poll; // Transient field for poll data
    private Date createdAt;

    public Post() {}

    public Post(String title, String content, int tagId, int userId) {
        this.title = title;
        this.content = content;
        this.tagId = tagId;
        this.userId = userId;
        this.createdAt = new Date();
    }
    
    public Post(String title, String content, int tagId, int userId, String mediaUrl, String mediaType) {
        this(title, content, tagId, userId);
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public int getTagId() { return tagId; }
    public void setTagId(int tagId) { this.tagId = tagId; }

    public String getTagName() { return tagName; }
    public void setTagName(String tagName) { this.tagName = tagName; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    
    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
    
    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }

    public int getUserVote() { return userVote; }
    public void setUserVote(int userVote) { this.userVote = userVote; }

    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
