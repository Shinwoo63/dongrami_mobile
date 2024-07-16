document.addEventListener("DOMContentLoaded", function() {
    const voteListDiv = document.getElementById('vote-list');
    const commentDiv = document.getElementById('comment-section');
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    const voteId = id;
    let previousOption = null;


    let currentPage;  // 현재 페이지를 나타내는 변수 추가
    const pageSize = 5;  // 페이지 당 댓글 수 설정 (예시로 10개씩)

    // 페이지 로딩 시 투표 정보 가져오기
    fetchVoteById(voteId);

    function fetchVoteById(id) {
        fetch(`/api/votes/${voteId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch vote');
                }
                return response.json();
            })
            .then(vote => {
                const voteDiv = createVoteElement(vote);
                voteListDiv.appendChild(voteDiv);
                fetchComments(voteId);
            })
            .catch(error => {
                console.error('Error fetching vote:', error);
            });
    }

    function createVoteElement(vote) {
        const voteDiv = document.createElement('div');
        voteDiv.classList.add('vote-item');

        voteDiv.innerHTML = `
            <h2>${vote.question}</h2>
            <img src="${vote.voteImage}" alt="Vote Image">
            <p>${vote.option1}</p>
            <p>${vote.option2}</p>
        `;
        const buttonContainer = document.createElement('div');
        buttonContainer.classList.add('button-container');

        const button1 = document.createElement('button');
        button1.textContent = '투표';
        button1.id = `button1`;
        button1.addEventListener('click', function() {
            voteOption(vote.voteId, 'option1');
            if (button1 && button2) {
                button1.classList.add('selected');
                button2.classList.remove('selected');
            }
        });

        const button2 = document.createElement('button');
        button2.textContent = '투표';
        button2.id = `button2`;
        button2.addEventListener('click', function() {
            voteOption(vote.voteId, 'option2');
            if (button1 && button2) {
                button2.classList.add('selected');
                button1.classList.remove('selected');
            }
        });
        buttonContainer.appendChild(button1);
        buttonContainer.appendChild(button2);
        voteDiv.appendChild(buttonContainer);

        const barContainer1 = document.createElement('div');
        barContainer1.classList.add('bar-container1');
        barContainer1.id = `barContainer1_${vote.voteId}`;
        barContainer1.innerHTML = `
            <div class="bar1" id="bar1_${vote.voteId}"></div>
            <span class="text-left" id="text1">A</span>
            <span class="percentage" id="percentage1_${vote.voteId}"></span>
        `;
        voteDiv.appendChild(barContainer1);

        const barContainer2 = document.createElement('div');
        barContainer2.classList.add('bar-container2');
        barContainer2.id = `barContainer2_${vote.voteId}`;
        barContainer2.innerHTML = `
            <div class="bar2" id="bar2_${vote.voteId}"></div>
            <span class="text-left" id="text2">B</span>
            <span class="percentage" id="percentage2_${vote.voteId}"></span>
        `;
        voteDiv.appendChild(barContainer2);

        return voteDiv;
    }

    window.voteOption = function(voteId, option) {
        fetch(`/api/votes/${voteId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ option: option, previousOption: previousOption }),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to vote');
            }
            return response.json();
        })
        .then(updatedVote => {
            console.log('Vote successful:', updatedVote);
            updateVoteResults(updatedVote);
            previousOption = option; // 현재 선택을 이전 선택으로 저장
        })
        .catch(error => {
            console.error('Error voting:', error);
        });
    };

    function updateVoteResults(vote) {
        const totalVotes = vote.option1Count + vote.option2Count;
        const percentage1 = totalVotes === 0 ? 0 : Math.round((vote.option1Count / totalVotes) * 100);
        const percentage2 = totalVotes === 0 ? 0 : Math.round((vote.option2Count / totalVotes) * 100);

        const percentage1Elem = document.getElementById(`percentage1_${vote.voteId}`);
        const percentage2Elem = document.getElementById(`percentage2_${vote.voteId}`);
        const bar1 = document.getElementById(`bar1_${vote.voteId}`);
        const bar2 = document.getElementById(`bar2_${vote.voteId}`);

        percentage1Elem.textContent = `${percentage1}%`;
        percentage2Elem.textContent = `${percentage2}%`;

        bar1.style.width = `${percentage1}%`;
        bar2.style.width = `${percentage2}%`;
    }

    // 댓글 조회
    function fetchComments(voteId, page = 0, size = 5) {
        fetch(`/api/replies/${voteId}/replies?page=${page}&size=${size}&sort=replyCreate,desc&level=1`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch comments');
                }
                return response.json();
            })
            .then(data => {
                const comments = data.content.sort((a, b) => new Date(b.replyCreate) - new Date(a.replyCreate));
                const commentDiv = document.getElementById('comment-section'); // `commentDiv`가 정의된 곳에 맞게 수정
                commentDiv.innerHTML = '';
                comments.forEach(comment => {
                    const commentElem = createCommentElement(comment);
                    commentDiv.appendChild(commentElem);
                    fetchReplies(comment.replyId, commentElem); // 각 댓글의 답글 가져오기
                });
				  const singlePostContainer = document.querySelector('.single-post');
    				if (comments.length === 0) {
        				singlePostContainer.style.minHeight = '400px';
   					 } else {
        					singlePostContainer.style.minHeight = 'auto';
    					}
                // 페이지 네이션 UI 업데이트
                updatePagination(data, voteId, size);
            })
            .catch(error => {
                console.error('Error fetching comments:', error);
            });
    }
