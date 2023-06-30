const socket = new SockJS('/comments');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected to WebSocket');
    stompClient.subscribe('/topic/comments', function (message) {
        const comment = JSON.parse(message.body);
        displayComment(comment);
    });
});

function submitComment(event) {
    event.preventDefault();
    const commentInput = document.getElementById('commentInput');
    const commentText = commentInput.value;

    const comment = {
        text: commentText
    };

    stompClient.send("/app/addComment", {}, JSON.stringify(comment));

    commentInput.value = '';
}

function displayComment(comment) {
    const commentsContainer = document.getElementById('commentsContainer');
    const commentElement = document.createElement('p');
    commentElement.textContent = comment.text;
    commentsContainer.appendChild(commentElement);
}

document.getElementById('commentForm').addEventListener('submit', submitComment);