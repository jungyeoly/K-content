function searchKeyword() {
    $.ajax({
        url: '/cs/test/goods/cntnt/search',
        type: 'GET',
        data: {
            search: document.getElementById("search-input").value
        },
        success: function (data) {
            const element = document.getElementById('card-list');
            element.innerHTML = "";
            if (data.length != 0) {
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
            } else {
                inHtml = `<div class="col-xl-4 col-lg-6">
                        <h3>관련 상품이 존재하지 않습니다.</h3>
                    </div>`;
                element.insertAdjacentHTML('beforeend', inHtml);
            }


        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });

}

clickGoodsList = [];

function selectEnd(){
    const openerWindow = window.opener;
    window.opener.postMessage(clickGoodsList, "*");
    // console.log(clickGoodsList);
    window.close();
}

function addList(goodsId) {
    if (clickGoodsList.includes(goodsId)) {
        clickGoodsList.pop(goodsId);
    } else {
        clickGoodsList.push(goodsId);
    }
}
function detail(goodsId) {
    console.log("ehfrhdlTek");
    // 상품 상세 화면 보여주기
    // 상품 담아가
    // const formHtml = `
    //                 <form id="goodsDetail" action="/cs/test/goods/detail" method="get">
    //                     <input  id="goodsId" name="goodsId"  />
    //                 </form>`;
    //
    // const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    // const form = doc.body.firstChild;
    // document.body.append(form);
    // document.getElementById("goodsId").value = goodsId;
    // document.getElementById('goodsDetail').submit();
}

function selPageF(pageNum) {

    $.ajax({
        url: "/cs/test/goods/cntnt/" + pageNum,
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
        url: "/cs/test/goods/cntnt/" + prePage,
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
        url: "/cs/test/goods/cntnt/" + nexPage,
        method: "get",
        seccess: function (data) {
            const element = document.getElementById('layout');
            element.innerHTML = "";
            element.insertAdjacentHTML('beforeend', data);
        }
    })
})