function fetchReplies(parentReId, commentElem, page = 0, size = 5) {
    fetch(`/api/replies/${parentReId}/reply?page=${page}&size=${size}`)
        .then(response => {
			 console.log('Response:', response);
            if (!response.ok) {
                throw new Error('Failed to fetch replies');
            }
            return response.json();
        })
        .then(data => {
            const replies = data.content;
            const totalPages = data.totalPages;
            const currentPage = data.number;

            console.log('Fetched replies:', replies);

            const replyForm = commentElem.querySelector('.comment-reply-form');
            let replyContainer = replyForm.querySelector('.reply-container');

            if (replyContainer) {
                replyContainer.innerHTML = '';
            } else {
                replyContainer = document.createElement('div');
                replyContainer.classList.add('reply-container');
                replyForm.appendChild(replyContainer);
            }

            if (replies.length === 0) {
                const noRepliesDiv = document.createElement('div');
                noRepliesDiv.classList.add('no-replies');
                noRepliesDiv.textContent = '답글이 없습니다.';
                replyContainer.appendChild(noRepliesDiv);
                replyForm.style.minHeight = '150px';
            } else {
                replies.forEach(reply => {
                    const replyElem = createReplyElement(reply);
                    replyContainer.appendChild(replyElem);
                });
                replyForm.style.minHeight = 'auto';
            }

            // 페이징 버튼 생성
            createPaginationButtons(replyContainer, parentReId, commentElem, totalPages, currentPage);

            // 이벤트 리스너 설정 (중복되지 않도록)
            const postReplyButton = replyForm.querySelector('.post-reply-button');

            // 기존 이벤트 리스너 제거
            const newButton = postReplyButton.cloneNode(true);
            postReplyButton.parentNode.replaceChild(newButton, postReplyButton);

            newButton.addEventListener('click', function() {
                const replyContent = document.getElementById(`replyContent_${parentReId}`).value;
                postReply(parentReId, replyContent, commentElem, page, size);

                if (replyContainer.querySelector('.no-replies')) {
                    replyContainer.querySelector('.no-replies').remove();
                    replyForm.style.minHeight = 'auto';
                }
            });
        })
        .catch(error => {
            console.error('Error fetching replies:', error);
        });
}


