$(function() {
	const exampleModal = document.getElementById('exampleModal')

	if (exampleModal) {
		exampleModal.addEventListener('show.bs.modal', event => {

			const button = event.relatedTarget;
			const inqryId = button.getAttribute('data-bs-whatever');

			const modalBodyInput = exampleModal.querySelector(".modal-body input[name='inqryId']");
			const modalBodyInput2 = exampleModal.querySelector(".modal-body input[name='inqryPwd']");

			modalBodyInput.value = inqryId;
			modalBodyInput2.value = "";
		})
	}

	$(".inqry-row").click(function(e) {
		const inqryPwd = $(this).attr("data-inqry-pwd");
		var inqryId = $("#exampleModal").find("input[name='inqryId']").val();
		if (inqryPwd == null) {
			window.location.href = "/inqury/detail/" + inqryId;
		}
	})

	$("#userPassword").submit(function(e) {
		e.preventDefault();
		var inqryId = $("#exampleModal").find("input[name='inqryId']").val();
		var enteredPwd = $("#exampleModal").find("input[name='inqryPwd']").val();

		if (enteredPwd === "") {
			Swal.fire({
				title: '비밀번호를 입력해주세요',
				icon: 'warning',

				confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
				confirmButtonText: '확인',

			});
		} else {
			$.ajax({
				url: "/inqury/check-password",
				method: "post",
				data: {
					inqryId: inqryId,
					enteredPwd: enteredPwd
				},
				success: function(data) {
					var inqryId = $("#exampleModal").find("input[name='inqryId']").val();
					window.location.href = "/inqury/detail/" + inqryId;
				},
				error: function() {
					Swal.fire({
						title: '비밀번호가 일치하지 않습니다.',
						icon: 'warning',

						confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
						confirmButtonText: '확인',

					});
				}
			});
		}
	})

	/*$(".submit-btn").click(function(e) {
		e.preventDefault();
		var inqryId = $("#exampleModal").find("input[name='inqryId']").val();
			var enteredPwd = $("#exampleModal").find("input[name='inqryPwd']").val();
		
		if (enteredPwd === "") {
			alert("비밀번호를 입력하세요.");
			return;
		}		
		
		$.ajax({
			url: "/inqury/check-password",
			method: "post",
			data: {
				inqryId: inqryId,
				enteredPwd: enteredPwd
			},
			success: function(data) {
				var inqryId = $("#exampleModal").find("input[name='inqryId']").val();
				window.location.href = "/inqury/detail/" + inqryId;
			},
			error: function() {
				alert("비밀번호가 일치하지 않습니다.");
			}
		});
	});*/

	$(".selpage").click(function() {
		var nowPage = $(this).data("selpage");

		$.ajax({
			url: "/inqury/" + nowPage,
			method: "get",
			success: function(data) {
				let layout = $(".layout");
				layout.find(".container").remove();
				layout.append(data)
			}
		})
	})

	$(".prepage").click(function() {
		var prePage = $(this).data("prepage") - 1;

		$.ajax({
			url: "/inqury/" + prePage,
			method: "get",
			seccess: function(data) {
				let layout = $(".layout");
				layout.find(".container").remove();
				layout.append(data);
			}
		})
	})

	$(".nexpage").click(function() {
		var nexPage = $(this).data("nexpage") + 1;

		$.ajax({
			url: "/inqury/" + prePage,
			method: "get",
			seccess: function(data) {
				let layout = $(".layout");
				layout.find(".container").remove();
				layout.append(data);
			}
		})
	})
});