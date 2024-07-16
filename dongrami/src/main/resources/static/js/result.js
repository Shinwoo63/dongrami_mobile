$(document).ready(function () {
    // 현재 로그인된 사용자 정보를 hidden input 필드에서 가져오기
    const currentUserNickname = $('#currentUserNickname').val();
    const userId = $('#currentUserId').val(); // 추가: 사용자 ID 가져오기

    // 콘솔에 닉네임을 출력하여 확인
    console.log('Current User Nickname:', currentUserNickname);
    console.log('Current User ID:', userId);

    // 리뷰 쓰기 버튼 클릭 시 모달 열기
    $('#write-review').click(function () {
        $('#reviewModal').show();
        $('body').addClass('modal-open');
        
        // 로그인된 사용자 정보 설정
        $('.custom-user-name').text(currentUserNickname);
        $('.custom-user-role').text(`#${$('#subcategory_name').text()}`);

        // 기본 평점 5점으로 설정
        updateRating(5);

        // 초기 리뷰 텍스트와 글자 수 설정
        $('#custom-review-text').val('');
        $('#custom-character-count').text('0/50');
    });

    // 모달 닫기 버튼 클릭 시 모달 닫기
    $('.custom-close').click(function () {
        closeModal();
    });

    // 모달 창 밖 클릭 시 모달 닫기
    $(window).click(function (event) {
        if (event.target == $('#reviewModal')[0]) {
            closeModal();
        }
    });

    // 모달 닫기 함수
    function closeModal() {
        $('#reviewModal').hide();
        $('body').removeClass('modal-open');
    }

    // 평점 클릭 시 평점 업데이트
    $('.custom-modal-star').click(function () {
        const index = $(this).index();
        updateRating(index + 1);
    });

    // 평점 업데이트 함수
    function updateRating(rating) {
        $('.custom-modal-star').each(function (index) {
            $(this).text(index < rating ? '★' : '☆');
        });
        $('.custom-modal-rating-score').text(`${rating}점`);
        $('#ratingScore').val(rating);
    }

    // 리뷰 폼 제출 시
    $('#reviewForm').submit(function(event) {
        event.preventDefault();
    
        const reviewText = $('#custom-review-text').val().trim();
        const rating = parseInt($('#ratingScore').val());
    
        // 유효성 검사
        if (!reviewText) {
            alert('리뷰 텍스트를 입력해 주세요.');
            return;
        }
        if (isNaN(rating) || rating < 1 || rating > 5) {
            alert('유효한 평점을 입력해 주세요 (1-5).');
            return;
        }
    
        const review = {
            rating: rating,
            reviewText: reviewText,
            subcategoryId: parseInt($('#subcategoryId').val()), // Subcategory ID 가져오기
            userId: userId,                                    // User ID 가져오기
            resultId: parseInt($('#cardId').val())              // Result ID 가져오기
        };
    
        console.log('Review Data:', review); // review 데이터 확인
    
        $.ajax({
            url: '/review/api/reviews',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(review),
            success: function(response) {
                console.log('Response:', response);
                closeModal();
                showReviewRegistrationModal(); // 리뷰 등록 성공 시 모달 표시
            },
            error: function(xhr, status, error) {
                console.error('리뷰 저장 중 오류 발생:', xhr);
                console.error('Status:', status);
                console.error('Error:', error);
                alert(`리뷰 저장 중 오류가 발생했습니다: ${xhr.responseText}`);
            }
        });
    });

    // 리뷰 등록 버튼 클릭 시 폼 제출 트리거
    $('#custom-submit-review').click(function () {
        $('#reviewForm').submit();
    });

    function showReviewRegistrationModal() {
        $('#review-registration-modal').show();
    }

    function closeReviewRegistrationModal() {
        $('#review-registration-modal').hide();
    }

    // OK 버튼 클릭 시 모달 닫기
    $(document).on('click', '.review-registration-ok-button', function () {
        closeReviewRegistrationModal();
    });
});