
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
	console.log('validateForm 실행');
	var title = document.getElementById('title').value;
	var category = document.getElementById('category').value;
	var cntnt = document.getElementById('cntnt').value;
	console.log(category, title, cntnt); // 값이 제대로 설정되었는지 확인

	if (!category || category.trim() === "") {
		console.log('Category is empty!'); // 카테고리가 비어있는지 확인
		showModal("K-Spectrum", "카테고리를 선택하세요.");
		return false;
	}

	if (!title || title.trim() === "") {
		console.log('Title is empty!'); // 제목이 비어있는지 확인
		showModal("K-Spectrum", "제목을 입력하세요.");
		return false;
	}

	if (!cntnt || cntnt.trim() === "") {
		console.log('Content is empty!'); // 내용이 비어있는지 확인
		showModal("K-Spectrum", "내용을 입력하세요.");
		return false;
	}
	// 유효성 검사를 통과한 경우 모달을 보여줍니다.
	 showModal('정말로 등록하시겠습니까?', '등록을 원하시면 확인을, 취소하시면 취소를 눌러주세요.', function() {
      document.querySelector('form').submit();
    });
  }

document.querySelector('form').addEventListener('submit', function(event) {
	event.preventDefault(); // 폼의 기본 제출 동작을 막습니다.
	validateForm(); // 여기서 validateForm 함수를 호출합니다.
});


// 취소 버튼 이벤트 리스너
document.querySelector('.cancel-btn').addEventListener('click', function(event) {
    event.preventDefault();
    
    let isFormFilled = false;
    document.querySelectorAll('#category, #title, #cntnt').forEach(element => {
        if (element.value.trim() !== '') isFormFilled = true;
    });

    if (isFormFilled) {
        showModal('취소 확인', '입력한 내용이 모두 사라집니다. 정말로 취소하시겠습니까?', function() {
            // '확인' 버튼이 눌렸을 때 실행될 콜백 함수
            document.querySelector('form').reset();  // 폼을 리셋합니다.
            var commonModal = bootstrap.Modal.getInstance(document.getElementById('commonModal'));
            if (commonModal) commonModal.hide();  // 모달을 숨깁니다.
        });
    } else {
        window.location.href = '/commu';  // 메인 페이지로 바로 이동합니다.
    }
});


function showModal(title, content, callback) {
  document.getElementById('commonModalLabel').textContent = title;
  document.querySelector('.modal-body').textContent = content;
  var commonModal = new bootstrap.Modal(document.getElementById('commonModal'));

  const confirmButton = document.querySelector('.modal-footer .btn-confirm');
  if (confirmButton) {
    confirmButton.onclick = function() {
      if (callback) callback();
      commonModal.hide();
    }
  }

  commonModal.show();
}


document.getElementById('category').addEventListener('change', function() {
	let commuCateCode = this.value;
	let form = document.querySelector('form');
	form.setAttribute('action', "/commu/write/" + commuCateCode);
});

window.addEventListener('DOMContentLoaded', (event) => {
	if (document.getElementById('commuWritePage')) {
		const cancelButton = document.querySelector('.modal-footer .btn-secondary');
		if (cancelButton) cancelButton.style.display = 'none';
	}

	const confirmButton = document.querySelector('.modal-footer .btn-confirm');
	if (confirmButton) {
		confirmButton.addEventListener('click', function() {
			var commonModal = bootstrap.Modal.getInstance(document.getElementById('commonModal'));
			if (commonModal) commonModal.hide();
		});
	}
});


