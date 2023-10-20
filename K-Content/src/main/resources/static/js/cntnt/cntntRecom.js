$(document).ready(function () {
    $.ajax({
        url: '/cs/test/youtube/keyword', type: 'GET',
        success: function (contentList) {
            const element = document.getElementById('card-list');
            element.innerHTML = '';
            for (i = 0; i < contentList.length; i++) {
                // var str = '';
                // arr = Array.from(contentList[i].title);

                // var replacedString = contentList[i].title.replace(/&quot;/g, '\\"');
                singleReplaceString = contentList[i].title.replace(/&#39;/g, "\\'");

                // arr.map((data, idx) => {
                //     console.log("data:" + data);
                //     if (data == "'") {
                //         str = str + "&quot;";
                //     } else if (data == '"') {
                //         str = str + "&quot;";
                //     } else {
                //         str = str + data;
                //     }
                // })
                // Ep 4 &quot;The Golden Trio (Plus One)&quot; - Overthinking with Kat &amp; June
                // &#39;뜨거운 타격감&#39; 최지만 현지해설 &quot;최지만에겐 슬라이더가 위협이 되지 않죠&quot; #SPORTSTIME
                //
                inHtml = `
            <li class="card-item" id="card-item" onclick="cntntMake( '${singleReplaceString}' , '${contentList[i].url}' )">
                <figure class="card-image"style="background-image: url(${contentList[i].thumbnail})">
                    <img src=${contentList[i].thumbnail} >
                </figure>
                <div class="card-desc">
                   ${contentList[i].title}
                </div>
            </li>`;
                element.insertAdjacentHTML("afterbegin", inHtml);
            }

        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
})

//콘텐츠 생성 페이지 form 유튜브
function cntntMake(spaArr, url) {
    const formHtml = `
                    <form id="contentMake" action="/cs/test/content-form" method="get">
                        <input  id="cntntURL" name="cntntURL"  />
                        <input  id="cntntTitle" name="cntntTitle"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("cntntURL").value = url;
    document.getElementById("cntntTitle").value = spaArr;
    document.getElementById('contentMake').submit();
}
function handleKeyDown(event) {
    if (event.key === 'Enter') {
        searchButton();
    }
}
function searchButton() {
    $.ajax({
        url: '/cs/test/youtube/keyword', type: 'GET',
        data: {
            searchKeyword: document.getElementById('search-input').value
        },
        success: function (contentList) {
            const element = document.getElementById('card-list');
            element.innerHTML = '';
            for (i = 0; i < contentList.length; i++) {
                singleReplaceString = contentList[i].title.replace(/&#39;/g, "\\'");

                inHtml = `
            <li class="card-item" id="card-item" onclick="cntntMake( ' ${singleReplaceString}' , '${contentList[i].url}' )">
                <figure class="card-image"style="background-image: url(${contentList[i].thumbnail})">
                    <img src=${contentList[i].thumbnail} >
                </figure>
                <div class="card-desc">
                   ${contentList[i].title}
                </div>
            </li>`;
                element.insertAdjacentHTML("afterbegin", inHtml);

            }
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}
