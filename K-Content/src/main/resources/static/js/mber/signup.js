
var idRegExp = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d_]{5,12}$/; // 아이디 유효성 검사 정규식
var emailRegExp = /^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;

var idFeedback = document.getElementById("check-id-feedback");
var pwdFeedback = document.getElementById("check-pwd-feedback");
var emailFeedback = document.getElementById("check-email-feedback");
var nameFeedback = document.getElementById("check-name-feedback");
var phoneFeedback = document.getElementById("check-phone-feedback");
var authNumFeedback = document.getElementById("check-authnum-feedback");

var isIdValid = false;
var isPwdValid = false;
var isPwdConfirmValid = false;
var isEmailValid = false;
var isEmailExistCheck = false;
var isAuthNumValid = false;
var isNameValid = false;

var authCodeInput = document.getElementById("auth-code-input");
let serverAuthCode = '';
var emailCheckBtn = document.getElementById("email-check-btn");

var spinner = document.getElementById("email-check-spinner");
var buttonText = document.getElementById("email-check-btn-text");



// ================ 오늘 날짜 ================ //
function getTodayDate() {
	const today = new Date();
	const year = today.getFullYear();
	const month = String(today.getMonth() + 1).padStart(2, '0');
	const day = String(today.getDate()).padStart(2, '0');
	return `${year}-${month}-${day}`;
}

// 오늘 날짜를 가져와서 "min" 속성에 설정
document.getElementById('mber_birth').setAttribute('max', getTodayDate());

// ================ ID 유효성검사 ================ //
function checkId(mberId) {
	idFeedback.style.display = 'block';
	// 중복 검사 요청을 보냄
	$.ajax({
		type: "POST",
		url: "/mber/checkid", // 서버의 아이디 중복 체크 URL로 변경
		data: {
			mberId: mberId
		},
		success: function(result) {
			if (mberId.trim() == '') {
				idFeedback.style.display = 'none';
				idFeedback.innerHTML = "";
				isIdValid = false;
				return;
			} else if (!idRegExp.test(mberId)) {
				idFeedback.style.color = '#dc3545';
				idFeedback.innerHTML = "영문, 숫자, 밑줄(_)로 이루어진 5자 이상 12자 이하의 아이디를 입력하세요.";
				isIdValid = false;
				return;
			}

			if (result == false) {
				idFeedback.style.display = 'none';
				isIdValid = true;
			} else if (result == true) {
				idFeedback.style.color = '#dc3545';
				idFeedback.innerHTML = "이미 사용 중인 아이디입니다.";
				isIdValid = false;
			}
		},
		error: function(request, status) {
			console.log("code:" + request.status + "\n"
				+ "message:" + request.responseText + "\n"
				+ "error:" + error);
		}
	});
}
// ================ 비밀번호 유효성검사 ===============//
function checkPwd(mberPwd) {
	pwdFeedback.style.display = 'block';
	$.ajax({
		type: "POST",
		url: "/mber/checkpwd",
		data: {
			mberPwd: mberPwd
		},
		success: function(result) {
			if (mberPwd.trim() == '') {
				pwdFeedback.style.display = 'none';
				pwdFeedback.innerHTML = "";
				isPwdValid = false;
				return;
			}
			if (result == true) {
				pwdFeedback.style.display = 'none';
				isPwdValid = true;
			} else {
				pwdFeedback.style.color = '#dc3545';
				pwdFeedback.innerHTML = "최소 8자, 최대 16자이어야 하며, 영문, 숫자, 특수문자(!@#$%^&*)를 모두 포함해야 합니다.";
				isPwdValid = false;
			}
		},
		error: function(request, status) {
			console.log("code:" + request.status + "\n"
				+ "message:" + request.responseText + "\n"
				+ "error:" + error);
		}
	});
}

// ================ 이름 유효성검사 ===============//
function checkName(mberName) {
	$.ajax({
		type: "POST",
		url: "/mber/checkname",
		data: { mberName: mberName },
		success: function(result) {
			if (mberName.trim() == '') {
				nameFeedback.style.display = 'none';
				nameFeedback.innerHTML = "";
				isNameValid = false;
				return;
			}
			if (result == true) {
				nameFeedback.style.display = 'none';
				isNameValid = true;
			} else {
				nameFeedback.style.display = 'block';
				nameFeedback.style.color = '#dc3545';
				nameFeedback.innerHTML = "한글 또는 영문으로 된 이름을 입력해주세요.";
				isNameValid = false;
			}
		},
		error: function(request, status) {
			console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
		}
	});

}

// ================ 이메일 유효성검사 ===============//
function checkEmail(mberEmail) {
	emailFeedback.style.display = 'block';
	if (mberEmail.trim() == '') {
		emailFeedback.style.display = 'none';
		emailFeedback.innerHTML = "";
		emailCheckBtn.disabled = true;
		isEmailValid = false;
		return;
	} else if (!emailRegExp.test(mberEmail)) {
		emailFeedback.style.color = '#dc3545';
		emailCheckBtn.disabled = true;
		emailFeedback.innerHTML = "이메일 형식에 맞게 입력해주세요.";
		isEmailValid = false;
		return;
	} else if (emailRegExp.test(mberEmail)) {
		emailFeedback.style.color = '#198754';
		emailCheckBtn.disabled = false;
		emailFeedback.innerHTML = "이메일 인증을 하러 가볼까요?";
		isEmailValid = true;
	}
}

