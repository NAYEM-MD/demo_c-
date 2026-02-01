<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Tag" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Post - Galaxy^bbs</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/post.css">
</head>
<body>

    <jsp:include page="/common/header.jsp" />

    <div class="create-post-container">
        <div class="create-post-header">
            <h2>Create post</h2>
            <div class="drafts-btn">Drafts</div>
        </div>

        <div class="post-form-card">
            <div class="post-tabs">
                <div class="tab active">
                    <span class="tab-icon">📄</span> Post
                </div>
                <div class="tab" id="imageVideoTab">
                    <span class="tab-icon">🖼️</span> Images & Video
                </div>
                <!-- Link tab removed -->
                <div class="tab" id="pollTab">
                    <span class="tab-icon">📊</span> Poll
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/posts/create" method="post" class="post-form" enctype="multipart/form-data">
                
                <!-- Hidden input to track post type -->
                <input type="hidden" name="postType" id="postType" value="text">

                <div class="title-input-wrapper">
                    <input type="text" name="title" placeholder="Title" required maxlength="300">
                    <span class="char-count">0/300</span>
                </div>

                <!-- Hidden file input -->
                <input type="file" name="mediaFile" id="mediaFile" accept="image/*,video/*" style="display: none;">
                
                <div id="mediaPreview" style="margin-bottom: 15px; display: none;">
                    <img id="imagePreview" src="" style="max-width: 100%; max-height: 400px; border-radius: 4px; display: none;">
                    <video id="videoPreview" controls style="max-width: 100%; max-height: 400px; border-radius: 4px; display: none;"></video>
                    <button type="button" id="removeMedia" style="margin-top: 5px; color: red; background: none; border: none; cursor: pointer;">Remove</button>
                </div>

                <div class="body-editor-wrapper" id="textBodyWrapper">
                    <div class="editor-toolbar">
                        <button type="button" class="toolbar-btn">B</button>
                        <button type="button" class="toolbar-btn">i</button>
                        <button type="button" class="toolbar-btn">🔗</button>
                        <button type="button" class="toolbar-btn">📷</button>
                        <button type="button" class="toolbar-btn">🎥</button>
                    </div>
                    <textarea name="content" placeholder="Text (optional)"></textarea>
                </div>

                <!-- Poll UI -->
                <div id="pollFields" style="display: none; border: 1px solid #ccc; border-radius: 4px; padding: 15px; margin-bottom: 15px;">
                    <div class="form-group" style="margin-bottom: 15px;">
                        <label style="display: block; font-weight: bold; margin-bottom: 5px;">Question</label>
                        <textarea name="pollQuestion" placeholder="Ask a question..." style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; min-height: 60px;"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label style="display: block; font-weight: bold; margin-bottom: 5px;">Quiz options</label>
                        <div id="pollOptionsList">
                            <div class="poll-option" style="display: flex; gap: 10px; margin-bottom: 10px;">
                                <input type="text" name="pollOption" placeholder="Option 1" style="flex: 1; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
                                <button type="button" class="delete-option" style="padding: 0 10px; background: none; border: 1px solid #ccc; border-radius: 4px; cursor: pointer;">🗑️</button>
                            </div>
                            <div class="poll-option" style="display: flex; gap: 10px; margin-bottom: 10px;">
                                <input type="text" name="pollOption" placeholder="Option 2" style="flex: 1; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
                                <button type="button" class="delete-option" style="padding: 0 10px; background: none; border: 1px solid #ccc; border-radius: 4px; cursor: pointer;">🗑️</button>
                            </div>
                        </div>
                        <button type="button" id="addPollOption" style="margin-top: 10px; padding: 8px 16px; background-color: #f6f7f8; border: 1px solid #0079d3; color: #0079d3; border-radius: 20px; font-weight: bold; cursor: pointer;">+ Add option</button>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn-secondary">Save Draft</button>
                    <button type="submit" class="btn-primary">Post</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const imageVideoTab = document.getElementById('imageVideoTab');
            const pollTab = document.getElementById('pollTab');
            const mediaFile = document.getElementById('mediaFile');
            const mediaPreview = document.getElementById('mediaPreview');
            const imagePreview = document.getElementById('imagePreview');
            const videoPreview = document.getElementById('videoPreview');
            const removeMedia = document.getElementById('removeMedia');
            const pollFields = document.getElementById('pollFields');
            const textBodyWrapper = document.getElementById('textBodyWrapper');
            const postTypeInput = document.getElementById('postType');
            const addPollOptionBtn = document.getElementById('addPollOption');
            const pollOptionsList = document.getElementById('pollOptionsList');

            // Tab switching logic
            const tabs = document.querySelectorAll('.tab');
            tabs.forEach(tab => {
                tab.addEventListener('click', function() {
                    // Reset active state
                    tabs.forEach(t => t.classList.remove('active'));
                    this.classList.add('active');

                    // Reset all views
                    textBodyWrapper.style.display = 'none';
                    mediaPreview.style.display = 'none';
                    pollFields.style.display = 'none';
                    
                    // Specific logic
                    if (this.id === 'imageVideoTab') {
                        mediaFile.click();
                        // Note: Preview is shown in 'change' event
                        postTypeInput.value = 'media';
                    } else if (this.id === 'pollTab') {
                        pollFields.style.display = 'block';
                        postTypeInput.value = 'poll';
                    } else {
                        // Default Post tab
                        textBodyWrapper.style.display = 'block';
                        postTypeInput.value = 'text';
                    }
                });
            });

            // Handle file selection
            mediaFile.addEventListener('change', function(e) {
                const file = e.target.files[0];
                if (!file) return;

                // Ensure Image/Video tab is active
                tabs.forEach(t => t.classList.remove('active'));
                imageVideoTab.classList.add('active');
                
                textBodyWrapper.style.display = 'none';
                pollFields.style.display = 'none';
                mediaPreview.style.display = 'block';
                postTypeInput.value = 'media';

                const reader = new FileReader();
                reader.onload = function(e) {
                    if (file.type.startsWith('image/')) {
                        imagePreview.src = e.target.result;
                        imagePreview.style.display = 'block';
                        videoPreview.style.display = 'none';
                        videoPreview.src = "";
                    } else if (file.type.startsWith('video/')) {
                        videoPreview.src = e.target.result;
                        videoPreview.style.display = 'block';
                        imagePreview.style.display = 'none';
                        imagePreview.src = "";
                    }
                };
                reader.readAsDataURL(file);
            });

            // Handle remove media
            removeMedia.addEventListener('click', function() {
                mediaFile.value = '';
                mediaPreview.style.display = 'none';
                imagePreview.src = '';
                videoPreview.src = '';
                // Switch back to text post
                tabs[0].click(); 
            });

            // Poll Options Logic
            addPollOptionBtn.addEventListener('click', function() {
                const optionDiv = document.createElement('div');
                optionDiv.className = 'poll-option';
                optionDiv.style.cssText = 'display: flex; gap: 10px; margin-bottom: 10px;';
                
                const input = document.createElement('input');
                input.type = 'text';
                input.name = 'pollOption';
                input.placeholder = 'Option ' + (pollOptionsList.children.length + 1);
                input.style.cssText = 'flex: 1; padding: 8px; border: 1px solid #ccc; border-radius: 4px;';
                
                const deleteBtn = document.createElement('button');
                deleteBtn.type = 'button';
                deleteBtn.className = 'delete-option';
                deleteBtn.innerHTML = '🗑️';
                deleteBtn.style.cssText = 'padding: 0 10px; background: none; border: 1px solid #ccc; border-radius: 4px; cursor: pointer;';
                
                deleteBtn.addEventListener('click', function() {
                    optionDiv.remove();
                });
                
                optionDiv.appendChild(input);
                optionDiv.appendChild(deleteBtn);
                pollOptionsList.appendChild(optionDiv);
            });

            // Delegate delete event for existing options
            pollOptionsList.addEventListener('click', function(e) {
                if (e.target.classList.contains('delete-option')) {
                    if (pollOptionsList.children.length > 2) {
                        e.target.closest('.poll-option').remove();
                    } else {
                        alert('A poll must have at least 2 options.');
                    }
                }
            });

        });
    </script>
</body>
</html>