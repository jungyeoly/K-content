$(document).ready(function() {
	// URL에서 쿼리 문자열을 가져옴
	var queryString = window.location.search;
	
	// 쿼리 문자열에서 특정 매개변수 추출 (예: keyword)
	var searchParams = new URLSearchParams(queryString);
	var keyword = searchParams.get("keyword");
	
	var start = 1;
	var end = 15;
	
	if (keyword) {
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
	} else {
	    var cateValue = 'All'; // 초기값 설정
	
		$.ajax({
			url: "/user/content",
			type: "GET",
			success: function(result) {
				var layout = $(".layout");
				layout.append(result);
			}
		})
	}
	
			
	$(window).scroll(function() {
		if ( Math.round($(window).scrollTop()) + 1 >= $(document).height() - $(window).height() ) {
			start = end + 1;
			end = start + 14;
			$.ajax({
				type: 'GET',
				url: '/content/scroll',
				data: {start : start, end : end, cate: cateValue},
				success: function(result) {
					change(result);
				}
			}) 
		}
	})

	$(".cate").click(function() {
		start = 1;
		end = 15;
		$(".cate").removeClass("active");
		$(this).addClass("active");
		cateValue = this.getAttribute('data-maincate-value');
		
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


function change(result){
	for (var i = result.contentList.length - 1; i >= 0; i--) {
		/* console.log(data.contentList[i].cntntThumnail); */
		var Thumnail = result.contentList[i].cntntThumnail;
		var Title = result.contentList[i].cntntTitle;
		var cntntId = result.contentList[i].cntntId;
		var scroll = `
			  <img src="`+ Thumnail + `" class="card-img-top" alt="">
			  <div class="card-body">
			    <p class="card-title card-text text-center">` + Title + `</p>
			  </div>
		`;
		
		var divEl = document.createElement("div");
		divEl.setAttribute("class", "card mx-3 my-3");
		divEl.setAttribute("style", "width: 18rem;");
		divEl.setAttribute("cntntId", cntntId);
		divEl.setAttribute("onclick", "cntntDetail(this.getAttribute('cntntId'))");
		divEl.innerHTML = scroll;
		document.querySelector("#contentsList").appendChild(divEl);
	}
}
