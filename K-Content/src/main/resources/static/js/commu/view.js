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
        if(reportForm) {
            reportForm.submit();
        }
    });
}





// '확인' 버튼에 한 번만 이벤트 리스너 추가
document.getElementById('confirmBtn').addEventListener('click', function() {
    const deleteForm = document.querySelector('form[method="post"]');
    if(deleteForm) {
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
			success: function(commucomment) {
				var newComment = `
                <div class="single-comment" data-id="${commucomment.commuCommentId}">
                    <strong>${commucomment.commuCommentMberId}</strong>
                    <p>${commucomment.commuCommentCntnt}</p>
                    <div class="comment-date">${commucomment.commuCommentRegistDate}</div>
                    <button class="update-comment" data-id="${commucomment.commuCommentId}">수정</button>
                    <button class="delete-comment" data-id="${commucomment.commuCommentId}">삭제</button>
                </div>
            `;
				$(".comment-list").append(newComment);
			},
			error: function(err) {
				console.log(err);
				alert("댓글 작성 중 오류 발생");
			}
		});
	});

	// 댓글 수정
	$(".comment-list").on("click", ".update-comment", function() {
		var commuCommentId = $(this).data("id");
		var updatedCommuCommentCntnt = prompt("수정할 내용을 입력하세요.");

		if (updatedCommuCommentCntnt) {
			var formData = {
				commuCommentId: commuCommentId,
				commuCommentCntnt: updatedCommuCommentCntnt
			};

			$.ajax({
				url: "/commu/comment/update/" + commuCommentId,
				type: "POST",
				data: JSON.stringify(formData),
				contentType: 'application/json',
				dataType: 'json',
				success: function(updatedComment) {
					var targetComment = $(".single-comment[data-id='" + commuCommentId + "']");
					targetComment.find("p").text(updatedComment.commuCommentCntnt);
				},
				error: function(err) {
					console.log(err);
					alert("댓글 수정 중 오류 발생");
				}
			});
		}
	});

	// 댓글 삭제
	$(".comment-list").on("click", ".delete-comment", function() {
		var commuCommentId = $(this).data("id");

		if (confirm("댓글을 삭제하시겠습니까?")) {
			$.ajax({
				url: "/commu/comment/delete/" + commuCommentId,
				type: "POST",
				contentType: 'application/json',
				dataType: 'json',
				success: function(response) {
					$(".single-comment[data-id='" + commuCommentId + "']").remove();
				},
				error: function(err) {
					console.log(err);
					alert("댓글 삭제 중 오류 발생");
				}
			});
		}
	});
});
