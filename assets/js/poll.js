function votePoll(pollId, optionId) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/galaxy_test/polls/vote', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    
    xhr.onload = function() {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.status === 'success') {
                updatePollUI(pollId, response);
            }
        } else if (xhr.status === 409) {
            alert('You have already voted on this poll.');
        } else if (xhr.status === 401) {
            window.location.href = '/galaxy_test/auth/login';
        } else {
            alert('Error voting on poll. Please try again.');
        }
    };
    
    xhr.send('pollId=' + pollId + '&optionId=' + optionId);
}

function updatePollUI(pollId, data) {
    const totalVotes = data.totalVotes;
    
    // Update total votes text
    const totalVotesElement = document.getElementById('total-votes-' + pollId);
    if (totalVotesElement) {
        totalVotesElement.textContent = 'Live | ' + totalVotes + ' votes';
    }
    
    // Update each option
    data.options.forEach(option => {
        const percent = totalVotes > 0 ? Math.round((option.count / totalVotes) * 100) : 0;
        
        // Update progress bar width
        const progressBar = document.getElementById('progress-' + option.id);
        if (progressBar) {
            progressBar.style.width = percent + '%';
        }
        
        // Update percentage text
        const percentText = document.getElementById('percent-' + option.id);
        if (percentText) {
            percentText.textContent = percent + '%';
        }
    });
}
