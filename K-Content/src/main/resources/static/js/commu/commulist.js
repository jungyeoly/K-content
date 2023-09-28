$(document).ready(function() {
	let currentPage = 1; // 페이지 초기화

	// 페이지 번호 클릭 이벤트 (이전/다음 버튼 제외)
	$(document).on('click', '.page-link.selpage', function(e) {
		e.preventDefault();
		const selectedPage = $(this).data('selpage');
		loadPage(selectedPage);
	});

	// 이전 페이지 버튼 클릭 이벤트
	$(document).on('click', '.page-link.prepage', function(e) {
		e.preventDefault();
		loadPage(currentPage - 1);
	});

	// 다음 페이지 버튼 클릭 이벤트
	$(document).on('click', '.page-link.nextpage', function(e) {
		e.preventDefault();
		loadPage(currentPage + 1);
	});

	// 메인 페이지
	function loadPage(page) {
		$.ajax({
			url: `/commu/ajax/${page}`,
			method: 'GET',
			dataType: 'json',
			cache: false,
			success: function(response) {
				updatePostList(response.commulist);
				updatePagination(response.nowPage, response.totalPageCount);
				currentPage = page; // 현재 페이지 업데이트
			},
			error: function(error) {
				console.error("Failed to load posts:", error);
			}
		});
	}

	// 카테고리 클릭 이벤트
$(document).on('click', '.cate', function() {
    const commuCateCode = $(this).data('maincate-value');
    loadCategoryPosts(commuCateCode, 1); // 페이지를 1페이지로 초기화하여 로드
});

	// "All" 카테고리 선택
	$(document).on('click', '.all-cate', function() {
		loadPage(1); // 페이지 초기화하여 첫 번째 페이지를 로드
	});

	function loadCategoryPosts(commuCateCode, page) {
		 $.ajax({
        url: `/commu/commucatecode/${commuCateCode}?page=${page}`,
        type: 'GET',
        dataType: 'json',
        success: function(response) {
            updatePostList(response.posts);
            // 클라이언트 측에서 페이지 번호 계산 및 업데이트
            updatePagination(page, Math.ceil(response.posts.length / 10));
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
        if (i === nowPage) {
            paginationHtml += `<li class="page-item active"><a class="page-link selpage" data-selpage="${i}">${i}</a></li>`;
        } else {
            paginationHtml += `<li class="page-item"><a class="page-link selpage" data-selpage="${i}">${i}</a></li>`;
        }
    }

    $('.pagination').html(paginationHtml);
}

	function updatePostList(posts) {
		if (!Array.isArray(posts)) {
			console.error('Provided posts data is not an array:', posts);
			return; // Exit the function early
		}

		

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

		// 이전에 표시된 게시물 목록을 지우고 새로운 목록으로 대체합니다.
		$('tbody').empty().append(postListHtml);
	}

	

});
