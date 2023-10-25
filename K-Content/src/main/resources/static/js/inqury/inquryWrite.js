$(document).ready(function() {
    $("#userWrite").submit(function(event) {
        event.preventDefault();

        var title = $("#title").val();
        var cntnt = $("#cntnt").val();

        if (title.trim() === "") {
        	Swal.fire({
        		   title: '제목을 입력해주세요',
        		   icon: 'warning',
        		   confirmButtonText: '확인', // confirm 버튼 텍스트 지정
                confirmButtonColor: '#14dbc8',
        		});
        } else if (cntnt.trim() === "") {
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

    $('#cntnt').summernote({
        placeholder: '내용을 작성하세요',
        height: 400,
        maxHeight: 400
    });

});
