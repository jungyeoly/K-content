// 선택한 파일들을 저장하는 배열
let selectedFiles = [];

// 사용자가 파일을 선택했을 때, 선택한 파일들을 selectedFiles 배열에 추가하는 함수
function appendFileList() {
    const fileInputElement = document.getElementById('attachment');
    if (!fileInputElement) return;
    const files = fileInputElement.files;
    for (let i = 0; i < files.length; i++) {
        selectedFiles.push(files[i]);
    }
    displayFileList();
}

function displayFileList() {
    const fileList = document.getElementById('fileList');
    if (!fileList) return;
    fileList.innerHTML = '';
    selectedFiles.forEach((file, index) => {
        const li = document.createElement('li');
        li.textContent = file.name;
        const deleteButton = document.createElement('button');
        deleteButton.textContent = '삭제';
        deleteButton.dataset.index = index;
        deleteButton.onclick = function() {
            const idx = parseInt(this.dataset.index);
            selectedFiles.splice(idx, 1);
            displayFileList();
        }
        li.appendChild(deleteButton);
        fileList.appendChild(li);
    });
}

async function validateForm() {
    const titleElem = document.getElementById('title');
    const categoryElem = document.getElementById('category');
    const cntntElem = document.getElementById('cntnt');

    const title = titleElem ? titleElem.value : "";
    const category = categoryElem ? categoryElem.value : "";
    const cntnt = cntntElem ? cntntElem.value : "";

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
    return true;
}

function showModal(title, content) {
    const commonModalLabelElem = document.getElementById('commonModalLabel');
    const modalBodyElem = document.querySelector('.modal-body');
    if (!commonModalLabelElem || !modalBodyElem) return;

    commonModalLabelElem.textContent = title;
    modalBodyElem.textContent = content;

    var commonModal = new bootstrap.Modal(document.getElementById('commonModal'));
    commonModal.show();
}

const fileSelectButton = document.getElementById('fileSelectButton');
if (fileSelectButton) {
    fileSelectButton.onclick = function(event) {
        event.preventDefault();
        const attachmentElem = document.getElementById('attachment');
        if (attachmentElem) attachmentElem.click();
    };
}

const categoryElem = document.getElementById('category');
if (categoryElem) {
    categoryElem.addEventListener('change', function() {
        let commuCateCode = this.value;
        let form = document.querySelector('form');
        let commuIdElem = document.getElementById('commuId');
        let commuId = commuIdElem ? parseInt(commuIdElem.textContent) : null;
        form.setAttribute('action', "/commu/update/" + commuCateCode + "/" + commuId);
    });
}

const commuFileElem = document.querySelector('.commu-File');
if (commuFileElem) {
    commuFileElem.addEventListener('click', function(event) {
        if (event.target && event.target.getAttribute('data-filename')) {
            removeFileFromDisplayList(event, event.target);
        }
    });
}

function removeFileFromDisplayList(event, buttonElement) {
    event.preventDefault();
    event.stopPropagation();

    const fileName = buttonElement.getAttribute('data-filename');
    const listItem = buttonElement.parentElement;
    const fileId = buttonElement.getAttribute('data-fileId');

   if (listItem.getAttribute('data-type') === 'existing') {
        deleteFileFromServer(fileId).then((response) => {
            if (response && response.success) {
                listItem.remove();
            } else {
                showModal("Error", response.message || "파일을 삭제하는 동안 오류가 발생했습니다.");
            }
        }).catch((error) => {
            showModal("Error", error.message || "파일을 삭제하는 동안 오류가 발생했습니다.");
        });
    } else {
        const index = selectedFiles.findIndex(file => file.name === fileName);
        if (index > -1) {
            selectedFiles.splice(index, 1);
        }
        displayFileList();
    }
}

async function deleteFileFromServer(commuFileId) {
    const url = `/commu/delete-file`;
    const headers = {
        "Content-Type": "application/json;charset=UTF-8"
    };
    const body = JSON.stringify({ commuFileId: commuFileId }); 
	console.log(commuFileId)
    return fetch(url, {
        method: 'POST',
        headers: headers,
        body: body 
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Network response was not ok");
        }
        return response.json();
    });
}

function onSubmitForm(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(document.querySelector('form'));
for (let file of selectedFiles) {
    formData.append('commuUploadFiles', file);
}
    
    // selectedFiles 배열에 있는 파일들을 formData에 추가
    for(let i=0; i<selectedFiles.length; i++) {
        formData.append('commuUploadFiles', selectedFiles[i]);
    }
    
    fetch(form.action, {
        method: 'POST',
        body: formData,
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 성공 메시지 또는 페이지 리디렉션
        } else {
            showModal("Error", data.message);
        }
    })
    .catch(error => {
        showModal("Error", error.message);
    });
}

// 이벤트 리스너 추가
document.querySelector('form').addEventListener('submit', onSubmitForm);

