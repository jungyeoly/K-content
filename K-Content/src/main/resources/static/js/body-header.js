$("#searchForm").submit(function(event) {
	event.preventDefault();

	var keyword = $("#searchInput").val();
	console.log(keyword);
	$.ajax({
		url: "/user/content",
		type: "POST",
		data: { keyword : keyword },
		success: function(data) {
			location.href = "/?keyword=" + encodeURIComponent(keyword);
		}
	})
});