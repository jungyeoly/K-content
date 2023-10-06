document.addEventListener('DOMContentLoaded', function() {
    const titleInput = document.getElementById('title');

    // 현재 페이지의 경로가 '/cms/commu/write'인지 확인
    if (window.location.pathname === '/cms/commu/write') {
        // localStorage에서 제목 값을 가져옵니다.
        const savedTitle = localStorage.getItem('titleValue');

        // 저장된 제목 값이 있으면 그 값을 사용하고, 없으면 기본값을 사용합니다.
        titleInput.value = savedTitle ? savedTitle : "[공지사항]";
        
        // 입력 필드에서 키를 누를 때마다 이 함수를 호출
        titleInput.addEventListener('input', function() {
            // "[공지사항]" 텍스트가 제목 시작 부분에 없으면 추가
            if (!this.value.startsWith("[공지사항]")) {
                this.value = "[공지사항] " + this.value;
            }

            // 사용자가 입력한 값을 localStorage에 저장합니다.
            localStorage.setItem('titleValue', this.value);
        });
    }
});

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

    // /cms/commu/write 페이지에서만 [공지사항] 텍스트를 제외하고 제목을 검사
	if (window.location.pathname === '/cms/commu/write') {
		title = title.replace("[공지사항]", "").trim();
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

document.querySelector('form').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작을 막습니다.
    
    // validateForm 함수를 호출하고, 결과가 true이면 폼 제출
    if (validateForm()) {
        this.submit();
    }
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
	document.querySelector('.modal-backdrop').remove();
}



document.getElementById('category').addEventListener('change', function() {
    let commuCateCode = this.value;
    let form = document.querySelector('form');
	  // URL이 '/cms'로 시작하는지 확인
    if (window.location.pathname.startsWith('/cms')) {
        form.setAttribute('action', "/cms/commu/write/" + commuCateCode);
    } else {
        form.setAttribute('action', "/commu/write/" + commuCateCode);
    }
});
document.addEventListener('DOMContentLoaded', function() {
    // 현재 페이지의 경로가 '/cms/commu/write'인지 확인
    if (window.location.pathname === '/cms/commu/write') {
        const titleInput = document.getElementById('title');
        
        // 페이지 로드 시 제목 필드에 기본값 설정
        titleInput.value = "[공지사항]";
        
        // 입력 필드에서 키를 누를 때마다 이 함수를 호출
        titleInput.addEventListener('input', function() {
            // "[공지사항]" 텍스트가 제목 시작 부분에 없으면 추가
            if (!this.value.startsWith("[공지사항]")) {
                this.value = "[공지사항] " + this.value;
            }
        });
    }

});
