$.ajax({
    url: '/cs/getallcntnt', type: 'GET',
    success: function (contentList) {
        const element = document.getElementById('card-list');

        for (i = 0; i < contentList.length; i++) {
            inHtml = `
            <li class="card-item" id="card-item" onclick="cntntDetail(${contentList[i].cntntId})">
                <figure class="card-image"style="background-image: url(${contentList[i].cntntThumnail})">
                    <img src=${contentList[i].cntntThumnail} >
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


function searchButton() {

    var requestData = {
        searchKeyword: document.getElementById('search-input').value
    };

    $.ajax({
        url: '/cs/getsearchcntnt', type: 'GET',
        data: requestData,
        success: function (contentList) {
            const element = document.getElementById('card-list');
            element.innerHTML='';
            for (i = 0; i < contentList.length; i++) {
                inHtml = `
            <li class="card-item" id="card-item" onclick="cntntDetail(${contentList[i].cntntId})">
                <figure class="card-image"style="background-image: url(${contentList[i].cntntThumnail})">
                    <img src=${contentList[i].cntntThumnail} >
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
}


function cntntDetail(cntntId) {

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
