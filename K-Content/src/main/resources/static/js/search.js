$(document).ready(function() {
	$("#searchForm").submit(function(event) {
		event.preventDefault();
		    
		var keyword = $("#searchInput").val();
		$.ajax({
			url: "/user/content",
			type: "POST",
			data: { keyword : keyword },
			success: function(data) {
				let layout = $(".layout");
				layout.find(".container").remove();
				layout.append(data);
			}
		})
	});
});