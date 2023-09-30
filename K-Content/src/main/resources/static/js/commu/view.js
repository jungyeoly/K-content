function maskUserId(userId) {
	if (userId.length <= 4) {
		return userId;
	}
	return userId.substring(0, 4) + '*'.repeat(userId.length - 4);
}

$(document).ready(function() {
	$('.single-comment strong').each(function() {
		var originalId = $(this).text();
		var maskedId = maskUserId(originalId);
		$(this).text(maskedId);
	});
});




//게시글 신고 기능
function showReportConfirmModal() {
	const modal = document.getElementById('commonModal');
	const modalTitle = modal.querySelector('.modal-title');
	const modalBody = modal.querySelector('.modal-body');

	modalTitle.textContent = "신고 확인";
	modalBody.textContent = "정말로 게시글을 신고하겠습니까?";

	// Bootstrap 5의 모달을 수동으로 표시하는 코드
	const bsModal = new bootstrap.Modal(modal);
	bsModal.show();

	// '확인' 버튼에 리스너 추가
	document.getElementById('confirmBtn').addEventListener('click', function() {
		const reportForm = document.querySelector('form[method="post"][th\\:action*="report"]');
		if (reportForm) {
			reportForm.submit();
		}
	});
}





// '확인' 버튼에 한 번만 이벤트 리스너 추가
document.getElementById('confirmBtn').addEventListener('click', function() {
	const deleteForm = document.querySelector('form[method="post"]');
	if (deleteForm) {
		deleteForm.submit();
	}
});

function showDeleteConfirmModal() {
	const modal = document.getElementById('commonModal');
	const modalTitle = modal.querySelector('.modal-title');
	const modalBody = modal.querySelector('.modal-body');

	modalTitle.textContent = "삭제 확인";
	modalBody.textContent = "정말로 삭제하시겠습니까?";

	// Bootstrap 5의 모달을 수동으로 표시하는 코드
	const bsModal = new bootstrap.Modal(modal);
	bsModal.show();
}

function showConfirmModal(event, actionType) {
	event.preventDefault(); // form 제출을 방지

	// 모달 내용 변경
	document.querySelector(".modal-body").textContent = "정말로 삭제하시겠습니까?";

	// actionType에 따라 data-action 설정 (필요한 경우)
	document.getElementById('commonModal').dataset.action = actionType;
}


