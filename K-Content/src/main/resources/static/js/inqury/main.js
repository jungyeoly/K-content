$(document).ready(function() {
	let page = "[[${session.page}]]"
	
	if(page == 'null') {
		page = 1;
	}

	$.ajax({
		url: "/inqury/" + page,
		type: "GET",
		success: function(data) {
		var layout = $(".layout");
		layout.append(data);
		}
	})
})