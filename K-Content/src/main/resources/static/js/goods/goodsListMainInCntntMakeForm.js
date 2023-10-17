var section = document.getElementById('layout');
console.log(section);


function addBorder() {
    console.log("지금 리로드됨");
    console.log(clickGoodsList.length);
    for (var i = 0; i < clickGoodsList.length; i++) {
        // console.log("clickGoodsList: " + clickGoodsList[i]);

        var border = document.getElementById(clickGoodsList[i]);
        if (border != null) {
            console.log("border: " + border);
            border.classList.add('selected');
        } else {
            continue;
        }


    }
}


// section.addEventListener("change", function () {
//     console.log("지금 리로드됨");
//     console.log(clickGoodsList.length);
//     for (var i = 0; i < clickGoodsList.length; i++) {
//         var element = document.getElementById(clickGoodsList[i]);
//         element.classList.add('selected');
//     }
// })

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

var clickGoodsList = [];

function selectEnd() {

    if (clickGoodsList.length == 0) {
        alert("상품을 선택하지 않았습니다.")
    } else {
        const openerWindow = window.opener;
        window.opener.postMessage(clickGoodsList, "*");
        // console.log(clickGoodsList);
        window.close();
    }
}

function addList(goodsId) {
    if (clickGoodsList.includes(goodsId)) {
        clickGoodsList.pop(goodsId);
    } else {
        clickGoodsList.push(goodsId);
    }
    var element = document.getElementById(goodsId);
    if (element.classList.contains('selected')) {
        element.classList.remove('selected');
    } else {
        element.classList.add('selected');
    }
    console.log(clickGoodsList);
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
