function vote(postId, value, element) {
    // Check if user is logged in (rudimentary check, backend validates too)
    
    fetch('http://localhost:8080/galaxy_test/vote', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `postId=${postId}&value=${value}`
    })
    .then(response => {
        if (response.status === 401) {
            window.location.href = 'http://localhost:8080/galaxy_test/auth/login';
            return;
        }
        return response.json();
    })
    .then(data => {
        if (data && data.success) {
            // Update vote count
            const voteContainer = element.closest('.vote-column');
            const countElement = voteContainer.querySelector('.vote-count');
            countElement.textContent = data.newCount;
            
            // Update UI state
            const upArrow = voteContainer.querySelector('.up');
            const downArrow = voteContainer.querySelector('.down');
            
            // Reset both
            upArrow.classList.remove('active');
            downArrow.classList.remove('active');
            
            // Set active based on userVote
            if (data.userVote === 1) {
                upArrow.classList.add('active');
            } else if (data.userVote === -1) {
                downArrow.classList.add('active');
            }
        }
    })
    .catch(error => console.error('Error:', error));
}
