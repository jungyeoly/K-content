$(document).ready(function() {
	$("#userDelete").submit(function(event) {
		event.preventDefault();

		Swal.fire({
			title: '정말 삭제하시겠습니까?',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#14dbc8',
			cancelButtonColor: '#d33',
			confirmButtonText: '확인',
			cancelButtonText: '취소',

			reverseButtons: true,

		}).then(result => {
			if (result.isConfirmed) {
				this.submit();
			}
		});
	});
});
