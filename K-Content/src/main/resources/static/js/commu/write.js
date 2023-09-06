let selectedFiles = [];

function appendFileList() {
    const files = document.getElementById('attachment').files;
    for (let i = 0; i < files.length; i++) {
        selectedFiles.push(files[i]);
    }
    displayFileList();
}

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
            const idx = parseInt(this.dataset.index);
            selectedFiles.splice(idx, 1);
            displayFileList();
        }
        li.appendChild(deleteButton);
        fileList.appendChild(li);
    });
}

function validateForm() {
    var title = document.getElementById('commuTitle').value;
    var category = document.getElementById('category').value;
    var content = document.getElementById('commuCntnt').value;

    if (!title || title.trim() === "") {
        alert("제목을 입력하세요.");
        return false;
    }

    if (!category || category.trim() === "") {
        alert("카테고리를 선택하세요.");
        return false;
    }

    if (!content || content.trim() === "") {
        alert("내용을 입력하세요.");
        return false;
    }

    return true;
}
