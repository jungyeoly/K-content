$(document).ready(function() {
    // $('#inqry-cntnt').summernote({
    //     placeholder: '내용을 작성하세요',
    //     height: 400,
    //     maxHeight: 400
    // });

    $('#cntnt').summernote({
        placeholder: '내용을 작성하세요',
        height: 400,
        maxHeight: 400
    });
    $("#userWrite").submit(function(event) {
        event.preventDefault();

        var cntnt = $("#cntnt").val();

        if (cntnt.trim() === "") {
        	Swal.fire({
        		   title: '내용을 입력해주세요',
        		   icon: 'warning',
        		   confirmButtonText: '확인', // confirm 버튼 텍스트 지정
                confirmButtonColor: '#14dbc8'
        		});
        } else {
            this.submit();
        }
    });
});
