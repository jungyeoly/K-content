$(document).ready(function() {
    $("#userWrite").submit(function(event) {
        event.preventDefault();

        var cntnt = $("#cntnt").val();

        if (cntnt.trim() === "") {
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