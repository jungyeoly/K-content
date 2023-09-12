$(document).ready(function () {

    var requestData = {
        trendQueryList: document.getElementById("trendQueryList").value.slice(1, -1)
    };
    console.log(requestData);
    $.ajax({
        url: '/cs/instacrol',
        type: 'GET',
        data: requestData,
        success: function (data) {
            const element = document.getElementById('trend');
            data.slice(1, -1);
            data.slice(1, -1);
            element.innerHTML = data

            console.log(data);
        },
        error: function (error) {
            console.error('에러 발생: ', error);
        }
    });

    var requestData = {
        targetContentIdF: document.getElementById("contentId").value
    };

    $.ajax({
        url: '/cs/youtube/iframe',
        type: 'GET',
        data: requestData, // 데이터 객체 전달
        success: function (data2) {
            // 서버에서 받은 데이터를 result 요소에 추가합니다.
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
