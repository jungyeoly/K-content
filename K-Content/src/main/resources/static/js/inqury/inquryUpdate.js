$(document).ready(function() {
    $("#userUpdate").submit(function(event) {
        event.preventDefault();

        var title = $("#title").val();
        var cntnt = $("#cntnt").val();

        if (title.trim() === "") {
            Swal.fire({
                title: '제목을 입력해주세요',
                icon: 'warning',
                confirmButtonText: '확인',
            });
        } else if (cntnt.trim() === "") {
            Swal.fire({
                title: '내용을 입력해주세요',
                icon: 'warning',
                confirmButtonText: '확인',
            });
        } else {
            Swal.fire({
                title: '수정하시겠습니까?',
                icon: 'question',
                showCancelButton: true,
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
				reverseButtons: true, // 버튼 순서 거꾸로
            }).then((result) => {
                if (result.isConfirmed) {
                    this.submit();
                }
            });
        }
    });
});
