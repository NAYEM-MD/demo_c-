package model;

import java.io.Serializable;

public class PollOption implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int pollId;
    private String optionText;
    private int voteCount;

    public PollOption() {}

    public PollOption(String optionText) {
        this.optionText = optionText;
        this.voteCount = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPollId() { return pollId; }
    public void setPollId(int pollId) { this.pollId = pollId; }

    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }

    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
}
