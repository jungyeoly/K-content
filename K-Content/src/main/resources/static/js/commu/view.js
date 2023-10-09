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
  // 페이지 로딩 시 댓글과 답글 관련 초기 설정
    initializeComments();

    // 이벤트 핸들러 등록
    registerEventHandlers();

    function initializeComments() {
        $(".replies").hide();
        updateAllReplyCounts();
        updateCommentCount();
    }

    function updateAllReplyCounts() {
        $(".single-comment").each(function() {
            var commuCommentId = $(this).data("id");
            updateReplyCount(commuCommentId);
        });
    }

    function updateCommentCount() {
        var comments = $(".single-comment").length;
        $(".comment-list-section h3 span").text(comments + '개');
    }

    function getTotalRepliesCount(commuCommentId) {
        var $comment = $(".single-comment[data-id='" + commuCommentId + "']");
        var directReplies = $comment.find(".replies .single-comment").length;

        var nestedReplies = 0;
        if (directReplies > 0) {
            $comment.find("> .replies > .single-comment").each(function() {
                nestedReplies += getTotalRepliesCount($(this).data('id'));
            });
        }

        return directReplies + nestedReplies;
    }

    function updateReplyCount(commuCommentId) {
        var totalReplies = getTotalRepliesCount(commuCommentId);
        $(".single-comment[data-id='" + commuCommentId + "']").find(".reply-count").text(totalReplies + '개');
        var $parentComment = $(".single-comment[data-id='" + commuCommentId + "']").closest('.replies').closest('.single-comment');
        if ($parentComment.length) {
            updateReplyCount($parentComment.data('id'));
        }
    }




	// 새로운 댓글/대댓글 HTML 생성
	function createCommentHTML(comment) {
		var replyButtonHTML = comment.commuCommentRefId ? '' : `<button class="view-replies-btn" style="display:none;">답글(<span class="reply-count">0개</span>)</button>`;

		return `
            <div class="single-comment" data-id="${comment.commuCommentId}">
                <strong>${comment.commuCommentMberId}</strong>
                <p>${comment.commuCommentCntnt}</p>
                <div class="comment-date">${comment.commuCommentRegistDate}</div>
                <div class="comment-buttons">
                    <button class="reply-to-comment">답글</button>
                    <button class="update-comment" data-id="${comment.commuCommentId}">수정</button>
                    <button class="delete-comment" data-id="${comment.commuCommentId}">삭제</button>
                    ${replyButtonHTML}
                    <div class="replyForm">
                        <form>
                            <input type="hidden" name="commuCommentRefId" value="${comment.commuCommentId}">
                            <textarea name="commuCommentCntnt" rows="2" placeholder="답글을 입력하세요."></textarea>
                            <button type="button" class="cancel-reply">취소</button>
                            <button type="button" class="post-reply">등록</button>
                        </form>
                    </div>
                <div class="replies"></div>
            </div>`;
	}


	// 초기 댓글 수 업데이트
	updateCommentCount();



	function postComment(formData, isReply, callback) {
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
					// 원본 댓글을 찾아냅니다.
					var $originalComment = $(".single-comment[data-id='" + formData.commuCommentRefId + "']");
					// 원본 댓글의 직접적인 답글 영역을 찾습니다.
					var $repliesDiv = $originalComment.find("> .replies").first();

					// 새로운 답글 HTML을 올바른 위치에 삽입합니다.
					$repliesDiv.append(newComment);

					// 답글 추가 후 원본 댓글의 답글 보기 버튼을 업데이트
					var $viewRepliesBtn = $originalComment.find(".view-replies-btn");
					if ($viewRepliesBtn.length === 0) {
						$originalComment.find(".comment-buttons").append('<button class="view-replies-btn">답글(<span class="reply-count">0개</span>)</button>');
					} else {
						$viewRepliesBtn.show();
					}

					// 답글 추가 후 원본 댓글의 답글 수 업데이트
					updateReplyCount(formData.commuCommentRefId);


					// 답글 입력 폼 초기화 및 숨기기
					$(".replyForm:visible").find("[name='commuCommentCntnt']").val('');
					$(".replyForm:visible").hide();
				} else {
					// 새 댓글을 댓글 리스트의 가장 아래에 추가
					$(".comment-list-section").append(newComment);
				}
				// 전체 댓글 수 업데이트
				updateCommentCount();

				// 콜백이 있다면 호출
				if (callback) callback();
				// 페이지 맨 아래로 스크롤 이동
				$("html, body").animate({ scrollTop: $(document).height() }, "slow");
			},
			error: function(err) {
				console.log(err);
				alert("댓글 작성 중 오류 발생");
			}
		});
	}
