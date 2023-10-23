document.addEventListener("DOMContentLoaded", function() {
	const findOptionRadios = document
		.querySelectorAll('input[type="radio"][name="findOption"]');
	const findIdForm = document
		.querySelector(".findId");
	const findPwdForm = document
		.querySelector(".findPwd");

	findOptionRadios.forEach(function(radio) {
		radio.addEventListener("change", function() {
			if (this.value === "ID") {
				findIdForm.style.display = "flex";
				findPwdForm.style.display = "none";
			} else if (this.value === "PWD") {
				findIdForm.style.display = "none";
				findPwdForm.style.display = "flex";
			}
		});
	});
});

$(document).ready(function() {
	$("#fId").click(function() {
		var name = $("#nameI").val();
		var phone = $("#phoneI").val();

		$.ajax({
			type: "POST",
			url: "/findUserId",  // 서버의 엔드포인트 URL
			data: { name: name, phone: phone },  // 전송할 데이터
			success: function(data) {
				if (data !== "no") {
					$("#resultId").html("찾은 아이디: " + data);
				} else {
					$("#resultId").html("일치하는 사용자가 없습니다.");
				}
			},
			error: function(error) {
				console.log("에러 발생: " + error);
			}
		});
	});
	
	$("#fPwd").click(function() {
		var name = $("#nameP").val();
		var email = $("#emailP").val();
		var phone = $("#phoneP").val();

		$.ajax({
			type: "POST",
			url: "/findUserPwd",  // 서버의 엔드포인트 URL
			data: { name: name, email: email, phone: phone },  // 전송할 데이터
			success: function(data) {
				if (data !== "no") {
					$("#resultPwd").html("임시 비밀번호를 생성하였습니다. <br>입력하신 이메일을 통해서 확인부탁드립니다.");
				} else {
					$("#resultPwd").html("일치하는 사용자가 없습니다.");
				}
			},
			error: function(error) {
				console.log("에러 발생: " + error);
			}
		});
	});
});
