
$(document).ready(function() {
	let currentPage = 1; // 페이지 초기화
	let loading = false; // 페이지 로딩 중 여부를 나타내는 플래그
	let commuCateCode = null;  // 현재 선택된 카테고리 저장 변수



	loadPage(currentPage);

	// 페이지 번호 클릭 이벤트
	$(document).on('click', '.page-link', function(e) {
		e.preventDefault();
		if (!loading) {
			const selectedPage = $(this).data('page');
			if (selectedPage !== currentPage) {
				if (commuCateCode) {
					loadCategoryPosts(commuCateCode, selectedPage);
				} else {
					loadPage(selectedPage);
				}
			}
		}
	});


	// 이전 페이지 버튼 클릭 이벤트
	$(document).on('click', '.page-link.prepage', function(e) {
		e.preventDefault();
		if (!loading && currentPage > 1) {
			loadPage(currentPage - 1);
		}
	});

	// 다음 페이지 버튼 클릭 이벤트
	$(document).on('click', '.page-link.nextpage', function(e) {
		e.preventDefault();
		if (!loading && currentPage < totalPageCount) {
			loadPage(currentPage + 1);
		}
	});

	// 메인 페이지
	function loadPage(page) {
		if (typeof page === 'undefined' || !page) {
			page = 1;
		}

		loading = true; // 페이지 로딩 중 플래그 설정
		$.ajax({
			url: `/commu/ajax/${page}`,
			method: 'GET',
			dataType: 'json',
			cache: false,
			success: function(response) {
				updatePostList(response.commulist);
				updatePagination(response.nowPage, response.totalPageCount);
				currentPage = page;
			},
			error: function(error) {
				console.error("Failed to load posts:", error);
			},
			complete: function() {
				loading = false; // 페이지 로딩 완료 시 플래그 해제
			}
		});
	}


	// 카테고리 클릭 이벤트
	$(document).on('click', '.cate', function() {
		commuCateCode = $(this).data('maincate-value');
		loadCategoryPosts(commuCateCode, 1); // 페이지를 1페이지로 초기화하여 로드
	});

	// 카테고리 클릭 이벤트
	function loadCategoryPosts(commuCateCode, page) {
		$.ajax({
			url: `/commu/commucatecode/${commuCateCode}?page=${page}`,
			type: 'GET',
			dataType: 'json',
			success: function(response) {
				updatePostList(response.posts);
				updatePagination(response.nowPage, response.totalPageCount);
				currentPage = page; // currentPage를 업데이트하여 페이지 번호를 유지
			},
			error: function(error) {
				console.error("Error fetching posts by category:", error);
			}
		});
	}

	// 페이지 목록을 동적으로 생성하는 함수
	function updatePagination(nowPage, totalPageCount) {
		let paginationHtml = '';
		for (let i = 1; i <= totalPageCount; i++) {
			const pageLinkClass = i === nowPage ? 'page-item active' : 'page-item';
			paginationHtml += `
            <li class="${pageLinkClass}">
                <a class="page-link" data-page="${i}">${i}</a>
            </li>
        `;
		}

		const paginationElement = document.querySelector('.pagination'); // 페이지 목록을 표시할 요소 선택
		if (paginationElement) {
			paginationElement.innerHTML = paginationHtml; // 페이지 목록을 요소에 할당
		}
	}





	function updatePostList(posts) {
		let postListHtml = '';
		posts.forEach(commu => {
			postListHtml += `
                <tr class="commu-row" data-commu-id="${commu.commuId}">
                    <td>${commu.commuId}</td>
                    <td>${commu.commonCodeVal}</td>
                    <td><a href="/commu/detail/${commu.commuId}">${commu.commuTitle}</a></td>
                    <td>${commu.commuMberId}</td>
                    <td>${commu.commuReadCnt}</td>
                    <td>${commu.commuRegistDate}</td>
                </tr>
            `;
		});
		$('tbody').empty().append(postListHtml);
	}
});