function createPaginationButtons(container, parentReId, commentElem, totalPages, currentPage) {
    const paginationContainer = document.createElement('div');
    paginationContainer.classList.add('pagination-container');

    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i + 1;
        if (i === currentPage) {
            pageButton.classList.add('active');
        }
        pageButton.addEventListener('click', () => {
            fetchReplies(parentReId, commentElem, i);
        });
        paginationContainer.appendChild(pageButton);
    }

    container.appendChild(paginationContainer);
}






    function updatePagination(data, voteId, size) {
        const paginationDiv = document.getElementById('pagination');
        paginationDiv.innerHTML = '';

        // 이전 페이지 버튼
        const prevButton = document.createElement('button');
        prevButton.textContent = '< 이전';
        prevButton.onclick = () => fetchComments(voteId, data.number - 1, size);
        prevButton.setAttribute('id', 'prevButton'); // id 부여
        paginationDiv.appendChild(prevButton);

        // 페이지 번호 버튼
        let startPage = Math.max(0, data.number - 1); // 시작 페이지 번호 계산
        let endPage = startPage + 2; // 보여줄 페이지 버튼의 끝 번호 계산
        if (endPage > data.totalPages - 1) {
            endPage = data.totalPages - 1;
        }
        startPage = Math.max(0, endPage - 2); // 시작 페이지 번호 다시 계산

        for (let i = startPage; i <= endPage; i++) {
            const pageNumberButton = document.createElement('button');
            pageNumberButton.setAttribute('id', 'pageButton');
            pageNumberButton.textContent = i + 1;
            pageNumberButton.addEventListener('click', function() {
                fetchComments(voteId, i, size); // 페이지 번호를 인자로 하여 fetchComments 호출
            });

            if (i === data.number) {
                pageNumberButton.classList.add('current-page'); // 현재 페이지에 해당하는 버튼에 클래스 추가
            }

            paginationDiv.appendChild(pageNumberButton);
        }

        // 다음 페이지 버튼
        if (!data.last) {
            const nextButton = document.createElement('button');
            nextButton.textContent = '다음 >';
            nextButton.onclick = () => fetchComments(voteId, data.number + 1, size);
            nextButton.setAttribute('id', 'nextButton'); // id 부여
            paginationDiv.appendChild(nextButton);
        } else {
            const nextButton = document.getElementById('nextButton');
            if (nextButton) {
                paginationDiv.removeChild(nextButton); // 버튼 제거
            }
        }
    }

function createCommentElement(comment) {
    const commentDiv = document.createElement('div');
    commentDiv.classList.add('comment-item');
	const formattedReplyCreate = comment.replyCreate.replace('T', ' ').substring(0, 16);
    commentDiv.innerHTML = `
        <div class="comment-content">
            <h4>${comment.member.nickname}</h4>
            <p>${comment.content}</p>
            <h5>${formattedReplyCreate}</h5>
        </div>
        <div class="comment-actions">
            <button class="menu-button">...</button>
             <button class="edit-button" style="display:none;">수정</button>
             <button class="delete-button" style="display:none;">삭제</button>
        </div>
        <div class="comment-edit-form" style="display:none;">
            <textarea class="edit-content">${comment.content}</textarea>
            <button class="save-edit-button">저장</button>
            <button class="cancel-edit-button">취소</button>
        </div>
		<div class="comment-parent-form">
		 	<button class="reply-button">답글</button>
        	<div class="comment-reply-form" style="display:none;">
            	<textarea id="replyContent_${comment.replyId}" class="reply-content" placeholder="답글 내용"></textarea>
            	<button class="post-reply-button">작성</button>
        	</div>
        </div>
    `;

    const menuButton = commentDiv.querySelector('.menu-button');
    const editButton = commentDiv.querySelector('.edit-button');
    const deleteButton = commentDiv.querySelector('.delete-button');
    const replyButton = commentDiv.querySelector('.reply-button');
    const menuOptions = commentDiv.querySelector('.menu-options');
    const editForm = commentDiv.querySelector('.comment-edit-form');
    const replyForm = commentDiv.querySelector('.comment-reply-form');
    const saveEditButton = commentDiv.querySelector('.save-edit-button');
    const cancelEditButton = commentDiv.querySelector('.cancel-edit-button');
    const editContent = commentDiv.querySelector('.edit-content');
    const postReplyButton = commentDiv.querySelector('.post-reply-button');

    menuButton.addEventListener('click', function() {
        toggleMenuButtons(editButton, deleteButton, replyButton, editForm, replyForm);
    });

    editButton.addEventListener('click', function() {
        toggleForm(editForm, commentDiv.querySelector('.comment-content'));
             // 수정 버튼과 삭제 버튼을 숨기기
        editButton.style.display = 'none';
        deleteButton.style.display = 'none';
    });

    cancelEditButton.addEventListener('click', function() {
        toggleForm(editForm, commentDiv.querySelector('.comment-content'));
        
    });

    saveEditButton.addEventListener('click', function() {
        const updatedContent = editContent.value;
        updateComment(comment.replyId, updatedContent, commentDiv);
        editForm.style.display = 'none';
    });

    deleteButton.addEventListener('click', function() {
        deleteComment(comment.replyId, commentDiv);
    });

    replyButton.addEventListener('click', function() {
    toggleForm(replyForm);
});


    postReplyButton.addEventListener('click', function() {
        const replyContent = document.getElementById(`replyContent_${comment.replyId}`).value;
        postReply(comment.replyId, replyContent, commentDiv);
    });

    return commentDiv;
}


    function toggleMenuButtons(editButton, deleteButton, replyButton, editForm, replyForm) {
        const buttons = [editButton, deleteButton];
        buttons.forEach(button => {
            if (button.style.display === 'none') {
                button.style.display = 'block';
            } else {
                button.style.display = 'none';
            }
        });

        if (editForm) {
            editForm.style.display = 'none';
        }

        if (replyForm) {
            replyForm.style.display = 'none';
        }
    }

    function toggleForm(form, contentElement) {
        if (form.style.display === 'none') {
            form.style.display = 'block';
        } else {
            form.style.display = 'none';
        }
    }

    function updateComment(replyId, updatedContent, commentElem) {
        const currentDate = new Date().toISOString();  // 현재 시간을 ISO 포맷으로 가져옴
        fetch(`/api/replies/${replyId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ content: updatedContent, replyModify: currentDate }),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to update comment');
            }
            return response.json();
        })
        .then(updatedComment => {
            commentElem.querySelector('p').textContent = updatedComment.content;
            commentElem.querySelector('.edit-form').style.display = 'none';
            commentElem.querySelector('p').style.display = 'block'; // 기존 댓글 다시 보이기
        })
        .catch(error => {
            console.error('Error updating comment:', error);
        });
    }

    function deleteComment(replyId, commentElem) {
        fetch(`/api/replies/${replyId}`, {
            method: 'DELETE',
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete comment');
            }
            commentElem.remove();
        })
        .catch(error => {
            console.error('Error deleting comment:', error);
        });
    }
  function postReply(parentReId, replyContent, commentElem) {
    const userId = document.getElementById('userId').value;  // 사용자 ID 가져오기
    const now = new Date();  // 현재 시간
    const kstOffset = 9 * 60 * 60 * 1000;  // KST는 UTC+9
    const kstDate = new Date(now.getTime() + kstOffset);  // KST 시간 계산

    const replyData = {
        content: replyContent,
        level: 2,  // 레벨 설정 (답글은 2로 고정)
        member: userId,
        parentReId: parentReId,
        replyCreate: kstDate.toISOString(),  // KST 시간으로 설정
        voteId: voteId,
    };

    fetch(`/api/replies/${voteId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(replyData)
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById(`replyContent_${parentReId}`).value = '';  // 답글 입력 폼 초기화
        fetchReplies(parentReId, commentElem);  // 답글 다시 불러오기
    })
    .catch((error) => {
        console.error('Error posting reply:', error);
    });
}




