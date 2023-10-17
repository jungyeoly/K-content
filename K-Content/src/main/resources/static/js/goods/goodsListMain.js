function searchKeyword() {
    $.ajax({
        url: '/cs/test/goods/search',
        type: 'GET',
        data: {
            search: document.getElementById("search-input").value
        },
        success: function (data) {
            const element = document.getElementById('card-list');
            element.innerHTML = "";
            if (data.length != 0) {
                for (var i = 0; i < data.length; i++) {
                    inHtml = `
                        <div id="goodsBox" className="border"> 
                                <div class="single-category mb-30" onclick="detail(${data[i].goodsId})">
                                    <div class="category-img">
                                        <img style="width: 300px; height: 400px;margin:auto; display: block"" src="/img/goods/${data[i].goodsFileId}"
                                             alt="">
                                            <div class="category-caption p-2">
                                                <h6 style="text-align: center; ">${data[i].goodsName}</h6>
                                            </div>
                                    </div>
                                </div>
                        </div>
                `;
                    element.insertAdjacentHTML('beforeend', inHtml);
                }
            } else {
                inHtml = ` <div class="container text-center mt-5">
            <img alt="" src="/img/fail_goods.png">
        </div>`;
                element.insertAdjacentHTML('beforeend', inHtml);
            }


        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}

function detail(goodsId) {
    // 상품 상세 화면 보여주기
    const formHtml = `
                    <form id="goodsDetail" action="/cs/test/goods/detail" method="get">
                        <input  id="goodsId" name="goodsId"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("goodsId").value = goodsId;
    document.getElementById('goodsDetail').submit();
}

function selPageF(pageNum) {

    $.ajax({
        url: "/cs/test/goods/" + pageNum,
        method: "get",
        success: function (data) {

            const element = document.getElementById('layout');
            element.innerHTML = "";
            element.insertAdjacentHTML('beforeend', data);
        }
    })
}

function prePageF(pageNum) {
    var prePage = pageNum - 1;
    $.ajax({
        url: "/cs/test/goods/" + prePage,
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
            url: "/cs/test/goods/" + nexPage,
            method: "get",
            seccess: function (data) {
                const element = document.getElementById('layout');
                element.innerHTML = "";
                element.insertAdjacentHTML('beforeend', data);
            }
        })
    }
)
