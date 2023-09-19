var fileInput = document.getElementById("fileInput");
//값이 변경될때 호출 되는 이벤트 리스너

var file;
fileInput.addEventListener('change', function (e) {
    file = e.target.files[0]; //선택된 파일
    var reader = new FileReader();
    reader.readAsDataURL(file); //파일을 읽는 메서드
    console.log(e.target.files)
    reader.onload = function () {
        var photoFrame = document.createElement("img");
        photoFrame.src = `${reader.result}`;
        photoFrame.className = "photoFrame";
        document.getElementById("pictures").appendChild(photoFrame);

        photoFrame.addEventListener("click", function () {
            document.getElementById("pictures").removeChild(photoFrame);
        })
    }
})


function createGoods() {
    var keywordDivList = [];
    var keywordDiv = document.getElementById("keywordList");
    var keywordDivCount = keywordDiv.getElementsByClassName("keywordButton");
    for (i = 0; i < keywordDivCount.length; i++) {
        var trimmedStr = keywordDivCount[i].textContent.replace(/^\s+|\s+$/g, "");
        keywordDivList.push(trimmedStr);
    }
    const fileInput = document.getElementById('input-file');
    const selectedFile = fileInput.files[0];


    var formData = new FormData();
    formData.append("goodsTitle", $("#name").val());
    formData.append("goodsBrand", $("#brand").val());
    formData.append("goodsURL", $("#link").val());
    formData.append("goodsPrice", $("#price").val());
    formData.append("keywordList", keywordDivList);
    formData.append("goodsFile", selectedFile);


    // var sendData = {
    //     "goodsTitle": $("#name").val(),
    //     "goodsBrand": $("#brand").val(),
    //     "goodsURL": $("#link").val(),
    //     "goodsPrice": $("#price").val(),
    //     "keywordList": keywordDivList,
    //     "goodsFile":selectedFile
    //     //이제 파일
    //
    //     //TODO 파일 명, 경로 담아서 보내기
    // };

    console.log(formData);

    $.ajax({
        url: '/cs/goods',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function () {
            alert("상품이 등록되었습니다!")
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}

// 키워드 삭제
function delKeyword(key) {
    const div = document.getElementById(key);
    div.remove();
}
function makeKeyword() {
    word = document.getElementById("inputKeyword").value;

    if (word == null || word == '') {
        alert("키워드를 입력하세요!");
    } else {
        const thisDiv = document.getElementsByClassName('newKeyword')[0];
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
        document.getElementById("inputKeyword").value = '';
    }


}
