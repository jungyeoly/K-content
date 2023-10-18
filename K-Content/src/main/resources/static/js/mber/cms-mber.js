
$(document).ready(function() {
	// 페이지 로딩 시 초기 데이터 가져오기
	loadMberData(currentPage);

});
// 페이지네이션 링크 클릭 시 데이터 다시 가져오기
$(".pagination").on("click", "a", function(event) {
	event.preventDefault(); // 기본 이벤트 취소 (페이지 이동 방지)
	var page = $(this).attr("href").split("/").pop(); // 클릭한 링크의 페이지 번호 가져오기
	loadMberData(page); // 데이터 비동기로 가져오기
});

function loadMberData(page) {
	currentPage = page; // 현재 페이지 업데이트
	$
		.ajax({
			type: 'GET',
			url: '/cs/mber/list/' + page,
			dataType: 'json',
			success: function(data) {
				// 회원 정보를 표시할 HTML을 생성
				var html = '';
				data
					.forEach(function(mber, index) {
						var rownum = (page - 1) * 10 + index
							+ 1;
						html += '<tr>';
						html += '<td>' + rownum++ + '</td>';
						html += '<td>' + mber.mberRole
							+ '</td>';
						html += '<td>' + mber.mberId + '</td>';
						html += '<td>' + mber.mberName
							+ '</td>';
						html += '<td>' + mber.mberEmail
							+ '</td>';
						html += '<td>' + mber.mberGenderCode
							+ '</td>';
						if (mber.mberBirth == null) {
							html += '<td>-</td>';
						} else {
							html += '<td>' + mber.mberBirth
								+ '</td>';
						}
						if (mber.mberPhone == null) {
							html += '<td>-</td>';
						} else {
							html += '<td>' + mber.mberPhone
								+ '</td>';
						}
						html += '<td>' + mber.mberRegistDate
							+ '</td>';
						if (mber.mberUpdateDate == null) {
							html += '<td>-</td>';
						} else {
							html += '<td>'
								+ mber.mberUpdateDate
								+ '</td>';
						}
						if (mber.mberStat == '비활성화') {
							html += '<td class="fw-bold text-danger">'
								+ mber.mberStat + '</td>';
						} else {
							html += '<td class="fw-bold">'
								+ mber.mberStat + '</td>';
						}
						html += '<td>';

						if (mber.mberRole != '관리자') {
							if (mber.mberStat == '비활성화') {
								html += '<button class="btn" data-mber-id="'
									+ mber.mberId
									+ '" onclick="changeStatus(this.getAttribute(\'data-mber-id\'), \'C0202\')">변경</button>';
							} else if (mber.mberStat == '활성화') {
								html += '<button class="btn" data-mber-id="'
									+ mber.mberId
									+ '" onclick="changeStatus(this.getAttribute(\'data-mber-id\'), \'C0201\')">변경</button>';
							}
						}
						html += '</td>';
						html += '</tr>';
					});
				// 회원 리스트 테이블 업데이트
				$('#mberTable tbody').html(html);
				updatePagination();
				$(".pagination").hide();
			},
			error: function() {
				alert('데이터를 불러오는데 실패했습니다.');
			}
		});
}
function updatePagination() {
	// 기존 페이지네이션 링크 지우기
	$(".pagination").empty();
	var currentPage = "[[${nowPage}]]";
	var startPage = "[[${startPage}]]";
	var endPage = "[[${endPage}]]";
	var totalPageBlock = "[[${totalPage}]]";
	
	console.log(currentPage)
	console.log(startPage)
	console.log(endPage)
	console.log(totalPage)

	// totalPageBlock을 기반으로 페이지네이션 링크 추가
	for (var i = startPage; i <= endPage; i++) {
		var pageLink = $('<a class="page-link selpage">');
		pageLink.text(i);
		pageLink.attr('href', '/cs/mber/list/' + i);
		if (currentPage == i) {
			pageLink.addClass('active');
		}
		$(".pagination").append($('<li>').append(pageLink));
	}
}

document.getElementById("search-btn").addEventListener("click", function(event) {
	event.preventDefault(); // 기본 이벤트 취소 (페이지 새로고침 방지)

	var findType = document.getElementById("findType").value;
	var findKeyword = document.getElementById("findKeyword").value;
	console.log(findType);
	console.log(findKeyword);


	// 검색 결과를 비동기로 가져오는 함수 호출
	searchMber(findType, findKeyword);
});

function searchMber(findType, findKeyword) {

	$.ajax({
		type: 'GET',
		url: '/cs/mber/search-mber',
		data: {
			findType: findType,
			findKeyword: findKeyword,
			page: 1
		},
		dataType: 'json',
		success: function(data) {
			var html = '';
			if (data.length === 0) {
				// 검색 결과가 없는 경우 알림 표시
				html = '<tr><td colspan="12" class="text-center">검색 결과가 없습니다.</td></tr>';
			} else {
				data.forEach(function(mber, index) {
					var rownum = (currentPage - 1) * 10 + index + 1;
					html += '<tr>';
					html += '<td>' + rownum++ + '</td>';
					html += '<td>' + mber.mberRole + '</td>';
					html += '<td>' + mber.mberId + '</td>';
					html += '<td>' + mber.mberName + '</td>';
					html += '<td>' + mber.mberEmail + '</td>';
					html += '<td>' + mber.mberGenderCode + '</td>';
					if (mber.mberBirth == null) {
						html += '<td>-</td>';
					} else {
						html += '<td>' + mber.mberBirth + '</td>';
					}
					if (mber.mberPhone == null) {
						html += '<td>-</td>';
					} else {
						html += '<td>' + mber.mberPhone + '</td>';
					}
					html += '<td>' + mber.mberRegistDate + '</td>';
					if (mber.mberUpdateDate == null) {
						html += '<td>-</td>';
					} else {
						html += '<td>' + mber.mberUpdateDate + '</td>';
					}
					if (mber.mberStat == '비활성화') {
						html += '<td class="fw-bold text-danger">' + mber.mberStat + '</td>';
					} else {
						html += '<td class="fw-bold">' + mber.mberStat + '</td>';
					}
					html += '<td>';
					if (mber.mberRole != '관리자') {
						if (mber.mberStat == '비활성화') {
							html += '<button class="btn" data-mber-id="' + mber.mberId + '" onclick="changeStatus(this.getAttribute(\'data-mber-id\'), \'C0202\')">변경</button>';
						} else if (mber.mberStat == '활성화') {
							html += '<button class="btn" data-mber-id="' + mber.mberId + '" onclick="changeStatus(this.getAttribute(\'data-mber-id\'), \'C0201\')">변경</button>';
						}
					}
					html += '</td>';
					html += '</tr>';
				});
			}
			$('#mberTable tbody').html(html); // 검색 결과 업데이트
			updatePagination();

		},
		error: function() {
			alert('검색에 실패했습니다.');
		}
	});
}

function changeStatus(mberId, newStatus) {
	$.ajax({
		type: 'POST',
		url: '/cs/mber/change-stat',
		data: {
			mberId: mberId,
			newStatus: newStatus
		},
		success: function(response) {
			if (response.success) {
				loadMberData(currentPage);
			} else {
				alert('상태 변경 실패했습니다.');
			}
		},
		error: function() {
			alert('서버 오류로 인해 상태 변경에 실패했습니다.');
		}
	});
}
