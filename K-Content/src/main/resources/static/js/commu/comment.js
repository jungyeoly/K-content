


$(document).ready(function() {

})


function pageRe() {

	commentFi = document.getElementById("comment-Box");
	commentFi.innerHTML = ' '

	$.ajax({
		url: "/commu/detail/comment",
		type: "GET",
		async: true,
		data: { commuId: document.getElementById("commuId").value },
		success: function(data) {
			let layout = $("#comment-Box");
			layout.find("#comment-Box").remove();
			layout.append(data);
		}
	});
}

document.querySelectorAll('.reply-show').forEach(function(button) {
	button.addEventListener('click', function(event) {
		const clickedButton = event.currentTarget;
		const commentId = clickedButton.getAttribute('data-id');
		console.log(commentId);
		const parentDiv = clickedButton.closest('.comment-buttons');

		const targetReplyBoxes = parentDiv.querySelectorAll(`.reply-box-update[data-id="${commentId}"]`);
		targetReplyBoxes.forEach(function(targetReplyBox) {
			if (targetReplyBox) {
				targetReplyBox.style.display = (targetReplyBox.style.display === 'none' || !targetReplyBox.style.display) ? 'block' : 'none';
			}
		});
	});
});


function postComment() {
	$.ajax({
		url: "/commu/detail/comment",
		type: "POST",
		async: true,
		data: {
			commuCommentCommuId: document.getElementById("commuId").value,
			commuCommentCntnt: document.getElementById("cntnt").value
		},
		success: function() {
			pageRe();
		},
		error: function(err) {
			console.log(err);
			alert("댓글 작성 중 오류 발생");
		}
	});
}

function postReply(commentID) {

	// 해당 버튼 요소를 선택합니다.
	var replyForm = $('.replyForm').find(`#${commentID}`);

	inputText = replyForm.val();
	$.ajax({
		url: "/commu/detail/reply",
		type: "POST",
		async: true,
		data: {
			commuCommentCommuId: document.getElementById("commuId").value,
			coCntnt: inputText,
			async: true,
			commuCommentRefId: commentID
		},
		success: function() {

			pageRe();
		},
		error: function(err) {
			console.log(err);
			alert("댓글 작성 중 오류 발생");
		}
	});

}

function deleteComment(commentID, refID) {
	Swal.fire({
		title: '정말로 삭제하시겠습니까?',
		text: "이 작업은 되돌릴 수 없습니다.",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: '예, 삭제합니다!',
		cancelButtonText: '취소'
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				url: "/commu/comment/delete",
				type: "POST",
				async: true,
				data: {
					commuCommentId: commentID,
					commuCommentRefId: refID
				},
				success: function() {
					pageRe();
				},
				error: function(err) {
					console.log(err);
					Swal.fire({
						title: '오류!',
						text: '댓글 작성 중 오류 발생',
						icon: 'error',
						confirmButtonText: '확인'
					});
				}
			});
		}
	});
}


$(document).ready(function() {
	// 페이지 로딩 시 댓글과 답글 관련 초기 설정
	initializeComments();

	// 이벤트 핸들러 등록
	registerEventHandlers();

	function initializeComments() {
		$(".replies").hide();

		updateCommentCount();
	}

	function updateCommentCount() {
		var comments = $(".single-comment").length;
		$(".comment-list-section h3 span").text(comments + '개');
	}

	// 초기 댓글 수 업데이트
	updateCommentCount();

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


		$(".comment-list-section").on("click", ".reply-show", function(e) {
			e.stopPropagation();
			// 모든 답글 폼을 숨깁니다.
			$(".reply-box").hide();
			// 클릭된 "답글" 버튼에 해당하는 답글 폼만 보여줍니다.
			$(this).closest(".single-comment").find(".reply-box").show();
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

			var commuCommentCntnt = $("#commentForm").find("[name='commuCommentCntnt']").val().trim(); // 앞뒤 공백 제거

			if (!commuCommentCntnt) { // 댓글 내용이 없는 경우
				alert("댓글 내용을 입력해주세요.");
				return; // 댓글 등록 중단
			}

			var formData = {
				commuCommentCommuId: $("#commentForm").find("[name='commuCommentCommuId']").val(),
				commuCommentCntnt: $("#commentForm").find("[name='commuCommentCntnt']").val()
			};

			postComment(formData, false, function() {
				$("#commentForm").find("[name='commuCommentCntnt']").val('');  // 댓글 폼 초기화
				updateCommentCount();
			});
		});
		$(".comment-list-section").on("click", ".update-comment", function(e) {
			e.stopPropagation();
			//다른 textarea 숨기기
			$('.replyForm textarea').hide();
			$('.replyForm button').hide();
			$(".reply-id").show();

			var originalCommentId = $(this).data("original-id");
			var replyCommentId = $(this).data("reply-id");

			var isReply = false;
			var commentIdToUse;

			if (originalCommentId) {
				commentIdToUse = originalCommentId;
			} else if (replyCommentId) {
				commentIdToUse = replyCommentId;
				isReply = true;
			}

			var $textarea;
			if (isReply) {
				$textarea = $('.update-comment-textarea[data-reply-id="' + commentIdToUse + '"]');
			} else {
				$textarea = $('.update-comment-textarea[data-id="' + commentIdToUse + '"]'); // 이 부분은 원래 댓글에 대한 data-id를 참조하려는 경우를 위해 유지
			}
			$textarea.show();

			var currentText;
			var $currentP;


			if (isReply) {
				$currentP = $(this).closest('.reply-box').find('p');
			} else {
				$currentP = $(this).closest('.update').find('p');
			}

			currentText = $currentP.text();
			if ($(this).text() === "수정") {
				$textarea.val(currentText).show();
				$currentP.hide();

				$('.update-comment').text("수정");
				var updateText = $textarea.val().trim();

				if (!updateText) {
					alert("댓글 내용을 입력해주세요.");
					return;
				}
				// 취소 버튼 추가
				var cancelButton = '<button class="cancel-update">취소</button>';
				$(this).after(cancelButton);
				$(this).text("저장");
			} else {
				var updateText = $textarea.val();
				console.log(updateText);
				$.ajax({
					url: "/commu/comment/update",
					type: "POST",
					async: true,
					data: {
						commuCommentId: commentid,
						commuCommentCntnt: updateText
					},
					success: function() {
						pageRe();
					},
					error: function(err) {
						console.log(err);
						alert("댓글 수정 중 오류 발생");
					}
				});
			}
		});

		// 취소 버튼 클릭 이벤트
		$(".comment-list-section").on("click", ".cancel-update", function(e) {
			e.stopPropagation();
			var $updateButton = $(this).prev();
			var commentid = $updateButton.data("id");
			var $textarea = $('.update-comment-textarea[data-id="' + commentid + '"]');

			$textarea.hide();
			 $textarea.closest('.comment-box').find('p').show();

			$updateButton.text("수정");
			$(this).remove(); //취소 버튼 제거
		
		});			

	}

});
