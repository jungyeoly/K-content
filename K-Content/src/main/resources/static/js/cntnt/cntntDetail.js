$(document).ready(function () {

    var requestData = {
        trendQueryList: document.getElementById("trendQueryList").value.slice(1, -1),
        cntntId: document.getElementById("cntntId").value
    };
    // 추천 콘텐츠
    $.ajax({
        url: '/cs/contentbykeyword', type: 'GET',
        data: requestData,
        success: function (contentList) {

            const element = document.getElementById('card-list');
            for (i = 0; i < contentList.length; i++) {
                console.log(contentList[i].cntntThumnail)
                console.log("키워드 기반 추천 콘텐츠" + contentList[i].cntntTitle);

                inHtml = `
            <li class="card-item" id="card-item" onclick="recomCntntDetail(${contentList[i].cntntId})">
                <figure class="card-image"style="background-image: url(${contentList[i].cntntThumnail})">
                    <img src=${contentList[i].cntntThumnail} alt="일분이">
                </figure>
                <div class="card-desc">
                   ${contentList[i].cntntTitle}
                </div>
            </li>`;
                element.insertAdjacentHTML("afterbegin", inHtml);

            }
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });

    // 키워드로 인스타 크롤링
    $.ajax({
        url: '/cs/instacrol', type: 'GET',
        data: requestData,
        success: function (data) {
            console.log(data);
            const element = document.getElementById('trend');
            data.slice(1, -1);
            data.slice(1, -1);
            element.innerHTML = data
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });


    //

    var requestData = {
        targetContentIdF: document.getElementById("contentId").value
    };

    $.ajax({
        url: '/cs/youtube/iframe', type: 'GET', data: requestData, // 데이터 객체 전달
        success: function (data2) {
            // 서버에서 받은 데이터를 result 요소에 추가합니다.
            const element = document.getElementById('iframe');
            const inHtml = `<iframe width="560" height="315" src="https://www.youtube.com/embed/` + data2 + "\"" + `frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>`;
            element.innerHTML = inHtml
            console.log(data2);
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });


});

function recomCntntDetail(cntntId) {
    const formHtml = `
                    <form id="contentDetail" action="/cs/contentdetail" method="get">
                        <input  id="targetContentIdF" name="targetContentIdF"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("targetContentIdF").value = cntntId;
    document.getElementById('contentDetail').submit();
}


function updateCntnt(cntntId)  {
    cntntId = document.getElementById('cntntId').value;
    // console.log(cntntId);
    const formHtml = `
                    <form id="updateCntnt" action="/cs/makecontent/update" method="get">
                        <input  id="targetContentIdF" name="targetContentIdF"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("targetContentIdF").value = cntntId;
    document.getElementById('updateCntnt').submit();
}

function deleteCntnt() {

}
