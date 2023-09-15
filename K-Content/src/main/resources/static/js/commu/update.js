// 선택한 파일들을 저장하는 배열
let selectedFiles = [];
// 삭제할 파일들을 저장하는 배열
let filesToDelete = [];

// 사용자가 파일을 선택했을 때, 선택한 파일들을 selectedFiles 배열에 추가하는 함수
function appendFileList() {
    // 사용자가 선택한 파일들을 가져옵니다.
    const files = document.getElementById('attachment').files;
    for (let i = 0; i < files.length; i++) {
        selectedFiles.push(files[i]);
    }
    // 선택한 파일 리스트를 화면에 표시합니다.
    displayFileList();
}

// selectedFiles 배열에 있는 파일들을 화면에 표시하는 함수
function displayFileList() {
    const fileList = document.getElementById('fileList');
    fileList.innerHTML = '';
    selectedFiles.forEach((file, index) => {
        const li = document.createElement('li');
        li.textContent = file.name;
        const deleteButton = document.createElement('button');
        deleteButton.textContent = '삭제';
        deleteButton.dataset.index = index;
        deleteButton.onclick = function() {
            // 파일 삭제 버튼을 클릭하면 해당 파일을 selectedFiles 배열에서 제거합니다.
            const idx = parseInt(this.dataset.index);
            selectedFiles.splice(idx, 1);
            // 화면에 있는 파일 리스트를 업데이트합니다.
            displayFileList();
        }
        li.appendChild(deleteButton);
        fileList.appendChild(li);
    });
}

async function validateForm() {
    var title = document.getElementById('title').value;
    var category = document.getElementById('category').value;
    var cntnt = document.getElementById('cntnt').value;

    // 제목, 카테고리, 내용의 유효성을 검사합니다.
    if (!title || title.trim() === "") {
        showModal("Error", "제목을 입력하세요.");
        return false;
    }
    if (!category || category.trim() === "") {
        showModal("Error", "카테고리를 선택하세요.");
        return false;
    }
    if (!cntnt || cntnt.trim() === "") {
        showModal("Error", "내용을 입력하세요.");
        return false;
    }

    // 선택된 새로운 파일들을 서버에 전송합니다.
    // (이 부분은 필요한 경우에만 추가하시면 됩니다. 일반적인 폼 제출로도 파일을 서버로 전송할 수 있습니다.)

    return true;
}

// 에러나 알림을 사용자에게 표시하는 함수
function showModal(title, content) {
    document.getElementById('commonModalLabel').textContent = title;
    document.querySelector('.modal-body').textContent = content;
    // Bootstrap의 Modal 기능을 사용하여 모달을 표시합니다.
    var commonModal = new bootstrap.Modal(document.getElementById('commonModal'));
    commonModal.show();
}

// 파일 선택 버튼을 클릭하면 파일 선택창이 열리게 하는 이벤트 핸들러
document.getElementById('fileSelectButton').onclick = function(event) {
    event.preventDefault();
    document.getElementById('attachment').click();
};

// 카테고리를 변경할 때 폼의 action 값을 변경하는 이벤트 핸들러
document.getElementById('category').addEventListener('change', function() {
    let commuCateCode = this.value;
    let form = document.querySelector('form');
	let commuId = parseInt(document.getElementById('commuId').textContent);
    form.setAttribute('action', "/commu/update/" + commuCateCode + "/" + commuId);
});

document.querySelector('.commu-File').addEventListener('click', function(event) {
    // 삭제 버튼을 클릭했는지 확인
    if (event.target && event.target.getAttribute('data-filename')) {
        removeFileFromDisplayList(event, event.target);
    }
});

function removeFileFromDisplayList(event, buttonElement) {
    event.preventDefault();
    event.stopPropagation();

    const fileName = buttonElement.getAttribute('data-filename');
    const listItem = buttonElement.parentElement;

    // 기존 파일인지 확인
    if (listItem.getAttribute('data-type') === 'existing') {
        // AJAX로 서버에 파일 삭제 요청
        const fileId = btnElement.getAttribute('data-fileid');
        deleteFileFromServer(fileId).then((response) => {
            if (response.success) {
                listItem.remove();
            } else {
                showModal("Error", "파일을 삭제하는 동안 오류가 발생했습니다.");
            }
        }).catch((error) => {
            showModal("Error", "파일을 삭제하는 동안 오류가 발생했습니다.");
        });
    } else {
        // 새로 추가된 파일 삭제 처리
        const index = selectedFiles.findIndex(file => file.name === fileName);
        if (index > -1) {
            selectedFiles.splice(index, 1);
        }
        displayFileList();
    }
}


function deleteFileFromServer(fileId) {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open("DELETE", `/path-to-your-api/delete-file/${fileId}`, true);
        xhr.onload = function() {
            if (this.status >= 200 && this.status < 400) {
                resolve(JSON.parse(this.response));
            } else {
                reject(new Error(this.statusText));
            }
        };
        xhr.onerror = function() {
            reject(new Error("Request failed"));
        };
        xhr.send();
    });
}





// 파일 삭제 목록에 파일을 추가하고 화면에서 해당 파일을 제거하는 함수
function addToDeleteList(fileName) {
    filesToDelete.push(fileName);
    const existingFileList = document.querySelector(".commu-File > ul");
    const listItem = Array.from(existingFileList.children).find(li => li.querySelector("span").textContent === fileName);
    if (listItem) existingFileList.removeChild(listItem);
}
