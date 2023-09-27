
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
var isEmailValid = false;
var isEmailExistCheck = false;
var isNameValid = false;
var isAuthNumValid = false;

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
				idFeedback.style.color = '#198754';
				idFeedback.innerHTML = "사용 가능한 아이디입니다.";
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
				pwdFeedback.style.color = '#198754';
				pwdFeedback.innerHTML = "보안상으로 좋은 비밀번호예요!";
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
				nameFeedback.style.display = 'block';
				nameFeedback.style.color = '#198754';
				nameFeedback.innerHTML = "멋진 이름이네요, 기억할게요!";
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
		isEmailValid = false;
		return;
	} else if (!emailRegExp.test(mberEmail)) {
		emailFeedback.style.color = '#dc3545';
		emailFeedback.innerHTML = "이메일 형식에 맞게 입력해주세요.";
		isEmailValid = false;
		return;
	} else if (emailRegExp.test(mberEmail)) {
		emailFeedback.style.color = '#198754';
		emailFeedback.innerHTML = "이메일 인증을 하러 가볼까요?";
		isEmailValid = true;
	}
}

// ================ 비밀번호 보기 ================ //
$(document).ready(function() {
	$('#see_pwd').on('click', function() {
		$('input').toggleClass('active');
		if ($('input').hasClass('active')) {
			$(this).attr('class', "fa fa-eye-slash")
				.prev('input').attr('type', "text");
		} else {
			$(this).attr('class', "fa fa-eye")
				.prev('input').attr('type', 'password');
		}
	});
});

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
						isEmailExistCheck = true;
						// 이메일 중복 체크가 성공하면 이메일 인증 요청을 보냅니다.
						sendEmailAuthRequest();
					} else if (result == true) {
						emailFeedback.style.color = '#dc3545';
						emailFeedback.innerHTML = "이미 존재하는 이메일입니다.";
						document.getElementById('mber_email').disabled = false;
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
						console.log('수신된 검증 코드:', serverAuthCode);

						emailFeedback.innerText = '인증 메일이 발송되었습니다.';

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
		emailFeedback.innerHTML = "";
		isAuthNumValid = false;
		return;
	} else if (enteredAuthNum.length < 6) {
		emailFeedback.style.color = '#000000';
		emailFeedback.innerHTML = "인증번호 확인 중입니다...";
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
					emailFeedback.style.color = '#dc3545';
					emailFeedback.innerHTML = "인증 실패";
					isAuthNumValid = false;
				} else if (result == true) {
					emailFeedback.style.color = '#198754';
					emailFeedback.innerHTML = "인증 성공";
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

// 유효성 검사 함수
function validateForm() {
	if (isIdValid && isPwdValid && isNameValid && isEmailValid && isAuthNumValid) {
		// 모든 검사가 성공하면 양식 제출 허용
		return true;
	} else {
		// 일부 검사가 실패하면 양식 제출 방지하고 알림 메시지 표시
		alert("모든 필수 항목을 올바르게 작성해주세요.");
		return false;
	}
}

// 제출 버튼에 이벤트 리스너 추가
document.getElementById("signup-btn").addEventListener("click", function(event) {

	console.log(isIdValid);
	console.log(isPwdValid);
	console.log(isNameValid);
	console.log(isEmailValid);
	console.log(isAuthNumValid);
	if (!validateForm()) {
		event.preventDefault(); // 검사 실패 시 양식 제출 방지
	}
});
