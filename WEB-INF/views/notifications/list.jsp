<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Notification" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Notifications - Galaxy^bbs</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=2">
    <style>
        .notifications-container {
            max-width: 800px;
            margin: 20px auto;
            background: white;
            border-radius: 4px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .notifications-header {
            padding: 15px 20px;
            border-bottom: 1px solid #eee;
        }
        .notification-item {
            padding: 15px 20px;
            border-bottom: 1px solid #eee;
            display: flex;
            align-items: flex-start;
            gap: 15px;
            transition: background 0.2s;
        }
        .notification-item:hover {
            background-color: #f8f9fa;
        }
        .notification-item.unread {
            background-color: #e3f2fd;
        }
        .notification-icon {
            font-size: 24px;
        }
        .notification-content {
            flex: 1;
        }
        .notification-time {
            color: #878a8c;
            font-size: 12px;
            margin-top: 5px;
        }
        .notification-link {
            text-decoration: none;
            color: inherit;
            display: block;
        }
    </style>
</head>
<body>

    <jsp:include page="/common/header.jsp" />

    <div class="notifications-container">
        <div class="notifications-header">
            <h2>Notifications</h2>
        </div>
        
        <div class="notifications-list">
            <%
            List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
            if (notifications != null && !notifications.isEmpty()) {
                for (Notification n : notifications) {
            %>
            <a href="${pageContext.request.contextPath}/posts/detail?id=<%= n.getPostId() %>#comment-<%= n.getCommentId() %>" class="notification-link">
                <div class="notification-item <%= !n.isRead() ? "unread" : "" %>">
                    <div class="notification-icon">💬</div>
                    <div class="notification-content">
                        <div>
                            <strong>u/<%= n.getActorName() %></strong> commented on your post 
                            <strong>"<%= n.getPostTitle() %>"</strong>
                        </div>
                        <div style="color: #555; margin-top: 5px; font-style: italic;">
                            "<%= n.getCommentContent() != null && n.getCommentContent().length() > 50 ? n.getCommentContent().substring(0, 50) + "..." : n.getCommentContent() %>"
                        </div>
                        <div class="notification-time"><%= n.getCreatedAt() %></div>
                    </div>
                </div>
            </a>
            <%
                }
            } else {
            %>
            <div style="padding: 20px; text-align: center; color: #878a8c;">
                No notifications yet.
            </div>
            <% } %>
        </div>
    </div>

</body>
</html>