$(document).ready(function () {
    var cntntURL = "[[${content.getCntntUrl()}]]";
    console.log(cntntURL);
    // 여기는 이거 말고 url 을 받아갈 수 있어 url 받아가는 거로 다시 해봐봐보바봐
    var requestData = {
        targetContentIdF: cntntURL
    };

    // /cs/instaimg URL로 GET 요청을 보냅니다.
    $.ajax({
        url: '/cs/youtube/iframe',
        type: 'GET',
        data: requestData, // 데이터 객체 전달
        success: function (data2) {
            const element = document.getElementById('iframe');
            const inHtml = `<iframe width="560" height="315" src="https://www.youtube.com/embed/` + data2 + "\"" +
                `frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>`;
            element.innerHTML = inHtml
            console.log(data2);
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

function goodsNewPage() {
    window.open("http://localhost:8083/cs/goods/", "/cs/goods/", "width=1200, height=800");
}

clickGoodsList = [];
console.log(clickGoodsList);

