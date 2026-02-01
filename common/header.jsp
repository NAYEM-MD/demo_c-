<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="dao.NotificationDAO" %>
<%
    User currentUser = (User) session.getAttribute("user");
    int unreadCount = 0;
    if (currentUser != null) {
        NotificationDAO notificationDAO = new NotificationDAO();
        unreadCount = notificationDAO.getUnreadCount(currentUser.getId());
    }
%>
<header class="app-header">
    <div class="header-left">
        <a href="${pageContext.request.contextPath}/posts/feed" class="logo-link">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png" alt="Galaxy^bbs" class="header-logo" onerror="this.style.display='none';this.nextElementSibling.style.display='block'">
            <span class="logo-text">Galaxy^bbs</span>
        </a>
    </div>
    
    <div class="header-center">
        <div class="search-bar">
            <input type="text" placeholder="Search posts...">
        </div>
    </div>
    
    <div class="header-right">
        <a href="${pageContext.request.contextPath}/posts/create" class="create-post-btn">
            <span class="plus-icon">+</span> Create
        </a>
        
        <div class="user-actions">
            <% if (currentUser != null) { %>
            <a href="${pageContext.request.contextPath}/notifications" class="icon-btn" title="Notifications">
                <svg viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" class="icon">
                    <path d="M10 2a6 6 0 00-6 6v3.586l-.707.707A1 1 0 004 14h12a1 1 0 00.707-1.707L16 11.586V8a6 6 0 00-6-6zM10 18a3 3 0 01-3-3h6a3 3 0 01-3 3z"/>
                </svg>
                <% if (unreadCount > 0) { %>
                    <span class="badge" style="position: absolute; top: 0; right: 0; background: #ff4500; color: white; border-radius: 50%; padding: 1px 4px; font-size: 10px; min-width: 12px; text-align: center; line-height: 1;"><%= unreadCount %></span>
                <% } %>
            </a>

            <!-- User Dropdown -->
            <div class="user-menu-container">
                <button id="user-menu-btn" class="user-menu-btn" onclick="toggleUserMenu(event)">
                    <div class="user-avatar-small">
                        <img src="${pageContext.request.contextPath}/assets/img/default-avatar.png" onerror="this.src='https://www.redditstatic.com/avatars/avatar_default_02_0079D3.png'" alt="u/<%= currentUser.getUsername() %>">
                    </div>
                    <div class="user-info-brief">
                        <span class="username-text">u/<%= currentUser.getUsername() %></span>
                        <span class="karma-text">1 karma</span>
                    </div>
                    <span class="dropdown-arrow">▼</span>
                </button>

                <div id="user-dropdown" class="user-dropdown-content">
                    <div class="dropdown-section-header">
                        <span class="section-icon">👤</span>
                        <div class="section-info">
                            <span class="section-title">My Stuff</span>
                            <span class="section-subtitle">Online Status: On</span>
                        </div>
                    </div>

                    <a href="${pageContext.request.contextPath}/profile?username=<%= currentUser.getUsername() %>" class="dropdown-item">
                        <span class="item-icon">
                            <!-- Simple Profile Icon -->
                            <svg viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" class="menu-icon"><path d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"/></svg>
                        </span>
                        View Profile
                    </a>

                    <div class="dropdown-item" onclick="toggleDisplayMode(event)">
                        <span class="item-icon">
                            <!-- Moon Icon -->
                            <svg viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" class="menu-icon"><path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z"/></svg>
                        </span>
                        <span>Display Mode</span>
                        <div class="toggle-switch">
                            <input type="checkbox" id="darkModeToggle">
                            <span class="slider round"></span>
                        </div>
                    </div>

                    <a href="${pageContext.request.contextPath}/settings" class="dropdown-item">
                        <span class="item-icon">
                            <!-- Settings Icon -->
                            <svg viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" class="menu-icon"><path fill-rule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd"/></svg>
                        </span>
                        Settings
                    </a>

                    <div class="dropdown-divider"></div>

                    <a href="${pageContext.request.contextPath}/auth/logout" class="dropdown-item">
                        <span class="item-icon">
                             <!-- Logout Icon -->
                             <svg viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" class="menu-icon"><path fill-rule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 102 0V4a1 1 0 00-1-1zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z" clip-rule="evenodd"/></svg>
                        </span>
                        Log Out
                    </a>
                </div>
            </div>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/auth/login" class="btn-primary" style="margin-right: 10px;">Log In</a>
                <a href="${pageContext.request.contextPath}/auth/register" class="btn-secondary">Sign Up</a>
            <% } %>
        </div>
    </div>
</header>

