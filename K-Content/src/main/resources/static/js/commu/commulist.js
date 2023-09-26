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
        let isSelected = i === currentPage ? 'selected' : '';
        paginationHtml += `<div class="page-btn ${isSelected}"><a href="/commu?currentPage=${i}" data-page="${i}">${i}</a></div>`;
    }
    $('.pagination').html(paginationHtml);
}