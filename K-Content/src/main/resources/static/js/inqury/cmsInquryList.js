$(function(){
   $(".inqry-row").click(function() {
        const inqryId = $(this).attr("data-inqry-id");
		console.log(inqryId);
		$.ajax({
            url: "/cs/inqry/detail/" + inqryId,
            method: "get",
            success: function(data) {
				let layout = $(".layout");
				layout.find(".container").remove();
				layout.append(data);
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