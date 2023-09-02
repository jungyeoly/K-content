const mainTab = ducument.getElementById('main');
const commTab = document.getElementById('comm');
const csTab = document.getElemetById('cs');
const categories = documet.querySelectorAll('.categories li');

mainTab.addEventListeer('click', function() {
	categories.style.display = 'block';
});

commTab.addEventListener('click', function() {
	const bestCategory = categories.querySelector('li:first-child');
	if(bestCategory) {
		bestCategory.textCotent = 'ALL';
	}
	
	categories.style.display = 'block';
});

csTab.addEventListener('click', function () {
    categories.style.display = 'none';
});