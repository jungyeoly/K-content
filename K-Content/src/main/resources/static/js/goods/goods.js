clickGoodsList = [];

function selectEnd(){
    console.log(clickGoodsList);
    const openerWindow = window.opener;
    console.log(openerWindow);
    window.opener.postMessage(clickGoodsList, "*");
    window.close();
}

function addList(goodsId) {
    if (clickGoodsList.includes(goodsId)) {
        clickGoodsList.pop(goodsId);
    } else {
        clickGoodsList.push(goodsId);
    }
}


function searchKeyword() {
    console.log("검색 시작");
    var requestData = {
        search: document.getElementById("search").value
    };
    console.log(requestData);
    $.ajax({
        url: '/cs/goods/search',
        type: 'GET',
        data: requestData,
        success: function (data) {
            const element = document.getElementById('goodsPad');
            element.innerHTML = "";
            for (var i = 0; i < data.length; i++) {
                inHtml = `<div class="col-xl-4 col-lg-6" >
                        <div class="single-category mb-30"  onclick="addList(${data[i].goodsId})">
                            <div class="category-img">

                                <img style="width: 400px; height: 300px" src="/img/goods/${data[i].goodsFileName}"  alt="">
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

$(document).ready(function () {

    $.ajax({
        url: '/cs/goods/list',
        type: 'GET',
        success: function (data) {
            const element = document.getElementById('goodsPad');

            for (var i = 0; i < data.length; i++) {
                inHtml = `<div class="col-xl-3 col-lg-6" >
                        <div class="single-category mb-30" onclick="addList(${data[i].goodsId})">
                            <div class="category-img">

                                <img style="width: 200px; height: 130px; margin:auto; display: block" src="/img/goods/${data[i].goodsFileName}" alt="">

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