document.getElementById('replyForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const content = document.getElementById('content').value;
    const level = document.getElementById('level').value;
    const userIdElem = document.getElementById('userId');  // 사용자 ID 요소 가져오기
    const parentReId = document.getElementById('parentReId').value; // 부모 댓글의 ID
	 const now = new Date();  // 현재 시간
	const kstOffset = 9 * 60 * 60 * 1000;  // KST는 UTC+9
    const kstDate = new Date(now.getTime() + kstOffset);  // KST 시간 계산
    // userId 요소가 존재하지 않거나 value가 null인 경우 경고 메시지 표시 후 함수 종료
	 if (!userIdElem || !userIdElem.value) {
      $('#loginModal').modal('show'); // 모달을 표시합니다.
      return;  // 함수 종료
    }

    const userId = userIdElem.value;
    const replyData = {
        content: content,
        level: level,
        member: userId,
        parentReId: parentReId,
        replyCreate: kstDate.toISOString(),
        voteId: voteId,
    };

    fetch(`/api/replies/${voteId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(replyData)
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById('content').value = '';  // 댓글 작성 후 content 필드 초기화
        document.getElementById('parentReId').value = ''; // 부모 댓글 ID 초기화
        fetchComments(voteId);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
});


  function createReplyElement(reply) {
    const replyElem = document.createElement('div');
    replyElem.classList.add('reply-container'); // reply-container 클래스 추가
	const formattedReplyCreate = reply.replyCreate.replace('T', ' ').substring(0, 16);
    replyElem.innerHTML = `
        <div class="reply-contents">
            <p>${reply.content}</p>
            <small>${reply.member.nickname}</small>
            <h6>${formattedReplyCreate}</h6>
        </div>
    `;
    
    return replyElem;
}


});
