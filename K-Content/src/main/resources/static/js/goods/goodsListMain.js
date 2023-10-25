function searchKeyword() {
    const firstPage = 1;
    $.ajax({
        url: '/cs/cntnt/goods/search',
        type: 'GET',
        data: {
            search: document.getElementById("search-input").value,
            page: firstPage
        },
        success: function (data) {

            const element = document.getElementById('card-list');
            element.innerHTML = "";
            const elementPage = document.getElementById('page-nav');
            elementPage.innerHTML = "";

            if (data[0].length != 0) {
                for (var i = 0; i < data[0].length; i++) {
                    inHtml = `
                        <div id="goodsBox" className="border"> 
                            <div class="single-category mb-30" onclick="detail(${data[0][i].goodsId})">
                                <div class="category-img">
                                    <img style="width: 300px; height: 400px;margin:auto; display: block"" src="/img/goods/${data[0][i].goodsFileId}"
                                        alt="">
                                    <div class="category-caption p-2">
                                        <h6 style="text-align: center; ">${data[0][i].goodsName}</h6>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `;
                    element.insertAdjacentHTML('beforeend', inHtml);
                }
            } else {
                inHtml = ` <div class="container text-center mt-5">
                    <img alt="" src="/img/fail_goods_admin.png">
                </div>`;
                element.insertAdjacentHTML('beforeend', inHtml);
            }

            var startPage = data[1].startPage;
            var endPage = data[1].endPage;
            var htmlString = '';
            for (var i = startPage; i <= endPage; i++) {
                htmlString += '<li class="page-item' + (i === data[1].nowPage ? ' active' : '') + '"><a class="page-link selpage" id="selpage" pageNum="' + i + '" onclick=selPageF(' + i + ') data-selpage="' + i + '">' + i + '</a></li>';
            }

            if (data[1].nowPageBlock > 1) {
                htmlString = '<li class="page-item"><a class="page-link prepage" id="prepage" onclick="prePageF()" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>' + htmlString;
            }

            if (data[1].nowPageBlock < data[1].totalPageBlock) {
                htmlString += '<li class="page-item"><a class="page-link nexpage" id="nexpage" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>';
            }

            var paginationList = document.createElement('ul');
            paginationList.setAttribute('class', 'pagination');
            paginationList.setAttribute('id', 'paginationList');
            paginationList.innerHTML = htmlString;
            elementPage.appendChild(paginationList);

        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}


function detail(goodsId) {
    // 상품 상세 화면 보여주기
    const formHtml = `
                    <form id="goodsDetail" action="/cs/cntnt/goods/detail" method="get">
                        <input  id="goodsId" name="goodsId"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("goodsId").value = goodsId;
    document.getElementById('goodsDetail').submit();
}

function selPageF(pageNum) {
    console.log("pageNum: " + pageNum);

    $.ajax({
        url: "/cs/cntnt/goods/search",
        type: 'GET',
        data: {
            search: document.getElementById("search-input").value,
            page: pageNum
        },
        success: function (data) {
            const element = document.getElementById('card-list');
            element.innerHTML = "";
            const elementPage = document.getElementById('page-nav');
            elementPage.innerHTML = "";
            if (data[0].length != 0) {
                for (var i = 0; i < data[0].length; i++) {
                    inHtml = `
                        <div id="goodsBox" className="border"> 
                            <div class="single-category mb-30" onclick="detail(${data[0][i].goodsId})">
                                <div class="category-img">
                                    <img style="width: 300px; height: 400px;margin:auto; display: block"" src="/img/goods/${data[0][i].goodsFileId}"
                                        alt="">
                                    <div class="category-caption p-2">
                                        <h6 style="text-align: center; ">${data[0][i].goodsName}</h6>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `;
                    element.insertAdjacentHTML('beforeend', inHtml);
                }
            } else {
                inHtml = ` <div class="container text-center mt-5">
                    <img alt="" src="/img/fail_goods_admin.png">
                </div>`;
                element.insertAdjacentHTML('beforeend', inHtml);
            }
            var startPage = data[1].startPage;
            var endPage = data[1].endPage;
            var htmlString = '';
            for (var i = startPage; i <= endPage; i++) {
                htmlString += '<li class="page-item' + (i === data[1].nowPage ? ' active' : '') + '"><a class="page-link selpage" id="selpage" pageNum="' + i + '" onclick=selPageF(' + i + ') data-selpage="' + i + '">' + i + '</a></li>';
            }

            if (data[1].nowPageBlock > 1) {
                htmlString = '<li class="page-item"><a class="page-link prepage" id="prepage" onclick="prePageF()" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>' + htmlString;
            }

            if (data[1].nowPageBlock < data[1].totalPageBlock) {
                htmlString += '<li class="page-item"><a class="page-link nexpage" id="nexpage" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>';
            }

            var paginationList = document.createElement('ul');
            paginationList.setAttribute('class', 'pagination');
            paginationList.setAttribute('id', 'paginationList');
            paginationList.innerHTML = htmlString;
            elementPage.appendChild(paginationList);

        }
    })
}

function prePageF(pageNum) {
    var prePage = pageNum - 1;
    $.ajax({
        url: "/cs/cntnt/goods/" + prePage,
        method: "get",
        success: function (data) {

            const element = document.getElementById('layout');
            element.innerHTML = "";
            element.insertAdjacentHTML('beforeend', data);
        }
    })
}


$(".nexpage").click(function () {
        var nexPage = $(this).data("nexpage") + 1;

        $.ajax({
            url: "/cs/cntnt/goods/" + nexPage,
            method: "get",
            seccess: function (data) {
                const element = document.getElementById('layout');
                element.innerHTML = "";
                element.insertAdjacentHTML('beforeend', data);
            }
        })
    }
)
