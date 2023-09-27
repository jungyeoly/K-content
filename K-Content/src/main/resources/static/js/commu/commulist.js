// 페이지 번호 클릭 이벤트
$(document).on('click', '.page-link', function(e) {
    e.preventDefault();  // 기존의 페이지 이동을 막음

    const selectedPage = $(this).data('selpage');  // "selpage" 데이터 속성을 사용
    fetchPostsForPage(selectedPage);
});

let commuCateCode = null; // 현재 선택된 카테고리를 저장할 전역 변수
function fetchPostsForPage(page) {
  let url = `/commu/commucatecode/${commuCateCode}?page=${page}`;


    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success: function(response) {
            updatePostList(response.commulist || response.posts);
            updatePagination(response.totalPages, page);
        },
        error: function(error) {
            console.error("Error fetching posts:", error);
        }
    });
}





let currentPage = 1;  // 현재 페이지 초기값 설정
var page = $('page-link selpage').prop("data-selpage");
$(document).on('click', '.cate', function() {
    const commuCateCode = $(this).data('maincate-value');
    
    $.ajax({
        url: `/commu/commucatecode/${commuCateCode}?page=${page}`,
        type: 'GET',
        //dataType: 'json',
		dataType: {
			commuCateCode:commuCateCode // 좌:우 기준으로 좌==컨트롤러에서 받을 이름, 우==ajax를 요청할 때 컨트롤러에 넘겨줄 값
			,page:page
		},
        success: function(response) {
			var postList = response.posts//<<< posts라는 이름으로 넣었던 오브젝트는?? List<Commu>
			//게시글 테이블 생성패서 뿌려주면 됨
            updatePostList(postList);
            updatePagination(response.totalPageCount);
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