function registerEventHandlers() {
	$(".comment-list-section").on("click", ".view-replies-btn", function(e) {
		e.stopPropagation();
		var $repliesDiv = $(this).closest(".single-comment").find(".replies").first();
		$repliesDiv.toggle(); // 이것만으로는 모든 관련된 답글을 표시할 수 없습니다.

		// 추가: 원본 댓글에 관련된 모든 답글을 표시합니다.
		$repliesDiv.find(".replies").show();
	});
	$(".comment-list-section").on("click", ".cancel-reply", function(e) {
		e.stopPropagation(); // 이벤트 전파 중단
		var $replyForm = $(this).closest(".replyForm");
		$replyForm.css("display", "none");
	});
	$(".comment-list-section").on("click", ".single-comment", function(e) {
		e.stopPropagation();


		// 클릭한 댓글 또는 답글의 답글 폼만 보여줍니다.
		$(this).find(".replyForm").first().show();
	});
	$("#submitComment").on("click", function(e) {
		e.preventDefault();
		var commuCommentId = $(this).data("id");
		$(".replyForm").hide();

		// 댓글 작성 후에만 조회하도록 아래의 조건을 추가
		if (commuCommentId) {
			$.ajax({
				url: "/commu/comment/" + commuCommentId,
				type: "GET",
				dataType: 'json',
				success: function(response) {
					if (response.status === "success") {
						var commuCommentWithReplies = response.commuCommentWithReplies;
						var newComment = createCommentHTML(commuCommentWithReplies);



						// 댓글을 화면에 추가
						$(".comment-list-section").append(newComment);
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
		}
	});
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

		postComment(formData, false, function() {
			$("#commentForm").find("[name='commuCommentCntnt']").val('');  // 댓글 폼 초기화
			updateCommentCount();
		});
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

		postComment(formData, true, function() {
			// 답글을 추가한 후 원본 댓글의 답글 수를 업데이트합니다.
			updateReplyCount(formData.commuCommentRefId);

			// 전체 댓글 수를 업데이트합니다.
			updateCommentCount();
		});
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

			$.ajax({
				url: "/commu/comment/update/" + commuCommentId,
				type: "POST",
				data: JSON.stringify({
					commuCommentId: commuCommentId,
					commuCommentCntnt: updatedCommentText
				}),
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
	// 댓글 삭제
	$(".comment-list-section").on("click", ".delete-comment", function() {
		var commuCommentId = $(this).data("id");
		var isOriginComment = $(this).closest('.single-comment').data('is-origin') === 'true';

		// 부모 댓글 ID를 찾습니다.
		var parentCommentId = $(this).closest('.single-comment').find('.replies').data('parent-id');

		$.ajax({
			url: "/commu/comment/delete/" + commuCommentId,
			type: "POST",
			contentType: 'application/json',
			dataType: 'json',
			success: function() {
				// 댓글을 삭제합니다.
				$(".single-comment[data-id='" + commuCommentId + "']").remove();

				// 만약 부모 댓글 ID가 존재하면, 해당 댓글의 답글 개수를 업데이트합니다.
				if (parentCommentId) {
					updateReplyCount(parentCommentId);
				}

				// 전체 댓글 개수를 업데이트합니다.
				updateCommentCount();
			},
			error: function(err) {
				console.log(err);
				alert("댓글 삭제 중 오류 발생");
			}
		});
	});

}
}); 