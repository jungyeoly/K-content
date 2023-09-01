$(function(){
    $(".inqry-row").click(function() {
        var passwordForm = $(this).find(".password-form");
        var inqryId = $(this).data("inqry-id");
		
        $(".password-form").addClass("hidden");
        passwordForm.toggleClass("hidden");
    });

    $(".submit-btn").click(function(e) {
        e.preventDefault();
        var passwordForm = $(this).closest(".password-form");
        var inqryId = passwordForm.find("input[name='inqryId']").val();
        var enteredPwd = passwordForm.find(".password-input").val();
        
        if (enteredPwd === "") {
			alert("비밀번호를 입력하세요.");
			return;
		}			

        $.ajax({
            url: "/inqury/check-password",
            method: "post",
            data: {
                inqryId: inqryId,
                enteredPwd: enteredPwd
            },
            success: function(data) {
				var inqryId = passwordForm.closest(".inqry-row").data("inqry-id");
                window.location.href = "/inqury/detail/" + inqryId;
            },
            error: function() {
            	alert("비밀번호가 일치하지 않습니다.");
            }
        });
    });

	$(function() {
			$(".selpage").click(function() {
				var nowPage = $(this).data("selpage");
				
				$.ajax({
					url: "/inqury/" + nowPage,
					method: "get",
					success: function(data) {
						let layout = $(".layout");
						layout.find(".container").remove();
						layout.append(data)
					}
				})
			})
			
		})
});