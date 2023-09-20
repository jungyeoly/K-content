// 유튜브 iframe 가져오기
$(document).ready(function () {
    var cntntURL = document.getElementById("contentURL").value;
    console.log("cntntURL" + cntntURL);
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

// 키워드 삭제
function delKeyword(key) {
    const div = document.getElementById(key);
    div.remove();
}

// 키워드 생성
function makeKeyword() {
    word = document.getElementById("inputKeyword").value;

    if (word == null || word == '') {
        alert("키워드를 입력하세요!");
    } else {
        const thisDiv = document.getElementsByClassName('newKeyword')[0];
        innerHtml = `
          <div id="${word}">
                <button type="button" style="margin-left: 10px" class="btn btn-primary position-relative keywordButton">
                     ${word}
                </button>
                <button style="z-index: 10; margin-left: -10px"
                        class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                            key="${word}"
                        onclick="delKeyword(this.getAttribute('key'))">X
                </button>
        </div>`;

        thisDiv.insertAdjacentHTML("afterbegin", innerHtml);
        document.getElementById("inputKeyword").value = '';
    }
}

window.name = "goods_parent";

//굿즈 검색 팝업 생성
function goodsNewPage() {
    window.open("http://localhost:8080/cs/goods", "/cs/goods", "width=1200, height=800");
}

// 굿즈 검색 팝업에서 클릭한 상품 콘텐츠 생성 페이지로 보내기
const receiveMessage = async (e) => {
    var receivedData = e.data;
    selectGoods(receivedData)
}
window.addEventListener("message", receiveMessage, false);

function selectGoods(receivedData) {
    var str = "";
    for (i = 0; i < receivedData.length; i++) {
        if (receivedData.length == i + 1) {
            str += receivedData[i];
        } else {
            str += receivedData[i] + ",";
        }
    }
    /*    receivedData.map((data,index) => {
            console.log(data, index);
        })*/
    var requestData = {
        sendData: str
    };
    $.ajax({
        url: '/cs/goods/content-form',
        type: 'GET',
        data: requestData,
        success: function (data) {
            const element = document.getElementById('goodsList');

            for (var i = 0; i < data.length; i++) {
                goodsID = data[i].goodsId;
                inHtml = `<div id="${goodsID}" >
                    <a th:href="${data[i].goodsPurchsLink}">
                             <div class="card" style="width: 18rem; height: 240px">
                                <div style="width:18rem; height:140px">
                                <img style="width: 200px; height: 130px; margin:auto; display: block" src="/img/goods/${data[i].goodsFileId}" alt="">
                                </div>
                                <div style="width:18rem; height:100px;border:1px solid">
                                        <h5 class="text-center" >${data[i].goodsName}</h5>
                                        <p class="text-center" >${data[i].goodsPrice} 원</p>
                                        <p class="text-center" >${data[i].goodsBrand}</p>
                                </div>
                              <input type="hidden" class="goodsList" value="${goodsID}">
                              <button style="z-index: 10; margin-left: -10px"
                                    class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" 
                                    goodsID="${goodsID}"
                                    onclick="delGoods(this.getAttribute('goodsID'))">X
                              </button>
                             </div>
                </a>
                    </div>`;
                element.insertAdjacentHTML('beforeend', inHtml);
            }
        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}


//선택한 굿즈 삭제
function delGoods(goodsID) {
    const div = document.getElementById(goodsID);
    div.remove();

}

//컨텐츠 생성 폼 제출
function createContent() {
    var keywordDivList = [];
    var goodsDivList = [];
    var keywordDiv = document.getElementById("keywordList");
    var keywordDivCount = keywordDiv.getElementsByClassName("keywordButton");
    var goodsDiv = document.getElementById("goodsList");
    var goodsDivCount = goodsDiv.getElementsByClassName("goodsList");

    for (i = 0; i < keywordDivCount.length; i++) {
        var trimmedStr = keywordDivCount[i].textContent.replace(/^\s+|\s+$/g, "");
        keywordDivList.push(trimmedStr);
    }


    for (i = 0; i < goodsDivCount.length; i++) {
        goodsDivList.push(goodsDivCount[i].value);
    }

    if (document.getElementById("insertOrModify").value == '수정') {
        var sendData = {
            "cntntId": document.getElementById("cntntId").value,
            "is": document.getElementById("insertOrModify").value,
            "cntntUrl": document.getElementById("url").value,
            "cntntTitle": document.getElementById("title").value,
            "keywordList": keywordDivList,
            "goodsList": goodsDivList,
            "cntntCateCode": document.getElementById("category").value
        };
    } else if (document.getElementById("insertOrModify").value == '생성') {
        var sendData = {
            "is": document.getElementById("insertOrModify").value,
            "cntntUrl": document.getElementById("url").value,
            "cntntTitle": document.getElementById("title").value,
            "keywordList": keywordDivList,
            "goodsList": goodsDivList,
            "cntntCateCode": document.getElementById("category").value
        };
    }

    //콘텐츠 생성/수정
    $.ajax({
        url: '/cs/content',
        type: 'POST',
        data: JSON.stringify(sendData),
        contentType: 'application/json',
        success: function (data) {
            alert("컨텐츠가 등록되었습니다!")
        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}

function printIframe() {
    key = document.getElementById("url").value;
    var words = key.split('=');
    keyword = words[1];

    $.ajax({
        url: 'https://noembed.com/embed?url=https://www.youtube.com/watch?v=' + keyword,
        type: 'get',
        success: function (data) {
            let parseData = JSON.parse(data);
            document.getElementById("title").value = parseData.title;
            const element = document.getElementById('iframe');
            const inHtml = `<iframe width="560" height="315" src="https://www.youtube.com/embed/` + keyword + "\"" +
                `frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>`;
            element.innerHTML = inHtml

        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}

