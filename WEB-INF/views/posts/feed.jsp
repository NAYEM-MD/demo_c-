<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Post" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Galaxy^bbs</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/feed.css">
</head>
<body>

    <jsp:include page="/common/header.jsp" />

    <div class="main-container">
        <!-- Feed Column -->
        <div class="feed-column">
            <% 
            List<Post> posts = (List<Post>) request.getAttribute("posts");
            if (posts != null && !posts.isEmpty()) {
                for (Post post : posts) {
            %>
            <div class="post-card" onclick="location.href='${pageContext.request.contextPath}/posts/detail?id=<%= post.getId() %>'">
                <div class="vote-column">
                    <div class="vote-arrow up <%= post.getUserVote() == 1 ? "active" : "" %>" onclick="vote(<%= post.getId() %>, 1, this); event.stopPropagation();">⬆</div>
                    <div class="vote-count"><%= post.getVoteCount() %></div>
                    <div class="vote-arrow down <%= post.getUserVote() == -1 ? "active" : "" %>" onclick="vote(<%= post.getId() %>, -1, this); event.stopPropagation();">⬇</div>
                </div>
                <div class="post-content">
                    <div class="post-meta">
                        <span class="community-name">r/<%= post.getTagName() != null ? post.getTagName() : "general" %></span>
                        <span class="posted-by">Posted by u/<%= post.getAuthorName() %></span>
                    </div>
                    <h3 class="post-title"><%= post.getTitle() %></h3>
                    <div class="post-preview">
                        <%= post.getContent() != null ? (post.getContent().length() > 200 ? post.getContent().substring(0, 200) + "..." : post.getContent()) : "" %>
                    </div>
                    <% if (post.getMediaUrl() != null) { %>
                    <div class="post-media" style="margin-top: 10px;">
                        <% if ("image".equals(post.getMediaType())) { %>
                            <img src="<%= post.getMediaUrl() %>" style="max-width: 100%; max-height: 500px; border-radius: 4px;">
                        <% } else if ("video".equals(post.getMediaType())) { %>
                            <video src="<%= post.getMediaUrl() %>" controls style="max-width: 100%; max-height: 500px; border-radius: 4px;"></video>
                        <% } %>
                    </div>
                    <% } %>
                    
                    <% if ("poll".equals(post.getMediaType()) && post.getPoll() != null) { 
                        int totalVotes = 0;
                        for (model.PollOption option : post.getPoll().getOptions()) {
                            totalVotes += option.getVoteCount();
                        }
                    %>
                    <div class="poll-container" id="poll-<%= post.getPoll().getId() %>" onclick="event.stopPropagation()">
                        <h3><%= post.getPoll().getQuestion() %></h3>
                        <div class="poll-options">
                            <% for (model.PollOption option : post.getPoll().getOptions()) { 
                                int percent = totalVotes > 0 ? (int)((double)option.getVoteCount() / totalVotes * 100) : 0;
                            %>
                                <div class="poll-option-display" onclick="votePoll(<%= post.getPoll().getId() %>, <%= option.getId() %>); event.stopPropagation()">
                                    <div class="poll-progress" id="progress-<%= option.getId() %>" style="width: <%= percent %>%;"></div>
                                    <span class="poll-option-text"><%= option.getOptionText() %></span>
                                    <span class="poll-percentage" id="percent-<%= option.getId() %>"><%= percent %>%</span>
                                </div>
                            <% } %>
                        </div>
                        <div class="poll-footer">
                            <span class="live-indicator"></span>
                            <span id="total-votes-<%= post.getPoll().getId() %>">Live | <%= totalVotes %> votes</span>
                        </div>
                    </div>
                    <% } %>
                    
                    <div class="post-footer">
                        <div class="footer-btn">💬 Comments</div>
                    </div>
                </div>
            </div>
            <% 
                }
            } else {
            %>
                <div class="post-card">
                    <div class="post-content" style="padding: 20px; text-align: center;">
                        <p>No posts yet. Be the first to create one!</p>
                    </div>
                </div>
            <% } %>
        </div>

        <!-- Sidebar Column -->
        <div class="sidebar-column">
            <div class="sidebar-card">
                <div class="card-header">
                    <h3>Trending Today</h3>
                </div>
                <div class="card-content">
                    <div class="trending-item">
                        <div class="trend-icon"></div>
                        <div class="trend-info">
                            <h4>r/technology</h4>
                            <p>25.4M members</p>
                        </div>
                    </div>
                    <div class="trending-item">
                        <div class="trend-icon"></div>
                        <div class="trend-info">
                            <h4>r/gaming</h4>
                            <p>18.2M members</p>
                        </div>
                    </div>
                    <div class="trending-item">
                        <div class="trend-icon"></div>
                        <div class="trend-info">
                            <h4>r/askreddit</h4>
                            <p>45.1M members</p>
                        </div>
                    </div>
                    <div class="trending-item">
                        <div class="trend-icon"></div>
                        <div class="trend-info">
                            <h4>r/funny</h4>
                            <p>52.3M members</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="sidebar-card">
                <div class="card-header">
                    <h3>About</h3>
                </div>
                <div class="card-content">
                    <p>Welcome to Galaxy^bbs! Join communities, share content, and connect with people who share your interests.</p>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/vote.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/poll.js"></script>
</body>
</html>