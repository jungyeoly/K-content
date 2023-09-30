$(document).ready(function() {
	$.ajax({
		url: "/user/content",
		type: "GET",
		success: function(result) {
			var layout = $(".layout");
			layout.append(result);
		}
	})

	$(".cate").click(function() {
		var cateValue = this.getAttribute('data-maincate-value');

		if (cateValue == null) {
			cateValue = 'All';
		}
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

})

function cntntDetail(cntntId) {

	const formHtml = `
<form id="contentDetail" action="/user/content/detail" method="get">
	<input  id="targetContentIdF" name="targetContentIdF"  />
</form>`;

	const doc = new DOMParser().parseFromString(formHtml, 'text/html');
	const form = doc.body.firstChild;
	document.body.append(form);
	document.getElementById("targetContentIdF").value = cntntId;
	document.getElementById('contentDetail').submit();
}

