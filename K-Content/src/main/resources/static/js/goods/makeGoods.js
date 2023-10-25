var fileInput = document.getElementById("input-file");
//값이 변경될때 호출 되는 이벤트 리스너

var file;
var photoFrame = document.createElement("img");
fileInput.addEventListener('change', function (e) {

    var elements = document.getElementsByClassName('photoFrame');
    while (elements.length > 0) {
        elements[0].parentNode.removeChild(elements[0]);
    }

    file = e.target.files[0]; //선택된 파일
    var reader = new FileReader();
    reader.readAsDataURL(file); //파일을 읽는 메서드
    reader.onload = function () {
        photoFrame.src = `${reader.result}`;
        photoFrame.className = "photoFrame";
        document.getElementById("pictures").appendChild(photoFrame);

        photoFrame.addEventListener("click", function () {
            document.getElementById("pictures").removeChild(photoFrame);
            photoFrame.src = null;
        })
    }
})


function removeFile(id) {
    document.getElementById(id).remove();
}

function createGoods() {
    event.preventDefault();
    var keywordDivList = [];
    var keywordDiv = document.getElementById("keywordList");
    var keywordDivCount = keywordDiv.getElementsByClassName("keywordButton");
    for (i = 0; i < keywordDivCount.length; i++) {
        var trimmedStr = keywordDivCount[i].textContent.replace(/^\s+|\s+$/g, "");
        keywordDivList.push(trimmedStr);
    }

    console.log(keywordDivList.length);
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

    const fileInput = document.getElementById('input-file');
    const selectedFile = fileInput.files[0];
    console.log(file);
    fileInput.setAttribute(file[0], file[1])

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
        url: '/cs/cntnt/goods',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function () {

            Swal.fire({
                title: '상품이 등록 되었습니다.',
                icon: 'success',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인',
                reverseButtons: true,

            }).then(result => {
                if (result.isConfirmed) {
                    location.href = '/cs/cntnt/goods';
                }
            });

        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });
}

window.addEventListener('load', () => {
    const forms = document.getElementsByClassName('validation-form');

    Array.prototype.filter.call(forms, (form) => {
        form.addEventListener('submit', function (event) {
            if (form.checkValidity() === false) {
                console.log("sdfsdfsd");
                event.preventDefault();
                event.stopPropagation();
            }

            form.classList.add('was-validated');
        }, false);
    });
}, false);


