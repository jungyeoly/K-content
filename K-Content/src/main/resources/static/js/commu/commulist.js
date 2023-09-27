// 페이지 번호 클릭 이벤트
$(document).on('click', '.page-btn a', function(e) {
    e.preventDefault();  // 기존의 페이지 이동을 막음

    const selectedPage = $(this).data('page');
    currentPage = selectedPage; // 선택한 페이지 번호를 'currentPage' 변수에 할당
    fetchPostsForPage(selectedPage);
});

let commuCateCode = null; // 현재 선택된 카테고리를 저장할 전역 변수
function fetchPostsForPage(page) {
	let url = `/commu/data?page=${page}`;
	if (commuCateCode) {
		 url =  `/commu/commucatecode/${commuCateCode}?page=${page}`,
	
    $.ajax({
        url: url, // 변경된 엔드포인트
        type: 'GET',
        dataType: 'json',
        success: function(response) {
            updatePostList(response.commulist || response.posts); // 엔드포인트에서 반환되는 JSON 키에 맞게 수정해야 할 수도 있습니다.
            updatePagination(response.totalPages, page);
        },
        error: function(error) {
            console.error("Error fetching posts:", error);
        }
    });
}
}




let currentPage = 1;  // 현재 페이지 초기값 설정

$(document).on('click', '.cate', function() {
    const commuCateCode = $(this).data('maincate-value');
    
    $.ajax({
        url: `/commu/commucatecode/${commuCateCode}?page=${currentPage}`,
        type: 'GET',
        dataType: 'json',
        success: function(response) {
            updatePostList(response.posts);
            updatePagination(response.totalPages);
        },
        error: function(error) {
            console.error("Error fetching posts by category:", error);
        }
    });
});

function updatePostList(posts) {
	
    if (!Array.isArray(posts)) {
       console.error('Provided posts data is not an array:', posts);
        return; // Exit the function early
    }

	console.log(posts)

    let postListHtml = '';
    posts.forEach(commu => {
        postListHtml += `
            <tr class="commu-row" data-commu-id="${commu.commuId}">
                <td>${commu.commuId}</td>
                <td>${commu.commonCodeVal}</td> 
                <td><a href="/commu/${commu.commuId}">${commu.commuTitle}</a></td>
                <td>${commu.commuMberId}</td>
                <td>${commu.commuReadCnt}</td>
                <td>${commu.commuRegistDate}</td>
            </tr>
        `;
    });

    $('tbody').html(postListHtml);
}

function updatePagination(totalPages, currentPage) {
    let paginationHtml = '';
     for(let i=1; i<=totalPages; i++) {
        let isSelected = i === Number(currentPage) ? 'selected' : ''; // 문자열을 숫자로 변환하여 비교
        paginationHtml += `<div class="page-btn ${isSelected}"><a href="/commu?page=${i}" data-page="${i}">${i}</a></div>`;
    }
    $('.pagination').html(paginationHtml);
}