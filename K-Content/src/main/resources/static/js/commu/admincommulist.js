
$(document).ready(function() {
	let currentPage = 1; // 페이지 초기화
	let loading = false; // 페이지 로딩 중 여부를 나타내는 플래그
	let commuCateCode = null;  // 현재 선택된 카테고리 저장 변수
	let currentKeyWord = null; // 현재 선택 검색 상태 저장 변수


	loadPage(currentPage);

// 검색 폼 제출 이벤트 리스너 추가
$('#searchForm').submit(function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작을 막음
    let keyword = $('#searchInput').val();

    if (keyword.trim() !== "") { // 검색어가 비어있지 않은 경우
		currentKeyWord = keyword; //검색 상태 저장        
searchPosts(keyword, 1); // 첫 페이지부터 검색 결과를 보여줌
        $('#searchInput').val(''); // 검색창의 내용을 비움
    } else {
	currentKeyWord = null; //검색어가 비어있으면 검색 상태 해제
	}
});

function searchPosts(keyword, page) {
    $.ajax({
        url: `/cms/commu/search/${page}`,
        type: 'GET',
        data: {
            keyword: keyword
        },
        dataType: 'json',
        success: function(response) {
            updatePostList(response.commuList); // 기존의 게시글 목록 업데이트 함수를 사용하여 검색 결과를 화면에 표시
            updatePagination(response.nowPage, response.totalPageCount); // 페이징 업데이트
            currentPage = page;
        },
        error: function(error) {
            console.error("Error fetching search results:", error);
        }
    });
}

	// 페이지 번호 클릭 이벤트
	$(document).on('click', '.page-link', function(e) {
		e.preventDefault();
		if (!loading) {
			const selectedPage = $(this).data('page');
			if (selectedPage !== currentPage) {
				 // 검색 상태인 경우
            if (currentKeyWord) {
                searchPosts(currentKeyWord, selectedPage);
            } 
				 // 카테고리 상태인 경우
            else if (commuCateCode) {
                loadCategoryPosts(commuCateCode, selectedPage);
            } 
            // 그 외 (일반 상태)
            else {
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


/// 메인 페이지
function loadPage(page) {
    if (typeof page === 'undefined' || !page) {
        page = 1;
    }
  
    let requestURL = `/cms/commu/ajax/${page}`;

    loading = true; // 페이지 로딩 중 플래그 설정
    $.ajax({
        url: requestURL,
        method: 'GET',
        dataType: 'json',
        cache: false,
        success: function(response) {
            updatePostList(response.commulist);

			console.log("test: " + response.commulist[0]);

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
		$(".cate").removeClass("active");
		$(this).addClass("active");
		commuCateCode = $(this).data('maincate-value');
		loadCategoryPosts(commuCateCode, 1); // 페이지를 1페이지로 초기화하여 로드
	});

	// 카테고리 클릭 이벤트
	function loadCategoryPosts(commuCateCode, page) {
		switch (commuCateCode) {
			case '전체':
			    commuCateCode = 'All';
			    break;
			case '음악':
			    commuCateCode = 'POP';
			    break;
			case '연예인':
			    commuCateCode = 'Celebrity';
			    break;
			case '음식':
			    commuCateCode = 'Food';
			    break;
			case '영화':
			    commuCateCode = 'Movie';
			    break;
			case '스포츠':
			    commuCateCode = 'Sports';
			    break;
			case '패션':
			    commuCateCode = 'Fashion';
			    break;
			case '미용':
			    commuCateCode = 'Beauty';
			    break;
			case '드라마':
			    commuCateCode = 'Drama';
			    break;
			case '여행':
			    commuCateCode = 'Travel';
			    break;
			case '게임':
			    commuCateCode = 'Game';
			    break;
		  default:
		    commuCateCode = 'All';
		}

		$.ajax({
			url: `/cms/commu/commucatecode/${commuCateCode}?page=${page}`,
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
                    <td><a href="/cms/commu/detail/${commu.commuId}">${commu.commuTitle}</a></td>
                    <td>${commu.commuMberId}</td>
                    <td>${commu.commuReadCnt}</td>
                    <td>${commu.commuRegistDate}</td>
                </tr>
            `;
		});
		$('tbody').empty().append(postListHtml);
	}
});
