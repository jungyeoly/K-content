document.addEventListener('DOMContentLoaded', function() {
	const titleInput = document.getElementById('title');

	// 현재 페이지의 경로가 '/cs/commu/write'인지 확인
	if (window.location.pathname === '/cs/commu/write') {
		
		// 입력 필드를 항상 비워짐으로 설정
		titleInput.value = "";

		// 입력 필드에서 키를 누를 때마다 이 함수를 호출
		titleInput.addEventListener('input', function() {
			// "[공지]" 텍스트가 제목 시작 부분에 없으면 추가
			if (!this.value.startsWith("[공지]")) {
				this.value = "[공지] " + this.value;
			}
		});
	}
});

document.querySelector('.cancel-btn').addEventListener('click', function() {
    
    const titleInput = document.getElementById('title');
      // 현재 페이지의 경로가 '/cs/commu/write'인지 확인하여 해당 경로에서만 [공지]를 추가
    if (window.location.pathname === '/cs/commu/write') {
        titleInput.value = "[공지]";
}
});


let selectedFiles = [];

function appendFileList() {
	const files = document.getElementById('attachment').files;
	selectedFiles = []; // selectedFiles 배열 초기화
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

	// /cs/commu/write 페이지에서만 [공지] 텍스트를 제외하고 제목을 검사
	if (window.location.pathname === '/cs/commu/write') {
		title = title.replace("[공지]", "").trim();
	}

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
	return true;
}

function showModal(title, content) {
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


document.querySelector('form').addEventListener('submit',async function(event) {
	event.preventDefault(); // 폼의 기본 제출 동작을 막습니다.

	// validateForm 함수를 호출하고, 결과가 true이면 폼 제출
	  if (validateForm()) {
        this.submit(); // 폼 제출
    }
});


	const confirmButton = document.querySelector('.modal-footer .btn-confirm');
	if (confirmButton) {
		// 기존의 모든 이벤트 리스너를 제거
		let newConfirmBtn = confirmButton.cloneNode(true);
		confirmButton.parentNode.replaceChild(newConfirmBtn, confirmButton);

		// 새로운 콜백으로 이벤트 리스너를 설정
		newConfirmBtn.onclick = function() {
			
		};
	}

document.getElementById('category').addEventListener('change', function() {
	let commuCateCode = this.value;
	let form = document.querySelector('form');
	// URL이 '/cs'로 시작하는지 확인
	if (window.location.pathname.startsWith('/cs')) {
		form.setAttribute('action', "/cs/commu/write/" + commuCateCode);
	} else {
		form.setAttribute('action', "/commu/write/" + commuCateCode);
	}
});

document.addEventListener('DOMContentLoaded', function() {
	const titleInput = document.getElementById('title');
	// 현재 페이지의 경로가 '/cs/commu/write'인지 확인
	if (window.location.pathname === '/cs/commu/write') {
		// 페이지 로드 시 제목 필드에 기본값 설정
		titleInput.value = "[공지]";

		// 입력 필드에서 키를 누를 때마다 이 함수를 호출
		titleInput.addEventListener('input', function() {
			// "[공지]" 텍스트가 제목 시작 부분에 없으면 추가
			if (!this.value.startsWith("[공지]")) {
				this.value = "[공지] " + this.value;
			}
		});
	}
});


