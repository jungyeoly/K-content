
//페이지 로딩 시 댓글 페이지 들고오기
$.ajax({
	url: "/commu/detail/comment",
	type: "GET",
	data: { commuId: document.getElementById("commuId").value },
	success: function(data) {
		let layout = $("#comment-Box");
		layout.find("#comment-Box").remove();
		layout.append(data);
	}
});

function showDeleteConfirmModal() {
	Swal.fire({
		title: '삭제 확인',
		text: '정말로 삭제하시겠습니까?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonText: '확인',
		cancelButtonText: '취소'
	}).then((result) => {
		if (result.isConfirmed) {
			const deleteForm = document.querySelector('form[method="post"]');
			if (deleteForm) {
				deleteForm.submit();
			}
		}
	});
}
