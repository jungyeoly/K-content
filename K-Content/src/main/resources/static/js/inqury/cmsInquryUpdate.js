$(document).ready(function() {
    $('#cntnt').summernote({
        placeholder: '내용을 작성하세요',
        height: 400,
        maxHeight: 400
    });
    $("#userUpdate").submit(function(event) {
        event.preventDefault();

        var cntnt = $("#cntnt").val();

        if (cntnt.trim() === "") {
            Swal.fire({
                title: '내용을 입력해주세요',
                icon: 'warning',
                confirmButtonText: '확인',
                confirmButtonColor: '#14dbc8'
            });
        } else {
            Swal.fire({
                title: '수정하시겠습니까?',
                icon: 'question',
                showCancelButton: true,
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                confirmButtonColor: '#14dbc8',
				reverseButtons: true, // 버튼 순서 거꾸로
            }).then((result) => {
                if (result.isConfirmed) {
                    this.submit();
                }
            });
        }
    });
});
