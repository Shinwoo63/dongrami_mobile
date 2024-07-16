document.addEventListener('DOMContentLoaded', function() {
    const saveButton = document.getElementById('save-result');
    
    saveButton.addEventListener('click', function(e) {
        e.preventDefault();
        
        

        const userId = document.getElementById('currentUserId').value;
        const webReadingId = parseInt(document.getElementById('webReadingId').value);
        const readingTitle = document.getElementById('reading1Title').value;
        const subId = parseInt(document.getElementById('subcategoryId').value);
        const cardId = parseInt(document.getElementById('cardId').value);
        
       /* console.log("회원 ==> ", userId);
	console.log("해석 ==> ", webReadingId);
	console.log("타이틀 ===> ", readingTitle);
	console.log("소주제 ===> ", subId);
	console.log("카드 ===> ", cardId);*/

        const formData = new FormData();
        formData.append('userId', userId);
        formData.append('webReadingId', webReadingId);
        formData.append('readingTitle', readingTitle);
        formData.append('subId', subId);
        formData.append('cardId', cardId);

        fetch('/save', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            console.log('서버 응답:', data);
            if (data.success) {
                $('#saveSuccessModal').modal('show');
            } else {
                $('#saveErrorModal .modal-body').text(data.message || '저장에 실패했습니다.');
                $('#saveErrorModal').modal('show');
            }
        })
        .catch((error) => {
            console.error('에러:', error);
            $('#saveErrorModal .modal-body').text('서버와의 통신 중 오류가 발생했습니다.');
            $('#saveErrorModal').modal('show');
        });
    });
});
