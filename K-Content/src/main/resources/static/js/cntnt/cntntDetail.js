$(document).ready(function () {
    showLoadingOverlay();
    var requestData = {
        trendQueryList: document.getElementById("trendQueryList").value.slice(1, -1),
        cntntId: document.getElementById("cntntId").value
    };
    // 추천 콘텐츠
    $.ajax({
        url: '/cs/cntnt/content/keyword', type: 'GET',
        data: requestData,
        success: function (contentList) {
            const element = document.getElementById('card-list');
            if (contentList.length == 0) {
                inHtml = `
            <div><h5 style="margin-top: 60px"> 관련 컨텐츠가 존재하지 않습니다.</h5>
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
                }
            }


        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
    const element = document.getElementById('trend');
    $.ajax({
        url: '/cntnt/insta-img', type: 'GET',
        data: requestData,
        success: function (data2) {

            data2.slice(1, -1);
            data2.slice(1, -1);
            console.log(data2);
            element.innerHTML = data2
            hideLoadingOverlay();
        }, error: function (error) {
            hideLoadingOverlay();
            element.innerHTML = `<img src="/img/fail_cro.png" >`;

        }
    });

    var requestData = {
        targetContentIdF: document.getElementById("contentId").value
    };

    $.ajax({
        url: '/cs/cntnt/youtube/iframe', type: 'GET', data: requestData, // 데이터 객체 전달
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
function showLoadingOverlay() {
    console.log("보이시");
    document.getElementById('loadingOverlay').style.display = '';
    document.getElementById('loadingOverlay').style.display = 'block';
}

function hideLoadingOverlay() {
    console.log("지우기");
    document.getElementById('loadingOverlay').style.display = '';
    document.getElementById('loadingOverlay').style.display = 'none';
}
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
                    <form id="updateCntnt" action="/cs/cntnt/content/modify-form" method="get">
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
    Swal.fire({
        title: '삭제 여부',
        text: '컨텐츠를 삭제하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        confirmButtonColor: '#14dbc8'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/cs/cntnt/content', type: 'patch',
                data: {
                    cntntId: document.getElementById("cntntId").value
                }, // 데이터 객체 전달
                success: function (data2) {
                    Swal.fire({
                        title: '삭제 완료',
                        text: '컨텐츠 삭제가 완료 되었습니다.',
                        icon: 'success',
                        confirmButtonText: '확인',
                        confirmButtonColor: '#14dbc8'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.replace("/cs/cntnt");
                        }
                    });
                }, error: function (error) {
                    console.error('에러 발생: ', error);
                }
            });
        }
    });
}

// function deleteCntnt() {
//     if (confirm('컨텐츠를 삭제하시겠습니까?')) {
//         $.ajax({
//             url: '/cs/cntnt/content', type: 'patch',
//             data: {
//                 cntntId: document.getElementById("cntntId").value
//             }, // 데이터 객체 전달
//             success: function (data2) {
//                 if (confirm('삭제가 완료 되었습니다')) {
//                     window.location.replace("/cs/cntnt");
//                 } else {
//                     window.location.replace("/cs/cntnt");
//                 }
//             }, error: function (error) {
//                 console.error('에러 발생: ', error);
//             }
//         });
//     }
//
// }

//상품클릭시 이동할건지 물어봄
function moveGoods(goodsUrl) {
    Swal.fire({
        title: '이동 여부',
        text: '해당 상품 판매 사이트로 이동하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        confirmButtonColor: '#14dbc8'
    }).then((result) => {
        if (result.isConfirmed) {
            window.open(goodsUrl);
        }
    });
}
