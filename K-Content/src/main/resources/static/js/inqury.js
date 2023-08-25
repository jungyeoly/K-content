const dragArea = document.querySelector('.drag-area');

// 파일 드래그
dragArea.addEventListener('dragover', (event) => {
	event.preventDefault();
	dragArea.classList.add('active');
});

// 드래그 area 밖
dragArea.addEventListener('dragleave', () => {
	dragArea.classList.remove('active');
});

// 드래그 드롭
dragArea.addEventListener('drop', (event) => {
	event.preventDefault();
});