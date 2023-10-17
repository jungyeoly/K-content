var section = document.getElementById('layout');
let clickGoodsSet = new Set();

console.log("돌고있다");
const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);
const parameterValue = urlParams.get('goods');
const selectedGoodsSet = parameterValue ? new Set(JSON.parse(decodeURIComponent(parameterValue))) : new Set();

for (let item of selectedGoodsSet) {
    clickGoodsSet.add(item);
    console.log(document.getElementsByClassName("container"));
}
addBorder();

function addBorder() {

    for (let item of clickGoodsSet) {
        let border = document.getElementById(item);
        if (border) {
            border.classList.add('selected');
        }
    }
}

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
                    var inHtml = `<div class="col-xl-4 col-lg-6">
                        <div class="single-category mb-30"  onclick="addList(${data[i].goodsId})">
                            <div class="category-img"  style="width: 300px; height: 400px">
                                <img style="width: 300px; height: 400px" src="/img/goods/${data[i].goodsFileId}"  alt="">
                                <div class="category-caption">
                                    <h6 style="text-align: center; " >${data[i].goodsName}</h6>
                                </div>
                            </div>
                        </div>
                    </div>`;
                    element.insertAdjacentHTML('beforeend', inHtml);
                }
            } else {
                inHtml = `<div>
                        <h5>관련 상품이 존재하지 않습니다.</h5>
                    </div>`;
                element.insertAdjacentHTML('beforeend', inHtml);
            }
        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}


function selectEnd() {

    if (clickGoodsSet.size == 0) {
        alert("상품을 선택하지 않았습니다.")
    } else {

        const selectedGoodsArray = Array.from(clickGoodsSet);

        for (let i = 0; i < selectedGoodsArray.length; i++) {

            console.log(selectedGoodsArray[i]);
        }
        window.opener.postMessage(selectedGoodsArray, "*");

        window.close();
    }
}

function addList(goodsId) {
    var element = document.getElementById(goodsId);
    if (clickGoodsSet.has(goodsId)) {
        clickGoodsSet.delete(goodsId);
        element.classList.remove('selected');
    } else {
        clickGoodsSet.add(goodsId);
        element.classList.add('selected');
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
            addBorder();
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
            addBorder();
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
            addBorder();
        }
    })
})
