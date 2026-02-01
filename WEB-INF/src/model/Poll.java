package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Poll implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int postId;
    private String question;
    private List<PollOption> options = new ArrayList<>();

    public Poll() {}

    public Poll(int postId, String question) {
        this.postId = postId;
        this.question = question;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public List<PollOption> getOptions() { return options; }
    public void setOptions(List<PollOption> options) { this.options = options; }
    
    public void addOption(PollOption option) {
        this.options.add(option);
    }
}
