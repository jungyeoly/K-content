$(document).ready(function () {
    var requestData = {
        trendQueryList: document.getElementById("trendQueryList").value.slice(1, -1),
        cntntId: document.getElementById("cntntId").value
    };
    // 추천 콘텐츠
    $.ajax({
        url: '/cs/test/content/keyword', type: 'GET',
        data: requestData,
        success: function (contentList) {
            const element = document.getElementById('card-list');
            if (contentList.length == 0) {
                inHtml = `
            <div> 관련 컨텐츠가 존제히지 않습니다.
            </div>
                        `;
                element.insertAdjacentHTML("afterbegin", inHtml);
            } else {
                for (i = 0; i < contentList.length; i++) {
                    inHtml = `
            <li class="rel-box m-3" onclick="recomCntntDetail(${contentList[i].cntntId})">
                <figure class="rel-image" style="background-image: url(${contentList[i].cntntThumnail})">
                    <img src=${contentList[i].cntntThumnail} alt="일분이" style="display: none">
                </figure>
                <div class="rel-title">
                    ${contentList[i].cntntTitle}
                </div>
            </li> `;
                    element.insertAdjacentHTML("afterbegin", inHtml);
                    //밑에 html 코드도 위에 붙여본겁니다. 안예쁩니다.
                    // <li className="goods-box card-item" id="card-item" style="margin-bottom: 70px"
                    //     onClick="recomCntntDetail(${contentList[i].cntntId})">
                    //     <div className="card " style="width: 18rem; height: 240px">
                    //         <figure className="card-image" style=" background-image: url(${contentList[i].cntntThumnail})">
                    //             <img className="card-img-top" src=${contentList[i].cntntThumnail} alt="일분이"
                    //                  style="display: none">
                    //         </figure>
                    //         <div className="card-desc" style="width:18rem; border:1px solid">
                    //             ${contentList[i].cntntTitle}
                    //         </div>
                    //     </div>
                    // </li>
                }
            }


        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });

    $.ajax({
        url: '/cs/test/insta-img', type: 'GET',
        data: requestData,
        success: function (data2) {
            const element = document.getElementById('trend');
            data2.slice(1, -1);
            data2.slice(1, -1);
            console.log(data2);
            element.innerHTML = data2
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });

    var requestData = {
        targetContentIdF: document.getElementById("contentId").value
    };

    $.ajax({
        url: '/cs/test/youtube/iframe', type: 'GET', data: requestData, // 데이터 객체 전달
        success: function (data2) {
            // 서버에서 받은 데이터를 result 요소에 추가합니다.
            const element = document.getElementById('iframe');
            const inHtml = `<iframe width="560" height="315" src="https://www.youtube.com/embed/` + data2 + "\"" + `frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>`;
            element.innerHTML = inHtml
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });

});

//콘텐츠 상세 화면 출력
function recomCntntDetail(cntntId) {
    const formHtml = `
                    <form id="contentDetail" action="/user/content/detail" method="get">
                        <input  id="targetContentIdF" name="targetContentIdF"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("targetContentIdF").value = cntntId;
    document.getElementById('contentDetail').submit();
}

// 콘텐츠 수정 화면 요청
function updateCntnt(cntntId) {
    cntntId = document.getElementById('cntntId').value;
    // console.log(cntntId);
    const formHtml = `
                    <form id="updateCntnt" action="/cs/test/content/modify-form" method="get">
                        <input  id="targetContentIdF" name="targetContentIdF"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("targetContentIdF").value = cntntId;
    document.getElementById('updateCntnt').submit();
}

//콘텐츠 삭제 처리
function deleteCntnt() {
    if (confirm('컨텐츠를 삭제하시겠습니까?')) {
        $.ajax({
            url: '/cs/test/content', type: 'patch',
            data: {
                cntntId: document.getElementById("cntntId").value
            }, // 데이터 객체 전달
            success: function (data2) {
                if (confirm('삭제가 완료 되었습니다')) {
                    window.location.replace("/cs/test");
                } else {
                    window.location.replace("/cs/test");
                }
            }, error: function (error) {
                console.error('에러 발생: ', error);
            }
        });
    }

}
