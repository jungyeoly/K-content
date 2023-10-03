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

	function updateReplyCount(commentId) {
		var replies = $(".single-comment[data-id='" + commentId + "'] .replies .single-comment").length;
		$(".single-comment[data-id='" + commentId + "'] .reply-count").text(replies);

		// 답글이 없으면 '답글 보기' 버튼을 숨깁니다.
		if (replies === 0) {
			$(".single-comment[data-id='" + commentId + "'] .view-replies-btn").hide();
		} else {
			$(".single-comment[data-id='" + commentId + "'] .view-replies-btn").show();
		}
	}

	// 새로운 댓글/대댓글 HTML 생성
	function createCommentHTML(comment) {
		return `
            <div class="single-comment" data-id="${comment.commuCommentId}">
                <strong>${comment.commuCommentMberId}</strong>
                <p>${comment.commuCommentCntnt}</p>
                <div class="comment-date">${comment.commuCommentRegistDate}</div>
                <button class="update-comment" data-id="${comment.commuCommentId}">수정</button>
                <button class="delete-comment" data-id="${comment.commuCommentId}">삭제</button>
   <button class="view-replies-btn">답글(<span class="reply-count">0개</span>)</button>                
				<div class="replies"></div>
                <div class="replyForm">
                    <form>
                        <input type="hidden" name="commuCommentRefId" value="${comment.commuCommentId}">
                        <textarea name="commuCommentCntnt" rows="2" placeholder="답글을 입력하세요."></textarea>
                        <button type="button" class="post-reply">등록</button>
                    </form>
                </div>
            </div>`;
	}

	function postComment(formData, isReply) {
		$.ajax({
			url: "/commu/comment",
			type: "POST",
			data: JSON.stringify(formData),
			contentType: 'application/json',
			dataType: 'json',
			success: function(response) {
				  console.log("Server Response:", response); 
				var commucomment = response.comment;
				var newComment = createCommentHTML(commucomment);
				
				if (isReply) {
					// 답글을 해당 댓글의 "replies" 영역에 추가
					$(".single-comment[data-id='" + formData.commuCommentRefId + "'] > .replies").append(newComment);
 console.log("Target Element:", targetElement); 
					// 답글 입력 폼을 초기화하고 숨깁니다.
					$(".replyForm:visible").find("[name='commuCommentCntnt']").val('');
					$(".replyForm:visible").hide();
					updateReplyCount(formData.commuCommentRefId);
				} else {
					// 새로운 댓글을 댓글 리스트의 가장 아래에 추가
					$(".comment-list-section").append(newComment);

					// 댓글 입력 폼 초기화
					$("#commentForm").find("[name='commuCommentCntnt']").val('');
				}
			},
			error: function(err) {
				console.log(err);
				alert("댓글 작성 중 오류 발생");
			}
		});
	}



	$(".comment-list-section").on("click", ".view-replies-btn", function(e) {
		e.stopPropagation();
		var $repliesDiv = $(this).closest(".single-comment").find(".replies").first();
		$repliesDiv.toggle();
	});


	$(".comment-list-section").on("click", ".single-comment", function(e) {
		e.stopPropagation();

		// 모든 답글 폼을 숨깁니다.
		$(".replyForm").hide();

		// 클릭한 댓글 또는 답글의 답글 폼만 보여줍니다.
		$(this).find(".replyForm").first().show();
	});

	/*$("#commentForm button").on("click", function(e) {
		e.preventDefault();

		var commuCommentId = $(this).data("id");
		$(".replyForm").hide();
		$.ajax({
			url: "/commu/comment/" + commuCommentId,
			type: "GET",
			dataType: 'json',
			success: function(response) {
				if (response.status === "success") {
					console.log(commuCommentId); 

					var commuCommentWithReplies = response.commuCommentWithReplies;
					var replies = commuCommentWithReplies.replies;

					replies.forEach(function(reply) {
						var replyHtml = createCommentHTML(reply);
						$(".single-comment[data-id='" + commuCommentId + "'] > .replies").append(replyHtml);
					});
				} else {
					alert(response.message);
				}
			},
			error: function(err) {
				console.log(err);
				alert("댓글 및 대댓글 조회 중 오류 발생");
			}
		});
	});*/

	$(".comment-list-section").on("click", ".reply-to-comment", function(e) {
		e.stopPropagation();
		// 모든 답글 폼을 숨깁니다.
		$(".replyForm").hide();

		// 클릭된 "답글" 버튼에 해당하는 답글 폼만 보여줍니다.
		$(this).closest(".single-comment").find(".replyForm").first().show();
	});

	// 댓글 등록
	$("#commentForm button").on("click", function(e) {
		e.preventDefault();

		var formData = {
			commuCommentMberId: $("#commentForm").find("[name='commuCommentMberId']").val(),
			commuCommentCommuId: $("#commentForm").find("[name='commuCommentCommuId']").val(),
			commuCommentCntnt: $("#commentForm").find("[name='commuCommentCntnt']").val()
		};

		postComment(formData, false);
	});

	// 답글 등록
	$(".comment-list-section").on("click", ".post-reply", function() {
		var $replyForm = $(this).closest(".replyForm");

		var formData = {
			commuCommentMberId: $("#commentForm").find("[name='commuCommentMberId']").val(),
			commuCommentCommuId: $("#commentForm").find("[name='commuCommentCommuId']").val(),
			commuCommentCntnt: $replyForm.find("[name='commuCommentCntnt']").val(),
			commuCommentRefId: $replyForm.find("[name='commuCommentRefId']").val()
		};

		postComment(formData, true);
	});

	$(".comment-list-section").on("click", ".update-comment-textarea", function(e) {
		e.stopPropagation();  // textarea 클릭시 다른 요소는 클릭되지 않게 함
	});
	$(".comment-list-section").on("click", ".update-comment", function(e) {
		e.stopPropagation();  // 다른요소 못건들이게 
		var $commentDiv = $(this).closest('.single-comment');
		if ($(this).text() === "수정") {
			var currentCommentText = $commentDiv.find('p').text();
			$commentDiv.find('p').hide(); // p 태그 숨기기 추가
			$commentDiv.find('.update-comment-textarea').val(currentCommentText).show(); // textarea 보여주기
			$(this).text("저장");
		} else {
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
					$commentDiv.find('p').text(response.updatedComment).show(); // p 태그를 다시 보여주기
					$commentDiv.find('.update-comment-textarea').hide(); // textarea 숨기기
					$commentDiv.find('.comment-date').text(response.commuCommentUpdateDate);
					$commentDiv.find('.update-comment').text("수정");
				},
				error: function(err) {
					console.log(err);
					alert("댓글 수정 중 오류 발생");
				}
			});
		}
	});

	$(".comment-list-section").on("click", ".delete-comment", function() {
		var commuCommentId = $(this).data("id");
	    var parentCommentId = $(this).closest('.single-comment').data('id');
		$.ajax({
			url: "/commu/comment/delete/" + commuCommentId,
			type: "POST",
			contentType: 'application/json',
			dataType: 'json',
			success: function() {
				$(".single-comment[data-id='" + commuCommentId + "']").remove();
				var commentCountElement = $(".comment-list-section h3 span");
				var currentCount = parseInt(commentCountElement.text());
				updateReplyCount(parentCommentId); // 답글이 삭제된 댓글의 ID를 업데이트
				commentCountElement.text(currentCount - 1);
			},
			error: function(err) {
				console.log(err);
				alert("댓글 삭제 중 오류 발생");
			}
		});
	});
});