// ================ 비밀번호 보기 ================ //
$(document).ready(function() {
	$('#see-pwd').on('click', function() {
		$('input').toggleClass('active');
		if ($('input').hasClass('active')) {
			$(this).attr('class', "fa fa-eye-slash")
				.prev('input').attr('type', "text");
		} else {
			$(this).attr('class', "fa fa-eye")
				.prev('input').attr('type', 'password');
		}
	});
    $('#see-pwd-confirm').click(function() {
        const passwordConfirmInput = $('#mber_pwd_confirm');
        if (passwordConfirmInput.attr('type') === 'password') {
            passwordConfirmInput.attr('type', 'text');
            $(this).removeClass('fa-eye').addClass('fa-eye-slash');
        } else {
            passwordConfirmInput.attr('type', 'password');
            $(this).removeClass('fa-eye-slash').addClass('fa-eye');
        }
    });
});

// ================ 비밀번호, 비밀번호 확인과 일치 ================ //
function checkPasswordMatch() {

    var password = document.getElementById("mber_pwd").value;
    var confirmPassword = document.getElementById("mber_pwd_confirm").value;
    var feedback = document.getElementById("check-pwd-confirm-feedback");

	feedback.style.display = 'block';

    if (password === confirmPassword) {
        feedback.style.color = '#198754';
		feedback.style.display = 'none';
		isPwdConfirmValid = true;
} else {
        feedback.style.color = '#dc3545';
        feedback.innerHTML = "비밀번호 확인이 일치하지 않습니다.";
		feedback.style.display = 'block';
		isPwdConfirmValid = false;
}
}

// ================ 이메일 중복 검사 후 인증 메일 ================ //
$(document).ready(function() {

	emailCheckBtn.addEventListener('click', function() {
		var mberEmail = document.getElementById('mber_email').value;

		if (emailRegExp.test(mberEmail)) {
			document.getElementById('mber_email').readOnly = true;
			$.ajax({
				type: "POST",
				url: "/mber/checkemail",
				data: { mberEmail: mberEmail },
				success: function(result) {

					if (result == false) {
						emailFeedback.style.color = '#198754';
						emailFeedback.innerHTML = "이메일 인증을 하러 가볼까요?";
						document.getElementById('mber_email').readOnly = true;
						isEmailExistCheck = true;
						sendEmailAuthRequest();
					} else if (result == true) {
						emailFeedback.style.color = '#dc3545';
						emailFeedback.innerHTML = "이미 존재하는 이메일입니다.";
						document.getElementById('mber_email').readOnly = false;
						isEmailExistCheck = false;
					} 
				},
				error: function(request, status) {
					console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
				}
			});
		}

		function sendEmailAuthRequest() {
			// 이메일 유효성 검증 통과했고, 이메일 중복 체크 완료했을 때
			if (isEmailValid && isEmailExistCheck) {
				console.log('이메일:', mberEmail);

				emailFeedback.style.display = 'block';
				emailFeedback.innerHTML = "";
				emailFeedback.readOnly = true;
				spinner.classList.remove('d-none');
				buttonText.innerText = '';

				$.ajax({
					type: 'POST',

					url: '/mber/emailauth',
					data: {
						mberEmail: mberEmail
					},
					success: function(response) {
						spinner.classList.add('d-none');
						buttonText.innerText = '메일 인증';
						serverAuthCode = response.trim();

						emailFeedback.innerText = '인증 메일이 발송되었습니다.';
						console.log('이메일인증번호:', serverAuthCode);
						authCodeInput.disabled = false;
						authCodeInput.style.backgroundColor = 'white';
						emailCheckBtn.blur();
					},
					error: function() {
						emailFeedback.innerText = '이메일 입력 오류';
						emailCheckBtn.blur();
					}
				});
			}
		}
	});

});
// ================ 인증번호 검사 ================ //
function checkAuthNum(enteredAuthNum) {
	authNumFeedback.style.display = 'block';

	if (enteredAuthNum.length === 0) {
		authNumFeedback.innerHTML = "";
		isAuthNumValid = false;
		return;
	} else if (enteredAuthNum.length < 6) {
		authNumFeedback.style.color = '#000000';
		authNumFeedback.innerHTML = "인증번호 확인 중입니다...";
		isAuthNumValid = false;
		return;
	} else if (enteredAuthNum.length === 6) {

		// 서버에 인증번호 확인 요청을 보냅니다.
		$.ajax({
			type: "POST",
			url: "/mber/checkauthnum",
			data: { enteredAuthNum: enteredAuthNum },
			success: function(result) {
				if (result == false) {
					authNumFeedback.style.color = '#dc3545';
					authNumFeedback.innerHTML = "인증 실패";
					isAuthNumValid = false;
				} else if (result == true) {
					authNumFeedback.style.color = '#198754';
					authNumFeedback.innerHTML = "인증 성공";
					isAuthNumValid = true;
					authCodeInput.disabled = true;
				}
			},
			error: function(request, status) {
				console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
			}
		});
	}
}
// ================ 전화번호 변환 ================ //
function oninputPhone(target) {
	target.value = target.value
		.replace(/[^0-9]/g, '')
		.replace(/(^02.{0}|^01.{1}|[0-9]{3,4})([0-9]{3,4})([0-9]{4})/g, "$1-$2-$3");
}

// 유효성 검사
function validateForm() {
	if (isIdValid && isPwdValid && isPwdConfirmValid && isNameValid && isEmailValid && isAuthNumValid) {

		return true;
	} else {
		 Swal.fire({
			                    title: '모든 필수 항목을 올바르게 작성해주세요.',
			                    icon: 'error',
			                    confirmButtonText: '확인',
			                    confirmButtonColor: '#14dbc8'
			                });
		return false;
	}
}

document.getElementById("signup-btn").addEventListener("click", function(event) {


	if (!validateForm()) {
		event.preventDefault();
	}
});
