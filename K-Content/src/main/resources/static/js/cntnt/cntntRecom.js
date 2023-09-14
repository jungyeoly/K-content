$(document).ready(function () {
    $.ajax({
        url: '/cs/getsearchyoutube', type: 'GET',
        success: function (contentList) {

            const element = document.getElementById('card-list');
            element.innerHTML = '';
            for (i = 0; i < contentList.length; i++) {
                var str = '';
                arr = Array.from(contentList[i].title);
                arr.map((data,idx) => {
                    if(data == "'"){
                        //arr[idx] = "\\'";
                        str = str + "\\'";
                    }
                    else if(data == '"'){
                        //arr[idx] = '\\"';
                        str = str + '\\"';
                    }
                    else{
                        str = str + data;
                    }
                })
                // spaArr =arr.join('');


                // console.log("spaArrspaArrspaArr "+str);
                inHtml = `
            <li class="card-item" id="card-item" onclick="cntntMake( '${str}' , '${contentList[i].url}' )">
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


function cntntMake(spaArr,url) {
console.log("spaArr: "+ spaArr);
    console.log("url: "+ url);
    // JSON.stringify(contentList);
    // console.log(JSON.stringify(contentList));
    // arr = Array.from(cntntTitle);
    // index = [];
    //
    // for (i = 0; i < arr.length; i++) {
    //     if (arr[i] == "\"" || arr[i] == "\'") {
    //         index.push(i);
    //     }
    // }
    //
    // for (i = 0; i < index.length; i++) {
    //     if (index[i] == 0) {
    //         arr.unshift('\\');
    //     } else {
    //         arr.splice(i, 0, '\\');
    //     }
    // }

    // console.log("arr" +arr);
    // const formHtml = `
    //                 <form id="contentMake" action="/cs/makecontent" method="get">
    //                     <input  id="cntntURL" name="cntntURL"  />
    //                     <input  id="cntntTitle" name="cntntTitle"  />
    //                 </form>`;
    //
    // const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    // const form = doc.body.firstChild;
    // document.body.append(form);
    // document.getElementById("cntntURL").value = cntntURL;
    // document.getElementById("cntntTitle").value = cntntTitle;
    // ;
    // console.log(document.getElementsByClassName("cntntURL").value);
    // console.log(document.getElementsByClassName("cntntTitle").value);
    // document.getElementById('contentMake').submit();
}

function searchButton() {

    var requestData = {
        searchKeyword: document.getElementById('search-input').value
    };

    $.ajax({
        url: '/cs/getsearchyoutube', type: 'GET',
        data: requestData,
        success: function (contentList) {
            const element = document.getElementById('card-list');
            element.innerHTML = '';
            for (i = 0; i < contentList.length; i++) {
                inHtml = `
            <li class="card-item" id="card-item" onclick="cntntMake(${contentList[i]})">
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
