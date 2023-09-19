$(document).ready(function() {
	const bkmk = $("#cntntBkmk");
	var star = $(".goodsBkmk"); 
	
	bkmk.click(function() {
		if($(this).hasClass("fa-regular")) {
			$(this).addClass("fa-solid").removeClass("fa-regular");
			$(this).css("color", "#14dbc8")
		} else {
			$(this).addClass("fa-regular").removeClass("fa-solid");
			$(this).css("color", "")
		}
  	});
	star.click(function(event) {
		event.preventDefault();
		if($(this).hasClass("fa-regular")) {
			$(this).addClass("fa-solid").removeClass("fa-regular");
			$(this).css("color", "#14dbc8")
		} else {
			$(this).addClass("fa-regular").removeClass("fa-solid");
			$(this).css("color", "")
		}
	})
})