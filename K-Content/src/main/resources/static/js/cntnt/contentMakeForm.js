$(document).ready(function () {
    var cntntURL = document.getElementById("contentURL").value;
    var requestData = {
        targetContentIdF: cntntURL
    };

    $.ajax({
        url: '/cs/youtube/iframe',
        type: 'GET',
        data: requestData, // 데이터 객체 전달
        success: function (data2) {
            const element = document.getElementById('iframe');
            const inHtml = `<iframe width="560" height="315" src="https://www.youtube.com/embed/` + data2 + "\"" +
                `frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>`;
            element.innerHTML = inHtml
        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });


});

function delKeyword(key) {
    const div = document.getElementById(key);
    div.remove();
}

function makeKeyword() {
    word = document.getElementById("inputKeyword").value;

    const thisDiv = document.getElementsByClassName('keyword')[0];
    console.log(thisDiv);
    innerHtml = `
          <div id="${word}">
                <button type="button" style="margin-left: 10px" class="btn btn-primary position-relative keywordButton"
                        >
                     ${word}
                </button>
                <button style="z-index: 10; margin-left: -10px"
                        class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                            key="${word}"
                        onclick="delKeyword(this.getAttribute('key'))">X
                </button>
        </div>`;

    thisDiv.insertAdjacentHTML("afterbegin", innerHtml);
}

window.name = "goods_parent";

function goodsNewPage() {
    window.open("http://localhost:8083/cs/goods/", "/cs/goods/", "width=1200, height=800");
}

const receiveMessage = async (e) => {
    var receivedData = e.data;
    // 원하는 작업을 수행하거나 데이터를 출력

    // console.log("자식 창에서 받은 데이터:", receivedData);
    selectGoods(receivedData)
}
window.addEventListener("message", receiveMessage, false);

function selectGoods(receivedData) {
    var str = "";
    for( i=0; i<receivedData.length; i++){
        if(receivedData.length == i+1){
            str += receivedData[i];
        }else {
            str += receivedData[i] + ",";
        }
    }
    console.log(str);
    var requestData = {
        sendData: str
    };
    console.log(requestData);
    $.ajax({
        url: '/cs/goods/makecntntselectgoods',
        type: 'GET',
        data: requestData,
        success: function (data) {
            const element = document.getElementById('goods-box');

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
}

