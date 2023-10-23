const titleInput = document.getElementById('title');
const commuCateCode = titleInput.getAttribute('data-commu-cate-code');
const commuId = titleInput.getAttribute('data-commu-id');

// 현재 페이지의 경로가 /cs로 시작하는지 확인
var isCsPage = window.location.pathname.startsWith('/cs');
console.log("isCsPage:", isCsPage);
// /cs로 시작하는 페이지에서만 실행
if (isCsPage) {
	var updateURL = `/cs/commu/update/${commuCateCode}/${commuId}`;
	console.log("isCsPage:", isCsPage);
	if (window.location.pathname === updateURL) {
		// 페이지 로드 시 제목 필드의 기본값을 "[공지]"로 설정
		if (!titleInput.value.startsWith("[공지]")) {
			titleInput.value = "[공지] " + titleInput.value;
		}


		titleInput.addEventListener('input', enforcePrefix);

		titleInput.addEventListener('keydown', function(e) {
			const cursorPos = titleInput.selectionStart;
			const deleteKeys = ["Backspace", "Delete"];
			if ((deleteKeys.includes(e.key) && cursorPos <= 5) ||
				(e.key === "ArrowLeft" && cursorPos <= 6) ||
				(e.key === "ArrowRight" && cursorPos <= 5) ||
				e.ctrlKey) {
				e.preventDefault();
			}
		});

		const baseURL = window.location.origin;
		// 현재 페이지의 경로가 /cs로 시작하는지 확인
		let newPath = window.location.pathname.startsWith('/cs')
			? `/cs/commu/update/${commuCateCode}/${commuId}`
			: `/commu/update/${commuCateCode}/${commuId}`;

		form.setAttribute('action', `${baseURL}${newPath}`);
		console.log("DOMContentLoaded event completed.");
	}
}

//임시 삭제 파일 목록 배열 추가
let tempDeletedFiles = [];

// 선택한 파일들을 저장하는 배열
let selectedFiles = [];

function getCurrentPageType() {
	return window.location.pathname.includes('/cs') ? 'admin' : 'user';
}


// 사용자나 관리자가 파일을 선택했을 때, 선택한 파일들을 selectedFiles 배열에 추가하는 함수
function appendFileList() {
	console.log("appendFileList() started.");
	const pageType = getCurrentPageType();
	const attachmentId = pageType === 'admin' ? 'adminAttachment' : 'attachment';
	const fileInputElement = document.getElementById(attachmentId);
	if (!fileInputElement) return;
	const files = fileInputElement.files;
	console.log(appendFileList);
	for (let i = 0; i < files.length; i++) {
		if (!selectedFiles.some(file => file.name === files[i].name)) {
			selectedFiles.push(files[i]);
			console.log(files.length);
		}
	}
	displayFileList();
	console.log(displayFileList);
	console.log("appendFileList() completed.");
}

// selectedFiles 배열에 있는 모든 파일을 목록 형태로 표시하는 함수를 정의합니다. 삭제 버튼도 추가되며, 버튼을 클릭하면 해당 파일이 배열에서 제거
function displayFileList() {
	console.log("displayFileList() started.");
	const pageType = getCurrentPageType();
	const fileListId = pageType === 'admin' ? 'adminFileList' : 'fileList';
	const fileList = document.getElementById(fileListId);
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
	console.log("displayFileList() completed.");
}



//양식의 유효성을 검사하는 함수를 정의합니다. 만약 양식이 유효하지 않으면, 모달을 표시하여 사용자에게 알림
async function validateForm() {
	console.log("validateForm() started.");
	const titleElem = document.getElementById('title');
	const categoryElem = document.getElementById('category');
	const cntntElem = document.getElementById('cntnt');

	const title = titleElem ? titleElem.value : "";
	const category = categoryElem ? categoryElem.value : "";
	const cntnt = cntntElem ? cntntElem.value : "";

	if (!title || title.trim() === "") {
		await showModal("K-Spectrum", "제목을 입력하세요.");
		return false;
	}
	if (!category || category.trim() === "") {
		await showModal("K-Spectrum", "카테고리를 선택하세요.");
		return false;
	}
	if (!cntnt || cntnt.trim() === "") {
		await showModal("K-Spectrum", "내용을 입력하세요.");
		return false;
	}
	return true;
}

// 주어진 제목과 내용으로 모달을 표시하는 함수를 정의
async function showModal(title, content) {
	return new Promise((resolve) => {
		Swal.fire({
			title: title,
			text: content,
			icon: 'warning',
			confirmButtonText: '확인'
		}).then(() => {
			resolve();
		});
	});
}


