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

// 페이지 로딩 시 댓글과 답글 관련 초기 설정
initializeComments();

function initializeComments() {
	updateCommentCount();
}

function updateCommentCount() {
	var comments = $(".single-comment").length;
	$(".comment-list-section h3 span").text(comments + '개');
}

// 초기 댓글 수 업데이트
updateCommentCount();

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

	var targetReplyBox = $(this).closest(".single-comment").find(".reply-box");

	// 모든 다른 답글 폼을 숨깁니다.
	$(".reply-box").not(targetReplyBox).hide();

	// 모든 textarea도 숨깁니다.
	$('.update-comment-textarea').hide();

	// 클릭된 "답글" 버튼에 해당하는 답글 폼의 상태를 토글합니다.
	targetReplyBox.toggle();
});

$(".comment-list-section").on("click", ".reply-to-comment", function(e) {
	e.stopPropagation();

	var targetReplyForm = $(this).closest(".single-comment").find(".replyForm").first();

	// 모든 다른 답글 폼을 숨깁니다.
	$(".replyForm").not(targetReplyForm).hide();

	// 클릭된 "답글" 버튼에 해당하는 답글 폼의 상태를 토글합니다.
	targetReplyForm.toggle();
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
	//모든 textarea와 버튼을 숨김
	$('.update-comment-textarea').hide();

	// 기존의 취소 버튼을 모두 숨깁니다.
	$('.cancel-update').hide();
	//다른 textarea 숨기기
	$('.replyForm textarea').hide();
	$('.replyForm button').hide();
	$(".reply-id").show();
	// 현재 클릭된 버튼의 텍스트만 "수정"으로 변경합니다.


	// 모든 댓글과 답글의 수정 및 삭제 버튼을 표시
	/*$('.update-comment').text("수정");*/
	$('#delete, #delete_ref').show();

	// 모든 댓글과 답글의 원래 내용(p 태그)을 표시
	$('.update p, .reply-box-update p').show();


	var originalCommentId = $(this).data("original-id");
	var replyCommentId = $(this).data("reply-id");

	var isReply = false;
	var commentIdToUse;

	if (originalCommentId != null) {
		commentIdToUse = originalCommentId;
	} else if (replyCommentId) {
		commentIdToUse = replyCommentId;
		isReply = true;
	}

	var $textarea;
	if (isReply) {
		$textarea = $('.update-comment-textarea[data-reply-id="' + commentIdToUse + '"]');

	} else {
		$textarea = $('.update-comment-textarea[data-original-id="' + commentIdToUse + '"]'); // 이 부분은 원래 댓글에 대한 data-id를 참조하려는 경우를 위해 유지
		var chtext = $textarea.val();
		var chtext = $textarea.val().trim();("chtext " + chtext);
	}
	$textarea.show();

	var currentText;
	var $currentP;


	if (isReply) {
		$currentP = $(this).closest('.reply-box-update').find('p').first();

	} else {
		$currentP = $(this).closest('.update').find('p').first();
		// 이제 현재 textarea를 보여주고, 원래의 텍스트를 숨깁니다.
		$textarea.val($currentP.text()).show();
		$currentP.hide();

	}

	currentText = $currentP.text();

	if ($(this).text() === "수정") {
		//삭제 버튼 숨기기
		$(this).closest('div').find('#delete, #delete_ref').hide();
		$textarea.val(currentText).show();

		$textarea.show();
		$currentP.hide();

		$('.update-comment').text("수정");
		var chtext = $textarea.val().trim();
			if (chtext == null) {
			alert("댓글 내용을 입력해주세요.");
			return;
		}

		// 취소 버튼 추가
		var cancelButton = '<button class="cancel-update">취소</button>';
		$(this).after(cancelButton);
		$(this).text("저장");
		// 수정된 내용을 textarea에 반영
		$textarea.val(chtext).show();

	} else {
		var chtext = $textarea.val().trim();
	
		if (chtext == null) {
			alert("댓글 내용을 입력해주세요.");
			return;
		}

		$.ajax({
			url: "/commu/comment/update",
			type: "POST",
			async: true,
			data: {
				commuCommentId: commentIdToUse,
				commuCommentCntnt: chtext
			},
			success: function() {
				pageRe();
			},
			error: function(err) {
				alert("댓글 수정 중 오류 발생");
			}
		});
	}
});

// 취소 버튼 클릭 이벤트
$(".comment-list-section").on("click", ".cancel-update", function(e) {
	e.stopPropagation();
	var $updateButton = $(this).prev();
	var originalCommentId = $updateButton.data("original-id");
	var replyCommentId = $updateButton.data("reply-id");

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
		$textarea = $('.update-comment-textarea[data-original-id="' + commentIdToUse + '"]');
	}

	$textarea.hide();

	var $currentP;
	if (isReply) {
		$currentP = $textarea.closest('.reply-box').find('p');
	} else {
		$currentP = $textarea.closest('.update').find('p');
	}

	$currentP.show();

	$updateButton.text("수정");

	$(this).hide(); //취소 버튼 숨기기
	$(this).closest('div').find('#delete, #delete_ref').show();
});
