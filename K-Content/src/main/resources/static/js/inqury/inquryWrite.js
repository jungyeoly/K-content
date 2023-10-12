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
        		});
        } else if (cntnt.trim() === "") {
        	Swal.fire({
        		   title: '내용을 입력해주세요',
        		   icon: 'warning',
        		   confirmButtonText: '확인', // confirm 버튼 텍스트 지정
        		});
        } else {
            this.submit();
        }
    });
});