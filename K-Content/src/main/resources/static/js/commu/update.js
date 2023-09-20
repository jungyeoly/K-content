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
// selectedFiles 배열에 있는 모든 파일을 목록 형태로 표시하는 함수를 정의합니다. 삭제 버튼도 추가되며, 버튼을 클릭하면 해당 파일이 배열에서 제거
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
//양식의 유효성을 검사하는 함수를 정의합니다. 만약 양식이 유효하지 않으면, 모달을 표시하여 사용자에게 알림
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
// 주어진 제목과 내용으로 모달을 표시하는 함수를 정의
function showModal(title, content) {
	const commonModalLabelElem = document.getElementById('commonModalLabel');
	const modalBodyElem = document.querySelector('.modal-body');
	if (!commonModalLabelElem || !modalBodyElem) return;

	commonModalLabelElem.textContent = title;
	modalBodyElem.textContent = content;

	var commonModal = new bootstrap.Modal(document.getElementById('commonModal'));
	commonModal.show();
}
//파일 선택 버튼이 클릭되면, 파일 입력 요소를 클릭하는 이벤트 핸들러를 추가
const fileSelectButton = document.getElementById('fileSelectButton');
if (fileSelectButton) {
	fileSelectButton.onclick = function(event) {
		event.preventDefault();
		const attachmentElem = document.getElementById('attachment');
		if (attachmentElem) attachmentElem.click();
	};
}
//카테고리 선택 요소에 이벤트 핸들러를 추가하여, 카테고리가 변경되면 폼의 액션 URL을 업데이트
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
//파일 목록에서 특정 파일을 클릭하면 그 파일을 목록에서 제거하는 이벤트 핸들러를 추가
const commuFileElem = document.querySelector('.commu-File');
if (commuFileElem) {
	commuFileElem.addEventListener('click', function(event) {
		if (event.target && event.target.getAttribute('data-filename')) {
			removeFileFromDisplayList(event, event.target);
		}
	});
}
//서버에서 파일을 삭제하거나 selectedFiles 배열에서 파일을 제거하는 함수를 정의
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
//서버에서 특정 파일을 삭제하는 비동기 함수를 정의
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
//폼 제출 이벤트를 처리하는 함수를 정의합니다. 이 함수는 양식 데이터와 선택한 파일들을 서버에 POST 요청으로 전송
function onSubmitForm(event) {
	event.preventDefault();

	const form = event.target;
	const formData = new FormData(document.querySelector('form'));
	for (let file of selectedFiles) {
		formData.append('commuUploadFiles', file);
	}

	// selectedFiles 배열에 있는 파일들을 formData에 추가
	for (let i = 0; i < selectedFiles.length; i++) {
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

