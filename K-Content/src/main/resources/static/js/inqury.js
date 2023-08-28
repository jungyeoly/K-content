const dragArea = document.querySelector('.drag-area');

let button = document.querySelector('.button');
let input = document.querySelector("input[type='file']");

let file;

button.onclick = () => {
	input.click();
}

// 버튼으로 
input.addEventListener('change', function() {
	file = this.files[0];
	dragArea.classList.add('active');
	displayFile();
})

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
	
	file = event.dataTransfer.files[0];
	
	displayFile();	
	
});

function displayFile() { 
	let fileType = file.type;
	let validExtension = ['image/jpeg', 'image/jpg', 'image/png'];
	
	if(validExtension.includes(fileType)) {
		let fileReader = new FileReader();
		
		fileReader.onload = () => {
			let fileURL = fileReader.result;
			let imgTag = `<img src="${fileURL}" alt="">`;
			dragArea.innerHTML = imgTag;
		};
		fileReader.readAsDataURL(file);
	} else {
		alert("사진파일이 아닙니다.")
		dragArea.classList.remove('active');
	}
}