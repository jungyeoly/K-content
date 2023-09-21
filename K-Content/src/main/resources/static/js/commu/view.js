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