<style>
    .user-menu-container {
        position: relative;
        margin-left: 8px;
    }

    .user-menu-btn {
        background: transparent;
        border: 1px solid transparent;
        display: flex;
        align-items: center;
        padding: 4px 8px;
        border-radius: 4px;
        cursor: pointer;
        transition: border 0.2s;
        height: 36px;
    }

    .user-menu-btn:hover {
        border: 1px solid #edeff1;
    }

    .user-avatar-small img {
        width: 24px;
        height: 24px;
        border-radius: 4px;
        display: block;
    }

    .user-info-brief {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        margin-left: 8px;
        margin-right: 20px; /* Space for arrow */
        font-size: 12px;
        line-height: 1.2;
    }

    .username-text {
        font-weight: 500;
        color: #1c1c1c;
    }

    .karma-text {
        color: #a8aaab;
    }

    .dropdown-arrow {
        font-size: 10px;
        color: #878a8c;
    }

    .user-dropdown-content {
        display: none;
        position: absolute;
        right: 0;
        top: 100%;
        background-color: white;
        min-width: 250px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        border-radius: 4px;
        border: 1px solid #edeff1;
        z-index: 10000;
        margin-top: 5px;
    }

    .user-dropdown-content.show {
        display: block;
    }

    .dropdown-section-header {
        padding: 12px 16px;
        display: flex;
        align-items: center;
        color: #878a8c;
        border-bottom: 1px solid #edeff1;
    }

    .section-icon {
        font-size: 20px;
        margin-right: 10px;
        color: #878a8c;
    }

    .section-info {
        display: flex;
        flex-direction: column;
    }

    .section-title {
        font-weight: 500;
        font-size: 14px;
        color: #1c1c1c;
    }

    .section-subtitle {
        font-size: 12px;
        color: #878a8c;
    }

    .dropdown-item {
        display: flex;
        align-items: center;
        padding: 12px 16px;
        color: #1c1c1c;
        text-decoration: none;
        font-size: 14px;
        font-weight: 500;
        cursor: pointer;
        transition: background 0.2s;
    }

    .dropdown-item:hover {
        background-color: #f6f7f8;
    }

    .item-icon {
        margin-right: 12px;
        width: 20px;
        height: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #1c1c1c;
    }
    
    .menu-icon {
        width: 20px;
        height: 20px;
        fill: currentColor;
    }

    .dropdown-divider {
        height: 1px;
        background-color: #edeff1;
        margin: 4px 0;
    }
    
    /* Toggle Switch */
    .toggle-switch {
        margin-left: auto;
        position: relative;
        display: inline-block;
        width: 36px;
        height: 20px;
    }
    
    .toggle-switch input {
        opacity: 0;
        width: 0;
        height: 0;
    }
    
    .slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #ccc;
        transition: .4s;
    }
    
    .slider:before {
        position: absolute;
        content: "";
        height: 16px;
        width: 16px;
        left: 2px;
        bottom: 2px;
        background-color: white;
        transition: .4s;
    }
    
    input:checked + .slider {
        background-color: #0079d3;
    }
    
    input:checked + .slider:before {
        transform: translateX(16px);
    }
    
    .slider.round {
        border-radius: 20px;
    }
    
    .slider.round:before {
        border-radius: 50%;
    }
</style>

<script>
    function toggleUserMenu(event) {
        event.stopPropagation();
        var dropdown = document.getElementById("user-dropdown");
        dropdown.classList.toggle("show");
    }
    
    function toggleDisplayMode(event) {
        event.stopPropagation();
        var checkbox = document.getElementById("darkModeToggle");
        
        // If the click wasn't on the checkbox itself (e.g. on the text or container), toggle it manually.
        // Note: The input is hidden (0x0), so clicks are likely on the slider or container.
        if (event.target !== checkbox) {
            checkbox.checked = !checkbox.checked;
        }
        
        applyTheme(checkbox.checked);
    }
    
    function applyTheme(isDark) {
        if (isDark) {
            document.documentElement.setAttribute('data-theme', 'dark');
            localStorage.setItem('theme', 'dark');
        } else {
            document.documentElement.removeAttribute('data-theme');
            localStorage.setItem('theme', 'light');
        }
    }

    // Close the dropdown if the user clicks outside of it
    window.onclick = function(event) {
        if (!event.target.matches('.user-menu-btn') && !event.target.closest('.user-menu-btn') && !event.target.closest('.user-dropdown-content')) {
            var dropdowns = document.getElementsByClassName("user-dropdown-content");
            for (var i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
        }
    }

    // Initialize theme on load
    (function() {
        var storedTheme = localStorage.getItem('theme');
        var checkbox = document.getElementById("darkModeToggle");
        
        // Check system preference if no stored preference
        if (!storedTheme && window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
            // Optional: Default to system preference if you want
            // storedTheme = 'dark'; 
        }

        if (storedTheme === 'dark') {
            document.documentElement.setAttribute('data-theme', 'dark');
            if (checkbox) checkbox.checked = true;
        } else {
            document.documentElement.removeAttribute('data-theme');
            if (checkbox) checkbox.checked = false;
        }
    })();
</script>