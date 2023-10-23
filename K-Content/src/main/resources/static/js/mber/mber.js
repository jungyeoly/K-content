window.addEventListener('load', () => {
	const forms = document.getElementsByClassName('validation-form');

	Array.prototype.filter.call(forms, (form) => {
		form.addEventListener('submit', function(event) {
			if (form.checkValidity() === false) {
				event.preventDefault();
				event.stopPropagation();
			}

			form.classList.add('was-validated');
		}, false);
	});
}, false);


$(document).ready(function() {
	$('.see-pwd').on('click', function() {
		var passwordInput = $('#mber_pwd');
		var eyeIcon = $(this);

		if (passwordInput.attr('type') === 'password') {
			passwordInput.attr('type', 'text');
			eyeIcon.attr('class', 'fa fa-eye-slash');
		} else {
			passwordInput.attr('type', 'password');
			eyeIcon.attr('class', 'fa fa-eye');
		}
	});
});
