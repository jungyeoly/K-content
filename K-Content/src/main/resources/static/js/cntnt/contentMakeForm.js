// 유튜브 iframe 가져오기
const cntntGoodsSet = new Set();
$(document).ready(function () {
    var cntntURL = document.getElementById("url").value;
    console.log("cntntURL" + cntntURL);

    $.ajax({
        url: '/cs/test/youtube/iframe',
        type: 'GET',
        data: {
            targetContentIdF: cntntURL
        }, // 데이터 객체 전달
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
    var inputWord = document.getElementById("inputKeyword").value;
    var refineWord = inputWord.toLowerCase();
    var word = refineWord.replace(/(\s*)/g, '');
    if (word == null || word == '') {

        Swal.fire({
            title: '공백 불가',
            text: '키워드를 입력하세요',
            icon: 'warning',
            confirmButtonText: '확인'
        }).then((result) => {
        });


    } else {
        const thisDiv = document.getElementsByClassName('newKeyword')[0];
        innerHtml = `
          <div id="${word}" style="margin-top: 20px;">
                <button type="button" style="margin-left: 10px;" class="btn btn-primary position-relative keywordButton">
                     ${word}
                </button>
                <button style="z-index: 10; margin-left: -10px"
                        class="position-absolute  translate-middle badge rounded-pill bg-danger"
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

    const setString = JSON.stringify(Array.from(cntntGoodsSet));
    console.log("setString: " + setString);
    const url = `/cs/test/goods/cntnt?goods=${encodeURIComponent(setString)}`;
    window.open(url, "/cs/test/goods/cntnt", "width=1200, height=800");

}

// 굿즈 검색 팝업에서 클릭한 상품 콘텐츠 생성 페이지로 보내기
window.addEventListener("message", receiveMessage, false);

function receiveMessage(event) {
    // event.data에 전송된 데이터가 포함됩니다.
    const receivedData = event.data;
    console.log(receivedData); // 수신된 데이터 확인
    selectGoods(receivedData)
}


function selectGoods(receivedData) {
    console.log("receivedData: " + receivedData.length);
    var str = "";
    for (i = 0; i < receivedData.length; i++) {
        if (receivedData.length == i + 1) {
            str += receivedData[i];
        } else {
            str += receivedData[i] + ",";
        }
    }
    console.log("str: " + str);
    // console.log("cntntGoodsSet: "+cntntGoodsSet.size);
    /*    receivedData.map((data,index) => {
            console.log(data, index);
        })*/
    var requestData = {
        sendData: str
    };
    // console.log("requestData: "+requestData);
    $.ajax({
        url: '/cs/test/goods/item',
        type: 'GET',
        data: requestData,
        success: function (data) {
            const element = document.getElementById('goodsList');
            element.innerHTML = '';
            for (var i = 0; i < data.length; i++) {
                goodsID = data[i].goodsId;
                inHtml = `<div id="${goodsID}" class="goodsItem" >
                    <a th:href="${data[i].goodsPurchsLink}">
                             <div class="card" style="width: 240px; height: 240px">
                                <div style="width: 240px; height: 260px">
                                <img style="width: 240px; height: 260px; margin:auto; display: block; border: 1px solid black;" src="/img/goods/${data[i].goodsFileId}" alt="">
                                </div>
                                <div style="width: 240px; height:120px;border:1px solid">
                                <div class="text-box" style="margin: 10px">
                                        <h5 class="text-center" >${data[i].goodsName}</h5>
                                        <p class="text-center" >${data[i].goodsPrice} 원</p>
                                        <p class="text-center" >${data[i].goodsBrand}</p>
                                        </div>
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
                // console.log("cntntGoodsSet: "+cntntGoodsSet);
                // if(cntntGoodsSet.has(goodsID)){
                //
                // }else{
                //     console.log("goodsID: "+goodsID);

                element.insertAdjacentHTML('beforeend', inHtml);
                // }

                // for (let i = 0; i < cntntGoodsSet.length; i++) {
                //     if (!cntntGoodsSet.has(goodsID)) {
                //         element.insertAdjacentHTML('beforeend', inHtml);
                //     }
                //
                // }
            }
        },
        error: function (error) {
            // console.log(cntntGoodsSet)
            console.error('에러 발생: ', error);
        }
    });

}


//선택한 굿즈 삭제
function delGoods(goodsID) {
    const div = document.getElementById(goodsID);
    div.remove();
    cntntGoodsSet.delete(goodsID);
}

window.addEventListener('load', () => {
    const forms = document.getElementsByClassName('validation-form');

    Array.prototype.filter.call(forms, (form) => {
        form.addEventListener('submit', function (event) {
            if (form.checkValidity() === false) {
                console.log("sdfsdf");
                event.preventDefault();
                event.stopPropagation();
            }

            form.classList.add('was-validated');
        }, false);
    });
}, false);

//컨텐츠 생성 폼 제출
function createContent() {
    event.preventDefault();
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

    if (keywordDivList.length == 0) {
        Swal.fire({
            title: '키워드를 입력해주세요',
            icon: 'warning',
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인',
            reverseButtons: true,

        }).then(result => {
            if (result.isConfirmed) {
                return;
            }
        });
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
    console.log(sendData)
    // 콘텐츠 생성/수정
    $.ajax({
        url: '/cs/test/content',
        type: 'POST',
        data: JSON.stringify(sendData),
        contentType: 'application/json',
        success: function (data) {
            console.log(data)
            if (data == '수정완료') {
                Swal.fire({
                    title: '콘텐츠 수정 완료',
                    text: '콘텐츠 수정이 완료되었습니다.',
                    icon: 'success',
                    confirmButtonText: '확인'
                }).then((result) => {
                    if (result.isConfirmed) {
                        location.href = '/cs/test';
                    }
                });
            } else if (data == '생성완료') {
                Swal.fire({
                    title: '콘텐츠 생성 완료',
                    text: '콘텐츠 생성이 완료되었습니다.',
                    icon: 'success',
                    confirmButtonText: '확인'
                }).then((result) => {
                    if (result.isConfirmed) {
                        location.href = '/cs/test';
                    }
                });
            }
        },
        error: function (error) {
            console.error('에러 발생: ', error);
            Swal.fire({
                title: '콘텐츠 생성 실패',
                text: '콘텐츠 생성이 생성이 실패했습니다.',
                icon: 'error',
                confirmButtonText: '확인'
            }).then((result) => {
                if (result.isConfirmed) {

                }
            });
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


const targetDiv = document.getElementById("goodsList");

const observer = new MutationObserver((mutationsList, observer) => {

// goodsListDiv의 자식 div 요소들을 모두 선택합니다.
    const childDivs = targetDiv.querySelectorAll(".goodsItem");
    cntntGoodsSet.clear();
    for (let i = 0; i < childDivs.length; i++) {
        const div = childDivs[i];
        const id = div.id;
        if (id) {
            cntntGoodsSet.add(id);
        }
    }

    console.log(cntntGoodsSet);
});


// MutationObserver가 관찰할 변경 유형과 대상 요소를 설정합니다.
const config = {attributes: true, childList: true, subtree: true};

// MutationObserver를 시작합니다.
observer.observe(targetDiv, config);

// 변경 사항을 멈추려면 다음과 같이 호출합니다.
// observer.disconnect();
