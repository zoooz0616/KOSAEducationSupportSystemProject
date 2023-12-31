function updateInfo() {
	//비밀번호가 일치하지 않는다면 컷
	if($('.input_password').val()!=$('.input_password_confirm').val()){
		alertFade("비밀번호 확인이 일치하지 않습니다.","F9DCCB","FF333E")
		return;
	}

	//이름이 입력되지 않았다면 컷
	if($('.text_name').val()==""){
		alertFade("이름이 입력되지 않았습니다.","F9DCCB","FF333E")
		return;
	}
	//전화번호가 입력되지 않았다면 컷
	if($('.input_tel').val()==""){
		alertFade("전화번호가 입력되지 않았습니다.","F9DCCB","FF333E")
		return;
	}
	
	$.ajax({
		type: 'post',
		url: '/manager/mypage/update_info',
		data: {
			inputPassword:$('.input_password').val()
			,confirmPassword:$('.input_password_confirm').val()
			,inputTel:$('.input_tel').val()
			,inputName:$('.text_name').val()
		},
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

$(document).ready(
);