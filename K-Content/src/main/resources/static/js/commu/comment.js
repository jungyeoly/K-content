
$(document).ready(function() {
	// $("#reply-box").hide();
	// 왜 하나만 가려지는지 찾기

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
	// registerEventHandlers();
	$.ajax({
		url: "/commu/detail/comment",
		type: "POST",
		async: true,
		data: {
			commuCommentCommuId: document.getElementById("commuId").value,
			commuCommentCntnt: document.getElementById("cntnt").value
		},
		success: function() {
			// 답글 추가 후 원본 댓글의 답글 수 업데이트
		//	 updateReplyCount(formData.commuCommentRefId);
			// console.log("dsf")
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


	// var textarea = replyForm.find('.input-reply-textarea');

	inputText = replyForm.val();
	console.log(replyForm.val());
	// console.log(inputText)
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
			// 답글 추가 후 원본 댓글의 답글 수 업데이트
			// updateReplyCount(formData.commuCommentRefId);
			// console.log("dsf")
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

    // Update the direct count
    $(".single-comment[data-id='" + commuCommentId + "']").find(".reply-count").text(totalReplies + '개');

    // Update the "보기 버튼" with the reply count
    $(".single-comment[data-id='" + commuCommentId + "']").find(".view-replies-btn").text("보기 (" + totalReplies + "개)");

    // If the comment is nested inside another comment, update the parent comment's reply count as well.
    var $parentComment = $(".single-comment[data-id='" + commuCommentId + "']").closest('.replies').closest('.single-comment');
    if ($parentComment.length) {
        updateReplyCount($parentComment.data('id'));
    }
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
			e.stopPropagation();  // 다른요소 못건들이게
			$(".update-comment-textarea").hide();
			$("p").show();

			var $commentDiv = $(this).closest('.update');
			commentID = $(this).data("id");
			console.log("commentID", commentID);
			parentUpdateDiv = $(this).closest('.update');
			updateText = parentUpdateDiv.find('textarea').val();
			console.log("updateText", updateText);

			if ($(this).text() === "수정") {
				var currentCommentText = $commentDiv.find('p').text();
				$commentDiv.find('p').hide(); // p 태그 숨기기 추가
				$commentDiv.find('.update-comment-textarea').val(currentCommentText).show(); // textarea 보여주기
				$('.update-comment').text("수정");
				$(this).text("저장");

			} else {
				$.ajax({
					url: "/commu/comment/update",
					type: "POST",
					async: true,
					data: {
						commuCommentId: commentID,
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
	}

});
