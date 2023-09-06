$('#main').mouseover(function() {
	$('#comm-cate').css('display', 'none');
	$('#main-cate').css('display', 'block');
})

$('#main-cate').mouseleave(function() {
	$('#main-cate').css('display', 'none');
})

$("#comm").mouseover(function() {
	$('#main-cate').css('display', 'none');
	$('#comm-cate').css('display', 'block');
})

$('#comm-cate').mouseleave(function() {
	$('#comm-cate').css('display', 'none');
})

$('#cs').mouseover(function() {
	$('#comm-cate').css('display', 'none');
	$('#main-cate').css('display', 'none');
})

$('.logo').mouseover(function() {
	$('.categories-nav').css('display', 'none');
})