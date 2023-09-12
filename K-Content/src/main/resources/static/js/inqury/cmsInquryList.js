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
            url: "/cs/inqry/check-password",
            method: "post",
            data: {
                inqryId: inqryId,
                enteredPwd: enteredPwd
            },
            success: function(data) {
				var inqryId = passwordForm.closest(".inqry-row").data("inqry-id");
                window.location.href = "cs/inqry/detail/" + inqryId;
            },
            error: function() {
            	alert("잘못된 접근 입니다.");
            }
        });
    });

	$(".selpage").click(function() {
		var nowPage = $(this).data("selpage");
		
		$.ajax({
			url: "/cs/inqry/" + nowPage,
			method: "get",
			success: function(data) {
				let layout = $(".layout");
				layout.find(".container").remove();
				layout.append(data)
			}
		})
	})
	
	$(".prepage").click(function() {
		var prePage = $(this).data("prepage") - 1;
		
		$.ajax({
			url : "/cs/inqry/" + prePage,
			method: "get",
			seccess: function(data) {
				let layout = $(".layout");
				layout.find(".container").remove();
				layout.append(data);
			}
		})
	})
	
	$(".nexpage").click(function() {
		var nexPage = $(this).data("nexpage") + 1;
		
		$.ajax({
			url : "/cs/inqry/" + prePage,
			method: "get",
			seccess: function(data) {
				let layout = $(".layout");
				layout.find(".container").remove();
				layout.append(data);
			}
		})
	})
});