// 굿즈 생성, 컨텐츠 생성에서
function createGoodsInCntnt() {
    event.preventDefault();
    var keywordDivList = [];
    var keywordDiv = document.getElementById("keywordList");
    var keywordDivCount = keywordDiv.getElementsByClassName("keywordButton");
    for (i = 0; i < keywordDivCount.length; i++) {
        var trimmedStr = keywordDivCount[i].textContent.replace(/^\s+|\s+$/g, "");
        keywordDivList.push(trimmedStr);
    }
    if (keywordDivList.length == 0) {
        Swal.fire({
            title: '키워드를 입력하세요!',
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

    const fileInput = document.getElementById('input-file');
    const selectedFile = fileInput.files[0];
    console.log(file);
    fileInput.setAttribute(file[0], file[1])

    var formData = new FormData();
    formData.append("goodsTitle", $("#name").val());
    formData.append("goodsBrand", $("#brand").val());
    formData.append("goodsURL", $("#link").val());
    formData.append("goodsPrice", $("#price").val());
    formData.append("keywordList", keywordDivList);
    formData.append("goodsFile", selectedFile);


    $.ajax({
        url: '/cs/cntnt/goods',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function () {

            Swal.fire({
                title: '상품이 등록 되었습니다.',
                icon: 'success',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인',
                reverseButtons: true,

            }).then(result => {
                if (result.isConfirmed) {
                    location.href = '/cs/cntnt/goods/cntnt'; //이걸 으데로 이동시켜야?
                }
            });

        }, error: function (error) {
            Swal.fire({
                title: '상품 생성에 실패했습니다.',
                icon: 'warning',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인',
                reverseButtons: true,

            }).then(result => {
                if (result.isConfirmed) {
                    console.error('에러 발생: ', error);
                }
            });
        }
    });
}

// 키워드 삭제
function delKeyword(key) {
    const div = document.getElementById(key);
    div.remove();
}

function makeKeyword() {
    var inputWord = document.getElementById("inputKeyword").value;

    var word = inputWord.replace(/(\s*)/g, '');
    if (word == null || word == '' || word == ' ') {
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
          <div id="${word}" style="margin-top: 20px">
                <button type="button" style="margin-left: 10px" class="btn btn-primary position-relative keywordButton"
                        >
                     ${word}
                </button>
                <button style="z-index: 10; "
                        class="position-absolute translate-middle badge rounded-pill bg-danger"
                            key="${word}"
                        onclick="delKeyword(this.getAttribute('key'))">X
                </button>
        </div>`;

        thisDiv.insertAdjacentHTML("beforeend", innerHtml);
        document.getElementById("inputKeyword").value = '';
    }


}

function updateGoodsIf() {
    event.preventDefault();
    const fileInput = document.getElementById('input-file');
    const selectedFile = fileInput.files[0];

    if (selectedFile == null) {
        updateGoodsNoFile();
    } else {
        updateGoodsForm();
    }
}

function updateGoodsNoFile() {
    event.preventDefault();
    var keywordDivList = [];
    var keywordDiv = document.getElementById("keywordList");
    var keywordDivCount = keywordDiv.getElementsByClassName("keywordButton");
    console.log("keywordDivCount: " + keywordDivCount.length);
    for (i = 0; i < keywordDivCount.length; i++) {
        var trimmedStr = keywordDivCount[i].textContent.replace(/^\s+|\s+$/g, "");
        keywordDivList.push(trimmedStr);
        console.log("trimmedStr:" + trimmedStr)
    }
    console.log(keywordDivList.length);
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
    var formData = new FormData();
    formData.append("goodsId", $("#goodsId").val());
    formData.append("goodsTitle", $("#name").val());
    formData.append("goodsBrand", $("#brand").val());
    formData.append("goodsURL", $("#link").val());
    formData.append("goodsPrice", $("#price").val());
    formData.append("keywordList", keywordDivList);

    console.log("formData: " + formData)

    $.ajax({
        url: '/cs/cntnt/goods/form/nofile',
        type: 'patch',
        data: formData,
        processData: false,
        contentType: false,
        success: function () {
            Swal.fire({
                title: '상품 수정 완료',
                icon: 'success',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인',
                reverseButtons: true,

            }).then(result => {
                if (result.isConfirmed) {
                    location.href = '/cs/cntnt/goods';
                }
            });
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });

}

function updateGoodsForm() {
    event.preventDefault();
    console.log(document.getElementById("goodsId"));
    var keywordDivList = [];
    var keywordDiv = document.getElementById("keywordList");
    var keywordDivCount = keywordDiv.getElementsByClassName("keywordButton");
    console.log("keywordDivCount: " + keywordDivCount.length);
    for (i = 0; i < keywordDivCount.length; i++) {
        var trimmedStr = keywordDivCount[i].textContent.replace(/^\s+|\s+$/g, "");
        keywordDivList.push(trimmedStr);
        console.log("trimmedStr:" + trimmedStr)
    }
    console.log(keywordDivList.length);
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
    const fileInput = document.getElementById('input-file');
    const selectedFile = fileInput.files[0];

    var formData = new FormData();
    formData.append("goodsId", $("#goodsId").val());
    formData.append("goodsTitle", $("#name").val());
    formData.append("goodsBrand", $("#brand").val());
    formData.append("goodsURL", $("#link").val());
    formData.append("goodsPrice", $("#price").val());
    formData.append("keywordList", keywordDivList);
    formData.append("goodsFile", selectedFile);
    console.log("formData: " + formData)

    $.ajax({
        url: '/cs/cntnt/goods/form',
        type: 'patch',
        data: formData,
        processData: false,
        contentType: false,
        success: function () {
            Swal.fire({
                title: '상품 수정 완료',
                icon: 'success',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인',
                reverseButtons: true,

            }).then(result => {
                if (result.isConfirmed) {
                    location.href = '/cs/cntnt/goods';
                }
            });
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });

}

function cancle() {
    location.href = "/cs/cntnt/goods";
}

function delGoodsFile(key) {
    // if (confirm('상품사진을 삭제하시겠습니까?')) {
    //     $.ajax({
    //         url: '/cs/goods/file',
    //         type: 'delete',
    //         data: {
    //             key: key
    //         },
    //         success: function () {
    //             alert("사진이 삭제되었습니다!")
    //         }, error: function (error) {
    //             console.error('에러 발생: ', error);
    //         }
    //     });
    // }
}
