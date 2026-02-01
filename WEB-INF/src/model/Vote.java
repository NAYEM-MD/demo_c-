package model;

import java.io.Serializable;

public class Vote implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private int postId;
    private int voteValue; // 1 for upvote, -1 for downvote

    public Vote() {}

    public Vote(int userId, int postId, int voteValue) {
        this.userId = userId;
        this.postId = postId;
        this.voteValue = voteValue;
    }
    
    // ... id, userId, postId ...

    public int getVoteValue() { return voteValue; }
    public void setVoteValue(int voteValue) { this.voteValue = voteValue; }
}
