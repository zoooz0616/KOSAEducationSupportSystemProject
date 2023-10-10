function updateInfo() {
	if($('.input_password').val()!=$('.input_password_confirm').val()){
		alertFade("비밀번호 확인이 일치하지 않습니다.","F9DCCB","FF333E")
		return;
	}
	$.ajax({
		type: 'post',
		url: '/manager/mypage/update_info',
		data: {
			inputPassword:$('.input_password').val()
			,confirmPassword:$('.input_password_confirm').val()
			,inputTel:$('.input_tel').val()
		},
		async: false,
		success: function(response) {
			$('.input_password').val('');
			$('.input_password_confirm').val('');
			alertFade(response.message,"CFDEE6","0E5881");
		}
		, error: function(error) {
			console.log(error);
			alertFade(response.message,"F9DCCB","FF333E")
		}
	})
}

function loadWorklog(){
	console.log(Math.floor(Math.random() * 10));
}

$(document).ready(
	loadWorklog(),
	setInterval(loadWorklog, 3000)
);