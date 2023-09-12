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
	var title = document.getElementById('title').value;
	var category = document.getElementById('category').value;
	var cntnt = document.getElementById('cntnt').value;

	if (!title || title.trim() === "") {
		alert("제목을 입력하세요.");
		return false;
	}

	if (!category || category.trim() === "") {
		alert("카테고리를 선택하세요.");
		return false;
	}

	if (!cntnt || cntnt.trim() === "") {
		alert("내용을 입력하세요.");
		return false;
	}

	return true;
}

const fileSelectButton = document.getElementById('fileSelectButton');
fileSelectButton.onclick = function(event) {
	event.preventDefault();
	document.getElementById('attachment').click();
};


document.getElementById('category').addEventListener('change', function() {
	let commuCateCode = this.value;
	let form = document.querySelector('form');
	form.setAttribute('action', "/commu/write/" + commuCateCode);
});