// 파일 선택 버튼이 클릭되면, 파일 입력 요소를 클릭하는 이벤트 핸들러를 추가
const fileSelectButton = document.getElementById('fileSelectButton');
if (fileSelectButton) {
	fileSelectButton.onclick = function(event) {
		event.preventDefault();
		const pageType = getCurrentPageType();

		console.log("fileSelectButton clicked on", pageType, "page.");

		let attachmentId;
		if (pageType === 'admin') {
			attachmentId = 'adminAttachment';
			console.log(attachmentId);
		} else {
			attachmentId = 'attachment';
		}

		const attachmentElem = document.getElementById(attachmentId);
		if (attachmentElem) attachmentElem.click();
	};
}


// 카테고리 선택 요소에 이벤트 핸들러를 추가하여, 카테고리가 변경되면 폼의 액션 URL을 업데이트
const categoryElem = document.getElementById('category');

if (categoryElem) {
	categoryElem.addEventListener('change', function() {
		console.log("categoryElem value changed.");
		let commuCateCode = this.value;
		let form = document.querySelector('form');
		let commuIdElem = document.getElementById('commuId');
		let commuId = commuIdElem ? parseInt(commuIdElem.textContent) : null;

		// 현재 페이지의 호스트와 포트를 반환합니다
		const baseURL = window.location.origin;

		// 현재 경로가 /cs로 시작하는지 확인
		let newPath = window.location.pathname.startsWith('/cs')
			? `/cs/commu/update/${commuCateCode}/${commuId}`
			: `/commu/update/${commuCateCode}/${commuId}`;

		form.setAttribute('action', `${baseURL}${newPath}`);
	});
}

const formElem = document.querySelector('form');


// 파일 목록에서 특정 파일을 클릭하면 그 파일을 목록에서 제거하는 이벤트 핸들러를 추가
const commuFileElem = document.querySelector('.commu-File');
if (commuFileElem) {
	commuFileElem.addEventListener('click', function(event) {
		if (event.target && event.target.getAttribute('data-filename')) {
			removeFileFromDisplayList(event, event.target);
		}
	});
}

// 서버에서 파일을 삭제하거나 selectedFiles 배열에서 파일을 제거하는 함수를 정의
function removeFileFromDisplayList(event, buttonElement) {
	event.preventDefault();
	event.stopPropagation();

	const fileName = buttonElement.getAttribute('data-filename');
	const listItem = buttonElement.parentElement;
	const fileId = buttonElement.getAttribute('data-fileId');

	if (listItem.getAttribute('data-type') === 'existing') {
		// 서버에서 즉시 삭제하지 않고 tempDeletedFiles 배열에 추가
		tempDeletedFiles.push(fileId);
		listItem.remove();
	} else {
		const index = selectedFiles.findIndex(file => file.name === fileName);
		if (index > -1) {
			selectedFiles.splice(index, 1);
		}
		displayFileList();
	}
}
// 서버에서 특정 파일을 삭제하는 비동기 함수를 정의
async function deleteFileFromServer(commuFileId) {
	// window.location.origin은 현재 페이지의 호스트와 포트를 반환합니다 
	const baseURL = window.location.origin;

	// 현재 페이지의 경로가 '/cs'로 시작하는지 확인
	let path = window.location.pathname.startsWith('/cs')
		? `/cs/commu/delete-file`
		: `/commu/delete-file`;

	// 전체 URL 생성
	let url = `${baseURL}${path}`;
	console.log(url);

	const headers = {
		"Content-Type": "application/json;charset=UTF-8",
		"Accept": "application/json"
	};
	const body = JSON.stringify({ commuFileId: commuFileId });

	return fetch(url, {
		method: 'POST',
		headers: headers,
		body: body
	})
		.then(response => {
			console.log(response);
			if (!response.ok) {
				throw new Error("Network response was not ok");
			}
			return response.json();
		});
}

document.addEventListener('DOMContentLoaded', function() {
	// 저장 버튼에 대한 이벤트 핸들러
	const saveButton = document.querySelector('.saveBtn');
	if (saveButton) {
		saveButton.addEventListener('click', async function(event) {
			// 폼 제출 기본 동작 막기
			event.preventDefault();


			for (const fileId of tempDeletedFiles) {
				try {
					const response = await deleteFileFromServer(fileId);
					if (response.success) {
						console.log(`File with ID ${fileId} deleted successfully.`);
					} else {
						console.error(`Failed to delete file with ID ${fileId}.`);
					}
				} catch (error) {
					console.error(`Error deleting file with ID ${fileId}: ${error.message}`);
				}
			}
			// 모든 작업이 완료되었을 때 폼 제출
			// 유효성 검사를 통과한 경우에만 폼 제출
			const isValid = await validateForm();

			if (isValid) {
				const userForm = document.querySelector('#updateForm');
				const adminForm = document.querySelector('#adminUpdateForm');

				if (userForm) {
					userForm.submit();
				} else if (adminForm) {
					adminForm.submit();
				}
			}

		});
	}
	// 취소 버튼은 이미 <a> 태그로 되어 있어서 별도의 이벤트 핸들러가 필요 없습니다.
});
