//SVG 경로 및 길이 가져오기
var progressPath = document.querySelector('.progress-wrap svg.progress-circle path');
var pathLength = progressPath.getTotalLength();

// SVG 경로 초기화
progressPath.style.transition = 'none';
progressPath.style.strokeDasharray = pathLength + ' ' + pathLength;
progressPath.style.strokeDashoffset = pathLength;
progressPath.getBoundingClientRect();
progressPath.style.transition = 'stroke-dashoffset 10ms linear';

// 스크롤 시 프로그레스 업데이트
var updateProgress = function() {
	var scroll = $(window).scrollTop();
	var height = $(document).height() - $(window).height();
	var progress = pathLength - scroll * pathLength / height;
	progressPath.style.strokeDashoffset = progress;
};

// 스크롤 이벤트에 프로그레스 업데이트 연결
$(window).on('scroll', updateProgress);

// 스크롤 위치에 따라 프로그레스 표시 여부 결정
var offset = 50;
var duration = 50;

$(window).on('scroll', function() {
	if ($(this).scrollTop() > offset) {
		$('.progress-wrap').addClass('active-progress');
	} else {
		$('.progress-wrap').removeClass('active-progress');
	}
});

// 프로그레스 클릭 시 페이지 상단으로 스크롤
$('.progress-wrap').on('click', function(event) {
	event.preventDefault();
	$('html, body').animate({ scrollTop: 0 }, duration);
	return false;
});

