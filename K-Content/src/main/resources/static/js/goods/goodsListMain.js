$(document).ready(function () {

    $.ajax({
        url: '/cs/goods/list',
        type: 'GET',
        success: function (data) {
            const element = document.getElementById('card-list');

            for (var i = 0; i < data.length; i++) {
                inHtml = `<div class="col-xl-3 col-lg-6" >
                        <div class="single-category mb-30" onclick="detail(${data[i].goodsId})">
                            <div class="category-img">

                                <img style="width: 200px; height: 130px; margin:auto; display: block" src="/img/goods/${data[i].goodsFileId}" alt="">

                                <div class="category-caption">
                                    <h6 style="text-align: center; " >${data[i].goodsName}</h6>
                                </div>
                            </div>
                        </div>
                    </div>`;
                element.insertAdjacentHTML('beforeend', inHtml);
            }
        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
});

function searchKeyword() {
    var requestData = {
        search: document.getElementById("search-input").value
    };
    $.ajax({
        url: '/cs/goods/search',
        type: 'GET',
        data: requestData,
        success: function (data) {
            console.log(data.length)
            const element = document.getElementById('card-list');
            element.innerHTML = "";
            for (var i = 0; i < data.length; i++) {
                inHtml = `<div class="col-xl-4 col-lg-6">
                        <div class="single-category mb-30"  onclick="detail(${data[i].goodsId})">
                            <div class="category-img">
                                <img style="width: 400px; height: 300px" src="/img/goods/${data[i].goodsFileId}"  alt="">
                                <div class="category-caption">
                                    <h6 style="text-align: center; " >${data[i].goodsName}</h6>
                                </div>
                            </div>
                        </div>
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
