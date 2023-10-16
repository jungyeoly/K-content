$(function(){
   $(".inqry-row").click(function() {
        const inqryId = $(this).attr("data-inqry-id");

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
	
	$(function() {
		$('ul.tab li').click(function() {
			var activeTab = $(this).attr('data-tab');
			var selectedValue = $(".tab li.on").data("tab");

			var page = 1;
			
			$.ajax({
				url:"/cs/inqry/" + page,
				method: "get",
				success: function (data) {
					var layout = $(".layout");
					layout.empty();
					layout.append(data);
					
					$('ul.tab li').removeClass('on');
					$('.tabcont').removeClass('on');
					$('#' + activeTab).addClass('on');
					$('.' + activeTab).addClass('on');
							
					$.ajax({
						url: "/cs/inqry/paging",
						method: "get",
						data: {"page" : page, "activeTab" : activeTab},
						success: function(data) {
							$("#allPage").empty();
							$("#unPage").empty();
							
							if(activeTab == "menu1") {
								$("#allPage").append(data);
							} else {
								$("#unPage").append(data);
							}
						}
					})
				}
			})
			
		})
	});
});