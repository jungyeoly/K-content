
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
	console.log(fileList);
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

	if (!category || category.trim() === "") {
		showModal("K-Spectrum", "카테고리를 선택하세요.");
		return false;
	}

	if (!title || title.trim() === "") {
		showModal("K-Spectrum", "제목을 입력하세요.");
		return false;
	}

	if (!cntnt || cntnt.trim() === "") {
		showModal("K-Spectrum", "내용을 입력하세요.");
		return false;
	}
}

document.querySelector('form').addEventListener('submit', function(event) {
	event.preventDefault(); // 폼의 기본 제출 동작을 막습니다.
	validateForm(); // 여기서 validateForm 함수를 호출합니다.
});

function showModal(title, content, callback) {
	document.getElementById('commonModalLabel').textContent = title;
	document.querySelector('.modal-body').textContent = content;
	var commonModal = new bootstrap.Modal(document.getElementById('commonModal'));

	
	

	const confirmButton = document.querySelector('.modal-footer .btn-confirm');
	if (confirmButton) {
		// 기존의 모든 이벤트 리스너를 제거
		let newConfirmBtn = confirmButton.cloneNode(true);
		confirmButton.parentNode.replaceChild(newConfirmBtn, confirmButton);

		// 새로운 콜백으로 이벤트 리스너를 설정
		newConfirmBtn.onclick = function() {
			if (callback) callback();
			commonModal.hide();
		};
	}

	commonModal.show();
}



document.getElementById('category').addEventListener('change', function() {
	let commuCateCode = this.value;
	let form = document.querySelector('form');
	form.setAttribute('action', "/commu/write/" + commuCateCode);
});
