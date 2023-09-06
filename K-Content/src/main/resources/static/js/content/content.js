$(document).ready(function() {
		
		$.ajax({
			url: "/user/content",
			type: "GET",
			success: function(data) {
				console.log(data);
				var layout = $(".layout");
				layout.append(data);
			}
		})
	})
	
	$(".cate").click(function() {
		var cateValue = this.getAttribute('data-cate-value');
        console.log('선택한 cate 값: ' + cateValue);
        
        $.ajax({
        	url: "/user/content",
        	type: "GET",
        	data: { cate: cateValue },
        	success: function(data) {
				let layout = $(".layout");
				layout.find(".container").remove();
				layout.append(data);
        	}
        })
	})