$(document).ready(function() {

	// 댓글 작성
	$("#commentForm button").on("click", function(e) {
		console.log("Button clicked");
		e.preventDefault();

		var formData = {
			commuCommentMberId: $("#commentForm").find("[name='commuCommentMberId']").val(),
			commuCommentCommuId: $("#commentForm").find("[name='commuCommentCommuId']").val(),
			commuCommentCntnt: $("#commentForm").find("[name='commuCommentCntnt']").val()


		};

		$.ajax({
			url: "/commu/comment",
			type: "POST",
			data: JSON.stringify(formData),
			contentType: 'application/json',
			dataType: 'json',
			success: function(response) {
				console.log(response);
				var commucomment = response.comment;
				// 댓글 수 업데이트
				var commentCountElement = $(".comment-list-section h3 span");
				var currentCount = parseInt(commentCountElement.text());
				commentCountElement.text(currentCount + 1);

				var newComment = `
        <div class="single-comment" data-id="${commucomment.commuCommentId}">
            <strong>${commucomment.commuCommentMberId}</strong>
            <p>${commucomment.commuCommentCntnt}</p>
            <div class="comment-date">${commucomment.commuCommentRegistDate}</div>
            <button class="update-comment" data-id="${commucomment.commuCommentId}">수정</button>
            <button class="delete-comment" data-id="${commucomment.commuCommentId}">삭제</button>
        </div>
    `;
				$(".comment-list-section").append(newComment);

				// 댓글 폼 리셋
				$("#commentForm")[0].reset();
			},
			error: function(err) {
				console.log(err);
				alert("댓글 작성 중 오류 발생");
			}
		});
	});
	// 답글 입력 영역 초기에 숨기기
	$(".replyForm").hide();

	// 답글 달기 버튼 클릭 시 답글 입력 영역 표시/숨기기 토글
	$(".comment-list-section").on("click", ".reply-to-comment", function() {
		$(this).closest(".single-comment").find(".replyForm").toggle();
	});

	// 대댓글 등록
	$(".comment-list-section").on("click", ".post-reply", function() {
		var $replyForm = $(this).closest(".replyForm");
		var $closestComment = $(this).closest(".single-comment");
		
		var formData = {
			commuCommentMberId: $("#commentForm").find("[name='commuCommentMberId']").val(),
			commuCommentCommuId: $("#commentForm").find("[name='commuCommentCommuId']").val(),
			commuCommentCntnt: $replyForm.find("[name='commuCommentCntnt']").val(),
			commuCommentRefId: $replyForm.find("[name='commuCommentRefId']").val()
		};

		$.ajax({
			url: "/commu/comment",
			type: "POST",
			data: JSON.stringify(formData),
			contentType: 'application/json',
			dataType: 'json',
			success: function(response) {
				console.log(response);
				// 새로운 답글 생성
				var commucomment = response.comment;
				var newReply = `
                <div class="single-comment" data-id="${commucomment.commuCommentId}">
                    <strong>${commucomment.commuCommentMberId}</strong>
                    <p>${commucomment.commuCommentCntnt}</p>
                    <div class="comment-date">${commucomment.commuCommentRegistDate}</div>
                    <button class="update-comment" data-id="${commucomment.commuCommentId}">수정</button>
                    <button class="delete-comment" data-id="${commucomment.commuCommentId}">삭제</button>
                </div>
            `;

				// 상위 댓글의 replies div에 답글 추가
			$closestComment.find(".replies").append(newReply);

				// 답글 폼 초기화
				$replyForm.find("form")[0].reset();


			},
			error: function(err) {
				console.log(err);
				alert("답글 작성 중 오류 발생");
			}
		});
	});

	// 댓글 수정/저장 버튼 클릭 이벤트
	$(".comment-list-section").on("click", ".update-comment", function() {
		var $commentDiv = $(this).closest('.single-comment');

		if ($(this).text() === "수정") { // 댓글을 수정하는 경우
			var currentCommentText = $commentDiv.find('.comment-content p').text();
			// 댓글 내용을 텍스트에서 텍스트에리어로 변경
			$commentDiv.find('.comment-content p').hide();
			$commentDiv.find('.comment-content .update-comment-textarea').val(currentCommentText).show();
			$(this).text("저장"); // 버튼 텍스트를 "저장"으로 변경
		} else { // 수정된 내용을 저장하는 경우
			var updatedCommentText = $commentDiv.find('.update-comment-textarea').val();
			var commuCommentId = $commentDiv.data('id');

			var formData = {
				commuCommentId: commuCommentId,
				commuCommentCntnt: updatedCommentText
			};

			$.ajax({
				url: "/commu/comment/update/" + commuCommentId,
				type: "POST",
				data: JSON.stringify(formData),
				contentType: 'application/json',
				dataType: 'json',
				success: function(response) {
					console.log(response);
					// 성공적으로 저장된 경우 화면을 업데이트
					$commentDiv.find('.comment-content p').text(response.updatedComment).show();
					$commentDiv.find('.comment-content .update-comment-textarea').hide();
					// 수정된 날짜를 화면에 업데이트
					$commentDiv.find('.comment-date').text(response.commuCommentUpdateDate);
					$commentDiv.find('.update-comment').text("수정"); // 버튼 텍스트를 "수정"으로 변경

				},
				error: function(err) {
					console.log(err);
					alert("댓글 수정 중 오류 발생");
				}
			});
		}
	});


	// 댓글 삭제
	$(".comment-list-section").on("click", ".delete-comment", function() {
		var commuCommentId = $(this).data("id");
		console.log(commuCommentId);

		{
			$.ajax({
				url: "/commu/comment/delete/" + commuCommentId,
				type: "POST",
				contentType: 'application/json',
				dataType: 'json',
				success: function() {
					$(".single-comment[data-id='" + commuCommentId + "']").remove();
					// 댓글 수 업데이트
					var commentCountElement = $(".comment-list-section h3 span");
					var currentCount = parseInt(commentCountElement.text());
					commentCountElement.text(currentCount - 1);
				},
				error: function(err) {
					console.log(err);
					alert("댓글 삭제 중 오류 발생");
				}
			});
		}
	});
})